# -*- coding: utf-8 -*-
"""درس ۱۴ — آموزش Rust از صفر: بردار (Vec) و پروژهٔ کوچک «فهرست کارها»
گردش ۶۷ — «درس ۱۳ و ۱۴ را آماده کن ببینم» — فقط پیش‌نویس، بدون انتشار"""
import sys, pathlib
for _p in pathlib.Path(__file__).resolve().parents:
    if (_p / 'scripts' / 'poster_lib.py').exists():
        sys.path.insert(0, str(_p / 'scripts')); break
from poster_lib import T, TF, FB, FG, VAR, FN, STR, CMT, NUM, OUT, ACCENT, GREEN, ACCENT2, ORANGE

SPEC = {
    "category_fa": "آموزشی",   # فقط داخلی — هرگز روی عکس/متن نمی‌آید
    "series_label": "آموزش Rust از صفر",
    "lesson_no": 14,
    "title": "بردار (Vec)",
    "title_size": 56,
    "subtitle": "لیستی که نفس می‌کشد + پروژهٔ کوچک: فهرست کارها",
    "clean_footer": "آموزش Rust از صفر",
    "sections": [
        {"type": "code", "name": "src/main.rs", "lh": 31, "lines": [
            {"segs": [T("struct", FN), T(" Kar { onvan: ", FG), T("String", FN), T(", anjamShod: ", FG), T("bool", FN), T(" }", FG)]},
            {"segs": [T("", FG)]},
            {"segs": [T("fn", FN), T(" main() {", FG)]},
            {"segs": [T("    let mut", FN), T(" list: Vec<Kar> = Vec::new();", FG)]},
            {"segs": [T("    list.push(Kar { onvan: String::from(", FG), T("\"tamrin\"", STR), T("),", FG)]},
            {"segs": [T("                    anjamShod: ", FG), T("false", FN), T(" });", FG)]},
            {"segs": [T("    println!(", FG), T("\"tedad: {}\"", STR), T(", list.len());", FG)]},
            {"segs": [T("    for", FN), T(" kar ", FG), T("in", FN), T(" list {", FG)]},
            {"segs": [T("        let", FN), T(" alamat = ", FG), T("if", FN), T(" kar.anjamShod { ", FG), T("\"[x]\"", STR), T(" } ", FG), T("else", FN), T(" { ", FG), T("\"[ ]\"", STR), T(" };", FG)]},
            {"segs": [T("        println!(", FG), T("\"{} {}\"", STR), T(", alamat, kar.onvan);", FG)]},
            {"segs": [T("    }", FG)]},
            {"segs": [T("}", FG)]},
            {"sep": "خروجی"},
            {"segs": [T("tedad: 1", OUT)]},
            {"segs": [T("[ ] tamrin", OUT)]},
        ]},
        {"type": "bullets", "h": 80, "gap": 10, "desc_size": 23, "items": [
            {"title": [FB("mut", ACCENT)], "desc": "جعبهٔ تغییرپذیر — بردار وسط برنامه می‌تواند بزرگ شود"},
            {"title": [T("list.push(kar)", ORANGE, 26)], "desc": "عضو تازه به انتهای بردار اضافه می‌شود؛ len طولش را می‌گوید"},
        ]},
    ],
    "note": "پروژهٔ آخر مسیر: «فهرست کارها» با struct + Vec — اگر همین برنامهٔ کوچک را خودت بنویسی، یعنی از درس ۱ تا ۱۴ را واقعاً یاد گرفته‌ای!",
}
