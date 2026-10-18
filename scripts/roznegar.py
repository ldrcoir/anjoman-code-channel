#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
روزنگار — سامانهٔ انتشار روزانهٔ انجمن کدنویسی
================================================
هر بار اجرا، یک پست از صف (content/queue/lessons یا content/queue/variety)
را به کانال بله منتشر می‌کند:

۱) انتخاب قدیمی‌ترین پست unpublished از صف انتخابی
۲) رندر دو نسخهٔ عکس: برند (با نشان انجمن) + clean (بدون نام/آدرس انجمن)
۳) بایگانی متن کامل فارسی عکس در text.md
۴) بررسی سادهٔ کیفیت (اندازه/وجود فایل)
۵) ارسال نسخهٔ برند به کانال + ثبت شناسنامه در meta.json
۶) انتقال به posts/ با کد سه‌رقمی جدید + به‌روزرسانی آمار + commit و push

مصرف: python3 roznegar.py --kind lesson  [--force] [--dry]
state روزانه هر نوع پست جدا نگه داشته می‌شود (.publish_state.json)
"""
import os
import sys
import json
import fcntl
import shutil
import subprocess
import importlib.util
from datetime import datetime
from zoneinfo import ZoneInfo
from pathlib import Path

import requests

sys.path.insert(0, "/home/z/my-project/scripts")
sys.path.insert(0, "/home/z/my-project/anjoman-code-bot/scripts")
from poster_lib import render, spec_text

TEHRAN = ZoneInfo("Asia/Tehran")
ROOT = Path("/home/z/my-project")
REPO = ROOT / "anjoman-code-bot"
QUEUE = REPO / "content" / "queue"
POSTS = REPO / "posts"
LOCK = ROOT / ".roznegar.lock"
STATE = ROOT / ".publish_state.json"
LOG = ROOT / "logs" / "roznegar.log"
GH_REPO_URL = "https://github.com/ldrcoir/anjoman-code-channel.git"
CHAT_ID = "5100057643"

FORCE = "--force" in sys.argv
DRY = "--dry" in sys.argv
KIND = "variety" if "--kind" in sys.argv and "variety" in sys.argv else "lesson"


def log(msg):
    line = f"[{datetime.now(TEHRAN).isoformat(timespec='seconds')}] {msg}"
    print(line, flush=True)
    LOG.parent.mkdir(exist_ok=True)
    with open(LOG, "a", encoding="utf-8") as f:
        f.write(line + "\n")


def load_env():
    env = {}
    f = ROOT / ".env"
    if f.exists():
        for line in f.read_text().splitlines():
            if "=" in line and not line.startswith("#"):
                k, v = line.split("=", 1)
                env[k.strip()] = v.strip()
    return env


def already_today(kind):
    if STATE.exists() and not FORCE:
        s = json.loads(STATE.read_text())
        return s.get(kind) == datetime.now(TEHRAN).date().isoformat()
    return False


def mark_today(kind, folder_name):
    s = json.loads(STATE.read_text()) if STATE.exists() else {}
    s[kind] = datetime.now(TEHRAN).date().isoformat()
    s[f"{kind}_last"] = folder_name
    STATE.write_text(json.dumps(s, ensure_ascii=False))


def next_queue_item(kind):
    base = QUEUE / ("lessons" if kind == "lesson" else "variety")
    dirs = sorted([d for d in base.iterdir() if d.is_dir()]) if base.exists() else []
    return dirs[0] if dirs else None


def next_code():
    nums = []
    for d in POSTS.iterdir():
        if d.is_dir() and d.name[:3].isdigit():
            nums.append(int(d.name[:3]))
    return max(nums) + 1 if nums else 1


def load_spec_module(folder):
    sf = folder / "spec.py"
    name = folder.name.replace("-", "_")
    spec = importlib.util.spec_from_file_location(name, sf)
    mod = importlib.util.module_from_spec(spec)
    spec.loader.exec_module(mod)
    return mod


def git(*args):
    return subprocess.run(["git", "-C", str(REPO), *args],
                          capture_output=True, text=True, timeout=120)


def send_photo(token, png, caption):
    with open(png, "rb") as f:
        r = requests.post(
            f"https://tapi.bale.ai/bot{token}/sendPhoto",
            data={"chat_id": CHAT_ID, "caption": caption},
            files={"photo": (Path(png).name, f, "image/png")}, timeout=120)
    resp = r.json()
    if not resp.get("ok"):
        raise RuntimeError(f"Bale API: {resp}")
    return resp["result"]["message_id"]


def publish(kind):
    env = load_env()
    token = env.get("BALE_TOKEN")
    if not token:
        log("ERROR: BALE_TOKEN not found")
        return False

    folder = next_queue_item(kind)
    if not folder:
        log(f"صف {kind} خالی است — پست جدید اضافه شود")
        return False
    log(f"[{kind}] انتخاب پست: {folder.name}")

    # ۱) رندر
    try:
        mod = load_spec_module(folder)
        SPEC = mod.SPEC
        png = folder / "post.png"
        png_clean = folder / "post-clean.png"
        render(SPEC, png, brand=True)
        render(SPEC, png_clean, brand=False)
        (folder / "text.md").write_text(spec_text(SPEC), encoding="utf-8")
        for p in (png, png_clean):
            if not p.exists() or p.stat().st_size < 20000:
                raise RuntimeError(f"rendered file missing/too small: {p.name}")
        if len((folder / "text.md").read_text(encoding="utf-8")) < 100:
            raise RuntimeError("text.md too short")
        log(f"رندر انجام شد: post.png ({png.stat().st_size // 1024}KB) + "
            f"post-clean.png ({png_clean.stat().st_size // 1024}KB) + text.md")
    except Exception as e:
        log(f"ERROR render {folder.name}: {e}")
        return False

    if DRY:
        log("حالت dry — ارسال نشد")
        return True

    # ۲) ارسال به کانال
    caption = (folder / "caption.txt").read_text(encoding="utf-8").strip()
    try:
        message_id = send_photo(token, png, caption)
        log(f"منتشر شد در کانال — message_id={message_id}")
    except Exception as e:
        log(f"ERROR send {folder.name}: {e}")
        return False

    # ۳) کد سه‌رقمی و انتقال به آرشیو
    code = next_code()
    slug = folder.name
    for pre in ("0", "1", "2", "3", "4", "5", "6", "7", "8", "9"):
        if len(slug) > 4 and slug[0] == pre and slug[1] in "0123456789":
            slug = slug.split("-", 1)[1] if "-" in slug else slug
    dest_name = f"{code:03d}-{slug}"
    meta = {
        "number": code,
        "code": f"{code:03d}",
        "category": SPEC.get("category", "?"),
        "category_fa": SPEC.get("category_fa", ""),
        "series": SPEC.get("series_label", ""),
        "lesson": SPEC.get("lesson_no"),
        "title": SPEC.get("title", folder.name),
        "date": datetime.now(TEHRAN).date().isoformat(),
        "message_id": message_id,
        "image": "post.png",
        "clean_image": "post-clean.png",
    }
    dest = POSTS / dest_name
    if dest.exists():
        shutil.rmtree(dest)
    shutil.copytree(folder, dest)
    (dest / "meta.json").write_text(
        json.dumps(meta, ensure_ascii=False, indent=2), encoding="utf-8")
    shutil.rmtree(folder)
    log(f"آرشیو شد: posts/{dest_name}")

    # ۴) آمار + push
    subprocess.run([sys.executable, str(REPO / "scripts" / "update_stats.py")],
                   check=False)
    git("add", "-A")
    git("commit", "-m",
        f"پست {code:03d} منتشر شد: {meta['title']} (دسته {meta['category']})")
    gh = env.get("GITHUB_TOKEN")
    if gh:
        p = git("push", f"https://ldrcoir:{gh}@github.com/ldrcoir/anjoman-code-channel.git", "main")
        git("remote", "set-url", "origin", GH_REPO_URL)
        log("push: OK" if p.returncode == 0 else f"push FAILED: {p.stderr[:200]}")
    else:
        log("push skipped: GITHUB_TOKEN not found")

    mark_today(kind, dest_name)
    return True


def main():
    LOCK.parent.mkdir(exist_ok=True)
    with open(LOCK, "w") as lf:
        try:
            fcntl.flock(lf, fcntl.LOCK_EX | fcntl.LOCK_NB)
        except BlockingIOError:
            log("اجرای دیگری در جریان است — خروج")
            return
        if already_today(KIND):
            log(f"[{KIND}] امروز قبلاً منتشر شده — خروج")
            return
        ok = publish(KIND)
        log("پایان موفق" if ok else "پایان بدون انتشار")


if __name__ == "__main__":
    main()
