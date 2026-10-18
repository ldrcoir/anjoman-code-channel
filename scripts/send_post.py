#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""ارسال پست به کانال انجمن کدنویسی در بله (Bale Bot API)"""
import os
import sys
import requests
from pathlib import Path

TOKEN = os.environ.get("BALE_TOKEN", "").strip()
if not TOKEN:
    env = Path("/home/z/my-project/.env")
    if env.exists():
        for line in env.read_text().splitlines():
            if line.startswith("BALE_TOKEN="):
                TOKEN = line.split("=", 1)[1].strip()
                break
if not TOKEN:
    sys.exit("BALE_TOKEN not found")

CHAT_ID = "5100057643"  # کانال انجمن کدنویسی - @anjomanfaghihi
BASE = f"https://tapi.bale.ai/bot{TOKEN}"

CAPTION = """🚀 سری آموزش پایتون | قسمت اول
✨ اولین برنامه‌ات را بنویس!

پایتون یکی از ساده‌ترین و پرکاربردترین زبان‌های برنامه‌نویسی است؛ همان زبانی که در گرایش «برنامه‌نویسی با پایتون» مسابقات کدنویسی جشنواره علمی–پژوهشی هم به کار می‌آید.

در تصویر، اولین برنامه‌ات را می‌بینی:
🟦 متغیر؛ جعبه‌ای برای نگه‌داری داده‌ها
🟦 تابع print؛ نمایش هر چیزی روی صفحه
🟦 رشته (String)؛ متنی که میان گیومه می‌نشیند

🎯 تمرین این قسمت:
با متغیر name اسم خودت را بگذار و برنامه‌ای بنویس که به خودت خوش‌آمد بگو؛ جوابت را همین‌جا بفرست!

📁 دسته‌بندی: فعالیت ترویجی و تولید محتوا
(بر اساس شیوه‌نامه انجمن کدنویسی جشنواره علمی)

🔗 کانال انجمن کدنویسی:
ble.ir/anjomanfaghihi

#انجمن_کدنویسی #پایتون #برنامه_نویسی #آموزش_کدنویسی #تفکر_الگوریتمی #تولید_محتوا #فعالیت_ترویجی #جشنواره_علمی #پژوهش_سرا"""

PHOTO = "/home/z/my-project/download/python-post-01.png"

url = f"{BASE}/sendPhoto"
with open(PHOTO, "rb") as f:
    r = requests.post(url, data={"chat_id": CHAT_ID, "caption": CAPTION},
                      files={"photo": ("python-post-01.png", f, "image/png")}, timeout=90)
print("HTTP", r.status_code)
print(r.text[:800])
