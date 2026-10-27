# -*- coding: utf-8 -*-
"""«۲. انرژی بادی» — ادامهٔ سری آموزشی منابع انرژی (بعد از «۱. انرژی خورشیدی» ادمین)"""
import sys, pathlib
for _p in pathlib.Path(__file__).resolve().parents:
    if (_p / 'scripts' / 'enerjy_poster.py').exists():
        sys.path.insert(0, str(_p / 'scripts')); break
from poster_lib import FB
from enerjy_poster import INK, GRN, AMBER_D, SKY, ORG

SPEC = {
    "category_fa": "آموزشی",   # فقط داخلی — هرگز روی عکس/متن نمی‌آید
    "series_label": "منابع انرژی نو | آبان ۱۴۰۵",
    "title": "۲. انرژی بادی",
    "title_size": 50,
    "subtitle": "باد، خورشیدی است که در حرکت دیده می‌شود",
    "clean_footer": "آموزش انرژی‌های نوین",
    "sections": [
        {"type": "stat", "value": "۳۳۸", "unit": "مگاوات",
         "label": "ظرفیت نصب‌شدهٔ برق‌بادی ایران — عمدتاً منجیل و بینالود", "size": 76},
        {"type": "bullets", "h": 100, "gap": 12, "desc_size": 25, "items": [
            {"icon": "wind", "title": [FB("باد از کجا می‌آید؟", GRN)], "desc": "گرمایش نامتعادل زمین توسط خورشید، باد می‌سازد"},
            {"icon": "bolt", "title": [FB("توربین چطور کار می‌کند؟", SKY)], "desc": "پره‌ها ژنراتور را می‌چرخانند؛ بدون دود و بدون سوخت"},
            {"icon": "sun", "title": [FB("چرا منجیل؟", AMBER_D)], "desc": "گلوگاه طبیعی باد در ایران — قدیمی‌ترین مزرعهٔ بادی کشور"},
            {"icon": "leaf", "title": [FB("چرا مهم است؟", ORG)], "desc": "هر کیلووات‌ساعت بادی یعنی CO2 ناشده در هوای شهرها"},
        ]},
    ],
    "note": "منبع: ظرفیت‌ها بر پایهٔ گزارش‌های ساتبا — نکتهٔ علمی: توربین‌های امروزی تا بیش از ۲۶۰ متر بلندی دارند",
}
