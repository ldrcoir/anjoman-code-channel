# -*- coding: utf-8 -*-
"""درس ۶ — آموزش Rust از صفر: انواع داده (i32 و f64 و bool)
طبق وعدهٔ کپشن درس ۵: «درس بعدی: انواع داده‌ها (i32 و f64)»"""
import sys, pathlib
for _p in pathlib.Path(__file__).resolve().parents:
    if (_p / 'scripts' / 'poster_lib.py').exists():
        sys.path.insert(0, str(_p / 'scripts')); break
from poster_lib import T, TF, FB, FG, VAR, FN, STR, CMT, NUM, OUT, ACCENT, GREEN, ACCENT2, ORANGE

SPEC = {
    "category_fa": "آموزشی",   # فقط داخلی — هرگز روی عکس/متن نمی‌آید
    "series_label": "آموزش Rust از صفر",
    "lesson_no": 6,
    "title": "انواع داده: i32 و f64",
    "title_size": 54,
    "subtitle": "به هر داده یک جعبهٔ اندازه‌درست",
    "clean_footer": "آموزش Rust از صفر",
    "sections": [
        {"type": "code", "name": "src/main.rs", "lh": 46, "lines": [
            {"segs": [T("let", FN), T(" sen: ", FG), T("i32", FN), T(" = ", FG), T("15", NUM), T(";", FG), T("      // sen-e daneshamooz", CMT)]},
            {"segs": [T("let", FN), T(" miangin: ", FG), T("f64", FN), T(" = ", FG), T("18.75", NUM), T(";", FG), T(" // miangin-e nomre", CMT)]},
            {"segs": [T("let", FN), T(" ghabol: ", FG), T("bool", FN), T(" = ", FG), T("true", NUM), T(";", FG), T("   // ghabol shodi?", CMT)]},
            {"sep": "خروجی"},
            {"segs": [T("sen: 15", OUT)]},
            {"segs": [T("miangin: 18.75", OUT)]},
            {"segs": [T("ghabol: true", OUT)]},
        ]},
        {"type": "bullets", "h": 86, "gap": 10, "desc_size": 25, "items": [
            {"title": [FB("i32", ACCENT)], "desc": "عدد صحیح — مثل ۱۵ یا -۴۰؛ پیش‌فرضِ راست"},
            {"title": [FB("f64", GREEN)], "desc": "عدد اعشاری — مثل 18.75؛ دقتِ دوبرابر"},
            {"title": [FB("bool", ACCENT2)], "desc": "فقط true یا false؛ جوابِ بله/نه"},
        ]},
    ],
    "note": "تمرین: سن و معدلت را در دو متغیرِ نوع‌دار نگه دار و با println! چاپشان کن",
}
