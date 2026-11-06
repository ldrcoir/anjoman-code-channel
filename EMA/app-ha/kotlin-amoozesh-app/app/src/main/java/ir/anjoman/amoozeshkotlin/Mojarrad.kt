package ir.anjoman.amoozeshkotlin

/*
 * اجراکنندهٔ زمین بازی کد — درخت کد را قدم‌به‌قدم اجرا می‌کند
 * همهٔ خطاها با شمارهٔ خط و جملهٔ فارسی گزارش می‌شوند
 */

internal class Mojarrad(val voroodiHa: List<String>) {

    val tabHa = HashMap<String, TabTarif>()
    val sinfHa = HashMap<String, SinfTarif>()
    val global = Mohit(null)
    var ghadamHa = 0
    var omgh = 0
    var voroodiIndex = 0
    val khoruji = StringBuilder()

    fun khata(khat: Int, peygham: String): Nothing = throw KhataKotlin(peygham, khat)

    fun ejra(barname: Blok): String {
        for (d in barname.dastoorHa) {
            when (d) {
                is TabGereh -> tabHa[d.tab.esm] = d.tab
                is SinfGereh -> sinfHa[d.sinf.esm] = d.sinf
                is VarVar -> {
                    val v = arzyabi(d.meghdar, global)
                    nooCheckKon(d.noo, v, d.khat)
                    global.moteghayerHa[d.esm] = v
                    if (d.avazSho) global.ghabelTaghir.add(d.esm)
                }
                else -> khata(0, "در سطح بالای برنامه فقط تعریف تابع، کلاس و متغیر مجاز است.")
            }
        }
        val main = tabHa["main"]
            ?: khata(0, "تابع main پیدا نشد! برنامه باید fun main() داشته باشد.")
        try {
            sedazadanTab(main, emptyList(), null, 0)
        } catch (e: ParvazeBargardan) {
            // return در داخل main یعنی پایان برنامه — مشکلی نیست
        }
        return khoruji.toString()
    }

    // ---------- ابزارهای کمکی ----------

    fun matnBeMatn(a: Arzesh): String = when (a) {
        is Hich -> "null"
        is Sahih -> a.meghdar.toString()
        is Ashari -> a.meghdar.toString()
        is Matn -> a.meghdar
        is Mantri -> a.meghdar.toString()
        is Fehrest -> "[" + a.javabe.joinToString(", ") { matnBeMatn(it) } + "]"
        is Bazeh -> a.az.toString() + ".." + a.ta.toString()
        is Shey -> a.sinf.esm + "@" + Integer.toHexString(a.hashCode())
    }

    fun arzeshYeksan(a: Arzesh, b: Arzesh): Boolean {
        if (a is Hich && b is Hich) return true
        if (a is Hich || b is Hich) return false
        if (a is Sahih && b is Sahih) return a.meghdar == b.meghdar
        if (a is Ashari && b is Ashari) return a.meghdar == b.meghdar
        if (a is Sahih && b is Ashari) return a.meghdar.toDouble() == b.meghdar
        if (a is Ashari && b is Sahih) return a.meghdar == b.meghdar.toDouble()
        if (a is Matn && b is Matn) return a.meghdar == b.meghdar
        if (a is Mantri && b is Mantri) return a.meghdar == b.meghdar
        return a == b
    }

    fun hessabi(a: Arzesh, b: Arzesh, op: String, khat: Int): Arzesh {
        val aAdad = a is Sahih || a is Ashari
        val bAdad = b is Sahih || b is Ashari
        if (op == "+" && (a is Matn || b is Matn)) {
            return Matn(matnBeMatn(a) + matnBeMatn(b))
        }
        if (!aAdad || !bAdad) {
            khata(khat, "عملگر «$op» بین این دو مقدار معنی ندارد (${matnBeMatn(a)} و ${matnBeMatn(b)}).")
        }
        val ashariAst = a is Ashari || b is Ashari
        val x = if (a is Sahih) a.meghdar.toDouble() else (a as Ashari).meghdar
        val y = if (b is Sahih) b.meghdar.toDouble() else (b as Ashari).meghdar
        if (!ashariAst && a is Sahih && b is Sahih) {
            return when (op) {
                "+" -> Sahih(a.meghdar + b.meghdar)
                "-" -> Sahih(a.meghdar - b.meghdar)
                "*" -> Sahih(a.meghdar * b.meghdar)
                "/" -> {
                    if (b.meghdar == 0) khata(khat, "تقسیم بر صفر ممکن نیست!")
                    Sahih(a.meghdar / b.meghdar)
                }
                "%" -> {
                    if (b.meghdar == 0) khata(khat, "باقیماندهٔ تقسیم بر صفر ممکن نیست!")
                    Sahih(a.meghdar % b.meghdar)
                }
                else -> khata(khat, "عملگر ناشناخته: $op")
            }
        }
        val natije = when (op) {
            "+" -> x + y
            "-" -> x - y
            "*" -> x * y
            "/" -> {
                if (y == 0.0 && !ashariAst) khata(khat, "تقسیم بر صفر ممکن نیست!")
                x / y
            }
            "%" -> {
                if (y == 0.0 && !ashariAst) khata(khat, "باقیماندهٔ تقسیم بر صفر ممکن نیست!")
                x % y
            }
            else -> khata(khat, "عملگر ناشناخته: $op")
        }
        return if (ashariAst) Ashari(natije) else Ashari(natije).beSahih(khat)
    }

    fun Ashari.beSahih(khat: Int): Arzesh {
        val d = this.meghdar
        return if (d == Math.floor(d) && !d.isInfinite()) Sahih(d.toInt()) else Ashari(d)
    }

    fun moghayese(a: Arzesh, b: Arzesh, op: String, khat: Int): Mantri {
        val ok = (a is Sahih || a is Ashari) && (b is Sahih || b is Ashari) ||
            (a is Matn && b is Matn)
        if (!ok) khata(khat, "مقایسهٔ «$op» بین این دو مقدار معنی ندارد.")
        val n = when {
            a is Sahih && b is Sahih -> compareValues(a.meghdar, b.meghdar)
            a is Sahih && b is Ashari -> compareValues(a.meghdar.toDouble(), b.meghdar)
            a is Ashari && b is Sahih -> compareValues(a.meghdar, b.meghdar.toDouble())
            a is Ashari && b is Ashari -> compareValues(a.meghdar, b.meghdar)
            a is Matn && b is Matn -> compareValues(a.meghdar, b.meghdar)
            else -> 0
        }
        return Mantri(when (op) {
            "<" -> n < 0
            ">" -> n > 0
            "<=" -> n <= 0
            else -> n >= 0
        })
    }

    fun nooCheckKon(noo: String?, v: Arzesh, khat: Int) {
        if (noo == null) return
        val nooSade = noo.replace("?", "")
        if (v is Hich && !noo.contains("?")) {
            khata(khat, "نوع «$nooSade» مقدار نال قبول نمی‌کند؛ اگر می‌خواهی نال باشد، بنویس: $nooSade?")
        }
        when (nooSade) {
            "Int" -> if (v !is Sahih && v !is Hich) khata(khat, "داخل Int فقط عدد کامل می‌گذارند؛ مقدار تو ${matnBeMatn(v)} بود.")
            "Double" -> if (v !is Ashari && v !is Sahih && v !is Hich) khata(khat, "داخل Double فقط عدد می‌گذارند.")
            "String" -> if (v !is Matn && v !is Hich) khata(khat, "داخل String فقط متن می‌گذارند؛ مقدار تو ${matnBeMatn(v)} بود.")
            "Boolean" -> if (v !is Mantri && v !is Hich) khata(khat, "داخل Boolean فقط true یا false می‌گذارند.")
        }
    }

    // ---------- ارزیابی ----------
    fun arzyabi(g: Gereh, m: Mohit): Arzesh {
        ghadamHa++
        if (ghadamHa > 500000) {
            khata(0, "برنامه خیلی طول کشید! احتمالاً یک حلقهٔ بی‌نهایت داری.")
        }
        return when (g) {
            is Parche -> g.arzesh
            is ParcheMatn -> {
                val sab = StringBuilder()
                for (p in g.posheHa) {
                    if (p.sabetAst) sab.append(p.sabet)
                    else sab.append(matnBeMatn(arzyabi(p.ebarat!!, m)))
                }
                Matn(sab.toString())
            }
            is Moteghayyer -> {
                val v = m.peydaKon(g.esm)
                    ?: khata(g.khat, "متغیر «${g.esm}» تعریف نشده است.")
                v
            }
            is Dooi -> {
                if (g.amalgar == "&&") {
                    val c = arzyabi(g.chap, m)
                    if (c !is Mantri) khata(g.khat, "سمت چپ && باید درست/غلط باشد.")
                    if (!c.meghdar) return Mantri(false)
                    val r = arzyabi(g.rast, m)
                    if (r !is Mantri) khata(g.khat, "سمت راست && باید درست/غلط باشد.")
                    return Mantri(r.meghdar)
                }
                if (g.amalgar == "||") {
                    val c = arzyabi(g.chap, m)
                    if (c !is Mantri) khata(g.khat, "سمت چپ || باید درست/غلط باشد.")
                    if (c.meghdar) return Mantri(true)
                    val r = arzyabi(g.rast, m)
                    if (r !is Mantri) khata(g.khat, "سمت راست || باید درست/غلط باشد.")
                    return Mantri(r.meghdar)
                }
                if (g.amalgar == "in") {
                    val v = arzyabi(g.chap, m)
                    val baz = arzyabi(g.rast, m)
                    return Mantri(darIn(v, baz, g.khat))
                }
                val a = arzyabi(g.chap, m)
                val b = arzyabi(g.rast, m)
                when (g.amalgar) {
                    "+", "-", "*", "/", "%" -> hessabi(a, b, g.amalgar, g.khat)
                    "==", "!=" -> {
                        val y = arzeshYeksan(a, b)
                        Mantri(if (g.amalgar == "==") y else !y)
                    }
                    "<", ">", "<=", ">=" -> moghayese(a, b, g.amalgar, g.khat)
                    else -> khata(g.khat, "عملگر «${g.amalgar}» پشتیبانی نمی‌شود.")
                }
            }
            is YeganehPish -> {
                when (g.amalgar) {
                    "!" -> {
                        val v = arzyabi(g.sojekt, m)
                        if (v !is Mantri) khata(g.khat, "علامت ! فقط برای درست/غلط است.")
                        Mantri(!v.meghdar)
                    }
                    "-" -> {
                        val v = arzyabi(g.sojekt, m)
                        if (v is Sahih) Sahih(-v.meghdar)
                        else if (v is Ashari) Ashari(-v.meghdar)
                        else khata(g.khat, "منفی فقط برای عدد است.")
                    }
                    "+" -> arzyabi(g.sojekt, m)
                    "++", "--" -> {
                        val ghadim = arzyabi(g.sojekt, m)
                        val jadid = if (ghadim is Sahih) Sahih(ghadim.meghdar + if (g.amalgar == "++") 1 else -1)
                        else if (ghadim is Ashari) Ashari(ghadim.meghdar + if (g.amalgar == "++") 1.0 else -1.0)
                        else khata(g.khat, "++ و -- فقط برای عدد است.")
                        ghozashtanRoo(g.sojekt, jadid, "=", m, g.khat)
                        jadid
                    }
                    else -> khata(g.khat, "عملگر «${g.amalgar}» پشتیبانی نمی‌شود.")
                }
            }
            is YeganehPas -> {
                val ghadim = arzyabi(g.sojekt, m)
                val jadid = if (ghadim is Sahih) Sahih(ghadim.meghdar + if (g.amalgar == "++") 1 else -1)
                else if (ghadim is Ashari) Ashari(ghadim.meghdar + if (g.amalgar == "++") 1.0 else -1.0)
                else khata(g.khat, "++ و -- فقط برای عدد است.")
                ghozashtanRoo(g.sojekt, jadid, "=", m, g.khat)
                ghadim
            }
            is Sedazadan -> sedazadan(g.esm, g.vargha.map { arzyabi(it, m) }, null, g.khat)
            is MetodZan -> {
                val sojekt = arzyabi(g.sojekt, m)
                if (g.aman && sojekt is Hich) return Hich
                metod(sojekt, g.esm, g.vargha.map { arzyabi(it, m) }, m, g.khat)
            }
            is Dastresi -> {
                val sojekt = arzyabi(g.sojekt, m)
                if (g.aman && sojekt is Hich) return Hich
                vijegi(sojekt, g.esm, g.khat)
            }
            is Andis -> {
                val sojekt = arzyabi(g.sojekt, m)
                val andis = arzyabi(g.andis, m)
                if (sojekt is Fehrest) {
                    if (andis !is Sahih) khata(g.khat, "اندیس لیست باید عدد باشد.")
                    if (andis.meghdar < 0 || andis.meghdar >= sojekt.javabe.size) {
                        khata(g.khat, "اندیس ${andis.meghdar} خارج از محدودهٔ لیست است؛ اندازهٔ لیست ${sojekt.javabe.size} است.")
                    }
                    sojekt.javabe[andis.meghdar]
                } else if (sojekt is Matn) {
                    if (andis !is Sahih) khata(g.khat, "اندیس متن باید عدد باشد.")
                    if (andis.meghdar < 0 || andis.meghdar >= sojekt.meghdar.length) {
                        khata(g.khat, "اندیس خارج از محدودهٔ متن است.")
                    }
                    Matn(sojekt.meghdar[andis.meghdar].toString())
                } else {
                    khata(g.khat, "فقط لیست و متن اندیس دارد.")
                }
            }
            is BazehSaz -> {
                val az = arzyabi(g.az, m)
                val ta = arzyabi(g.ta, m)
                if (az !is Sahih || ta !is Sahih) khata(g.khat, "دو سر بازه باید عدد کامل (Int) باشند.")
                val ghadamVal = if (g.ghadam != null) {
                    val gh = arzyabi(g.ghadam, m)
                    if (gh !is Sahih) khata(g.khat, "گام (step) باید عدد کامل باشد.")
                    gh.meghdar
                } else 1
                when (g.nooBazeh) {
                    ".." -> Bazeh(az.meghdar, ta.meghdar, ghadamVal)
                    "until" -> Bazeh(az.meghdar, ta.meghdar - 1, ghadamVal)
                    "downTo" -> Bazeh(az.meghdar, ta.meghdar, -ghadamVal)
                    else -> khata(g.khat, "بازهٔ ناشناخته.")
                }
            }
            is InChap -> {
                val v = arzyabi(g.sojekt, m)
                val baz = arzyabi(g.bazeh, m)
                Mantri(darIn(v, baz, g.khat))
            }
            is PorsTaNolli -> {
                val v = arzyabi(g.sharT, m)
                if (v is Hich) arzyabi(g.vagarna, m) else v
            }
            is NemudNolli -> {
                val v = arzyabi(g.sojekt, m)
                if (v is Hich) khata(g.khat, "مقدار نال است! (همان NullPointerException معروف)")
                v
            }
            is EbaratAgar -> {
                val v = arzyabi(g.sharT, m)
                if (v !is Mantri) khata(g.khat, "شرط if باید درست یا غلط (Boolean) باشد.")
                if (v.meghdar) arzyabi(g.va, m) else arzyabi(g.vagarna, m)
            }
            is AgarGereh -> {
                val v = arzyabi(g.sharT, m)
                if (v !is Mantri) khata(g.khat, "شرط if باید درست یا غلط (Boolean) باشد.")
                if (v.meghdar) {
                    arzyabi(g.badane, m)
                } else if (g.vagarna != null) {
                    arzyabi(g.vagarna, m)
                } else Hich
            }
            is VaqtiGereh -> {
                val sojekt = if (g.sojekt != null) arzyabi(g.sojekt, m) else null
                for (i in g.sharTha.indices) {
                    val sh = g.sharTha[i]
                    var barkhord = false
                    if (sh.pishfarzAst) {
                        barkhord = true
                    } else if (sh.darBazeh) {
                        val baz = arzyabi(sh.sharT!!, m)
                        barkhord = sojekt is Sahih && baz is Bazeh && baz.darBazeh(sojekt.meghdar)
                    } else if (sh.sharT is InChap) {
                        val v = arzyabi(sh.sharT, m)
                        barkhord = v is Mantri && v.meghdar
                    } else if (sojekt != null) {
                        val v = arzyabi(sh.sharT!!, m)
                        barkhord = arzeshYeksan(sojekt, v)
                    } else {
                        val v = arzyabi(sh.sharT!!, m)
                        barkhord = v is Mantri && v.meghdar
                    }
                    if (barkhord) return arzyabi(g.badaneHa[i], m)
                }
                Hich
            }
            is VarVar -> {
                if (m.moteghayerHa.containsKey(g.esm)) {
                    khata(g.khat, "متغیر «${g.esm}» در همین محدوده دوبار تعریف شده است.")
                }
                val v = arzyabi(g.meghdar, m)
                nooCheckKon(g.noo, v, g.khat)
                m.moteghayerHa[g.esm] = v
                if (g.avazSho) m.ghabelTaghir.add(g.esm)
                Hich
            }
            is Gozashtan -> {
                val v = arzyabi(g.meghdar, m)
                ghozashtanRoo(g.hadaf, v, g.amalgar, m, g.khat)
                Hich
            }
            is EbaratDastoor -> {
                arzyabi(g.ebarat, m)
                Hich
            }
            is Blok -> {
                val zir = Mohit(m)
                for (d in g.dastoorHa) {
                    arzyabi(d, zir)
                }
                Hich
            }
            is BarayeGereh -> {
                val baz = arzyabi(g.bazeh, m)
                val zir = Mohit(m)
                val adadHa: List<Arzesh> = when (baz) {
                    is Bazeh -> baz.hame().map { Sahih(it) }
                    is Fehrest -> baz.javabe.toList()
                    is Matn -> baz.meghdar.map { Matn(it.toString()) }
                    else -> khata(g.khat, "حلقهٔ for فقط روی بازه و لیست و متن می‌چرخد.")
                }
                for (a in adadHa) {
                    zir.moteghayerHa.clear()
                    zir.moteghayerHa[g.esm] = a
                    try {
                        arzyabi(g.badane, zir)
                    } catch (e: ParvazeEdame) {
                        // ادامهٔ دور بعد
                    } catch (e: ParvazeShekan) {
                        return Hich
                    }
                }
                Hich
            }
            is TaVaqtiGereh -> {
                while (true) {
                    val v = arzyabi(g.sharT, m)
                    if (v !is Mantri) khata(g.khat, "شرط while باید درست یا غلط (Boolean) باشد.")
                    if (!v.meghdar) break
                    try {
                        arzyabi(g.badane, m)
                    } catch (e: ParvazeEdame) {
                        // دور بعد
                    } catch (e: ParvazeShekan) {
                        break
                    }
                }
                Hich
            }
            is ShekanGereh -> throw ParvazeShekan
            is EdameGereh -> throw ParvazeEdame
            is BargardanGereh -> {
                val v = if (g.meghdar != null) arzyabi(g.meghdar, m) else Hich
                throw ParvazeBargardan(v)
            }
            is TabGereh -> {
                tabHa[g.tab.esm] = g.tab
                Hich
            }
            is SinfGereh -> {
                sinfHa[g.sinf.esm] = g.sinf
                Hich
            }
        }
    }

    fun darIn(v: Arzesh, baz: Arzesh, khat: Int): Boolean = when {
        baz is Bazeh && v is Sahih -> baz.darBazeh(v.meghdar)
        baz is Fehrest -> baz.javabe.any { arzeshYeksan(it, v) }
        baz is Matn && v is Matn -> baz.meghdar.contains(v.meghdar)
        else -> khata(khat, "عملگر in با این مقدارها معنی ندارد.")
    }

    // هدفِ انتساب را به شکل قابل‌نوشتن درمی‌آوریم
    fun ghadimGir(hadaf: Gereh, m: Mohit): Arzesh = arzyabi(hadaf, m)

    fun ghozashtanRoo(hadaf: Gereh, v: Arzesh, amalgar: String, m: Mohit, khat: Int) {
        val meghdarAsli = if (amalgar == "=") v else {
            val ghadim = arzyabi(hadaf, m)
            if (amalgar == "+=" && (ghadim is Matn || v is Matn)) Matn(matnBeMatn(ghadim) + matnBeMatn(v))
            else hessabi(ghadim, v, amalgar.removeSuffix("="), khat)
        }
        when (hadaf) {
            is Moteghayyer -> {
                val mh = m.mohitElan(hadaf.esm)
                if (mh != null) {
                    if (hadaf.esm !in mh.ghabelTaghir) {
                        khata(khat, "«${hadaf.esm}» با val ساخته شده و مقدارش عوض نمی‌شود؛ به جایش var بنویس.")
                    }
                    mh.moteghayerHa[hadaf.esm] = meghdarAsli
                } else {
                    val sh = m.sheyElan(hadaf.esm)
                    if (sh != null) {
                        vijegiGhozashtan(sh, hadaf.esm, meghdarAsli, khat)
                    } else {
                        khata(khat, "متغیر «${hadaf.esm}» تعریف نشده است.")
                    }
                }
            }
            is Dastresi -> {
                val sojekt = arzyabi(hadaf.sojekt, m)
                if (sojekt is Shey) {
                    vijegiGhozashtan(sojekt, hadaf.esm, meghdarAsli, khat)
                } else {
                    khata(khat, "به این ویژگی نمی‌شود مقدار داد.")
                }
            }
            is Andis -> {
                val sojekt = arzyabi(hadaf.sojekt, m)
                val andis = arzyabi(hadaf.andis, m)
                if (sojekt is Fehrest) {
                    if (andis !is Sahih) khata(khat, "اندیس لیست باید عدد باشد.")
                    if (andis.meghdar < 0 || andis.meghdar >= sojekt.javabe.size) {
                        khata(khat, "اندیس ${andis.meghdar} خارج از محدودهٔ لیست است.")
                    }
                    sojekt.javabe[andis.meghdar] = meghdarAsli
                } else {
                    khata(khat, "فقط عنصرهای لیست با اندیس عوض می‌شوند.")
                }
            }
            else -> khata(khat, "سمت چپِ «=» درست نیست.")
        }
    }

    fun vijegiGhozashtan(sh: Shey, esm: String, v: Arzesh, khat: Int) {
        val vij = sh.sinf.vijegiHa.firstOrNull { it.esm == esm }
            ?: khata(khat, "کلاس «${sh.sinf.esm}» ویژگی‌ای به نام «$esm» ندارد.")
        if (!vij.avazSho) {
            khata(khat, "ویژگی «$esm» با val ساخته شده و عوض نمی‌شود.")
        }
        sh.mahalHa[esm] = v
    }

    // ---------- صدا زدن تابع ----------
    fun sedazadan(tab: TabTarif, vargha: List<Arzesh>, daryaft: Shey?, khat: Int): Arzesh {
        omgh++
        if (omgh > 150) {
            omgh--
            khata(khat, "عمق فراخوانی تابع خیلی زیاد شد! شاید شرط توقف (return) را جا انداخته‌ای.")
        }
        try {
            if (vargha.size > tab.parametrHa.size) {
                khata(khat, "تابع «${tab.esm}» فقط ${tab.parametrHa.size} پارامتر دارد.")
            }
            val m = Mohit(global)
            m.daryaft = daryaft
            for (i in tab.parametrHa.indices) {
                val p = tab.parametrHa[i]
                val v = if (i < vargha.size) vargha[i] else {
                    if (p.pishfarz == null) {
                        khata(khat, "پارامتر «${p.esm}» در صدا زدنِ «${tab.esm}» جا افتاده است.")
                    }
                    arzyabi(p.pishfarz, m)
                }
                nooCheckKon(p.noo, v, khat)
                m.moteghayerHa[p.esm] = v
                m.ghabelTaghir.add(p.esm)
            }
            return try {
                if (tab.badane != null) arzyabi(tab.badane, m)
                else if (tab.badaneEbarat != null) arzyabi(tab.badaneEbarat, m)
                else Hich
            } catch (e: ParvazeBargardan) {
                e.arzesh
            }
        } finally {
            omgh--
        }
    }

    // ---------- متدها و ویژگی‌های آماده ----------
    fun metod(sojekt: Arzesh, esm: String, vargha: List<Arzesh>, m: Mohit, khat: Int): Arzesh {
        if (sojekt is Shey) {
            val rav = sojekt.sinf.raveshHa[esm]
                ?: khata(khat, "کلاس «${sojekt.sinf.esm}» متدی به نام «$esm» ندارد.")
            return sedazadan(rav, vargha, sojekt, khat)
        }
        if (sojekt is Hich) khata(khat, "مقدار نال است! شاید باید از ?. استفاده کنی.")
        if (sojekt is Matn) {
            return when (esm) {
                "uppercase", "toUpperCase" -> Matn(sojekt.meghdar.uppercase())
                "lowercase", "toLowerCase" -> Matn(sojekt.meghdar.lowercase())
                "reversed" -> Matn(sojekt.meghdar.reversed())
                "trim" -> Matn(sojekt.meghdar.trim())
                "repeat" -> {
                    val n = vargha.getOrNull(0)
                    if (n !is Sahih) khata(khat, "repeat یک عدد کامل می‌گیرد.")
                    Matn(sojekt.meghdar.repeat(n.meghdar))
                }
                "contains" -> {
                    val n = vargha.getOrNull(0)
                    if (n !is Matn) khata(khat, "contains یک متن می‌گیرد.")
                    Mantri(sojekt.meghdar.contains(n.meghdar))
                }
                "toInt" -> {
                    val n = sojekt.meghdar.trim().toIntOrNull()
                        ?: khata(khat, "متن «${sojekt.meghdar}» به عدد کامل (Int) تبدیل نمی‌شود.")
                    Sahih(n)
                }
                "toDouble" -> {
                    val n = sojekt.meghdar.trim().toDoubleOrNull()
                        ?: khata(khat, "متن «${sojekt.meghdar}» به عدد اعشاری (Double) تبدیل نمی‌شود.")
                    Ashari(n)
                }
                "toString" -> Matn(sojekt.meghdar)
                else -> khata(khat, "رشته، متدی به نام «$esm» ندارد.")
            }
        }
        if (sojekt is Sahih || sojekt is Ashari) {
            return when (esm) {
                "toString" -> Matn(matnBeMatn(sojekt))
                "toInt" -> {
                    if (sojekt is Sahih) sojekt else Sahih((sojekt as Ashari).meghdar.toInt())
                }
                "toDouble" -> {
                    if (sojekt is Ashari) sojekt else Ashari((sojekt as Sahih).meghdar.toDouble())
                }
                else -> khata(khat, "عدد، متدی به نام «$esm» ندارد.")
            }
        }
        if (sojekt is Fehrest) {
            return when (esm) {
                "add" -> {
                    val v = vargha.getOrNull(0) ?: khata(khat, "add یک مقدار می‌گیرد.")
                    sojekt.javabe.add(v)
                    Hich
                }
                "removeAt" -> {
                    val n = vargha.getOrNull(0)
                    if (n !is Sahih) khata(khat, "removeAt یک عدد کامل می‌گیرد.")
                    if (n.meghdar < 0 || n.meghdar >= sojekt.javabe.size) {
                        khata(khat, "اندیس خارج از محدودهٔ لیست است.")
                    }
                    sojekt.javabe.removeAt(n.meghdar)
                }
                "indexOf" -> {
                    val v = vargha.getOrNull(0) ?: khata(khat, "indexOf یک مقدار می‌گیرد.")
                    val i = sojekt.javabe.indexOfFirst { arzeshYeksan(it, v) }
                    Sahih(i)
                }
                "contains" -> {
                    val v = vargha.getOrNull(0) ?: khata(khat, "contains یک مقدار می‌گیرد.")
                    Mantri(sojekt.javabe.any { arzeshYeksan(it, v) })
                }
                "sum" -> {
                    var jam = 0.0
                    var ashariAst = false
                    for (x in sojekt.javabe) {
                        if (x is Sahih) jam += x.meghdar
                        else if (x is Ashari) { jam += x.meghdar; ashariAst = true }
                        else khata(khat, "sum فقط برای لیست عددی است.")
                    }
                    if (ashariAst) Ashari(jam) else Sahih(jam.toInt())
                }
                "reversed" -> Fehrest(sojekt.javabe.reversed().toMutableList())
                "toString" -> Matn(matnBeMatn(sojekt))
                else -> khata(khat, "لیست، متدی به نام «$esm» ندارد.")
            }
        }
        if (sojekt is Bazeh) {
            return when (esm) {
                "toString" -> Matn(matnBeMatn(sojekt))
                else -> khata(khat, "بازه، متدی به نام «$esm» ندارد.")
            }
        }
        khata(khat, "اینجا نمی‌شود «$esm» را صدا زد.")
    }

    fun vijegi(sojekt: Arzesh, esm: String, khat: Int): Arzesh {
        if (sojekt is Hich) khata(khat, "مقدار نال است! شاید باید از ?. استفاده کنی.")
        if (sojekt is Matn && esm == "length") return Sahih(sojekt.meghdar.length)
        if (sojekt is Fehrest && esm == "size") return Sahih(sojekt.javabe.size)
        if (sojekt is Shey) {
            return sojekt.mahalHa[esm]
                ?: khata(khat, "کلاس «${sojekt.sinf.esm}» ویژگی‌ای به نام «$esm» ندارد.")
        }
        khata(khat, "«$esm» یک ویژگی نیست؛ اگر متد است پرانتز بگذار: $esm()")
    }

    // ---------- توابع آماده ----------
    fun sedazadan(esm: String, vargha: List<Arzesh>, daryaft: Shey?, khat: Int): Arzesh {
        // اول تابع‌های خود کاربر
        val tab = tabHa[esm]
        if (tab != null) return sedazadanTab(tab, vargha, daryaft, khat)
        val kelas = sinfHa[esm]
        if (kelas != null) {
            if (vargha.size > kelas.vijegiHa.size) {
                khata(khat, "کلاس «${kelas.esm}» فقط ${kelas.vijegiHa.size} ویژگی در سازنده دارد.")
            }
            val mahal = HashMap<String, Arzesh>()
            val m = Mohit(global)
            for (i in kelas.vijegiHa.indices) {
                val vij = kelas.vijegiHa[i]
                val v = if (i < vargha.size) vargha[i] else {
                    if (vij.pishfarz == null) khata(khat, "ویژگی «${vij.esm}» مقدار نگرفت.")
                    arzyabi(vij.pishfarz, m)
                }
                nooCheckKon(vij.noo, v, khat)
                mahal[vij.esm] = v
            }
            return Shey(kelas, mahal)
        }
        return when (esm) {
            "println", "print" -> {
                if (vargha.size > 1) khata(khat, "$esm فقط یک ورودی می‌گیرد.")
                val matn = if (vargha.isEmpty()) "" else matnBeMatn(vargha[0])
                khoruji.append(matn)
                if (esm == "println") khoruji.append('\n')
                Hich
            }
            "readln", "readLine" -> {
                if (voroodiIndex >= voroodiHa.size) {
                    khata(khat, "ورودی بیشتری هست نیست! در کادر «ورودی» هر مقدار را در یک خط بنویس.")
                }
                val s = voroodiHa[voroodiIndex].removeSuffix("\r")
                voroodiIndex++
                Matn(s)
            }
            "listOf", "mutableListOf", "arrayListOf" -> Fehrest(vargha.toMutableList())
            else -> khata(khat, "تابع «$esm» تعریف نشده است.")
        }
    }

    fun sedazadanTab(tab: TabTarif, vargha: List<Arzesh>, daryaft: Shey?, khat: Int): Arzesh {
        omgh++
        if (omgh > 150) {
            omgh--
            khata(khat, "عمق فراخوانی تابع خیلی زیاد شد! شاید شرط توقف (return) را جا انداخته‌ای.")
        }
        try {
            if (vargha.size > tab.parametrHa.size) {
                khata(khat, "تابع «${tab.esm}» فقط ${tab.parametrHa.size} پارامتر دارد.")
            }
            val m = Mohit(global)
            m.daryaft = daryaft
            for (i in tab.parametrHa.indices) {
                val p = tab.parametrHa[i]
                val v = if (i < vargha.size) vargha[i] else {
                    if (p.pishfarz == null) {
                        khata(khat, "پارامتر «${p.esm}» در صدا زدنِ «${tab.esm}» جا افتاده است.")
                    }
                    arzyabi(p.pishfarz, m)
                }
                nooCheckKon(p.noo, v, khat)
                m.moteghayerHa[p.esm] = v
                m.ghabelTaghir.add(p.esm)
            }
            return try {
                if (tab.badane != null) arzyabi(tab.badane, m)
                else if (tab.badaneEbarat != null) arzyabi(tab.badaneEbarat, m)
                else Hich
            } catch (e: ParvazeBargardan) {
                e.arzesh
            }
        } finally {
            omgh--
        }
    }
}

/*
 * نمای بیرونی زمین بازی: کد و ورودی را می‌گیرد، خروجی یا خطا می‌دهد
 */
class NatijeKod(val movafagh: Boolean, val khoruji: String, val peyghamKhata: String?, val khatKhata: Int)

object Tafsir {
    fun ejra(kod: String, voroodiHa: List<String> = emptyList()): NatijeKod {
        return try {
            val tHa = Leksor(kod).leksorKon()
            val p = Parsir(tHa)
            val barname = p.barname()
            val khoruji = Mojarrad(voroodiHa).ejra(barname)
            NatijeKod(true, khoruji, null, 0)
        } catch (e: KhataKotlin) {
            val shomareh = if (e.khat > 0) "خط ${e.khat}: " else ""
            NatijeKod(false, "", shomareh + e.peygham, e.khat)
        } catch (e: Exception) {
            NatijeKod(false, "", "یه خطای غیرمنتظره رخ داد: " + (e.message ?: e.toString()), 0)
        }
    }
}
