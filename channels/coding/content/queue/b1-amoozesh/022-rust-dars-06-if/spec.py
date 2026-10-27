# -*- coding: utf-8 -*-
"""درس ۶ — آموزش Rust از صفر: شرط if و تصمیم‌گیری"""
import sys, pathlib
for _p in pathlib.Path(__file__).resolve().parents:
    if (_p / 'scripts' / 'poster_lib.py').exists():
        sys.path.insert(0, str(_p / 'scripts')); break
from poster_lib import T, TF, FB, FG, VAR, FN, STR, CMT, NUM, OUT, ACCENT, GREEN, ACCENT2, ORANGE

SPEC = {
    "category_fa": "آموزشی",   # فقط داخلی — هرگز روی عکس/متن نمی‌آید
    "series_label": "آموزش Rust از صفر",
    "lesson_no": 6,
    "title": "تصمیم‌گیری: شرط if",
    "title_size": 54,
    "subtitle": "کدِ تو هم مثل تو تصمیم می‌گیرد",
    "clean_footer": "آموزش Rust از صفر",
    "sections": [
        {"type": "code", "name": "src/main.rs", "lh": 46, "lines": [
            {"segs": [T("let", FN), T(" emtiaz = ", FG), T("17", NUM), T(";", FG), T("  // emtiaz-e bazi", CMT)]},
            {"segs": [T("if", FN), T(" emtiaz >= ", FG), T("20", NUM), T(" {", FG)]},
            {"segs": [T("    println!", FN), T("(", FG), T("\"Barande shodi!\"", STR), T(");", FG)]},
            {"segs": [T("} else if", FN), T(" emtiaz >= ", FG), T("10", NUM), T(" {", FG)]},
            {"segs": [T("    println!", FN), T("(", FG), T("\"Edame bede!\"", STR), T(");", FG)]},
            {"segs": [T("} else", FN), T(" {", FG)]},
            {"segs": [T("    println!", FN), T("(", FG), T("\"Dobara talash kon\"", STR), T(");", FG)]},
            {"segs": [T("}", FG)]},
        ]},
        {"type": "bullets", "h": 86, "gap": 10, "desc_size": 25, "items": [
            {"title": [FB("شرط = true/false", ACCENT)], "desc": "پرانتز لازم نیست؛ آکولاد اجباری است"},
            {"title": [FB("else if و else", GREEN)], "desc": "زنجیرهٔ شرایط از بالا به پایین چک می‌شود"},
            {"title": [FB("if یک عبارت است", ACCENT2)], "desc": "در Rust می‌شود نتیجهٔ if را در let ریخت!"},
        ]},
    ],
    "note": "تمرین: در بازی رفیق مار، اگر امتیاز از رکورد بیشتر شد پیام «Rekord jadid!» چاپ کن",
}
