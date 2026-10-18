# -*- coding: utf-8 -*-
"""پست ۰۰۷ — معرفی منابع یادگیری پایتون"""
import sys
sys.path.insert(0, "/home/z/my-project/scripts")
from poster_lib import render, FB, ACCENT, GREEN, ACCENT2, ORANGE

SPEC = {
    "category": "MOAR",
    "series_label": "معرفی منابع آموزشی  •  بخش اول",
    "title": "مسیر یادگیری پایتون",
    "title_size": 58,
    "subtitle": "از کجا شروع کنیم؟ سه راه پیشنهادی ما",
    "sections": [
        {"type": "bullets", "h": 96, "gap": 14, "colors": [ACCENT, GREEN, ACCENT2, ORANGE],
         "items": [
            {"title": [FB("مستندات و آموزش رسمی پایتون", ACCENT, 29)],
             "desc": "آموزش گام‌به‌گام با مثال‌های ساده؛ بهترین نقطه شروع"},
            {"title": [FB("کتاب «پایتون برای همه»", GREEN, 29)],
             "desc": "منبع رایگان و شناخته‌شده برای شروع بدون پیش‌نیاز"},
            {"title": [FB("سایت‌های حل مسئله", ACCENT2, 29)],
             "desc": "تمرین‌های کوچک روزانه؛ مهارتت را واقعا محک می‌زنند"},
            {"title": [FB("کارگاه‌های انجمن", ORANGE, 29)],
             "desc": "کارگاه‌های حضوری ما به‌زودی اعلام می‌شود؛ از دست نده"},
         ]},
    ],
    "note": "لینک منابع پیشنهادی هر هفته، در گروه اعضای انجمن به اشتراک گذاشته می‌شود",
}

def build(out_path):
    return render(SPEC, out_path)
