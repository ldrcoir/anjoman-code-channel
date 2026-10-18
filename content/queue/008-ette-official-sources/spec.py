# -*- coding: utf-8 -*-
"""پست ۰۰۸ — اطلاع‌رسانی: منابع رسمی خبر جشنواره"""
import sys
sys.path.insert(0, "/home/z/my-project/scripts")
from poster_lib import render, FB, T, WHITE, ACCENT, GREEN, ACCENT2, ORANGE

SPEC = {
    "category": "ETTE",
    "series_label": "اطلاعیه  •  منابع رسمی خبر مسابقات",
    "title": "خبرهای جشنواره از کجا؟",
    "title_size": 56,
    "subtitle": "این سه منبع را دنبال کن تا هیچ خبری را از دست ندهی",
    "sections": [
        {"type": "bullets", "h": 96, "gap": 14, "colors": [ACCENT, GREEN, ACCENT2, ORANGE],
         "items": [
            {"title": [FB("شبکه ملی شاد", WHITE, 30), T(" pajouheshsara", ACCENT, 26)],
             "desc": "کانال رسمی پژوهش‌سراهای دانش‌آموزی؛ منبع اول اخبار"},
            {"title": [FB("کانال مسابقات کدنویسی", WHITE, 30), T(" @codenevisi", GREEN, 26)],
             "desc": "اعلام کارگاه‌ها و آموزش‌های رایگان گرایش‌ها"},
            {"title": [FB("کانال انجمن ما", WHITE, 30), T(" @anjomanfaghihi", ACCENT2, 26)],
             "desc": "اطلاعیه‌ها و برنامه‌های انجمن کدنویسی مدرسه فقیهی"},
            {"title": [FB("یادت باشد!", ORANGE, 30)],
             "desc": "زمان و نحوه ثبت‌نام فقط از منابع رسمی اعلام می‌شود"},
         ]},
    ],
    "note": "این پیام را برای هم‌کلاسی‌هایت بفرست تا همه باخبر باشند!",
}

def build(out_path):
    return render(SPEC, out_path)
