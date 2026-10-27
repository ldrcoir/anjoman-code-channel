#!/usr/bin/env python3
"""گردش ۳۰ — آرشیو ساکت پست ادمین (پیام ۱۸۳: درس ۱ پایتون، ویدیو).
فقط getFile/دانلود — هیچ کاری در کانال نمی‌کند."""
import json, os, time, shutil
from urllib.request import Request, urlopen

BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
env = {}
with open(os.path.join(BASE_DIR, ".env")) as f:
    for line in f:
        if "=" in line and not line.strip().startswith("#"):
            k, v = line.split("=", 1); env[k.strip()] = v.strip()
TOKEN = env["BALE_TOKEN"].strip()
CHAT_ID = 5100057643
MSG_ID = 183

def call(method, payload):
    req = Request(f"https://tapi.bale.ai/bot{TOKEN}/{method}",
                  data=json.dumps(payload).encode(),
                  headers={"Content-Type": "application/json"})
    try:
        with urlopen(req, timeout=30) as r:
            return json.loads(r.read().decode())
    except Exception as e:
        try: body = e.read().decode()[:200]
        except Exception: body = ""
        return {"ok": False, "err": str(e)[:100], "body": body}

def download(file_id, dest):
    g = call("getFile", {"file_id": file_id})
    if not g.get("ok"):
        print("  getFile failed:", g); return False
    path = g["result"]["file_path"]
    url = f"https://tapi.bale.ai/file/bot{TOKEN}/{path}"
    with urlopen(url, timeout=180) as r, open(dest, "wb") as f:
        shutil.copyfileobj(r, f)
    print(f"  saved {dest} ({os.path.getsize(dest)/1e6:.1f} MB)")
    return True

def main():
    ev = None
    d = json.load(open(os.path.join(BASE_DIR, "docs", "tool-results", "g31_probe2.json")))
    for e in d["channels"]["coding"]["recent_raw_events"]:
        if e["event"].get("message_id") == MSG_ID:
            ev = e["event"]; break
    assert ev, "event 183 not found"

    outdir = os.path.join(BASE_DIR, "channels", "coding", "posts", "b1-amoozesh", "022-python-dars-01-nasb")
    os.makedirs(outdir, exist_ok=True)

    vid = ev["video"]
    ok = download(vid["file_id"], os.path.join(outdir, "python-dars-01.mp4"))

    with open(os.path.join(outdir, "caption.txt"), "w") as f:
        f.write(ev.get("caption", ""))

    meta = {
        "message_id": MSG_ID,
        "chat": "انجمن کدنویسی",
        "chat_id": CHAT_ID,
        "published_at": "2026-10-26T13:07:26+03:30",
        "published_by": "ادمین کانال (هویت کانال — طبق دستور مالک ثبت نمی‌شود)",
        "series": "آموزش پایتون از صفر",
        "lesson_no": 1,
        "title": "درس ۱ — نصب پایتون و اولین اجرا (ویدیو)",
        "shevnameh": "بند ۱ — آموزش",
        "hashtags": "#آموزش_پایتون #Python #برنامه_نویسی #آموزش_کدنویسی",
        "media": {"type": "video", "resolution": "1920x1080", "duration_sec": vid["duration"],
                   "size_bytes": vid["file_size"], "archived_as": "python-dars-01.mp4"},
        "status": "live",
        "archived_in": "گردش ۳۰ — آرشیو ساکت؛ پست ادمین، دست نخورده",
        "video_download_ok": ok,
    }
    with open(os.path.join(outdir, "meta.json"), "w") as f:
        json.dump(meta, f, ensure_ascii=False, indent=1)
    print("meta.json written")
    # باید راستی‌آزمایی شود که پیام هنوز زنده است (پروب)
    r = call("editMessageReplyMarkup", {"chat_id": CHAT_ID, "message_id": MSG_ID})
    print("still alive:", r.get("ok"))

if __name__ == "__main__":
    main()
