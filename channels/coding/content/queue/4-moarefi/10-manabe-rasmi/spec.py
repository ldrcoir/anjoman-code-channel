# -*- coding: utf-8 -*-
import sys
import sys, pathlib
for _p in pathlib.Path(__file__).resolve().parents:
    if (_p / 'scripts' / 'poster_lib.py').exists():
        sys.path.insert(0, str(_p / 'scripts')); break
from poster_lib import (T, TF, FB, FG, VAR, FN, STR, CMT, NUM, OUT,
                        ACCENT, GREEN, ACCENT2, ORANGE)
SPEC = {'category_fa': 'اطلاعیه', 'series_label': 'اطلاع\u200cرسانی انجمن کدنویسی', 'title': 'منابع رسمی خبر', 'title_size': 58, 'subtitle': 'تازه\u200cهای مسابقات را از کجا بفهمیم؟', 'clean_footer': 'منابع رسمی خبر', 'sections': [{'type': 'bullets', 'h': 100, 'gap': 14, 'desc_size': 26, 'items': [{'title': [('پژوهش\u200cسرای منطقه', 'fa_bold', 31, (34, 211, 238), True)], 'desc': 'اطلاعیه\u200cها و شیوه\u200cنامهٔ رسمی'}, {'title': [('سامانهٔ ثبت\u200cنام', 'fa_bold', 31, (52, 211, 153), True)], 'desc': 'لینک به\u200cمحض اعلام رسمی در همین کانال'}, {'title': [('کانال انجمن کدنویسی', 'fa_bold', 31, (167, 139, 250), True)], 'desc': 'تقویم، یادآوری مهلت\u200cها و آموزش پایه'}, {'title': [('اصول ما', 'fa_bold', 31, (251, 146, 60), True)], 'desc': 'دقت، صبر و اعتماد فقط به منبع رسمی'}]}], 'note': 'هر خبر مهم، با هشتگ اطلاعیه این\u200cجا اعلام می\u200cشود'}
