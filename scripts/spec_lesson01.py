# -*- coding: utf-8 -*-
"""درس ۱ — آموزش پایتون از صفر: اولین برنامه (نسخهٔ بازسازیشده با شماره درس و دستهٔ آموزشی)"""
import sys
sys.path.insert(0, "/home/z/my-project/scripts")
sys.path.insert(0, "/home/z/my-project/anjoman-code-bot/scripts")
from poster_lib import (render, spec_text, T, TF, FB, FG, VAR, FN, STR, OUT,
                        NUM, CMT, ACCENT, GREEN, ACCENT2)

SPEC = {
    "category": "AMOO",
    "category_fa": "آموزشی",
    "series_label": "آموزش پایتون از صفر",
    "lesson_no": 1,
    "title": "اولین برنامه‌ات را بنویس!",
    "title_size": 58,
    "subtitle": "در ۳۰ ثانیه با زبان پایتون آشنا شو",
    "clean_footer": "آموزش پایتون از صفر",
    "sections": [
        {"type": "code", "name": "first_program.py", "lines": [
            {"segs": [T("name", VAR), T(" = ", FG), T('"', STR), TF("دانش\u200cآموز", STR), T('"', STR)]},
            {"segs": [T("print", FN), T("(", FG), T('"', STR), TF("سلام", STR), T('"', STR), T(", ", FG), T("name", VAR), T(")", FG)]},
            {"segs": [T("print", FN), T("(", FG), T('"', STR), TF("به دنیای کدنویسی خوش آمدی!", STR), T('"', STR), T(")", FG)]},
            {"sep": "خروجی برنامه"},
            {"segs": [TF("سلام دانش\u200cآموز", OUT)]},
            {"segs": [TF("به دنیای کدنویسی خوش آمدی!", OUT)]},
        ]},
        {"type": "bullets", "h": 92, "gap": 12, "colors": [ACCENT, GREEN, ACCENT2],
         "items": [
            {"title": [FB("متغیر "), T("(Variable)", ACCENT, 30)],
             "desc": "جعبه\u200cای برای نگه\u200cداری داده\u200cها"},
            {"title": [FB("تابع "), T("print()", GREEN, 30)],
             "desc": "هر چیزی را روی صفحه نمایش می\u200cدهد"},
            {"title": [FB("رشته "), T("(String)", ACCENT2, 30)],
             "desc": "متن است؛ میان گیومه می\u200cنشیند"},
         ]},
    ],
    "note": "درس بعدی: متغیرها را عمیق\u200cتر می\u200cشناسیم",
}
