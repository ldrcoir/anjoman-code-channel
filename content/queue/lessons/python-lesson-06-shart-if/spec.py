# -*- coding: utf-8 -*-
"""درس ۶ — آموزش پایتون از صفر: تصمیم‌گیری با if"""
import sys
sys.path.insert(0, "/home/z/my-project/scripts")
from poster_lib import (render, spec_text, T, TF, FB, FG, VAR, FN, STR, OUT,
                        NUM, CMT, ACCENT, GREEN)

SPEC = {
    "category": "AMOO",
    "category_fa": "آموزشی",
    "series_label": "آموزش پایتون از صفر",
    "lesson_no": 6,
    "title": "تصمیم‌گیری با if",
    "title_size": 60,
    "subtitle": "حالا که متغیر و عملگر را بلدی، برنامه‌ات فکر کند!",
    "clean_footer": "آموزش پایتون از صفر",
    "sections": [
        {"type": "code", "name": "shart.py", "lines": [
            {"segs": [T("score", VAR), T(" = ", FG), T("16", NUM)]},
            {"segs": [T("if", FN), T(" score", VAR), T(" >= ", FG), T("10", NUM), T(":", FG)]},
            {"segs": [T("    ", FG), T("print", FN), T("(", FG), T('"', STR), TF("قبول شدی!", STR), T('"', STR), T(")", FG)]},
            {"segs": [T("else", FN), T(":", FG)]},
            {"segs": [T("    ", FG), T("print", FN), T("(", FG), T('"', STR), TF("تمرین بیشتر!", STR), T('"', STR), T(")", FG)]},
            {"sep": "مثال ۲ — چند شرط با elif"},
            {"segs": [T("temp", VAR), T(" = ", FG), T("35", NUM)]},
            {"segs": [T("if", FN), T(" temp", VAR), T(" > ", FG), T("30", NUM), T(":", FG)]},
            {"segs": [T("    ", FG), T("print", FN), T("(", FG), T('"', STR), TF("هوا گرم است", STR), T('"', STR), T(")", FG)]},
            {"segs": [T("elif", FN), T(" temp", VAR), T(" > ", FG), T("15", NUM), T(":", FG)]},
            {"segs": [T("    ", FG), T("print", FN), T("(", FG), T('"', STR), TF("هوا معتدل است", STR), T('"', STR), T(")", FG)]},
            {"segs": [T("else", FN), T(":", FG)]},
            {"segs": [T("    ", FG), T("print", FN), T("(", FG), T('"', STR), TF("هوا سرد است", STR), T('"', STR), T(")", FG)]},
        ]},
    ],
    "note": "تورفتگی ۴ فاصله‌ای اجباری است؛ پایتون بلوک را از تورفتگی می‌فهمد",
}
