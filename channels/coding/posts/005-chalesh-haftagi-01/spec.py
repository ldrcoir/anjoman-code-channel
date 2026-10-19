# -*- coding: utf-8 -*-
import sys
import sys, pathlib
for _p in pathlib.Path(__file__).resolve().parents:
    if (_p / 'scripts' / 'poster_lib.py').exists():
        sys.path.insert(0, str(_p / 'scripts')); break
from poster_lib import (T, TF, FB, FG, VAR, FN, STR, CMT, NUM, OUT,
                        ACCENT, GREEN, ACCENT2, ORANGE)
SPEC = {'category_fa': 'رویداد', 'series_label': 'چالش هفتگی انجمن', 'title': 'چالش هفتگی ۱', 'title_size': 60, 'subtitle': 'ماشین\u200cحساب دوست\u200cداشتنی', 'clean_footer': 'چالش هفتگی | شمارهٔ ۱', 'sections': [{'type': 'code', 'lh': 50, 'lines': [{'segs': [('a', 'mono', 33, (122, 162, 247), False), (' = ', 'mono', 33, (220, 225, 235), False), ('12', 'mono', 33, (249, 174, 124), False)]}, {'segs': [('b', 'mono', 33, (122, 162, 247), False), (' = ', 'mono', 33, (220, 225, 235), False), ('5', 'mono', 33, (249, 174, 124), False)]}, {'segs': [('print', 'mono', 33, (187, 134, 252), False), ('(', 'mono', 33, (220, 225, 235), False), ('a + b', 'mono', 33, (122, 162, 247), False), (')', 'mono', 33, (220, 225, 235), False)]}, {'segs': [('print', 'mono', 33, (187, 134, 252), False), ('(', 'mono', 33, (220, 225, 235), False), ('a * b', 'mono', 33, (122, 162, 247), False), (')', 'mono', 33, (220, 225, 235), False)]}, {'sep': 'خروجی'}, {'segs': [('17', 'mono', 33, (252, 211, 77), False), ('   و   ', 'mono', 33, (120, 133, 155), False), ('60', 'mono', 33, (252, 211, 77), False)]}]}, {'type': 'bullets', 'h': 84, 'gap': 11, 'desc_size': 27, 'items': [{'title': [('مهلت', 'fa_bold', 31, (251, 146, 60), True)], 'desc': 'تا پنجشنبه همین هفته'}, {'title': [('ارسال', 'fa_bold', 31, (34, 211, 238), True)], 'desc': 'کدت را به کانال بفرست؛ جمعه مرور می\u200cکنیم'}]}], 'note': 'نام\u200cگذاری گویا و کامنت\u200cگذاری را فراموش نکن'}
