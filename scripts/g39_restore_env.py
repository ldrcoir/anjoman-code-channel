#!/usr/bin/env python3
# گردش ۳۹ - بازیابی توکن‌ها از anjoman-code-bot/.env و پروب اولیهٔ کانال انرژی
import json, os, time, urllib.request, urllib.parse

ROOT = "/home/z/my-project"
BOTENV = os.path.join(ROOT, "anjoman-code-bot", ".env")

toks = {}
with open(BOTENV) as f:
    for line in f:
        line = line.strip()
        if "=" in line and not line.startswith("#"):
            k, v = line.split("=", 1)
            toks[k] = v

BALE = toks.get("BALE_TOKEN", "")
ENJ = toks.get("ENERJY_TOKEN", "")
GITHUB = toks.get("GITHUB_TOKEN", "")
print("keys in bot .env:", sorted(toks.keys()), "| lens:", len(BALE), len(ENJ), len(GITHUB))

# بازسازی .env اصلی (بدون چاپ مقدار)
main_env = os.path.join(ROOT, ".env")
lines = []
with open(main_env) as f:
    for line in f:
        if line.strip() and not line.startswith(("BALE_TOKEN", "ENERJY_TOKEN", "GITHUB_TOKEN")):
            lines.append(line.rstrip("\n"))
lines.append("BALE_TOKEN=" + BALE)
lines.append("ENERJY_TOKEN=" + ENJ)
lines.append("GITHUB_TOKEN=" + GITHUB)
with open(main_env, "w") as f:
    f.write("\n".join(lines) + "\n")
os.chmod(main_env, 0o600)
print("main .env restored:", [l.split("=")[0] for l in lines])

def api(tok, meth, **p):
    url = f"https://tapi.bale.ai/bot{tok}/{meth}"
    data = urllib.parse.urlencode(p).encode()
    req = urllib.request.Request(url, data=data)
    try:
        with urllib.request.urlopen(req, timeout=25) as r:
            return json.loads(r.read().decode())
    except urllib.error.HTTPError as e:
        try:
            return json.loads(e.read().decode())
        except Exception:
            return {"ok": False, "error_code": e.code, "description": "http%d" % e.code}
    except Exception as e:
        return {"ok": False, "error_code": -1, "description": str(e)[:80]}

for name, t in [("BALE(coding)", BALE), ("ENERJY", ENJ)]:
    me = api(t, "getMe")
    if me.get("ok"):
        u = me["result"]
        print(f"getMe {name}: OK @{u.get('username')} id={u.get('id')}")
    else:
        print(f"getMe {name}: FAIL {me.get('error_code')} {str(me.get('description'))[:60]}")

EN_CHAT = 4602076598
known = [3, 4, 8, 9, 10, 23, 24, 25, 26, 28]
alive, dead, foreign = [], [], []
for mid in known:
    r = api(ENJ, "editMessageReplyMarkup", chat_id=EN_CHAT, message_id=mid)
    if r.get("ok"):
        alive.append(mid)
    else:
        d = str(r.get("description", ""))
        if "not found" in d.lower():
            dead.append(mid)
        else:
            foreign.append((mid, r.get("error_code"), d[:50]))
print("ALIVE(own):", alive)
print("DEAD:", dead)
print("FOREIGN/LOCKED:", foreign)

# اسکن بالای کانال برای پیام تازه (۲۹ تا ۴۰)
newbies = []
for mid in range(29, 41):
    r = api(ENJ, "editMessageReplyMarkup", chat_id=EN_CHAT, message_id=mid)
    if r.get("ok"):
        newbies.append(mid)
print("NEW above 28:", newbies)
