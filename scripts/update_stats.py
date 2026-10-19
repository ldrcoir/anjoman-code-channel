#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
آمار انتشار کانال — تولید channels/<slug>/content/stats.md و stats.json
از روی شناسنامهٔ (meta.json) پست‌های آرشیوشدهٔ همان کانال.

مصرف: python3 update_stats.py --channel coding
(بدون پارامتر: آمار همهٔ کانال‌ها ساخته می‌شود)
"""
import json
import sys
from datetime import datetime
from zoneinfo import ZoneInfo
from pathlib import Path

TEHRAN = ZoneInfo("Asia/Tehran")
REPO = Path("/home/z/my-project/anjoman-code-bot")


def build_stats(chdir):
    slug = chdir.name
    posts_dir = chdir / "posts"
    posts = []
    if posts_dir.exists():
        for d in sorted(posts_dir.iterdir()):
            m = d / "meta.json"
            if d.is_dir() and m.exists():
                meta = json.loads(m.read_text(encoding="utf-8"))
                meta["_folder"] = d.name
                posts.append(meta)
    posts.sort(key=lambda x: x.get("number", 0))

    by_cat, by_series = {}, {}
    for p in posts:
        c = p.get("category_fa") or p.get("category") or "؟"   # دسته فقط داخلی
        by_cat[c] = by_cat.get(c, 0) + 1
        s = p.get("series") or "—"
        by_series[s] = by_series.get(s, 0) + 1

    now = datetime.now(TEHRAN)
    stats = {
        "updated": now.isoformat(timespec="seconds"),
        "channel": slug,
        "total": len(posts),
        "by_category": by_cat,
        "by_series": by_series,
        "posts": [
            {k: p.get(k) for k in
             ("code", "title", "category_fa", "series", "lesson", "date", "message_id")}
            for p in posts
        ],
    }
    (chdir / "content" / "stats.json").write_text(
        json.dumps(stats, ensure_ascii=False, indent=2), encoding="utf-8")

    L = []
    L.append(f"# آمار انتشار کانال «{slug}»")
    L.append("")
    L.append(f"آخرین به‌روزرسانی: {now.strftime('%Y-%m-%d %H:%M')} (تهران)")
    L.append("")
    L.append(f"**مجموع پست‌ها: {len(posts)}**")
    L.append("")
    L.append("> دسته‌بندی فقط برای مستندسازی داخلی است و روی عکس/متن کانال نمی‌آید.")
    L.append("")
    L.append("## به تفکیک دستهٔ محتوا (داخلی)")
    L.append("")
    L.append("| دسته | تعداد |")
    L.append("|------|-------|")
    for c, n in sorted(by_cat.items(), key=lambda x: -x[1]):
        L.append(f"| {c} | {n} |")
    L.append("")
    L.append("## به تفکیک سری")
    L.append("")
    L.append("| سری | تعداد |")
    L.append("|-----|-------|")
    for s, n in sorted(by_series.items(), key=lambda x: -x[1]):
        L.append(f"| {s} | {n} |")
    L.append("")
    L.append("## فهرست پست‌ها")
    L.append("")
    L.append("| کد | عنوان | دسته (داخلی) | درس | تاریخ | شناسهٔ پیام |")
    L.append("|----|-------|--------------|-----|-------|-------------|")
    for p in posts:
        lesson = p.get("lesson") or "—"
        L.append(f"| {p.get('code')} | {p.get('title')} | {p.get('category_fa')} | "
                 f"{lesson} | {p.get('date')} | {p.get('message_id')} |")
    L.append("")
    (chdir / "content" / "stats.md").write_text("\n".join(L), encoding="utf-8")
    print(f"stats[{slug}] updated: total={len(posts)}")


def main():
    chdir_arg = None
    if "--channel" in sys.argv:
        i = sys.argv.index("--channel")
        if i + 1 < len(sys.argv):
            chdir_arg = sys.argv[i + 1]
    if chdir_arg:
        build_stats(REPO / "channels" / chdir_arg)
    else:
        base = REPO / "channels"
        if base.exists():
            for d in sorted(base.iterdir()):
                if d.is_dir() and not d.name.startswith("_") and (d / "channel.json").exists():
                    build_stats(d)


if __name__ == "__main__":
    main()
