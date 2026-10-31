#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""گردش ۳۵ — انتشار با اجازهٔ صریح مالک (پیام همین امروز):
«پست کانال انرژی‌های نوین را قرار بده» + «نسخهٔ pdf نشریهٔ انجمن کامپیوتر را نزدیک خود پوسترش
(در صورت امکان) قرار بده» + «مطمئن شو نسخهٔ آنلاین و دانلودی بازی‌ها دقیقاً یکی باشد»

کانال کدنویسی (ترتیب مهم — پوستر و PDF پشت‌سرهم):
  A) پوستر مجلهٔ شمارهٔ ۲ (آبان ۱۴۰۵)  — از صف آمادهٔ بند ۲
  B) فایل PDF مجله — بلافاصله بعد از پوستر («نزدیک خود پوسترش»)
  C) فایل دانلودی رفیق مار (rafiq-mar.html) — بایت‌به‌بایت برابر نسخهٔ Pages
  D) فایل دانلودی ربات مسیریاب (robot-masiryab.html) — بایت‌به‌بایت برابر نسخهٔ Pages
کانال انرژی:
  E) «۴. انرژی آب (برق‌آبی)» — دنبالهٔ خورشیدی/بادی/زمین‌گرمایی — موضوع تازه، سابقهٔ کانال ندارد

قواعد: قاب کپشن + سقف ۱۰۲۴ + هشتگ ≥۳ طبق rahnamaye-tag + گارد دوبارسردن (پایش فید،
حذف کپی) + پروب بی‌اثر editMessageReplyMarkup + هیچ forwardMessage + 429 → ۳۵ ثانیه."""
import json, sys, time
from pathlib import Path
import requests

REPO = Path("/home/z/my-project/anjoman-code-bot")
sys.path.insert(0, str(REPO / "scripts"))
from caption_frame import apply_caption_frame

env = {}
for line in (REPO / ".env").read_text().splitlines():
    if "=" in line and not line.strip().startswith("#"):
        k, v = line.split("=", 1); env[k.strip()] = v.strip()

CFG_CODING = json.loads((REPO / "channels/coding/channel.json").read_text(encoding="utf-8"))
CFG_ENERJY = json.loads((REPO / "channels/enerjy-novin/channel.json").read_text(encoding="utf-8"))

CAP_ABAN_POSTER = (REPO / "channels/coding/content/queue/b2-nashriyeh/aband-1405/caption.txt").read_text(encoding="utf-8")

CAP_ABAN_PDF = """📄 نسخهٔ کامل PDF | مجلهٔ انجمن کدنویسی — شمارهٔ ۲ (آبان ۱۴۰۵)

در این شماره: سرمقالهٔ دبیر، اخبار هوش مصنوعی، اخبار فناوری و کدنویسی، معرفی زبان C، معرفی نرم‌افزار Git، ترفند ماه و تمرین شمارهٔ ۲.
برای مطالعه فایل را باز کن؛ روی گوشی و رایانه خواناست و برای چاپ هم آماده است.

#انجمن_کدنویسی #مجله_کدنویسی #نشریه_انجمن #آبان_۱۴۰۵"""

CAP_MAR_DOC = """🐍 نسخهٔ دانلودی «رفیق مار» — دقیقاً همان نسخهٔ آنلاین

همین فایل را دانلود کن و با مرورگر (کروم یا فایرفاکس) باز کن؛ همان بازیِ آنلاین است، بایت‌به‌بایت یکی: همان زمین شطرنجی، همان شروعِ درست به سمتِ اولین دکمه، همان روانی. هیچ نصبی لازم نیست و حتی بدون اینترنت هم بازی می‌کند.
نسخهٔ آنلاین برای مقایسه: https://clikr.ir/86iTfY
کنجکاوی؟ فایل را با یک ویرایشگر متنی باز کن و کدش را ببین — دست‌سازِ بچه‌های انجمن.

#انجمن_کدنویسی #فعالیت_خلاقانه #بازی_مرورگری #رفیق_مار"""

CAP_MASIRYAB_DOC = """🤖 نسخهٔ دانلودی «ربات مسیریاب» — دقیقاً همان نسخهٔ آنلاین

دانلود کن، با مرورگر باز کن؛ همان بازیِ وب است با همان سه مرحله، زمین شطرنجی و برنامه‌سازی دستوربه‌دستور — نه یک نویسه فرق دارد. فایل تک‌صفحه‌ای است؛ هرجا ببری، بازی با توست.
نسخهٔ آنلاین برای مقایسه: https://clikr.ir/rxp3tH
کدش را می‌خواهی؟ با ویرایشگر متنی باز کن — دست‌سازِ بچه‌های انجمن.

#انجمن_کدنویسی #فعالیت_خلاقانه #بازی_مرورگری #ربات_مسیریاب"""

CAP_ENERJY_AB = """💧 ۴. انرژی آب (برق‌آبی)

آبِ ایستاده پشت سد فقط منظر نیست؛ انرژیِ ذخیره‌شده است. وقتی دریچه باز می‌شود، آب با فشار به پایین می‌ریزد و توربین را می‌چرخاند؛ ژنراتور هم همان حرکت را به برق تبدیل می‌کند. سد مثل یک باتری غول‌آساست: باران و برف دوباره پُرش می‌کنند و این چرخه هیچ‌وقت تمام نمی‌شود.

برق‌آبی یک مزیت مهم هم دارد: چند ثانیه بعد از فرمان، توربین می‌چرخد؛ برای همین در ساعات اوج مصرف کنار سایر نیروگاه‌ها می‌آید و شبکه را تعادل نگه می‌دارد. نمونهٔ جهانی‌اش نروژ است که حدود ۹۰ درصد برقش را از آب می‌گیرد؛ ایران هم با کارون ۳ در مسجدسلیمان بزرگ‌ترین نیروگاه برق‌آبی کشور را دارد (۲٬۲۸۰ مگاوات).

منبع: گزارش‌های ساتبا و وزارت نیرو — بازبینی آبان ۱۴۰۵

#انرژی_آب #انرژی_پاک #انرژی_های_نوین #انرژی_پژوهان #دانستنی_انرژی"""

JOBS = {
    "aban-poster": {
        "run": "coding", "kind": "photo",
        "token": env["BALE_TOKEN"].strip(), "cfg": CFG_CODING, "chat_id": 5100057643,
        "body": CAP_ABAN_POSTER,
        "file": REPO / "channels/coding/posts/b2-nashriyeh/027-aban-1405/post.png",
        "folder": REPO / "channels/coding/posts/b2-nashriyeh/027-aban-1405",
    },
    "aban-pdf": {
        "run": "coding", "kind": "doc",
        "token": env["BALE_TOKEN"].strip(), "cfg": CFG_CODING, "chat_id": 5100057643,
        "body": CAP_ABAN_PDF,
        "file": REPO / "channels/coding/posts/b2-nashriyeh/027-aban-1405/magazine.pdf",
        "folder": REPO / "channels/coding/posts/b2-nashriyeh/027-aban-1405",
    },
    "mar-doc": {
        "run": "coding", "kind": "doc",
        "token": env["BALE_TOKEN"].strip(), "cfg": CFG_CODING, "chat_id": 5100057643,
        "body": CAP_MAR_DOC,
        "file": REPO / "channels/coding/posts/b4-khollagheh/028-fail-mar-downloadi/rafiq-mar.html",
        "folder": REPO / "channels/coding/posts/b4-khollagheh/028-fail-mar-downloadi",
    },
    "masiryab-doc": {
        "run": "coding", "kind": "doc",
        "token": env["BALE_TOKEN"].strip(), "cfg": CFG_CODING, "chat_id": 5100057643,
        "body": CAP_MASIRYAB_DOC,
        "file": REPO / "channels/coding/posts/b4-khollagheh/029-fail-masiryab-downloadi/robot-masiryab.html",
        "folder": REPO / "channels/coding/posts/b4-khollagheh/029-fail-masiryab-downloadi",
    },
    "enerjy-ab": {
        "run": "enerjy", "kind": "photo",
        "token": env["ENERJY_TOKEN"].strip(), "cfg": CFG_ENERJY, "chat_id": 4602076598,
        "body": CAP_ENERJY_AB,
        "file": REPO / "channels/enerjy-novin/posts/alef-tarviji/010-enerjy-ab/post.png",
        "folder": REPO / "channels/enerjy-novin/posts/alef-tarviji/010-enerjy-ab",
    },
}

def call_json(token, method, payload):
    r = requests.post(f"https://tapi.bale.ai/bot{token}/{method}", json=payload, timeout=25)
    try:
        return r.json()
    except Exception:
        return {"ok": False, "err": f"http {r.status_code}", "body": r.text[:200]}

def drain_feed(token):
    offset, ups = 0, []
    for _ in range(30):
        res = call_json(token, "getUpdates", {"offset": offset, "limit": 100, "timeout": 0})
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

def send_media(job, caption):
    token, chat_id = job["token"], job["chat_id"]
    field = "photo" if job["kind"] == "photo" else "document"
    with open(job["file"], "rb") as f:
        r = requests.post(
            f"https://tapi.bale.ai/bot{token}/{'sendPhoto' if job['kind'] == 'photo' else 'sendDocument'}",
            data={"chat_id": str(chat_id), "caption": caption},
            files={field: (job["file"].name, f)},
            timeout=180)
    try:
        data = r.json()
    except Exception:
        data = {"ok": False, "err": f"http {r.status_code}", "body": r.text[:300]}
    if data.get("ok"):
        return data
    s = json.dumps(data, ensure_ascii=False)
    if "429" in s or "Too Many" in s:  # ۴۲۹ → ۳۵ ثانیه سرد و تلاش دوباره
        print("  ⏳ 429 — ۳۵ ثانیه صبر…")
        time.sleep(35)
        return send_media(job, caption)
    return data

def run_job(name):
    job = JOBS[name]
    print(f"\n=== {name} ({job['kind']}) ===")
    caption = apply_caption_frame(job["body"], job["cfg"])
    n_tags = caption.count("#")
    assert n_tags >= 3, f"هشتگ کمتر از ۳: {n_tags}"
    assert len(caption) <= 1024, f"کپشن بلند: {len(caption)}"
    print(f"کپشن: {len(caption)} نویسه، {n_tags} هشتگ ✓")

    before = [m.get("message_id") for m in feed_channel_posts(job["token"])]
    print("فید پیش از ارسال:", sorted([b for b in before if b])[-6:] if before else "خالی")

    res = send_media(job, caption)
    if not res.get("ok"):
        print("❌ ارسال ناموفق:", json.dumps(res, ensure_ascii=False)[:400])
        return {"ok": False, "err": res}
    mid = res["result"]["message_id"]
    print("✅ ارسال ok — message_id =", mid)

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

    probe = call_json(job["token"], "editMessageReplyMarkup",
                      {"chat_id": job["chat_id"], "message_id": mid})
    alive = bool(probe.get("ok")) or "not found" not in json.dumps(probe).lower()
    print("پروب پیام:", "زنده ✓" if alive else json.dumps(probe, ensure_ascii=False)[:150])

    (job["folder"] / "caption.txt").write_text(caption, encoding="utf-8")
    time.sleep(3)
    return {"ok": True, "message_id": mid, "alive": alive, "dupes_deleted": dupes,
            "caption_chars": len(caption), "hashtags": n_tags}

if __name__ == "__main__":
    which = sys.argv[1] if len(sys.argv) > 1 else "coding"
    names = [n for n, j in JOBS.items() if j["run"] == which]
    results = {}
    for n in names:
        results[n] = run_job(n)
    out = Path(f"/home/z/my-project/tool-results/g35_send_{which}.json")
    out.write_text(json.dumps(results, ensure_ascii=False, indent=1), encoding="utf-8")
    print("\nsaved:", out)
    for k, v in results.items():
        print(k, "->", json.dumps(v, ensure_ascii=False)[:200])
