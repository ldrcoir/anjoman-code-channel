#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""رندر یک پوشهٔ پست مشخص (برند + clean + text.md)
مصرف: python3 render_one.py <مسیر پوشهٔ پست>
برای پیش‌نمایش قبل از انتشار — خروجی‌ها داخل خود پوشه ذخیره می‌شوند."""
import sys
import importlib.util
from pathlib import Path

sys.path.insert(0, "/home/z/my-project/scripts")
sys.path.insert(0, "/home/z/my-project/anjoman-code-bot/scripts")
from poster_lib import render, spec_text

folder = Path(sys.argv[1]).resolve()
sf = folder / "spec.py"
name = folder.name.replace("-", "_")
mod_spec = importlib.util.spec_from_file_location(name, sf)
mod = importlib.util.module_from_spec(mod_spec)
mod_spec.loader.exec_module(mod)
SPEC = mod.SPEC

render(SPEC, folder / "post.png", brand=True)
render(SPEC, folder / "post-clean.png", brand=False)
(folder / "text.md").write_text(spec_text(SPEC), encoding="utf-8")
print("rendered:", folder.name,
      "| post.png:", (folder / "post.png").stat().st_size // 1024, "KB",
      "| post-clean.png:", (folder / "post-clean.png").stat().st_size // 1024, "KB")
