#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
resend_prev — «قبلی را هم ویرایش کن» (گردش ۲۱)
بله هنوز editMessageMedia ندارد (501 Coming soon)؛ پس ویرایش تصویر پستِ
منتشرشده فقط با حذف و ارسال مجدد ممکن است. این اسکریپت:
  ۱) کپشن آرشیو را با fa_lint و سقف ۱۰۲۴ نویسه سنجیده و با نام‌کاربری زندهٔ کانال تراز می‌کند
  ۲) پیام قدیمی را حذف و همان پست را با پوسترِ جدید طراحی می‌فرستد
  ۳) message_id تازه را در meta.json همان پست، stats.json و stats.md همگام می‌کند
ترتیب: اول ۰۱۳ خوارزمی، بعد ۰۱۴ درس ۴ — تا ترتیب نسبی کانال حفظ شود.
"""
import json
import re
import sys
import time
from pathlib import Path

import requests

REPO = Path(__file__).resolve().parent.parent
sys.path.insert(0, str(REPO / "scripts"))
import fa_lint  # noqa: E402

# توکن از .env ریشه یا مخزن
for env_p in (REPO / ".env", REPO.parent / ".env"):
    if env_p.exists():
        for line in env_p.read_text().splitlines():
            if line.startswith("BALE_TOKEN="):
                TOK = line.split("=", 1)[1].strip()
        if env_p.name == ".env" and REPO.parent.name == "my-project" and env_p.parent == REPO.parent:
            break
CHAT_ID = "5100057643"
API = f"https://tapi.bale.ai/bot{TOK}"

POSTS = {
    "013": {"folder": REPO / "channels/coding/posts/013-kharazmi",
            "old_id": None, "new_id": 53},   # قبلاً ارسال مجدد شد — فقط همگام‌سازی آمار
    "014": {"folder": REPO / "channels/coding/posts/014-rust-dars-04-commentha",
            "old_id": 50, "new_id": None},
}


def api(method, **data):
    r = requests.post(f"{API}/{method}", data=data, timeout=30)
    return r.json()


def live_username():
    j = api("getChat", chat_id=CHAT_ID)
    if j.get("ok"):
        return (j["result"].get("username") or "").strip()
    return ""


def send_photo(png, caption):
    with open(png, "rb") as f:
        r = requests.post(f"{API}/sendPhoto",
                          data={"chat_id": CHAT_ID, "caption": caption},
                          files={"photo": f}, timeout=60)
    return r.json()


def sync_stats(code, new_id, folder):
    for rel in ("channels/coding/content/stats.json",
                folder.relative_to(REPO) / "meta.json"):
        p = REPO / rel
        d = json.loads(p.read_text(encoding="utf-8"))
        if "posts" in d:
            for post in d["posts"]:
                if post.get("code") == code:
                    post["message_id"] = new_id
        else:
            d["message_id"] = new_id
        p.write_text(json.dumps(d, ensure_ascii=False, indent=2) + "\n",
                     encoding="utf-8")
    p = REPO / "channels/coding/content/stats.md"
    txt = p.read_text(encoding="utf-8")
    txt = re.sub(rf"(\| {code} \|[^\n]*\| )\d+( \|)",
                 rf"\g<1>{new_id}\2", txt)
    p.write_text(txt, encoding="utf-8")


def main():
    lu = live_username()
    print(f"نام‌کاربری زندهٔ کانال: @{lu or 'نامشخص'}")
    results = {}
    for code, info in POSTS.items():
        folder = info["folder"]
        if info.get("new_id"):
            sync_stats(code, info["new_id"], folder)
            print(f"{code}: آمار با شناسهٔ {info['new_id']} همگام شد")
            continue
        caption = (folder / "caption.txt").read_text(encoding="utf-8").strip()
        if lu:
            caption = re.sub(r"ble\.ir/[A-Za-z0-9_\-]+", f"ble.ir/{lu}", caption)
        if fa_lint.lint_text(caption, source=code):
            print(f"FA_LINT {code}: کپشن غلط دارد — ادامه نداد")
            return 1
        n = len(caption)
        print(f"{code}: کپشن {n} نویسه (سقف ۱۰۲۴) → {'OK' if n <= 1024 else 'بیش از حد!'}")
        if n > 1024:
            return 1
        old_id = info["old_id"]
        j = api("deleteMessage", chat_id=CHAT_ID, message_id=old_id)
        print(f"{code}: حذف پیام {old_id} → ok={j.get('ok')} {j.get('description','')}")
        if not j.get("ok"):
            print(f"{code}: حذف نشد — این پست را دست نمی‌زنم")
            results[code] = None
            continue
        new_id = None
        for attempt in range(3):
            j2 = send_photo(folder / "post.png", caption)
            if j2.get("ok"):
                new_id = j2["result"]["message_id"]
                break
            print(f"{code}: تلاش ارسال {attempt + 1} ناموفق: {j2.get('description')}")
            time.sleep(3)
        if new_id is None:
            print(f"CRITICAL {code}: پیام قدیمی حذف شد و ارسال جدید شکست خورد! دستی بررسی شود")
            return 2
        print(f"{code}: منتشر شد → message_id={new_id}")
        sync_stats(code, new_id, folder)
        results[code] = new_id
    print("نتیجه:", json.dumps(results, ensure_ascii=False))
    return 0


if __name__ == "__main__":
    sys.exit(main())
