#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""رندر آیتم‌های کانال انرژی‌پژوهان با برند خودش (مشابه کاری که roznegar در انتشار می‌کند)
مصرف: python3 render_enerjy.py   ← همهٔ صف
      python3 render_enerjy.py <مسیر پوشه>  ← یک آیتم"""
import sys, json, importlib.util
from pathlib import Path

ROOT = Path("/home/z/my-project")
REPO = ROOT / "anjoman-code-bot"
sys.path.insert(0, str(REPO / "scripts"))
from poster_lib import render, spec_text, set_channel

cfg = json.loads((REPO / "channels/enerjy-novin/channel.json").read_text(encoding="utf-8"))
logo = REPO / "channels/enerjy-novin/assets/logo.png"
cfg["_logo_path"] = str(logo) if logo.exists() else ""
set_channel(cfg)

QUEUE = REPO / "channels/enerjy-novin/content/queue"
target = Path(sys.argv[1]).resolve() if len(sys.argv) > 1 else None

folders = [target] if target else [f.parent for f in sorted(QUEUE.rglob("spec.py"))]
for folder in folders:
    sf = folder / "spec.py"
    if not sf.exists():
        continue
    name = folder.name.replace("-", "_")
    spec = importlib.util.spec_from_file_location(name, sf)
    mod = importlib.util.module_from_spec(spec)
    spec.loader.exec_module(mod)
    render(mod.SPEC, folder / "post.png", brand=True)
    render(mod.SPEC, folder / "post-clean.png", brand=False)
    (folder / "text.md").write_text(spec_text(mod.SPEC), encoding="utf-8")
    print("rendered:", folder.relative_to(QUEUE),
          "| post.png:", (folder / "post.png").stat().st_size // 1024, "KB",
          "| post-clean.png:", (folder / "post-clean.png").stat().st_size // 1024, "KB")
