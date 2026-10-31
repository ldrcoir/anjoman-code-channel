#!/usr/bin/env python3
# گردش ۳۹ - شناسایی پیام ۳۲ و بولت‌پروف آخرین آپدیت‌ها
import json, os, time, urllib.request, urllib.parse

ROOT = "/home/z/my-project"
with open(os.path.join(ROOT, ".env")) as f:
    env = dict(l.strip().split("=", 1) for l in f if "=" in l)
ENJ = env["ENERJY_TOKEN"]
EN_CHAT = 4602076598

def api(meth, **p):
    url = f"https://tapi.bale.ai/bot{ENJ}/{meth}"
    data = urllib.parse.urlencode(p).encode()
    req = urllib.request.Request(url, data=data)
    try:
        with urllib.request.urlopen(req, timeout=30) as r:
            return json.loads(r.read().decode())
    except urllib.error.HTTPError as e:
        try:
            return json.loads(e.read().decode())
        except Exception:
            return {"ok": False, "error_code": e.code, "description": "http%d" % e.code}
    except Exception as e:
        return {"ok": False, "error_code": -1, "description": str(e)[:80]}

# همهٔ آپدیت‌های معلق (offset نمی‌دهیم)
r = api("getUpdates", timeout=0)
if r.get("ok"):
    ups = r["result"]
    print(f"{len(ups)} آپدیت معلق")
    for u in ups[-12:]:
        for key in ("channel_post", "edited_channel_post", "message"):
            m = u.get(key)
            if m:
                mid = m.get("message_id")
                txt = (m.get("text") or m.get("caption") or "")[:60].replace("\n", " | ")
                ph = bool(m.get("photo"))
                doc = bool(m.get("document"))
                svc = m.get("group_chat_created") or m.get("pinned_message") or m.get("delete_chat_photo") or m.get("new_chat_photo")
                print(f"  [{key}] id={mid} photo={ph} doc={doc} service={bool(svc)} | {txt}")
else:
    print("getUpdates ناموفق:", r.get("error_code"), str(r.get("description"))[:60])

# تشخیص نوع ۳۲ با تلاش ویرایش متن
t = api("editMessageText", chat_id=EN_CHAT, message_id=32, text="probe")
print("editMessageText(32):", t.get("error_code"), str(t.get("description"))[:70])
