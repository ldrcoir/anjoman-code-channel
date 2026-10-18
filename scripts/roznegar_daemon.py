#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
روزنگار — دیمن زمان‌بند انتشار روزانهٔ انجمن کدنویسی
=====================================================
اسلات‌های انتشار (به‌وقت تهران):
- درس پایتون (آموزشی): هر روز ساعت ۱۰:۰۰ صبح
- پست متنوع (ترویجی/رویداد/معرفی/اطلاعیه): ساعت ۱۹:۰۰ فقط شنبه، سه‌شنبه و پنج‌شنبه
→ هر روز ۱ پست و در آن سه روز ۲ پست (سقف ۳ پست در روز رعایت می‌شود)

- در صورت ری‌استارت محیط، با اجرای مجدد این اسکریپت ادامه می‌دهد (pid-guard دارد).
- اگر سر ساعت امروز گذشته باشد و هنوز منتشر نشده، بلافاصله جبران می‌کند.
- لاگ: logs/roznegar_daemon.log
"""
import os
import sys
import time
import subprocess
from datetime import datetime, timedelta
from zoneinfo import ZoneInfo
from pathlib import Path

TEHRAN = ZoneInfo("Asia/Tehran")
ROOT = Path("/home/z/my-project")
PID = ROOT / ".roznegar.pid"
LOG = ROOT / "logs" / "roznegar_daemon.log"

# درس: هر روز ۱۰:۰۰ | متنوع: شنبه/سه‌شنبه/پنج‌شنبه ۱۹:۰۰
SLOTS = {
    "lesson":  {"hour": 10, "minute": 0,  "days": None},            # هر روز
    "variety": {"hour": 19, "minute": 0,  "days": {5, 1, 3}},       # شنبه/سه‌شنبه/پنج‌شنبه
}


def log(msg):
    line = f"[{datetime.now(TEHRAN).isoformat(timespec='seconds')}] {msg}"
    print(line, flush=True)
    LOG.parent.mkdir(exist_ok=True)
    with open(LOG, "a", encoding="utf-8") as f:
        f.write(line + "\n")


def run_roznegar(kind):
    log(f"اجرای روزنگار ({kind}) …")
    r = subprocess.run(
        [sys.executable, str(ROOT / "anjoman-code-bot" / "scripts" / "roznegar.py"),
         "--kind", kind],
        capture_output=True, text=True, timeout=600)
    log(f"روزنگار ({kind}) finished rc={r.returncode}")
    for line in (r.stdout or "").strip().splitlines():
        log("  | " + line)
    if r.returncode != 0:
        log("  stderr: " + (r.stderr or "")[:400])


def slot_done_today(kind):
    """آیا اسلات امروز از طریق state روزنگار انجام شده؟"""
    state = ROOT / ".publish_state.json"
    if not state.exists():
        return False
    import json
    s = json.loads(state.read_text())
    return s.get(kind) == datetime.now(TEHRAN).date().isoformat()


def next_run_for(kind, slot):
    now = datetime.now(TEHRAN)
    run = now.replace(hour=slot["hour"], minute=slot["minute"],
                      second=0, microsecond=0)
    while True:
        if run > now and not (run.date() == now.date() and slot_done_today(kind)):
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
    log("دیمن روزنگار راه‌اندازی شد — درس ۱۰:۰۰ هر روز | متنوع ۱۹:۰۰ شنبه/سه‌شنبه/پنج‌شنبه")

    try:
        while True:
            now = datetime.now(TEHRAN)
            # جبران دیرهنگام
            for kind, slot in SLOTS.items():
                if slot["days"] is not None and now.weekday() not in slot["days"]:
                    continue
                if now.hour >= slot["hour"] and not slot_done_today(kind):
                    log(f"جبران انتشار {kind} امروز (دیرهنگام)")
                    run_roznegar(kind)

            # نزدیک‌ترین اسلات آینده
            candidates = []
            for kind, slot in SLOTS.items():
                if slot_done_today(kind) and now.hour >= slot["hour"]:
                    continue
                nr = next_run_for(kind, slot)
                candidates.append((nr, kind))
            candidates.sort()
            nr, kind = candidates[0]
            wait = (nr - datetime.now(TEHRAN)).total_seconds()
            log(f"انتشار بعدی: {kind} @ {nr.isoformat()} — انتظار {int(wait/60)} دقیقه")
            while wait > 0:
                time.sleep(min(wait, 1800))
                wait = (nr - datetime.now(TEHRAN)).total_seconds()
                if wait > 3600:
                    break
            # اجرای اسلات
            slot = SLOTS[kind]
            if slot["days"] is not None and datetime.now(TEHRAN).weekday() not in slot["days"]:
                continue
            if not slot_done_today(kind):
                run_roznegar(kind)
    finally:
        PID.unlink(missing_ok=True)


if __name__ == "__main__":
    main()
