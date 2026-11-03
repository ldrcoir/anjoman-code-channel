#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""گردش ۵۹ — بازرندر درس ۱۱ (اصلاحِ return) و درس ۱۲ در صف b1-amoozesh"""
import sys
import importlib.util
from pathlib import Path

REPO = Path("/home/z/my-project/anjoman-code-bot")
sys.path.insert(0, str(REPO / "scripts"))
from poster_lib import render  # noqa: E402

TARGETS = [
    "037-rust-dars-11-tavabe",
    "038-rust-dars-12-sazeha",
]

for name in TARGETS:
    folder = REPO / "channels" / "coding" / "content" / "queue" / "b1-amoozesh" / name
    sf = folder / "spec.py"
    spec = importlib.util.spec_from_file_location(name.replace("-", "_"), sf)
    mod = importlib.util.module_from_spec(spec)
    spec.loader.exec_module(mod)
    render(mod.SPEC, folder / "post.png", brand=True)
    render(mod.SPEC, folder / "post-clean.png", brand=False)
    print(f"{name}: post.png={(folder / 'post.png').stat().st_size // 1024}KB "
          f"clean={(folder / 'post-clean.png').stat().st_size // 1024}KB")
