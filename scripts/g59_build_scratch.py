#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""گردش ۵۹ — ساخت پروژهٔ اسکرچ ۳ «انرژی‌های نوین» (.sb3) — نسخهٔ تمیز
صحنه: خورشیدِ چرخان، دو توربین بادی، ابرها، رودخانه + سد در لبهٔ راست، موج‌های روان، جوانهٔ زیست‌توده، عنوان فارسی
خروجی: download/anim-scratch-enerjy/enerjy-novin-animation.sb3 (+ assets برای رندر ویدیو)
"""
import json, hashlib, zipfile
from pathlib import Path

OUT = Path("/home/z/my-project/download/anim-scratch-enerjy")
ASSETS = OUT / "_assets"
ASSETS.mkdir(parents=True, exist_ok=True)

# ══════════════ ۱) اس‌وی‌جی لباس‌ها و صحنه ══════════════
def w(name, s):
    (ASSETS / name).write_text(s, encoding="utf-8")

w("backdrop.svg", """<svg viewBox="0 0 480 360" width="480" height="360" xmlns="http://www.w3.org/2000/svg">
<defs>
  <linearGradient id="sky" x1="0" y1="0" x2="0" y2="1">
    <stop offset="0" stop-color="#BFE9FB"/><stop offset="1" stop-color="#EAF9EF"/>
  </linearGradient>
  <linearGradient id="water" x1="0" y1="0" x2="0" y2="1">
    <stop offset="0" stop-color="#57BBEA"/><stop offset="1" stop-color="#2E8FC7"/>
  </linearGradient>
</defs>
<rect x="0" y="0" width="480" height="360" fill="url(#sky)"/>
<ellipse cx="70" cy="245" rx="150" ry="45" fill="#A5DB8F"/>
<ellipse cx="410" cy="252" rx="170" ry="50" fill="#93D17E"/>
<rect x="0" y="240" width="480" height="120" fill="#7BC96A"/>
<rect x="0" y="352" width="480" height="8" fill="#5FB551"/>
<g fill="#FDE68A"><circle cx="30" cy="285" r="3"/><circle cx="215" cy="270" r="3"/><circle cx="300" cy="288" r="3"/><circle cx="452" cy="278" r="3"/></g>
<g fill="#F87171"><circle cx="120" cy="295" r="3"/><circle cx="330" cy="268" r="3"/></g>
<rect x="0" y="298" width="480" height="62" fill="url(#water)"/>
<!-- سدّ در لبهٔ راست -->
<rect x="438" y="246" width="42" height="114" rx="5" fill="#94A3B8"/>
<rect x="438" y="246" width="42" height="10" rx="5" fill="#64748B"/>
<rect x="448" y="288" width="22" height="28" rx="4" fill="#475569"/>
<!-- پرتاب آب از دریچه به سمت رودخانه -->
<path d="M 448 306 q -16 6 -22 22 l -8 -4 q 8 -18 26 -24 z" fill="#8FD8F5" opacity=".92"/>
</svg>""")

w("sun.svg", """<svg viewBox="0 0 120 120" width="120" height="120" xmlns="http://www.w3.org/2000/svg">
<g stroke="#F59E0B" stroke-width="7" stroke-linecap="round">
  <line x1="60" y1="8" x2="60" y2="26"/><line x1="60" y1="94" x2="60" y2="112"/>
  <line x1="8" y1="60" x2="26" y2="60"/><line x1="94" y1="60" x2="112" y2="60"/>
  <line x1="23" y1="23" x2="36" y2="36"/><line x1="84" y1="84" x2="97" y2="97"/>
  <line x1="97" y1="23" x2="84" y2="36"/><line x1="36" y1="84" x2="23" y2="97"/>
</g>
<circle cx="60" cy="60" r="30" fill="#FCD34D"/>
<circle cx="60" cy="60" r="30" fill="none" stroke="#FBBF24" stroke-width="4"/>
<circle cx="50" cy="54" r="4.5" fill="#92400E"/><circle cx="70" cy="54" r="4.5" fill="#92400E"/>
<path d="M 48 66 q 12 10 24 0" stroke="#92400E" stroke-width="3.5" fill="none" stroke-linecap="round"/>
</svg>""")

CLOUD = """<svg viewBox="0 0 180 70" width="180" height="70" xmlns="http://www.w3.org/2000/svg">
<g fill="#FFFFFF" opacity="0.95">
  <ellipse cx="55" cy="45" rx="45" ry="20"/>
  <ellipse cx="95" cy="35" rx="50" ry="24"/>
  <ellipse cx="135" cy="47" rx="38" ry="17"/>
</g>
<g fill="#E3F2FD" opacity="0.9"><ellipse cx="70" cy="52" rx="40" ry="12"/></g>
</svg>"""
w("cloud1.svg", CLOUD)
w("cloud2.svg", CLOUD)

w("tower.svg", """<svg viewBox="0 0 90 160" width="90" height="160" xmlns="http://www.w3.org/2000/svg">
<rect x="41" y="18" width="8" height="140" rx="3" fill="#E2E8F0"/>
<rect x="41" y="18" width="4" height="140" fill="#CBD5E1"/>
<rect x="33" y="150" width="24" height="9" rx="4" fill="#CBD5E1"/>
<!-- ناسل: محفظهٔ موتور بالای برج که پروانه به آن وصل می‌شود -->
<rect x="31" y="7" width="30" height="18" rx="7" fill="#F8FAFC" stroke="#94A3B8" stroke-width="2"/>
<rect x="31" y="7" width="30" height="8" rx="5" fill="#E2E8F0"/>
<!-- توپیِ پشتِ پروانه — حلقهٔ اتصال پروانه به ناسل -->
<circle cx="45" cy="16" r="7" fill="#64748B"/>
<circle cx="45" cy="16" r="3.2" fill="#334155"/>
</svg>""")

w("rotor.svg", """<svg viewBox="0 0 120 120" width="120" height="120" xmlns="http://www.w3.org/2000/svg">
<!-- سه پره که همه از مرکز (توپی) می‌روند بیرون -->
<g fill="#F1F5F9" stroke="#94A3B8" stroke-width="2">
  <path d="M 60 60 L 56 12 Q 60 4 64 12 Z"/>
  <path d="M 60 60 L 101.6 84 Q 107.6 91.2 98.6 93.6 Z"/>
  <path d="M 60 60 L 18.4 84 Q 12.4 91.2 21.4 93.6 Z"/>
</g>
<!-- توپیِ جلو: حلقهٔ فلزی + دیسک روشن + پیچ مرکز — پره‌ها به همین وصل‌اند -->
<circle cx="60" cy="60" r="10" fill="#F8FAFC" stroke="#64748B" stroke-width="2.5"/>
<circle cx="60" cy="60" r="6.5" fill="#E2E8F0" stroke="#94A3B8" stroke-width="1.5"/>
<circle cx="60" cy="60" r="2.6" fill="#475569"/>
</svg>""")

w("wave.svg", """<svg viewBox="0 0 190 30" width="190" height="30" xmlns="http://www.w3.org/2000/svg">
<path d="M 4 18 q 22 -16 46 0 t 46 0 t 46 0 t 46 0" fill="none" stroke="#BFEAFB" stroke-width="6" stroke-linecap="round"/>
</svg>""")

w("javaneh1.svg", """<svg viewBox="0 0 80 100" width="80" height="100" xmlns="http://www.w3.org/2000/svg">
<ellipse cx="40" cy="84" rx="16" ry="12" fill="#8D6E4C"/>
<ellipse cx="35" cy="80" rx="6" ry="4" fill="#A9825E"/>
</svg>""")
w("javaneh2.svg", """<svg viewBox="0 0 80 100" width="80" height="100" xmlns="http://www.w3.org/2000/svg">
<path d="M 40 88 q -1 -16 0 -26" stroke="#3E8E41" stroke-width="5" fill="none" stroke-linecap="round"/>
<path d="M 40 70 q -12 -3 -14 -16 q 14 -1 14 16" fill="#4ADE80"/>
<path d="M 40 66 q 11 -4 13 -17 q -15 0 -13 17" fill="#22C55E"/>
</svg>""")
w("javaneh3.svg", """<svg viewBox="0 0 80 100" width="80" height="100" xmlns="http://www.w3.org/2000/svg">
<path d="M 40 92 q -2 -30 0 -46" stroke="#3E8E41" stroke-width="6" fill="none" stroke-linecap="round"/>
<path d="M 40 74 q -14 -3 -17 -18 q 16 -1 17 18" fill="#4ADE80"/>
<path d="M 40 68 q 13 -4 16 -19 q -17 0 -16 19" fill="#22C55E"/>
<path d="M 40 50 q -10 -3 -12 -14 q 12 -1 12 14" fill="#4ADE80"/>
<circle cx="40" cy="34" r="13" fill="#FDE047" stroke="#EAB308" stroke-width="3"/>
<circle cx="40" cy="34" r="6" fill="#92400E"/>
</svg>""")

# ══════════════ ۲) عنوان فارسی PNG ══════════════
from PIL import Image, ImageDraw, ImageFont
img = Image.new("RGBA", (660, 150), (0, 0, 0, 0))
d = ImageDraw.Draw(img)
f = ImageFont.truetype("/home/z/my-project/assets/fonts/Vazirmatn-Black.ttf", 86)
d.text((360, 75), "انرژی‌های نوین", font=f, fill=(14, 92, 51, 255), anchor="mm",
       direction="rtl", language="fa", stroke_width=8, stroke_fill=(255, 255, 255, 230))
lx, ly = 38, 34
d.polygon([(lx+18, ly), (lx+2, ly+46), (lx+20, ly+46), (lx+10, ly+92),
           (lx+46, ly+34), (lx+26, ly+34), (lx+38, ly)],
          fill=(251, 191, 36, 255), outline=(180, 130, 10, 255))
img.save(ASSETS / "title.png")

# ══════════════ ۳) سازندهٔ بلوک ══════════════
class Blocks:
    def __init__(self):
        self.d = {}; self.i = 0
    def add(self, opcode, inputs=None, fields=None, parent=None, top=False, x=0, y=0):
        self.i += 1
        bid = f"b{self.i:04d}"
        self.d[bid] = {"opcode": opcode, "next": None, "parent": parent,
                       "inputs": inputs or {}, "fields": fields or {},
                       "shadow": False, "topLevel": bool(top)}
        if top: self.d[bid]["x"] = x; self.d[bid]["y"] = y
        if parent: self.d[parent]["next"] = bid
        return bid
    def chain(self, stmts, parent=None):
        first = None; last = parent
        for op, inp, fld in stmts:
            last = self.add(op, inp, fld, parent=last)
            if first is None: first = last
        return first, last

def num(v): return [1, [4, str(v)]]
def sub(b): return [2, b]

def attach_loop(blk, last_id, body_first):
    fvid = blk.add("control_forever", parent=last_id)
    if body_first: blk.d[fvid]["inputs"] = {"SUBSTACK": sub(body_first)}
    return fvid

# ══════════════ ۴) لباس‌ها ══════════════
COSTUME_FILES = {}
def costume(fname, cx, cy):
    data = (ASSETS / fname).read_bytes()
    md5 = hashlib.md5(data).hexdigest()
    ext = fname.rsplit(".", 1)[1]
    COSTUME_FILES[f"{md5}.{ext}"] = fname
    return {"assetId": md5, "name": fname.rsplit(".", 1)[0], "md5ext": f"{md5}.{ext}",
            "dataFormat": ext, "rotationCenterX": cx, "rotationCenterY": cy,
            "bitmapResolution": 1 if ext == "svg" else 2}, f"{md5}.{ext}"

def sprite(name, costumes, x=0, y=0, size=100, rotation_style="all around",
           layer=1, visible=True):
    blk = Blocks()
    assets = []
    for c, cx, cy in costumes:
        cost, fn = costume(c, cx, cy)
        assets.append(cost)
    return {"isStage": False, "name": name, "variables": {}, "lists": {},
            "broadcasts": {}, "blocks": blk.d, "comments": {},
            "currentCostume": 0, "costumes": assets, "sounds": [], "volume": 100,
            "layerOrder": layer, "visible": visible, "x": x, "y": y, "size": size,
            "direction": 90, "draggable": False, "rotationStyle": rotation_style}, blk

# ══════════════ ۵) اسپرایت‌ها و اسکریپت‌ها ══════════════
stage_cost, _ = costume("backdrop.svg", 240, 180)
stage = {"isStage": True, "name": "Stage", "variables": {}, "lists": {},
         "broadcasts": {}, "blocks": {}, "comments": {}, "currentCostume": 0,
         "costumes": [stage_cost], "sounds": [], "volume": 100, "layerOrder": 0,
         "tempo": 60, "videoTransparency": 50, "videoState": "off",
         "textToSpeechLanguage": None}
sprites = {"Stage": stage}

# خورشید: forever [ turn 4 ]
sun, b = sprite("Khorshid", [("sun.svg", 60, 60)], x=-170, y=95, layer=1)
flag = b.add("event_whenflagclicked", top=True, x=40, y=40)
first, last = b.chain([("motion_turnright", {"DEGREES": num(4)}, {})])
attach_loop(b, flag, first)
sprites["Khorshid"] = sun

# ابرها: forever [ glide راست، glide چپ ]
cloud1, b = sprite("Abr1", [("cloud1.svg", 90, 35)], x=-240, y=140,
                   rotation_style="don't rotate", layer=2)
flag = b.add("event_whenflagclicked", top=True, x=40, y=40)
g1, g1e = b.chain([("motion_glidesecstowait", {"SECS": num(7), "X": num(240), "Y": num(140)}, {}),
                   ("motion_glidesecstowait", {"SECS": num(7), "X": num(-240), "Y": num(140)}, {})])
b.d[flag]["next"] = g1; b.d[g1]["parent"] = flag
attach_loop(b, g1e, None)
sprites["Abr1"] = cloud1

cloud2, b = sprite("Abr2", [("cloud2.svg", 90, 35)], x=120, y=92, size=70,
                   rotation_style="don't rotate", layer=3)
flag = b.add("event_whenflagclicked", top=True, x=40, y=40)
g2, g2e = b.chain([("motion_glidesecstowait", {"SECS": num(5), "X": num(-240), "Y": num(92)}, {}),
                   ("motion_glidesecstowait", {"SECS": num(5), "X": num(240), "Y": num(92)}, {})])
b.d[flag]["next"] = g2; b.d[g2]["parent"] = flag
attach_loop(b, g2e, None)
sprites["Abr2"] = cloud2

# برج‌ها: بدون اسکریپت (ثابت)
tower1, _ = sprite("Edde1", [("tower.svg", 45, 16)], x=150, y=40,
                   rotation_style="don't rotate", layer=4)
sprites["Edde1"] = tower1
tower2, _ = sprite("Edde2", [("tower.svg", 45, 16)], x=-95, y=40, size=75,
                   rotation_style="don't rotate", layer=6)
sprites["Edde2"] = tower2

# روتورها: forever [ turn ]
rotor1, b = sprite("Parvane1", [("rotor.svg", 60, 60)], x=150, y=40, layer=5)
flag = b.add("event_whenflagclicked", top=True, x=40, y=40)
r1, _ = b.chain([("motion_turnright", {"DEGREES": num(6)}, {})])
attach_loop(b, flag, r1)
sprites["Parvane1"] = rotor1

rotor2, b = sprite("Parvane2", [("rotor.svg", 60, 60)], x=-95, y=40, size=75, layer=7)
flag = b.add("event_whenflagclicked", top=True, x=40, y=40)
r2, _ = b.chain([("motion_turnright", {"DEGREES": num(9)}, {})])
attach_loop(b, flag, r2)
sprites["Parvane2"] = rotor2

# موج‌ها: forever [ change x -speed ; if x < -250 → set x 150 ]
def make_wave(name, y, speed, layer, start_x):
    sp, b = sprite(name, [("wave.svg", 95, 15)], x=start_x, y=y,
                   rotation_style="don't rotate", layer=layer)
    flag = b.add("event_whenflagclicked", top=True, x=40, y=40)
    dx, _ = b.chain([("motion_changexby", {"DX": num(-speed)}, {})])
    xpos = b.add("motion_xposition")
    lt = b.add("operator_lt", inputs={"OPERAND1": [3, [10, ""], sub(xpos)],
                                      "OPERAND2": num(-250)})
    b.d[xpos]["parent"] = lt
    setx, _ = b.chain([("motion_setx", {"X": num(150)}, {})])
    iff = b.add("control_if", parent=None)
    b.d[iff]["inputs"] = {"CONDITION": sub(lt), "SUBSTACK": sub(setx)}
    b.d[setx]["parent"] = iff
    b.d[dx]["next"] = iff; b.d[iff]["parent"] = dx
    fvid = b.add("control_forever", parent=flag)
    b.d[flag]["next"] = fvid
    b.d[fvid]["inputs"] = {"SUBSTACK": sub(dx)}
    b.d[dx]["parent"] = fvid
    return sp

sprites["Moj1"] = make_wave("Moj1", -128, 4, 8, 0)
sprites["Moj2"] = make_wave("Moj2", -146, 3, 9, -120)

# جوانه: forever [ لباس۱+اندازه ۵۵، صبر، لباس۲+۸۰، صبر، لباس۳+۱۰۰، صبر ]
javaneh, b = sprite("Javaneh", [("javaneh1.svg", 40, 92), ("javaneh2.svg", 40, 92),
                                ("javaneh3.svg", 40, 92)], x=170, y=-88,
                    rotation_style="don't rotate", layer=10)
flag = b.add("event_whenflagclicked", top=True, x=40, y=40)
body, _ = b.chain([
    ("looks_switchcostumeto", {}, {"COSTUME": ["javaneh1", None]}),
    ("looks_setsizeto", {"SIZE": num(55)}, {}),
    ("control_wait", {"DURATION": num(1)}, {}),
    ("looks_switchcostumeto", {}, {"COSTUME": ["javaneh2", None]}),
    ("looks_setsizeto", {"SIZE": num(80)}, {}),
    ("control_wait", {"DURATION": num(1)}, {}),
    ("looks_switchcostumeto", {}, {"COSTUME": ["javaneh3", None]}),
    ("looks_setsizeto", {"SIZE": num(100)}, {}),
    ("control_wait", {"DURATION": num(2.5)}, {}),
])
attach_loop(b, flag, body)
sprites["Javaneh"] = javaneh

# عنوان: set ghost 0 → wait 2.5 → forever [ repeat15(+6) ، repeat15(-6) ]
title, b = sprite("Onvan", [("title.png", 330, 75)], x=60, y=112,
                  rotation_style="don't rotate", layer=11)
flag = b.add("event_whenflagclicked", top=True, x=40, y=40)
setg = b.add("looks_seteffectto", {"VALUE": num(0)}, {"EFFECT": ["GHOST", None]}, parent=flag)
w1 = b.add("control_wait", {"DURATION": num(2.5)}, {}, parent=setg)
c1, _ = b.chain([("looks_changeeffectby", {"CHANGE": num(6)}, {"EFFECT": ["GHOST", None]})])
b.d[c1]["parent"] = None
rep1 = b.add("control_repeat", {"TIMES": num(15), "SUBSTACK": sub(c1)}, parent=None)
c2, _ = b.chain([("looks_changeeffectby", {"CHANGE": num(-6)}, {"EFFECT": ["GHOST", None]})])
b.d[c2]["parent"] = None
rep2 = b.add("control_repeat", {"TIMES": num(15), "SUBSTACK": sub(c2)}, parent=None)
b.d[rep1]["next"] = rep2; b.d[rep2]["parent"] = rep1
fvt = b.add("control_forever", parent=w1)
b.d[w1]["next"] = fvt
b.d[fvt]["inputs"] = {"SUBSTACK": sub(rep1)}
b.d[rep1]["parent"] = fvt
sprites["Onvan"] = title

# ══════════════ ۶) مونتاژ و اعتبارسنجی ══════════════
order = ["Stage", "Khorshid", "Abr1", "Abr2", "Edde1", "Parvane1",
         "Edde2", "Parvane2", "Moj1", "Moj2", "Javaneh", "Onvan"]
targets = [sprites[n] for n in order]

errs = []
for t in targets:
    ids = set(t["blocks"].keys())
    for bid, blk in t["blocks"].items():
        if blk["next"] and blk["next"] not in ids: errs.append(f"{t['name']} {bid}: next→{blk['next']} missing")
        if blk["parent"] and blk["parent"] not in ids: errs.append(f"{t['name']} {bid}: parent→{blk['parent']} missing")
        for k, v in blk["inputs"].items():
            if not isinstance(v, list): continue
            if v[0] == 2 and isinstance(v[1], str) and v[1] not in ids:
                errs.append(f"{t['name']} {bid}.{k}: ref {v[1]} missing")
            if v[0] == 3 and len(v) > 2 and isinstance(v[2], list) and isinstance(v[2][1], str) and v[2][1] not in ids:
                errs.append(f"{t['name']} {bid}.{k}: shadow-ref {v[2][1]} missing")
    # نام لباس در fields
    names = {c["name"] for c in t["costumes"]}
    for bid, blk in t["blocks"].items():
        for k, v in blk["fields"].items():
            if k == "COSTUME" and v[0] not in names:
                errs.append(f"{t['name']} {bid}: costume '{v[0]}' not in {names}")
    for c in t["costumes"]:
        if c["md5ext"] not in COSTUME_FILES:
            errs.append(f"{t['name']}: asset {c['md5ext']} missing")
if errs:
    print("VALIDATION ERRORS:")
    for e in errs: print(" -", e)
    raise SystemExit(1)

project = {"targets": targets, "monitors": [], "extensions": [],
           "meta": {"semver": "3.0.0", "vm": "2.3.4", "agent": "g59-builder"}}

sb3 = OUT / "enerjy-novin-animation.sb3"
with zipfile.ZipFile(sb3, "w", zipfile.ZIP_DEFLATED) as z:
    z.writestr("project.json", json.dumps(project, ensure_ascii=False, indent=1))
    seen = set()
    for t in targets:
        for c in t["costumes"]:
            if c["md5ext"] in seen: continue
            seen.add(c["md5ext"])
            z.write(ASSETS / COSTUME_FILES[c["md5ext"]], c["md5ext"])
print("OK:", sb3, sb3.stat().st_size, "bytes | sprites:", [t["name"] for t in targets])
