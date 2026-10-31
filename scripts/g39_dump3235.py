#!/usr/bin/env python3
# گردش ۳۹ - JSON کامل آپدیت‌های معلق + دانلود عکس پیام ۳۲ و ۳۵ برای شناسایی
import json, os, urllib.request, urllib.parse

ROOT = "/home/z/my-project"
with open(os.path.join(ROOT, ".env")) as f:
    env = dict(l.strip().split("=", 1) for l in f if "=" in l)
ENJ = env["ENERJY_TOKEN"]
EN_CHAT = 4602076598
OUT = os.path.join(ROOT, "tool-results")
os.makedirs(OUT, exist_ok=True)

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

r = api("getUpdates", timeout=0)
if not r.get("ok"):
    print("getUpdates FAIL", r.get("error_code")); raise SystemExit

ups = r["result"]
print(f"{len(ups)} آپدیت")
full_dump = []
for u in ups:
    for key in ("channel_post", "edited_channel_post", "message"):
        m = u.get(key)
        if not m:
            continue
        mid = m.get("message_id")
        full_dump.append(u)
        if mid in (32, 35):
            with open(os.path.join(OUT, f"g39_msg{mid}_update.json"), "w") as f:
                json.dump(u, f, ensure_ascii=False, indent=1)
            ph = m.get("photo") or []
            best = max(ph, key=lambda s: (s.get("width") or 0) * (s.get("height") or 0), default=None)
            fid = (best or {}).get("file_id")
            sender = m.get("from", {}).get("username") or m.get("from", {}).get("id")
            print(f"پیام {mid}: sender={sender} date={m.get('date')} file_id={'دارد' if fid else 'ندارد'}")
            if fid:
                gf = api("getFile", file_id=fid)
                if gf.get("ok"):
                    fp = gf["result"].get("file_path") or gf["result"].get("path")
                    url = f"https://tapi.bale.ai/file/bot{ENJ}/{fp}"
                    dest = os.path.join(OUT, f"g39_msg{mid}_live.jpg")
                    urllib.request.urlretrieve(url, dest)
                    print(f"  دانلود شد: {dest} ({os.path.getsize(dest)//1024}KB)")
                else:
                    print("  getFile FAIL:", gf.get("error_code"), str(gf.get("description"))[:50])
            cap = (m.get("caption") or "")[:100].replace("\n", " | ")
            print(f"  کپشن: {cap!r}")

with open(os.path.join(OUT, "g39_pending_all.json"), "w") as f:
    json.dump(full_dump, f, ensure_ascii=False, indent=1)
print("خلاصه ذخیره شد")
