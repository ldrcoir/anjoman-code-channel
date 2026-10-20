#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""مولد PDF نشریه — HTML راست‌به‌چپ با فونت وزیرمتن و چاپ با Chromium (Playwright)
مصرف: python3 magazine_build.py <پوشهٔ شمارهٔ نشریه>
خروجی: magazine.pdf در همان پوشه (+ source.html برای بازتولید)
"""
import sys
import importlib.util
from pathlib import Path

FONT_DIR = "/home/z/my-project/assets/fonts"


def esc(s):
    return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")


CSS = f"""
@font-face {{ font-family:'Vazirmatn'; src:url('file://{FONT_DIR}/Vazirmatn-Regular.ttf'); font-weight:400; }}
@font-face {{ font-family:'Vazirmatn'; src:url('file://{FONT_DIR}/Vazirmatn-Medium.ttf'); font-weight:500; }}
@font-face {{ font-family:'Vazirmatn'; src:url('file://{FONT_DIR}/Vazirmatn-Bold.ttf'); font-weight:700; }}
@font-face {{ font-family:'Vazirmatn'; src:url('file://{FONT_DIR}/Vazirmatn-Black.ttf'); font-weight:900; }}
@page {{ size:A4; margin:0; }}
* {{ margin:0; padding:0; box-sizing:border-box; -webkit-print-color-adjust:exact; print-color-adjust:exact; }}
body {{ font-family:'Vazirmatn'; direction:rtl; color:#1c2340; font-size:13.5px; line-height:2; }}
.page {{ width:210mm; height:296mm; padding:16mm 15mm 14mm; page-break-after:always; position:relative; overflow:hidden; background:#fff; }}
.page:last-child {{ page-break-after:auto; }}
.brandbar {{ display:flex; justify-content:space-between; align-items:center; border-bottom:3px solid #22D3EE; padding-bottom:6px; margin-bottom:26px; }}
.brandbar .b {{ font-weight:900; font-size:15px; color:#0D1230; }}
.brandbar .n {{ font-weight:700; font-size:12px; color:#64748B; direction:ltr; }}
.mono {{ font-family:'DejaVu Sans Mono', monospace; direction:ltr; }}
h1.cover {{ font-weight:900; font-size:44px; line-height:1.5; color:#0D1230; margin-top:26mm; }}
.cover-sub {{ font-size:17px; color:#334155; font-weight:700; margin-top:4mm; }}
.cover-date {{ display:inline-block; background:#0D1230; color:#fff; border-radius:12px; padding:2px 18px; font-weight:700; font-size:15px; margin-top:6mm; }}
.toc {{ margin-top:14mm; border:1.5px solid #E2E8F0; border-radius:14px; padding:8mm 10mm; }}
.toc h3 {{ color:#22D3EE; font-size:15px; font-weight:900; margin-bottom:4mm; }}
.toc .row {{ display:flex; justify-content:space-between; border-bottom:1px dashed #E2E8F0; padding:2.2mm 0; font-weight:500; }}
.toc .row:last-child {{ border-bottom:none; }}
.toc .pg {{ color:#22D3EE; font-weight:900; direction:ltr; }}
.cover-foot {{ position:absolute; bottom:12mm; right:15mm; left:15mm; text-align:center; color:#64748B; font-size:12px; border-top:1px solid #E2E8F0; padding-top:4mm; }}
h2.sec {{ font-weight:900; font-size:23px; color:#0D1230; margin-bottom:5mm; }}
h2.sec span {{ color:#22D3EE; }}
p {{ text-align:justify; margin-bottom:4mm; }}
.news h3 {{ font-size:15.5px; font-weight:900; color:#0E7490; margin:4mm 0 1mm; }}
.news .item {{ border-right:3.5px solid #22D3EE; padding-right:5mm; margin-bottom:5.5mm; }}
.hl {{ background:#ECFEFF; border:1.5px solid #A5F3FC; border-radius:12px; padding:5mm 7mm; margin-top:6mm; font-weight:700; }}
.codebox {{ background:#0D1230; color:#E2E8F0; border-radius:12px; padding:5mm 7mm; direction:ltr; text-align:left; font-family:'DejaVu Sans Mono', monospace; font-size:12.5px; line-height:1.9; margin:3mm 0 4mm; }}
.codebox .fn {{ color:#C4B5FD; }} .codebox .str {{ color:#A6D6A7; }} .codebox .out {{ color:#FCD34D; }}
.codebox .lbl {{ color:#94A3B8; direction:rtl; text-align:right; font-family:'Vazirmatn'; font-size:11.5px; border-bottom:1px solid #334155; padding-bottom:2mm; margin-bottom:2.5mm; }}
table.g {{ width:100%; border-collapse:collapse; margin-top:3mm; }}
table.g td {{ border-bottom:1px solid #E2E8F0; padding:2.6mm 3mm; vertical-align:top; }}
table.g td.k {{ font-weight:900; color:#0E7490; width:34mm; direction:ltr; text-align:left; font-family:'DejaVu Sans Mono', monospace; }}
.two {{ display:flex; gap:8mm; }} .two > div {{ flex:1; }}
.card {{ border:1.5px solid #E2E8F0; border-radius:12px; padding:4mm 6mm; margin-bottom:4mm; }}
.card h4 {{ font-weight:900; font-size:14.5px; color:#0D1230; margin-bottom:1mm; }}
.card .c {{ direction:ltr; text-align:left; font-family:'DejaVu Sans Mono', monospace; font-weight:700; color:#0E7490; font-size:12.5px; }}
.card p {{ margin:0; font-size:12.8px; }}
.bigfoot {{ position:absolute; bottom:11mm; right:15mm; left:15mm; background:#0D1230; color:#fff; border-radius:14px; padding:4.5mm 8mm; display:flex; justify-content:space-between; align-items:center; font-size:12.5px; font-weight:700; }}
.bigfoot .l {{ direction:ltr; color:#22D3EE; font-family:'DejaVu Sans Mono', monospace; }}
.cal .row {{ display:flex; gap:5mm; margin-bottom:4.5mm; }}
.cal .when {{ min-width:42mm; background:#ECFEFF; border:1.5px solid #A5F3FC; border-radius:10px; text-align:center; font-weight:900; color:#0E7490; padding:2.5mm 2mm; font-size:12.5px; }}
.cal .what {{ flex:1; border-bottom:1px dashed #E2E8F0; padding:2.5mm 0; }}
"""


def build_html(i):
    P = []
    P.append("<html><head><meta charset='utf-8'><style>" + CSS + "</style></head><body>")

    # ── صفحهٔ ۱: جلد
    toc = "".join(
        f"<div class='row'><span>{esc(t)}</span><span class='pg'>{esc(pg)}</span></div>"
        for t, pg in i["toc"])
    P.append(f"""
<div class='page'>
  <div class='brandbar'><div class='b'>{esc(i['subtitle'])}</div><div class='n mono'>{esc(i['handle'])}</div></div>
  <h1 class='cover'>{esc(i['title'])}</h1>
  <div class='cover-sub'>خبر، معرفی زبان و نرم‌افزار، ترفند و تقویم — به انتخاب اعضای انجمن</div>
  <div class='cover-date'>{esc(i['issue_no'])} · {esc(i['date'])}</div>
  <div class='toc'><h3>فهرست این شماره</h3>{toc}</div>
  <div class='cover-foot'>نسخهٔ کامل در قالب PDF — برای مطالعه، دانلود و چاپ · {esc(i['channel'])}: {esc(i['link'])}</div>
</div>""")

    # ── صفحهٔ ۲: سرمقاله + خبرها
    ed = i["editorial"]
    ed_paras = "".join(f"<p>{esc(x)}</p>" for x in ed["paras"][:2])
    news_items = "".join(
        f"<div class='item'><h3>{esc(n['h'])}</h3><p>{esc(n['p'])}</p></div>"
        for n in i["news"]["items"])
    P.append(f"""
<div class='page'>
  <div class='brandbar'><div class='b'>{esc(i['title'])}</div><div class='n mono'>{esc(i['issue_no'])}</div></div>
  <h2 class='sec'>◈ {esc(ed['title'])}</h2>
  {ed_paras}
  <h2 class='sec' style='margin-top:6mm'>◈ {esc(i['news']['title'])}</h2>
  {news_items}
</div>""")

    # ── صفحات اخبار اضافه (هوش مصنوعی / فناوری) — از شمارهٔ ۲ بخش دائمی شدند
    for key in ("ai_news", "it_news"):
        N = i.get(key)
        if not N:
            continue
        items = "".join(
            f"<div class='item'><h3>{esc(n['h'])}</h3><p>{esc(n['p'])}</p></div>"
            for n in N["items"])
        note = f"<div class='hl'>{esc(N['note'])}</div>" if N.get("note") else ""
        P.append(f"""
<div class='page'>
  <div class='brandbar'><div class='b'>{esc(i['title'])}</div><div class='n mono'>{esc(i['issue_no'])}</div></div>
  <h2 class='sec'>◈ {esc(N['title'])}</h2>
  {items}
  {note}
</div>""")

    # ── صفحهٔ ۳: معرفی زبان
    L = i["language"]
    L_paras = "".join(f"<p>{esc(x)}</p>" for x in L["paras"])
    code = "<br>".join(esc(x) for x in L["code"])
    out = " | ".join(esc(x) for x in L["out"])
    gloss = "".join(f"<tr><td class='k'>{esc(k)}</td><td>{esc(v)}</td></tr>"
                    for k, v in L["glossary"])
    P.append(f"""
<div class='page'>
  <div class='brandbar'><div class='b'>{esc(i['title'])}</div><div class='n mono'>{esc(i['issue_no'])}</div></div>
  <h2 class='sec'>◈ {esc(L['title'])}</h2>
  {L_paras}
  <div class='codebox'><div class='lbl'>{esc(L['code_name'])}</div>{code}<br><br>
  <span class='lbl' style='display:block'>خروجی:</span><span class='out'>{out}</span></div>
  <table class='g'>{gloss}</table>
  <div class='hl'>🦀 {esc(L['exercise'])}</div>
</div>""")

    # ── صفحهٔ ۴: نرم‌افزار + ترفند
    S, T = i["software"], i["tip"]
    S_paras = "".join(f"<p>{esc(x)}</p>" for x in S["paras"])
    scs = "".join(f"<tr><td class='k'>{esc(k)}</td><td>{esc(v)}</td></tr>"
                  for k, v in S["shortcuts"])
    T_paras = "".join(f"<p>{esc(x)}</p>" for x in T["paras"])
    cmds = "".join(f"<div class='card'><div class='c'>{esc(c)}</div><p>{esc(d)}</p></div>"
                   for c, d in T["commands"])
    P.append(f"""
<div class='page'>
  <div class='brandbar'><div class='b'>{esc(i['title'])}</div><div class='n mono'>{esc(i['issue_no'])}</div></div>
  <h2 class='sec'>◈ {esc(S['title'])}</h2>
  {S_paras}
  <table class='g'>{scs}</table>
  <h2 class='sec' style='margin-top:7mm'>◈ {esc(T['title'])}</h2>
  {T_paras}
  {cmds}
  <p style='margin-top:2mm'>{esc(T['closing'])}</p>
</div>""")

    # ── صفحهٔ ۵: تقویم
    C = i["calendar"]
    rows = "".join(f"<div class='row'><div class='when'>{esc(w)}</div><div class='what'>{esc(x)}</div></div>"
                   for w, x in C["items"])
    P.append(f"""
<div class='page'>
  <div class='brandbar'><div class='b'>{esc(i['title'])}</div><div class='n mono'>{esc(i['issue_no'])}</div></div>
  <h2 class='sec'>◈ {esc(C['title'])}</h2>
  <div class='cal'>{rows}</div>
  <div class='hl'>📮 {esc(C['invite'])}</div>
  <div class='bigfoot'><div>{esc(i['channel'])}</div><div class='l'>{esc(i['link'])}</div></div>
</div>""")

    P.append("</body></html>")
    return "".join(P)


def main():
    folder = Path(sys.argv[1]).resolve()
    spec = importlib.util.spec_from_file_location("issue", folder / "issue.py")
    mod = importlib.util.module_from_spec(spec)
    spec.loader.exec_module(mod)
    html = build_html(mod.ISSUE)
    html_path = folder / "source.html"
    html_path.write_text(html, encoding="utf-8")

    pdf_path = folder / "magazine.pdf"
    from playwright.sync_api import sync_playwright
    with sync_playwright() as p:
        b = p.chromium.launch()
        pg = b.new_page()
        pg.goto(html_path.as_uri())
        pg.wait_for_timeout(700)
        pg.pdf(path=str(pdf_path), format="A4", print_background=True,
               margin={"top": "0", "bottom": "0", "left": "0", "right": "0"})
        b.close()
    print("PDF:", pdf_path, pdf_path.stat().st_size, "bytes")


if __name__ == "__main__":
    main()
