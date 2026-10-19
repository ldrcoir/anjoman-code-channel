#!/bin/bash
# ─────────────────────────────────────────────────────────────
# بوت‌استرپ روزنگار v4 — پس از هر ریست سرور فقط همین اجرا شود
# کارها: همگام‌سازی مخزن با گیت‌هاب + روشن‌کردن دیمن چندکاناله
# (دیمن در شروع، اسلات‌های ازدست‌رفتهٔ همهٔ کانال‌ها را خودش جبران می‌کند)
# ─────────────────────────────────────────────────────────────
set -u
ROOT=/home/z/my-project
REPO=$ROOT/anjoman-code-bot
LOG=$ROOT/logs/bootstrap.log
TS() { date "+%Y-%m-%d %H:%M:%S %z"; }

mkdir -p "$ROOT/logs"
echo "[$(TS)] ── bootstrap start" >> "$LOG"

# ۱) بررسی توکن‌ها در .env (خارج از مخزن — هرگز commit نمی‌شود)
if [ ! -f "$ROOT/.env" ]; then
  echo "[$(TS)] ERROR: .env نیست" >> "$LOG"; exit 1
fi
grep -q '^BALE_TOKEN=.\+' "$ROOT/.env" || grep -q '^BALE_TOKEN_' "$ROOT/.env" \
  || { echo "[$(TS)] ERROR: هیچ توکن بله‌ای نیست" >> "$LOG"; exit 1; }
grep -q '^GITHUB_TOKEN=.\+' "$ROOT/.env" || echo "[$(TS)] WARN: GITHUB_TOKEN نیست — فقط انتشار محلی" >> "$LOG"

# ۲) همگام‌سازی مخزن با گیت‌هاب (منبع حقیقتِ واحد)
GH=$(grep '^GITHUB_TOKEN=' "$ROOT/.env" | cut -d= -f2)
if [ -n "$GH" ]; then
  if git -C "$REPO" fetch "https://ldrcoir:${GH}@github.com/ldrcoir/anjoman-code-channel.git" main 2>>"$LOG"; then
    # فقط اگر محلی جلوتر نیست hard-reset کن؛ وگرنه merge محلی را نگه دار
    AHEAD=$(git -C "$REPO" rev-list --count FETCH_HEAD..HEAD 2>/dev/null || echo 0)
    if [ "$AHEAD" -gt 0 ]; then
      git -C "$REPO" merge --ff-only FETCH_HEAD >>"$LOG" 2>&1 \
        || git -C "$REPO" push "https://ldrcoir:${GH}@github.com/ldrcoir/anjoman-code-channel.git" main >>"$LOG" 2>&1
      echo "[$(TS)] محلی $AHEAD کامیت جلوتر بود — حفظ و ارسال شد" >> "$LOG"
    else
      git -C "$REPO" reset --hard FETCH_HEAD >>"$LOG" 2>&1
      echo "[$(TS)] git sync OK (به ریموت تراز شد)" >> "$LOG"
    fi
    git -C "$REPO" remote set-url origin https://github.com/ldrcoir/anjoman-code-channel.git
  else
    echo "[$(TS)] WARN: fetch ناموفق — ادامه با نسخهٔ محلی" >> "$LOG"
  fi
fi

# ۳) دیمن روزنگار چندکاناله — اگر مرده، دوباره روشن کن (double-fork مستقل)
if [ -f "$ROOT/.roznegar.pid" ] && [ -d "/proc/$(cat "$ROOT/.roznegar.pid" 2>/dev/null)" ]; then
  echo "[$(TS)] دیمن زنده است (pid $(cat "$ROOT/.roznegar.pid"))" >> "$LOG"
else
  rm -f "$ROOT/.roznegar.pid"
  python3 "$REPO/scripts/roznegar_daemon.py" --detach >> "$LOG" 2>&1
  echo "[$(TS)] دیمن چندکاناله روشن شد" >> "$LOG"
fi

# ۴) فهرست کانال‌های فعال را در لاگ بنویس
for CH in "$REPO"/channels/*/channel.json; do
  [ -f "$CH" ] || continue
  SLUG=$(python3 -c "import json;print(json.load(open('$CH'))['slug'])" 2>/dev/null)
  echo "[$(TS)] کانال فعال: $SLUG" >> "$LOG"
done

echo "[$(TS)] ── bootstrap done" >> "$LOG"
