# -*- coding: utf-8 -*-
"""درس ۱ — آموزش Rust از صفر: Rust چیست و چرا؟"""
import sys, pathlib
for _p in pathlib.Path(__file__).resolve().parents:
    if (_p / 'scripts' / 'poster_lib.py').exists():
        sys.path.insert(0, str(_p / 'scripts')); break
from poster_lib import T, TF, FB, FG, VAR, FN, STR, CMT, NUM, OUT, ACCENT, GREEN, ACCENT2, ORANGE

SPEC = {
    "category_fa": "آموزشی",   # فقط داخلی — هرگز روی عکس/متن نمی‌آید
    "series_label": "آموزش Rust از صفر",
    "lesson_no": 1,
    "title": "Rust چیست و چرا؟",
    "title_size": 58,
    "subtitle": "زبانی که سرعت و امنیت را با هم می‌دهد",
    "clean_footer": "آموزش Rust از صفر",
    "sections": [
        {"type": "code", "name": "main.rs", "lines": [
            {"segs": [T("// ", CMT, 30), TF("اولین نگاه به Rust", CMT, 28)]},
            {"segs": [T("fn ", FN), T("main", VAR), T("() {", FG)]},
            {"segs": [T("    ", FG), T("println!", FN), T("(", FG), T('"', STR), T("Salam, donya!", STR), T('"', STR), T(");", FG)]},
            {"segs": [T("}", FG)]},
        ]},
        {"type": "bullets", "h": 90, "gap": 11, "desc_size": 26, "items": [
            {"title": [FB("سریع", ACCENT)], "desc": "بدون زباله‌روب حافظه؛ سرعتی هم‌ردیف C"},
            {"title": [FB("امن", GREEN)], "desc": "خطاهای حافظه را همان موقعِ کامپایل می‌گیرد"},
            {"title": [FB("محبوب", ACCENT2)], "desc": "سال‌ها محبوب‌ترین زبانِ نظرسنجی توسعه‌دهندگان"},
            {"title": [FB("کاربردی", ORANGE)], "desc": "هستهٔ لینوکس، فایرفاکس و بخش‌هایی از ویندوز"},
        ]},
    ],
    "note": "درس بعد: Rust را نصب می‌کنیم و اولین برنامه را اجرا می‌کنیم",
}
