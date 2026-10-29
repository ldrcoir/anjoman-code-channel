# -*- coding: utf-8 -*-
"""معرفی انجمن — نخستین پست روزانهٔ کانال انرژی‌پژوهان (بند ت — معرفی)
رنگ پالت همان پیش‌فرض برند است؛ پالت کدنویسی دست نمی‌خورد."""
import sys, pathlib
for _p in pathlib.Path(__file__).resolve().parents:
    if (_p / 'scripts' / 'poster_lib.py').exists():
        sys.path.insert(0, str(_p / 'scripts')); break
from poster_lib import FB, WHITE, ACCENT, ACCENT2, GREEN, ORANGE, GOLD

SPEC = {
    "category_fa": "معرفی",   # فقط داخلی — هرگز روی عکس/متن نمی‌آید
    "series_label": "معرفی کانال | آبان ۱۴۰۵",
    "title": "انرژی‌های نوین",
    "title_size": 56,
    "subtitle": "کانال رسمی انجمن دانش‌آموزی انرژی‌های نوین",
    "clean_footer": "پست معرفی انجمن",
    "sections": [
        {"type": "grid", "card_h": 96, "gap": 14, "desc_size": 25, "items": [
            {"title": [FB("روزی یک پست", ACCENT, 29)], "desc": "هر روز ساعت ۱۰:۰۰ یک آیتم تازه"},
            {"title": [FB("چرخهٔ هفتگی", GREEN, 29)], "desc": "خبر، نیروگاه، آموزش، پروژه"},
            {"title": [FB("خبر با منبع", ORANGE, 29)], "desc": "هر خبر تاریخ و منبع معتبر دارد"},
            {"title": [FB("عددهای واقعی", ACCENT2, 29)], "desc": "پروژه با اندازه‌گیری واقعی"},
        ]},
        {"type": "bullets", "h": 118, "gap": 16, "desc_size": 26, "items": [
            {"title": [FB("پروژهٔ بزرگ سال", GREEN)], "desc": "پنل خورشیدی بام مدرسه — روایت هفتگی با عکس و عدد"},
            {"title": [FB("نشریهٔ ماهانه + مصاحبه", ORANGE)], "desc": "شمارهٔ اول آخر آبان؛ گفت‌وگو با کارشناس انرژی"},
            {"title": [FB("مسابقهٔ علمی فصلی", GOLD)], "desc": "هر فصل یک آزمون کوتاه با معرفی در نشریه"},
        ]},
    ],
    "note": "همین حالا عضو شوید و پیشنهاد موضوع‌های علمی‌تان را برای ما بفرستید",
}
