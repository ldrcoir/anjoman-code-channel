#!/bin/bash
# ─────────────────────────────────────────────────────────────
# بوت‌استرپ روزنگار — پس از هر ریست سرور این اسکریپت اجرا می‌شود
# کارها: همگام‌سازی مخزن با گیت‌هاب + روشن‌کردن دیمن زمان‌بند
# (دیمن در شروع، اسلات‌های ازدست‌رفتهٔ امروز را خودش جبران می‌کند)
# ─────────────────────────────────────────────────────────────
set -u
ROOT=/home/z/my-project
REPO=$ROOT/anjoman-code-bot
LOG=$ROOT/logs/bootstrap.log
TS() { date "+%Y-%m-%d %H:%M:%S %z"; }

mkdir -p "$ROOT/logs"
echo "[$(TS)] ── bootstrap start" >> "$LOG"

# ۱) بررسی توکن‌ها در .env
if [ ! -f "$ROOT/.env" ]; then
  echo "[$(TS)] ERROR: .env نیست" >> "$LOG"; exit 1
fi
grep -q '^BALE_TOKEN=.\+' "$ROOT/.env" || { echo "[$(TS)] ERROR: BALE_TOKEN نیست" >> "$LOG"; exit 1; }
grep -q '^GITHUB_TOKEN=.\+' "$ROOT/.env" || echo "[$(TS)] WARN: GITHUB_TOKEN نیست — فقط انتشار محلی" >> "$LOG"

# ۲) همگام‌سازی مخزن با گیت‌هاب (منبع حقیقتِ واحد)
GH=$(grep '^GITHUB_TOKEN=' "$ROOT/.env" | cut -d= -f2)
if [ -n "$GH" ]; then
  if git -C "$REPO" fetch "https://ldrcoir:${GH}@github.com/ldrcoir/anjoman-code-channel.git" main 2>>"$LOG"; then
    git -C "$REPO" reset --hard FETCH_HEAD >>"$LOG" 2>&1
    git -C "$REPO" remote set-url origin https://github.com/ldrcoir/anjoman-code-channel.git
    echo "[$(TS)] git sync OK" >> "$LOG"
  else
    echo "[$(TS)] WARN: fetch ناموفق — ادامه با نسخهٔ محلی" >> "$LOG"
  fi
fi

# ۳) دیمن روزنگار — اگر مرده، دوباره روشن کن (double-fork مستقل)
if [ -f "$ROOT/.roznegar.pid" ] && [ -d "/proc/$(cat "$ROOT/.roznegar.pid" 2>/dev/null)" ]; then
  echo "[$(TS)] دیمن زنده است (pid $(cat "$ROOT/.roznegar.pid"))" >> "$LOG"
else
  rm -f "$ROOT/.roznegar.pid"
  python3 "$REPO/scripts/roznegar_daemon.py" --detach >> "$LOG" 2>&1
  echo "[$(TS)] دیمن روشن شد" >> "$LOG"
fi

echo "[$(TS)] ── bootstrap done" >> "$LOG"
