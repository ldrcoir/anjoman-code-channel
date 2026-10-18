# -*- coding: utf-8 -*-
"""پست ۰۰۶ — چالش هفتگی اول: خروجی برنامه چیست؟"""
import sys
sys.path.insert(0, "/home/z/my-project/scripts")
from poster_lib import render, T, FB, FG, VAR, FN, NUM, ACCENT, GREEN, ACCENT2

SPEC = {
    "category": "ROOY",
    "series_label": "رویدادهای علمی  •  چالش هفتگی انجمن",
    "title": "خروجی این برنامه چند است؟",
    "title_size": 52,
    "subtitle": "چالش اول — برای همه علاقه‌مندان کدنویسی",
    "sections": [
        {"type": "code", "name": "challenge.py", "lines": [
            {"segs": [T("a", VAR), T(" = ", FG), T("7", NUM)]},
            {"segs": [T("b", VAR), T(" = ", FG), T("3", NUM)]},
            {"segs": [T("print", FN), T("(", FG), T("a", VAR), T(" + ", FG), T("b", VAR), T(" * ", FG), T("2", NUM), T(")", FG)]},
            {"sep": "سوال این هفته"},
        ]},
        {"type": "bullets", "h": 92, "gap": 14, "colors": [ACCENT, GREEN, ACCENT2],
         "items": [
            {"title": [FB("اول فکر کن", ACCENT, 30)],
             "desc": "بدون اجرا حدس بزن؛ به ترتیب عملگرها دقت کن"},
            {"title": [FB("بعد امتحان کن", GREEN, 30)],
             "desc": "کد را اجرا کن و ببین حدست درست بود یا نه"},
            {"title": [FB("پاسخ بده", ACCENT2, 30)],
             "desc": "جواب و توضیح کوتاهت را در گروه انجمن بفرست"},
         ]},
    ],
}

def build(out_path):
    return render(SPEC, out_path)
