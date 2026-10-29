# -*- coding: utf-8 -*-
"""ایام فاطمیه — کانال انرژی‌پژوهان (پیام مناسبتی سوگواری، تم عزا)
تاریخ: یکشنبه ۱۴۰۵-۰۸-۰۳ = ۱۴ جمادی‌الاول ۱۴۴۸ (درون دههٔ نخست فاطمیه)
حدیث متفاوت از پست کانال کدنویسی است."""
import sys, pathlib
for _p in pathlib.Path(__file__).resolve().parents:
    if (_p / 'scripts' / 'poster_lib.py').exists():
        sys.path.insert(0, str(_p / 'scripts')); break
from poster_lib import FB, WHITE, GOLD, GOLD_SOFT

SPEC = {
    "theme": "mourning",
    "category_fa": "اطلاعیه",   # فقط داخلی — هرگز روی عکس/متن نمی‌آید
    "series_label": "ایام فاطمیه | ۱۴ جمادی‌الاول ۱۴۴۸",
    "title": "شهادت دخت پیامبر اسلام (س)",
    "title_size": 58,
    "subtitle": "السلام علیک یا فاطمهٔ الزهرا سلام‌الله‌علیها",
    "clean_footer": "ایام فاطمیه تسلیت باد",
    "sections": [
        {"type": "quote", "pad": 40, "lines": [
            {"text": "«فاطمه بضعهٔ من است؛", "size": 44, "color": GOLD, "gap": 14},
            {"text": "آزار او آزار من است", "size": 44, "color": WHITE, "gap": 12},
            {"text": "و آزار من آزار خداست.»", "size": 44, "color": WHITE, "gap": 16},
            {"text": "— پیامبر اسلام (ص)", "size": 26, "family": "fa_bold", "color": GOLD_SOFT},
        ]},
        {"type": "bullets", "h": 118, "gap": 16, "desc_size": 27, "items": [
            {"title": [FB("بانوی علم و عفاف", GOLD)], "desc": "دختر پیامبر (ص)، همسر امام علی (ع) و مادر ائمهٔ طاهرین (ع)"},
            {"title": [FB("مکتب صبر و ایثار", GOLD)], "desc": "علم، عفاف، ایثار و عبادت؛ کوتاه‌ترین عمر، بلندترین مکتب"},
        ]},
    ],
    "note": "به یاد آن بانوی بزرگ، فعالیت علمی انجمن مطابق برنامه آغاز می‌شود",
}
