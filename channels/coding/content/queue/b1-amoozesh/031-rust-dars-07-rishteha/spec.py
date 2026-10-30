# -*- coding: utf-8 -*-
"""درس ۷ — آموزش Rust از صفر: رشته‌ها (String) و ترکیب متن با عدد
طبق وعدهٔ کپشن درس ۶: «درس بعدی: رشته‌ها (String) و ترکیب متن با عدد»"""
import sys, pathlib
for _p in pathlib.Path(__file__).resolve().parents:
    if (_p / 'scripts' / 'poster_lib.py').exists():
        sys.path.insert(0, str(_p / 'scripts')); break
from poster_lib import T, TF, FB, FG, VAR, FN, STR, CMT, NUM, OUT, ACCENT, GREEN, ACCENT2, ORANGE

SPEC = {
    "category_fa": "آموزشی",   # فقط داخلی — هرگز روی عکس/متن نمی‌آید
    "series_label": "آموزش Rust از صفر",
    "lesson_no": 7,
    "title": "رشته‌ها: متن در Rust",
    "title_size": 54,
    "subtitle": "دوختنِ متن و عدد با format!",
    "clean_footer": "آموزش Rust از صفر",
    "sections": [
        {"type": "code", "name": "src/main.rs", "lh": 46, "lines": [
            {"segs": [T("let", FN), T(" esm = String::from(", FG), T("\"Ali\"", STR), T(");", FG), T("    // reshte", CMT)]},
            {"segs": [T("let", FN), T(" sen = ", FG), T("15", NUM), T(";", FG), T("                     // adad", CMT)]},
            {"segs": [T("let", FN), T(" jomle = ", FG), T("format!", FN), T("(", FG), T("\"{} senash {}\"", STR), T(", esm, sen);", FG)]},
            {"segs": [T("println!", FN), T("(", FG), T("\"{} | tool: {}\"", STR), T(", jomle, esm.len());", FG)]},
            {"sep": "خروجی"},
            {"segs": [T("Ali senash 15 ast | tool: 3", OUT)]},
        ]},
        {"type": "bullets", "h": 86, "gap": 10, "desc_size": 25, "items": [
            {"title": [FB("String", ACCENT)], "desc": "رشتهٔ واقعی و قابل رشد — با String::from ساخته می‌شود"},
            {"title": [FB("format!", GREEN)], "desc": "متن و عدد را با {} به هم می‌دوزد؛ {} جای خالی است"},
            {"title": [FB("len()", ACCENT2)], "desc": "تعداد حرف‌های رشته را می‌شمارد"},
        ]},
    ],
    "note": "تمرین: با format! جمله‌ای بساز که نام و معدل‌ات را با هم نشان دهد",
}
