#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""رندر درس‌های Rust صف — برای بازبینی بصری قبل از انتشار"""
import sys, json
from pathlib import Path

ROOT = Path("/home/z/my-project")
REPO = ROOT / "anjoman-code-bot"
sys.path.insert(0, str(REPO / "scripts"))
from poster_lib import render, spec_text, set_channel

cfg = json.loads((REPO / "channels/coding/channel.json").read_text(encoding="utf-8"))
logo = REPO / "channels/coding/assets/logo.png"
cfg["_logo_path"] = str(logo) if logo.exists() else ""
set_channel(cfg)

queue = REPO / "channels/coding/content/queue/b1-amoozesh"
for folder in sorted(p for p in queue.iterdir() if p.is_dir()):
    if not (folder / "spec.py").exists():
        continue
    import importlib.util
    spec = importlib.util.spec_from_file_location(folder.name.replace("-", "_"), folder / "spec.py")
    mod = importlib.util.module_from_spec(spec)
    spec.loader.exec_module(mod)
    png = folder / "post.png"
    render(mod.SPEC, png, brand=True)
    render(mod.SPEC, folder / "post-clean.png", brand=False)
    (folder / "text.md").write_text(spec_text(mod.SPEC), encoding="utf-8")
    print(f"{folder.name}: {png.stat().st_size//1024}KB ✓")
