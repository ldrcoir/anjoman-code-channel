#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
kootah.py — کوتاه‌کنندهٔ لینک برای همهٔ پست‌های انجمن
استفاده:  python3 kootah.py <url> [نام-کلید]
خروجی:   لینک کوتاه (چاپ) — نگاشت در shortlinks.json ذخیره می‌شود تا هر URL فقط یک‌بار ساخته شود
سرویس:   TinyURL API (رایگان، بدون ثبت‌نام) — is.gd پشتیبان
"""
import json, sys, urllib.parse, urllib.request
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
CACHE = ROOT / "shortlinks.json"

def make_short(url: str) -> str:
    enc = urllib.parse.quote(url, safe="")
    for api in (f"https://tinyurl.com/api-create.php?url={enc}",
                f"https://is.gd/create.php?format=simple&url={enc}"):
        try:
            with urllib.request.urlopen(api, timeout=20) as r:
                out = r.read().decode("utf-8", "replace").strip()
            if out.startswith("http") and " " not in out and "Error" not in out:
                return out
        except Exception:
            continue
    raise RuntimeError("هیچ سرویس کوتاه‌کننده‌ای جواب نداد")

def shorten_links_in(text: str, min_len: int = 40) -> str:
    """همهٔ لینک‌های بلندِ متن کوتاه می‌شوند (سیاست مالک) — اگر سرویس در دسترس
    نباشد، همان لینک اصلی می‌ماند تا انتشار به‌هم نخورد."""
    import re
    def repl(m):
        url = m.group(0)
        if len(url) < min_len or "tinyurl.com/" in url:
            return url
        try:
            cache = {}
            if CACHE.exists():
                try: cache = json.loads(CACHE.read_text(encoding="utf-8"))
                except Exception: cache = {}
            key = "auto:" + url
            if key in cache:
                return cache[key]["short"]
            short = make_short(url)
            cache[key] = {"url": url, "short": short}
            CACHE.write_text(json.dumps(cache, ensure_ascii=False, indent=2) + "\n",
                             encoding="utf-8")
            return short
        except Exception:
            return url
    return re.sub(r"https?://[^\s\)\]>]+", repl, text)

def main():
    if len(sys.argv) < 2:
        print("استفاده: kootah.py <url> [کلید]"); sys.exit(1)
    url = sys.argv[1].strip()
    key = (sys.argv[2] if len(sys.argv) > 2 else url).strip()
    cache = {}
    if CACHE.exists():
        try: cache = json.loads(CACHE.read_text(encoding="utf-8"))
        except Exception: cache = {}
    if key in cache and cache[key].get("url") == url:
        print(cache[key]["short"]); return
    short = make_short(url)
    cache[key] = {"url": url, "short": short}
    CACHE.write_text(json.dumps(cache, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    print(short)

if __name__ == "__main__":
    main()
