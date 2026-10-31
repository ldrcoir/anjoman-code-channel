#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""گردش ۳۴ — کاور پست «بازی‌های مرورگری انجمن»:
اسکرین‌شات واقعی هر دو بازی از فایل‌های برنچ web (نسخهٔ زندهٔ Pages) + کولاژ ۱۰۸۰×۱۳۵۰
سبک بصری = همان هاب بازی‌ها (سرمه‌ای + فیروزه‌ای/سبز). رندر فارسی با raqm: direction=rtl.
خروجی: <پوشه پست>/post.png و post-clean.png"""
import os
from pathlib import Path
from playwright.sync_api import sync_playwright
from PIL import Image, ImageDraw, ImageFont

REPO = Path("/home/z/my-project/anjoman-code-bot")
FONT_DIR = REPO / "assets" / "fonts"
WEB = Path("/home/z/my-project/wt-web")
OUT_DIR = REPO / "channels/coding/posts/b4-khollagheh/026-baziha-ye-webi"
OUT_DIR.mkdir(parents=True, exist_ok=True)
TMP = Path("/home/z/my-project/tool-results/g34_shots")
TMP.mkdir(parents=True, exist_ok=True)

GAMES = [
    {"html": WEB / "robot-masiryab.html", "url_extra": "", "label": "ربات مسیریاب",
     "color": (52, 211, 153), "shot": TMP / "masiryab.png"},
    {"html": WEB / "rafiq-mar.html", "url_extra": "?demo=1", "label": "رفیق مار",
     "color": (251, 146, 60), "shot": TMP / "mar.png"},
]

# ---------- ۱) اسکرین‌شات هر دو بازی (پهنای موبایل، صفحهٔ کامل) ----------
with sync_playwright() as p:
    b = p.chromium.launch(args=["--force-color-profile=srgb"])
    pg = b.new_page(viewport={"width": 560, "height": 1000}, device_scale_factor=1)
    errs = []
    pg.on("pageerror", lambda e: errs.append(str(e)))
    pg.on("console", lambda m: errs.append(m.text) if m.type == "error" else None)
    for g in GAMES:
        pg.goto(f"file://{g['html']}{g['url_extra']}")
        pg.wait_for_timeout(2600)
        pg.screenshot(path=str(g["shot"]), full_page=True)
        im = Image.open(g["shot"])
        print("shot:", g["label"], im.size)
    b.close()
if errs:
    print("⚠️ خطاهای کنسول:", errs[:6])

# ---------- ۲) کولاژ ۱۰۸۰×۱۳۵۰ ----------
W, H = 1080, 1350
canvas = Image.new("RGB", (W, H))
d = ImageDraw.Draw(canvas)
# پس‌زمینهٔ گرادیان مورب مثل هاب (160deg، #0D1230 → #1A2151)
c1, c2 = (13, 18, 48), (26, 33, 81)
for y in range(H):
    t = y / H
    row = tuple(int(c1[i] + (c2[i] - c1[i]) * t) for i in range(3))
    d.line([(0, y), (W, y)], fill=row)

def F(name, size):
    return ImageFont.truetype(str(FONT_DIR / name), size)

f_title = F("Lalezar-Regular.ttf", 54)
f_sub = F("Vazirmatn-Medium.ttf", 27)
f_label = F("Vazirmatn-Bold.ttf", 30)
f_link = F("Lalezar-Regular.ttf", 46)
f_small = F("Vazirmatn-Medium.ttf", 24)

# سربرگ
d.text((W // 2, 66), "بازی‌های مرورگری انجمن کدنویسی", font=f_title,
       fill=(255, 255, 255), anchor="mm", direction="rtl", language="fa")
d.text((W // 2, 122), "بدون دانلود و نصب — فقط لینک را در مرورگر باز کن", font=f_sub,
       fill=(198, 208, 228), anchor="mm", direction="rtl", language="fa")

# دو خانهٔ بازی — برچسب بالای هر خانه
ROW_Y, ROW_H = 205, 880
cell_w = 505
xs = [14, W - 14 - cell_w]
for g, x in zip(GAMES, xs):
    # برچسب
    chip_h = 46
    tw = d.textlength(g["label"], font=f_label, direction="rtl", language="fa")
    pad = 26
    cx = x + cell_w // 2
    d.rounded_rectangle([cx - tw / 2 - pad, ROW_Y - chip_h - 10, cx + tw / 2 + pad, ROW_Y - 10],
                        radius=14, fill=g["color"])
    d.text((cx, ROW_Y - chip_h // 2 - 10), g["label"], font=f_label, fill=(13, 18, 48),
           anchor="mm", direction="rtl", language="fa")
    # تصویر — مقیاس به ارتفاع خانه، برش وسط‌چین به پهنای خانه
    im = Image.open(g["shot"]).convert("RGB")
    scale = ROW_H / im.height
    im = im.resize((int(im.width * scale), ROW_H), Image.LANCZOS)
    if im.width > cell_w:
        left = (im.width - cell_w) // 2
        im = im.crop((left, 0, left + cell_w, ROW_H))
    else:
        back = Image.new("RGB", (cell_w, ROW_H), (10, 14, 38))
        back.paste(im, ((cell_w - im.width) // 2, 0))
        im = back
    # قاب رنگی دور تصویر
    d.rectangle([x - 3, ROW_Y - 3, x + cell_w + 3, ROW_Y + ROW_H + 3],
                outline=g["color"], width=4)
    canvas.paste(im, (x, ROW_Y))

# پانویس — قاب قرصی لینک + آی‌دی (سبک پوسترهای انجمن)
fy = ROW_Y + ROW_H + 26
d.line([(64, fy), (W - 64, fy)], fill=(34, 211, 238), width=3)
d.text((W // 2, fy + 58), "ble.ir/anjomanfaghihi", font=f_link,
       fill=(34, 211, 238), anchor="mm", direction="rtl", language="fa")
d.text((W // 2, fy + 122), "کد بازی‌ها باز است — در ویرایشگر متنی بازشان کن و ببین چطور ساخته شده‌اند",
       font=f_small, fill=(148, 163, 184), anchor="mm", direction="rtl", language="fa")

canvas.save(OUT_DIR / "post.png")
print("post.png:", (OUT_DIR / 'post.png').stat().st_size // 1024, "KB")
