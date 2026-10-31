#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""گردش ۲۴ — ممیزی و اصلاح هشتگ آیتم‌های صف کدنویسی طبق docs/rahnamaye-tag.md
(دستور مالک: «اگر جای هشتگ‌گذاری پستی اشتباه بود خودت ادیت کن»)
- هشتگ آیتمِ جاافتاده اضافه می‌شود؛ هشتگ نامعتبر (#نشریه_الکترونیکی) جایگزین می‌شود.
- سقف ۵ هشتگ (قاعدهٔ راهنمای تگ) با حذف کم‌اهمیت‌ترین‌ها رعایت می‌شود."""
import re
from pathlib import Path

Q = Path("/home/z/my-project/anjoman-code-bot/channels/coding/content/queue")

# (مسیر، هشتگ آیتم الزامی، حذف‌شدنی‌ها در صورت سرریز)
FIXES = [
    ("b1-amoozesh/rust-dars-05-moteghayer", "#آموزش_راست", ["#کدنویسی"]),
    ("b2-nashriyeh/aband-1405", "#نشریه_انجمن",
     ["#نشریه_الکترونیکی", "#اخبار_فناوری", "#Rust", "#دانش_آموز"]),
    ("b5-moarefi-manabe/10-manabe-rasmi", "#معرفی_کتاب", ["#جشنواره_علمی"]),
    ("b7-mansabet/abane13-roz-daneshamooz", "#مناسبت", ["#کدنویسی"]),
    ("b7-mansabet/abane24-roz-ketab", "#مناسبت", ["#کتاب_خوانی"]),
    ("b7-mansabet/azare16-roz-daneshjoo", "#مناسبت", ["#کدنویسی"]),
    ("b7-mansabet/fatemeh-sa-1448", "#مناسبت", []),
    ("b7-mansabet/shabe-yalda", "#مناسبت", []),
]

for rel, item_tag, droppable in FIXES:
    f = Q / rel / "caption.txt"
    cap = f.read_text(encoding="utf-8").strip()
    tags = re.findall(r"#\S+", cap)
    if item_tag in tags:
        print(f"• {rel}: هشتگ آیتم از قبل هست — رد شد")
        continue
    # هشتگ‌های نامعتبر/دروغ را اول حذف کن
    for bad in droppable:
        if bad in tags:
            tags.remove(bad)
    tags.insert(1, item_tag)   # بعد از هشتگ پایه
    while len(tags) > 5:
        tags.pop()
    # بازسازی بلوک هشتگ: خطوط هشتگ‌دار کپشن با فهرست نو جایگزین می‌شود
    lines = cap.splitlines()
    kept, replaced = [], False
    for ln in lines:
        s = ln.strip()
        if s.startswith("#"):
            if not replaced:
                kept.append(" ".join(tags))
                replaced = True
            continue
        kept.append(ln)
    if not replaced:
        kept += ["", " ".join(tags)]
    f.write_text("\n".join(kept).strip() + "\n", encoding="utf-8")
    print(f"✓ {rel}: {item_tag} اضافه شد ← {' '.join(tags)}")
