# -*- coding: utf-8 -*-
"""درس ۴ — آموزش Rust از صفر: کامنت‌ها و پیام‌های کامپایلر"""
import sys, pathlib
for _p in pathlib.Path(__file__).resolve().parents:
    if (_p / 'scripts' / 'poster_lib.py').exists():
        sys.path.insert(0, str(_p / 'scripts')); break
from poster_lib import T, TF, FB, FG, VAR, FN, STR, CMT, NUM, OUT, ACCENT, GREEN, ACCENT2, ORANGE

SPEC = {
    "category_fa": "آموزشی",   # فقط داخلی — هرگز روی عکس/متن نمی‌آید
    "series_label": "آموزش Rust از صفر",
    "lesson_no": 4,
    "title": "کامنت‌ها و پیام‌های کامپایلر",
    "title_size": 50,
    "subtitle": "یادداشت برای آدم‌ها؛ خطاها را برای تو ترجمه می‌کند",
    "clean_footer": "آموزش Rust از صفر",
    "sections": [
        {"type": "code", "name": "src/main.rs", "lines": [
            {"segs": [T("// in avalin comment-e man ast", CMT)]},
            {"segs": [T("let", FN), T(" shahr = ", FG), T('"Tehran"', STR), T(";", FG)]},
            {"segs": [T("println!", FN), T("(", FG), T('"{}"', STR), T(", ", FG), T("shahr", VAR), T(");", FG)]},
            {"sep": "اگر اشتباه کنی چه می‌گوید؟"},
            {"segs": [T("error[E0308]: mismatched types", OUT)]},
            {"segs": [T("  --> src/main.rs:4:9", OUT)]},
            {"segs": [T("  help: expected `i32`, found `&str`", OUT)]},
        ]},
        {"type": "bullets", "h": 86, "gap": 10, "desc_size": 25, "items": [
            {"title": [FB("// یک خطی", ACCENT)], "desc": "تا آخر همان خط؛ پرکاربردترین کامنت"},
            {"title": [FB("/* بلاکی */", GREEN)], "desc": "چند خطی؛ برای توضیح‌های بلندتر"},
            {"title": [FB("/// مستندسازی", ACCENT2)], "desc": "جلد تابع می‌نشیند؛ سند رسمی کد تو"},
        ]},
    ],
    "note": "تمرین: بالای برنامهٔ درس ۳ یک کامنت /// بنویس",
}
