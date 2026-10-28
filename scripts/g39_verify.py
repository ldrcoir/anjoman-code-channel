#!/usr/bin/env python3
# گردش ۳۹ - راستی‌آزمایی: پیام‌های قدیم مرده، جدید زنده، پیام ۳۲ چیست
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

print("== پیام‌های قدیم (باید مرده باشند) ==")
for old in [3, 23, 24, 26, 28]:
    r = api("editMessageReplyMarkup", chat_id=EN_CHAT, message_id=old)
    d = str(r.get("description", ""))[:50]
    print(f"  {old}: {'❗ هنوز زنده' if r.get('ok') else 'مرده ✓ (' + d + ')'}")

print("== پیام‌های جدید + همسایه‌ها (۳۲ چیست؟) ==")
for mid in [29, 30, 31, 32, 33, 34]:
    r = api("editMessageReplyMarkup", chat_id=EN_CHAT, message_id=mid)
    print(f"  {mid}: {'زنده' if r.get('ok') else 'ناموجود (' + str(r.get('description'))[:40] + ')'}")
