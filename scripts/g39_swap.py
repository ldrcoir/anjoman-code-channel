#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
گردش ۳۹ — تعویض ۵ پوستر «ششمین دوره» کانال انرژی‌های نوین
مجوز مالک: «تونستی ویرایش کن نتونستی پاک کن از اول» (editMessageMedia در بله ۵۰۱ است → حذف+بازنشر)
روش: یکی‌یکی — حذف پیام قدیم، بلافاصله بازنشر پوستر اصلاح‌شده با کپشن عین آرشیو
بدون forwardMessage (ممنوع دائمی) — بدون parse_mode (متن لیترال)
"""
import json, os, time, urllib.request, urllib.parse

ROOT = "/home/z/my-project"
REPO = os.path.join(ROOT, "repo-tmp", "channels", "enerjy-novin")
SWAP = os.path.join(REPO, "photo-swap-g36")
with open(os.path.join(ROOT, ".env")) as f:
    env = dict(l.strip().split("=", 1) for l in f if "=" in l)
ENJ = env["ENERJY_TOKEN"]
EN_CHAT = 4602076598
STATE = os.path.join(ROOT, "tool-results", "g39_swap_state.json")
LOG = os.path.join(ROOT, "tool-results", "g39_swap.log")

def log(msg):
    line = f"[{time.strftime('%H:%M:%S')}] {msg}"
    print(line, flush=True)
    with open(LOG, "a") as f:
        f.write(line + "\n")

def load_state():
    if os.path.exists(STATE):
        with open(STATE) as f:
            return json.load(f)
    return {}

def save_state(st):
    with open(STATE, "w") as f:
        json.dump(st, f, ensure_ascii=False, indent=1)

def api(meth, **p):
    url = f"https://tapi.bale.ai/bot{ENJ}/{meth}"
    data = urllib.parse.urlencode(p).encode()
    req = urllib.request.Request(url, data=data)
    try:
        with urllib.request.urlopen(req, timeout=40) as r:
            return json.loads(r.read().decode())
    except urllib.error.HTTPError as e:
        try:
            return json.loads(e.read().decode())
        except Exception:
            return {"ok": False, "error_code": e.code, "description": "http%d" % e.code}
    except Exception as e:
        return {"ok": False, "error_code": -1, "description": str(e)[:80]}

def api_retry(meth, tries=4, **p):
    for i in range(tries):
        r = api(meth, **p)
        if r.get("ok"):
            return r
        ec = r.get("error_code")
        d = str(r.get("description", ""))[:70]
        if ec == 429:
            log(f"  {meth}: 429 → سکوت ۳۵ث")
            time.sleep(35)
            continue
        if ec == 401:
            log(f"  {meth}: فلیپ ۴۰۱ (تلاش {i+1}/{tries}) → ۱۵ث")
            time.sleep(15)
            continue
        return r  # خطای معنادار (مثلاً not found) → بدون ری‌ترای
    return r

# نگاشت: پیام قدیم → (پوستر اصلاح‌شده، کپشن آرشیو)
ITEMS = [
    (3,  "msg003-moarefi.png",        "posts/te-moarefi/001-moarefi-enerjy/caption.txt"),
    (23, "msg023-enerjy-bad.png",     "posts/alef-tarviji/008-enerjy-badi-live/caption.txt"),
    (24, "msg024-niroogah-saveh.png", "posts/pe-niroogah/006-niroogah-kaveh-saveh/caption.txt"),
    (26, "msg026-zamin-germaei.png",  "posts/alef-tarviji/009-enerjy-zamin-germaei/caption.txt"),
    (28, "msg028-enerjy-ab.png",      "posts/alef-tarviji/010-enerjy-ab/caption.txt"),
]

st = load_state()
log(f"شروع | state: {json.dumps(st, ensure_ascii=False)}")

for old_id, img, cap_rel in ITEMS:
    key = str(old_id)
    if st.get(key, {}).get("new_id"):
        log(f"{key}: قبلاً کامل شده (new={st[key]['new_id']}) — رد شد")
        continue
    img_path = os.path.join(SWAP, img)
    with open(cap_rel and os.path.join(REPO, cap_rel), encoding="utf-8") as f:
        caption = f.read().strip()
    log(f"— پیام {key}: پوستر {img} ({os.path.getsize(img_path)//1024}KB)، کپشن {len(caption)} نویسه —")

    # ۱) حذف پیام قدیم (اگر هنوز حذف نشده)
    if not st.get(key, {}).get("deleted"):
        d = api_retry("deleteMessage", chat_id=EN_CHAT, message_id=old_id)
        if not d.get("ok"):
            log(f"  حذف {key} ناموفق: {d.get('error_code')} {str(d.get('description'))[:60]}")
            save_state(st)
            time.sleep(20)
            continue
        st[key] = {"deleted": True, "new_id": None}
        save_state(st)
        log(f"  حذف {key} ✓")
        time.sleep(2)
    else:
        log(f"  {key} قبلاً حذف شده بود — مستقیم بازنشر")

    # ۲) بازنشر پوستر اصلاح‌شده با کپشن عین آرشیو
    boundary = "----g39bale"
    with open(img_path, "rb") as f:
        img_bytes = f.read()
    parts = []
    def field(name, val):
        parts.append(f"--{boundary}\r\nContent-Disposition: form-data; name=\"{name}\"\r\n\r\n{val}\r\n".encode())
    field("chat_id", str(EN_CHAT))
    body_cap = caption.encode("utf-8")
    parts.append(
        f"--{boundary}\r\nContent-Disposition: form-data; name=\"caption\"; filename=\"cap.txt\"\r\nContent-Type: text/plain; charset=utf-8\r\n\r\n".encode()
        + body_cap + b"\r\n")
    parts.append(
        f"--{boundary}\r\nContent-Disposition: form-data; name=\"photo\"; filename=\"{img}\"\r\nContent-Type: image/png\r\n\r\n".encode()
        + img_bytes + b"\r\n")
    parts.append(f"--{boundary}--\r\n".encode())
    body = b"".join(parts)

    sent = None
    for i in range(4):
        req = urllib.request.Request(
            f"https://tapi.bale.ai/bot{ENJ}/sendPhoto", data=body,
            headers={"Content-Type": f"multipart/form-data; boundary={boundary}"})
        try:
            with urllib.request.urlopen(req, timeout=60) as r:
                sent = json.loads(r.read().decode())
        except urllib.error.HTTPError as e:
            try:
                sent = json.loads(e.read().decode())
            except Exception:
                sent = {"ok": False, "error_code": e.code, "description": "http%d" % e.code}
        except Exception as e:
            sent = {"ok": False, "error_code": -1, "description": str(e)[:80]}
        if sent.get("ok"):
            break
        ec = sent.get("error_code")
        log(f"  sendPhoto تلاش {i+1}: {ec} {str(sent.get('description'))[:60]}")
        time.sleep(35 if ec == 429 else 15)
    if not sent or not sent.get("ok"):
        log(f"  بازنشر {key} ناموفق ماند — متوقف (وضعیت ذخیره شد)")
        save_state(st)
        break
    new_id = sent["result"]["message_id"]
    st[key] = {"deleted": True, "new_id": new_id}
    save_state(st)
    log(f"  بازنشر ✓ پیام {key} → جدید {new_id}")
    time.sleep(3)

done = sum(1 for _, k, _ in ITEMS if st.get(str(k), {}).get("new_id"))
log(f"پایان اجرا: {done}/۵ کامل | state: {json.dumps(st, ensure_ascii=False)}")
