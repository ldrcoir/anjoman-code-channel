# -*- coding: utf-8 -*-
"""درس ۲ — آموزش پایتون از صفر: متغیرها"""
import sys
sys.path.insert(0, "/home/z/my-project/scripts")
from poster_lib import (render, spec_text, T, TF, FB, FG, VAR, FN, STR, OUT,
                        NUM, CMT, ACCENT, GREEN, ACCENT2)

SPEC = {
    "category": "AMOO",
    "category_fa": "آموزشی",
    "series_label": "آموزش پایتون از صفر",
    "lesson_no": 2,
    "title": "متغیرها؛ جعبه‌های داده",
    "title_size": 56,
    "subtitle": "داده‌ها را ذخیره کن، عوض کن، استفاده کن",
    "clean_footer": "آموزش پایتون از صفر",
    "sections": [
        {"type": "code", "name": "variables.py", "lines": [
            {"segs": [T("name", VAR), T(" = ", FG), T('"', STR), TF("علی", STR), T('"', STR)]},
            {"segs": [T("age", VAR), T(" = ", FG), T("15", NUM)]},
            {"segs": [T("print", FN), T("(", FG), T("name", VAR), T(", ", FG), T("age", VAR), T(")", FG)]},
            {"sep": "مثال ۲ — تغییر مقدار متغیر"},
            {"segs": [T("score", VAR), T(" = ", FG), T("18", NUM)]},
            {"segs": [T("score", VAR), T(" = ", FG), T("score", VAR), T(" + ", FG), T("2", NUM)]},
            {"segs": [T("print", FN), T("(", FG), T("score", VAR), T(")", FG), T("   # 20", CMT)]},
            {"sep": "مثال ۳ — جابه‌جایی دو متغیر"},
            {"segs": [T("a", VAR), T(", ", FG), T("b", VAR), T(" = ", FG), T("1", NUM), T(", ", FG), T("2", NUM)]},
            {"segs": [T("a", VAR), T(", ", FG), T("b", VAR), T(" = ", FG), T("b", VAR), T(", ", FG), T("a", VAR)]},
            {"segs": [T("print", FN), T("(", FG), T("a", VAR), T(", ", FG), T("b", VAR), T(")", FG), T("   # 2 1", CMT)]},
        ]},
    ],
    "note": "قانون نام‌گذاری: با حرف شروع شود، بدون فاصله؛ name و Name یکی نیستند",
}
