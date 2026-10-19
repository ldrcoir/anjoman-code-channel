#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
روزنگار — سامانهٔ انتشار روزانهٔ انجمن کدنویسی (نسخه ۳)
========================================================
مقاوم در برابر ریست سرور:
- وضعیت انتشار (state.json) داخل مخزن گیت است؛ منبع حقیقتِ واحد گیت‌هاب است
- در شروع اجرا state از گیت‌هاب pull می‌شود تا هیچ‌وقت گم یا تکراری نشود
- بعد از هر انتشار: آمار + commit + push

هر اجرا یک پست از صف (content/queue/lessons یا variety) منتشر می‌کند:
۱) انتخاب قدیمی‌ترین پست منتشرنشدهٔ صف
۲) رندر دو نسخهٔ عکس: برند + clean (بدون نام/آدرس انجمن)
۳) بایگانی متن کامل فارسی عکس در text.md
۴) بررسی کیفیت (اندازهٔ فایل)
۵) ارسال به کانال + شناسنامهٔ meta.json با دستهٔ فارسی
۶) انتقال به posts/NNN-... + آمار + commit و push

مصرف: python3 roznegar.py --kind lesson|variety [--force] [--dry]
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
STATE = REPO / "content" / "state.json"          # داخل مخزن — نسخه‌پشتیبان گیت‌هاب
LOG = ROOT / "logs" / "roznegar.log"
GH_REPO_URL = "https://github.com/ldrcoir/anjoman-code-channel.git"
GH_PUSH_URL_TPL = "https://ldrcoir:{tok}@github.com/ldrcoir/anjoman-code-channel.git"
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


def git(*args):
    return subprocess.run(["git", "-C", str(REPO), *args],
                          capture_output=True, text=True, timeout=180)


def git_sync_with_remote(env):
    """state را از گیت‌هاب تازه می‌کند؛ اگر محلی جلوتر باشد چیزی از دست نمی‌رود"""
    tok = env.get("GITHUB_TOKEN")
    if not tok:
        log("pull: GITHUB_TOKEN نیست — رد شد")
        return
    push_url = GH_PUSH_URL_TPL.format(tok=tok)
    f = git("fetch", push_url, "main")
    if f.returncode != 0:
        log(f"pull: fetch ناموفق — ادامه با نسخهٔ محلی ({f.stderr[:150]})")
        return
    m = git("merge", "--ff-only", "FETCH_HEAD")
    if m.returncode == 0:
        git("remote", "set-url", "origin", GH_REPO_URL)
        if "Already up to date" not in (m.stdout or ""):
            log("pull: از گیت‌هاب همگام شد")
    else:
        # واگرایی نادر — بازگشت به ریموت (فقط وقتی محلی قابل اعتماد نیست)
        log(f"pull: ff-merge نشد ({(m.stdout or m.stderr)[:120]}) — reset به ریموت")
        git("reset", "--hard", "FETCH_HEAD")
        git("remote", "set-url", "origin", GH_REPO_URL)


def git_push(env):
    tok = env.get("GITHUB_TOKEN")
    if not tok:
        log("push: GITHUB_TOKEN نیست — رد شد")
        return
    p = git("push", GH_PUSH_URL_TPL.format(tok=tok), "main")
    git("remote", "set-url", "origin", GH_REPO_URL)
    log("push: OK" if p.returncode == 0 else f"push FAILED: {p.stderr[:300]}")


def read_state():
    if STATE.exists():
        try:
            return json.loads(STATE.read_text(encoding="utf-8"))
        except Exception:
            pass
    return {}


def write_state(s):
    STATE.write_text(json.dumps(s, ensure_ascii=False, indent=2), encoding="utf-8")


def already_today(kind):
    if STATE.exists() and not FORCE:
        return read_state().get(kind) == datetime.now(TEHRAN).date().isoformat()
    return False


def mark_today(kind, folder_name):
    s = read_state()
    s[kind] = datetime.now(TEHRAN).date().isoformat()
    s[f"{kind}_last"] = folder_name
    write_state(s)


def next_queue_item(kind):
    base = QUEUE / ("lessons" if kind == "lesson" else "variety")
    dirs = sorted([d for d in base.iterdir() if d.is_dir()]) if base.exists() else []
    return dirs[0] if dirs else None


def next_code():
    nums = []
    if POSTS.exists():
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


def send_photo(token, png, caption):
    with open(png, "rb") as f:
        r = requests.post(
            f"https://tapi.bale.ai/bot{token}/sendPhoto",
            data={"chat_id": CHAT_ID, "caption": caption},
            files={"photo": (Path(png).name, f, "image/png")}, timeout=180)
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
    slug = folder.name.split("-", 1)[1] if folder.name[:2].isdigit() and "-" in folder.name else folder.name
    dest_name = f"{code:03d}-{slug}"
    cat_fa = SPEC.get("category_fa", "نامشخص")
    meta = {
        "number": code,
        "code": f"{code:03d}",
        "category_fa": cat_fa,
        "series": SPEC.get("series_label", ""),
        "lesson": SPEC.get("lesson_no"),
        "title": SPEC.get("title", folder.name),
        "date": datetime.now(TEHRAN).date().isoformat(),
        "message_id": message_id,
        "image": "post.png",
        "clean_image": "post-clean.png",
        "statute": "شیوه‌نامه هفتمین دوره مسابقات کدنویسی پژوهش‌سراها ۱۴۰۴-۱۴۰۳",
    }
    dest = POSTS / dest_name
    if dest.exists():
        shutil.rmtree(dest)
    shutil.copytree(folder, dest)
    (dest / "meta.json").write_text(
        json.dumps(meta, ensure_ascii=False, indent=2), encoding="utf-8")
    shutil.rmtree(folder)
    log(f"آرشیو شد: posts/{dest_name}")

    # ۴) ثبت state «قبل» از commit/push تا هیچ‌وقت با reset از دست نرود
    mark_today(kind, dest_name)

    # ۵) آمار + commit + push
    subprocess.run([sys.executable, str(REPO / "scripts" / "update_stats.py")],
                   check=False)
    git("add", "-A")
    git("commit", "-m",
        f"پست {code:03d} منتشر شد: {meta['title']} (دستهٔ {cat_fa})")
    git_push(env)
    return True


def main():
    LOCK.parent.mkdir(exist_ok=True)
    with open(LOCK, "w") as lf:
        try:
            fcntl.flock(lf, fcntl.LOCK_EX | fcntl.LOCK_NB)
        except BlockingIOError:
            log("اجرای دیگری در جریان است — خروج")
            return
        env = load_env()
        git_sync_with_remote(env)
        if already_today(KIND):
            log(f"[{KIND}] امروز قبلاً منتشر شده — خروج")
            return
        ok = publish(KIND)
        log("پایان موفق" if ok else "پایان بدون انتشار")


if __name__ == "__main__":
    main()
