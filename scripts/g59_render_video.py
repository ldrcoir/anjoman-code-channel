#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""گردش ۵۹ — رندر «فیلم اجرا» انیمیشن اسکرچ انرژی‌های نوین
همان منطقِ بلوک‌های sb3 (چرخش خورشید ۴°، روتورها ۶° و ۹°، گلیس ابرها، موج رونده، رشد جوانه، محو عنوان)
خروجی: download/anim-scratch-enerjy/enerjy-novin-run.mp4 (۲۴ ثانیه، ۳۰fps، 960×720) + سه فریم پیش‌نمایش
"""
from pathlib import Path
from playwright.sync_api import sync_playwright
from PIL import Image
import subprocess, math

OUT = Path("/home/z/my-project/download/anim-scratch-enerjy")
ASSETS = OUT / "_assets"
FRAMES = OUT / "_frames"
FRAMES.mkdir(exist_ok=True)
S = 2  # مقیاس رندر (۹۶۰×۷۲۰)

SVGS = {
    "backdrop": ("backdrop.svg", 480, 360),
    "sun": ("sun.svg", 120, 120),
    "cloud1": ("cloud1.svg", 180, 70),
    "cloud2": ("cloud2.svg", 180, 70),
    "tower": ("tower.svg", 90, 160),
    "rotor": ("rotor.svg", 120, 120),
    "wave": ("wave.svg", 190, 30),
    "javaneh1": ("javaneh1.svg", 80, 100),
    "javaneh2": ("javaneh2.svg", 80, 100),
    "javaneh3": ("javaneh3.svg", 80, 100),
}

# ── ۱) شطرنجی‌کردن اس‌وی‌جی‌ها با کرومیوم (پس‌زمینهٔ شفاف، ۲×) ──
with sync_playwright() as pw:
    browser = pw.chromium.launch()
    page = browser.new_page(viewport={"width": 500, "height": 400}, device_scale_factor=S)
    for key, (fname, w, h) in SVGS.items():
        if (ASSETS / f"{key}_2x.png").exists():
            continue
        svg = (ASSETS / fname).read_text(encoding="utf-8")
        html = f"""<!DOCTYPE html><html><head><meta charset="utf-8"></head>
<body style="margin:0;background:transparent">
<div id="s" style="width:{w}px;height:{h}px">{svg}</div></body></html>"""
        page.set_content(html)
        el = page.locator("#s")
        el.screenshot(path=str(ASSETS / f"{key}_2x.png"), omit_background=True)
        print("rasterized:", key)
    browser.close()

IMGS = {k: Image.open(ASSETS / f"{k}_2x.png").convert("RGBA") for k, _ in [(k, v) for k, v in SVGS.items()]}
TITLE = Image.open(ASSETS / "title.png").convert("RGBA")  # 660×150 — روی بوم ۲× مستقیم

CW, CH = 480 * S, 360 * S

def paste(img, sx, sy, cx, cy, deg=0.0, scale=1.0, alpha=1.0, piksel=False):
    """img خام (۲×)؛ (sx,sy) مختصات جهانی اسپرایت؛ (cx,cy) مرکز چرخش
    پیش‌فرض بر حسب واحدِ اس‌وی‌جی (با S ضرب می‌شود)؛ piksel=True یعنی بر حسب پیکسلِ خود تصویر (عنوان png)"""
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
    return (im, X, Y)

FPS = 30
DUR = 24
NF = FPS * DUR

def tri(t, period):
    p = (t % period) / period  # 0..1
    if p < 0.5: return p * 2
    return 2 - p * 2

xw1, xw2 = 0.0, -120.0
base = Image.open(ASSETS / "backdrop_2x.png").convert("RGBA")

for n in range(NF):
    t = n / FPS
    frame = base.copy()
    # خورشید: turn 4 هر فریم
    frame.alpha_composite(*[paste(IMGS["sun"], -170, 110, 60, 60, deg=4 * n)][0:1], dest=None) if False else None
    im, X, Y = paste(IMGS["sun"], -170, 95, 60, 60, deg=4 * n)
    frame.alpha_composite(im, (X, Y))
    # ابرها (گلیس خطی رفت‌وبرگشت)
    c1x = -240 + 480 * tri(t, 14)
    im, X, Y = paste(IMGS["cloud1"], c1x, 140, 90, 35)
    frame.alpha_composite(im, (X, Y))
    c2x = 240 - 480 * tri(t, 10)
    im, X, Y = paste(IMGS["cloud2"], c2x, 92, 90, 35, scale=0.7)
    frame.alpha_composite(im, (X, Y))
    # برج‌ها و روتورها
    im, X, Y = paste(IMGS["tower"], 150, 40, 45, 16)
    frame.alpha_composite(im, (X, Y))
    im, X, Y = paste(IMGS["rotor"], 150, 40, 60, 60, deg=6 * n)
    frame.alpha_composite(im, (X, Y))
    im, X, Y = paste(IMGS["tower"], -95, 40, 45, 16, scale=0.75)
    frame.alpha_composite(im, (X, Y))
    im, X, Y = paste(IMGS["rotor"], -95, 40, 60, 60, deg=9 * n, scale=0.75)
    frame.alpha_composite(im, (X, Y))
    # موج‌ها: هر فریم -۴ و بازگشت از -۲۵۰ به ۱۵۰ (شبیه‌سازی دقیق بلوک‌ها)
    xw1 -= 4
    if xw1 < -250: xw1 = 150
    xw2 -= 3
    if xw2 < -250: xw2 = 150
    im, X, Y = paste(IMGS["wave"], xw1, -128, 95, 15)
    frame.alpha_composite(im, (X, Y))
    im, X, Y = paste(IMGS["wave"], xw2, -146, 95, 15)
    frame.alpha_composite(im, (X, Y))
    # جوانه: چرخهٔ ۴.۵ ثانیه‌ای (۱+۱+۲.۵)
    cyc = t % 4.5
    if cyc < 1:   cost, sz = "javaneh1", 0.55
    elif cyc < 2: cost, sz = "javaneh2", 0.80
    else:         cost, sz = "javaneh3", 1.00
    im, X, Y = paste(IMGS[cost], 170, -88, 40, 92, scale=sz)
    frame.alpha_composite(im, (X, Y))
    # عنوان: ghost 0 → ۹۰ → ۰ (پریود ۳.۵ ثانیه)
    tc = t % 3.5
    if tc < 2.5: ghost = 0
    elif tc < 3.0: ghost = min(90, 6 * int((tc - 2.5) * FPS))
    else: ghost = max(0, 90 - 6 * int((tc - 3.0) * FPS))
    alpha = (100 - ghost) / 100.0
    im, X, Y = paste(TITLE, 60, 112, 330, 75, alpha=alpha, piksel=True)
    frame.alpha_composite(im, (X, Y))
    frame.convert("RGB").save(FRAMES / f"{n:04d}.jpg", quality=90)
    if n % 120 == 0:
        print(f"frame {n}/{NF}")

print("frames done:", NF)
mp4 = OUT / "enerjy-novin-run.mp4"
subprocess.run(["ffmpeg", "-y", "-r", str(FPS), "-i", str(FRAMES / "%04d.jpg"),
                "-c:v", "libx264", "-crf", "20", "-pix_fmt", "yuv420p",
                "-movflags", "+faststart", str(mp4)], check=True,
               stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)
print("mp4:", mp4, mp4.stat().st_size, "bytes")

# سه فریم پیش‌نمایش برای مالک
for sec, name in [(1.0, "preview-1-khorshid-turbin"), (6.4, "preview-2-javaneh-moj"), (11.2, "preview-3-abr-onvan")]:
    idx = int(sec * FPS)
    Image.open(FRAMES / f"{idx:04d}.jpg").convert("RGB").save(OUT / f"{name}.jpg", quality=88)
print("previews saved")
