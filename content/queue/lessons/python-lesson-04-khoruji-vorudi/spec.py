# -*- coding: utf-8 -*-
"""درس ۴ — آموزش پایتون از صفر: خروجی با print و ورودی با input"""
import sys
sys.path.insert(0, "/home/z/my-project/scripts")
from poster_lib import (render, spec_text, T, TF, FB, FG, VAR, FN, STR, OUT,
                        NUM, CMT, ACCENT, GREEN)

SPEC = {
    "category": "AMOO",
    "category_fa": "آموزشی",
    "series_label": "آموزش پایتون از صفر",
    "lesson_no": 4,
    "title": "خروجی و ورودی",
    "title_size": 60,
    "subtitle": "با print نشان بده، با input بپرس",
    "clean_footer": "آموزش پایتون از صفر",
    "sections": [
        {"type": "code", "name": "talk.py", "lines": [
            {"segs": [T("name", VAR), T(" = ", FG), T("input", FN), T("(", FG), T('"', STR), TF("اسمت چیه؟ ", STR), T('"', STR), T(")", FG)]},
            {"segs": [T("print", FN), T("(", FG), T('"', STR), TF("سلام", STR), T('"', STR), T(", ", FG), T("name", VAR), T(")", FG)]},
            {"sep": "مثال ۲ — نوشتن مرتب با f-string"},
            {"segs": [T("age", VAR), T(" = ", FG), T("15", NUM)]},
            {"segs": [T("print", FN), T("(f", FG), T('"', STR), TF("سن تو ", STR), T("{age}", VAR), TF(" سال است", STR), T('"', STR), T(")", FG)]},
            {"sep": "مثال ۳ — گرفتن عدد از کاربر"},
            {"segs": [T("n", VAR), T(" = ", FG), T("int", FN), T("(", FG), T("input", FN), T("(", FG), T('"', STR), TF("یک عدد بگو: ", STR), T('"', STR), T("))", FG)]},
            {"segs": [T("print", FN), T("(", FG), T("n", VAR), T(" * ", FG), T("2", NUM), T(")", FG)]},
        ]},
        {"type": "bullets", "h": 92, "gap": 12, "colors": [ACCENT, GREEN],
         "items": [
            {"title": [FB("تابع "), T("input()", ACCENT, 30)],
             "desc": "همیشه متن (str) می‌دهد؛ برای عدد از int() کمک بگیر"},
            {"title": [FB("f-string")],
             "desc": "میان f\"...\" هر متغیری را داخل { } بگذار تا چاپ شود"},
         ]},
    ],
}
