# -*- coding: utf-8 -*-
"""پست ۰۰۵ — معرفی مشاهیر رایانه: خوارزمی"""
import sys
sys.path.insert(0, "/home/z/my-project/scripts")
from poster_lib import render, FB, ACCENT, GREEN, ACCENT2, ORANGE

SPEC = {
    "category": "MOAR",
    "category_fa": "معرفی",
    "clean_footer": "مشاهیر دنیای رایانه",
    "series_label": "معرفی مشاهیر دنیای رایانه  •  بخش اول",
    "title": "خوارزمی؛ پدر الگوریتم",
    "title_size": 58,
    "subtitle": "ابوجعفر محمد بن موسی خوارزمی — ریاضی‌دان ایرانی قرن سوم هجری",
    "sections": [
        {"type": "bullets", "h": 96, "gap": 14, "colors": [ACCENT, GREEN, ACCENT2, ORANGE, ACCENT],
         "items": [
            {"title": [FB("کاشف جبر", ACCENT, 30)],
             "desc": "کتاب «الجبر و المقابله» او، ریشه واژه Algebra در ریاضیات است"},
            {"title": [FB("نام او = الگوریتم", GREEN, 30)],
             "desc": "واژه Algorithm از شکل لاتین‌شده نام او ساخته شده است"},
            {"title": [FB("ستاره خانه‌حکمت بغداد", ACCENT2, 30)],
             "desc": "در مرکز علمی و پژوهشی جهان آن روزگار می‌درخشید"},
            {"title": [FB("هنر حل مسئله", ORANGE, 30)],
             "desc": "مسئله را به گام‌های کوچک و دقیق بشکن؛ همان کاری که او کرد"},
            {"title": [FB("میراث امروز", ACCENT, 30)],
             "desc": "هر برنامه‌ای که امروز می‌نویسیم، ادامه راه اوست"},
         ]},
    ],
}

def build(out_path):
    return render(SPEC, out_path)
