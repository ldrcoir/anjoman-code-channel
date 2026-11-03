#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""گردش ۶۰ — شبیه‌سازی دقیق موتور ورق‌زن کتاب (قبل/بعد از رفع اشکال)
همان ریاضیات VaraghZanView کاتلین با صفحه‌های واقعی کتاب (assets خصوصی)
خروجی: download/preview-g60/*.jpg  +  آزمون جست‌وجو با matn.json واقعی
"""
from pathlib import Path
from PIL import Image, ImageDraw, ImageFont
import numpy as np

OUT = Path("/home/z/my-project/download/preview-g60")
OUT.mkdir(parents=True, exist_ok=True)
PAGES = Path("/tmp/pajoohesh-assets/extracted/pages")
FNT = "/home/z/my-project/scripts/actroot/anjoman-code-bot/kotlin-pajoohesh-app/app/src/main/assets/fonts/Vazirmatn-Bold.ttf"

# ---------- قاب گوشی ----------
W, H = 720, 1280
ZAMIN = (244, 237, 222)          # تم کلاسیک کرم
RANG_ZAMIN_TIR = (23, 20, 18)    # تم زغالی گرم

def safhe(shomare):
    return Image.open(PAGES / f"p{shomare:03d}.webp").convert("RGB")

def mostatil(b, w, h):
    """همان mostatilSafhe کاتلین"""
    nesbe = b.width / b.height
    nesbeGhabe = w / h
    if nesbe > nesbeGhabe:
        hh = w / nesbe
        return (0.0, (h - hh) / 2.0, float(w), (h + hh) / 2.0)
    ww = h * nesbe
    return ((w - ww) / 2.0, 0.0, (w + ww) / 2.0, float(h))

def dar_mostatil(frame, b, rect):
    L, T, R, Bm = rect
    bb = b.resize((max(1, int(R - L)), max(1, int(Bm - T))), Image.LANCZOS)
    frame.paste(bb, (int(L), int(T)))

# ---------- گرادیان افقی (شبیه LinearGradient اندروید) ----------
def grad_pasha(x0, x1, y0, y1, stops, pos=None):
    """لایهٔ RGBA با گرادیان افقی؛ stops=[(r,g,b,a),...]"""
    wpx = max(1, int(x1 - x0))
    r = np.linspace(0.0, 1.0, wpx)
    if pos is None:
        pos = [i / (len(stops) - 1) for i in range(len(stops))]
    pos = np.array(pos, dtype=float)
    chan = []
    for ci in range(4):
        vals = np.array([s[ci] for s in stops], dtype=float)
        chan.append(np.interp(r, pos, vals))
    lay = np.zeros((int(y1 - y0), wpx, 4), dtype=np.uint8)
    for ci in range(4):
        lay[:, :, ci] = chan[ci][None, :]
    return Image.fromarray(lay, "RGBA")

def composite(frame, lay, x0, y0):
    frame.paste(lay, (int(x0), int(y0)), lay)

# ---------- موتور جدید (همان ریاضیات کاتلین اصلاح‌شده) ----------
def lift_dar(x, c, cy, max_lift):
    if x <= c:
        return 0.0
    return max_lift * np.sin(np.pi * min(1.0, (x - c) / (2.2 * cy)))

def sahneh_jadid(frame, barg, zir, te, jahat):
    rect = mostatil(barg, W, H)
    L, T, R, Bm = rect
    pahna, boland = R - L, Bm - T
    cy = pahna * 0.05
    c = (L - cy + (pahna + 2 * cy) * te) if jahat == 1 else (R + cy - (pahna + 2 * cy) * te)
    max_lift = boland * 0.055 * (1 - 0.55 * te)

    # ۱) زیرِ برگه
    dar_mostatil(frame, zir, rect)
    # ۲) سایهٔ برگه روی زیر
    if L < c < R + cy:
        x0 = max(L, c - 1.9 * cy)
        composite(frame, grad_pasha(x0, c, T, Bm, [(0, 0, 0, 0), (0, 0, 0, 96)]), x0, T)
    # ۳) بخش تختِ برگه
    if c < R and c > L - cy:
        az = max(L, c)
        qab = frame.crop((int(az), int(T), int(R), int(Bm))).copy()
        src = barg.resize((int(pahna), int(boland)), Image.LANCZOS)
        frame.paste(src.crop((int(az - L), 0, src.width, src.height)), (int(az), int(T)))
        if c > L:
            x1 = min(R, c + cy)
            composite(frame, grad_pasha(c, x1, T, Bm, [(0, 0, 0, 60), (0, 0, 0, 0)]), c, T)
    # ۴) پشتِ کاغذ
    if L < c < R:
        labe = min(L + 2 * (c - L), R)
        if labe > c:
            rah, noghat = 14, []
            for i in range(rah + 1):
                x = c + (labe - c) * i / rah
                noghat.append((x, T - lift_dar(x, c, cy, max_lift)))
            noghat.append((labe, Bm - lift_dar(labe, c, cy, max_lift)))
            for i in range(rah, -1, -1):
                x = c + (labe - c) * i / rah
                noghat.append((x, Bm - lift_dar(x, c, cy, max_lift)))
            mask = Image.new("L", (W, H), 0)
            ImageDraw.Draw(mask).polygon([(p[0], p[1]) for p in noghat], fill=255)
            grad = grad_pasha(c, labe, 0, 1, [(231, 223, 201, 255), (250, 246, 234, 255), (242, 236, 220, 255)], [0.0, 0.35, 1.0])
            kaghaz = Image.new("RGBA", (W, H), (0, 0, 0, 0))
            kaghaz.paste(grad.resize((max(1, int(labe - c)), H)), (int(c), 0))
            frame.paste(kaghaz.convert("RGB"), (0, 0), mask)
            # سایهٔ استوانه فقط داخل برگه (ترکیب آلفای واقعی)
            x1 = min(R, c + 2.4 * cy)
            if x1 > c:
                box = (int(c), max(0, int(T - max_lift)), int(x1), int(Bm))
                bw, bh = max(1, box[2] - box[0]), max(1, box[3] - box[1])
                saye = np.asarray(grad_pasha(box[0], box[2], box[1], box[3], [(0, 0, 0, 78), (0, 0, 0, 0)]).resize((bw, bh)), dtype=float)
                ma = (np.asarray(mask.crop(box).resize((bw, bh)), dtype=float) / 255.0)[..., None]
                reg = np.asarray(frame.crop(box).resize((bw, bh)).convert("RGB"), dtype=float)
                aa = saye[..., 3:4] / 255.0 * ma
                out = reg * (1 - aa) + saye[..., :3] * aa
                frame.paste(Image.fromarray(out.astype(np.uint8)), (box[0], box[1]))
    # ۵) سایهٔ لبهٔ آزاد
    labe2 = L + 2 * (c - L)
    dp1 = 3.0
    if labe2 < R - dp1 and c > L + dp1:
        composite(frame, grad_pasha(labe2 - 7 * dp1, labe2 + 4 * dp1, T, Bm, [(0, 0, 0, 0), (0, 0, 0, 70)]), labe2 - 7 * dp1, T)
    # ۶) درخشش خط تاشدن
    if L < c < R:
        composite(frame, grad_pasha(c - 1.5 * dp1, c + 2.5 * dp1, T, Bm, [(0, 0, 0, 0), (255, 255, 255, 58), (0, 0, 0, 0)], [0.0, 0.5, 1.0]), c - 1.5 * dp1, T)

# ---------- موتور قدیم (با اشکال — برای مقایسهٔ قبل) ----------
def flap_mesh_kohan(frame, pust, rect, c, cy, max_lift, tedad_sotoon=28):
    """مش تاشدهٔ قدیم: آینهٔ صفحهٔ مقصد حول خط تاشدن"""
    L, T, R, Bm = rect
    pahna = R - L
    src = pust.resize((int(pahna), int(Bm - T)), Image.LANCZOS)
    for k in range(tedad_sotoon + 1):
        X = k / tedad_sotoon * pahna
        x = L + 2 * (c - L) - X
        if x > R: x = R
        if x >= c:
            lift = max_lift * np.sin(np.pi * min(1.0, (x - c) / (2.2 * cy)))
        else:
            x, lift = c, 0.0
        # نوار متناظر از مبدأ
        x0_src = min(src.width - 1, max(0, int(X / pahna * src.width)))
        strip = src.crop((x0_src, 0, min(src.width, x0_src + max(1, src.width // tedad_sotoon)), src.height))
        frame.paste(strip, (int(x), int(T - lift)))
    # سایهٔ استوانه
    x1 = min(R, c + 2.4 * cy)
    if x1 > c:
        composite(frame, grad_pasha(c, x1, T - max_lift, Bm, [(0, 0, 0, 78), (0, 0, 0, 0)]), c, T - max_lift)

def sahneh_kohan_jelo(frame, ru, pust, te):
    rect = mostatil(ru, W, H)
    L, T, R, Bm = rect
    pahna, boland = R - L, Bm - T
    cy = pahna * 0.05
    c = L - cy + (pahna + 2 * cy) * te
    max_lift = boland * 0.055 * (1 - 0.55 * te)
    dar_mostatil(frame, pust, rect)                      # زیر: مقصد
    if L < c < R + cy:
        x0 = max(L, c - 1.9 * cy)
        composite(frame, grad_pasha(x0, c, T, Bm, [(0, 0, 0, 0), (0, 0, 0, 96)]), x0, T)
    if c < R:
        az = max(L, c)
        src = ru.resize((int(pahna), int(boland)), Image.LANCZOS)
        frame.paste(src.crop((min(src.width, int(az - L)), 0, src.width, src.height)), (int(az), int(T)))
        if c > L:
            x1 = min(R, c + cy)
            composite(frame, grad_pasha(c, x1, T, Bm, [(0, 0, 0, 60), (0, 0, 0, 0)]), c, T)
    if L < c < R + 2 * cy:
        flap_mesh_kohan(frame, pust, rect, c, cy, max_lift)   # کپی مقصد — همان ایراد!
    labe = L + 2 * (c - L)
    dp1 = 3.0
    if labe < R - dp1 and c > L + dp1:
        composite(frame, grad_pasha(labe - 7 * dp1, labe + 4 * dp1, T, Bm, [(0, 0, 0, 0), (0, 0, 0, 70)]), labe - 7 * dp1, T)
    if L < c < R:
        composite(frame, grad_pasha(c - 1.5 * dp1, c + 2.5 * dp1, T, Bm, [(0, 0, 0, 0), (255, 255, 255, 58), (0, 0, 0, 0)], [0.0, 0.5, 1.0]), c - 1.5 * dp1, T)

# ---------- شب‌خوانی (ماتریس کاتلین) ----------
def filter_shab(img):
    a = np.asarray(img.convert("RGB"), dtype=float)
    lum = 0.33 * a[:, :, 0] + 0.59 * a[:, :, 1] + 0.11 * a[:, :, 2]
    inv = np.clip(255.0 - lum, 0, 255)          # همان گیرِ [۰..۲۵۵] اندروید
    # گرما: r+=0.05g , g+=0.02b روی تصویر وارونه
    r2 = np.clip(inv + 0.05 * inv, 0, 255)
    g2 = np.clip(inv + 0.02 * inv, 0, 255)
    out = np.stack([r2, g2, inv], axis=2).astype(np.uint8)
    return Image.fromarray(out)

# ---------- برچسب فارسی ----------
f_lab = ImageFont.truetype(FNT, 30)
def label(img, matn):
    d = ImageDraw.Draw(img)
    d.rectangle([0, 0, img.width, 56], fill=(20, 20, 20))
    d.text((img.width - 18, 28), matn, font=f_lab, fill=(255, 255, 255), anchor="rm", direction="rtl", language="fa")
    return img

# ---------- ساخت شبکه‌ها ----------
p5, p6 = safhe(5), safhe(6)
p4 = safhe(4)

# قبل — جلو (کپی مقصد روی برگه)
tiles = []
for te in (0.2, 0.45, 0.7, 0.9):
    f = Image.new("RGB", (W, H), ZAMIN)
    sahneh_kohan_jelo(f, p5, p6, te)
    tiles.append(label(f, f"قبل — ورق به جلو ({int(te*100)}٪) — ایراد: کپی صفحهٔ مقصد روی برگه"))
grid = Image.new("RGB", (W * 2, (H + 8) * 2), (40, 40, 40))
for i, t in enumerate(tiles):
    grid.paste(t, ((i % 2) * W, (i // 2) * (H + 8)))
grid.resize((W, (H + 8))).save(OUT / "ghabl-jelo.jpg", quality=86)

# قبل — عقب (متن آینه‌ای)
tiles = []
for te in (0.2, 0.45, 0.7, 0.9):
    f = Image.new("RGB", (W, H), ZAMIN)
    sahneh_kohan_jelo(f, p5, p4, te)     # ru=فعلی، pust=قبلی
    f = f.transpose(Image.FLIP_LEFT_RIGHT)  # آینهٔ کل صحنه = اشکال متن آینه‌ای
    tiles.append(label(f, f"قبل — برگشت به عقب ({int(te*100)}٪) — ایراد: متن آینه‌ای"))
grid = Image.new("RGB", (W * 2, (H + 8) * 2), (40, 40, 40))
for i, t in enumerate(tiles):
    grid.paste(t, ((i % 2) * W, (i // 2) * (H + 8)))
grid.resize((W, (H + 8))).save(OUT / "ghabl-aghab.jpg", quality=86)

# بعد — جلو
tiles = []
for te in (0.12, 0.3, 0.5, 0.7, 0.88, 0.99):
    f = Image.new("RGB", (W, H), ZAMIN)
    sahneh_jadid(f, p5, p6, te, 1)
    tiles.append(label(f, f"بعد — ورق به جلو ({int(te*100)}٪): زیر=صفحهٔ بعد، برگه=همین صفحه، پشت=کاغذ"))
grid = Image.new("RGB", (W * 3, (H + 8) * 2), (40, 40, 40))
for i, t in enumerate(tiles):
    grid.paste(t, ((i % 3) * W, (i // 3) * (H + 8)))
grid.resize((W * 3 // 2, (H + 8))).save(OUT / "bad-jelo.jpg", quality=86)

# بعد — عقب
tiles = []
for te in (0.12, 0.3, 0.5, 0.7, 0.88, 0.99):
    f = Image.new("RGB", (W, H), ZAMIN)
    sahneh_jadid(f, p4, p5, te, -1)      # برگه=صفحهٔ قبلی(مقصد)، زیر=صفحهٔ فعلی
    tiles.append(label(f, f"بعد — برگشت به عقب ({int(te*100)}٪): بدون هیچ متن آینه‌ای"))
grid = Image.new("RGB", (W * 3, (H + 8) * 2), (40, 40, 40))
for i, t in enumerate(tiles):
    grid.paste(t, ((i % 3) * W, (i // 3) * (H + 8)))
grid.resize((W * 3 // 2, (H + 8))).save(OUT / "bad-aghab.jpg", quality=86)

# شب‌خوانی در تم تیره — قبل/بعد (فیلتر فقط روی صفحهٔ کتاب اعمال می‌شود، مثل اندروید)
tir = Image.new("RGB", (W, H), RANG_ZAMIN_TIR)
rect5 = mostatil(p5, W, H)
ghabl = tir.copy()
dar_mostatil(ghabl, filter_shab(p5), rect5)   # رفتار قدیم: فیلتر وارونه روی صفحه
bad = tir.copy()
dar_mostatil(bad, p5, rect5)                   # رفتار جدید: در تم تیره فیلتر قفل است
comp = Image.new("RGB", (W * 2 + 8, H), (40, 40, 40))
comp.paste(label(ghabl, "قبل: تم تیره + شب‌خوانی = صفحه تیره در تاریکی"), (0, 0))
comp.paste(label(bad, "بعد: در تم تیره شب‌خوانی قفل است — صفحه خوانا"), (W + 8, 0))
comp.resize((W, H // 2)).save(OUT / "shab-dar-tem-tir.jpg", quality=86)

print("SIM OK — frames in", OUT)
for f in sorted(OUT.glob("*.jpg")):
    print(" -", f.name, f.stat().st_size // 1024, "KB")
