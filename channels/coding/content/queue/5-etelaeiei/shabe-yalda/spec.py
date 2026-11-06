# -*- coding: utf-8 -*-
"""شب یلدا — بلندترین شب سال (پیام مناسبتی/فرهنگی)"""
import sys, pathlib
for _p in pathlib.Path(__file__).resolve().parents:
    if (_p / 'scripts' / 'poster_lib.py').exists():
        sys.path.insert(0, str(_p / 'scripts')); break
from poster_lib import FB, ACCENT, GREEN, ACCENT2, ORANGE

SPEC = {
    "category_fa": "اطلاعیه",   # فقط داخلی
    "series_label": "۳۰ آذر",
    "title": "شب یلدا",
    "title_size": 66,
    "subtitle": "بلندترین شب سال — گرم با هندوانه، حافظ و یک چالش کد",
    "clean_footer": "شب یلدا",
    "sections": [
        {"type": "bullets", "h": 92, "gap": 13, "desc_size": 26, "items": [
            {"title": [FB("چالش یلدایی", ACCENT)], "desc": "برنامه‌ای بنویس که طول شب و روز شهرت را حساب کند — خروجی‌ات را در کانال بفرست"},
            {"title": [FB("انار و کد", GREEN)], "desc": "کنار هندوانه و آجیل، چند خط کد هم بگذار؛ خنده‌دارتر از فال حافظ می‌شود"},
            {"title": [FB("مرور سال", ACCENT2)], "desc": "آخر سال میلادی نزدیک است؛ ببین از مهر تا حالا چه یاد گرفتی و چه ساختی"},
        ]},
        {"type": "code", "name": "yalda.rs", "lines": [
            {"segs": [('let ', 'mono', 33, (187, 134, 252), False), ('shab', 'mono', 33, (122, 162, 247), False), (' = ', 'mono', 33, (220, 225, 235), False), ('"deraz"', 'mono', 33, (166, 214, 167), True), (';', 'mono', 33, (220, 225, 235), False)]},
            {"segs": [('let ', 'mono', 33, (187, 134, 252), False), ('kod', 'mono', 33, (122, 162, 247), False), (' = ', 'mono', 33, (220, 225, 235), False), ('"garm"', 'mono', 33, (166, 214, 167), True), (';', 'mono', 33, (220, 225, 235), False)]},
            {"segs": [('// ', 'mono', 33, (120, 133, 155), False), ('شب بلند است، اما کدنویس از آن نمی‌ترسد', 'fa_med', 30, (120, 133, 155), True)]},
        ]},
    ],
    "note": "یلدا مبارک — ۳۰ آذر ۱۴۰۵",
}
