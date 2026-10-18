#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
scheduler_daemon.py — دیمن زمان‌بند روزانه کانال انجمن کدنویسی
هر روز ساعت ۱۹:۰۰ به‌وقت تهران (و فقط یک‌بار در شبانه‌روز) auto_publish.py را اجرا می‌کند.
- در صورت ری‌استارت محیط، با اجرای مجدد این اسکریپت، ادامه می‌دهد (pid-guard دارد).
- اگر سر ساعت ۱۹ امروز گذشته باشد و هنوز پست امروز منتشر نشده، بلافاصله جبران می‌کند.
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
PID = ROOT / ".scheduler.pid"
STATE = ROOT / ".publish_state.json"
LOG = ROOT / "logs" / "scheduler.log"
PUBLISH_HOUR = 19
PUBLISH_MIN = 0


def log(msg):
    line = f"[{datetime.now(TEHRAN).isoformat(timespec='seconds')}] {msg}"
    print(line, flush=True)
    LOG.parent.mkdir(exist_ok=True)
    with open(LOG, "a", encoding="utf-8") as f:
        f.write(line + "\n")


def published_today():
    if STATE.exists():
        s = json.loads(STATE.read_text())
        return s.get("last_date") == datetime.now(TEHRAN).date().isoformat()
    return False


def next_run():
    now = datetime.now(TEHRAN)
    run = now.replace(hour=PUBLISH_HOUR, minute=PUBLISH_MIN, second=0, microsecond=0)
    if run <= now or published_today():
        run = (now + timedelta(days=1)).replace(
            hour=PUBLISH_HOUR, minute=PUBLISH_MIN, second=0, microsecond=0)
    return run


def run_publish():
    log("اجرای auto_publish …")
    r = subprocess.run([sys.executable, str(ROOT / "scripts" / "auto_publish.py")],
                       capture_output=True, text=True, timeout=600)
    log(f"auto_publish finished rc={r.returncode}")
    for line in (r.stdout or "").strip().splitlines():
        log("  | " + line)
    if r.returncode != 0:
        log("  stderr: " + (r.stderr or "")[:400])


def main():
    if PID.exists():
        old = PID.read_text().strip()
        if old and os.path.exists(f"/proc/{old}"):
            log(f"دیمن قبلی با pid={old} در حال اجراست — خروج")
            return
    PID.write_text(str(os.getpid()))
    log("دیمن زمان‌بند راه‌اندازی شد (ساعت انتشار: ۱۹:۰۰ تهران)")

    try:
        while True:
            if datetime.now(TEHRAN).hour >= PUBLISH_HOUR and not published_today():
                log("جبران انتشار امروز (دیرهنگام)")
                run_publish()
            nr = next_run()
            wait = (nr - datetime.now(TEHRAN)).total_seconds()
            log(f"انتشار بعدی: {nr.isoformat()} — انتقال {int(wait/60)} دقیقه‌ای")
            # خواب قطعه‌قطعه تا در صورت اصلاح ساعت سیستم، منعطف بماند
            while wait > 0:
                time.sleep(min(wait, 1800))
                wait = (nr - datetime.now(TEHRAN)).total_seconds()
                if wait > 3600:  # اگر ساعت جلو/عقب شد، محاسبه مجدد
                    break
            if published_today():
                continue
            run_publish()
    finally:
        PID.unlink(missing_ok=True)


if __name__ == "__main__":
    main()
