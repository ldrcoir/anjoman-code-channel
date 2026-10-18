#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
تولید عکس‌نوشته آموزشی برای کانال انجمن کدنویسی
سری آموزش پایتون — قسمت اول: اولین برنامه
رندر فارسی با Pillow + libraqm (direction='rtl')
"""
from PIL import Image, ImageDraw, ImageFont

W, H = 1080, 1350
MARGIN = 64

FONT_DIR = "/home/z/my-project/assets/fonts"
VZ_BLACK = f"{FONT_DIR}/Vazirmatn-Black.ttf"
VZ_BOLD  = f"{FONT_DIR}/Vazirmatn-Bold.ttf"
VZ_MED   = f"{FONT_DIR}/Vazirmatn-Medium.ttf"
MONO     = "/usr/share/fonts/truetype/dejavu/DejaVuSansMono.ttf"

# پالت رنگ
BG_TOP = (13, 18, 48)
BG_BOT = (26, 33, 81)
ACCENT = (34, 211, 238)      # cyan
ACCENT2 = (167, 139, 250)    # violet
GREEN  = (52, 211, 153)      # emerald
WHITE  = (255, 255, 255)
LIGHT  = (232, 238, 248)
SUB    = (198, 208, 228)     # زیرنویس هدر
DESC   = (218, 226, 242)     # توضیحات کارت‌ها — روشن و خوانا
MUTED  = (148, 163, 184)
CARD_BG = (255, 255, 255, 24)
CARD_BR = (255, 255, 255, 60)
CODE_BG = (11, 14, 34)
C_VAR  = (122, 162, 247)
C_FN   = (187, 134, 252)
C_STR  = (166, 214, 167)
C_CMT  = (120, 133, 155)
C_OUT  = (252, 211, 77)
C_FG   = (220, 225, 235)


def F(path, size):
    return ImageFont.truetype(path, size)

# ---------- helpers ----------
def rtl_right(d, text, font, color, right_x, y):
    """متن فارسی تراز راست"""
    d.text((right_x, y), text, font=font, fill=color, direction="rtl", anchor="ra")

def rtl_center(d, text, font, color, cx, y):
    d.text((cx, y), text, font=font, fill=color, direction="rtl", anchor="ma")

def seg_w(d, text, font, is_fa):
    return d.textlength(text, font=font, direction="rtl" if is_fa else "ltr")

def draw_rtl_segs(d, segs, right_x, y):
    """قطعات یک خط راست‌چین: اولین قطعه سمت راست می‌نشیند"""
    cur = right_x
    for text, font, color, is_fa in segs:
        w = seg_w(d, text, font, is_fa)
        d.text((cur, y), text, font=font, fill=color,
               direction="rtl" if is_fa else "ltr", anchor="ra")
        cur -= w

def draw_ltr_segs(d, segs, x, y):
    """قطعات یک خط چپ‌چین (کد)"""
    cur = x
    for text, font, color, is_fa in segs:
        d.text((cur, y), text, font=font, fill=color,
               direction="rtl" if is_fa else "ltr", anchor="la")
        cur += seg_w(d, text, font, is_fa)

# ---------- پس‌زمینه ----------
def make_background():
    img = Image.new("RGB", (W, H), BG_TOP)
    px = img.load()
    for y in range(H):
        t = y / (H - 1)
        r = int(BG_TOP[0] + (BG_BOT[0] - BG_TOP[0]) * t)
        g = int(BG_TOP[1] + (BG_BOT[1] - BG_TOP[1]) * t)
        b = int(BG_TOP[2] + (BG_BOT[2] - BG_TOP[2]) * t)
        for x in range(W):
            px[x, y] = (r, g, b)
    return img

def add_decor(img):
    layer = Image.new("RGBA", (W, H), (0, 0, 0, 0))
    d = ImageDraw.Draw(layer)
    d.ellipse([W-260, -160, W+180, 280], fill=ACCENT2 + (36,))
    d.ellipse([-180, 180, 160, 520], fill=ACCENT + (30,))
    d.ellipse([W-320, H-330, W+60, H+90], fill=GREEN + (26,))
    for gx in range(40, W, 76):
        for gy in range(40, H, 76):
            d.ellipse([gx, gy, gx+3, gy+3], fill=(255, 255, 255, 10))
    g1 = F(MONO, 46)
    g2 = F(MONO, 38)
    for txt, x, y, fnt, a in [
        ("</>", 78, 470, g1, 30), ("{ }", 930, 330, g1, 26),
        ("#", 990, 690, g2, 26), ("//", 66, 1210, g2, 24),
        ("</>", 940, 1130, g2, 22), ("=>", 70, 330, g2, 22),
    ]:
        d.text((x, y), txt, font=fnt, fill=(255, 255, 255, a))
    img = Image.alpha_composite(img.convert("RGBA"), layer)
    return img.convert("RGB")

# ---------- اجزا ----------
def badge(d, y):
    f = F(VZ_BOLD, 30)
    label = "انجمن کدنویسی"
    fi = F(MONO, 30)
    icon = "</>"
    lw = seg_w(d, label, f, True)
    iw = seg_w(d, icon, fi, False)
    pad, gap = 26, 14
    bw = lw + iw + pad * 3 + gap
    x1 = W - MARGIN
    x0 = x1 - bw
    d.rounded_rectangle([x0, y, x1, y + 62], radius=31,
                        fill=(255, 255, 255, 16), outline=ACCENT + (160,), width=2)
    d.text((x1 - pad, y + 14), icon, font=fi, fill=ACCENT, direction="ltr", anchor="ra")
    d.text((x1 - pad - iw - gap, y + 14), label, font=f, fill=WHITE,
           direction="rtl", anchor="ra")
    return y + 62

def code_block(d, y):
    x0, x1 = MARGIN, W - MARGIN
    h = 452
    d.rounded_rectangle([x0, y, x1, y + h], radius=24, fill=CODE_BG,
                        outline=CARD_BR, width=1)
    bar_h = 58
    d.rounded_rectangle([x0, y, x1, y + bar_h + 18], radius=24, fill=(24, 30, 56))
    d.rectangle([x0, y + bar_h - 6, x1, y + bar_h], fill=(24, 30, 56))
    dot_y = y + bar_h / 2
    for i, c in enumerate([(255, 95, 87), (254, 188, 46), (40, 200, 64)]):
        cx = x0 + 30 + i * 34
        d.ellipse([cx - 9, dot_y - 9, cx + 9, dot_y + 9], fill=c)
    ff = F(MONO, 26)
    fname = "first_program.py"
    fw = d.textlength(fname, font=ff, direction="ltr")
    d.text(((x0 + x1) / 2 - fw / 2, y + bar_h / 2 - 15), fname, font=ff,
           fill=MUTED, direction="ltr")
    mono = F(MONO, 33)
    mono_fa = F(VZ_MED, 33)
    lx = x0 + 40
    ly = y + bar_h + 34
    lh = 58
    lines = [
        [("name", mono, C_VAR, False), (" = ", mono, C_FG, False),
         ('"', mono, C_STR, False), ("دانش\u200cآموز", mono_fa, C_STR, True),
         ('"', mono, C_STR, False)],
        [("print", mono, C_FN, False), ("(", mono, C_FG, False),
         ('"', mono, C_STR, False), ("سلام", mono_fa, C_STR, True),
         ('", ', mono, C_FG, False), ("name", mono, C_VAR, False),
         (")", mono, C_FG, False)],
        [],
        [("# ", mono, C_CMT, False), ("خروجی:", mono_fa, C_CMT, True)],
        [("سلام دانش\u200cآموز", mono_fa, C_OUT, True)],
        [("به دنیای کدنویسی خوش آمدی!", mono_fa, C_OUT, True)],
    ]
    for i, segs in enumerate(lines):
        yy = ly + i * lh
        if i == 4:
            d.line([x0 + 24, yy - 16, x1 - 24, yy - 16],
                   fill=(255, 255, 255, 26), width=1)
            ftag = F(VZ_MED, 23)
            d.text((x1 - 40, yy - 44), "خروجی برنامه", font=ftag,
                   fill=(255, 255, 255, 185), direction="rtl", anchor="ra")
        if segs:
            draw_ltr_segs(d, segs, lx, yy)
    return y + h

def bullet_card(d, y, title_segs, desc, accent):
    x0, x1 = MARGIN, W - MARGIN
    h = 100
    d.rounded_rectangle([x0, y, x1, y + h], radius=20, fill=CARD_BG,
                        outline=CARD_BR, width=1)
    d.rounded_rectangle([x1 - 22, y + 16, x1 - 10, y + h - 16], radius=6, fill=accent)
    tx_right = x1 - 44
    f_title = F(VZ_BOLD, 32)
    f_desc = F(VZ_MED, 29)
    # عنوان: قطعات راست‌چین
    cur = tx_right
    for text, font, color, is_fa in title_segs:
        w = seg_w(d, text, font, is_fa)
        d.text((cur, y + 16), text, font=font, fill=color,
               direction="rtl" if is_fa else "ltr", anchor="ra")
        cur -= w
    d.text((tx_right, y + 56), desc, font=f_desc, fill=DESC,
           direction="rtl", anchor="ra")
    return y + h

def footer(d, y):
    f = F(VZ_BOLD, 30)
    fmono = F(MONO, 30)
    label = "کانال انجمن کدنویسی"
    handle = "@anjomanfaghihi"
    w1 = seg_w(d, label, f, True)
    w2 = seg_w(d, handle, fmono, False)
    pill_w = w1 + w2 + 90
    x0 = (W - pill_w) / 2
    d.rounded_rectangle([x0, y, x0 + pill_w, y + 64], radius=32,
                        fill=ACCENT + (26,), outline=ACCENT + (150,), width=2)
    right_edge = (W + (w1 + w2)) / 2
    d.text((right_edge, y + 14), label, font=f, fill=WHITE,
           direction="rtl", anchor="ra")
    d.text((right_edge - w1, y + 15), handle, font=fmono, fill=ACCENT,
           direction="ltr", anchor="ra")
    return y + 64

# ---------- ساخت تصویر ----------
def build():
    img = add_decor(make_background())
    d = ImageDraw.Draw(img, "RGBA")
    y = 52
    y = badge(d, y) + 34
    fs = F(VZ_MED, 33)
    rtl_right(d, "سری آموزش پایتون  •  قسمت اول", fs, ACCENT, W - MARGIN, y)
    y += 56
    ft = F(VZ_BLACK, 62)
    rtl_right(d, "اولین برنامه\u200cات را بنویس!", ft, WHITE, W - MARGIN, y)
    y += 92
    fs2 = F(VZ_MED, 34)
    rtl_right(d, "در ۳۰ ثانیه با زبان پایتون آشنا شو", fs2, SUB, W - MARGIN, y)
    y += 62
    y = code_block(d, y) + 34
    mono_t = F(MONO, 30)
    y = bullet_card(d, y,
        [("متغیر ", F(VZ_BOLD, 31), WHITE, True), ("(Variable)", mono_t, ACCENT, False)],
        "جعبه\u200cای برای نگه\u200cداری داده\u200cها", ACCENT) + 16
    y = bullet_card(d, y,
        [("تابع ", F(VZ_BOLD, 31), WHITE, True), ("print()", mono_t, GREEN, False)],
        "هر چیزی را روی صفحه نمایش می\u200cدهد", GREEN) + 16
    y = bullet_card(d, y,
        [("رشته ", F(VZ_BOLD, 31), WHITE, True), ("(String)", mono_t, ACCENT2, False)],
        "متن است؛ میان گیومه می\u200cنشیند", ACCENT2) + 28
    footer(d, min(y, H - 78))
    out = "/home/z/my-project/download/python-post-01.png"
    img.save(out, "PNG")
    print("saved:", out, img.size)

if __name__ == "__main__":
    build()
