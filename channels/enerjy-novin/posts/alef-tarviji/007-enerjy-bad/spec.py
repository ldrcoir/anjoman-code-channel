# -*- coding: utf-8 -*-
"""۲. انرژی باد — دنبالهٔ پست «۱. انرژی خورشیدی» ادمین کانال (پیام ۹)
صحت علمی طبق بند ۳ شیوه‌نامه: ظرفیت منجیل از گزارش‌های ساتبا/اخبار رسمی.
رندر با موتور مستقل انرژی (enerjy_poster)."""
import sys, pathlib
for _p in pathlib.Path(__file__).resolve().parents:
    if (_p / 'scripts' / 'enerjy_poster.py').exists():
        sys.path.insert(0, str(_p / 'scripts')); break
from poster_lib import FB
from enerjy_poster import INK, GRN, AMBER_D, SKY, ORG

SPEC = {
    "category_fa": "ترویجی",   # فقط داخلی — هرگز روی عکس/متن نمی‌آید
    "series_label": "انرژی‌های نوین | دنبالهٔ ۲",
    "title": "انرژی باد",
    "title_size": 58,
    "subtitle": "باد در حرکت است؛ برق هم با آن می‌آید",
    "clean_footer": "معرفی انرژی باد",
    "sections": [
        {"type": "stat", "value": "۱۰۰+", "unit": "مگاوات",
         "label": "ظرفیت مزرعهٔ بادی منجیل — بزرگ‌ترین مزرعهٔ بادی ایران", "size": 76},
        {"type": "bullets", "h": 100, "gap": 12, "desc_size": 25, "items": [
            {"icon": "wind", "title": [FB("باد از کجا می‌آید؟", GRN)], "desc": "گرم‌شدن نابرابر زمین، هوا را به حرکت درمی‌آورد"},
            {"icon": "bolt", "title": [FB("توربین چه می‌کند؟", SKY)], "desc": "پروانه‌ها ژنراتور را می‌چرخانند — بدون هیچ سوختی"},
            {"icon": "leaf", "title": [FB("منجیل و خواف", AMBER_D)], "desc": "دو کوره‌وای ایران: دشت‌های بادخیز گیلان و خراسان"},
            {"icon": "sun", "title": [FB("چرا پاک است؟", ORG)], "desc": "نه سوخت می‌خواهد، نه دود تولید می‌کند"},
        ]},
    ],
    "note": "منبع: گزارش‌های ساتبا و اخبار رسمی انرژی — بازبینی آبان ۱۴۰۵",
}
