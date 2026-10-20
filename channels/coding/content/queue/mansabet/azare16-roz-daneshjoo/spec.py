# -*- coding: utf-8 -*-
"""۱۶ آذر — روز دانشجو (پیام مناسبتی)"""
import sys, pathlib
for _p in pathlib.Path(__file__).resolve().parents:
    if (_p / 'scripts' / 'poster_lib.py').exists():
        sys.path.insert(0, str(_p / 'scripts')); break
from poster_lib import FB, ACCENT, GREEN, ACCENT2, ORANGE

SPEC = {
    "category_fa": "اطلاعیه",   # فقط داخلی
    "series_label": "۱۶ آذر",
    "title": "روز دانشجو",
    "title_size": 60,
    "subtitle": "از نیمکت مدرسه تا صندلی دانشگاه — یک راه پیوسته",
    "clean_footer": "۱۶ آذر — روز دانشجو",
    "sections": [
        {"type": "bullets", "h": 92, "gap": 13, "desc_size": 26, "items": [
            {"title": [FB("مسیرهای کامپیوتر", ACCENT)], "desc": "مهندسی کامپیوتر، هوش مصنوعی، علوم داده و امنیت — همه با کدنویسی باز می‌شوند"},
            {"title": [FB("رشته‌ای که آینده می‌سازد", GREEN)], "desc": "سلامت، انرژی، حمل‌ونقل و سرگرمی؛ هر صنعتی به برنامه‌نویس نیاز دارد"},
            {"title": [FB("قدم اول، امروز", ACCENT2)], "desc": "درس‌های روزانهٔ Rust ما همان نقطهٔ شروع است — از مهر تا امروز"},
            {"title": [FB("شرط ورود", ORANGE)], "desc": "کنکور ریاضی و تجربی؛ برنامه‌نویسی همین حالا هم برای درس‌هایت مفید است"},
        ]},
    ],
    "note": "۱۶ آذر ۱۴۰۵ — روز دانشجو",
}
