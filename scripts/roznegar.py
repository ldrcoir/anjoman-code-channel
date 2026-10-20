#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
روزنگار — سامانهٔ انتشار روزانهٔ کانال‌ها (نسخه ۴ — چندانجمنی)
================================================================
هر کانال/انجمن یک پوشهٔ مستقل دارد: channels/<slug>/ با شناسنامهٔ
channel.json، صف، آرشیو، state و آمار خودش — هیچ قاطی‌شدنی در کار نیست،
حتی اگر دو کانال به یک انجمن تعلق داشته باشند.

مقاوم در برابر ریست سرور:
- وضعیت انتشار (state.json) داخل مخزن گیت است؛ منبع حقیقتِ واحد گیت‌هاب است
- در شروع اجرا state از گیت‌هاب pull می‌شود تا هیچ‌وقت گم یا تکراری نشود
- بعد از هر انتشار: آمار + commit + push

هر اجرا یک پست از صف همان کانال منتشر می‌کند:
۱) انتخاب قدیمی‌ترین پست منتشرنشدهٔ صف (lessons یا variety)
۲) رندر دو نسخهٔ عکس: برند + clean (بدون نام/آدرس انجمن) — بدون هیچ دسته‌بندی
۳) بایگانی متن کامل فارسی عکس در text.md
۴) بررسی کیفیت (اندازهٔ فایل)
۵) ارسال به کانال + شناسنامهٔ meta.json (دسته فقط داخلی — طبق شیوه‌نامه)
۶) انتقال به posts/NNN-... + آمار + commit و push

مصرف: python3 roznegar.py --channel <slug> --kind lesson|variety [--force] [--dry]
"""
import os
import re
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
sys.path.insert(0, str(REPO / "scripts"))
from poster_lib import render, spec_text, set_channel  # noqa: E402

TEHRAN = ZoneInfo("Asia/Tehran")
LOCK = ROOT / ".roznegar.lock"
LOG = ROOT / "logs" / "roznegar.log"
GH_REPO_URL = "https://github.com/ldrcoir/anjoman-code-channel.git"
GH_PUSH_URL_TPL = "https://ldrcoir:{tok}@github.com/ldrcoir/anjoman-code-channel.git"

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


def load_channel(slug):
    """شناسنامهٔ کانال را می‌خواند و مسیرها را برمی‌گرداند"""
    chdir = REPO / "channels" / slug
    cj = chdir / "channel.json"
    if not cj.exists():
        raise FileNotFoundError(f"کانال «{slug}» وجود ندارد: {cj}")
    cfg = json.loads(cj.read_text(encoding="utf-8"))
    cfg["_dir"] = str(chdir)
    logo = chdir / "assets" / "logo.png"
    cfg["_logo_path"] = str(logo) if logo.exists() else ""
    return cfg


def token_for(env, cfg):
    tok = env.get(cfg.get("token_key") or "")
    return tok or env.get("BALE_TOKEN")


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
    # هیچ‌وقت کامیت‌های محلی را با reset نابود نکن:
    # اگر محلی جلوتر یا واگرده بود، نسخهٔ محلی مرجع است (فقط push می‌شود)
    ahead = (git("rev-list", "--count", "FETCH_HEAD..HEAD").stdout or "0").strip()
    if ahead and ahead != "0":
        m = git("merge", "--ff-only", "FETCH_HEAD")
        if m.returncode == 0:
            log("pull: از گیت‌هاب همگام شد")
        else:
            log(f"pull: محلی {ahead} کامیت جلوتر/واگرده — نسخهٔ محلی حفظ شد")
    else:
        git("reset", "--hard", "FETCH_HEAD")
        log("pull: به ریموت تراز شد")
    git("remote", "set-url", "origin", GH_REPO_URL)


def git_push(env):
    tok = env.get("GITHUB_TOKEN")
    if not tok:
        log("push: GITHUB_TOKEN نیست — رد شد")
        return
    p = git("push", GH_PUSH_URL_TPL.format(tok=tok), "main")
    git("remote", "set-url", "origin", GH_REPO_URL)
    log("push: OK" if p.returncode == 0 else f"push FAILED: {p.stderr[:300]}")


def already_today(state_path, kind):
    if state_path.exists() and not FORCE:
        s = json.loads(state_path.read_text(encoding="utf-8"))
        return s.get(kind) == datetime.now(TEHRAN).date().isoformat()
    return False


def mark_today(state_path, kind, folder_name):
    s = {}
    if state_path.exists():
        try:
            s = json.loads(state_path.read_text(encoding="utf-8"))
        except Exception:
            s = {}
    s[kind] = datetime.now(TEHRAN).date().isoformat()
    s[f"{kind}_last"] = folder_name
    state_path.write_text(json.dumps(s, ensure_ascii=False, indent=2),
                          encoding="utf-8")


KIND_DIRS = {"lesson": "lessons", "variety": "variety", "magazine": "magazine"}


def next_queue_item(queue_dir, kind):
    base = queue_dir / KIND_DIRS.get(kind, kind)
    dirs = sorted([d for d in base.iterdir() if d.is_dir()]) if base.exists() else []
    return dirs[0] if dirs else None


def next_code(posts_dir):
    nums = []
    if posts_dir.exists():
        for d in posts_dir.iterdir():
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


def send_photo(token, chat_id, png, caption):
    with open(png, "rb") as f:
        r = requests.post(
            f"https://tapi.bale.ai/bot{token}/sendPhoto",
            data={"chat_id": chat_id, "caption": caption},
            files={"photo": (Path(png).name, f, "image/png")}, timeout=180)
    resp = r.json()
    if not resp.get("ok"):
        raise RuntimeError(f"Bale API: {resp}")
    return resp["result"]["message_id"]


def send_document(token, chat_id, doc, caption):
    with open(doc, "rb") as f:
        r = requests.post(
            f"https://tapi.bale.ai/bot{token}/sendDocument",
            data={"chat_id": chat_id, "caption": caption},
            files={"document": (Path(doc).name, f, "application/pdf")}, timeout=300)
    resp = r.json()
    if not resp.get("ok"):
        raise RuntimeError(f"Bale API: {resp}")
    return resp["result"]["message_id"]


def fresh_handle(token, chat_id):
    """نام‌کاربری زندهٔ کانال را از API می‌گیرد — ادمین‌ها گاهی آدرس را عوض می‌کنند"""
    try:
        r = requests.get(f"https://tapi.bale.ai/bot{token}/getChat",
                         json={"chat_id": chat_id}, timeout=30)
        res = r.json()
        if res.get("ok"):
            return (res.get("result") or {}).get("username")
    except Exception as e:
        log(f"getChat ناموفق: {e}")
    return None


def publish(cfg, kind, env):
    slug = cfg["slug"]
    chdir = Path(cfg["_dir"])
    queue = chdir / "content" / "queue"
    posts = chdir / "posts"
    state_path = chdir / "content" / "state.json"
    chat_id = cfg["chat_id"]
    token = token_for(env, cfg)
    if not token:
        log(f"ERROR: توکن کانال {slug} یافت نشد ({cfg.get('token_key')})")
        return False

    # ۰) گیت ماهانهٔ مجله — هر ۲۵ روز حداکثر یک شماره (شیوه‌نامه تناوب نگفته؛ ماهانه)
    if kind == "magazine":
        try:
            st = json.loads(state_path.read_text(encoding="utf-8") or "{}")
            last = st.get("magazine") or ""
            if last:
                delta = (datetime.now(TEHRAN).date() - date.fromisoformat(last)).days
                if delta < 25:
                    log(f"مجله {delta} روز پیش منتشر شده (ماهانه) — رد شد")
                    return False
        except Exception as e:
            log(f"state خواندن نشد ({e}) — مجله ادامه")

    # ۰) نام‌کاربری زندهٔ کانال — اگر ادمین‌ها آدرس را عوض کرده باشند،
    #    هم شناسنامهٔ کانال به‌روز می‌شود هم لینک کپشن و نشان روی عکس
    live = fresh_handle(token, chat_id)
    old = (cfg.get("handle") or "").lstrip("@")
    if live and live != old:
        cfg["handle"] = f"@{live}"
        cfg["link"] = f"ble.ir/{live}"
        clean = {k: v for k, v in cfg.items() if not k.startswith("_")}
        (chdir / "channel.json").write_text(
            json.dumps(clean, ensure_ascii=False, indent=2) + "\n",
            encoding="utf-8")
        log(f"⚠️ نام‌کاربری کانال عوض شده: @{old} → @{live} — channel.json به‌روز شد")

    folder = next_queue_item(queue, kind)
    if not folder:
        log(f"صف {kind} کانال {slug} خالی است — پست جدید اضافه شود")
        return False
    log(f"[{slug}/{kind}] انتخاب پست: {folder.name}")

    # ۱) برند همان کانال ست می‌شود — هر عکس با شناسنامهٔ کانال خودش رندر می‌شود
    set_channel(cfg)
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

    # ۲) ارسال به کانال — لینک کپشن همیشه با نام‌کاربری زندهٔ همین لحظه اصلاح می‌شود
    caption = (folder / "caption.txt").read_text(encoding="utf-8").strip()
    if live:
        caption = re.sub(r"ble\.ir/[A-Za-z0-9_\-]+", f"ble.ir/{live}", caption)
    try:
        message_id = send_photo(token, chat_id, png, caption)
        log(f"منتشر شد در کانال {cfg.get('handle', chat_id)} — message_id={message_id}")
    except Exception as e:
        log(f"ERROR send {folder.name}: {e}")
        return False

    # ۲-ب) اگر پست «مجله» باشد، فایل کامل PDF در پیام بعدی به‌صورت document می‌رود
    doc_message_id = None
    pdf_file = folder / "magazine.pdf"
    if pdf_file.exists():
        doc_cap_file = folder / "doc-caption.txt"
        doc_caption = (doc_cap_file.read_text(encoding="utf-8").strip()
                       if doc_cap_file.exists() else "📄 نسخهٔ کامل PDF")
        if live:
            doc_caption = re.sub(r"ble\.ir/[A-Za-z0-9_\-]+", f"ble.ir/{live}", doc_caption)
        try:
            doc_message_id = send_document(token, chat_id, pdf_file, doc_caption)
            log(f"PDF مجله ارسال شد — message_id={doc_message_id}")
        except Exception as e:
            log(f"ERROR send doc {folder.name}: {e}")

    # ۳) کد سه‌رقمیِ «همین کانال» و انتقال به آرشیوِ همین کانال
    code = next_code(posts)
    slug_in_name = folder.name.split("-", 1)[1] if folder.name[:2].isdigit() and "-" in folder.name else folder.name
    dest_name = f"{code:03d}-{slug_in_name}"
    cat_fa = SPEC.get("category_fa", "نامشخص")   # دسته فقط داخلی — طبق شیوه‌نامه
    meta = {
        "number": code,
        "code": f"{code:03d}",
        "channel": slug,
        "category_fa": cat_fa,
        "series": SPEC.get("series_label", ""),
        "lesson": SPEC.get("lesson_no"),
        "title": SPEC.get("title", folder.name),
        "date": datetime.now(TEHRAN).date().isoformat(),
        "message_id": message_id,
        "doc_message_id": doc_message_id,
        "image": "post.png",
        "clean_image": "post-clean.png",
        "statute": cfg.get("statute", ""),
    }
    dest = posts / dest_name
    if dest.exists():
        shutil.rmtree(dest)
    shutil.copytree(folder, dest)
    (dest / "meta.json").write_text(
        json.dumps(meta, ensure_ascii=False, indent=2), encoding="utf-8")
    shutil.rmtree(folder)
    log(f"آرشیو شد: channels/{slug}/posts/{dest_name}")

    # ۴) ثبت state «قبل» از commit/push تا هیچ‌وقت با reset از دست نرود
    mark_today(state_path, kind, dest_name)

    # ۵) آمار + commit + push
    subprocess.run([sys.executable, str(REPO / "scripts" / "update_stats.py"),
                    "--channel", slug], check=False)
    git("add", "-A")
    git("commit", "-m",
        f"[{slug}] پست {code:03d} منتشر شد: {meta['title']} (دستهٔ {cat_fa})")
    git_push(env)
    return True


def main():
    slug = "coding"
    if "--channel" in sys.argv:
        i = sys.argv.index("--channel")
        if i + 1 < len(sys.argv):
            slug = sys.argv[i + 1]
    LOCK.parent.mkdir(exist_ok=True)
    with open(LOCK, "w") as lf:
        try:
            fcntl.flock(lf, fcntl.LOCK_EX | fcntl.LOCK_NB)
        except BlockingIOError:
            log("اجرای دیگری در جریان است — خروج")
            return
        env = load_env()
        git_sync_with_remote(env)
        cfg = load_channel(slug)
        if not DRY and already_today(Path(cfg["_dir"]) / "content" / "state.json", KIND):
            log(f"[{slug}/{KIND}] امروز قبلاً منتشر شده — خروج")
            return
        ok = publish(cfg, KIND, env)
        log("پایان موفق" if ok else "پایان بدون انتشار")


if __name__ == "__main__":
    main()
