# -*- coding: utf-8 -*-
"""درس ۵ — آموزش پایتون از صفر: عملگرها"""
import sys
sys.path.insert(0, "/home/z/my-project/scripts")
from poster_lib import (render, spec_text, T, TF, FB, FG, VAR, FN, STR, OUT,
                        NUM, CMT, ACCENT, GREEN, ACCENT2, ORANGE)

SPEC = {
    "category": "AMOO",
    "category_fa": "آموزشی",
    "series_label": "آموزش پایتون از صفر",
    "lesson_no": 5,
    "title": "عملگرها در پایتون",
    "title_size": 60,
    "subtitle": "ابزارهای محاسبه و مقایسه",
    "clean_footer": "آموزش پایتون از صفر",
    "sections": [
        {"type": "code", "name": "operators.py", "lines": [
            {"segs": [T("print", FN), T("(", FG), T("7", NUM), T(" + ", FG), T("3", NUM), T(")", FG), T("      # 10", CMT)]},
            {"segs": [T("print", FN), T("(", FG), T("7", NUM), T(" % ", FG), T("3", NUM), T(")", FG), T("      # 1  باقیمانده", CMT)]},
            {"segs": [T("print", FN), T("(", FG), T("2", NUM), T(" ** ", FG), T("4", NUM), T(")", FG), T("     # 16 توان", CMT)]},
            {"segs": [T("print", FN), T("(", FG), T("10", NUM), T(" >= ", FG), T("9", NUM), T(")", FG), T("    # True", CMT)]},
            {"segs": [T("print", FN), T("(", FG), T("True", FN), T(" and ", FG), T("False", FN), T(")", FG), T("  # False", CMT)]},
        ]},
        {"type": "grid", "card_h": 100, "gap": 14, "desc_size": 23,
         "colors": [ACCENT, GREEN, ACCENT2, ORANGE],
         "items": [
            {"title": [FB("حسابی", ACCENT, 28)], "desc_ltr": True, "desc": "+  -  *  /  //  %  **"},
            {"title": [FB("مقایسه‌ای", GREEN, 28)], "desc_ltr": True, "desc": "==  !=  >  <  >=  <="},
            {"title": [FB("منطقی", ACCENT2, 28)], "desc_ltr": True, "desc": "and  or  not"},
            {"title": [FB("انتساب", ORANGE, 28)], "desc_ltr": True, "desc": "=  +=  -=  خلاصه‌نویسی"},
         ]},
    ],
    "note": "ترتیب اجرا مثل ریاضی است؛ پرانتز اولویت را عوض می‌کند",
}
