#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
poster_lib — کتابخانه مشترک تولید عکس‌نوشته کانال انجمن کدنویسی
هر پست صف (content/queue/NNN-…/spec.py) با این کتابخانه رندر می‌شود.
رندر فارسی: Pillow + libraqm (direction='rtl' روی متن خام)
"""
from PIL import Image, ImageDraw, ImageFont

W, H = 1080, 1350
MARGIN = 64

FONT_DIR = "/home/z/my-project/assets/fonts"
FONTS = {
    "mono":     "/usr/share/fonts/truetype/dejavu/DejaVuSansMono.ttf",
    "fa_bold":  f"{FONT_DIR}/Vazirmatn-Bold.ttf",
    "fa_med":   f"{FONT_DIR}/Vazirmatn-Medium.ttf",
    "fa_black": f"{FONT_DIR}/Vazirmatn-Black.ttf",
}

def font(family, size):
    return ImageFont.truetype(FONTS[family], size)

# ---------- پالت ----------
BG_TOP = (13, 18, 48)
BG_BOT = (26, 33, 81)
ACCENT = (34, 211, 238)
ACCENT2 = (167, 139, 250)
GREEN  = (52, 211, 153)
ORANGE = (251, 146, 60)
WHITE  = (255, 255, 255)
LIGHT  = (232, 238, 248)
SUB    = (198, 208, 228)
DESC   = (218, 226, 242)
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
C_NUM  = (249, 174, 124)

# نام‌های کوتاه برای spec ها
VAR, FN, STR, CMT, OUT, FG, NUM = C_VAR, C_FN, C_STR, C_CMT, C_OUT, C_FG, C_NUM

# ---------- helpers ----------
def rtl_right(d, text, fnt, color, right_x, y):
    d.text((right_x, y), text, font=fnt, fill=color, direction="rtl", anchor="ra")

def seg_w(d, seg):
    text, family, size, color, is_fa = seg
    return d.textlength(text, font=font(family, size),
                        direction="rtl" if is_fa else "ltr")

def draw_rtl_segs(d, segs, right_x, y):
    cur = right_x
    for seg in segs:
        text, family, size, color, is_fa = seg
        w = seg_w(d, seg)
        d.text((cur, y), text, font=font(family, size), fill=color,
               direction="rtl" if is_fa else "ltr", anchor="ra")
        cur -= w

def draw_ltr_segs(d, segs, x, y):
    cur = x
    for seg in segs:
        text, family, size, color, is_fa = seg
        d.text((cur, y), text, font=font(family, size), fill=color,
               direction="rtl" if is_fa else "ltr", anchor="la")
        cur += seg_w(d, seg)

# سازنده‌های قطعه (segment) برای spec ها
def T(text, color=FG, size=33):      return (text, "mono", size, color, False)
def TF(text, color=FG, size=33):     return (text, "fa_med", size, color, True)
def FB(text, color=WHITE, size=31):  return (text, "fa_bold", size, color, True)
def FM(text, color=WHITE, size=31):  return (text, "fa_med", size, color, True)

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
    g1 = font("mono", 46)
    g2 = font("mono", 38)
    for txt, x, y, fnt, a in [
        ("</>", 78, 470, g1, 30), ("{ }", 930, 330, g1, 26),
        ("#", 990, 690, g2, 26), ("//", 66, 1210, g2, 24),
        ("</>", 940, 1130, g2, 22), ("=>", 70, 330, g2, 22),
    ]:
        d.text((x, y), txt, font=fnt, fill=(255, 255, 255, a))
    return Image.alpha_composite(img.convert("RGBA"), layer).convert("RGB")

# ---------- اجزا ----------
def badge(d, y):
    f = font("fa_bold", 30)
    fi = font("mono", 30)
    label, icon = "انجمن کدنویسی", "</>"
    lw = d.textlength(label, font=f, direction="rtl")
    iw = d.textlength(icon, font=fi, direction="ltr")
    pad, gap = 26, 14
    x1 = W - MARGIN
    x0 = x1 - (lw + iw + pad * 3 + gap)
    d.rounded_rectangle([x0, y, x1, y + 62], radius=31,
                        fill=(255, 255, 255, 16), outline=ACCENT + (160,), width=2)
    d.text((x1 - pad, y + 14), icon, font=fi, fill=ACCENT, direction="ltr", anchor="ra")
    d.text((x1 - pad - iw - gap, y + 14), label, font=f, fill=WHITE,
           direction="rtl", anchor="ra")
    return y + 62

def code_block(d, y, sec):
    """sec: {name, lines:[{segs}|{sep:tag}]}"""
    x0, x1 = MARGIN, W - MARGIN
    bar_h, lh = 58, 58
    n = len(sec["lines"])
    h = bar_h + 34 + n * lh + 26
    d.rounded_rectangle([x0, y, x1, y + h], radius=24, fill=CODE_BG,
                        outline=CARD_BR, width=1)
    d.rounded_rectangle([x0, y, x1, y + bar_h + 18], radius=24, fill=(24, 30, 56))
    d.rectangle([x0, y + bar_h - 6, x1, y + bar_h], fill=(24, 30, 56))
    dot_y = y + bar_h / 2
    for i, c in enumerate([(255, 95, 87), (254, 188, 46), (40, 200, 64)]):
        cx = x0 + 30 + i * 34
        d.ellipse([cx - 9, dot_y - 9, cx + 9, dot_y + 9], fill=c)
    ff = font("mono", 26)
    fname = sec.get("name", "main.py")
    fw = d.textlength(fname, font=ff, direction="ltr")
    d.text(((x0 + x1) / 2 - fw / 2, y + bar_h / 2 - 15), fname, font=ff,
           fill=MUTED, direction="ltr")
    lx = x0 + 40
    ly = y + bar_h + 34
    for i, item in enumerate(sec["lines"]):
        yy = ly + i * lh
        if "sep" in item:
            d.line([x0 + 24, yy - 8, x1 - 24, yy - 8],
                   fill=(255, 255, 255, 26), width=1)
            ftag = font("fa_med", 23)
            d.text((x1 - 40, yy + 2), item["sep"], font=ftag,
                   fill=(255, 255, 255, 185), direction="rtl", anchor="ra")
        elif item.get("segs"):
            draw_ltr_segs(d, item["segs"], lx, yy)
    return y + h

def bullet_card(d, y, sec, idx, item):
    h = sec.get("h", 100)
    gap = sec.get("gap", 16)
    x0, x1 = MARGIN, W - MARGIN
    accent = sec.get("colors", [ACCENT, GREEN, ACCENT2])[idx % len(sec.get("colors", [ACCENT, GREEN, ACCENT2]))]
    d.rounded_rectangle([x0, y, x1, y + h], radius=20, fill=CARD_BG,
                        outline=CARD_BR, width=1)
    d.rounded_rectangle([x1 - 22, y + 16, x1 - 10, y + h - 16], radius=6, fill=accent)
    tx_right = x1 - 44
    draw_rtl_segs(d, item["title"], tx_right, y + int(h * 0.16))
    f_desc = font("fa_med", sec.get("desc_size", 29))
    d.text((tx_right, y + int(h * 0.56)), item["desc"], font=f_desc, fill=DESC,
           direction="rtl", anchor="ra")
    return y + h + gap

def footer(d, y, label="کانال انجمن کدنویسی", handle="@anjomanfaghihi"):
    f = font("fa_bold", 30)
    fmono = font("mono", 30)
    w1 = d.textlength(label, font=f, direction="rtl")
    w2 = d.textlength(handle, font=fmono, direction="ltr")
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

# ---------- رندر اصلی ----------
def render(spec, out_path):
    """spec: {series_label, title, title_size?, subtitle, sections[], note?}"""
    img = add_decor(make_background())
    d = ImageDraw.Draw(img, "RGBA")
    y = 52
    y = badge(d, y) + 34
    rtl_right(d, spec["series_label"], font("fa_med", 33), ACCENT, W - MARGIN, y)
    y += 56
    rtl_right(d, spec["title"], font("fa_black", spec.get("title_size", 62)),
              WHITE, W - MARGIN, y)
    y += spec.get("title_size", 62) + 30
    rtl_right(d, spec["subtitle"], font("fa_med", 34), SUB, W - MARGIN, y)
    y += 62
    for sec in spec["sections"]:
        if sec["type"] == "code":
            y = code_block(d, y, sec) + 34
        elif sec["type"] == "bullets":
            for idx, item in enumerate(sec["items"]):
                y = bullet_card(d, y, sec, idx, item)
            y += 18
    if spec.get("note"):
        y += 6
        rtl_right(d, spec["note"], font("fa_med", 24), SUB, W - MARGIN, y)
        y += 46
    footer(d, min(y, H - 78))
    img.save(out_path, "PNG")
    return out_path
