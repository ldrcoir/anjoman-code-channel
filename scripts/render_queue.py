#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""رندر همه پست‌های صف برای بازبینی پیش از انتشار"""
import sys
import importlib.util
from pathlib import Path

QUEUE = Path("/home/z/my-project/anjoman-code-bot/content/queue")
OUT = Path("/home/z/my-project/download/queue_preview")
OUT.mkdir(exist_ok=True)

for folder in sorted(QUEUE.iterdir()):
    if not folder.is_dir():
        continue
    spec_file = folder / "spec.py"
    if not spec_file.exists():
        print("skip (no spec):", folder.name)
        continue
    mod_spec = importlib.util.spec_from_file_location(folder.name.replace("-", "_"), spec_file)
    mod = importlib.util.module_from_spec(mod_spec)
    mod_spec.loader.exec_module(mod)
    out_png = OUT / f"{folder.name}.png"
    mod.build(str(out_png))
    print("rendered:", out_png)
