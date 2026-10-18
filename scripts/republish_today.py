#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""بازانتشار پست‌های ۱ و ۲ کانال — طبق بازخورد کاربر:
- پست‌های قبلی (message_id 4 و 5) حذف می‌شوند
- درس ۱ با شماره درس، دستهٔ آموزشی و فاصلهٔ درست برند بازنشر می‌شود
- درس ۲ جدید (متغیرها) جای «شرط if» منتشر می‌شود (ترتیب درست آموزش از صفر)
- برای هر پست: عکس برند + عکس clean + text.md + meta.json
"""
import sys
import json
import shutil
import subprocess
from datetime import datetime
from zoneinfo import ZoneInfo
from pathlib import Path

import requests

sys.path.insert(0, "/home/z/my-project/scripts")
sys.path.insert(0, "/home/z/my-project/anjoman-code-bot/scripts")
from poster_lib import render, spec_text
from spec_lesson01 import SPEC as SPEC_L1

TEHRAN = ZoneInfo("Asia/Tehran")
ROOT = Path("/home/z/my-project")
REPO = ROOT / "anjoman-code-bot"
STAGE = ROOT / "download" / "republish"
QUEUE_L2 = REPO / "content" / "queue" / "lessons" / "python-lesson-02-moteghayer"
POSTS = REPO / "posts"
SEND = "--send" in sys.argv

def load_env():
    env = {}
    for line in (ROOT / ".env").read_text().splitlines():
        if "=" in line and not line.startswith("#"):
            k, v = line.split("=", 1)
            env[k.strip()] = v.strip()
    return env

def bale(env, method, **data):
    r = requests.post(f"https://tapi.bale.ai/bot{env['BALE_TOKEN']}/{method}",
                      data=data, timeout=120)
    return r.json()

def git(*args):
    return subprocess.run(["git", "-C", str(REPO), *args],
                          capture_output=True, text=True, timeout=120)

def send_photo(env, png, caption):
    with open(png, "rb") as f:
        r = requests.post(
            f"https://tapi.bale.ai/bot{env['BALE_TOKEN']}/sendPhoto",
            data={"chat_id": "5100057643", "caption": caption},
            files={"photo": (Path(png).name, f, "image/png")}, timeout=120)
    resp = r.json()
    if not resp.get("ok"):
        raise RuntimeError(f"Bale API: {resp}")
    return resp["result"]["message_id"]

CAPTION_L1 = """🚀 آموزش پایتون از صفر | درس ۱
✨ اولین برنامه‌ات را بنویس!

پایتون یکی از ساده‌ترین و پرکاربردترین زبان‌های برنامه‌نویسی است؛ همان زبانی که در گرایش «برنامه‌نویسی با پایتون» مسابقات کدنویسی جشنواره علمی–پژوهشی هم به کار می‌آید.

در تصویر، اولین برنامه‌ات را می‌بینی:
🟦 متغیر؛ جعبه‌ای برای نگه‌داری داده‌ها
🟦 تابع print؛ نمایش هر چیزی روی صفحه
🟦 رشته (String)؛ متنی که میان گیومه می‌نشیند

🎯 تمرین درس ۱:
با متغیر name اسم خودت را بگذار و برنامه‌ای بنویس که به خودت خوش‌آمد بگو؛ جوابت را همین‌جا بفرست!

📁 دستهٔ محتوا: فعالیت آموزشی (AMOO)
(بر اساس شیوه‌نامه انجمن کدنویسی جشنواره علمی–پژوهشی)

🔗 کانال انجمن کدنویسی:
ble.ir/anjomanfaghihi

#انجمن_کدنویسی #پایتون #آموزش_پایتون #برنامه_نویسی #آموزش_کدنویسی #جشنواره_علمی #پژوهش_سرا"""

def main():
    env = load_env()
    STAGE.mkdir(parents=True, exist_ok=True)
    today = datetime.now(TEHRAN).date().isoformat()

    # ۱) رندر درس ۱ برای بازبینی
    png1 = STAGE / "lesson01-brand.png"
    png1c = STAGE / "lesson01-clean.png"
    render(SPEC_L1, png1, brand=True)
    render(SPEC_L1, png1c, brand=False)
    print("rendered lesson-01:", png1)

    if not SEND:
        print("حالت بازبینی — برای ارسال: --send")
        return

    # ۲) حذف پست‌های قدیمی ۱ و ۲ (و پیام تکراری احتمالی ۶)
    for mid in (4, 5, 6):
        try:
            resp = bale(env, "deleteMessage", chat_id="5100057643", message_id=mid)
            print(f"delete {mid}:", resp.get("ok"), resp.get("description", ""))
        except Exception as e:
            print(f"delete {mid}: error {e}")

    # ۳) ارسال درس ۱
    msg1 = send_photo(env, png1, CAPTION_L1)
    print("درس ۱ منتشر شد — message_id:", msg1)

    # ۴) آرشیو درس ۱
    arch1 = POSTS / "001-python-lesson-01"
    arch1.mkdir(exist_ok=True)
    shutil.copy(png1, arch1 / "post.png")
    shutil.copy(png1c, arch1 / "post-clean.png")
    (arch1 / "text.md").write_text(spec_text(SPEC_L1), encoding="utf-8")
    (arch1 / "caption.txt").write_text(CAPTION_L1, encoding="utf-8")
    old_img = arch1 / "python-post-01.png"
    if old_img.exists():
        old_img.unlink()
    meta1 = {
        "number": 1, "code": "001", "category": "AMOO", "category_fa": "آموزشی",
        "series": "python", "lesson": 1,
        "title": SPEC_L1["title"], "date": today, "message_id": msg1,
        "image": "post.png", "clean_image": "post-clean.png",
        "replaced_message_id": 4,
    }
    (arch1 / "meta.json").write_text(
        json.dumps(meta1, ensure_ascii=False, indent=2), encoding="utf-8")

    # ۵) درس ۲ (متغیرها): قبلاً توسط روزنگار به کانال ارسال شده (message_id=7)؛
    #    فقط آرشیو و شناسنامه — ارسال مجدد نمی‌کنیم (سقف ۲ پست امروز)
    msg2 = 7
    print("درس ۲ از قبل در کانال است — message_id:", msg2)

    # ۶) آرشیو درس ۲ (جایگزین شرط if)
    old2 = POSTS / "002-python-lesson-02"
    if old2.exists():
        shutil.rmtree(old2)
    dest2 = POSTS / "002-python-lesson-02-moteghayer"
    if dest2.exists():
        shutil.rmtree(dest2)
    shutil.copytree(QUEUE_L2, dest2)
    meta2 = {
        "number": 2, "code": "002", "category": "AMOO", "category_fa": "آموزشی",
        "series": "python", "lesson": 2,
        "title": "متغیرها؛ جعبه‌های داده", "date": today, "message_id": msg2,
        "image": "post.png", "clean_image": "post-clean.png",
        "replaced_message_id": 5,
    }
    (dest2 / "meta.json").write_text(
        json.dumps(meta2, ensure_ascii=False, indent=2), encoding="utf-8")
    shutil.rmtree(QUEUE_L2)  # از صف حذف شود

    # ثبت اسلات درس امروز در state روزنگار (تا جبران دوباره نکند)
    state_p = ROOT / ".publish_state.json"
    try:
        st = json.loads(state_p.read_text())
    except Exception:
        st = {}
    st["lesson"] = today
    st["lesson_last"] = "002-python-lesson-02-moteghayer"
    state_p.write_text(json.dumps(st, ensure_ascii=False))

    # ۷) آمار + push
    subprocess.run([sys.executable, str(REPO / "scripts" / "update_stats.py")],
                   check=False)
    git("add", "-A")
    c = git("commit", "-m",
            "بازانتشار درس ۱ و ۲ با ترتیب درست آموزش از صفر: شماره درس روی عکس، "
            "دستهٔ آموزشی اصلاح شد، فاصلهٔ برند درست شد، نسخهٔ clean و متن فارسی هر پست بایگانی شد")
    gh = env.get("GITHUB_TOKEN")
    if gh:
        p = git("push", f"https://ldrcoir:{gh}@github.com/ldrcoir/anjoman-code-channel.git", "main")
        git("remote", "set-url", "origin",
            "https://github.com/ldrcoir/anjoman-code-channel.git")
        print("push:", "OK" if p.returncode == 0 else p.stderr[:300])
    print("DONE — درس ۱:", msg1, "| درس ۲:", msg2)

if __name__ == "__main__":
    main()
