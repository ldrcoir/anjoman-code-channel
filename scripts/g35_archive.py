#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""گردش ۳۵ — آرشیو: متای پوشه‌های تازه، تفکیک کپشن پوستر/PDF مجله، INDEX هر دو کانال، state، حذف صف مصرف‌شده."""
import json, sys
from pathlib import Path

REPO = Path("/home/z/my-project/anjoman-code-bot")
sys.path.insert(0, str(REPO / "scripts"))
from caption_frame import apply_caption_frame

CFG_CODING = json.loads((REPO / "channels/coding/channel.json").read_text(encoding="utf-8"))

# ---------- ۱) تفکیک کپشن‌های پوشهٔ ۰۲۷ (پوستر vs PDF) ----------
F27 = REPO / "channels/coding/posts/b2-nashriyeh/027-aban-1405"
pdf_cap = (F27 / "caption.txt").read_text(encoding="utf-8")  # آخرین نوشته = کپشن PDF
(F27 / "doc-caption.txt").write_text(pdf_cap, encoding="utf-8")
qcap = (REPO / "channels/coding/content/queue/b2-nashriyeh/aband-1405/caption.txt").read_text(encoding="utf-8")
poster_cap = apply_caption_frame(qcap, CFG_CODING)
(F27 / "caption.txt").write_text(poster_cap, encoding="utf-8")
print("027: caption.txt =", len(poster_cap), "| doc-caption.txt =", len(pdf_cap))

# ---------- ۲) متا ----------
def meta(path, obj):
    Path(path).write_text(json.dumps(obj, ensure_ascii=False, indent=1), encoding="utf-8")
    print("meta:", path)

meta(F27 / "meta.json", {
    "number": 27, "code": "027", "channel": "coding",
    "category_fa": "نشریهٔ الکترونیکی (بند ۲)",
    "title": "مجلهٔ انجمن کدنویسی — شمارهٔ ۲ (آبان ۱۴۰۵) + پیوست PDF",
    "date": "2026-10-28",
    "message_id": 202, "doc_message_id": 204,
    "image": "post.png", "clean_image": "post-clean.png", "doc": "magazine.pdf",
    "hashtags": "#انجمن_کدنویسی #نشریه_انجمن #مجله_کدنویسی #اخبار_هوش_مصنوعی #برنامه_نویسی",
    "doc_hashtags": "#انجمن_کدنویسی #مجله_کدنویسی #نشریه_انجمن #آبان_۱۴۰۵",
    "caption_chars": len(poster_cap), "doc_caption_chars": len(pdf_cap),
    "statute": "شیوه‌نامهٔ هشتمین دورهٔ مسابقات کدنویسی پژوهش‌سراها ۱۴۰۵-۱۴۰۴ — بند ۲",
    "notes": [
        "گردش ۳۵: انتشار با اجازهٔ صریح مالک — «نسخهٔ pdf نشریهٔ انجمن کامپیوتر را نزدیک خود پوسترش (در صورت امکان) قرار بده»",
        "PDF بلافاصله بعد از پوستر رفت (۲۰۲ → ۲۰۴) — بدون هیچ پیامی بین این دو",
        "hold_until.txt صف (۲۰۲۶-۱۱-۲۰) با همین اجازهٔ مالک لغو و پوشه از صف به آرشیو منتقل شد",
        "هشتگ پیوست طبق راهنمای تگ: #نشریه_الکترونیکی → #نشریه_انجمن (همسو با اصلاح زندهٔ پیام ۲۴ در گردش ۲۵)",
        "کپی دوبارسردن پوستر (پیام ۲۰۳) بلافاصله با گارد فید حذف شد"
    ],
})

meta(REPO / "channels/coding/posts/b4-khollagheh/028-fail-mar-downloadi/meta.json", {
    "number": 28, "code": "028", "channel": "coding",
    "category_fa": "فعالیت خلاقانه (بند ۴)",
    "title": "نسخهٔ دانلودی «رفیق مار» — عین نسخهٔ آنلاین",
    "date": "2026-10-28",
    "message_id": 205,
    "doc": "rafiq-mar.html",
    "doc_md5": "377e01ab8d9e0a1b5f196f37b18b2990",
    "hashtags": "#انجمن_کدنویسی #فعالیت_خلاقانه #بازی_مرورگری #رفیق_مار",
    "caption_chars": 557,
    "statute": "شیوه‌نامهٔ هشتمین دورهٔ مسابقات کدنویسی پژوهش‌سراها ۱۴۰۵-۱۴۰۴ — بند ۴",
    "notes": [
        "گردش ۳۵: انتشار با اجازهٔ مالک — «نسخهٔ دانلودی بازی مرورگری بگذار و فرقی بین نسخهٔ آنلاین و دانلودی نباشه»",
        "فایل بایت‌به‌بایت برابر نسخهٔ زندهٔ GitHub Pages (md5 برابر web:f1324e3) — خودِ فایل HTML است نه APK؛ با مرورگر باز می‌شود",
        "بازی بدون اینترنت هم اجرا می‌شود (تنها ارجاع بیرونی: manifest/آیکن PWA که برای نصب وب است، برای گیم‌پلی لازم نیست)"
    ],
})

meta(REPO / "channels/coding/posts/b4-khollagheh/029-fail-masiryab-downloadi/meta.json", {
    "number": 29, "code": "029", "channel": "coding",
    "category_fa": "فعالیت خلاقانه (بند ۴)",
    "title": "نسخهٔ دانلودی «ربات مسیریاب» — عین نسخهٔ آنلاین",
    "date": "2026-10-28",
    "message_id": 206,
    "doc": "robot-masiryab.html",
    "doc_md5": "9895749cf22db523446b6ff77ebb1e05",
    "hashtags": "#انجمن_کدنویسی #فعالیت_خلاقانه #بازی_مرورگری #ربات_مسیریاب",
    "caption_chars": 500,
    "statute": "شیوه‌نامهٔ هشتمین دورهٔ مسابقات کدنویسی پژوهش‌سراها ۱۴۰۵-۱۴۰۴ — بند ۴",
    "notes": [
        "گردش ۳۵: همان دستور مالک — نسخهٔ دانلودی = همان فایل تک‌صفحهٔ وب (md5 برابر Pages)؛ هیچ منبع بیرونی ندارد",
        "کپی دوبارسردن (پیام ۲۰۷) با گارد فید حذف شد"
    ],
})

meta(REPO / "channels/enerjy-novin/posts/alef-tarviji/010-enerjy-ab/meta.json", {
    "number": 10, "code": "010", "channel": "enerjy-novin",
    "category_fa": "ترویجی/دانستنی",
    "shevnameh": "بند الف — ترویجی/آموزشی",
    "title": "۴. انرژی آب (برق‌آبی) — دنبالهٔ «۱. خورشیدی»، «۲. بادی» و «۳. زمین‌گرمایی»",
    "date": "2026-10-28",
    "message_id": 28,
    "image": "post.png",
    "clean_image": "post-clean.png",
    "statute": "شیوه‌نامهٔ ششمین دوره مسابقات انرژی‌های نوین ۱۴۰۵-۱۴۰۴",
    "hashtags": "#انرژی_آب #انرژی_پاک #انرژی_های_نوین #انرژی_پژوهان #دانستنی_انرژی",
    "caption_chars": 820,
    "facts": [
        "نروژ حدود ۹۰٪ برقش را از انرژی آب می‌گیرد (مرجع رسمی انرژی نروژ)",
        "کارون ۳ (مسجدسلیمان، خوزستان) = بزرگ‌ترین نیروگاه برق‌آبی ایران با ۲۲۸۰ مگاوات"
    ],
    "notes": [
        "گردش ۳۵: انتشار با اجازهٔ صریح مالک — «پست کانال انرژی‌های نوین را قرار بده» (دو بار تکرار شد)",
        "موضوع آب (برق‌آبی) در کانال سابقه نداشت — غیرتکراری؛ دنبالهٔ مستقیم سری منابع انرژی",
        "رندر با موتور مستقل انرژی (render_enerjy.py)؛ بازبینی چشمی پیش از ارسال ✓",
        "هویت بصری مستقل انرژی (سبز/کرم، سربرگ ششمین دوره، پانویس @anjoman_energypazhohan)"
    ],
})

# ---------- ۳) حذف صف مصرف‌شده ----------
import shutil
Q = REPO / "channels/coding/content/queue/b2-nashriyeh/aband-1405"
if Q.exists():
    shutil.rmtree(Q)
    print("queue removed:", Q)

# ---------- ۴) state هر دو کانال ----------
sc = json.loads((REPO / "channels/coding/content/state.json").read_text(encoding="utf-8"))
sc["magazine"] = "2026-10-28"; sc["magazine_last"] = "027-aban-1405"
sc["game"] = "2026-10-28"; sc["game_last"] = "029-fail-masiryab-downloadi"
(REPO / "channels/coding/content/state.json").write_text(json.dumps(sc, ensure_ascii=False, indent=1), encoding="utf-8")

se = json.loads((REPO / "channels/enerjy-novin/content/state.json").read_text(encoding="utf-8"))
se["lesson"] = "2026-10-28"; se["lesson_last"] = "010-enerjy-ab"
(REPO / "channels/enerjy-novin/content/state.json").write_text(json.dumps(se, ensure_ascii=False, indent=1), encoding="utf-8")
print("state.json ×2 updated")

# ---------- ۵) INDEX کدنویسی ----------
idx_c = REPO / "channels/coding/posts/INDEX.md"
idx_c.write_text(idx_c.read_text(encoding="utf-8") + """
## گردش ۳۵ (۱۴۰۵-۰۸-۰۶) — نشریهٔ ۲ با PDF کنار پوستر + نسخهٔ دانلودی بازی‌ها

دستور مالک: «پست کانال انرژی‌های نوین را قرار بده و نسخهٔ pdf نشریهٔ انجمن کامپیوتر را نزدیک خود پوسترش (در صورت امکان) قرار بده. مطمئن شو نسخهٔ آنلاین و دانلودی بازی‌ها دقیقاً یکی باشد».

| پیام | موضوع | آرشیو | دستهٔ شیوه‌نامه | وضعیت |
|---|---|---|---|---|
| ۲۰۲ | مجلهٔ انجمن کدنویسی — شمارهٔ ۲ (آبان ۱۴۰۵) — پوستر | ۰۲۷ | نشریه (بند ۲) | زنده ✓ (کپی ۲۰۳ حذف شد) |
| ۲۰۴ | فایل PDF شمارهٔ ۲ — **بلافاصله بعد از پوستر** | ۰۲۷ | نشریه (بند ۲) | زنده ✓ — «نزدیک خود پوسترش» طبق دستور مالک |
| ۲۰۵ | فایل دانلودی رفیق مار (rafiq-mar.html) | ۰۲۸ | خلاقانه (بند ۴) | زنده ✓ — md5 برابر نسخهٔ Pages |
| ۲۰۶ | فایل دانلودی ربات مسیریاب (robot-masiryab.html) | ۰۲۹ | خلاقانه (بند ۴) | زنده ✓ (کپی ۲۰۷ حذف شد) — md5 برابر Pages |

- هم‌ارزی آنلاین/دانلودی با md5 راستی‌آزمایی شد: فایل ارسالی = فایل برنچ web (f1324e3) = خروجی زندهٔ Pages. خود فایل HTML است (نه APK) و با مرورگر باز می‌شود؛ مار حتی بدون اینترنت هم اجرا می‌شود.
- نشریهٔ ۲ از صف (hold_until ۲۰۲۶-۱۱-۲۰) با اجازهٔ مالک آزاد و منتشر شد؛ پوشهٔ صف حذف و در ۰۲۷ آرشیو شد.
- کپشن پیوست PDF طبق راهنمای تگ: #نشریه_الکترونیکی → #نشریه_انجمن.
""", encoding="utf-8")
print("INDEX coding updated")

# ---------- ۶) INDEX انرژی ----------
idx_e = REPO / "channels/enerjy-novin/posts/INDEX.md"
idx_e.write_text(idx_e.read_text(encoding="utf-8") + """
## گردش ۳۵ (۱۴۰۵-۰۸-۰۶) — «۴. انرژی آب (برق‌آبی)»

دستور مالک (دو بار): «پست کانال انرژی‌های نوین را قرار بده» — پست تازهٔ غیرتکراری با هشتگ مناسب.

| پیام | موضوع | آرشیو | دستهٔ شیوه‌نامه | وضعیت |
|---|---|---|---|---|
| ۲۸ | ۴. انرژی آب (برق‌آبی) — دنبالهٔ خورشیدی/بادی/زمین‌گرمایی | ۰۱۰ | ترویجی (بند الف) | زنده ✓ — بدون کپی |

- موضوع آب (برق‌آبی) سابقهٔ کانال را نداشت؛ نروژ ۹۰٪ + کارون ۳ (۲۲۸۰ مگاوات، مسجدسلیمان) با منبع ساتبا/وزارت نیرو.
- رندر با موتور مستقل انرژی + بازبینی چشمی؛ ۵ هشتگ طبق الگوی پست ۰۰۹.
""", encoding="utf-8")
print("INDEX enerjy updated")
print("DONE")
