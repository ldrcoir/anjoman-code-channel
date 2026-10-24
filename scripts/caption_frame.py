# -*- coding: utf-8 -*-
"""قاب کپشن — دستور مالک ۱۴۰۵-۰۸-۰۲:
«مثل انجمن انرژی‌های نوین برای هشتگ‌ها و آی‌دی کانال یک قاب خوشگل درست کن»

این ماژول بلوک پایانی کپشن (لینک کانال + هشتگ‌ها) را داخل یک قاب دوخطیِ
مرتب بازسازی می‌کند. قاب با نویسه‌های خط‌کشی است تا در هر نمایشگری
(هم‌تراز‌شدن به راست/چپ مهم نیست) مثل یک جعبه دیده شود.

استفاده:
    from caption_frame import apply_caption_frame
    caption = apply_caption_frame(caption, cfg)   # cfg = channel.json کانال

قاعده‌ها:
- بدنهٔ کپشن (متن اصلی پست) دست‌نخورده می‌ماند.
- لینک همیشه از نام‌کاربری زندهٔ کانال (cfg["link"]) می‌آید — اگر کپشن
  لینک قدیمی داشته باشد، اصلاح می‌شود.
- هشتگ‌های موجود حفظ و تکراری‌ها حذف می‌شوند؛ ترتیب حفظ می‌شود.
- اگر کپشن اصلاً بلوک پایانی نداشت، قاب به ته آن اضافه می‌شود.
"""
import re

BAR = "═" * 30


def _extract_block(lines):
    """از ته کپشن، بلوکِ «لینک + هشتگ‌ها» را پیدا می‌کند.
    خروجی: (اندازهٔ بدنه، لینک یافت‌شده یا None، فهرست هشتگ‌ها)"""
    hashtags = []
    link = None
    cut = len(lines)
    i = len(lines) - 1
    seen_hashtag = False
    while i >= 0:
        s = lines[i].strip()
        if s.startswith("#"):
            hashtags.insert(0, s)
            seen_hashtag = True
            cut = i
        elif seen_hashtag and (s == "" or s.startswith(("🔗", "📢", "🌐", "📍", "💎"))
                               or re.search(r"ble\.ir/\S+", s)):
            m = re.search(r"ble\.ir/\S+", s)
            if m:
                link = m.group(0)
            cut = i
        else:
            break
        i -= 1
    # خط خالی بالای بلوک هم مال قاب است — بدنه بدون خط خالی انتهایی
    return cut, link, hashtags


def _dedup_keep_order(tags):
    seen, out = set(), []
    for t in " ".join(tags).split():
        if t not in seen:
            seen.add(t)
            out.append(t)
    return out


def apply_caption_frame(caption, cfg, extra_hashtags=None, hashtags_override=None):
    """کپشن را با قاب پایانی بازمی‌گرداند.
    extra_hashtags: اگر نبودند اضافه می‌شوند.
    hashtags_override: اگر داده شود، جایگزین کامل هشتگ‌های یافت‌شده است
    (برای اصلاح پست قبلی با هشتگ آیتم درست — گردش ۲۳)."""
    caption = caption.strip()
    lines = caption.splitlines()
    cut, link, hashtags = _extract_block(lines)
    body = "\n".join(lines[:cut]).strip()

    link = link or cfg.get("link") or (
        "ble.ir/" + (cfg.get("handle") or "").lstrip("@"))
    # لینک همیشه نسخهٔ زندهٔ کانال است
    link = re.sub(r"^.*ble\.ir/", "ble.ir/", link) if "ble.ir/" in link else (
        "ble.ir/" + (cfg.get("handle") or "").lstrip("@"))

    tags = _dedup_keep_order(hashtags) if hashtags_override is None else list(hashtags_override)
    for t in (extra_hashtags or []):
        if t not in tags:
            tags.append(t)

    frame_lines = ["╔" + BAR + "╗", "🔗 " + link]
    if tags:
        # هشتگ‌ها در یک یا دو خط — هر خط حداکثر ۴ هشتگ (خوانا در موبایل)
        for i in range(0, len(tags), 4):
            frame_lines.append(" ".join(tags[i:i + 4]))
    frame_lines.append("╚" + BAR + "╝")
    frame = "\n".join(frame_lines)

    out = (body + "\n\n" + frame).strip() if body else frame
    return out
