#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""گردش ۲۳ — اصلاح زندهٔ پست‌های کانال کدنویسی:
۱) هشتگ آیتمِ درست طبق docs/rahnamaye-tag.md (دستور مالک: «اگر پستی هشتگ مناسب
   نداشت خودت اصلاح کن»)
۲) قاب خوشگل دور «آی‌دی کانال + هشتگ‌ها» (دستور مالک: «یک قاب خوشگل درست کن»)
پست‌های پایتونی/عرشیا و پست دستی مالک دست نمی‌خورند (تقسیم کار ثابت).
"""
import sys, json
from pathlib import Path
sys.path.insert(0, "/home/z/my-project/anjoman-code-bot/scripts")
sys.path.insert(0, "/home/z/my-project/scripts")
from bale_api import api
from caption_frame import apply_caption_frame

REPO = Path("/home/z/my-project/anjoman-code-bot")
cfg = json.loads((REPO / "channels/coding/channel.json").read_text(encoding="utf-8"))
CHAT = cfg["chat_id"]

# (پوشهٔ آرشیو، message_id، هشتگ‌های نهایی طبق جدول راهنمای تگ)
JOBS = [
    ("006-rust-dars-01-chist",        18, ["#انجمن_کدنویسی", "#آموزش_راست", "#Rust", "#برنامه_نویسی"]),
    ("007-code-va-zendegi",           20, ["#انجمن_کدنویسی", "#فعالیت_خلاقانه", "#ایده_کدنویسی", "#زندگی_و_کد"]),
    ("008-mehr-1405",                 22, ["#انجمن_کدنویسی", "#نشریه_انجمن", "#مهر_۱۴۰۵", "#کدنویسی", "#برنامه_نویسی"]),
    ("009-rafiq-e-mar",               46, ["#انجمن_کدنویسی", "#فعالیت_خلاقانه", "#بازی_مرورگری", "#رفیق_مار"]),
    ("010-rust-dars-02-nasb-avvalin", 27, ["#انجمن_کدنویسی", "#آموزش_راست", "#Rust", "#برنامه_نویسی"]),
    ("011-moarefi-ketab-rust",        41, ["#انجمن_کدنویسی", "#معرفی_کتاب", "#رست", "#Rust", "#برنامه_نویسی"]),
    ("012-rust-dars-03-println-matn", 42, ["#انجمن_کدنویسی", "#آموزش_راست", "#Rust", "#برنامه_نویسی"]),
    ("013-kharazmi",                  53, ["#انجمن_کدنویسی", "#معرفی_مشاهیر", "#خوارزمی", "#الگوریتم"]),
    ("014-rust-dars-04-commentha",    54, ["#انجمن_کدنویسی", "#آموزش_راست", "#Rust", "#برنامه_نویسی"]),
    ("015-fatemieh-aval-1448",        57, ["#انجمن_کدنویسی", "#مناسبت", "#ایام_فاطمیه", "#شهادت_حضرت_فاطمه"]),
]

results = []
for slug, mid, tags in JOBS:
    cap_file = REPO / "channels/coding/posts" / slug / "caption.txt"
    old = cap_file.read_text(encoding="utf-8").strip()
    new = apply_caption_frame(old, cfg, hashtags_override=tags)
    status = {"post": slug, "msg": mid, "len_old": len(old), "len_new": len(new)}
    if len(new) > 1024:
        status["result"] = "SKIP — بلندتر از سقف ۱۰۲۴"
        results.append(status); print(status); continue
    r = api("BALE_TOKEN", "editMessageCaption",
            {"chat_id": CHAT, "message_id": mid, "caption": new})
    if r.get("ok"):
        cap_file.write_text(new + "\n", encoding="utf-8")
        status["result"] = "OK — کپشن زنده + آرشیو همگام"
    else:
        status["result"] = f"FAIL — {json.dumps(r, ensure_ascii=False)[:160]}"
    results.append(status); print(json.dumps(status, ensure_ascii=False))

ok = sum(1 for r in results if str(r.get("result", "")).startswith("OK"))
print(f"\nجمع‌بندی: {ok}/{len(JOBS)} پست ویرایش شد")
