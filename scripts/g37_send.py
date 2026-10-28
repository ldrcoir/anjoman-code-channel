#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""گردش ۳۷ — اصلاح نشریهٔ کانال کدنویسی (دستور صریح مالک، همین امروز):
«تو باید نسخه ۱ می‌ساخته... الان نوشتی ۲ و شماره ۱ مهر نیست...
حداقل این نشریه آخری که pdf. دادی اصلاح کن؛ الان pdf نشریه مهر نیست اصلاح کن»

اقدام: پیام‌های ۲۰۲ (پوستر شمارهٔ ۲ آبان) و ۲۰۴ (PDF شمارهٔ ۲ آبان) حذف و
نسخهٔ اصلاح‌شدهٔ «شمارهٔ ۱ — مهر ۱۴۰۵» (پوستر + PDF بلافاصله بعد از آن) منتشر می‌شود.
دلیل تعویض به‌جای ویرایش: بله editMessageMedia را ندارد (۵۰۱ — گردش ۳۶ ثابت شد)؛
پس تعویض فایل/عکس درجا با بات ممکن نیست.
قواعد: بدون forwardMessage؛ گارد دوبارسردن؛ 429 → ۳۵ ثانیه؛ پروب بی‌اثر پس از ارسال."""
import json, sys, time
from pathlib import Path
import requests

REPO = Path("/home/z/my-project/anjoman-code-bot")
sys.path.insert(0, str(REPO / "scripts"))
from caption_frame import apply_caption_frame

env = {}
for line in (REPO / ".env").read_text().splitlines():
    if "=" in line and not line.strip().startswith("#"):
        k, v = line.split("=", 1); env[k.strip()] = v.strip()

TOK = env["BALE_TOKEN"].strip()
CID = 5100057643
CFG = json.loads((REPO / "channels/coding/channel.json").read_text(encoding="utf-8"))
FOLDER = REPO / "channels/coding/posts/b2-nashriyeh/030-shomare-1-mehr-1405"

OLD_IDS = [202, 204]   # پوستر شمارهٔ ۲ آبان + PDF شمارهٔ ۲ آبان

CAP_POSTER = (FOLDER / "caption.txt").read_text(encoding="utf-8")
CAP_PDF = (FOLDER / "doc-caption.txt").read_text(encoding="utf-8")


def call_json(method, payload):
    r = requests.post(f"https://tapi.bale.ai/bot{TOK}/{method}", json=payload, timeout=25)
    try:
        return r.json()
    except Exception:
        return {"ok": False, "err": f"http {r.status_code}", "body": r.text[:200]}


def drain_feed():
    offset, ups = 0, []
    for _ in range(30):
        res = call_json("getUpdates", {"offset": offset, "limit": 100, "timeout": 0})
        if not res.get("ok"):
            break
        batch = res.get("result", [])
        if not batch:
            break
        ups.extend(batch)
        offset = max(u["update_id"] for u in batch) + 1
        if len(batch) < 100:
            break
    return ups


def feed_channel_posts():
    out = []
    for u in drain_feed():
        m = u.get("channel_post") or u.get("message")
        if m:
            out.append(m)
    return out


def probe(mid):
    r = call_json("editMessageReplyMarkup", {"chat_id": CID, "message_id": mid})
    if r.get("ok"):
        return "alive"
    s = json.dumps(r, ensure_ascii=False)
    if "not found" in s.lower():
        return "gone"
    return "other:" + s[:100]


def send_media(kind, path, caption):
    field = "photo" if kind == "photo" else "document"
    method = "sendPhoto" if kind == "photo" else "sendDocument"
    with open(path, "rb") as f:
        r = requests.post(f"https://tapi.bale.ai/bot{TOK}/{method}",
                          data={"chat_id": str(CID), "caption": caption},
                          files={field: (Path(path).name, f)},
                          timeout=180)
    try:
        data = r.json()
    except Exception:
        data = {"ok": False, "err": f"http {r.status_code}", "body": r.text[:300]}
    if data.get("ok"):
        return data
    s = json.dumps(data, ensure_ascii=False)
    if "429" in s or "Too Many" in s:
        print("  ⏳ 429 — ۳۵ ثانیه صبر…")
        time.sleep(35)
        return send_media(kind, path, caption)
    return data


def main():
    log = {"deleted": {}, "sent": {}, "dupes": []}

    # ── ۱) حذف نسخهٔ غلط (شمارهٔ ۲ — آبان)
    for mid in OLD_IDS:
        st = probe(mid)
        if st == "alive":
            d = call_json("deleteMessage", {"chat_id": CID, "message_id": mid})
            log["deleted"][mid] = "deleted" if d.get("ok") else json.dumps(d, ensure_ascii=False)[:150]
            print(f"🗑 پیام {mid} (نشریهٔ ۲ آبان) حذف شد:", d.get("ok"))
            time.sleep(3)
        else:
            log["deleted"][mid] = f"skip ({st})"
            print(f"پیام {mid} زنده نبود ({st}) — نیازی به حذف نیست")

    # ── ۲) پوستر اصلاح‌شده
    cap1 = apply_caption_frame(CAP_POSTER, CFG)
    assert cap1.count("#") >= 3 and len(cap1) <= 1024, "کپشن پوستر نامعتبر"
    before = sorted(m.get("message_id") for m in feed_channel_posts() if m.get("message_id"))
    print("فید پیش از ارسال:", before[-6:])
    r1 = send_media("photo", FOLDER / "post.png", cap1)
    if not r1.get("ok"):
        print("❌ ارسال پوستر ناموفق:", json.dumps(r1, ensure_ascii=False)[:400])
        log["sent"]["poster"] = {"ok": False, "err": r1}
    else:
        mid1 = r1["result"]["message_id"]
        print("✅ پوستر شمارهٔ ۱ مهر → message_id =", mid1)
        log["sent"]["poster"] = {"ok": True, "message_id": mid1, "caption_chars": len(cap1)}
        (FOLDER / "caption.txt").write_text(cap1, encoding="utf-8")

        # ── ۳) PDF بلافاصله بعد از پوستر («نزدیک خود پوسترش»)
        time.sleep(4)
        cap2 = apply_caption_frame(CAP_PDF, CFG)
        assert cap2.count("#") >= 3 and len(cap2) <= 1024, "کپشن PDF نامعتبر"
        r2 = send_media("doc", FOLDER / "magazine.pdf", cap2)
        if not r2.get("ok"):
            print("❌ ارسال PDF ناموفق:", json.dumps(r2, ensure_ascii=False)[:400])
            log["sent"]["pdf"] = {"ok": False, "err": r2}
        else:
            mid2 = r2["result"]["message_id"]
            print("✅ PDF شمارهٔ ۱ مهر → message_id =", mid2)
            log["sent"]["pdf"] = {"ok": True, "message_id": mid2, "caption_chars": len(cap2)}
            (FOLDER / "doc-caption.txt").write_text(cap2, encoding="utf-8")

        # ── ۴) گارد دوبارسردن
        for wait in (9, 19):
            time.sleep(wait)
            for m in feed_channel_posts():
                mid2x = m.get("message_id")
                capx = (m.get("caption") or m.get("text") or "")
                if mid2x and mid2x not in (mid1, log["sent"].get("pdf", {}).get("message_id")) \
                        and capx[:40] in (cap1[:40], cap2[:40]):
                    d = call_json("deleteMessage", {"chat_id": CID, "message_id": mid2x})
                    print(f"  🧹 کپی دوبارسردن {mid2x} → حذف:", d.get("ok"))
                    log["dupes"].append(mid2x)

        # ── ۵) پروب بی‌اثر
        time.sleep(3)
        for label, mid in (("poster", mid1), ("pdf", log["sent"].get("pdf", {}).get("message_id"))):
            if mid:
                st = probe(mid)
                log["sent"][label]["probe"] = st
                print(f"پروب {label} ({mid}):", st)

    out = Path("/home/z/my-project/tool-results/g37_send.json")
    out.write_text(json.dumps(log, ensure_ascii=False, indent=1), encoding="utf-8")
    print("saved:", out)
    print(json.dumps(log, ensure_ascii=False, indent=1)[:1200])


if __name__ == "__main__":
    main()
