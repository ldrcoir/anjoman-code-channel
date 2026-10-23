# -*- coding: utf-8 -*-
"""درس ۵ — آموزش Rust از صفر: متغیرها و تغییرناپذیری (let و let mut)"""
import sys, pathlib
for _p in pathlib.Path(__file__).resolve().parents:
    if (_p / 'scripts' / 'poster_lib.py').exists():
        sys.path.insert(0, str(_p / 'scripts')); break
from poster_lib import T, TF, FB, FG, VAR, FN, STR, CMT, NUM, OUT, ACCENT, GREEN, ACCENT2, ORANGE

SPEC = {
    "category_fa": "آموزشی",   # فقط داخلی — هرگز روی عکس/متن نمی‌آید
    "series_label": "آموزش Rust از صفر",
    "lesson_no": 5,
    "title": "متغیرها: let و let mut",
    "title_size": 54,
    "subtitle": "پیش‌فرضِ Rust: هیچ‌چیز بی‌اجازه عوض نمی‌شود",
    "clean_footer": "آموزش Rust از صفر",
    "sections": [
        {"type": "code", "name": "src/main.rs", "lines": [
            {"segs": [T("let", FN), T(" rekord = ", FG), T("12", NUM), T(";", FG), T("      // rekord-e bazi", CMT)]},
            {"segs": [T("let mut", FN), T(" emtiaz = ", FG), T("10", NUM), T(";", FG), T("    // emtiaz-e bazi", CMT)]},
            {"segs": [T("emtiaz = emtiaz + ", FG), T("5", NUM), T(";", FG)]},
            {"sep": "خروجی"},
            {"segs": [T("emtiaz: 15", OUT)]},
            {"sep": "اگر rekord را عوض کنی؟"},
            {"segs": [T("error[E0384]: cannot assign twice", OUT)]},
            {"segs": [T("  to immutable variable `rekord`", OUT)]},
        ]},
        {"type": "bullets", "h": 86, "gap": 10, "desc_size": 25, "items": [
            {"title": [FB("let = قفل", ACCENT)], "desc": "هر متغیر پیش‌فرض تغییرناپذیر است"},
            {"title": [FB("let mut", GREEN)], "desc": "با اجازهٔ mut مقدار عوض می‌شود"},
            {"title": [FB("کامپایلر حواسش هست", ACCENT2)], "desc": "بدون mut خطای E0384 می‌گیری!"},
        ]},
    ],
    "note": "تمرین: امتیاز رفیق مار را در متغیری mut دار نگه دار و سه بار به‌روزش کن",
}
