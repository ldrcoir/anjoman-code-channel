#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""گردش ۶۴ — عنوان نو کلیپ‌ها به فرمان سردبیر:
«اسم کانال را بنویس؛ فقط پژوهشسرا را نوشته بودی، انجمن دانش‌پژوهان ننوشته بودی»
سه خط: ۱) کانال: انرژی‌های نوین  ۲) موضوع کلیپ  ۳) نوار: پژوهش‌سرای امام هادی (ع) • انجمن دانش‌پژوهان
عنوان همیشه بالای صحنه است و به‌قدر کافی جمع‌وجور که با صحنه تداخل نکند."""
from pathlib import Path
from PIL import Image, ImageDraw, ImageFont

ASSETS = Path("/home/z/my-project/download/anim-scratch-enerjy/_assets")
FONT_BLACK = "/home/z/my-project/assets/fonts/Vazirmatn-Black.ttf"
FONT_BOLD = "/home/z/my-project/assets/fonts/Vazirmatn-Bold.ttf"

def sakht(onvan: str, movzu: str, khoroji: str):
    W, H = 1040, 184          # بوم ۲× → روی صحنه ۵۲۰×۹۲ واحد
    img = Image.new("RGBA", (W, H), (0, 0, 0, 0))
    d = ImageDraw.Draw(img)

    # خط ۱ — اسم کانال
    f1 = ImageFont.truetype(FONT_BLACK, 44)
    d.text((W // 2, 32), onvan, font=f1, fill=(14, 92, 51, 255), anchor="mm",
           direction="rtl", language="fa", stroke_width=5, stroke_fill=(255, 255, 255, 225))

    # خط ۲ — موضوع این کلیپ
    f2 = ImageFont.truetype(FONT_BOLD, 30)
    d.text((W // 2, 80), movzu, font=f2, fill=(15, 90, 140, 255), anchor="mm",
           direction="rtl", language="fa", stroke_width=4, stroke_fill=(255, 255, 255, 210))

    # خط ۳ — نوار نام‌ها: پژوهش‌سرا + انجمن دانش‌پژوهان
    f3 = ImageFont.truetype(FONT_BOLD, 23)
    esm = "پژوهش\u200cسرای دانش\u200cآموزی امام هادی (ع)   •   انجمن دانش\u200cپژوهان"
    bb = d.textbbox((0, 0), esm, font=f3, direction="rtl", language="fa")
    tw = bb[2] - bb[0]
    bx0, bx1 = (W - tw) // 2 - 16, (W + tw) // 2 + 16
    d.rounded_rectangle([bx0, 122, bx1, 164], radius=16, fill=(255, 255, 255, 175),
                        outline=(255, 255, 255, 215), width=2)
    d.text((W // 2, 143), esm, font=f3, fill=(30, 41, 59, 255), anchor="mm",
           direction="rtl", language="fa")

    img.save(khoroji)
    print("title:", khoroji.name, img.size)

sakht("انرژی\u200cهای نوین", "انرژی باد", ASSETS / "title_bad.png")
sakht("انرژی\u200cهای نوین", "انرژی خورشیدی", ASSETS / "title_khorshid.png")
