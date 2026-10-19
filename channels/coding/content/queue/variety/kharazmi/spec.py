# -*- coding: utf-8 -*-
import sys
import sys, pathlib
for _p in pathlib.Path(__file__).resolve().parents:
    if (_p / 'scripts' / 'poster_lib.py').exists():
        sys.path.insert(0, str(_p / 'scripts')); break
from poster_lib import (T, TF, FB, FG, VAR, FN, STR, CMT, NUM, OUT,
                        ACCENT, GREEN, ACCENT2, ORANGE)
SPEC = {'category_fa': 'معرفی', 'series_label': 'چهره\u200cهای دنیای کامپیوتر', 'title': 'خوارزمی؛ پدر الگوریتم', 'title_size': 54, 'subtitle': 'از نام یک دانشمند ایرانی تا واژهٔ Algorithm', 'clean_footer': 'چهره\u200cهای دنیای کامپیوتر', 'sections': [{'type': 'bullets', 'h': 88, 'gap': 12, 'desc_size': 26, 'items': [{'title': [('ابوجعفر محمد بن موسی خوارزمی', 'fa_bold', 31, (34, 211, 238), True)], 'desc': 'ریاضی\u200cدان ایرانی سدهٔ نهم میلادی'}, {'title': [('Algorithm', 'fa_bold', 31, (52, 211, 153), True)], 'desc': 'واژهٔ الگوریتم از نام او ساخته شد'}, {'title': [('کتاب الجبر', 'fa_bold', 31, (167, 139, 250), True)], 'desc': 'منشأ واژهٔ جبر در ریاضیات جهان'}]}, {'type': 'code', 'lines': [{'segs': [('algorithm', 'mono', 33, (187, 134, 252), False), (' = ', 'mono', 33, (220, 225, 235), False), ('"', 'mono', 33, (166, 214, 167), False), ('مسیر گام\u200cبه\u200cگام حل مسئله', 'fa_med', 33, (166, 214, 167), True), ('"', 'mono', 33, (166, 214, 167), False)]}, {'segs': [('# ', 'mono', 33, (120, 133, 155), False), ('هر برنامهٔ بزرگ، الگوریتم\u200cهای کوچک دارد', 'fa_med', 30, (120, 133, 155), True)]}]}], 'note': 'الگوریتم یعنی دستورِ قدم\u200cبه\u200cقدم؛ همان چیزی که در سری پایتون یاد می\u200cگیری'}
