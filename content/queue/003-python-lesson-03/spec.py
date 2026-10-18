# -*- coding: utf-8 -*-
"""پست ۰۰۳ — سری آموزش پایتون، قسمت سوم: حلقه for"""
import sys
sys.path.insert(0, "/home/z/my-project/scripts")
from poster_lib import render, T, TF, FB, FG, VAR, FN, STR, OUT, NUM, CMT, ACCENT, GREEN, ACCENT2

SPEC = {
    "category": "AMOO",
    "series_label": "سری آموزش پایتون  •  قسمت سوم",
    "title": "تکرار بدون خستگی!",
    "title_size": 60,
    "subtitle": "با حلقه for یک کار را چند بار انجام بده",
    "sections": [
        {"type": "code", "name": "stages.py", "lines": [
            {"segs": [T("for", FN), T(" i ", VAR), T("in ", FG), T("range", FN), T("(", FG), T("1", NUM), T(", ", FG), T("6", NUM), T("):", FG)]},
            {"segs": [T("    ", FG), T("print", FN), T("(", FG), T('"', STR), TF("مرحله", STR), T('", ', FG), T("i", VAR), T(")", FG)]},
            {"sep": "خروجی برنامه"},
            {"segs": [TF("مرحله 1", OUT)]},
            {"segs": [TF("مرحله 2", OUT)]},
            {"segs": [T("...", CMT)]},
        ]},
        {"type": "bullets", "h": 92, "gap": 14, "items": [
            {"title": [FB("حلقه "), T("for", ACCENT, 30)],
             "desc": "کارها را به تعداد مشخص تکرار می‌کند"},
            {"title": [FB("تابع "), T("range()", GREEN, 30)],
             "desc": "بازه تکرار را تعیین می‌کند؛ از ۱ تا ۵"},
            {"title": [FB("شمارنده "), T("i", ACCENT2, 30)],
             "desc": "در هر تکرار، یک قدم جلو می‌رود"},
        ]},
    ],
}

def build(out_path):
    return render(SPEC, out_path)
