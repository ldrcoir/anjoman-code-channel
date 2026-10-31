package ir.anjoman.amoozeshkotlin

/*
 * لکسر و پارسر زمین بازی کد
 * کد کاربر را به گره تبدیل می‌کند و وسط راه خطاها را با خطِ خطا گزارش می‌کند
 */

internal data class Yekan(val noo: String, val matn: String, val khat: Int)

// کلمه‌های کلیدی زیرمجموعهٔ آموزشی
internal val KELIDAT = setOf(
    "fun", "val", "var", "if", "else", "when", "for", "while", "in",
    "return", "break", "continue", "true", "false", "null", "class", "this",
    "until", "downTo", "step"
)

internal class Leksor(val matnKhol: String) {

    val khoroji = ArrayList<Yekan>()
    var i = 0
    var khat = 1

    fun leksorKon(): List<Yekan> {
        val dovomChar = listOf(
            "==", "!=", "<=", ">=", "&&", "||", "+=", "-=", "*=", "/=", "%=",
            "++", "--", "->", "..", "?.", "?:", "!!"
        )
        while (i < matnKhol.length) {
            val c = matnKhol[i]
            when {
                c == '\n' -> {
                    khat++
                    i++
                    if (khoroji.isNotEmpty() && khoroji.last().noo != "satr") khoroji.add(Yekan("satr", "", khat))
                }
                c == ' ' || c == '\t' || c == '\r' -> i++
                c == ';' -> {
                    // نقطه‌ویرگول مثل پایان خط عمل می‌کند
                    i++
                    if (khoroji.isNotEmpty() && khoroji.last().noo != "satr") khoroji.add(Yekan("satr", "", khat))
                }
                c == '/' && i + 1 < matnKhol.length && matnKhol[i + 1] == '/' -> {
                    while (i < matnKhol.length && matnKhol[i] != '\n') i++
                }
                c == '/' && i + 1 < matnKhol.length && matnKhol[i + 1] == '*' -> {
                    i += 2
                    while (i + 1 < matnKhol.length && !(matnKhol[i] == '*' && matnKhol[i + 1] == '/')) {
                        if (matnKhol[i] == '\n') khat++
                        i++
                    }
                    i += 2
                }
                c == '"' -> {
                    i++
                    val shoroKhat = khat
                    val sab = StringBuilder()
                    while (i < matnKhol.length && matnKhol[i] != '"') {
                        if (matnKhol[i] == '\n') throw KhataKotlin("رشتهٔ متنی با گیومه بسته نشد!", khat)
                        if (matnKhol[i] == '\\' && i + 1 < matnKhol.length) {
                            when (matnKhol[i + 1]) {
                                'n' -> sab.append('\n')
                                't' -> sab.append('\t')
                                '\\' -> sab.append('\\')
                                '"' -> sab.append('"')
                                '$' -> sab.append('\u0001') // نشانهٔ دلار خام
                                else -> sab.append(matnKhol[i + 1])
                            }
                            i += 2
                        } else {
                            sab.append(matnKhol[i])
                            i++
                        }
                    }
                    if (i >= matnKhol.length) throw KhataKotlin("رشتهٔ متنی با گیومه بسته نشد!", shoroKhat)
                    i++
                    khoroji.add(Yekan("matn", sab.toString(), shoroKhat))
                }
                c.isDigit() -> {
                    val shoro = i
                    while (i < matnKhol.length && matnKhol[i].isDigit()) i++
                    if (i < matnKhol.length && matnKhol[i] == '.' && i + 1 < matnKhol.length && matnKhol[i + 1].isDigit()) {
                        i++
                        while (i < matnKhol.length && matnKhol[i].isDigit()) i++
                    }
                    khoroji.add(Yekan("adad", matnKhol.substring(shoro, i), khat))
                }
                c.isLetter() || c == '_' -> {
                    val shoro = i
                    while (i < matnKhol.length && (matnKhol[i].isLetterOrDigit() || matnKhol[i] == '_')) i++
                    khoroji.add(Yekan("harf", matnKhol.substring(shoro, i), khat))
                }
                else -> {
                    val doHarfi = matnKhol.substring(i, minOf(i + 2, matnKhol.length))
                    if (doHarfi in dovomChar) {
                        khoroji.add(Yekan("amalgar", doHarfi, khat))
                        i += 2
                    } else if (c in "+-*/%=<>!(){}[],:.?") {
                        khoroji.add(Yekan("amalgar", c.toString(), khat))
                        i++
                    } else {
                        throw KhataKotlin("این نویسه در کاتلین شناخته نمی‌شود: «$c\"", khat)
                    }
                }
            }
        }
        khoroji.add(Yekan("tamam", "", khat))
        return khoroji
    }
}

internal class Parsir(val tHa: List<Yekan>) {

    var j = 0

    fun faali(): Yekan = tHa[j]

    fun faaliAst(noo: String, matn: String? = null): Boolean {
        val t = tHa[j]
        return t.noo == noo && (matn == null || t.matn == matn)
    }

    fun begir(noo: String, matnEntezar: String? = null, tozih: String? = null): Yekan {
        val t = tHa[j]
        val entezar = matnEntezar?.let { " «$it»" } ?: ""
        val bayad = tozih ?: (noo + entezar)
        if (t.noo != noo || (matnEntezar != null && t.matn != matnEntezar)) {
            val dar = when (t.noo) {
                "tamam" -> "آخرِ کد"
                "satr" -> "پایانِ خط"
                else -> "«${t.matn}»"
            }
            throw KhataKotlin("به جای $bayad، $dar آمد.", t.khat)
        }
        j++
        return t
    }

    fun satrHaRajKon() {
        while (faaliAst("satr")) j++
    }

    fun barname(): Blok {
        val dastoorHa = ArrayList<Gereh>()
        satrHaRajKon()
        while (!faaliAst("tamam")) {
            val t = faali()
            if (t.noo == "harf" && t.matn !in KELIDAT) {
                throw KhataKotlin("در سطح بالای برنامه فقط تعریف تابع (fun)، کلاس (class) و متغیر (val/var) مجاز است.", t.khat)
            }
            when {
                faaliAst("harf", "fun") -> dastoorHa.add(tabDars())
                faaliAst("harf", "class") -> dastoorHa.add(sinfDars())
                faaliAst("harf", "val") || faaliAst("harf", "var") -> dastoorHa.add(varVarDars())
                else -> throw KhataKotlin(
                    "در سطح بالای برنامه فقط fun و class و val/var مجاز است؛ دستورها را داخل fun main() بنویس.", t.khat)
            }
            satrHaRajKon()
        }
        return Blok(dastoorHa)
    }

    // ---------- دستورها ----------

    fun blok(): Blok {
        begir("amalgar", "{", "آکولاد باز {")
        val dastoorHa = ArrayList<Gereh>()
        satrHaRajKon()
        while (!faaliAst("amalgar", "}")) {
            if (faaliAst("tamam")) throw KhataKotlin("آکولاد بسته «}» جا افتاده است.", faali().khat)
            val d = dastoor() ?: break
            dastoorHa.add(d)
            satrHaRajKon()
        }
        begir("amalgar", "}", "آکولاد بسته }")
        return Blok(dastoorHa)
    }

    // بدنهٔ if و for و while: یا بلوک یا یک دستور
    fun badane(): Gereh {
        return if (faaliAst("amalgar", "{")) blok() else dastoor() ?: throw KhataKotlin("بدنه خالی است!", faali().khat)
    }

    fun dastoor(): Gereh? {
        satrHaRajKon()
        if (faaliAst("amalgar", "}") || faaliAst("tamam")) return null
        val t = faali()
        return when {
            faaliAst("harf", "val") || faaliAst("harf", "var") -> varVarDars()
            faaliAst("harf", "fun") -> tabDars()
            faaliAst("harf", "if") -> agarDars()
            faaliAst("harf", "when") -> vaqtiGereh()
            faaliAst("harf", "for") -> barayeDars()
            faaliAst("harf", "while") -> taVaqtiDars()
            faaliAst("harf", "return") -> {
                begir("harf", "return")
                if (faaliAst("satr") || faaliAst("amalgar", "}") || faaliAst("tamam")) {
                    BargardanGereh(null, t.khat)
                } else {
                    BargardanGereh(ebarat(), t.khat)
                }
            }
            faaliAst("harf", "break") -> { begir("harf", "break"); ShekanGereh }
            faaliAst("harf", "continue") -> { begir("harf", "continue"); EdameGereh }
            else -> {
                // انتساب یا یک عبارت
                val e = ebarat()
                val amalgarEntesab = listOf("=", "+=", "-=", "*=", "/=", "%=")
                if (faaliAst("amalgar") && faali().matn in amalgarEntesab) {
                    val op = begir("amalgar").matn
                    if (e !is Moteghayyer && e !is Dastresi && e !is Andis) {
                        throw KhataKotlin("سمت چپِ «=» باید یک متغیر یا ویژگی باشد.", faali().khat)
                    }
                    Gozashtan(e, op, ebarat(), t.khat)
                } else {
                    EbaratDastoor(e)
                }
            }
        }
    }

    fun varVarDars(): Gereh {
        val t = begir("harf") // val یا var
        val avazSho = t.matn == "var"
        val esm = begir("harf", tozih = "اسم متغیر").matn
        var noo: String? = null
        if (faaliAst("amalgar", ":")) {
            begir("amalgar", ":")
            noo = nooBekhon()
        }
        begir("amalgar", "=", "یک مقدار اولیه بعد از «=»")
        val meghdar = ebarat()
        return VarVar(esm, avazSho, noo, meghdar, t.khat)
    }

    fun nooBekhon(): String {
        val t = begir("harf", tozih = "نوع داده مثل Int یا String")
        var noo = t.matn
        if (faaliAst("amalgar", "<")) {
            begir("amalgar", "<")
            noo += "<" + begir("harf", tozih = "نوع داخل < >").matn + ">"
            begir("amalgar", ">", "«>»")
        }
        if (faaliAst("amalgar", "?")) {
            begir("amalgar", "?")
            noo += "?"
        }
        return noo
    }

    fun tabDars(): Gereh {
        val t = begir("harf", "fun")
        val esm = begir("harf", tozih = "اسم تابع").matn
        begir("amalgar", "(", "پرانتز باز (")
        val parametrHa = ArrayList<ParamTarif>()
        satrHaRajKon()
        if (!faaliAst("amalgar", ")")) {
            do {
                val pEsm = begir("harf", tozih = "اسم پارامتر").matn
                begir("amalgar", ":", "دونقطه و نوع پارامتر")
                val pNoo = nooBekhon()
                var pPishfarz: Gereh? = null
                if (faaliAst("amalgar", "=")) {
                    begir("amalgar", "=")
                    pPishfarz = ebarat()
                }
                parametrHa.add(ParamTarif(pEsm, pNoo, pPishfarz))
                if (faaliAst("amalgar", ",")) begir("amalgar", ",")
                satrHaRajKon()
            } while (!faaliAst("amalgar", ")"))
        }
        begir("amalgar", ")", "پرانتز بسته )")
        var khoruji: String? = null
        if (faaliAst("amalgar", ":")) {
            begir("amalgar", ":")
            khoruji = nooBekhon()
        }
        if (faaliAst("amalgar", "=")) {
            begir("amalgar", "=")
            val e = ebarat()
            return TabGereh(TabTarif(esm, parametrHa, khoruji, null, e), t.khat)
        }
        val badane = blok()
        return TabGereh(TabTarif(esm, parametrHa, khoruji, badane, null), t.khat)
    }

    fun sinfDars(): Gereh {
        val t = begir("harf", "class")
        val esm = begir("harf", tozih = "اسم کلاس").matn
        val vijegiHa = ArrayList<VijegiTarif>()
        begir("amalgar", "(", "پرانتز باز ( برای ویژگی‌ها")
        satrHaRajKon()
        if (!faaliAst("amalgar", ")")) {
            do {
                val k = begir("harf", tozih = "val یا var").matn
                if (k != "val" && k != "var") throw KhataKotlin("داخل پرانتز کلاس با val یا var شروع کن.", t.khat)
                val vEsm = begir("harf", tozih = "اسم ویژگی").matn
                begir("amalgar", ":", "دونقطه و نوع ویژگی")
                val vNoo = nooBekhon()
                var vPishfarz: Gereh? = null
                if (faaliAst("amalgar", "=")) {
                    begir("amalgar", "=")
                    vPishfarz = ebarat()
                }
                vijegiHa.add(VijegiTarif(vEsm, vNoo, k == "var", vPishfarz))
                if (faaliAst("amalgar", ",")) begir("amalgar", ",")
                satrHaRajKon()
            } while (!faaliAst("amalgar", ")"))
        }
        begir("amalgar", ")", "پرانتز بسته )")

        val raveshHa = HashMap<String, TabTarif>()
        val vijegiBadane = ArrayList<VijegiTarif>()
        if (faaliAst("amalgar", "{")) {
            begir("amalgar", "{")
            satrHaRajKon()
            while (!faaliAst("amalgar", "}")) {
                when {
                    faaliAst("harf", "fun") -> {
                        val tg = tabDars() as TabGereh
                        raveshHa[tg.tab.esm] = tg.tab
                    }
                    faaliAst("harf", "val") || faaliAst("harf", "var") -> {
                        val v = varVarDars() as VarVar
                        vijegiBadane.add(VijegiTarif(v.esm, v.noo, v.avazSho, v.meghdar))
                    }
                    else -> throw KhataKotlin("داخل کلاس فقط fun و val/var مجاز است.", faali().khat)
                }
                satrHaRajKon()
            }
            begir("amalgar", "}")
        }
        vijegiHa.addAll(vijegiBadane)
        return SinfGereh(SinfTarif(esm, vijegiHa, raveshHa), t.khat)
    }

    fun agarDars(): Gereh {
        val t = begir("harf", "if")
        begir("amalgar", "(", "پرانتز باز (")
        val sharT = ebarat()
        begir("amalgar", ")", "پرانتز بسته )")
        val badane = badane()
        var vagarna: Gereh? = null
        satrHaRajKon() // else می‌تواند در خط بعدی بیاید
        if (faaliAst("harf", "else")) {
            begir("harf", "else")
            vagarna = if (faaliAst("harf", "if")) agarDars() else badane()
        }
        return AgarGereh(sharT, badane, vagarna, t.khat)
    }

    fun vaqtiGereh(): Gereh {
        val t = begir("harf", "when")
        var sojekt: Gereh? = null
        if (faaliAst("amalgar", "(")) {
            begir("amalgar", "(")
            sojekt = ebarat()
            begir("amalgar", ")", "پرانتز بسته )")
        }
        begir("amalgar", "{", "آکولاد باز {")
        val sharTha = ArrayList<HalateVaqti>()
        val badaneHa = ArrayList<Gereh>()
        satrHaRajKon()
        while (!faaliAst("amalgar", "}")) {
            if (faaliAst("tamam")) throw KhataKotlin("آکولاد بستهٔ when جا افتاده است.", t.khat)
            if (faaliAst("harf", "else")) {
                begir("harf", "else")
                sharTha.add(HalateVaqti(null, false, true))
            } else {
                // چند شرط با کاما: 1, 2 ->
                var aval = true
                while (true) {
                    if (aval && faaliAst("harf", "in")) {
                        begir("harf", "in")
                        sharTha.add(HalateVaqti(bazehEbarat(), true, false))
                    } else {
                        val e = ebarat()
                        if (faaliAst("harf", "in")) {
                            begir("harf", "in")
                            val b = bazehEbarat()
                            sharTha.add(HalateVaqti(InChap(e, b, t.khat), false, false))
                        } else {
                            sharTha.add(HalateVaqti(e, false, false))
                        }
                    }
                    aval = false
                    if (faaliAst("amalgar", ",")) begir("amalgar", ",") else break
                }
            }
            begir("amalgar", "->", "فلش ->")
            badaneHa.add(badane())
            satrHaRajKon()
        }
        begir("amalgar", "}")
        return VaqtiGereh(sojekt, sharTha, badaneHa, t.khat)
    }

    fun barayeDars(): Gereh {
        val t = begir("harf", "for")
        begir("amalgar", "(", "پرانتز باز (")
        val esm = begir("harf", tozih = "اسم شمارندهٔ حلقه").matn
        begir("harf", "in", "کلمهٔ in")
        val bazeh = ebarat()
        begir("amalgar", ")", "پرانتز بسته )")
        val badane = badane()
        return BarayeGereh(esm, bazeh, badane, t.khat)
    }

    fun taVaqtiDars(): Gereh {
        val t = begir("harf", "while")
        begir("amalgar", "(", "پرانتز باز (")
        val sharT = ebarat()
        begir("amalgar", ")", "پرانتز بسته )")
        val badane = badane()
        return TaVaqtiGereh(sharT, badane, t.khat)
    }

    // ---------- عبارت‌ها (از کم‌اولویت به پراولویت) ----------

    fun ebarat(): Gereh = yaEbarat()

    fun yaEbarat(): Gereh {
        var chap = vaEbarat()
        while (faaliAst("amalgar", "||")) {
            val t = begir("amalgar")
            chap = Dooi(chap, t.matn, vaEbarat(), t.khat)
        }
        return chap
    }

    fun vaEbarat(): Gereh {
        var chap = mosaviEbarat()
        while (faaliAst("amalgar", "&&")) {
            val t = begir("amalgar")
            chap = Dooi(chap, t.matn, mosaviEbarat(), t.khat)
        }
        return chap
    }

    fun mosaviEbarat(): Gereh {
        var chap = moghayeseEbarat()
        while (faaliAst("amalgar") && faali().matn in listOf("==", "!=")) {
            val t = begir("amalgar")
            chap = Dooi(chap, t.matn, moghayeseEbarat(), t.khat)
        }
        return chap
    }

    fun moghayeseEbarat(): Gereh {
        var chap = elvisEbarat()
        while (faaliAst("amalgar") && faali().matn in listOf("<", ">", "<=", ">=") || faaliAst("harf", "in")) {
            val t = faali()
            j++
            if (t.matn == "in") {
                chap = InChap(chap, elvisEbarat(), t.khat)
            } else {
                chap = Dooi(chap, t.matn, elvisEbarat(), t.khat)
            }
        }
        return chap
    }

    fun elvisEbarat(): Gereh {
        val chap = bazehEbarat()
        if (faaliAst("amalgar", "?:")) {
            val t = begir("amalgar")
            val rast = elvisEbarat()
            return PorsTaNolli(chap, rast, t.khat)
        }
        return chap
    }

    fun bazehEbarat(): Gereh {
        val az = jamEbarat()
        if (faaliAst("amalgar", "..") || faaliAst("harf", "until") || faaliAst("harf", "downTo")) {
            val t = faali()
            j++
            val ta = jamEbarat()
            var ghadam: Gereh? = null
            if (faaliAst("harf", "step")) {
                begir("harf", "step")
                ghadam = jamEbarat()
            }
            return BazehSaz(az, ta, t.matn, ghadam, t.khat)
        }
        return az
    }

    fun jamEbarat(): Gereh {
        var chap = zarbEbarat()
        while (faaliAst("amalgar") && faali().matn in listOf("+", "-")) {
            val t = begir("amalgar")
            chap = Dooi(chap, t.matn, zarbEbarat(), t.khat)
        }
        return chap
    }

    fun zarbEbarat(): Gereh {
        var chap = yeganehEbarat()
        while (faaliAst("amalgar") && faali().matn in listOf("*", "/", "%")) {
            val t = begir("amalgar")
            chap = Dooi(chap, t.matn, yeganehEbarat(), t.khat)
        }
        return chap
    }

    fun yeganehEbarat(): Gereh {
        val t = faali()
        if (faaliAst("amalgar", "!") || faaliAst("amalgar", "-") || faaliAst("amalgar", "+")) {
            j++
            return YeganehPish(t.matn, yeganehEbarat(), t.khat)
        }
        if (faaliAst("amalgar", "++") || faaliAst("amalgar", "--")) {
            j++
            return YeganehPish(t.matn, paspostEbarat(), t.khat)
        }
        return paspostEbarat()
    }

    fun paspostEbarat(): Gereh {
        var e = oskolEbarat()
        while (true) {
            val t = faali()
            when {
                faaliAst("amalgar", "(") -> {
                    if (e !is Moteghayyer && e !is Dastresi) {
                        throw KhataKotlin("اینجا نمی‌شود تابع صدا زد.", t.khat)
                    }
                    j++
                    val vargha = ArrayList<Gereh>()
                    satrHaRajKon()
                    if (!faaliAst("amalgar", ")")) {
                        do {
                            vargha.add(ebarat())
                            if (faaliAst("amalgar", ",")) begir("amalgar", ",")
                            satrHaRajKon()
                        } while (!faaliAst("amalgar", ")"))
                    }
                    begir("amalgar", ")", "پرانتز بسته )")
                    e = if (e is Moteghayyer) {
                        Sedazadan(e.esm, vargha, e.khat)
                    } else {
                        // متدِ یک شیء: sojekt.esm(vargha)
                        val d = e as Dastresi
                        MetodZan(d.sojekt, d.esm, vargha, d.khat, d.aman)
                    }
                }
                faaliAst("amalgar", ".") -> {
                    j++
                    val esm = begir("harf", tozih = "اسم ویژگی یا متد").matn
                    e = Dastresi(e, esm, t.khat)
                }
                faaliAst("amalgar", "?.") -> {
                    j++
                    val esm = begir("harf", tozih = "اسم ویژگی یا متد").matn
                    e = Dastresi(e, esm, t.khat, aman = true)
                }
                faaliAst("amalgar", "[") -> {
                    j++
                    val andis = ebarat()
                    begir("amalgar", "]", "براکت بسته ]")
                    e = Andis(e, andis, t.khat)
                }
                faaliAst("amalgar", "++") || faaliAst("amalgar", "--") -> {
                    j++
                    if (e !is Moteghayyer && e !is Dastresi && e !is Andis) {
                        throw KhataKotlin("بعد از ${t.matn} باید یک متغیر بیاید.", t.khat)
                    }
                    e = YeganehPas(e, t.matn, t.khat)
                }
                faaliAst("amalgar", "!!") -> {
                    j++
                    e = NemudNolli(e, t.khat)
                }
                else -> return e
            }
        }
    }

    fun oskolEbarat(): Gereh {
        val t = faali()
        return when {
            t.noo == "adad" -> {
                j++
                if (t.matn.contains('.')) Parche(Ashari(t.matn.toDouble())) else Parche(Sahih(t.matn.toInt()))
            }
            t.noo == "matn" -> {
                j++
                matnGereh(t)
            }
            faaliAst("harf", "true") || faaliAst("harf", "false") -> {
                j++
                Parche(Mantri(t.matn == "true"))
            }
            faaliAst("harf", "null") -> {
                j++
                Parche(Hich)
            }
            faaliAst("harf", "this") -> {
                j++
                Moteghayyer("this", t.khat)
            }
            faaliAst("harf", "if") -> ebaratAgar()
            faaliAst("harf", "when") -> vaqtiGereh()
            t.noo == "harf" -> {
                j++
                if (t.matn in KELIDAT && t.matn !in listOf("until", "downTo", "step")) {
                    throw KhataKotlin("کلمهٔ «${t.matn}» اینجا نمی‌تواند به کار برود.", t.khat)
                }
                Moteghayyer(t.matn, t.khat)
            }
            faaliAst("amalgar", "(") -> {
                begir("amalgar", "(")
                val e = ebarat()
                begir("amalgar", ")", "پرانتز بسته )")
                e
            }
            else -> {
                if (t.noo == "amalgar" && t.matn in listOf("}", ")", "]")) {
                    throw KhataKotlin("یه عبارت جا افتاده؛ احتمالاً پرانتز یا کاما ناقص نوشته شده.", t.khat)
                }
                throw KhataKotlin("عبارت را نتوانستم بفهمم؛ به «${if (t.noo == "tamam") "آخر کد" else t.matn}» رسیدم.", t.khat)
            }
        }
    }

    fun ebaratAgar(): EbaratAgar {
        val t = begir("harf", "if")
        begir("amalgar", "(", "پرانتز باز (")
        val sharT = ebarat()
        begir("amalgar", ")", "پرانتز بسته )")
        val va = ebarat()
        satrHaRajKon() // else می‌تواند در خط بعدی بیاید
        begir("harf", "else", "کلمهٔ else — اگر if به‌عنوان مقدار به کار می‌رود باید else هم داشته باشد")
        val vagarna = ebarat()
        return EbaratAgar(sharT, va, vagarna, t.khat)
    }

    // رشتهٔ متنی را به قطعه‌های ثابت و عبارت تبدیل می‌کنیم
    fun matnGereh(t: Yekan): ParcheMatn {
        val raw = t.matn
        val posheHa = ArrayList<PosheMatn>()
        val sabet = StringBuilder()
        var i = 0
        while (i < raw.length) {
            val c = raw[i]
            if (c == '\u0001') { // دلار خام از \$
                sabet.append('$')
                i++
            } else if (c == '$' && i + 1 < raw.length) {
                val badi = raw[i + 1]
                if (badi.isLetterOrDigit() || badi == '_') {
                    if (sabet.isNotEmpty()) {
                        posheHa.add(PosheMatn(true, sabet.toString(), null))
                        sabet.clear()
                    }
                    var esm = ""
                    var k = i + 1
                    while (k < raw.length && (raw[k].isLetterOrDigit() || raw[k] == '_')) {
                        esm += raw[k]
                        k++
                    }
                    posheHa.add(PosheMatn(false, "", Moteghayyer(esm, t.khat)))
                    i = k
                } else if (badi == '{') {
                    if (sabet.isNotEmpty()) {
                        posheHa.add(PosheMatn(true, sabet.toString(), null))
                        sabet.clear()
                    }
                    var omgh = 1
                    var k = i + 2
                    val dakhel = StringBuilder()
                    while (k < raw.length && omgh > 0) {
                        if (raw[k] == '{') omgh++
                        if (raw[k] == '}') {
                            omgh--
                            if (omgh == 0) break
                        }
                        dakhel.append(raw[k])
                        k++
                    }
                    if (omgh > 0) throw KhataKotlin("آکولاد بستهٔ \${} جا افتاده است.", t.khat)
                    val zirParsir = Parsir(Leksor(dakhel.toString()).leksorKon())
                    posheHa.add(PosheMatn(false, "", zirParsir.ebarat()))
                    i = k + 1
                } else {
                    sabet.append('$')
                    i++
                }
            } else {
                sabet.append(c)
                i++
            }
        }
        if (sabet.isNotEmpty()) posheHa.add(PosheMatn(true, sabet.toString(), null))
        return ParcheMatn(posheHa, t.khat)
    }
}

// صدازدن متد؛ جدا نگه داشتیم که در پس‌پست راحت تشخیص داده شود
class MetodZan(val sojekt: Gereh, val esm: String, val vargha: List<Gereh>, val khat: Int, val aman: Boolean = false) : Gereh()

// ++ و -- بعد از متغیر
class YeganehPas(val sojekt: Gereh, val amalgar: String, val khat: Int) : Gereh()
