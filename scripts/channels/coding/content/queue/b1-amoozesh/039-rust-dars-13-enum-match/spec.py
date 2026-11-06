# -*- coding: utf-8 -*-
"""درس ۱۳ — آموزش Rust از صفر: شمارشی‌ها (enum) و تطبیق الگو با match
گردش ۶۷ — «درس ۱۳ و ۱۴ را آماده کن ببینم» — فقط پیش‌نویس، بدون انتشار"""
import sys, pathlib
for _p in pathlib.Path(__file__).resolve().parents:
    if (_p / 'scripts' / 'poster_lib.py').exists():
        sys.path.insert(0, str(_p / 'scripts')); break
from poster_lib import T, TF, FB, FG, VAR, FN, STR, CMT, NUM, OUT, ACCENT, GREEN, ACCENT2, ORANGE

SPEC = {
    "category_fa": "آموزشی",   # فقط داخلی — هرگز روی عکس/متن نمی‌آید
    "series_label": "آموزش Rust از صفر",
    "lesson_no": 13,
    "title": "شمارشی‌ها و match",
    "title_size": 52,
    "subtitle": "وقتی مقدار فقط یکی از چند حالت می‌تواند باشد",
    "clean_footer": "آموزش Rust از صفر",
    "sections": [
        {"type": "code", "name": "src/main.rs", "lh": 33, "lines": [
            {"segs": [T("enum", FN), T(" Cheragh { Roshan, Khamoosh }", FG)]},
            {"segs": [T("", FG)]},
            {"segs": [T("fn", FN), T(" main() {", FG)]},
            {"segs": [T("    let", FN), T(" cheragh = Cheragh::Roshan;", FG)]},
            {"segs": [T("", FG)]},
            {"segs": [T("    match", FN), T(" cheragh {", FG)]},
            {"segs": [T("        Cheragh::Roshan => println!(", FG), T("\"roshan ast!\"", STR), T("),", FG)]},
            {"segs": [T("        Cheragh::Khamoosh => println!(", FG), T("\"khamoosh.\"", STR), T("),", FG)]},
            {"segs": [T("    }", FG)]},
            {"segs": [T("}", FG)]},
            {"sep": "خروجی"},
            {"segs": [T("roshan ast!", OUT)]},
        ]},
        {"type": "bullets", "h": 80, "gap": 10, "desc_size": 23, "items": [
            {"title": [FB("enum", ACCENT)], "desc": "فهرست حالت‌های ممکنِ یک مقدار — مثل روشن/خاموش"},
            {"title": [T("match", GREEN, 30)], "desc": "تابلو برق چندتایی: هر حالت با یک شاخه، هر شاخه کارِ خودش"},
            {"title": [T("حالتِ جامانده؟", ORANGE, 26)], "desc": "کامپایلر خطا می‌دهد و بیلد نمی‌شود — هیچ حالتی از قلم نمی‌افتد"},
        ]},
    ],
    "note": "تمرین: enumی برای فصل‌های سال بساز و با match برای هر فصل یک یادگاری چاپ کن — بهار: باران و شکوفه، زمستان: برف و نارنج!",
}
