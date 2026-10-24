# -*- coding: utf-8 -*-
"""شهادت حضرت فاطمهٔ زهرا (س) — ۳ جمادی‌الثانی ۱۴۴۸ (پیام مناسبتی سوگواری، تم عزا)
گردش ۲۲: هماهنگ با تم سوگواری فاطمیهٔ اول — با حدیثی متفاوت تا دو پست تکراری نباشند."""
import sys, pathlib
for _p in pathlib.Path(__file__).resolve().parents:
    if (_p / 'scripts' / 'poster_lib.py').exists():
        sys.path.insert(0, str(_p / 'scripts')); break
from poster_lib import FB, ACCENT, WHITE, GOLD, GOLD_SOFT

SPEC = {
    "theme": "mourning",
    "category_fa": "اطلاعیه",   # فقط داخلی
    "series_label": "دههٔ دوم ایام فاطمیه | ۳ جمادی‌الثانی ۱۴۴۸",
    "title": "شهادت حضرت فاطمهٔ زهرا (س)",
    "title_size": 52,
    "subtitle": "تسلیت به محضر امام زمان (عج) و همهٔ شیعیان",
    "clean_footer": "شهادت حضرت فاطمهٔ زهرا (س)",
    "sections": [
        {"type": "quote", "pad": 36, "lines": [
            {"text": "«اللّهُمَّ صَلِّ عَلَى فَاطِمَةَ وَأَبِيهَا", "size": 40, "color": GOLD, "gap": 12},
            {"text": "وَبَعْلِهَا وَبَنِيهَا وَمَنْ وَلَاهَا بِالْعِزِّ الَّذِي لَكَ»", "size": 34, "color": WHITE, "gap": 14},
            {"text": "— سلام معروف بر حضرت زهرا (س) در زیارت اهل‌بیت", "size": 24, "family": "fa_bold", "color": GOLD_SOFT},
        ]},
        {"type": "bullets", "h": 96, "gap": 14, "desc_size": 27, "items": [
            {"title": [FB("بانوی بزرگ اسلام", GOLD)], "desc": "دختر پیامبر (ص)؛ الگوی ایثار، علم، عفاف و سخاوت"},
            {"title": [FB("مدرسهٔ زندگی", GOLD)], "desc": "کمتر از ۱۸ سال زیست، اما صد‌ها نسل را معلم شد"},
            {"title": [FB("به یاد او", WHITE)], "desc": "به رسم احترام، امروز کانال انجمن پرشور نیست؛ فقط این یادبود"},
        ]},
    ],
    "note": "التماس دعا — انجمن علمی‌پژوهشی کدنویسی",
}
