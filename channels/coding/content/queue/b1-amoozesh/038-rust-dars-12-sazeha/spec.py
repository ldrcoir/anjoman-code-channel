# -*- coding: utf-8 -*-
"""درس ۱۲ — آموزش Rust از صفر: سازه‌ها (struct)
ادامهٔ طبیعی درس ۱۱ (توابع): بعد از آرایه (فهرست هم‌نوع‌ها)، نوبت داده‌های مرتبطِ چندنوع"""
import sys, pathlib
for _p in pathlib.Path(__file__).resolve().parents:
    if (_p / 'scripts' / 'poster_lib.py').exists():
        sys.path.insert(0, str(_p / 'scripts')); break
from poster_lib import T, TF, FB, FG, VAR, FN, STR, CMT, NUM, OUT, ACCENT, GREEN, ACCENT2, ORANGE

SPEC = {
    "category_fa": "آموزشی",   # فقط داخلی — هرگز روی عکس/متن نمی‌آید
    "series_label": "آموزش Rust از صفر",
    "lesson_no": 12,
    "title": "سازه‌ها (struct)",
    "title_size": 54,
    "subtitle": "داده‌های مرتبط، یک‌جا و با نام",
    "clean_footer": "آموزش Rust از صفر",
    "sections": [
        {"type": "code", "name": "src/main.rs", "lh": 33, "lines": [
            {"segs": [T("struct", FN), T(" DaneshAmooz {        ", FG), T("// ghalab-e jadid", CMT)]},
            {"segs": [T("    esm: ", FG), T("String", FN), T(",", FG)]},
            {"segs": [T("    sen: ", FG), T("i32", FN), T(",", FG)]},
            {"segs": [T("}", FG)]},
            {"segs": [T("fn", FN), T(" main() {", FG)]},
            {"segs": [T("    ", FG), T("let", FN), T(" d1 = DaneshAmooz {", FG)]},
            {"segs": [T("        esm: String::from(", FG), T("\"Sara\"", STR), T("),", FG)]},
            {"segs": [T("        sen: ", FG), T("15", NUM), T(",", FG)]},
            {"segs": [T("    };", FG)]},
            {"segs": [T("    println!", FN), T("(", FG), T("\"{} - {}\"", STR), T(", d1.esm, d1.sen);", FG)]},
            {"segs": [T("}", FG)]},
            {"sep": "خروجی"},
            {"segs": [T("Sara - 15", OUT)]},
        ]},
        {"type": "bullets", "h": 76, "gap": 8, "desc_size": 24, "items": [
            {"title": [FB("struct", ACCENT)], "desc": "قالبی که خودت می‌سازی؛ هر فیلد نام و نوع دارد"},
            {"title": [T("DaneshAmooz { .. }", GREEN, 31)], "desc": "نمونه‌سازی از روی قالب؛ همهٔ فیلدها باید پر شوند"},
            {"title": [FB("d1.esm", ORANGE)], "desc": "با نقطه به هر فیلدِ نمونه می‌رسی"},
        ]},
    ],
    "note": "تمرین: سازه‌ای برای کتاب بساز: esm، nevisande، sale-chap؛ یک نمونه بساز و فیلدهایش را چاپ کن",
}
