#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""گردش ۶۰ — دانلود آرتیفکت APK نسخهٔ ۱.۲ کتابچهٔ پژوهش (بیلد Actions)
هیچ توکنی چاپ نمی‌شود."""
import json, sys, time, zipfile, io, urllib.request, urllib.error
from pathlib import Path

ROOT = Path("/home/z/my-project")
TOKEN = [l.split("=", 1)[1].strip() for l in (ROOT / "scripts/actroot/anjoman-code-bot/.env").read_text().splitlines() if l.startswith("GITHUB_TOKEN=")][0]
REPO = "ldrcoir/anjoman-code-channel"
HDR = {"Authorization": f"Bearer {TOKEN}", "Accept": "application/vnd.github+json"}
WANT_RUN = sys.argv[1] if len(sys.argv) > 1 else None   # شناسهٔ ران، اگر داده شود

class NoRedirect(urllib.request.HTTPRedirectHandler):
    def redirect_request(self, req, fp, code, msg, headers, newurl):
        return None

op = urllib.request.build_opener(NoRedirect)

def gh_json(url):
    req = urllib.request.Request(url, headers=HDR)
    return json.load(urllib.request.urlopen(req, timeout=60))

# یافتن رانِ بیلد پژوهش که با پوش تازه تحریک شده
run = None
for i in range(30):
    runs = gh_json(f"https://api.github.com/repos/{REPO}/actions/workflows/build-pajoohesh-apk.yml/runs?per_page=5")
    for r in runs["workflow_runs"]:
        if WANT_RUN and str(r["id"]) != str(WANT_RUN):
            continue
        if r["head_sha"].startswith("f67169d") or WANT_RUN:
            run = r
            break
    if run:
        break
    time.sleep(10)

if not run:
    raise SystemExit("ران بیلد پیدا نشد")
print("run:", run["id"], "|", run["status"], "|", run["conclusion"], "| sha:", run["head_sha"][:7])

# صبر تا پایان بیلد
for i in range(90):
    run = gh_json(f"https://api.github.com/repos/{REPO}/actions/runs/{run['id']}")
    if run["status"] == "completed":
        break
    time.sleep(10)
print("پایان:", run["status"], run["conclusion"])
if run["conclusion"] != "success":
    raise SystemExit(f"بیلد ناموفق: {run['conclusion']} — run {run['id']}")

# دانلود آرتیفکت
arts = gh_json(f"https://api.github.com/repos/{REPO}/actions/runs/{run['id']}/artifacts")
a = next(x for x in arts["artifacts"] if x["name"] == "dastavardha-pajoohesh-apk")
print("artifact:", a["name"], "| size:", a["size_in_bytes"] // 1024, "KB")

req = urllib.request.Request(a["archive_download_url"], headers=HDR)
try:
    op.open(req, timeout=60)
    raise SystemExit("انتظار ۳۰۲ می‌رفت")
except urllib.error.HTTPError as e:
    if e.code not in (302, 301, 307):
        raise
    loc = e.headers["Location"]
data = urllib.request.urlopen(loc, timeout=300).read()
z = zipfile.ZipFile(io.BytesIO(data))
names = z.namelist()
print("zip members:", names)
apk_bytes = z.read(names[0])

out = ROOT / "download/DastavardhayePajoohesh-v1.2.apk"
out.write_bytes(apk_bytes)
print("saved:", out, "|", len(apk_bytes) // 1024, "KB")
