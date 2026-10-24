# -*- coding: utf-8 -*-
"""جلد شمارهٔ ۲ نشریه — رندر با poster_lib (بدون هیچ نام دسته روی عکس)"""
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
    "subtitle": "شمارهٔ ۲ — آبان ۱۴۰۵",
    "clean_footer": "نشریهٔ الکترونیکی | شمارهٔ ۲",
    "sections": [
        {"type": "bullets", "h": 92, "gap": 13, "desc_size": 26, "items": [
            {"title": [FB("اخبار هوش مصنوعی", ACCENT)], "desc": "گزارش استنفورد؛ همکارهای دیجیتال؛ موج متن‌باز"},
            {"title": [FB("اخبار فناوری و کدنویسی", GREEN)], "desc": "Rust نسخهٔ ۱.۹۹؛ هشدار امنیتی؛ GitHub Universe"},
            {"title": [FB("معرفی نرم‌افزار", ACCENT2)], "desc": "Git؛ زمان‌سفری که پروژه‌ات را امن می‌کند"},
            {"title": [FB("معرفی زبان", ORANGE)], "desc": "C؛ ریشه‌ای که ویندوز و لینوکس روی آن ساخته شد"},
        ]},
    ],
    "note": "نسخهٔ کامل PDF در پیام بعدی همین کانال",
}
