# -*- coding: utf-8 -*-
"""شهادت حضرت فاطمهٔ زهرا (س) — ۳ جمادی‌الثانی ۱۴۴۸ (پیام مناسبتی)"""
import sys, pathlib
for _p in pathlib.Path(__file__).resolve().parents:
    if (_p / 'scripts' / 'poster_lib.py').exists():
        sys.path.insert(0, str(_p / 'scripts')); break
from poster_lib import FB, ACCENT, WHITE

SPEC = {
    "category_fa": "اطلاعیه",   # فقط داخلی
    "series_label": "۳ جمادی‌الثانی ۱۴۴۸",
    "title": "شهادت حضرت فاطمهٔ زهرا (س)",
    "title_size": 52,
    "subtitle": "تسلیت به محضر امام زمان (عج) و همهٔ شیعیان",
    "clean_footer": "شهادت حضرت فاطمهٔ زهرا (س)",
    "sections": [
        {"type": "bullets", "h": 96, "gap": 14, "desc_size": 27, "items": [
            {"title": [FB("بانوی بزرگ اسلام", ACCENT)], "desc": "دختر پیامبر (ص)؛ الگوی ایثار، علم، عفاف و سخاوت"},
            {"title": [FB("مدرسهٔ زندگی", ACCENT)], "desc": "کمتر از ۱۸ سال زیست، اما صد‌ها نسل را معلم شد"},
            {"title": [FB("به یاد او", WHITE)], "desc": "به رسم احترام، امروز کانال انجمن پرشور نیست؛ فقط این یادبود"},
        ]},
    ],
    "note": "التماس دعا — انجمن علمی‌پژوهشی کدنویسی",
}
