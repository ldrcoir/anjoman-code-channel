#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""اسکرین‌شات پستِ بازی از خود صفحهٔ بازی — دو نسخهٔ برند و clean
مصرف: python3 game_shot.py <پوشهٔ بازی> <فایل html>
خروجی: post.png و post-clean.png در همان پوشه (۱۲۸۰×۱۳۵۰ نیست — دقیقاً ۱۰۸۰×۱۳۵۰)"""
import sys
from pathlib import Path
from playwright.sync_api import sync_playwright

folder = Path(sys.argv[1]).resolve()
html = Path(sys.argv[2]).resolve()

with sync_playwright() as p:
    b = p.chromium.launch(args=["--force-color-profile=srgb"])
    pg = b.new_page(viewport={"width": 1080, "height": 1350}, device_scale_factor=1)
    errs = []
    pg.on("pageerror", lambda e: errs.append(str(e)))
    pg.on("console", lambda m: errs.append(m.text) if m.type == "error" else None)

    # نسخهٔ برند — صفحه با demo خودش بازی می‌کند تا کادر پر باشد
    pg.goto(f"file://{html}?demo=1")
    pg.wait_for_timeout(2600)
    pg.screenshot(path=str(folder / "post.png"))
    emtiaz = pg.text_content("#nemayesh-emtiaz")
    h = pg.evaluate("document.body.scrollHeight")

    # نسخهٔ clean — بدون نام انجمن/مدرسه
    pg.goto(f"file://{html}?demo=1&clean=1")
    pg.wait_for_timeout(1800)
    pg.screenshot(path=str(folder / "post-clean.png"))

    # تست تعامل: شروع + کلیدها بدون خطا
    pg.goto(f"file://{html}")
    pg.wait_for_timeout(400)
    pg.click("#dokme-shroo")
    pg.wait_for_timeout(600)
    pg.keyboard.press("ArrowUp")
    pg.wait_for_timeout(600)
    pg.click("#dokme-mekes")
    pg.wait_for_timeout(300)
    vaziat = pg.text_content("#nemayesh-vaziat")
    b.close()

print("post.png + post-clean.png ساخته شد | امتیاز دمو:", emtiaz,
      "| ارتفاع بدنه:", h, "| وضعیت تست:", vaziat)
if errs:
    print("⚠️ خطاهای کنسول:")
    for e in errs[:8]:
        print("  -", e)
    sys.exit(1)
print("بدون خطای کنسول — سالم")
