# -*- coding: utf-8 -*-
"""درس ۱۱ — آموزش Rust از صفر: توابع (fn)
طبق وعدهٔ کپشن درس ۱۰: «درس بعدی: توابع — کدِ یک‌بارنوشته، بارها استفاده‌شده»"""
import sys, pathlib
for _p in pathlib.Path(__file__).resolve().parents:
    if (_p / 'scripts' / 'poster_lib.py').exists():
        sys.path.insert(0, str(_p / 'scripts')); break
from poster_lib import T, TF, FB, FG, VAR, FN, STR, CMT, NUM, OUT, ACCENT, GREEN, ACCENT2, ORANGE

SPEC = {
    "category_fa": "آموزشی",   # فقط داخلی — هرگز روی عکس/متن نمی‌آید
    "series_label": "آموزش Rust از صفر",
    "lesson_no": 11,
    "title": "توابع",
    "title_size": 60,
    "subtitle": "کدِ یک‌بارنوشته، بارها استفاده‌شده",
    "clean_footer": "آموزش Rust از صفر",
    "sections": [
        {"type": "code", "name": "src/main.rs", "lh": 36, "lines": [
            {"segs": [T("fn", FN), T(" moadele(a: ", FG), T("i32", FN), T(", b: ", FG), T("i32", FN), T(") -> ", FG), T("i32", FN), T(" {", FG)]},
            {"segs": [T("    (a + b) / ", FG), T("2", NUM), T("        ", FG), T("// bazgasht", CMT)]},
            {"segs": [T("}", FG)]},
            {"segs": [T("fn", FN), T(" main() {", FG)]},
            {"segs": [T("    println!", FN), T("(", FG), T("\"{}\"", STR), T(", moadele(", FG), T("18", NUM), T(", ", FG), T("20", NUM), T("));  ", FG), T("// seda 1", CMT)]},
            {"segs": [T("    println!", FN), T("(", FG), T("\"{}\"", STR), T(", moadele(", FG), T("15", NUM), T(", ", FG), T("17", NUM), T("));  ", FG), T("// seda 2", CMT)]},
            {"segs": [T("}", FG)]},
            {"sep": "خروجی"},
            {"segs": [T("19", OUT)]},
            {"segs": [T("16", OUT)]},
        ]},
        {"type": "bullets", "h": 84, "gap": 10, "desc_size": 24, "items": [
            {"title": [FB("fn", ACCENT)], "desc": "کلیدواژهٔ تعریف تابع؛ اسمش را خودت انتخاب می‌کنی"},
            {"title": [T("-> i32", GREEN, 31)], "desc": "نوع مقدارِ برگشتی؛ خطِ آخر بدون سمیکالن برمی‌گردد"},
            {"title": [FB("moadele(18, 20)", ORANGE)], "desc": "صدا زدن تابع: اسم + ورودی‌ها داخل پرانتز"},
        ]},
    ],
    "note": "تمرین: تابعی بنویس که سه نمره بگیرد و معدلشان را برگرداند؛ بعد در main صدایش کن و چاپ کن",
}
