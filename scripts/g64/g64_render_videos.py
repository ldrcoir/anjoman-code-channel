#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""گردش ۶۴ — رندر دوبارهٔ دو کلیپ اسکرچ به فرمان سردبیر:
۱) عنوان سه‌نامی همیشه بالای صحنه (نه فقط چند ثانیه) و جمع‌وجور بدون تداخل
۲) هیچ فریمی در download ساخته نمی‌شود — فریم‌ها در /tmp هستند و بعد از انکود پاک می‌شوند
۳) حجم کم: ۲۴fps + crf ۲۶ + مقیاس ۷۲۰ → هدف زیر ~۱.۵ مگابایت برای ~۲۴ ثانیه"""
from pathlib import Path
from PIL import Image
import subprocess, math, shutil

OUT = Path("/home/z/my-project/download/anim-scratch-enerjy")
ASSETS = OUT / "_assets"
TMP = Path("/tmp/clip64")
S = 2  # ۹۶۰×۷۲۰

SVGS_BAD = {
    "backdrop": ("backdrop.svg", 480, 360), "sun": ("sun.svg", 120, 120),
    "cloud1": ("cloud1.svg", 180, 70), "cloud2": ("cloud2.svg", 180, 70),
    "tower": ("tower.svg", 90, 160), "rotor": ("rotor.svg", 120, 120),
    "wave": ("wave.svg", 190, 30), "javaneh1": ("javaneh1.svg", 80, 100),
    "javaneh2": ("javaneh2.svg", 80, 100), "javaneh3": ("javaneh3.svg", 80, 100),
}
SVGS_KHORSHID = {
    "kbackdrop": ("khorshidi_backdrop.svg", 480, 360), "ksun": ("khorshid.svg", 120, 120),
    "kpanel": ("panel.svg", 120, 80), "kjaryan": ("jaryan.svg", 40, 40),
    "kcheragh": ("cheragh.svg", 80, 80), "kbat0": ("bat0.svg", 60, 84),
    "kbat1": ("bat1.svg", 60, 84), "kbat2": ("bat2.svg", 60, 84),
}

from playwright.sync_api import sync_playwright
with sync_playwright() as pw:
    browser = pw.chromium.launch()
    page = browser.new_page(viewport={"width": 520, "height": 420}, device_scale_factor=S)
    for d in (SVGS_BAD, SVGS_KHORSHID):
        for key, (fname, w, h) in d.items():
            if (ASSETS / f"{key}_2x.png").exists():
                continue
            svg = (ASSETS / fname).read_text(encoding="utf-8")
            html = f"""<!DOCTYPE html><html><head><meta charset="utf-8"></head>
<body style="margin:0;background:transparent">
<div id="s" style="width:{w}px;height:{h}px">{svg}</div></body></html>"""
            page.set_content(html)
            page.locator("#s").screenshot(path=str(ASSETS / f"{key}_2x.png"), omit_background=True)
            print("rasterized:", key)
    browser.close()

CW, CH = 480 * S, 360 * S
FPS = 24

def paste(img, sx, sy, cx, cy, deg=0.0, scale=1.0, alpha=1.0, piksel=False):
    im = img
    if not piksel:
        cx, cy = cx * S, cy * S
    if scale != 1.0:
        im = im.resize((int(im.width * scale), int(im.height * scale)), Image.LANCZOS)
        cx, cy = cx * scale, cy * scale
    if deg:
        im = im.rotate(-deg, resample=Image.BICUBIC, center=(cx, cy))
    if alpha < 1.0:
        a = im.getchannel("A").point(lambda v: int(v * alpha))
        im = im.copy(); im.putalpha(a)
    X = int((sx + 240) * S - cx)
    Y = int((180 - sy) * S - cy)
    return im, X, Y

def tri(t, period):
    p = (t % period) / period
    return p * 2 if p < 0.5 else 2 - p * 2

def film(base_img, ghabe, onvan_img, onvan_pos, dor, mp4_name, fr_dir):
    """فریم‌ها فقط در /tmp؛ عنوان همیشه با شفافیت ثابت بالای صحنه."""
    fr = TMP / fr_dir
    if fr.exists(): shutil.rmtree(fr)
    fr.mkdir(parents=True)
    NF = FPS * dor
    for n in range(NF):
        t = n / FPS
        frame = base_img.copy()
        ghabe(t, frame, n)
        im, X, Y = paste(onvan_img, onvan_pos[0], onvan_pos[1], 520, 92, alpha=0.95, piksel=True)
        frame.alpha_composite(im, (X, Y))
        frame.convert("RGB").save(fr / f"{n:05d}.jpg", quality=88)
        if n % 240 == 0: print(f"  frame {n+1}/{NF}")
    mp4 = OUT / mp4_name
    subprocess.run(["ffmpeg", "-y", "-r", str(FPS), "-i", str(fr / "%05d.jpg"),
                    "-vf", "scale=720:540", "-c:v", "libx264", "-crf", "26",
                    "-preset", "medium", "-pix_fmt", "yuv420p", "-movflags", "+faststart",
                    str(mp4)], check=True, stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)
    shutil.rmtree(fr)
    print("mp4:", mp4.name, round(mp4.stat().st_size / 1024), "KB")

# ═══ فیلم باد ═══
IMG = {k: Image.open(ASSETS / f"{k}_2x.png").convert("RGBA") for k in SVGS_BAD}
TITLE_BAD = Image.open(ASSETS / "title_bad.png").convert("RGBA")
base_bad = Image.open(ASSETS / "backdrop_2x.png").convert("RGBA")
xw1, xw2 = 0.0, -120.0

def ghabe_bad(t, frame, n):
    global xw1, xw2
    im, X, Y = paste(IMG["sun"], -170, 95, 60, 60, deg=4 * n)
    frame.alpha_composite(im, (X, Y))
    c1x = -240 + 480 * tri(t, 14)
    im, X, Y = paste(IMG["cloud1"], c1x, 140, 90, 35)
    frame.alpha_composite(im, (X, Y))
    c2x = 240 - 480 * tri(t, 10)
    im, X, Y = paste(IMG["cloud2"], c2x, 92, 90, 35, scale=0.7)
    frame.alpha_composite(im, (X, Y))
    im, X, Y = paste(IMG["tower"], 150, 40, 45, 16)
    frame.alpha_composite(im, (X, Y))
    im, X, Y = paste(IMG["rotor"], 150, 40, 60, 60, deg=6 * n)
    frame.alpha_composite(im, (X, Y))
    im, X, Y = paste(IMG["tower"], -95, 40, 45, 16, scale=0.75)
    frame.alpha_composite(im, (X, Y))
    im, X, Y = paste(IMG["rotor"], -95, 40, 60, 60, deg=9 * n, scale=0.75)
    frame.alpha_composite(im, (X, Y))
    xw1 -= 4
    if xw1 < -250: xw1 = 150
    xw2 -= 3
    if xw2 < -250: xw2 = 150
    im, X, Y = paste(IMG["wave"], xw1, -128, 95, 15)
    frame.alpha_composite(im, (X, Y))
    im, X, Y = paste(IMG["wave"], xw2, -146, 95, 15)
    frame.alpha_composite(im, (X, Y))
    cyc = t % 4.5
    if cyc < 1:   cost, sz = "javaneh1", 0.55
    elif cyc < 2: cost, sz = "javaneh2", 0.80
    else:         cost, sz = "javaneh3", 1.00
    im, X, Y = paste(IMG[cost], 170, -88, 40, 92, scale=sz)
    frame.alpha_composite(im, (X, Y))

film(base_bad, ghabe_bad, TITLE_BAD, (0, 128), 24, "enerjy-novin-run.mp4", "f_bad")

# ═══ فیلم خورشیدی ═══
KIMG = {k: Image.open(ASSETS / f"{k}_2x.png").convert("RGBA") for k in SVGS_KHORSHID}
TITLE_KHOR = Image.open(ASSETS / "title_khorshid.png").convert("RGBA")
base_kh = Image.open(ASSETS / "kbackdrop_2x.png").convert("RGBA")

def ghabe_kh(t, frame, n):
    p = tri(t, 18)
    ksun_x = 210 - 420 * p
    ksun_y = 110 - 5 * p
    im, X, Y = paste(KIMG["ksun"], ksun_x, ksun_y, 60, 60)
    frame.alpha_composite(im, (X, Y))
    deg = 8 * math.sin(2 * math.pi * t / 3.2)
    im, X, Y = paste(KIMG["kpanel"], 153, -34, 60, 62, deg=deg)
    frame.alpha_composite(im, (X, Y))
    for off, (x0, y0, x1, y1) in [(0.0, (150, -45, 0, -70)), (0.7, (0, -70, -155, -55))]:
        ph = ((t - off) / 1.4) % 1.0
        if ph >= 0:
            jx = x0 + (x1 - x0) * ph
            jy = y0 + (y1 - y0) * ph
            ja = max(0.0, math.sin(math.pi * ph))
            im, X, Y = paste(KIMG["kjaryan"], jx, jy, 20, 20, alpha=ja)
            frame.alpha_composite(im, (X, Y))
    sz = 1.0 + 0.12 * math.sin(2 * math.pi * t / 2.4)
    im, X, Y = paste(KIMG["kcheragh"], -155, -58, 40, 40, scale=sz)
    frame.alpha_composite(im, (X, Y))
    cyc = t % 4.2
    if cyc < 1.4:   bat = "kbat0"
    elif cyc < 2.8: bat = "kbat1"
    else:           bat = "kbat2"
    im, X, Y = paste(KIMG[bat], -1, -71, 30, 74, scale=0.65)
    frame.alpha_composite(im, (X, Y))

film(base_kh, ghabe_kh, TITLE_KHOR, (0, 128), 22, "khorshidi-run.mp4", "f_kh")

# سه فریم کنترل فقط در /tmp برای بازبینی چشمی خودم
for tag, mp4, secs in [("bad", "enerjy-novin-run.mp4", [0.6, 12.0]), ("kh", "khorshidi-run.mp4", [0.6, 8.0])]:
    for sec in secs:
        idx = int(sec * FPS)
        subprocess.run(["ffmpeg", "-y", "-i", str(OUT / mp4), "-vf", f"select=eq(n\\,{idx})",
                        "-vframes", "1", str(TMP / f"chk-{tag}-{int(sec)}.jpg")],
                       stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)
print("done")
