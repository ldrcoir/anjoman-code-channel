# -*- coding: utf-8 -*-
"""نیروگاه خورشیدی متانول کاوه — بند پ (معرفی نیروگاه‌های انرژی نوین ایران)
صحت علمی طبق بند ۳ شیوه‌نامه: تاریخ + منبع؛ اعداد از گزارش‌های رسمی افتتاح و ساتبا.
رندر با موتور مستقل انرژی (enerjy_poster) — بلوک آماری + آیکون‌های برداری."""
import sys, pathlib
for _p in pathlib.Path(__file__).resolve().parents:
    if (_p / 'scripts' / 'enerjy_poster.py').exists():
        sys.path.insert(0, str(_p / 'scripts')); break
from poster_lib import FB
from enerjy_poster import INK, GRN, AMBER_D, SKY, ORG

SPEC = {
    "category_fa": "معرفی",   # فقط داخلی — هرگز روی عکس/متن نمی‌آید
    "series_label": "نیروگاه‌های ایران | آبان ۱۴۰۵",
    "title": "نیروگاه خورشیدی متانول کاوه",
    "title_size": 50,
    "subtitle": "بزرگ‌ترین نیروگاه خورشیدی تک‌خاستگاه ایران — ساوه",
    "clean_footer": "معرفی نیروگاه خورشیدی",
    "sections": [
        {"type": "stat", "value": "۱۳۰", "unit": "مگاوات",
         "label": "ظرفیت نیروگاه — افتتاح ۱ خرداد ۱۴۰۵", "size": 76},
        {"type": "bullets", "h": 100, "gap": 12, "desc_size": 25, "items": [
            {"icon": "sun", "title": [FB("فتوولتائیک یعنی چه؟", GRN)], "desc": "تبدیل مستقیم نور خورشید به برق — بدون توربین"},
            {"icon": "bolt", "title": [FB("رکورددار تازهٔ ایران", SKY)], "desc": "رکورد قبلی: فولاد مبارکه با ۱۲۰ مگاوات (تیر ۱۴۰۴)"},
            {"icon": "leaf", "title": [FB("چرا مهم است؟", AMBER_D)], "desc": "برق پاک یعنی گازِ ناشده و هوای پاک‌تر برای شهرها"},
            {"icon": "wind", "title": [FB("جهش تجدیدپذیرها", ORG)], "desc": "از ۱۲۵۰ به ۵۰۰۰+ مگاوات در دو سال — رتبهٔ ۳ آسیا"},
        ]},
    ],
    "note": "منبع: گزارش‌های رسمی افتتاح و اعلام ساتبا (مرداد ۱۴۰۵) — بازبینی آبان ۱۴۰۵",
}
