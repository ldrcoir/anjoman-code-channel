#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
poster_lib — کتابخانه مشترک تولید عکس‌نوشته برای همهٔ کانال‌ها (نسخه ۴)
قاعدهٔ طلایی: دسته‌بندی محتوا هرگز روی عکس یا متن کانال نوشته نمی‌شود؛
دسته فقط داخلی است (meta.json / stats / plan / inventory) طبق شیوه‌نامه.

معماری چندانجمنی:
- هر کانال شناسنامهٔ خودش را در channels/<slug>/channel.json دارد
- قبل از رندر، roznegar.py با set_channel(cfg) برند همان کانال را ست می‌کند:
  نام برند، پانویس، آیدی کانال و لوگوی اختصاصی channels/<slug>/assets/logo.png

امکانات:
- چیپ شماره درس («درس ۱») در ردیف بالای عکس
- نسخهٔ clean بدون نام/آدرس انجمن (برند=False) برای استفادهٔ آزاد
- ذخیرهٔ متن فارسی هر عکس با spec_text() (بدون هیچ اشارهٔ دسته‌بندی)
- هوک لوگوی اختصاصی هر کانال
رندر فارسی: Pillow + libraqm (direction='rtl' روی متن خام)
"""
import os
from pathlib import Path
from PIL import Image, ImageDraw, ImageFont

W, H = 1080, 1350
MARGIN = 64

# مسیر فونت‌ها از محل خود فایل مشتق می‌شود (repo/assets/fonts)؛ محیطی هم قابل‌بازنویسی است
FONT_DIR = os.environ.get("ANJOMAN_FONT_DIR") or str(
    Path(__file__).resolve().parent.parent / "assets" / "fonts")
FONTS = {
    "mono":     "/usr/share/fonts/truetype/dejavu/DejaVuSansMono.ttf",
    "fa_bold":  f"{FONT_DIR}/Vazirmatn-Bold.ttf",
    "fa_med":   f"{FONT_DIR}/Vazirmatn-Medium.ttf",
    "fa_black": f"{FONT_DIR}/Vazirmatn-Black.ttf",
    # فونت عنوان — لاله‌زار؛ چاپی و تمیز (بازخورد مالک: فونت عنوان قبلی خراب به نظر می‌رسید)
    "fa_title": f"{FONT_DIR}/Lalezar-Regular.ttf",
}
# ---------- شناسنامهٔ کانال فعال (چندانجمنی) ----------
# مقدار پیش‌فرض فقط برای اجرای مستقل spec ها؛ roznegar قبل از رندر set_channel می‌زند
CHANNEL = {
    "slug": "coding",
    "brand_name": "انجمن کدنویسی",
    "footer_label": "کانال انجمن کدنویسی",
    "handle": "@anjomanfaghihi",
}
LOGO_PATH = Path("")


def set_channel(cfg):
    """شناسنامهٔ کانال فعال را قبل از رندر ست می‌کند (فراخوانی از roznegar)"""
    global CHANNEL, LOGO_PATH
    CHANNEL = {
        "slug": cfg.get("slug", "coding"),
        "brand_name": cfg.get("brand_name", "انجمن کدنویسی"),
        "footer_label": cfg.get("footer_label", "کانال انجمن کدنویسی"),
        "handle": cfg.get("handle", "@anjomanfaghihi"),
    }
    logo = cfg.get("_logo_path")
    LOGO_PATH = Path(logo) if logo else Path("")

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

FA_DIGITS = "۰۱۲۳۴۵۶۷۸۹"
def fa_num(n):
    return "".join(FA_DIGITS[int(c)] if c.isdigit() else c for c in str(n))

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
def badge(d, y, img):
    """نشان بالا-راست: لوگوی اختصاصی کانال (اگر موجود باشد) یا متن برند"""
    if LOGO_PATH and LOGO_PATH.exists():
        try:
            logo = Image.open(LOGO_PATH).convert("RGBA")
            lh = 62
            lw = round(logo.width * lh / logo.height)
            logo = logo.resize((lw, lh), Image.LANCZOS)
            img.paste(logo, (W - MARGIN - lw, y), logo)
            return y + lh
        except Exception:
            pass
    f = font("fa_bold", 30)
    fi = font("mono", 30)
    label, icon = CHANNEL["brand_name"], "</>"
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

def lesson_chip(d, y, n):
    """چیپ شماره درس بالا-چپ"""
    f = font("fa_bold", 30)
    label = f"درس {fa_num(n)}"
    lw = d.textlength(label, font=f, direction="rtl")
    pad = 26
    x0 = MARGIN
    x1 = x0 + lw + pad * 2
    d.rounded_rectangle([x0, y, x1, y + 62], radius=31,
                        fill=ACCENT + (38,), outline=ACCENT + (190,), width=2)
    d.text((x1 - pad, y + 14), label, font=f, fill=WHITE,
           direction="rtl", anchor="ra")

def code_block(d, y, sec):
    """sec: {name, lh?, lines:[{segs}|{sep:tag}]}"""
    x0, x1 = MARGIN, W - MARGIN
    bar_h, lh = 56, sec.get("lh", 52)
    n = len(sec["lines"])
    h = bar_h + 28 + n * lh + 22
    d.rounded_rectangle([x0, y, x1, y + h], radius=24, fill=CODE_BG,
                        outline=CARD_BR, width=1)
    d.rounded_rectangle([x0, y, x1, y + bar_h + 18], radius=24, fill=(24, 30, 56))
    d.rectangle([x0, y + bar_h - 6, x1, y + bar_h], fill=(24, 30, 56))
    dot_y = y + bar_h / 2
    for i, c in enumerate([(255, 95, 87), (254, 188, 46), (40, 200, 64)]):
        cx = x0 + 30 + i * 34
        d.ellipse([cx - 9, dot_y - 9, cx + 9, dot_y + 9], fill=c)
    ff = font("mono", 26)
    fname = sec.get("name", "main.rs")
    fw = d.textlength(fname, font=ff, direction="ltr")
    d.text(((x0 + x1) / 2 - fw / 2, y + bar_h / 2 - 15), fname, font=ff,
           fill=MUTED, direction="ltr")
    lx = x0 + 40
    ly = y + bar_h + 28
    for i, item in enumerate(sec["lines"]):
        yy = ly + i * lh
        if "sep" in item:
            d.line([x0 + 24, yy - 4, x1 - 24, yy - 4],
                   fill=(255, 255, 255, 26), width=1)
            ftag = font("fa_med", 23)
            d.text((x1 - 40, yy + 4), item["sep"], font=ftag,
                   fill=(255, 255, 255, 185), direction="rtl", anchor="ra")
        elif item.get("segs"):
            draw_ltr_segs(d, item["segs"], lx, yy)
    return y + h

def bullet_card(d, y, sec, idx, item):
    h = sec.get("h", 92)
    gap = sec.get("gap", 12)
    x0, x1 = MARGIN, W - MARGIN
    colors = sec.get("colors", [ACCENT, GREEN, ACCENT2])
    accent = colors[idx % len(colors)]
    d.rounded_rectangle([x0, y, x1, y + h], radius=20, fill=CARD_BG,
                        outline=CARD_BR, width=1)
    d.rounded_rectangle([x1 - 22, y + 14, x1 - 10, y + h - 14], radius=6, fill=accent)
    tx_right = x1 - 44
    draw_rtl_segs(d, item["title"], tx_right, y + 12)
    f_desc = font("fa_med", sec.get("desc_size", 28))
    dsc_dir = "ltr" if item.get("desc_ltr") else "rtl"
    d.text((tx_right, y + int(h * 0.57)), item["desc"], font=f_desc, fill=DESC,
           direction=dsc_dir, anchor="ra")
    return y + h + gap

def grid(d, y, sec):
    """شبکه ۲×۲ کارت — ردیف از راست شروع می‌شود"""
    items = sec["items"]
    ch = sec.get("card_h", 100)
    gy = sec.get("gap", 14)
    gx = 14
    cw = (W - 2 * MARGIN - gx) / 2
    colors = sec.get("colors", [ACCENT, GREEN, ACCENT2, ORANGE])
    f_title = font("fa_bold", 27)
    f_desc = font("fa_med", sec.get("desc_size", 23))
    for idx, item in enumerate(items):
        r, c = divmod(idx, 2)
        x1 = W - MARGIN - c * (cw + gx)
        x0 = x1 - cw
        yy = y + r * (ch + gy)
        accent = colors[idx % len(colors)]
        d.rounded_rectangle([x0, yy, x1, yy + ch], radius=18, fill=CARD_BG,
                            outline=CARD_BR, width=1)
        d.rounded_rectangle([x1 - 18, yy + 12, x1 - 8, yy + ch - 12], radius=5,
                            fill=accent)
        draw_rtl_segs(d, item["title"], x1 - 32, yy + 14)
        dsc_dir = "ltr" if item.get("desc_ltr") else "rtl"
        d.text((x1 - 32, yy + int(ch * 0.58)), item["desc"], font=f_desc,
               fill=DESC, direction=dsc_dir, anchor="ra")
    rows = (len(items) + 1) // 2
    return y + rows * ch + (rows - 1) * gy

def footer(d, y, label=None, handle=None):
    label = label or CHANNEL["footer_label"]
    handle = handle or CHANNEL["handle"]
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

def footer_clean(d, y, label):
    """پانویس نسخهٔ clean — فقط برچسب خنثای محتوا، بدون نام/آدرس انجمن"""
    f = font("fa_bold", 29)
    fi = font("mono", 28)
    icon = "</>"
    w1 = d.textlength(label, font=f, direction="rtl")
    w2 = d.textlength(icon, font=fi, direction="ltr")
    pill_w = w1 + w2 + 84
    x0 = (W - pill_w) / 2
    d.rounded_rectangle([x0, y, x0 + pill_w, y + 62], radius=31,
                        fill=ACCENT2 + (26,), outline=ACCENT2 + (140,), width=2)
    right_edge = (W + (w1 + w2)) / 2
    d.text((right_edge, y + 13), icon, font=fi, fill=ACCENT2,
           direction="ltr", anchor="ra")
    d.text((right_edge - w2, y + 13), label, font=f, fill=WHITE,
           direction="rtl", anchor="ra")
    return y + 62

# ---------- رندر اصلی ----------
def render(spec, out_path, brand=True):
    """spec: {series_label, lesson_no?, title, title_size?, subtitle, sections[],
              note?, clean_footer?}
    توجه: دسته‌بندی محتوا عمداً روی عکس نمی‌آید (قاعدهٔ دائمی — بازخورد کاربر).
    دسته فقط در meta.json/stats ثبت می‌شود.
    """
    img = add_decor(make_background())
    d = ImageDraw.Draw(img, "RGBA")
    y = 46
    if brand:
        y = badge(d, y, img)
    if spec.get("lesson_no"):
        lesson_chip(d, 46, spec["lesson_no"])
    # فاصلهٔ استاندارد بین ردیف بالا و متن‌ها (اصلاح بازخورد: نزدیک نبودن برند به متن)
    y = 46 + 62 + 54
    rtl_right(d, spec["series_label"], font("fa_med", 33), ACCENT, W - MARGIN, y)
    y += 50
    tsize = spec.get("title_size", 62)
    rtl_right(d, spec["title"], font("fa_title", tsize), WHITE, W - MARGIN, y)
    y += int(tsize * 1.22) + 20
    rtl_right(d, spec["subtitle"], font("fa_med", 34), SUB, W - MARGIN, y)
    y += 58
    sections = spec["sections"] if brand else spec.get("clean_sections", spec["sections"])
    for sec in sections:
        if sec["type"] == "code":
            y = code_block(d, y, sec) + 28
        elif sec["type"] == "bullets":
            for idx, item in enumerate(sec["items"]):
                y = bullet_card(d, y, sec, idx, item)
            y += 14
        elif sec["type"] == "grid":
            y = grid(d, y, sec) + 18
    if spec.get("note"):
        y += 4
        rtl_right(d, spec["note"], font("fa_med", 24), SUB, W - MARGIN, y)
        y += 44
    fy = min(y + 6, H - 88)
    if fy - (y + 6) > 8:
        print(f"WARN poster_lib: فضای خالی زیاد پیش از پانویس ({out_path}) y={y}")
    if y + 70 > H - 12:
        print(f"WARN poster_lib: سرریز محتوا ({out_path}) y={y}")
    if brand:
        footer(d, fy)
    else:
        footer_clean(d, fy, spec.get("clean_footer", spec["series_label"]))
    img.save(out_path, "PNG")
    return out_path

# ---------- متن فارسی عکس (بایگانی برای استفادهٔ بعدی) ----------
def segs_text(segs):
    return "".join(s[0] for s in segs)

def spec_text(spec):
    """متن کامل فارسی عکس را به‌صورت Markdown برمی‌گرداند"""
    L = []
    L.append(f"# {spec['title']}")
    head = spec["series_label"]
    if spec.get("lesson_no"):
        head += f" — درس {fa_num(spec['lesson_no'])}"
    L.append(head)
    L.append("")
    L.append(spec["subtitle"])
    L.append("")
    for sec in spec["sections"]:
        if sec["type"] == "code":
            L.append(f"فایل «{sec.get('name', 'main.rs')}»:")
            L.append("```rust")
            for item in sec["lines"]:
                if "sep" in item:
                    L.append(f"# ──── {item['sep']} ────")
                elif item.get("segs"):
                    L.append(segs_text(item["segs"]))
            L.append("```")
            L.append("")
        elif sec["type"] == "bullets":
            for it in sec["items"]:
                L.append(f"- **{segs_text(it['title'])}**: {it['desc']}")
            L.append("")
        elif sec["type"] == "grid":
            for it in sec["items"]:
                L.append(f"- **{segs_text(it['title'])}**: {it['desc']}")
            L.append("")
    if spec.get("note"):
        L.append(f"نکته: {spec['note']}")
        L.append("")
    # دسته‌بندی عمداً در متن هم نمی‌آید — فقط داخلی است
    return "\n".join(L)
