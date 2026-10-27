#!/usr/bin/env python3
"""گردش ۳۴ — پروب بی‌اثر هر دو کانال پیش از انتشار دو پست جدید (دستور مالک).
هیچ forward/send/delete انجام نمی‌شود؛ فقط editMessageReplyMarkup (بی‌اثر) + فید getUpdates.
خروجی: tool-results/g34_probe.json"""
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
    "coding": {"token": env["BALE_TOKEN"].strip(), "chat_id": 5100057643,
               "known": [8, 12, 13, 14, 15, 16, 18, 20, 22, 24, 27, 41, 42, 46,
                          52, 54, 159, 160, 189, 190, 192, 194, 195, 196, 197, 200]},
    "enerjy": {"token": env["ENERJY_TOKEN"].strip(), "chat_id": 4602076598,
               "known": [3, 4, 8, 9, 10, 23, 24, 25]},
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
        tok = ch["token"]
        with ThreadPoolExecutor(max_workers=4) as ex:
            statuses = list(ex.map(lambda m: (m, probe(ch, m)), ch["known"]))
        alive = [m for m, s in statuses if s == "alive"]
        locked = [m for m, s in statuses if s == "locked-alive"]
        gone = [m for m, s in statuses if s.startswith("gone")]
        other = {m: s for m, s in statuses if s not in ("alive", "locked-alive") and not s.startswith("gone")}
        # فید تازه‌ها — فقط رویدادهای پیام (نه عضویت/خروج)
        ups = drain_raw(tok)
        msgs = []
        for u in ups:
            for key in ("channel_post", "message", "edited_message", "edited_channel_post"):
                if key in u:
                    m = u[key]
                    msgs.append({"id": m.get("message_id"),
                                 "kind": key,
                                 "has_photo": "photo" in m,
                                 "has_video": "video" in m,
                                 "caption_head": (m.get("caption") or m.get("text") or "")[:80]})
        # بالاترین پیام شناخته‌شده + جست‌وجوی پیام‌های بالاتر (مثلاً ۲۰۱+)
        top = max(alive + locked) if (alive or locked) else None
        extra = []
        if top:
            with ThreadPoolExecutor(max_workers=4) as ex:
                res2 = list(ex.map(lambda m: (m, probe(ch, m)), range(top + 1, top + 12)))
            extra = [(m, s) for m, s in res2 if s in ("alive", "locked-alive")]
        out["channels"][name] = {
            "alive": alive, "locked": locked, "gone": gone,
            "other": other, "feed_msgs": msgs,
            "top_known": top,
            "extra_alive_above_top": extra,
        }
        print(f"[{name}] alive={alive}")
        print(f"[{name}] locked={locked} gone={gone} other={other}")
        print(f"[{name}] feed={len(msgs)} msgs | extra above top: {extra}")
        for m in msgs:
            print("   feed:", m)
        time.sleep(1)

    out_dir = "/home/z/my-project/tool-results"
    os.makedirs(out_dir, exist_ok=True)
    with open(os.path.join(out_dir, "g34_probe.json"), "w", encoding="utf-8") as f:
        json.dump(out, f, ensure_ascii=False, indent=1)
    print("saved tool-results/g34_probe.json")

if __name__ == "__main__":
    main()
