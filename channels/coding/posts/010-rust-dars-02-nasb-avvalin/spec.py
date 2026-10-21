# -*- coding: utf-8 -*-
"""درس ۲ — آموزش Rust از صفر: نصب Rust و اولین برنامه"""
import sys, pathlib
for _p in pathlib.Path(__file__).resolve().parents:
    if (_p / 'scripts' / 'poster_lib.py').exists():
        sys.path.insert(0, str(_p / 'scripts')); break
from poster_lib import T, TF, FB, FG, VAR, FN, STR, CMT, NUM, OUT, ACCENT, GREEN, ACCENT2, ORANGE

SPEC = {
    "category_fa": "آموزشی",   # فقط داخلی — هرگز روی عکس/متن نمی‌آید
    "series_label": "آموزش Rust از صفر",
    "lesson_no": 2,
    "title": "نصب و اولین برنامه",
    "title_size": 58,
    "subtitle": "با rustup نصب کن، با cargo اجرا کن",
    "clean_footer": "آموزش Rust از صفر",
    "sections": [
        {"type": "code", "name": "terminal", "lh": 46, "lines": [
            {"segs": [T("$ ", CMT), T("cargo new salam", FG)]},
            {"segs": [T("$ ", CMT), T("cd salam", FG)]},
            {"segs": [T("$ ", CMT), T("cargo run", FG)]},
        ]},
        {"type": "code", "name": "src/main.rs", "lh": 46, "lines": [
            {"segs": [T("fn ", FN), T("main", VAR), T("() {", FG)]},
            {"segs": [T("    ", FG), T("println!", FN), T("(", FG), T('"', STR), T("Salam, donya!", STR), T('"', STR), T(");", FG)]},
            {"segs": [T("}", FG)]},
        ]},
        {"type": "bullets", "h": 80, "gap": 9, "desc_size": 24, "items": [
            {"title": [FB("rustup", ACCENT)], "desc": "نصب‌کنندهٔ رسمی؛ از نشانی rustup.rs"},
            {"title": [FB("cargo", GREEN)], "desc": "دستیار همیشگی؛ ساخت پروژه، build و اجرا"},
            {"title": [FB("src/main.rs", ACCENT2)], "desc": "فایل اصلی کد پروژه همین‌جاست"},
        ]},
    ],
    "note": "خروجی اولین برنامه‌ات را بفرست تا در جمعه معرفی کنیم",
}
