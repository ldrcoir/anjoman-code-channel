#!/usr/bin/env python3
"""کالیبراسیون پروب + شناسایی دو رویداد خالی انرژی — فقط خواندنی، بدون تغییر."""
import json, os, time
from urllib.request import Request, urlopen

BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
env = {}
with open(os.path.join(BASE_DIR, ".env")) as f:
    for line in f:
        if "=" in line and not line.strip().startswith("#"):
            k, v = line.split("=", 1); env[k.strip()] = v.strip()

def call(token, method, payload):
    req = Request(f"https://tapi.bale.ai/bot{token}/{method}",
                  data=json.dumps(payload).encode(),
                  headers={"Content-Type": "application/json"})
    try:
        with urlopen(req, timeout=20) as r:
            return json.loads(r.read().decode())
    except Exception as e:
        body = ""
        try: body = e.read().decode()[:200]
        except Exception: pass
        return {"ok": False, "err": str(e)[:80], "body": body}

coding = env["BALE_TOKEN"].strip()
enerjy = env["ENERJY_TOKEN"].strip()

# ۱) کالیبراسیون روی جفت شناخته‌شده: ۱۶۰ زنده / ۲۳ حذف‌شده (کدنویسی)
print("== calibration ==")
for mid in (160, 23):
    for label, payload in (("bare", {"message_id": mid}),
                            ("empty-kb", {"message_id": mid, "reply_markup": {"inline_keyboard": []}})):
        r = call(coding, "editMessageReplyMarkup", payload)
        print(f"  mid={mid} {label}: ok={r.get('ok')} {r.get('body', r.get('err',''))[:90]}")
        time.sleep(0.4)

# ۲) رویدادهای خام انرژی — ببینیم آن دو آپدیت خالی چیستند
print("== raw enerjy updates ==")
res = call(enerjy, "getUpdates", {"offset": 0, "limit": 100, "timeout": 0})
if res.get("ok"):
    ups = res.get("result", [])
    print("  count:", len(ups))
    for u in ups:
        print("  ", json.dumps(u, ensure_ascii=False)[:400])
else:
    print("  getUpdates failed:", r if False else res)
