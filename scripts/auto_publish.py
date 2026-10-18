#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
auto_publish.py — انتشار خودکار روزانه یک پست از صف محتوا
- قدیمی‌ترین پست unpublished را از content/queue برمی‌دارد
- رندر → بررسی ساده کیفیت → ارسال به کانال بله → ثبت meta.json → انتقال به آرشیو → commit/push
- هر روز فقط یک‌بار (به‌جز با پرچم --force)؛ با قفل فایلی برای جلوگیری از اجرای موازی
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

ROOT = Path("/home/z/my-project")
REPO = ROOT / "anjoman-code-bot"
QUEUE = REPO / "content" / "queue"
POSTS = REPO / "posts"
LOCK = ROOT / ".auto_publish.lock"
STATE = ROOT / ".publish_state.json"
LOG = ROOT / "logs" / "auto_publish.log"
TEHRAN = ZoneInfo("Asia/Tehran")

GH_REPO_URL = "https://github.com/ldrcoir/anjoman-code-channel.git"
FORCE = "--force" in sys.argv


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


def already_today():
    if STATE.exists() and not FORCE:
        s = json.loads(STATE.read_text())
        today = datetime.now(TEHRAN).date().isoformat()
        return s.get("last_date") == today
    return False


def next_queue_item():
    dirs = sorted([d for d in QUEUE.iterdir() if d.is_dir()])
    return dirs[0] if dirs else None


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


def publish():
    env = load_env()
    token = env.get("BALE_TOKEN")
    gh_token = env.get("GITHUB_TOKEN")
    if not token:
        log("ERROR: BALE_TOKEN not found")
        return False

    folder = next_queue_item()
    if not folder:
        log("صف خالی است — پست جدیدی به content/queue اضافه شود")
        return False
    log(f"انتخاب پست: {folder.name}")

    # 1) رندر
    try:
        mod = load_spec_module(folder)
        png = folder / "post.png"
        mod.build(str(png))
        if not png.exists() or png.stat().st_size < 20000:
            raise RuntimeError("rendered file missing/too small")
        log(f"رندر انجام شد: {png.name} ({png.stat().st_size // 1024}KB)")
    except Exception as e:
        log(f"ERROR render {folder.name}: {e}")
        return False

    # 2) ارسال به کانال
    caption_file = folder / "caption.txt"
    caption = caption_file.read_text(encoding="utf-8").strip()
    chat_id = "5100057643"
    try:
        with open(png, "rb") as f:
            r = requests.post(
                f"https://tapi.bale.ai/bot{token}/sendPhoto",
                data={"chat_id": chat_id, "caption": caption},
                files={"photo": (png.name, f, "image/png")},
                timeout=120)
        resp = r.json()
        if not resp.get("ok"):
            raise RuntimeError(f"Bale API: {resp}")
        message_id = resp["result"]["message_id"]
        log(f"منتشر شد در کانال — message_id={message_id}")
    except Exception as e:
        log(f"ERROR send {folder.name}: {e}")
        return False

    # 3) شناسنامه پست
    code_num = folder.name.split("-")[0]
    meta = {
        "number": int(code_num),
        "code": code_num,
        "category": getattr(mod, "SPEC", {}).get("category", "?"),
        "title": getattr(mod, "SPEC", {}).get("title", folder.name),
        "date": datetime.now(TEHRAN).date().isoformat(),
        "message_id": message_id,
        "image": "post.png",
    }
    (folder / "meta.json").write_text(
        json.dumps(meta, ensure_ascii=False, indent=2), encoding="utf-8")

    # 4) انتقال به آرشیو
    dest = POSTS / folder.name
    if dest.exists():
        shutil.rmtree(dest)
    shutil.move(str(folder), str(dest))
    log(f"آرشیو شد: posts/{folder.name}")

    # 5) commit و push
    git("add", "-A")
    c = git("commit", "-m",
            f"پست {code_num} منتشر شد: {meta['title']} (دسته {meta['category']})")
    if gh_token:
        push_url = f"https://ldrcoir:{gh_token}@github.com/ldrcoir/anjoman-code-channel.git"
        p = git("push", push_url, "main")
        git("remote", "set-url", "origin", GH_REPO_URL)  # پاک‌سازی توکن از config
        log("push: OK" if p.returncode == 0 else f"push FAILED: {p.stderr[:200]}")
    else:
        log("push skipped: GITHUB_TOKEN not found")

    # 6) وضعیت روز
    STATE.write_text(json.dumps(
        {"last_date": meta["date"], "last_post": folder.name},
        ensure_ascii=False))
    return True


def main():
    LOCK.parent.mkdir(exist_ok=True)
    with open(LOCK, "w") as lf:
        try:
            fcntl.flock(lf, fcntl.LOCK_EX | fcntl.LOCK_NB)
        except BlockingIOError:
            log("اجرای دیگری در جریان است — خروج")
            return
        if already_today():
            log("امروز قبلاً منتشر شده — خروج")
            return
        ok = publish()
        log("پایان موفق" if ok else "پایان بدون انتشار")


if __name__ == "__main__":
    main()
