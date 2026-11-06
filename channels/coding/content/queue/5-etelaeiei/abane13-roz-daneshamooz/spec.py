# -*- coding: utf-8 -*-
"""۱۳ آبان — روز دانش‌آموز (پیام مناسبتی، بدون نام دسته روی عکس)"""
import sys, pathlib
for _p in pathlib.Path(__file__).resolve().parents:
    if (_p / 'scripts' / 'poster_lib.py').exists():
        sys.path.insert(0, str(_p / 'scripts')); break
from poster_lib import T, FB, ACCENT, GREEN, ACCENT2, VAR, FN, STR, CMT

SPEC = {
    "category_fa": "اطلاعیه",   # فقط داخلی
    "series_label": "۱۳ آبان",
    "title": "روز دانش‌آموز",
    "title_size": 66,
    "subtitle": "تقدیم به دانش‌آموزان کوشای ایران‌زمین",
    "clean_footer": "۱۳ آبان — روز دانش‌آموز",
    "sections": [
        {"type": "bullets", "h": 92, "gap": 13, "desc_size": 26, "items": [
            {"title": [FB("چرا ۱۳ آبان؟", ACCENT)], "desc": "روزی که نام «دانش‌آموز» با ایستادگی گره خورد"},
            {"title": [FB("کدنویسی؛ قلم امروز", GREEN)], "desc": "نوشتن دیگر فقط با خودکار نیست؛ با زبان برنامه‌نویسی هم می‌شود"},
            {"title": [FB("تو هم می‌توانی", ACCENT2)], "desc": "هر برنامه‌نویس بزرگی روزی دانش‌آموز بود و اولین خطش «Salam» بود"},
        ]},
        {"type": "code", "name": "13-aban.rs", "lines": [
            {"segs": [('let ', 'mono', 33, (187, 134, 252), False), ('ayande', 'mono', 33, (122, 162, 247), False), (' = ', 'mono', 33, (220, 225, 235), False), ('"', 'mono', 33, (166, 214, 167), False), ('دست‌های تو', 'fa_med', 33, (166, 214, 167), True), ('";', 'mono', 33, (166, 214, 167), False)]},
            {"segs": [('// ', 'mono', 33, (120, 133, 155), False), ('آینده را همان‌ها می‌سازند که امروز یاد می‌گیرند', 'fa_med', 30, (120, 133, 155), True)]},
        ]},
    ],
    "note": "۱۳ آبان ۱۴۰۵ — روز دانش‌آموز گرامی باد",
}
