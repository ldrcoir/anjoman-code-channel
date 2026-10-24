# -*- coding: utf-8 -*-
"""آغاز دههٔ نخست ایام فاطمیه — ۱۳ جمادی‌الاول ۱۴۴۸ (پیام مناسبتی سوگواری، تم عزا)"""
import sys, pathlib
for _p in pathlib.Path(__file__).resolve().parents:
    if (_p / 'scripts' / 'poster_lib.py').exists():
        sys.path.insert(0, str(_p / 'scripts')); break
from poster_lib import FB, WHITE, GOLD, GOLD_SOFT

SPEC = {
    "theme": "mourning",
    "category_fa": "اطلاعیه",   # فقط داخلی — هرگز روی عکس/متن نمی‌آید
    "series_label": "دههٔ نخست ایام فاطمیه | ۱۳ جمادی‌الاول ۱۴۴۸",
    "title": "شهادت حضرت فاطمهٔ زهرا (س)",
    "title_size": 60,
    "subtitle": "السلام علیک یا فاطمهٔ الزهرا سلام‌الله‌علیها",
    "clean_footer": "ایام فاطمیه تسلیت باد",
    "sections": [
        {"type": "quote", "pad": 40, "lines": [
            {"text": "«فاطمه پارهٔ تن من است؛", "size": 46, "color": GOLD, "gap": 14},
            {"text": "هر که او را بیازارد، مرا بیازارد.»", "size": 46, "color": WHITE, "gap": 16},
            {"text": "— پیامبر اسلام (ص)", "size": 26, "family": "fa_bold", "color": GOLD_SOFT},
        ]},
        {"type": "bullets", "h": 118, "gap": 16, "desc_size": 27, "items": [
            {"title": [FB("بانوی دو عالم", GOLD)], "desc": "دختر پیامبر (ص)، همسر امام علی (ع) و مادر ائمهٔ طاهرین (ع)"},
            {"title": [FB("الگوی جاودانه", GOLD)], "desc": "علم، عفاف، ایثار و عبادت؛ کوتاه‌ترین عمر، بلندترین مکتب"},
        ]},
    ],
    "note": "به یاد آن بانوی بزرگ، روال آموزشی کانال مطابق برنامه ادامه دارد",
}
