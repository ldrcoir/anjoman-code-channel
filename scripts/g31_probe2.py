#!/usr/bin/env python3
"""گردش ۳۰ — پروب اصلاح‌شده (با chat_id) + ثبت خام رویداد پیام ۱۸۳.
هیچ forward/send ممنوع؛ در صورت یافتن کپی ممنوعهٔ زنده فقط deleteMessage."""
import json, os, time
from urllib.request import Request, urlopen
from concurrent.futures import ThreadPoolExecutor

BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
env = {}
with open(os.path.join(BASE_DIR, ".env")) as f:
    for line in f:
        if "=" in line and not line.strip().startswith("#"):
            k, v = line.split("=", 1); env[k.strip()] = v.strip()

CH = {
    "coding": {"token": env["BALE_TOKEN"].strip(), "chat_id": 5100057643},
    "enerjy": {"token": env["ENERJY_TOKEN"].strip(), "chat_id": 4602076598},
}

def call(token, method, payload):
    req = Request(f"https://tapi.bale.ai/bot{token}/{method}",
                  data=json.dumps(payload).encode(),
                  headers={"Content-Type": "application/json"})
    try:
        with urlopen(req, timeout=20) as r:
            return json.loads(r.read().decode())
    except Exception as e:
        body = ""
        try: body = e.read().decode()[:250]
        except Exception: pass
        return {"ok": False, "err": str(e)[:80], "body": body}

def probe(ch, mid):
    cid, tok = ch["chat_id"], ch["token"]
    r = call(tok, "editMessageReplyMarkup", {"chat_id": cid, "message_id": mid})
    if r.get("ok"): return "alive"
    s = json.dumps(r, ensure_ascii=False)
    if "not found" in s.lower(): return "gone"
    if "403" in s or "forbidden" in s.lower() or "permission" in s.lower(): return "locked-alive"
    if "429" in s: return "rate-limited"
    return "other:" + s[:100]

def drain_raw(token):
    offset, ups = 0, []
    for _ in range(40):
        res = call(token, "getUpdates", {"offset": offset, "limit": 100, "timeout": 0})
        if not res.get("ok"): break
        batch = res.get("result", [])
        if not batch: break
        ups.extend(batch)
        offset = max(u["update_id"] for u in batch) + 1
        if len(batch) < 100: break
    return ups

def main():
    out = {"time": time.strftime("%Y-%m-%d %H:%M:%S"), "channels": {}}
    for name, ch in CH.items():
        # رویداد خام تازه‌ها
        raw = drain_raw(ch["token"])
        posts = []
        for u in raw:
            for key in ("channel_post", "message", "edited_channel_post", "edited_message"):
                ev = u.get(key)
                if ev and (ev.get("date") or 0) >= 1792900000:  # از دیروز به بعد
                    posts.append({"update_id": u["update_id"], "key": key, "event": ev})
        # پروب
        live_expect = {
            "coding": [8, 12, 13, 14, 15, 16, 18, 20, 22, 24, 27, 41, 42, 46, 48, 52, 54, 159, 160, 183],
            "enerjy": [3, 4, 8, 9, 10],
        }[name]
        forbid = {
            "coding": [23, 43, 53, 157, 158, 161, 162, 163, 165] + list(range(166, 183)),
            "enerjy": [11, 13, 15] + list(range(16, 40)),
        }[name]
        res = {}
        with ThreadPoolExecutor(max_workers=3) as ex:
            for mid, st in zip(live_expect + forbid, ex.map(lambda m: probe(ch, m), live_expect + forbid)):
                res[mid] = st
        deleted = []
        for mid in forbid:
            if str(res.get(mid)).startswith(("alive", "locked-alive")):
                d = call(ch["token"], "deleteMessage", {"chat_id": ch["chat_id"], "message_id": mid})
                if d.get("ok"):
                    deleted.append(mid); res[mid] = "deleted-now"
        out["channels"][name] = {
            "probe": {str(k): v for k, v in res.items()},
            "live_confirmed": sorted(m for m, s in res.items() if str(s).startswith(("alive", "locked-alive"))),
            "deleted_now": deleted,
            "recent_raw_events": posts,
        }
        print(f"[{name}] live={out['channels'][name]['live_confirmed']} deleted_now={deleted} recent_events={len(posts)}")
        time.sleep(2)

    tr = os.path.join(BASE_DIR, "docs", "tool-results")
    os.makedirs(tr, exist_ok=True)
    with open(os.path.join(tr, "g31_probe2.json"), "w") as f:
        json.dump(out, f, ensure_ascii=False, indent=1)
    print("saved → docs/tool-results/g31_probe2.json")

if __name__ == "__main__":
    main()
