# -*- coding: utf-8 -*-
"""درس ۳ — آموزش پایتون از صفر: انواع داده"""
import sys
sys.path.insert(0, "/home/z/my-project/scripts")
from poster_lib import (render, spec_text, T, TF, FB, FG, VAR, FN, STR, OUT,
                        NUM, CMT, ACCENT, GREEN, ACCENT2, ORANGE)

SPEC = {
    "category": "AMOO",
    "category_fa": "آموزشی",
    "series_label": "آموزش پایتون از صفر",
    "lesson_no": 3,
    "title": "انواع داده در پایتون",
    "title_size": 58,
    "subtitle": "هر داده یک نوع دارد؛ با type() آن را ببین",
    "clean_footer": "آموزش پایتون از صفر",
    "sections": [
        {"type": "code", "name": "types.py", "lines": [
            {"segs": [T("age", VAR), T(" = ", FG), T("15", NUM), T("            # int", CMT)]},
            {"segs": [T("price", VAR), T(" = ", FG), T("99.5", NUM), T("         # float", CMT)]},
            {"segs": [T("name", VAR), T(" = ", FG), T('"', STR), TF("سارا", STR), T('"', STR), T("      # str", CMT)]},
            {"segs": [T("ok", VAR), T(" = ", FG), T("True", FN), T("           # bool", CMT)]},
            {"segs": [T("print", FN), T("(", FG), T("type", FN), T("(", FG), T("age", VAR), T("))", FG), T("  # <class 'int'>", CMT)]},
        ]},
        {"type": "grid", "card_h": 100, "gap": 14, "desc_size": 23,
         "colors": [ACCENT, GREEN, ACCENT2, ORANGE],
         "items": [
            {"title": [FB("int", ACCENT, 29)], "desc": "اعداد صحیح؛ مثل 15"},
            {"title": [FB("float", GREEN, 29)], "desc": "اعداد اعشاری؛ مثل 99.5"},
            {"title": [FB("str", ACCENT2, 29)], "desc": "متن؛ میان گیومه می‌نشیند"},
            {"title": [FB("bool", ORANGE, 29)], "desc": "منطقی؛ True یا False"},
         ]},
    ],
    "note": "تبدیل نوع: int() و float() و str() — مثلا int(\"12\") + 3 می‌شود 15",
}
