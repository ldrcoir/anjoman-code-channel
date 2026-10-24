#!/bin/bash
# ─────────────────────────────────────────────────────────────
# بیدار — نگهبان دیمون روزنگار (لایهٔ ۲)
# هر ۵ دقیقه بوت‌استرپِ ایدمپوتنت را صدا می‌زند؛ اگر دیمون
# مرده یا crash کرده باشد، bootstrap.sh خودش دوباره روشنش می‌کند.
# (لایهٔ ۱ = خود دیمون با جبران دیرهنگام؛ لایهٔ ۳ = GitHub Actions
#  لایهٔ ۴ = هوک .bashrc که این نگهبان را با اولین شل زنده می‌کند)
# ⚠️ خودمستقل‌سازی: بدون فراخوانِ --detached، خودش را با setsid از شل والد
# جدا می‌کند تا با بسته‌شدن شل/ابزار مُرد (حادثهٔ ۱۴۰۵-۰۸-۰۲ — nohup تنها کافی نبود)
# ─────────────────────────────────────────────────────────────
if [ "${1:-}" != "--detached" ]; then
  if command -v setsid >/dev/null 2>&1; then
    setsid bash "$0" --detached >/dev/null 2>&1 < /dev/null &
  else
    nohup bash "$0" --detached >/dev/null 2>&1 < /dev/null &
  fi
  exit 0
fi
ROOT=/home/z/my-project
LOG=$ROOT/logs/bidar.log
TS() { date "+%F %T%z"; }
echo "[$(TS)] بیدار روشن شد (هر ۵ دقیقه گارد — مستقل از شل)" >> "$LOG"
while true; do
  bash "$ROOT/anjoman-code-bot/scripts/bootstrap.sh" >> "$LOG" 2>&1
  sleep 300
done
