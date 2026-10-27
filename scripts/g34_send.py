#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""گردش ۳۴ — انتشار دو پست با اجازهٔ صریح مالک:
① کانال کدنویسی: «بازی‌های مرورگری انجمن» (کاور اسکرین‌شات واقعی دو بازی + سه لینک کوتاه)
② کانال انرژی: «۳. انرژی زمین‌گرمایی» (دنبالهٔ خورشیدی/بادی)
قواعد رعایت‌شده: قاب کپشن (caption_frame) + سقف ۱۰۲۴ + هشتگ ≥۳ + گارد دوبارسردن
(پایش فید پس از ارسال؛ کپی تکراری حذف) + پروب بی‌اثر زنده‌بودن + آرشیو متا.
هیچ forwardMessage — هیچ حذف دیگری."""
import json, os, sys, time
from pathlib import Path
import requests

REPO = Path("/home/z/my-project/anjoman-code-bot")
sys.path.insert(0, str(REPO / "scripts"))

env = {}
for line in (REPO / ".env").read_text().splitlines():
    if "=" in line and not line.strip().startswith("#"):
        k, v = line.split("=", 1); env[k.strip()] = v.strip()

from caption_frame import apply_caption_frame

CFG_CODING = json.loads((REPO / "channels/coding/channel.json").read_text(encoding="utf-8"))
CFG_ENERJY = json.loads((REPO / "channels/enerjy-novin/channel.json").read_text(encoding="utf-8"))

BODY_GAMES = """🎮 بازی‌های مرورگری انجمن — یک کلیک، بدون دانلود و نصب!

🤖 «ربات مسیریاب» — بازی تازهٔ انجمن:
به ربات برنامه بده؛ برنامه‌ات دستوربه‌دستور اجرا می‌شود و ربات خانه‌به‌خانه راه می‌افتد. مسیر اشتباه بود؟ برنامه را پاک کن، از اول بنویس و به پرچم برس! زمین شطرنجی است — فاصلهٔ پرچم را بشمار و درست برنامه‌ریزی کن. سه مرحله دارد؛ هر مرحله سخت‌تر.

🐍 «رفیق مار» — روون‌تر از قبل:
سیب‌ها را بخور تا مارت بلند شود. زمین شطرنجی است و بازی دقیقاً به همان سمتی راه می‌افتد که اولین دکمه را می‌زنی؛ دیگر چپ و راست قاطی نمی‌شود!

▶ روش بازی: فقط لینک‌ها را در مرورگر باز کن:
🗂 همهٔ بازی‌ها: https://clikr.ir/7qhpHt
🤖 ربات مسیریاب: https://clikr.ir/rxp3tH
🐍 رفیق مار: https://clikr.ir/86iTfY

کد بازی‌ها باز است؛ فایل html. را در هر ویرایشگر متنی باز کن و ببین چطور ساخته شده. دست‌سازِ بچه‌های انجمن‌اند — بازی بعدی، ایدهٔ شماست!

#انجمن_کدنویسی #فعالیت_خلاقانه #بازی_مرورگری #ربات_مسیریاب #رفیق_مار"""

BODY_ENERJY = """🌡 ۳. انرژی زمین‌گرمایی

زمین فقط زیر پای ما نیست؛ در دلش یک کورهٔ بزرگ هم هست. سنگ‌های داغ درون زمین آب را به بخار تبدیل می‌کنند و همین بخار، توربین و ژنراتور را می‌چرخاند و برق تولید می‌کند — بدون هیچ سوخت و دودی. و برخلاف خورشید و باد، زمین همیشه گرم است؛ نیروگاه زمین‌گرمایی شب و روز نمی‌شناسد و آرام و پیوسته برق می‌دهد.

ایران هم روی این گنج پا گذاشته است: نخستین میدان زمین‌گرمایی کشور در دامنهٔ سبلان، نزدیک مشکین‌شهر استان اردبیل، در حال توسعه است. نمونهٔ جهانیِ ماندگارش ایسلند است؛ جایی که حدود ۹۰ درصد خانه‌ها با گرمای زمین گرم می‌شوند.

منبع: گزارش‌های ساتبا و منابع رسمی انرژی — بازبینی آبان ۱۴۰۵

#انرژی_زمین_گرمایی #انرژی_پاک #انرژی_های_نوین #انرژی_پژوهان #دانستنی_انرژی"""

JOBS = [
    {
        "name": "coding-games",
        "token": env["BALE_TOKEN"].strip(),
        "cfg": CFG_CODING,
        "chat_id": 5100057643,
        "body": BODY_GAMES,
        "photo": REPO / "channels/coding/posts/b4-khollagheh/026-baziha-ye-webi/post.png",
        "folder": REPO / "channels/coding/posts/b4-khollagheh/026-baziha-ye-webi",
    },
    {
        "name": "enerjy-germaei",
        "token": env["ENERJY_TOKEN"].strip(),
        "cfg": CFG_ENERJY,
        "chat_id": 4602076598,
        "body": BODY_ENERJY,
        "photo": REPO / "channels/enerjy-novin/posts/alef-tarviji/009-enerjy-zamin-germaei/post.png",
        "folder": REPO / "channels/enerjy-novin/posts/alef-tarviji/009-enerjy-zamin-germaei",
    },
]

def call_json(token, method, payload):
    r = requests.post(f"https://tapi.bale.ai/bot{token}/{method}", json=payload, timeout=25)
    try:
        return r.json()
    except Exception:
        return {"ok": False, "err": f"http {r.status_code}", "body": r.text[:200]}

def drain_feed(token, timeout=0):
    offset, ups = 0, []
    for _ in range(30):
        res = call_json(token, "getUpdates", {"offset": offset, "limit": 100, "timeout": timeout})
        if not res.get("ok"):
            break
        batch = res.get("result", [])
        if not batch:
            break
        ups.extend(batch)
        offset = max(u["update_id"] for u in batch) + 1
        if len(batch) < 100:
            break
    return ups

def feed_channel_posts(token):
    out = []
    for u in drain_feed(token):
        m = u.get("channel_post") or u.get("message")
        if m:
            out.append(m)
    return out

def send_photo(job, caption):
    token, chat_id = job["token"], job["chat_id"]
    with open(job["photo"], "rb") as f:
        r = requests.post(
            f"https://tapi.bale.ai/bot{token}/sendPhoto",
            data={"chat_id": str(chat_id), "caption": caption},
            files={"photo": (job["photo"].name, f, "image/png")},
            timeout=120)
    try:
        data = r.json()
    except Exception:
        data = {"ok": False, "err": f"http {r.status_code}", "body": r.text[:300]}
    if data.get("ok"):
        return data
    s = json.dumps(data, ensure_ascii=False)
    if "429" in s or "Too Many" in s:  # ۴۲۹ → ۳۵ ثانیه سرد و تلاش دوباره (الگوی گردش‌های قبل)
        print("  ⏳ 429 — ۳۵ ثانیه صبر…")
        time.sleep(35)
        return send_photo(job, caption)
    return data

def main():
    results = {}
    for job in JOBS:
        print(f"\n=== {job['name']} ===")
        caption = apply_caption_frame(job["body"], job["cfg"])
        n_tags = caption.count("#")
        assert n_tags >= 3, f"هشتگ کمتر از ۳: {n_tags}"
        assert len(caption) <= 1024, f"کپشن بلند: {len(caption)}"
        print(f"کپشن: {len(caption)} نویسه، {n_tags} هشتگ ✓")

        # فید پیش از ارسال — نقطهٔ صفر برای گارد دوبارسردن
        before = [m.get("message_id") for m in feed_channel_posts(job["token"])]
        print("فید پیش از ارسال:", sorted(before)[-6:] if before else "خالی")

        res = send_photo(job, caption)
        if not res.get("ok"):
            print("❌ ارسال ناموفق:", json.dumps(res, ensure_ascii=False)[:400])
            results[job["name"]] = {"ok": False, "err": res}
            continue
        mid = res["result"]["message_id"]
        print("✅ sendPhoto ok — message_id =", mid)

        # پایش دوبارسردن بله: ۲ دور فید (۹ ثانیه و ۲۸ ثانیه)
        dupes = []
        for wait in (9, 19):
            time.sleep(wait)
            for m in feed_channel_posts(job["token"]):
                mid2 = m.get("message_id")
                cap = (m.get("caption") or m.get("text") or "")
                if mid2 and mid2 != mid and cap[:40] == caption[:40] and mid2 not in dupes:
                    d = call_json(job["token"], "deleteMessage", {"chat_id": job["chat_id"], "message_id": mid2})
                    print(f"  🧹 کپی دوبارسردن {mid2} → حذف:", d.get("ok"))
                    dupes.append(mid2)

        # پروب بی‌اثر زنده‌بودن (فقط editMessageReplyMarkup — قاعدهٔ گردش ۳۳)
        probe = call_json(job["token"], "editMessageReplyMarkup",
                          {"chat_id": job["chat_id"], "message_id": mid})
        alive = bool(probe.get("ok")) or "not found" not in json.dumps(probe).lower()
        print("پروب پیام:", "زنده ✓" if alive else json.dumps(probe, ensure_ascii=False)[:150])

        final_caption = caption if not dupes else caption
        (job["folder"] / "caption.txt").write_text(caption, encoding="utf-8")
        results[job["name"]] = {
            "ok": True, "message_id": mid, "alive": alive,
            "dupes_deleted": dupes, "caption_chars": len(caption), "hashtags": n_tags,
        }
        time.sleep(3)

    out = Path("/home/z/my-project/tool-results/g34_send.json")
    out.write_text(json.dumps(results, ensure_ascii=False, indent=1), encoding="utf-8")
    print("\nsaved:", out)
    for k, v in results.items():
        print(k, "->", json.dumps(v, ensure_ascii=False)[:200])

if __name__ == "__main__":
    main()
