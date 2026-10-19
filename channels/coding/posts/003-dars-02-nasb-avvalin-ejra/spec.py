# -*- coding: utf-8 -*-
import sys
import sys, pathlib
for _p in pathlib.Path(__file__).resolve().parents:
    if (_p / 'scripts' / 'poster_lib.py').exists():
        sys.path.insert(0, str(_p / 'scripts')); break
from poster_lib import (T, TF, FB, FG, VAR, FN, STR, CMT, NUM, OUT,
                        ACCENT, GREEN, ACCENT2, ORANGE)
SPEC = {'category_fa': 'آموزشی', 'series_label': 'آموزش پایتون از صفر', 'lesson_no': 2, 'title': 'نصب پایتون و اولین اجرا', 'title_size': 54, 'subtitle': 'سه قدم ساده تا اولین برنامه', 'clean_footer': 'آموزش پایتون از صفر | درس ۲', 'sections': [{'type': 'bullets', 'h': 84, 'gap': 11, 'desc_size': 27, 'items': [{'title': [('قدم ۱ — دانلود', 'fa_bold', 31, (34, 211, 238), True)], 'desc': 'از سایت رسمی python.org آخرین نسخه'}, {'title': [('قدم ۲ — نصب', 'fa_bold', 31, (52, 211, 153), True)], 'desc': 'تیک Add to PATH را فراموش نکن'}, {'title': [('قدم ۳ — اجرا', 'fa_bold', 31, (167, 139, 250), True)], 'desc': 'فایل main.py را با python اجرا کن'}]}, {'type': 'code', 'name': 'main.py', 'lines': [{'segs': [('print', 'mono', 33, (187, 134, 252), False), ('(', 'mono', 33, (220, 225, 235), False), ('"', 'mono', 33, (166, 214, 167), False), ('سلام دنیا', 'fa_med', 33, (166, 214, 167), True), ('"', 'mono', 33, (166, 214, 167), False), (')', 'mono', 33, (220, 225, 235), False)]}, {'sep': 'خروجی برنامه'}, {'segs': [('سلام دنیا', 'fa_med', 33, (252, 211, 77), True)]}]}], 'note': 'اگر خطا دیدی نگران نشو؛ درس ۵ کامل دربارهٔ خطاها حرف می\u200cزنیم'}
