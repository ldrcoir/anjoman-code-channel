# -*- coding: utf-8 -*-
"""درس ۳ — آموزش Rust از صفر: println! و چاپ متن"""
import sys, pathlib
for _p in pathlib.Path(__file__).resolve().parents:
    if (_p / 'scripts' / 'poster_lib.py').exists():
        sys.path.insert(0, str(_p / 'scripts')); break
from poster_lib import T, TF, FB, FG, VAR, FN, STR, CMT, NUM, OUT, ACCENT, GREEN, ACCENT2, ORANGE

SPEC = {
    "category_fa": "آموزشی",   # فقط داخلی — هرگز روی عکس/متن نمی‌آید
    "series_label": "آموزش Rust از صفر",
    "lesson_no": 3,
    "title": "چاپ متن با println!",
    "title_size": 58,
    "subtitle": "حرف زدن با صفحه؛ با جای‌خالی‌های {}",
    "clean_footer": "آموزش Rust از صفر",
    "sections": [
        {"type": "code", "name": "src/main.rs", "lines": [
            {"segs": [T("println!", FN), T("(", FG), T('"', STR), T("Salam, donya!", STR), T('"', STR), T(");", FG)]},
            {"segs": [T("println!", FN), T("(", FG), T('"', STR), T("javab: {}", STR), T('"', STR), T(", ", FG), T("42", NUM), T(");", FG)]},
            {"segs": [T("println!", FN), T("(", FG), T('"', STR), T("{} + {} = {}", STR), T('"', STR), T(", ", FG), T("2", NUM), T(", ", FG), T("3", NUM), T(", ", FG), T("5", NUM), T(");", FG)]},
            {"sep": "خروجی"},
            {"segs": [T("Salam, donya!", OUT)]},
            {"segs": [T("javab: 42", OUT)]},
            {"segs": [T("2 + 3 = 5", OUT)]},
        ]},
        {"type": "bullets", "h": 86, "gap": 10, "desc_size": 25, "items": [
            {"title": [FB("{}", ACCENT)], "desc": "جای‌خالی؛ هر {} یک مقدار می‌خواهد"},
            {"title": [FB("ترتیب مهم است", GREEN)], "desc": "مقدارها به‌ترتیب در آکولادها می‌نشینند"},
            {"title": [FB("عدد و متن", ACCENT2)], "desc": "println! هر نوع داده‌ای را چاپ می‌کند"},
        ]},
    ],
    "note": "تمرین: خودت را با دو خط println! معرفی کن",
}
