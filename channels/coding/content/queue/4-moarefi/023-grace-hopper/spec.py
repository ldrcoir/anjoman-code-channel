# -*- coding: utf-8 -*-
"""معرفی مشاهیر ۲ — گریس هاپر، مادر کامپایلر"""
import sys, pathlib
for _p in pathlib.Path(__file__).resolve().parents:
    if (_p / 'scripts' / 'poster_lib.py').exists():
        sys.path.insert(0, str(_p / 'scripts')); break
from poster_lib import FB, ACCENT, GREEN, ACCENT2, ORANGE

SPEC = {
    "category_fa": "معرفی",   # فقط داخلی — هرگز روی عکس/متن نمی‌آید
    "series_label": "چهره‌های دنیای کامپیوتر | آبان ۱۴۰۵",
    "title": "گریس هاپر؛ مادر کامپایلر",
    "title_size": 50,
    "subtitle": "زنی که به کامپیوتر یاد داد با زبان آدم‌ها حرف بزند",
    "clean_footer": "معرفی مشاهیر کامپیوتر",
    "sections": [
        {"type": "stat", "value": "۱۹۵۲", "unit": "میلادی",
         "label": "سال ساخت اولین کامپایلر تاریخ به دست هاپر", "size": 76},
        {"type": "bullets", "h": 100, "gap": 12, "desc_size": 25, "items": [
            {"icon": "bolt", "title": [FB("اولین باگ واقعی", GREEN)], "desc": "موج ۱۹۴۵ در رلهٔ رایانهٔ Mark II — از آن پس «باگ» رایج شد"},
            {"icon": "code", "title": [FB("زبان COBOL", ACCENT2)], "desc": "ایدهٔ زبان برنامه‌نویسی نزدیک به انگلیسی از او بود"},
            {"icon": "clock", "title": [FB("درس نانوثانیه", ACCENT)], "desc": "سیم ۳۰ سانتی به دانشجوها می‌داد: مسیر نور در یک نانوثانیه"},
            {"icon": "star", "title": [FB("دریادار نیروی دریایی", ORANGE)], "desc": "تا ۸۵ سالگی خدمت کرد؛ نشان افتخار آزادی آمریکا را گرفت"},
        ]},
    ],
    "note": "نقل هاپر: «خطرناک‌ترین جملهٔ دنیا این است: ما همیشه همین‌طور انجامش دادیم»",
}
