# -*- coding: utf-8 -*-
"""درس ۹ — آموزش Rust از صفر: تکرار با حلقه‌های while و loop
طبق وعدهٔ کپشن درس ۸: «درس بعدی: تکرار — حلقه‌های loop و while»"""
import sys, pathlib
for _p in pathlib.Path(__file__).resolve().parents:
    if (_p / 'scripts' / 'poster_lib.py').exists():
        sys.path.insert(0, str(_p / 'scripts')); break
from poster_lib import T, TF, FB, FG, VAR, FN, STR, CMT, NUM, OUT, ACCENT, GREEN, ACCENT2, ORANGE

SPEC = {
    "category_fa": "آموزشی",   # فقط داخلی — هرگز روی عکس/متن نمی‌آید
    "series_label": "آموزش Rust از صفر",
    "lesson_no": 9,
    "title": "تکرار: حلقه‌های while و loop",
    "title_size": 46,
    "subtitle": "همان کار را هزار بار، بدون کپی‌پیست",
    "clean_footer": "آموزش Rust از صفر",
    "sections": [
        {"type": "code", "name": "src/main.rs", "lh": 33, "lines": [
            {"segs": [T("let", FN), T(" ", FG), T("mut", FN), T(" shomare = ", FG), T("1", NUM), T(";   ", FG), T("// shomare-ye hafe", CMT)]},
            {"segs": [T("while", FN), T(" shomare <= ", FG), T("3", NUM), T(" {  ", FG), T("// ta shart dorost ast", CMT)]},
            {"segs": [T("    println!", FN), T("(", FG), T("\"hafe-ye {}\"", STR), T(", shomare);", FG)]},
            {"segs": [T("    shomare = shomare + ", FG), T("1", NUM), T(";", FG)]},
            {"segs": [T("}", FG)]},
            {"segs": [T("loop", FN), T(" {                 ", FG), T("// bi-payan ta break", CMT)]},
            {"segs": [T("    shomare = shomare + ", FG), T("1", NUM), T(";", FG)]},
            {"segs": [T("    if", FN), T(" shomare >= ", FG), T("6", NUM), T(" { ", FG), T("break", FN), T("; }", FG)]},
            {"segs": [T("}", FG)]},
            {"sep": "خروجی"},
            {"segs": [T("hafe-ye 1", OUT)]},
            {"segs": [T("hafe-ye 2", OUT)]},
            {"segs": [T("hafe-ye 3", OUT)]},
        ]},
        {"type": "bullets", "h": 80, "gap": 8, "desc_size": 24, "items": [
            {"title": [FB("while", ACCENT)], "desc": "اول شرط را چک می‌کند؛ تا وقتی درست است تکرار می‌شود"},
            {"title": [FB("loop", GREEN)], "desc": "حلقهٔ بی‌پایان — تا وقتی break نگویی می‌چرخد"},
            {"title": [FB("break", ACCENT2)], "desc": "کلید خروج از هر حلقه‌ای، همان‌جا که گفتی"},
        ]},
    ],
    "note": "تمرین: با while اعداد ۱ تا ۱۰ را چاپ کن؛ بعد همان کار را با loop و break انجام بده",
}
