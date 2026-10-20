# -*- coding: utf-8 -*-
"""جلد شمارهٔ ۱ نشریه — رندر با poster_lib (بدون هیچ نام دسته روی عکس)"""
import sys, pathlib
for _p in pathlib.Path(__file__).resolve().parents:
    if (_p / 'scripts' / 'poster_lib.py').exists():
        sys.path.insert(0, str(_p / 'scripts')); break
from poster_lib import FB, ACCENT, GREEN, ACCENT2, ORANGE

SPEC = {
    "category_fa": "ترویجی",   # فقط داخلی — هرگز روی عکس/متن نمی‌آید
    "series_label": "نشریهٔ الکترونیکی",
    "title": "مجلهٔ انجمن کدنویسی",
    "title_size": 56,
    "subtitle": "شمارهٔ ۱ — مهر ۱۴۰۵",
    "clean_footer": "نشریهٔ الکترونیکی | شمارهٔ ۱",
    "sections": [
        {"type": "bullets", "h": 92, "gap": 13, "desc_size": 26, "items": [
            {"title": [FB("خبرهای انجمن", ACCENT)], "desc": "آغاز سری «Rust از صفر» و کارگاه عملی اعضا"},
            {"title": [FB("معرفی زبان", GREEN)], "desc": "Rust؛ سرعت و امنیت حافظه با هم"},
            {"title": [FB("معرفی نرم‌افزار", ACCENT2)], "desc": "VS Code؛ کارگاه کدنویسی شما"},
            {"title": [FB("ترفند ماه", ORANGE)], "desc": "ساخت و اجرای پروژه‌ها با cargo"},
        ]},
    ],
    "note": "نسخهٔ کامل PDF در پیام بعدی همین کانال",
}
