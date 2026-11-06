# -*- coding: utf-8 -*-
"""چالش کدنویسی شمارهٔ ۱ — «خروجی این برنامه چیست؟»
کد عمداً روی ترتیب عملگرها (اول ضرب، بعد جمع) بازی می‌کند؛
سطح چالش هم‌تراز درس ۵ (متغیرها) است تا همهٔ اعضا بتوانند شرکت کنند."""
import sys, pathlib
for _p in pathlib.Path(__file__).resolve().parents:
    if (_p / 'scripts' / 'poster_lib.py').exists():
        sys.path.insert(0, str(_p / 'scripts')); break
from poster_lib import T, TF, FB, FG, VAR, FN, STR, CMT, NUM, OUT, ACCENT, GREEN, ACCENT2, ORANGE

SPEC = {
    "category_fa": "رویداد",   # فقط داخلی — هرگز روی عکس/متن نمی‌آید
    "series_label": "چالش کدنویسی",
    "title": "خروجی این برنامه چیست؟",
    "title_size": 50,
    "subtitle": "چالش شمارهٔ ۱ — اول حدس بزن، بعد جوابت را بفرست!",
    "clean_footer": "چالش کدنویسی",
    "sections": [
        {"type": "code", "name": "src/main.rs", "lh": 46, "lines": [
            {"segs": [T("fn", FN), T(" main() {", FG)]},
            {"segs": [T("    let", FN), T(" x = ", FG), T("3", NUM), T(";", FG)]},
            {"segs": [T("    let", FN), T(" y = x + ", FG), T("2", NUM), T(" * ", FG), T("3", NUM), T(";", FG)]},
            {"segs": [T("    println!", FN), T("(", FG), T("\"y = {}\"", STR), T(", y);", FG)]},
            {"segs": [T("}", FG)]},
            {"sep": "کدام درست است؟"},
            {"segs": [T("y = 11", OUT), T("  ؟", FG)]},
            {"segs": [T("y = 9", OUT), T("   ؟", FG)]},
        ]},
        {"type": "bullets", "h": 86, "gap": 10, "desc_size": 25, "items": [
            {"title": [FB("حدس بزن", ACCENT)], "desc": "قبل از هر چیز جوابت را یادداشت کن"},
            {"title": [FB("راهنما", GREEN)], "desc": "ترتیب عملگرها مثل ریاضی: اول * بعد +"},
            {"title": [FB("جوابت را بفرست", ACCENT2)], "desc": "پیام به کانال — اعلام پاسخ در چالش بعدی"},
        ]},
    ],
    "note": "برای مرور: درس ۵ — متغیرها با let و let mut",
}
