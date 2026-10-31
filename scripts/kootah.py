#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
kootah.py — کوتاه‌کنندهٔ لینک برای همهٔ پست‌های انجمن
استفاده:  python3 kootah.py <url> [نام-کلید]
خروجی:   لینک کوتاه (چاپ) — نگاشت در shortlinks.json ذخیره می‌شود تا هر URL فقط یک‌بار ساخته شود

سرویس:   clikr.ir — کوتاه‌کنندهٔ ایرانی با دامنهٔ .ir (بدون ثبت‌نام و بدون کلید)
         چون TinyURL و مشابه خارجی برای مخاطب داخل ایران فیلتر است، فقط سرویس ایرانی
         به کار می‌رود؛ اگر سرویس پاسخ نداد، لینک کامل می‌ماند (انتشار به‌هم نمی‌خورد).
نکته:    لینک‌های clikr.ir تاریخ انقضا (حدود ۳ ماه) دارند — پیش از هر دوره قهرمانی دوباره بساز.
"""
import json, re, sys, urllib.parse, urllib.request
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
CACHE = ROOT / "shortlinks.json"

# دامنه‌هایی که خودشان کوتاه‌اند و نباید دوباره کوتاه شوند
ALREADY_SHORT = ("clikr.ir/", "ble.ir/", "tiny.cc/")

def _clikr(url: str) -> str:
    """clikr.ir — POST JSON؛ خروجی: short_code → https://clikr.ir/<code>"""
    req = urllib.request.Request(
        "https://clikr.ir/api/shorten",
        data=json.dumps({"url": url}).encode("utf-8"),
        headers={"Content-Type": "application/json",
                 "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64)"},
        method="POST")
    with urllib.request.urlopen(req, timeout=25) as r:
        data = json.loads(r.read().decode("utf-8", "replace"))
    code = str(data.get("short_code") or "").strip()
    if not code or " " in code:
        raise RuntimeError(f"پاسخ غیرمنتظرهٔ clikr.ir: {data}")
    return f"https://clikr.ir/{code}"

def make_short(url: str) -> str:
    return _clikr(url)

def _cache_load() -> dict:
    if CACHE.exists():
        try:
            return json.loads(CACHE.read_text(encoding="utf-8"))
        except Exception:
            return {}
    return {}

def _cache_save(cache: dict) -> None:
    CACHE.write_text(json.dumps(cache, ensure_ascii=False, indent=2) + "\n",
                     encoding="utf-8")

def shorten_links_in(text: str, min_len: int = 40) -> str:
    """همهٔ لینک‌های بلندِ متن کوتاه می‌شوند (سیاست مالک) — اگر سرویس در دسترس
    نباشد، همان لینک اصلی می‌ماند تا انتشار به‌هم نخورد."""
    def repl(m):
        url = m.group(0)
        if len(url) < min_len or any(d in url for d in ALREADY_SHORT):
            return url
        try:
            cache = _cache_load()
            key = "auto:" + url
            if key in cache:
                return cache[key]["short"]
            short = make_short(url)
            cache[key] = {"url": url, "short": short}
            _cache_save(cache)
            return short
        except Exception:
            return url
    return re.sub(r"https?://[^\s\)\]>]+", repl, text)

def main():
    if len(sys.argv) < 2:
        print("استفاده: kootah.py <url> [کلید]"); sys.exit(1)
    url = sys.argv[1].strip()
    key = (sys.argv[2] if len(sys.argv) > 2 else url).strip()
    cache = _cache_load()
    if key in cache and cache[key].get("url") == url:
        print(cache[key]["short"]); return
    short = make_short(url)
    cache[key] = {"url": url, "short": short}
    _cache_save(cache)
    print(short)

if __name__ == "__main__":
    main()
