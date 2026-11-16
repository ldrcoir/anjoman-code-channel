# -*- coding: utf-8 -*-
"""درس ۱۰ — آموزش Rust از صفر: آرایه‌ها و حلقهٔ for
طبق وعدهٔ کپشن درس ۹: «درس بعدی: آرایه‌ها و حلقهٔ for»"""
import sys, pathlib
for _p in pathlib.Path(__file__).resolve().parents:
    if (_p / 'scripts' / 'poster_lib.py').exists():
        sys.path.insert(0, str(_p / 'scripts')); break
from poster_lib import T, TF, FB, FG, VAR, FN, STR, CMT, NUM, OUT, ACCENT, GREEN, ACCENT2, ORANGE

SPEC = {
    "category_fa": "آموزشی",   # فقط داخلی — هرگز روی عکس/متن نمی‌آید
    "series_label": "آموزش Rust از صفر",
    "lesson_no": 10,
    "title": "آرایه‌ها و حلقهٔ for",
    "title_size": 48,
    "subtitle": "فهرست اعداد، یک‌جا و منظم",
    "clean_footer": "آموزش Rust از صفر",
    "sections": [
        {"type": "code", "name": "src/main.rs", "lh": 33, "lines": [
            {"segs": [T("let", FN), T(" nomarat = [", FG), T("20", NUM), T(", ", FG), T("18", NUM), T(", ", FG), T("19", NUM), T("];  ", FG), T("// nomre-ha", CMT)]},
            {"segs": [T("println!", FN), T("(", FG), T("\"Aval: {}\"", STR), T(", nomarat[", FG), T("0", NUM), T("]);", FG)]},
            {"segs": [T("println!", FN), T("(", FG), T("\"Tedad: {}\"", STR), T(", nomarat.len());", FG)]},
            {"segs": [T("for", FN), T(" n ", FG), T("in", FN), T(" nomarat {            ", FG), T("// har nomre", CMT)]},
            {"segs": [T("    println!", FN), T("(", FG), T("\"Nomre: {}\"", STR), T(", n);", FG)]},
            {"segs": [T("}", FG)]},
            {"sep": "خروجی"},
            {"segs": [T("Aval: 20", OUT)]},
            {"segs": [T("Tedad: 3", OUT)]},
            {"segs": [T("Nomre: 20", OUT)]},
            {"segs": [T("Nomre: 18", OUT)]},
            {"segs": [T("Nomre: 19", OUT)]},
        ]},
        {"type": "bullets", "h": 80, "gap": 8, "desc_size": 24, "items": [
            {"title": [FB("آرایه", ACCENT)], "desc": "فهرست ثابت از چند مقدارِ هم‌نوع داخل [ ] با کاما"},
            {"title": [FB("nomarat[0]", GREEN)], "desc": "شمارش از صفر؛ صفرم = اولین عدد؛ len() تعدادشان"},
            {"title": [FB("for", ORANGE)], "desc": "روی همهٔ عضوها می‌چرخد و هر بار یکی را می‌دهد"},
        ]},
    ],
    "note": "تمرین: آرایه‌ای از معدل‌های ۵ نفر بساز و با for همه را چاپ کن؛ بعد تعدادشان را با len() بنویس",
}
