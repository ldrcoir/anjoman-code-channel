#!/usr/bin/env python3
"""گردش ۳۷ — پروب بی‌اثر کانال کدنویسی پیش از اصلاح نشریه (دستور مالک: شمارهٔ ۲ آبان → شمارهٔ ۱ مهر)
مالک گفته بود نشریهٔ قدیمی پاک شده؛ باید ببینیم الان دقیقاً چه چیزی زنده است.
هیچ forward/send/delete انجام نمی‌شود؛ فقط editMessageReplyMarkup (بی‌اثر) + فید getUpdates."""
import json, os, time
from urllib.request import Request, urlopen
from concurrent.futures import ThreadPoolExecutor

BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
env = {}
with open(os.path.join(BASE_DIR, ".env")) as f:
    for line in f:
        if "=" in line and not line.strip().startswith("#"):
            k, v = line.split("=", 1); env[k.strip()] = v.strip()

TOK = env["BALE_TOKEN"].strip()
CID = 5100057643
KNOWN = [8, 12, 13, 14, 15, 16, 18, 20, 22, 24, 27, 41, 42, 46, 52, 54,
         159, 160, 189, 190, 192, 194, 200, 201, 202, 203, 204, 205, 206, 207]


def call(method, payload):
    req = Request(f"https://tapi.bale.ai/bot{TOK}/{method}",
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


def probe(mid):
    r = call("editMessageReplyMarkup", {"chat_id": CID, "message_id": mid})
    if r.get("ok"): return "alive"
    s = json.dumps(r, ensure_ascii=False)
    if "not found" in s.lower(): return "gone"
    if "403" in s or "forbidden" in s.lower(): return "locked-alive"
    if "429" in s: return "rate-limited"
    return "other:" + s[:100]


def drain_feed():
    offset, ups = 0, []
    for _ in range(40):
        res = call("getUpdates", {"offset": offset, "limit": 100, "timeout": 0})
        if not res.get("ok"): break
        batch = res.get("result", [])
        if not batch: break
        ups.extend(batch)
        offset = max(u["update_id"] for u in batch) + 1
        if len(batch) < 100: break
    return ups


def main():
    with ThreadPoolExecutor(max_workers=4) as ex:
        statuses = list(ex.map(probe, KNOWN))
    alive = [m for m, s in zip(KNOWN, statuses) if s == "alive"]
    locked = [m for m, s in zip(KNOWN, statuses) if s == "locked-alive"]
    gone = [m for m, s in zip(KNOWN, statuses) if s.startswith("gone")]
    other = {m: s for m, s in zip(KNOWN, statuses)
             if s not in ("alive", "locked-alive") and not s.startswith("gone")}
    print("alive:", alive)
    print("locked:", locked, "| gone:", gone, "| other:", other)

    ups = drain_feed()
    for u in ups:
        for key in ("channel_post", "message", "edited_message", "edited_channel_post"):
            if key in u:
                m = u[key]
                print("feed:", m.get("message_id"), key,
                      (m.get("caption") or m.get("text") or "")[:60].replace("\n", " "))

    top = max(alive + locked) if (alive or locked) else None
    extra = []
    if top:
        with ThreadPoolExecutor(max_workers=4) as ex2:
            res2 = list(ex2.map(probe, range(top + 1, top + 16)))
        extra = [(m, s) for m, s in zip(range(top + 1, top + 16), res2)
                 if s in ("alive", "locked-alive")]
        # هر پیام «other» بالای کانال هم گزارش شود
        extra_other = [(m, s) for m, s in zip(range(top + 1, top + 16), res2)
                       if s.startswith("other")]
        print("extra alive above top:", extra, "| other:", extra_other)

    out = {"time": time.strftime("%Y-%m-%d %H:%M:%S"), "alive": alive, "locked": locked,
           "gone": gone, "other": other, "extra": extra}
    os.makedirs("/home/z/my-project/tool-results", exist_ok=True)
    with open("/home/z/my-project/tool-results/g37_probe.json", "w", encoding="utf-8") as f:
        json.dump(out, f, ensure_ascii=False, indent=1)
    print("saved tool-results/g37_probe.json")


if __name__ == "__main__":
    main()
