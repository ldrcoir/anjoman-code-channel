#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
بیدار-سی‌آی — لایهٔ ۳ بیداری: پشتیبان گیت‌هاب‌اکشنز
====================================================
وقتی سرور ما ریست شود و هیچکس bootstrap نزند، این اسکریپت روی
GitHub Actions اجرا می‌شود و اسلات‌های ازدست‌رفتهٔ امروز را منتشر می‌کند.

منطق (عین منطق دیمون):
- برای هر kind طبق channel.json: روز درست؟ ساعت گذشته؟ امروز انجام نشده؟
- اگر هر سه درست بود → roznegar.py همان کانال را اجرا می‌کند
  (roznegar خودش state را از گیت‌هاب تازه می‌کند، گیت‌های hold/date را
  می‌سنجد، می‌فرستد، آرشیو و commit/push می‌کند)
- چون ران Actions ۱۰ دقیقه «بعد از» ساعت دیمون است، اگر سرور زنده
  باشد state تازه push شده → اینجا رد می‌شود؛ هیچ دوبارسازی نیست.

محیط:
- ANJOMAN_ROOT  ریشه‌ای که .env و logs در آن است (پیش‌فرض /home/z/my-project)
- ANJOMAN_REPO  مسیر مخزن (پیش‌فرض ROOT/anjoman-code-bot)
- ANJOMAN_FONT_DIR  مسیر فونت‌ها برای poster_lib
- BEDAR_DRY=true  فقط شبیه‌سازی (--dry) بدون ارسال
"""
import os
import sys
import json
import subprocess
from datetime import datetime
from zoneinfo import ZoneInfo
from pathlib import Path

ROOT = Path(os.environ.get("ANJOMAN_ROOT", "/home/z/my-project"))
os.makedirs(ROOT, exist_ok=True)   # گارد: اگر ریشه در رانر ساخته نشده باشد، همین‌جا ساخته می‌شود
REPO = Path(os.environ.get("ANJOMAN_REPO", str(ROOT / "anjoman-code-bot")))
DRY = os.environ.get("BEDAR_DRY", "false").strip().lower() in ("1", "true", "yes")

now = datetime.now(ZoneInfo("Asia/Tehran"))
today = now.date().isoformat()
wd = now.weekday()

cfg_path = REPO / "channels" / "coding" / "channel.json"
cfg = json.loads(cfg_path.read_text(encoding="utf-8"))
sched = cfg.get("schedule") or {}

state_path = REPO / "channels" / "coding" / "content" / "state.json"
try:
    state = json.loads(state_path.read_text(encoding="utf-8") or "{}")
except Exception:
    state = {}

lines = [f"بیدار-سی‌آی | تهران {now.isoformat(timespec='seconds')} | "
         f"weekday={wd} | dry={DRY} | ROOT={ROOT} | REPO={REPO}"]
exit_code = 0

for kind, slot in sorted(sched.items()):
    days = slot.get("days")
    if days is not None and wd not in days:
        lines.append(f"— {kind}: امروز روز انتشارش نیست — رد شد")
        continue
    if (now.hour, now.minute) < (int(slot["hour"]), int(slot["minute"])):
        lines.append(f"— {kind}: هنوز ساعتش نرسیده ({slot['hour']:02d}:{slot['minute']:02d}) — رد شد")
        continue
    if state.get(kind) == today:
        lines.append(f"— {kind}: امروز انجام شده — رد شد")
        continue
    cmd = [sys.executable, str(REPO / "scripts" / "roznegar.py"),
           "--channel", cfg.get("slug", "coding"), "--kind", kind]
    if DRY:
        cmd.append("--dry")
    lines.append(f"→ {kind}: اجرای roznegar …")
    try:
        # env فرزند صریح است: هر سه مسیر + توکن‌ها، تا هیچ‌وقت گم نشوند
        child_env = dict(os.environ)
        child_env["ANJOMAN_ROOT"] = str(ROOT)
        child_env["ANJOMAN_REPO"] = str(REPO)
        child_env.setdefault("BALE_TOKEN", os.environ.get("BALE_TOKEN", ""))
        child_env.setdefault("GITHUB_TOKEN", os.environ.get("GITHUB_TOKEN", ""))
        r = subprocess.run(cmd, capture_output=True, text=True, timeout=900,
                           env=child_env)
        tail = "\n".join((r.stdout or "").strip().splitlines()[-8:])
        lines.append("  " + tail.replace("\n", "\n  "))
        if r.returncode != 0:
            lines.append("  stderr کامل: " + (r.stderr or "")[:1500])
            exit_code = 1
    except Exception as e:
        import traceback
        lines.append(f"  ERROR: {e}\n  " + traceback.format_exc()[-800:])
        exit_code = 1

report = "\n".join(lines)
print(report, flush=True)

# خلاصهٔ انسانی در GitHub Step Summary
ss = os.environ.get("GITHUB_STEP_SUMMARY")
if ss:
    with open(ss, "a", encoding="utf-8") as f:
        f.write("## گزارش بیدار\n\n```\n" + report + "\n```\n")

sys.exit(exit_code)
