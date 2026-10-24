#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
enerjy_poster — موتور مستقل عکس‌نوشتهٔ کانال «انرژی‌پژوهان اشرفی اصفهانی» (گردش ۲۴)

چرا موتور جدا؟ (دستور مالک، ۱۴۰۵-۰۸-۰۳): «پوستر انرژی خیلی شبیه کدنویسی بود؛
اصلاً شبیه نباشند.» تا امروز انرژی با poster_lib کدنویسی رندر می‌شد و فقط رنگش
فرق داشت. از این پس هویت بصری دو کانال کاملاً جداست:

   کدنویسی (poster_lib)          |  انرژی‌پژوهان (این فایل)
   ------------------------------+--------------------------------
   پس‌زمینهٔ سرمه‌ای تیره          |  پس‌زمینهٔ روشن کرم → سبزکمرنگ
   چیپ برند شناور بالا-راست       |  نوار سربرگ تمام‌عرض سبز تیره + خورشید
   عنوان راست‌چین، فونت لاله‌زار    |  عنوان وسط‌چین، فونت استعداد
   کارت شیشه‌ای + نوار رنگی راست   |  ردیف سفید مات + کپ رنگی چپ + آیکون برداری
   قرص شناور پانویس               |  نوار پانویس تمام‌عرض سبز
   نقطه‌چین + نمادهای کد           |  خورشید/تپه‌های نقاشی‌شده، بی‌نماد کد
   عزا: مشکی + طلایی + قاب دوخطه  |  عزا: سبزِ کاج + نقره‌ای + قاب یکخطه + فونت نسخ

رندر فارسی: Pillow + libraqm (direction='rtl' روی متن خام — بدون arabic_reshaper).
رنگ‌های spec (که از poster_lib می‌آیند) از لنز تمِ همین موتور عبور می‌کنند تا روی
پس‌زمینهٔ روشن خوانا بمانند؛ ساختار spec با poster_lib سازگار است.
"""
import os
import math
from pathlib import Path
from PIL import Image, ImageDraw, ImageFont

W, H = 1080, 1350
MARGIN = 64
HEADER_H = 152
FOOTER_H = 112

FONT_DIR = os.environ.get("ANJOMAN_FONT_DIR") or str(
    Path(__file__).resolve().parent.parent / "assets" / "fonts")
MONO_PATH = "/usr/share/fonts/truetype/dejavu/DejaVuSansMono.ttf"

# ---------- پالت روشن «تابلوی آفتاب» ----------
CREAM_TOP = (251, 248, 238)
CREAM_BOT = (231, 243, 228)
DEEP_0    = (17, 56, 34)     # بالای نوار سربرگ
DEEP_1    = (26, 92, 55)     # پایین نوار سربرگ
INK       = (23, 63, 42)     # عنوان
TEAL_K    = (13, 116, 140)   # کیکر
AMBER     = (245, 158, 11)
AMBER_D   = (180, 83, 9)
SUB_C     = (86, 104, 89)    # زیرعنوان
MUTED_C   = (107, 127, 113)  # خط نام/نکته
ROW_BG    = (255, 255, 255, 216)
ROW_DESC  = (91, 104, 95)
SKY       = (2, 132, 199)
GRN       = (22, 163, 74)
VIO       = (124, 58, 237)
ORG       = (234, 88, 12)
ICON_COLORS = [GRN, SKY, AMBER_D, VIO, ORG, TEAL_K]

# ---------- پالت عزای انرژی (سبزِ کاج + نقره‌ای) ----------
M_BG_TOP  = (10, 20, 15)
M_BG_BOT  = (19, 37, 27)
M_FRAME   = (185, 207, 192)
M_SILVER  = (226, 236, 229)
M_MUTED   = (158, 184, 168)
M_ACC     = (168, 200, 178)
M_QUOTE   = (208, 224, 213)

# نگاشت رنگ‌های poster_lib (که spec ها import می‌کنند) به رنگ‌های خوانای این تم
P_CYAN   = (34, 211, 238)
P_GREEN  = (52, 211, 153)
P_VIOLET = (167, 139, 250)
P_ORANGE = (251, 146, 60)
P_GOLD   = (222, 185, 100)
P_GOLDS  = (176, 142, 72)
P_WHITE  = (255, 255, 255)
P_DESC   = (218, 226, 242)

LIGHT_MAP = {
    P_CYAN: SKY, P_GREEN: GRN, P_VIOLET: VIO, P_ORANGE: ORG,
    P_GOLD: AMBER_D, P_GOLDS: AMBER_D, P_WHITE: INK, P_DESC: ROW_DESC,
    SUB_C: SUB_C, MUTED_C: MUTED_C, INK: INK,
}
MOURN_MAP = {
    P_GOLD: M_ACC, P_GOLDS: M_MUTED, P_WHITE: M_SILVER, P_DESC: M_QUOTE,
    P_CYAN: M_ACC, P_GREEN: M_ACC, P_VIOLET: M_QUOTE, P_ORANGE: M_MUTED,
}

CHANNEL = {
    "brand_name": "انرژی‌پژوهان اشرفی اصفهانی",
    "footer_label": "کانال انرژی‌پژوهان",
    "handle": "@anjoman_energypazhohan",
    "assoc_name": "انجمن دانش‌آموزی انرژی‌های نوین",
    "contest_line": "ششمین دورهٔ مسابقات انرژی‌های نوین",
}
LOGO_PATH = Path("")


def set_channel(cfg):
    global CHANNEL, LOGO_PATH
    CHANNEL = {
        "brand_name": cfg.get("brand_name", CHANNEL["brand_name"]),
        "footer_label": cfg.get("footer_label", CHANNEL["footer_label"]),
        "handle": cfg.get("handle", CHANNEL["handle"]),
        "assoc_name": cfg.get("association", CHANNEL["assoc_name"]),
        "contest_line": CHANNEL["contest_line"],
    }
    logo = cfg.get("_logo_path")
    LOGO_PATH = Path(logo) if logo else Path("")


FAM = {
    "fa_bold":  f"{FONT_DIR}/Vazirmatn-Bold.ttf",
    "fa_med":   f"{FONT_DIR}/Vazirmatn-Medium.ttf",
    "fa_black": f"{FONT_DIR}/Vazirmatn-Black.ttf",
    "fa_title": f"{FONT_DIR}/Estedad-Bold.ttf",      # عنوانِ انرژی — متفاوت از لاله‌زار کدنویسی
    "naskh":    f"{FONT_DIR}/NotoNaskhArabic-Bold.ttf",  # عنوانِ عزای انرژی
}

def font(key, size):
    path = MONO_PATH if key == "mono" else FAM.get(key, FAM["fa_bold"])
    return ImageFont.truetype(path, size)

FA_DIGITS = "۰۱۲۳۴۵۶۷۸۹"
def fa_num(n):
    return "".join(FA_DIGITS[int(c)] if c.isdigit() else c for c in str(n))

# ---------- رنگ spec → رنگ تم ----------
def tint(color, mourn):
    if mourn:
        return MOURN_MAP.get(color, color)
    return LIGHT_MAP.get(color, color)

# ---------- سگمنت‌ها (سازگار با فرمت poster_lib) ----------
def seg_w(d, seg, mourn):
    text, family, size, color, is_fa = seg
    return d.textlength(text, font=font(family, size),
                        direction="rtl" if is_fa else "ltr")

def draw_rtl_segs(d, segs, right_x, y, mourn):
    cur = right_x
    for seg in segs:
        text, family, size, color, is_fa = seg
        w = seg_w(d, seg, mourn)
        d.text((cur, y), text, font=font(family, size), fill=tint(color, mourn),
               direction="rtl" if is_fa else "ltr", anchor="ra")
        cur -= w

def center_text(d, text, key, size, color, y, direction="rtl"):
    d.text((W / 2, y), text, font=font(key, size), fill=color,
           direction=direction, anchor="ma")

# ---------- پس‌زمینه ----------
def _vgrad(img, y0, y1, c0, c1):
    px = img.load()
    for y in range(y0, y1):
        t = (y - y0) / max(1, (y1 - y0 - 1))
        px_row = tuple(int(c0[i] + (c1[i] - c0[i]) * t) for i in range(3))
        for x in range(W):
            px[x, y] = px_row

def bg_light():
    img = Image.new("RGB", (W, H), CREAM_TOP)
    _vgrad(img, 0, H, CREAM_TOP, CREAM_BOT)
    layer = Image.new("RGBA", (W, H), (0, 0, 0, 0))
    d = ImageDraw.Draw(layer)
    # خورشید بزرگِ محوِ بالا-چپ + پرتوهای ملایم
    d.ellipse([-210, -230, 210, 190], fill=AMBER + (34,))
    for i in range(12):
        a = i * (math.pi / 6)
        d.line([120 * math.cos(a), -20 + 120 * math.sin(a),
                205 * math.cos(a), -20 + 205 * math.sin(a)],
               fill=AMBER + (26,), width=10)
    # تپه‌های سبز پایین — به‌جای نقطه‌چین و نماد کد
    d.ellipse([-260, H - 210, 520, H + 260], fill=GRN + (30,))
    d.ellipse([560, H - 160, W + 300, H + 320], fill=(13, 116, 140) + (22,))
    return Image.alpha_composite(img.convert("RGBA"), layer).convert("RGB")

def bg_mourn():
    img = Image.new("RGB", (W, H), M_BG_TOP)
    _vgrad(img, 0, H, M_BG_TOP, M_BG_BOT)
    layer = Image.new("RGBA", (W, H), (0, 0, 0, 0))
    d = ImageDraw.Draw(layer)
    d.ellipse([W / 2 - 430, -320, W / 2 + 430, 190], fill=M_ACC + (16,))
    # قاب یکخطهٔ نقره‌ای + خط داخلی بسیار کم‌رنگ (کدنویسی: قاب دوخطهٔ طلایی)
    d.rectangle([26, 26, W - 26, H - 26], outline=M_FRAME + (86,), width=2)
    d.rectangle([35, 35, W - 35, H - 35], outline=M_FRAME + (24,), width=1)
    # ستارهٔ هشت‌پر نورانی بالای قاب
    cx, cy, r = W / 2, 84, 15
    pts = []
    for i in range(16):
        ang = i * (math.pi / 8) - math.pi / 2
        rr = r if i % 2 == 0 else r * 0.42
        pts.append((cx + rr * math.cos(ang), cy + rr * math.sin(ang)))
    d.polygon(pts, fill=M_FRAME + (170,))
    return Image.alpha_composite(img.convert("RGBA"), layer).convert("RGB")

# ---------- آیکون‌های برداری (جای ایموجی — مطمئن و یکدست) ----------
def icon_sun(d, cx, cy, r, c):
    d.ellipse([cx - r * 0.42, cy - r * 0.42, cx + r * 0.42, cy + r * 0.42], fill=c)
    for i in range(8):
        a = i * (math.pi / 4)
        d.line([cx + r * 0.58 * math.cos(a), cy + r * 0.58 * math.sin(a),
                cx + r * 0.95 * math.cos(a), cy + r * 0.95 * math.sin(a)], fill=c, width=3)

def icon_bolt(d, cx, cy, r, c):
    pts = [(0.02, -1), (0.5, -0.18), (0.16, -0.18), (0.38, 1), (-0.5, 0.06), (-0.12, 0.06)]
    d.polygon([(cx + px * r, cy + py * r) for px, py in pts], fill=c)

def icon_leaf(d, cx, cy, r, c):
    pts_l, pts_r = [], []
    for i in range(13):
        t = i / 12
        y = cy + (t - 0.5) * 2 * r
        wdt = r * 0.62 * (1 - abs(t - 0.42) * 1.7)
        pts_l.append((cx - max(1.5, wdt), y))
        pts_r.append((cx + max(1.5, wdt), y))
    d.polygon(pts_l + pts_r[::-1], fill=c)
    d.line([cx, cy - r, cx, cy + r], fill=(255, 255, 255), width=2)

def icon_wind(d, cx, cy, r, c):
    for k, dy in enumerate((-0.5, 0.05, 0.6)):
        w0 = r * (1.0 if k != 1 else 0.7)
        d.line([cx - w0, cy + dy * r, cx + w0 * 0.55, cy + dy * r], fill=c, width=4)
        d.arc([cx + w0 * 0.05, cy + dy * r - r * 0.3, cx + w0 * 0.85, cy + dy * r + r * 0.3],
              -90, 90, fill=c, width=4)

def icon_drop(d, cx, cy, r, c):
    pts = [(0.0, -1.0), (0.52, -0.22), (0.62, 0.18), (0.34, 0.72), (0.0, 0.86),
           (-0.34, 0.72), (-0.62, 0.18), (-0.52, -0.22)]
    d.polygon([(cx + px * r, cy + py * r) for px, py in pts], fill=c)

def icon_book(d, cx, cy, r, c):
    d.rounded_rectangle([cx - r * 0.9, cy - r * 0.68, cx - r * 0.06, cy + r * 0.68],
                        radius=3, outline=c, width=3)
    d.rounded_rectangle([cx + r * 0.06, cy - r * 0.68, cx + r * 0.9, cy + r * 0.68],
                        radius=3, outline=c, width=3)
    d.line([cx, cy - r * 0.68, cx, cy + r * 0.68], fill=c, width=3)

def icon_flame(d, cx, cy, r, c):
    pts = [(0.0, -1.0), (0.4, -0.42), (0.62, 0.06), (0.4, 0.66), (0.0, 0.9),
           (-0.4, 0.66), (-0.62, 0.06), (-0.4, -0.42)]
    d.polygon([(cx + px * r, cy + py * r) for px, py in pts], fill=c)

ICONS = {"sun": icon_sun, "bolt": icon_bolt, "leaf": icon_leaf, "wind": icon_wind,
         "drop": icon_drop, "book": icon_book, "flame": icon_flame}
ICON_CYCLE = ["sun", "bolt", "leaf", "wind", "drop", "book"]

def draw_icon(d, name, cx, cy, r, c):
    ICONS.get(name, icon_sun)(d, cx, cy, r, c)

# ---------- اجزا ----------
def header_band(d, img, brand):
    _vgrad(img, 0, HEADER_H, DEEP_0, DEEP_1)
    d.line([0, HEADER_H, W, HEADER_H], fill=AMBER, width=4)
    # خورشید کوچک سربرگ — سمت چپ
    icon_sun(d, MARGIN + 26, HEADER_H / 2 - 4, 26, (253, 230, 138))
    if brand:
        f = font("fa_black", 34)
        d.text((W - MARGIN, 34), CHANNEL["brand_name"], font=f, fill=(255, 255, 255),
               direction="rtl", anchor="ra")
        d.text((W - MARGIN, 92), CHANNEL["contest_line"], font=font("fa_med", 21),
               fill=(191, 227, 194), direction="rtl", anchor="ra")

def footer_band(d, img, brand, mourn=False):
    if mourn:
        # عزا: بدون نوار — فقط آیدی کوچک نقره‌ای وسط پایین، داخل قاب
        if brand:
            d.text((W / 2, H - 66), CHANNEL["handle"], font=font("mono", 27),
                   fill=M_MUTED, direction="ltr", anchor="ma")
        return
    y0 = H - FOOTER_H
    _vgrad(img, y0, H, DEEP_0, DEEP_1)
    d.line([0, y0, W, y0], fill=AMBER, width=4)
    if not brand:
        d.text((W / 2, y0 + (FOOTER_H - 34) / 2), CHANNEL["footer_label"],
               font=font("fa_bold", 29), fill=(255, 255, 255), direction="rtl", anchor="ma")
        return
    f_fa = font("fa_bold", 30)
    f_mono = font("mono", 29)
    label, handle = CHANNEL["footer_label"], CHANNEL["handle"]
    w1 = d.textlength(label, font=f_fa, direction="rtl")
    w2 = d.textlength(handle, font=f_mono, direction="ltr")
    gap_v, lw_v = 16, 2
    right_edge = (W + (w1 + gap_v * 2 + lw_v + w2)) / 2
    cy = y0 + FOOTER_H / 2
    d.text((right_edge, cy), label, font=f_fa, fill=(255, 255, 255),
           direction="rtl", anchor="rm")
    sx = right_edge - w1 - gap_v - lw_v / 2
    d.line([sx, cy - 16, sx, cy + 16], fill=(255, 255, 255, 150), width=lw_v)
    d.text((sx - lw_v / 2 - gap_v, cy), handle, font=f_mono, fill=(246, 196, 83),
           direction="ltr", anchor="rm")

# ---------- سکشن‌ها ----------
def row_items(d, y, sec, mourn):
    h = sec.get("h", 100)
    gap = sec.get("gap", 12)
    desc_size = sec.get("desc_size", 26)
    for idx, item in enumerate(sec["items"]):
        accent = tint(tuple(sec.get("row_colors", ICON_COLORS)[idx % len(sec.get("row_colors", ICON_COLORS))]), mourn) \
            if not mourn else M_ACC
        x0, x1 = MARGIN, W - MARGIN
        if mourn:
            # عزا: بدون کارت — دو خط وسط‌چین با جداکنندهٔ نقطه‌ای
            title_segs = [tuple(list(s[:4]) + [s[4]]) for s in item["title"]]
            wtot = sum(seg_w(d, s, mourn) for s in title_segs) + 12 * (len(title_segs) - 1)
            draw_rtl_segs(d, title_segs, (W + wtot) / 2, y + 4, mourn)
            d.text((W / 2, y + 52), item["desc"], font=font("fa_med", desc_size),
                   fill=M_QUOTE, direction="rtl", anchor="ma")
            ny = y + h
            for dx in range(0, 460, 26):
                d.ellipse([W / 2 - dx - 2, ny - 6, W / 2 - dx + 2, ny - 2],
                          fill=M_FRAME + (60,))
                d.ellipse([W / 2 + dx - 2, ny - 6, W / 2 + dx + 2, ny - 2],
                          fill=M_FRAME + (60,))
            y = ny + gap
            continue
        d.rounded_rectangle([x0, y, x1, y + h], radius=22, fill=ROW_BG)
        # کپ رنگی سمت چپ (کدنویسی: نوار سمت راست) + دایرهٔ آیکون
        d.rounded_rectangle([x0 + 16, y + 14, x0 + 29, y + h - 14], radius=6, fill=accent)
        ccx, ccy = x0 + 78, y + h / 2
        d.ellipse([ccx - 27, ccy - 27, ccx + 27, ccy + 27],
                  fill=accent + (36,), outline=accent + (150,), width=2)
        draw_icon(d, item.get("icon", ICON_CYCLE[idx % len(ICON_CYCLE)]), ccx, ccy, 15, accent)
        tx_right = x1 - 36
        draw_rtl_segs(d, item["title"], tx_right, y + 13, mourn)
        dsc_dir = "ltr" if item.get("desc_ltr") else "rtl"
        d.text((tx_right, y + int(h * 0.56)), item["desc"], font=font("fa_bold", desc_size),
               fill=ROW_DESC, direction=dsc_dir, anchor="ra")
        y += h + gap
    return y + 6

def grid_items(d, y, sec, mourn):
    items = sec["items"]
    ch = max(sec.get("card_h", 132), 128)   # حداقل ارتفاع: آیکون + عنوان + توضیح جا شوند
    gy = sec.get("gap", 14)
    gx = 14
    cw = (W - 2 * MARGIN - gx) / 2
    f_desc = font("fa_bold", sec.get("desc_size", 22))
    for idx, item in enumerate(items):
        r_i, c_i = divmod(idx, 2)
        x1 = W - MARGIN - c_i * (cw + gx)
        x0 = x1 - cw
        yy = y + r_i * (ch + gy)
        accent = ICON_COLORS[idx % len(ICON_COLORS)] if not mourn else M_ACC
        if mourn:
            d.rounded_rectangle([x0, yy, x1, yy + ch], radius=18,
                                fill=(255, 255, 255, 14), outline=M_FRAME + (60,), width=1)
        else:
            d.rounded_rectangle([x0, yy, x1, yy + ch], radius=18, fill=ROW_BG)
        ccx, ccy = x0 + cw / 2, yy + 30
        d.ellipse([ccx - 20, ccy - 20, ccx + 20, ccy + 20],
                  fill=accent + (34,), outline=accent + (150,), width=2)
        draw_icon(d, item.get("icon", ICON_CYCLE[idx % len(ICON_CYCLE)]), ccx, ccy, 11, accent)
        # عنوان وسط‌چین داخل کارت (کدنویسی: راست‌چین) — تمایز ساختاری
        title_segs = item["title"]
        wtot = sum(seg_w(d, s, mourn) for s in title_segs)
        draw_rtl_segs(d, title_segs, x0 + cw / 2 + wtot / 2, yy + 52, mourn)
        dsc_dir = "ltr" if item.get("desc_ltr") else "rtl"
        d.text((x0 + cw / 2, yy + ch - 30), item["desc"], font=f_desc,
               fill=ROW_DESC if not mourn else M_QUOTE, direction=dsc_dir, anchor="ma")
    rows = (len(items) + 1) // 2
    return y + rows * ch + (rows - 1) * gy

def quote_block(d, y, sec, mourn):
    pad = sec.get("pad", 36)
    x0, x1 = MARGIN + 30, W - MARGIN - 30
    line_col = (M_FRAME + (110,)) if mourn else (GRN + (130,))
    heights = []
    for ln in sec["lines"]:
        size = ln.get("size", 40)
        heights.append(int(size * 1.34) + ln.get("gap", 10))
    total = pad * 2 + sum(heights)
    d.line([x0, y + 6, x1, y + 6], fill=line_col, width=2)
    d.line([x0, y + total - 6, x1, y + total - 6], fill=line_col, width=2)
    cur = y + pad
    for ln, hh in zip(sec["lines"], heights):
        f = font(ln.get("family", "fa_med" if mourn else "fa_title"), ln.get("size", 40))
        color = tint(ln.get("color", M_SILVER if mourn else INK), mourn)
        d.text((W / 2, cur), ln["text"], font=f, fill=color, direction="rtl", anchor="ma")
        cur += hh
    return y + total

def stat_block(d, y, sec, mourn):
    value, unit = sec["value"], sec.get("unit", "")
    label = sec.get("label", "")
    f_val = font("fa_title", sec.get("size", 78))
    f_unit = font("fa_bold", 32)
    wv = d.textlength(value, font=f_val, direction="rtl")
    wu = d.textlength(" " + unit, font=f_unit, direction="rtl") if unit else 0
    rx = W / 2 + (wv + wu) / 2
    d.text((rx, y), value, font=f_val, fill=tint(AMBER_D, mourn), direction="rtl", anchor="ra")
    if unit:
        d.text((rx - wv, y + 34), " " + unit, font=f_unit, fill=tint(INK, mourn),
               direction="rtl", anchor="ra")
    if label:
        d.text((W / 2, y + int(sec.get("size", 78) * 1.12)), label,
               font=font("fa_bold", 27), fill=tint(MUTED_C, mourn), direction="rtl", anchor="ma")
    return y + int(sec.get("size", 78) * 1.12) + 52

# ---------- رندر اصلی ----------
def render(spec, out_path, brand=True):
    mourn = spec.get("theme") == "mourning"
    img = bg_mourn() if mourn else bg_light()
    d = ImageDraw.Draw(img, "RGBA")
    if mourn:
        y = 132
        center_text(d, spec["series_label"], "fa_bold", 28, M_MUTED, y)
        y += 48
        tsize = spec.get("title_size", 56) + 6
        d.text((W / 2, y), spec["title"], font=font("naskh", tsize), fill=M_SILVER,
               direction="rtl", anchor="ma")
        y += int(tsize * 1.42) + 4
        if brand:
            center_text(d, CHANNEL["brand_name"], "fa_med", 25, M_MUTED, y)
            y += 36
        center_text(d, spec["subtitle"], "fa_bold", 30, M_QUOTE, y)
        y += 58
    else:
        header_band(d, img, brand)
        y = HEADER_H + 42
        center_text(d, spec["series_label"], "fa_bold", 30, TEAL_K, y)
        y += 46
        tsize = spec.get("title_size", 56)
        d.text((W / 2, y), spec["title"], font=font("fa_title", tsize), fill=INK,
               direction="rtl", anchor="ma")
        y += int(tsize * 1.24) + 8
        if brand:
            center_text(d, CHANNEL["assoc_name"], "fa_med", 26, MUTED_C, y)
            y += 38
        # زیرخط کهربایی — امضای قالب انرژی
        d.rounded_rectangle([W / 2 - 75, y, W / 2 + 75, y + 8], radius=4, fill=AMBER)
        y += 30
        center_text(d, spec["subtitle"], "fa_bold", 31, SUB_C, y)
        y += 56
    sections = spec["sections"] if brand else spec.get("clean_sections", spec["sections"])
    for sec in sections:
        st = sec["type"]
        if st == "bullets":
            y = row_items(d, y, sec, mourn) + 16
        elif st == "grid":
            y = grid_items(d, y, sec, mourn) + 20
        elif st == "quote":
            y = quote_block(d, y, sec, mourn) + 24
        elif st == "stat":
            y = stat_block(d, y, sec, mourn) + 12
        elif st == "code":
            # پشتیبان — کدنویسی مال کانال کدنویسی است؛ انرژی اگر خواست متن فنی بگذارد
            d.rounded_rectangle([MARGIN, y, W - MARGIN, y + 120], radius=18,
                                fill=(24, 44, 32), outline=M_FRAME + (50,), width=1)
            d.text((MARGIN + 30, y + 24), "بخش فنی", font=font("fa_bold", 26),
                   fill=M_QUOTE, direction="rtl", anchor="ra")
            y += 140
    if spec.get("note"):
        y += 6
        center_text(d, spec["note"], "fa_bold", 24, MUTED_C if not mourn else M_MUTED, y)
        y += 40
    if not mourn:
        fy = H - FOOTER_H - 20
        if y > fy:
            print(f"WARN enerjy_poster: سرریز محتوا ({out_path}) y={y}")
        footer_band(d, img, brand)
    else:
        if y > H - 130:
            print(f"WARN enerjy_poster: سرریز محتوای عزا ({out_path}) y={y}")
        footer_band(d, img, brand, mourn=True)
    img.save(out_path, "PNG")
    return out_path

# ---------- متن فارسی عکس ----------
def segs_text(segs):
    return "".join(s[0] for s in segs)

def spec_text(spec):
    L = []
    L.append(f"# {spec['title']}")
    L.append(spec["series_label"])
    if CHANNEL.get("brand_name"):
        L.append(CHANNEL["brand_name"])
    L.append("")
    L.append(spec["subtitle"])
    L.append("")
    for sec in spec["sections"]:
        if sec["type"] in ("bullets", "grid"):
            for it in sec["items"]:
                L.append(f"- **{segs_text(it['title'])}**: {it['desc']}")
            L.append("")
        elif sec["type"] == "quote":
            for ln in sec["lines"]:
                L.append(f"«{ln['text']}»" if not ln["text"].startswith("—") else ln["text"])
            L.append("")
        elif sec["type"] == "stat":
            L.append(f"**{sec['value']} {sec.get('unit','')}** — {sec.get('label','')}")
            L.append("")
    if spec.get("note"):
        L.append(f"نکته: {spec['note']}")
        L.append("")
    return "\n".join(L)
