#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
روزنگار — دیمن زمان‌بند انتشار روزانهٔ همهٔ کانال‌ها (نسخه ۴ — چندانجمنی)
==========================================================================
هر کانال اسلات‌های انتشار خودش را از channels/<slug>/channel.json می‌خواند:
- «lesson»: پیش‌فرض هر روز ۱۰:۰۰ صبح (پست اصلی/آموزشی)
- «variety»: پیش‌فرض ۱۹:۰۰ شنبه، دوشنبه و پنج‌شنبه (پست متنوع)
- «occasion»: ۹:۳۰ هر روز چک می‌شود؛ فقط اگر date.txt صف برابر امروز باشد منتشر می‌کند
→ هر روز ۱ پست و در آن سه روز ۲ پست (سقف ۳ پست در روز رعایت می‌شود)

چندانجمنی:
- دیمن همهٔ channels/*/channel.json (به‌جز _TEMPLATE) را کشف می‌کند
- state هر کانال جداگانه است (channels/<slug>/content/state.json) — هیچ قاطی‌شدنی نیست
- افزودن کانال جدید = افزودن پوشهٔ جدید؛ دیمن خودش وارد چرخه می‌شود (بدون ری‌استارت)

مقاوم در برابر ریست سرور:
- در صورت ری‌استارت، با اجرای مجدد همین اسکریپت (یا bootstrap.sh) ادامه می‌دهد
- اگر سر ساعت امروز گذشته باشد و هنوز منتشر نشده، بلافاصله جبران می‌کند
- لاگ: logs/roznegar_daemon.log
"""
import os
import sys
import json
import time
import subprocess
from datetime import datetime, timedelta
from zoneinfo import ZoneInfo
from pathlib import Path

TEHRAN = ZoneInfo("Asia/Tehran")
ROOT = Path("/home/z/my-project")
REPO = ROOT / "anjoman-code-bot"
PID = ROOT / ".roznegar.pid"
LOG = ROOT / "logs" / "roznegar_daemon.log"

# weekday پایتون: شنبه=۵، یکشنبه=۶، دوشنبه=۰، … پنج‌شنبه=۳
DEFAULT_SLOTS = {
    "lesson":  {"hour": 10, "minute": 0, "days": None},
    "variety": {"hour": 19, "minute": 0, "days": [5, 1, 3]},
}


def log(msg):
    line = f"[{datetime.now(TEHRAN).isoformat(timespec='seconds')}] {msg}"
    print(line, flush=True)
    LOG.parent.mkdir(exist_ok=True)
    with open(LOG, "a", encoding="utf-8") as f:
        f.write(line + "\n")


def discover_channels():
    """همهٔ کانال‌های تعریف‌شده را برمی‌گرداند: [(slug, slots, state_path)]"""
    out = []
    base = REPO / "channels"
    if not base.exists():
        return out
    for d in sorted(base.iterdir()):
        cj = d / "channel.json"
        if not d.is_dir() or d.name.startswith("_") or not cj.exists():
            continue
        try:
            cfg = json.loads(cj.read_text(encoding="utf-8"))
        except Exception as e:
            log(f"channel.json نامعتبر برای {d.name}: {e}")
            continue
        raw = cfg.get("schedule") or {}
        slots = {}
        for kind, s in raw.items():
            days = s.get("days")
            slots[kind] = {
                "hour": int(s.get("hour", 10)),
                "minute": int(s.get("minute", 0)),
                "days": set(days) if days is not None else None,
            }
        if not slots:
            slots = DEFAULT_SLOTS
        out.append((cfg.get("slug", d.name), slots,
                    d / "content" / "state.json"))
    return out


def run_roznegar(slug, kind):
    log(f"اجرای روزنگار [{slug}] ({kind}) …")
    r = subprocess.run(
        [sys.executable, str(REPO / "scripts" / "roznegar.py"),
         "--channel", slug, "--kind", kind],
        capture_output=True, text=True, timeout=600)
    log(f"روزنگار [{slug}] ({kind}) finished rc={r.returncode}")
    for line in (r.stdout or "").strip().splitlines():
        log("  | " + line)
    if r.returncode != 0:
        log("  stderr: " + (r.stderr or "")[:400])


def slot_done_today(state_path, kind):
    """آیا اسلات امروزِ این کانال انجام شده؟ (state داخل مخزن گیت — مقاوم به ریست)"""
    if not state_path.exists():
        return False
    try:
        s = json.loads(state_path.read_text(encoding="utf-8"))
        return s.get(kind) == datetime.now(TEHRAN).date().isoformat()
    except Exception:
        return False


def next_run_for(slots, kind, state_path):
    slot = slots[kind]
    now = datetime.now(TEHRAN)
    run = now.replace(hour=slot["hour"], minute=slot["minute"],
                      second=0, microsecond=0)
    while True:
        if run > now and not (run.date() == now.date() and slot_done_today(state_path, kind)):
            if slot["days"] is None or run.weekday() in slot["days"]:
                return run
        run = (run + timedelta(days=1)).replace(
            hour=slot["hour"], minute=slot["minute"], second=0, microsecond=0)


def main():
    if PID.exists():
        old = PID.read_text().strip()
        if old and os.path.exists(f"/proc/{old}"):
            log(f"دیمن قبلی روزنگار با pid={old} در حال اجراست — خروج")
            return
    PID.write_text(str(os.getpid()))
    log("دیمن روزنگار v4 (چندانجمنی) راه‌اندازی شد")

    try:
        while True:
            channels = discover_channels()
            if not channels:
                log("هیچ کانالی تعریف نشده — ۱۰ دقیقه بعد دوباره بررسی می‌شود")
                time.sleep(600)
                continue
            now = datetime.now(TEHRAN)

            # جبران دیرهنگام برای همهٔ کانال‌ها و همهٔ اسلات‌ها
            for slug, slots, state_path in channels:
                for kind, slot in slots.items():
                    if slot["days"] is not None and now.weekday() not in slot["days"]:
                        continue
                    if (now.hour, now.minute) >= (slot["hour"], slot["minute"]) \
                            and not slot_done_today(state_path, kind):
                        log(f"[{slug}] جبران انتشار {kind} امروز (دیرهنگام)")
                        run_roznegar(slug, kind)
                        now = datetime.now(TEHRAN)

            # نزدیک‌ترین اسلات آینده بین همهٔ کانال‌ها
            candidates = []
            for slug, slots, state_path in channels:
                for kind in slots:
                    candidates.append(
                        (next_run_for(slots, kind, state_path), slug, kind))
            candidates.sort()
            nr, slug, kind = candidates[0]
            wait = (nr - datetime.now(TEHRAN)).total_seconds()
            log(f"انتشار بعدی: [{slug}] {kind} @ {nr.isoformat()} — "
                f"انتظار {int(wait/60)} دقیقه ({len(channels)} کانال فعال)")
            while wait > 0:
                time.sleep(min(wait, 1800))
                wait = (nr - datetime.now(TEHRAN)).total_seconds()
                if wait > 3600:
                    break

            # اجرای اسلات (با بررسی مجدد روزِ هفته بعد از خواب)
            slot = dict(DEFAULT_SLOTS.get(kind, {}))
            for s, slots, _ in channels:
                if s == slug:
                    slot = slots[kind]
                    break
            if slot.get("days") is not None and \
                    datetime.now(TEHRAN).weekday() not in slot["days"]:
                continue
            for s, _, state_path in channels:
                if s == slug and not slot_done_today(state_path, kind):
                    run_roznegar(slug, kind)
                    break
    finally:
        PID.unlink(missing_ok=True)


if __name__ == "__main__":
    if "--detach" in sys.argv:
        # خوددیمونیزه‌شدن با double-fork تا پردازه مستقل از session بماند (ppid=1)
        import signal
        pid = os.fork()
        if pid > 0:
            os.waitpid(pid, 0)
            sys.exit(0)
        os.setsid()
        signal.signal(signal.SIGHUP, signal.SIG_IGN)
        pid2 = os.fork()
        if pid2 > 0:
            os._exit(0)
        sys.stdout = open(LOG, "a", buffering=1)
        sys.stderr = sys.stdout
    main()
