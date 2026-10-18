# -*- coding: utf-8 -*-
"""پست ۰۰۲ — سری آموزش پایتون، قسمت دوم: دستور if"""
import sys
sys.path.insert(0, "/home/z/my-project/scripts")
from poster_lib import render, T, TF, FB, FG, VAR, FN, STR, OUT, NUM, CMT, ACCENT, GREEN, ACCENT2

SPEC = {
    "category": "AMOO",
    "series_label": "سری آموزش پایتون  •  قسمت دوم",
    "title": "برنامه‌ات را فکر کن!",
    "title_size": 60,
    "subtitle": "با دستور if برنامه‌ات تصمیم می‌گیرد",
    "sections": [
        {"type": "code", "name": "decision.py", "lines": [
            {"segs": [T("score", VAR), T(" = ", FG), T("18", NUM)]},
            {"segs": [T("if", FN), T(" ", FG), T("score", VAR), T(" >= ", FG), T("17", NUM), T(":", FG)]},
            {"segs": [T("    ", FG), T("print", FN), T("(", FG), T('"', STR), TF("عالی بود!", STR), T('"', STR), T(")", FG)]},
            {"segs": [T("else", FN), T(":", FG)]},
            {"segs": [T("    ", FG), T("print", FN), T("(", FG), T('"', STR), TF("بیشتر تمرین کن!", STR), T('"', STR), T(")", FG)]},
            {"sep": "خروجی برنامه"},
            {"segs": [TF("عالی بود!", OUT)]},
        ]},
        {"type": "bullets", "h": 92, "gap": 14, "items": [
            {"title": [FB("دستور "), T("if", ACCENT, 30)],
             "desc": "اگر شرط درست باشد، دستورات داخلش اجرا می‌شوند"},
            {"title": [FB("دستور "), T("else", GREEN, 30)],
             "desc": "اگر شرط درست نباشد، این بخش اجرا می‌شود"},
            {"title": [FB("عملگرهای "), T("==  >=  <=", ACCENT2, 30)],
             "desc": "برای مقایسه‌کردن دو مقدار به کار می‌روند"},
        ]},
    ],
}

def build(out_path):
    return render(SPEC, out_path)
