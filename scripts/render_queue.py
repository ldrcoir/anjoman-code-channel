#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""رندر همه پست‌های صف (lessons + variety) برای بازبینی پیش از انتشار
خروجی: پوشه‌های پست (post.png + post-clean.png + text.md) و پیش‌نمایش در download/queue_preview
"""
import sys
import importlib.util
from pathlib import Path

sys.path.insert(0, "/home/z/my-project/scripts")
sys.path.insert(0, "/home/z/my-project/anjoman-code-bot/scripts")
from poster_lib import render, spec_text

QUEUE = Path("/home/z/my-project/anjoman-code-bot/channels/coding/content/queue")
PREVIEW = Path("/home/z/my-project/download/queue_preview")
PREVIEW.mkdir(parents=True, exist_ok=True)

count = 0
for spec_file in sorted(QUEUE.rglob("spec.py")):
    folder = spec_file.parent
    name = folder.name.replace("-", "_")
    mod_spec = importlib.util.spec_from_file_location(name, spec_file)
    mod = importlib.util.module_from_spec(mod_spec)
    mod_spec.loader.exec_module(mod)
    SPEC = mod.SPEC
    # نسخهٔ برند و نسخهٔ clean در خود پوشهٔ پست
    render(SPEC, folder / "post.png", brand=True)
    render(SPEC, folder / "post-clean.png", brand=False)
    (folder / "text.md").write_text(spec_text(SPEC), encoding="utf-8")
    # پیش‌نمایش برای بازبینی
    render(SPEC, PREVIEW / f"{folder.name}.png", brand=True)
    count += 1
    print("rendered:", folder.name)
print(f"total: {count}")
