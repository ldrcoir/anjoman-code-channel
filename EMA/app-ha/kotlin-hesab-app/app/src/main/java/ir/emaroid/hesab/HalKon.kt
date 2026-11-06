package ir.emaroid.hesab

/*
 * حل‌کن مرحله‌به‌مرحلهٔ معادله — EmaRoid
 * معادلهٔ خطی ax + b = 0 و درجهٔ دوم ax² + bx + c = 0
 * خروجی: فهرست مراحل فارسی + جواب نهایی
 */
object HalKon {

    data class Hal(val marahel: List<String>, val javabHa: List<String>)

    fun halKon(moadele: String): Hal {
        val m = moadele.replace(" ", "").lowercase()
        if (!m.contains("=")) throw MahineHesab.Khata("معادله باید «=» داشته باشد")
        val doTarafe = m.split("=")
        if (doTarafe.size != 2) throw MahineHesab.Khata("فقط یک «=» مجاز است")
        val chap = doTarafe[0]
        val rast = doTarafe[1]

        val dar2 = (chap + rast).replace("x*x", "x^2").contains("x^2")
        return if (dar2) {
            val (a, b, c) = movaze2(chap, rast)
            if (a == 0.0) {
                // در واقع خطی بود
                val (bLin, cLin) = movazeLin(chap, rast)
                halLinear(bLin, cLin)
            } else {
                halDovom(a, b, c)
            }
        } else {
            val (a, b) = movazeLin(chap, rast)
            if (a == 0.0 && b == 0.0) return Hal(
                listOf("معادله را ساده کردیم و همهٔ جمله‌ها حذف شدند."),
                listOf("بی‌شمار جواب (هر عددی درست است)")
            )
            if (a == 0.0) throw MahineHesab.Khata("ضریب x صفر شد؛ این معادله جواب ندارد (تناقض)")
            halLinear(a, b)
        }
    }

    // ---------- درجهٔ دوم ----------
    fun movaze2(chap: String, rast: String): Triple<Double, Double, Double> {
        val (a1, b1, c1) = tajzie2(chap, +1)
        val (a2, b2, c2) = tajzie2(rast, +1)
        return Triple(a1 - a2, b1 - b2, c1 - c2)
    }

    fun movazeLin(chap: String, rast: String): Pair<Double, Double> {
        val (a1, c1) = tajzie(chap)
        val (a2, c2) = tajzie(rast)
        return Pair(a1 - a2, c1 - c2)
    }

    fun tajzie2(e: String, alamat: Int): Triple<Double, Double, Double> {
        val m = e.replace(" ", "").lowercase().replace("x*x", "x^2")
        var a = 0.0; var b = 0.0; var c = 0.0
        var i = 0
        while (i < m.length) {
            var al = alamat.toDouble()
            while (i < m.length && (m[i] == '+' || m[i] == '-')) {
                if (m[i] == '-') al = -al
                i++
            }
            if (i >= m.length) break
            var adad = 1.0
            if (m[i].isDigit() || m[i] == '.') {
                val sh = i
                while (i < m.length && (m[i].isDigit() || m[i] == '.')) i++
                adad = m.substring(sh, i).toDouble()
            }
            if (i < m.length && m[i] == 'x') {
                i++
                if (i < m.length && m[i] == '^') {
                    i++
                    if (i >= m.length || m[i] != '2') throw MahineHesab.Khata("فقط توان ۲ پشتیبانی می‌شود")
                    i++
                    a += al * adad
                } else {
                    b += al * adad
                }
                continue
            }
            c += al * adad
        }
        return Triple(a, b, c)
    }

    // ---------- خطی: تجزیهٔ (ضریب x ، ثابت) ----------
    // پشتیبانی: 3x+2، -x+1، 2(x+3)-4، x/2+1
    fun tajzie(ebarat: String): Pair<Double, Double> {
        val m = ebarat.replace(" ", "").lowercase()
        var a = 0.0
        var c = 0.0
        var i = 0
        while (i < m.length) {
            var al = 1.0
            while (i < m.length && (m[i] == '+' || m[i] == '-')) {
                if (m[i] == '-') al = -al
                i++
            }
            if (i >= m.length) break
            var adad = 1.0
            var adadDadim = false
            if (m[i].isDigit() || m[i] == '.') {
                val sh = i
                while (i < m.length && (m[i].isDigit() || m[i] == '.')) i++
                adad = m.substring(sh, i).toDouble()
                adadDadim = true
            }
            if (i < m.length && m[i] == 'x') {
                i++
                a += al * adad
                continue
            }
            if (i < m.length && m[i] == '(') {
                i++
                // پرانتز: فقط شکل ساده ax+b — پایان پرانتز برمی‌گردد
                val (pa, pc, payan) = tajzieDaron(m, i)
                a += al * adad * pa
                c += al * adad * pc
                i = payan
                continue
            }
            if (i < m.length && m[i] == '/') {
                i++
                val sh = i
                while (i < m.length && (m[i].isDigit() || m[i] == '.')) i++
                if (sh == i) throw MahineHesab.Khata("بعد از / عدد بیاور")
                val magh = m.substring(sh, i).toDouble()
                if (magh == 0.0) throw MahineHesab.Khata("تقسیم بر صفر")
                if (adadDadim) {
                    // عدد/عدد → ثابت
                    c += al * adad / magh
                } else {
                    // x/عدد → ضریب کسری
                    a += al / magh
                }
                continue
            }
            if (adadDadim) {
                c += al * adad
            } else {
                throw MahineHesab.Khata("جملهٔ «${m[i]}» را نمی‌فهمم — فعلاً معادلهٔ خطی ساده پشتیبانی می‌شود")
            }
        }
        return Pair(a, c)
    }

    // پرانتز داخلی تا پرانتز بسته؛ (ضریب، ثابت، اندیس پس از «)»)
    fun tajzieDaron(m: String, shoroo: Int): Triple<Double, Double, Int> {
        var tafavot = 0
        var i = shoroo
        while (i < m.length) {
            if (m[i] == '(') tafavot++
            if (m[i] == ')') {
                if (tafavot == 0) break
                tafavot--
            }
            i++
        }
        if (i >= m.length) throw MahineHesab.Khata("پرانتز بسته نگذاشتی")
        val daron = m.substring(shoroo, i)
        if (daron.contains("x^2") || daron.contains("x*x"))
            throw MahineHesab.Khata("درجهٔ دوم داخل پرانتز فعلاً پشتیبانی نمی‌شود")
        val (a, c) = tajzie(daron)
        return Triple(a, c, i + 1)
    }

    // ---------- مراحل ----------
    private fun halLinear(a: Double, b: Double): Hal {
        val marahel = mutableListOf<String>()
        marahel.add("۱) معادله را به شکل ax + b = 0 نوشتیم؛ ضریب x برابر ${MahineHesab.rishti(a)} و عدد ثابت برابر ${MahineHesab.rishti(b)} شد.")
        marahel.add("۲) عدد ثابت را به آن‌سوی تساوی می‌بریم: ${MahineHesab.rishti(a)}x = ${MahineHesab.rishti(-b)}")
        marahel.add("۳) دو طرف را بر ضریب x یعنی ${MahineHesab.rishti(a)} تقسیم می‌کنیم.")
        val javab = -b / a
        marahel.add("۴) جواب: x = ${MahineHesab.rishti(javab)}")
        return Hal(marahel, listOf("x = ${MahineHesab.rishti(javab)}"))
    }

    private fun halDovom(a2: Double, b2: Double, c2: Double): Hal {
        val marahel = mutableListOf<String>()
        marahel.add("۱) معادله را به شکل استاندارد ax² + bx + c = 0 مرتب کردیم:")
        marahel.add("    a = ${MahineHesab.rishti(a2)} ، b = ${MahineHesab.rishti(b2)} ، c = ${MahineHesab.rishti(c2)}")
        val delta = b2 * b2 - 4 * a2 * c2
        marahel.add("۲) دلتا: Δ = b² − 4ac = ${MahineHesab.rishti(b2)}² − 4×${MahineHesab.rishti(a2)}×${MahineHesab.rishti(c2)} = ${MahineHesab.rishti(delta)}")
        if (delta > 1e-12) {
            val r = Math.sqrt(delta)
            val x1 = (-b2 + r) / (2 * a2)
            val x2 = (-b2 - r) / (2 * a2)
            marahel.add("۳) Δ مثبت است ⇒ دو جواب دارد.")
            marahel.add("۴) x = (−b ± √Δ) ÷ (2a)")
            marahel.add("۵) x₁ = ${MahineHesab.rishti(x1)} ، x₂ = ${MahineHesab.rishti(x2)}")
            return Hal(marahel, listOf("x₁ = ${MahineHesab.rishti(x1)}", "x₂ = ${MahineHesab.rishti(x2)}"))
        }
        if (Math.abs(delta) <= 1e-12) {
            val x = -b2 / (2 * a2)
            marahel.add("۳) Δ صفر است ⇒ یک جواب مضاعف دارد.")
            marahel.add("۴) x = −b ÷ 2a = ${MahineHesab.rishti(x)}")
            return Hal(marahel, listOf("x = ${MahineHesab.rishti(x)} (مضاعف)"))
        }
        val r = Math.sqrt(-delta)
        val kh = -b2 / (2 * a2)
        marahel.add("۳) Δ منفی است ⇒ جواب حقیقی ندارد (دو جواب مختلط).")
        marahel.add("۴) x = ${MahineHesab.rishti(kh)} ± ${MahineHesab.rishti(r / (2 * a2))}i")
        return Hal(marahel, listOf("x = ${MahineHesab.rishti(kh)} ± ${MahineHesab.rishti(r / (2 * a2))}i"))
    }
}
