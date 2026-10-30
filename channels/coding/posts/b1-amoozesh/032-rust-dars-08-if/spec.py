# -*- coding: utf-8 -*-
"""درس ۸ — آموزش Rust از صفر: تصمیم‌گیری با شرط if/else
ادامهٔ زنجیرهٔ درس‌ها (درس ۷: رشته‌ها)؛ بازنویسی پیش‌نویس قدیمی صف با شمارهٔ درست"""
import sys, pathlib
for _p in pathlib.Path(__file__).resolve().parents:
    if (_p / 'scripts' / 'poster_lib.py').exists():
        sys.path.insert(0, str(_p / 'scripts')); break
from poster_lib import T, TF, FB, FG, VAR, FN, STR, CMT, NUM, OUT, ACCENT, GREEN, ACCENT2, ORANGE

SPEC = {
    "category_fa": "آموزشی",   # فقط داخلی — هرگز روی عکس/متن نمی‌آید
    "series_label": "آموزش Rust از صفر",
    "lesson_no": 8,
    "title": "تصمیم‌گیری: شرط if",
    "title_size": 54,
    "subtitle": "برنامه‌ات را دو راهی کن",
    "clean_footer": "آموزش Rust از صفر",
    "sections": [
        {"type": "code", "name": "src/main.rs", "lh": 41, "lines": [
            {"segs": [T("let", FN), T(" emtiaz: ", FG), T("i32", FN), T(" = ", FG), T("17", NUM), T(";", FG), T("   // emtiaz-e bazi", CMT)]},
            {"segs": [T("if", FN), T(" emtiaz >= ", FG), T("20", NUM), T(" {", FG)]},
            {"segs": [T("    println!", FN), T("(", FG), T("\"Barande shodi!\"", STR), T(");", FG)]},
            {"segs": [T("} ", FG), T("else if", FN), T(" emtiaz >= ", FG), T("10", NUM), T(" {", FG)]},
            {"segs": [T("    println!", FN), T("(", FG), T("\"Edame bede!\"", STR), T(");", FG)]},
            {"segs": [T("} ", FG), T("else", FN), T(" {", FG)]},
            {"segs": [T("    println!", FN), T("(", FG), T("\"Dobara talash kon\"", STR), T(");", FG)]},
            {"segs": [T("}", FG)]},
            {"sep": "خروجی"},
            {"segs": [T("Edame bede!", OUT)]},
        ]},
        {"type": "bullets", "h": 86, "gap": 10, "desc_size": 25, "items": [
            {"title": [FB("if", ACCENT)], "desc": "شرط true باشد بلوکش اجرا می‌شود؛ پرانتز لازم نیست"},
            {"title": [FB("else if / else", GREEN)], "desc": "زنجیرهٔ شرایط از بالا به پایین چک می‌شود"},
            {"title": [FB("if یک عبارت است", ACCENT2)], "desc": "می‌شود نتیجه‌اش را در let ریخت"},
        ]},
    ],
    "note": "تمرین: شرطی بنویس که اگر امتیاز از رکورد گذشت پیام Rekord jadid! چاپ کند",
}
