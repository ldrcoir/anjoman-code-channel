#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
fa_lint — چک‌کنندهٔ املای فارسی برای کپشن و متن پوسترها (گردش ۲۱)
بازخورد مالک (۱۴۰۵-۰۸-۰۲): «کتاب و کتاب‌خانه و غیره را حواست باشد»
→ قبل از هر انتشار، کپشن و متن با این فهرست سنجیده می‌شود؛
   اگر شکل نادرست (بدون نیم‌فاصله) پیدا شد، انتشار متوقف و خطا لاگ می‌شود.

مصرف مستقیم:
    python3 fa_lint.py <فایل متنی> [<فایل متنی> ...]
    خروجی کد بازگشت: 0 = سالم، 1 = غلط یافت شد

مصرف به‌عنوان ماژول:
    from fa_lint import lint_text, lint_problems
"""
import re
import sys

# واژه‌نامهٔ نادرست ← درست (بخش مهم: کلمه‌های «کتاب»، «خانه» و افعال «می»)
WRONG_RIGHT = {
    "کتاب خانه": "کتاب‌خانه",
    "کتاب خانه‌ها": "کتاب‌خانه‌ها",
    "می شود": "می‌شود",
    "نمی شود": "نمی‌شود",
    "می کند": "می‌کند",
    "نمی کند": "نمی‌کند",
    "می کنیم": "می‌کنیم",
    "نمی کنیم": "نمی‌کنیم",
    "می دانم": "می‌دانم",
    "نمی دانم": "نمی‌دانم",
    "می دانید": "می‌دانید",
    "می دهد": "می‌دهد",
    "نمی دهد": "نمی‌دهد",
    "می بینی": "می‌بینی",
    "می بیند": "می‌بیند",
    "بچه ها": "بچه‌ها",
    "خانه ها": "خانه‌ها",
}

# الگوهای با قاعده: «می» یا «نمی» جدا از فعل → باید نیم‌فاصله باشد
MI_PATTERN = re.compile(r"\b(ن?می)\s+(شود|شویم|شوید|کند|کنم|کنی|کنیم|کنید|کنند|دهد|دهم|دهی|دهیم|دهید|دهند|داند|دانم|دانی|دانیم|دانید|بینم|بینی|بیند|بینیم|بینید|گیرد|گیرم|گیری|گیریم|گیرید|آید|آیم|آیی|آییم|آیید|رود|روم|روی|رویم|روید)\b")
# «ها»ی جدا: واژهٔ مشخص + فاصله + ها (فقط پرتکرارهای امن تا هشدار کاذب ندهد)
HA_PATTERN = re.compile(r"\b(کتاب|کد|بازی|درس|مسیر|تیم|فایل|لینک|عکس|پرسش|پاسخ|تمرین|خانه)\s+ها\b")


def lint_problems(text):
    """فهرست مشکلات (خطاها) را برمی‌گرداند: [(نادرست، درستِ پیشنهادی، زمینه)]"""
    problems = []
    for wrong, right in WRONG_RIGHT.items():
        if wrong == right:
            continue
        for m in re.finditer(re.escape(wrong), text):
            s = max(0, m.start() - 25)
            ctx = text[s:m.end() + 25].replace("\n", " ")
            problems.append((wrong, right, ctx))
    for m in MI_PATTERN.finditer(text):
        s = max(0, m.start() - 25)
        ctx = text[s:m.end() + 25].replace("\n", " ")
        problems.append((m.group(0), m.group(1) + "‌" + m.group(2), ctx))
    for m in HA_PATTERN.finditer(text):
        s = max(0, m.start() - 25)
        ctx = text[s:m.end() + 25].replace("\n", " ")
        problems.append((m.group(0), m.group(1) + "‌ها", ctx))
    return problems


def lint_text(text, source=""):
    """چاپ خطاها؛ True یعنی مشکل پیدا شد"""
    problems = lint_problems(text)
    for wrong, right, ctx in problems:
        print(f"FA_LINT [{source}] «{wrong}» → «{right}» | …{ctx}…")
    return bool(problems)


def main():
    if len(sys.argv) < 2:
        print("مصرف: python3 fa_lint.py <فایل متنی> [<فایل متنی> ...]")
        return 2
    bad = False
    for path in sys.argv[1:]:
        try:
            text = open(path, encoding="utf-8").read()
        except OSError as e:
            print(f"FA_LINT: خواندن {path} ممکن نشد: {e}")
            bad = True
            continue
        if lint_text(text, source=path):
            bad = True
    if bad:
        print("FA_LINT: نتیجه — نیم‌فاصلهٔ جاافتاده پیدا شد؛ اصلاح شود.")
        return 1
    print("FA_LINT: سالم ✓")
    return 0


if __name__ == "__main__":
    sys.exit(main())
