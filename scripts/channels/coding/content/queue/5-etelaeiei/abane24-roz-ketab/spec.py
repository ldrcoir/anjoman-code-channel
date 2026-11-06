# -*- coding: utf-8 -*-
"""۲۴ آبان — روز کتاب و کتاب‌خوانی (پیام مناسبتی)"""
import sys, pathlib
for _p in pathlib.Path(__file__).resolve().parents:
    if (_p / 'scripts' / 'poster_lib.py').exists():
        sys.path.insert(0, str(_p / 'scripts')); break
from poster_lib import FB, ACCENT, GREEN, ACCENT2, ORANGE

SPEC = {
    "category_fa": "اطلاعیه",   # فقط داخلی
    "series_label": "۲۴ آبان",
    "title": "روز کتاب و کتاب‌خوانی",
    "title_size": 54,
    "subtitle": "حتی اگر کتاب، صفحه‌هایش روی مانیتور باشد",
    "clean_footer": "۲۴ آبان — روز کتاب و کتاب‌خوانی",
    "sections": [
        {"type": "bullets", "h": 92, "gap": 13, "desc_size": 26, "items": [
            {"title": [FB("کتاب‌خانهٔ مدرسه", ACCENT)], "desc": "عنوان‌های کدنویسی را بخواه؛ نبود؟ پیشنهاد خرید بده تا تهیه شود"},
            {"title": [FB("منبع رسمی رایگان", GREEN)], "desc": "کتاب رسمی زبان Rust آنلاین و رایگان است — لینک در پست «منابع رسمی» کانال"},
            {"title": [FB("قاعدهٔ یک صفحه", ACCENT2)], "desc": "هر شب فقط یک صفحه دربارهٔ کدنویسی بخوان؛ یک ماه بعد معجزه‌اش را می‌بینی"},
            {"title": [FB("پیشنهاد شما", ORANGE)], "desc": "کتاب خوبی می‌شناسی؟ در کانال معرفی‌اش کن؛ با نام تو منتشر می‌شود"},
        ]},
    ],
    "note": "۲۴ آبان ۱۴۰۵ — روز کتاب و کتاب‌خوانی",
}
