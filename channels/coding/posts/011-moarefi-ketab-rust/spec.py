# -*- coding: utf-8 -*-
"""معرفی کتاب — The Rust Programming Language (کتاب رسمی Rust؛ اطلاعات واقعی و بررسی‌شده)"""
import sys, pathlib
for _p in pathlib.Path(__file__).resolve().parents:
    if (_p / 'scripts' / 'poster_lib.py').exists():
        sys.path.insert(0, str(_p / 'scripts')); break
from poster_lib import FB, ACCENT, GREEN, ACCENT2, ORANGE

SPEC = {
    "category_fa": "ترویجی",   # فقط داخلی
    "series_label": "معرفی کتاب",
    "title": "کتاب رسمی زبان Rust",
    "title_size": 56,
    "subtitle": "The Rust Programming Language — همان «The Book» معروف",
    "clean_footer": "معرفی کتاب — The Rust Programming Language",
    "sections": [
        {"type": "bullets", "h": 118, "gap": 16, "desc_size": 26, "items": [
            {"title": [FB("کتاب رسمی پروژهٔ Rust", ACCENT)],
             "desc": "نوشتهٔ کلابنیک و نیکولز؛ مرجع اصلی خودِ تیم Rust"},
            {"title": [FB("از صفر تا پروژهٔ واقعی", GREEN)],
             "desc": "۲۱ فصل، سه پروژهٔ عملی: بازی حدس تا وب‌سرور چندنخه‌ای"},
            {"title": [FB("کاملاً رایگان", ACCENT2)],
             "desc": "نسخهٔ کامل آنلاین رایگان؛ چاپی از No Starch Press"},
            {"title": [FB("نسخهٔ تعاملی", ORANGE)],
             "desc": "با آزمون و تمرین مرحله‌به‌مرحله — لینک در کپشن"},
        ]},
    ],
    "note": "مسیر درس‌های روزانهٔ انجمن با همین کتاب هم‌راستاست",
}
