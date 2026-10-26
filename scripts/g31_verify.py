#!/usr/bin/env python3
"""گردش ۳۰ — راستی‌آزمایی بی‌صدا هر دو کانال.
قواعد: هیچ forward / send / edit-content ممنوع. فقط getUpdates + editMessageReplyMarkup-probe
(روی پیام‌های بدون دکمه اثر ظاهری ندارد) و در صورت یافتن کپیِ جامانده، deleteMessage.
"""
import json, time, os, sys
from urllib.request import Request, urlopen
from concurrent.futures import ThreadPoolExecutor

BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))

def load_env():
    env = {}
    with open(os.path.join(BASE_DIR, ".env")) as f:
        for line in f:
            line = line.strip()
            if line and not line.startswith("#") and "=" in line:
                k, v = line.split("=", 1)
                env[k.strip()] = v.strip()
    return env

ENV = load_env()
CHANNELS = {
    "coding": ENV["BALE_TOKEN"].strip(),
    "enerjy": ENV["ENERJY_TOKEN"].strip(),
}

def call(token, method, payload=None):
    url = f"https://tapi.bale.ai/bot{token}/{method}"
    data = json.dumps(payload).encode() if payload is not None else b"{}"
    req = Request(url, data=data, headers={"Content-Type": "application/json"})
    try:
        with urlopen(req, timeout=20) as r:
            return json.loads(r.read().decode())
    except Exception as e:
        body = ""
        try:
            body = e.read().decode()[:300]
        except Exception:
            pass
        return {"ok": False, "error": str(e)[:200], "body": body}

def drain(token):
    """فید کامل رویدادها را می‌کشد (صفحه‌به‌صفحه) — هیچ تغییر واقعی نمی‌دهد."""
    offset, all_updates = 0, []
    for _ in range(40):
        res = call(token, "getUpdates", {"offset": offset, "limit": 100, "timeout": 0})
        if not res.get("ok"):
            break
        ups = res.get("result", [])
        if not ups:
            break
        all_updates.extend(ups)
        offset = max(u["update_id"] for u in ups) + 1
        if len(ups) < 100:
            break
    return all_updates

def summarize_updates(name, ups):
    msgs = []
    for u in ups:
        for key in ("channel_post", "message", "edited_channel_post", "edited_message"):
            ev = u.get(key)
            if ev:
                msgs.append({
                    "kind": key,
                    "message_id": ev.get("message_id"),
                    "date": ev.get("date"),
                    "from": (ev.get("from") or {}).get("username") or (ev.get("from") or {}).get("id"),
                    "sender_chat": ((ev.get("sender_chat") or {}).get("username")
                                     or (ev.get("sender_chat") or {}).get("id")),
                    "forward": bool(ev.get("forward_from") or ev.get("forward_from_chat")),
                    "is_automatic_forward": ev.get("is_automatic_forward", False),
                    "text_head": ((ev.get("text") or ev.get("caption") or "")[:60]).replace("\n", " "),
                    "update_id": u["update_id"],
                })
    msgs.sort(key=lambda m: (m["date"] or 0, m["update_id"] or 0))
    return msgs

def probe(token, mid):
    """پروب وجود: editMessageReplyMarkup با keyboard خالیِ حذف‌شده.
    ok=True → زنده؛ 400 'message to edit not found' → حذف‌شده؛ 403 → زنده ولی قفل."""
    res = call(token, "editMessageReplyMarkup", {"message_id": mid, "reply_markup": None})
    if res.get("ok"):
        return "alive"
    s = json.dumps(res, ensure_ascii=False)
    if "not found" in s.lower():
        return "gone"
    if "403" in s or "Forbidden" in s or "permission" in s.lower():
        return "locked-alive"
    return f"other:{s[:80]}"

def main():
    out = {"time": time.strftime("%Y-%m-%d %H:%M:%S"), "channels": {}}
    for name, token in CHANNELS.items():
        ups = drain(token)
        msgs = summarize_updates(name, ups)
        # رویدادهای جدیدِ بعد از دیروز (2026-10-25 19:51 ~ epoch 1792040000)
        recent = [m for m in msgs if (m["date"] or 0) >= time.mktime(time.strptime("2026-10-25 12:00:00", "%Y-%m-%d %H:%M:%S"))]
        # هر رویداد فورواردی = کپی موقت من
        fwd = [m for m in msgs if m["forward"] or m["is_automatic_forward"]]
        out["channels"][name] = {
            "updates_total": len(ups),
            "events": msgs[-25:],
            "recent_since_yesterday_noon": recent,
            "forwarded_copies_in_feed": fwd,
        }
        print(f"[{name}] updates={len(ups)} recent={len(recent)} forwarded={len(fwd)}")
        for m in msgs[-12:]:
            print("   ", m)

    # پروب وجود کپی‌های تاریخی ممنوعه + پیام‌های مرجع (بازنامهٔ گردش ۲۹)
    coding = CHANNELS["coding"]; enerjy = CHANNELS["enerjy"]
    checks = {
        "coding": {"forbidden_copies": [23, 43, 53, 157, 158, 161, 162, 163, 165],
                    "must_be_alive": [159, 160]},
        "enerjy": {"forbidden_copies": [11, 13, 15], "must_be_alive": [3, 4, 8, 9, 10]},
    }
    ranges = {"coding": list(range(166, 181)), "enerjy": list(range(12, 30))}
    for name, token in CHANNELS.items():
        res = {}
        todo = checks[name]["forbidden_copies"] + checks[name]["must_be_alive"] + ranges[name]
        with ThreadPoolExecutor(max_workers=4) as ex:
            for mid, st in zip(todo, ex.map(lambda m: probe(token, m), todo)):
                res[mid] = st
        # اگر کپی ممنوعه‌ای زنده بود همین حالا حذف کن (بی‌صدا)
        deleted = []
        for mid in checks[name]["forbidden_copies"] + ranges[name]:
            if str(res.get(mid)).startswith(("alive", "locked-alive", "other")):
                d = call(token, "deleteMessage", {"message_id": mid})
                if d.get("ok"):
                    deleted.append(mid)
                    res[mid] = "deleted-now"
                else:
                    res[mid] += " | delete-failed:" + json.dumps(d, ensure_ascii=False)[:120]
        live = sorted([m for m, s in res.items() if str(s).startswith(("alive", "locked-alive"))])
        out["channels"][name]["probe"] = {str(k): v for k, v in res.items()}
        out["channels"][name]["deleted_now"] = deleted
        out["channels"][name]["live_now"] = live
        print(f"[{name}] probe done; deleted_now={deleted}; live={live}")

    os.makedirs(os.path.join(BASE_DIR, "docs", "tool-results"), exist_ok=True)
    with open(os.path.join(BASE_DIR, "docs", "tool-results", "g31_verify.json"), "w") as f:
        json.dump(out, f, ensure_ascii=False, indent=1)
    print("saved → docs/tool-results/g31_verify.json")

if __name__ == "__main__":
    main()
