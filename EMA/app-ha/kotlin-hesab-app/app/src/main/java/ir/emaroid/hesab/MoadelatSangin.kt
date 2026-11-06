package ir.emaroid.hesab

import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sqrt

/*
 * معادلات سنگین — EmaRoid
 * درجهٔ ۳ (کاردانو)، درجهٔ ۴ و بالاتر (دوران-کرنر)، و حل عددی f(x)=0
 * با مراحل فارسی؛ ورودیِ عددی = ضرایب با کاما، از بالاترین درجه
 * نمونه: 1,-5,6 برای x² − 5x + 6 = 0 → x=2 ، x=3
 */
object MoadelatSangin {

    data class Hal(val marahel: List<String>, val javabHa: List<String>)

    // ---------- عددشناسی مختلط ----------
    data class Morakkab(val re: Double, val im: Double) {
        operator fun plus(o: Morakkab) = Morakkab(re + o.re, im + o.im)
        operator fun minus(o: Morakkab) = Morakkab(re - o.re, im - o.im)
        operator fun times(o: Morakkab) = Morakkab(re * o.re - im * o.im, re * o.im + im * o.re)
        operator fun div(o: Morakkab): Morakkab {
            val d = o.re * o.re + o.im * o.im
            return Morakkab((re * o.re + im * o.im) / d, (im * o.re - re * o.im) / d)
        }
        val tol: Double get() = sqrt(re * re + im * im)
        fun harfi(): String {
            val r = MahineHesab.rishti(re)
            return if (abs(im) < 1e-9) r
            else "${r} ${if (im > 0) "+" else "−"} ${MahineHesab.rishti(abs(im))}i"
        }
    }

    // ---------- ورودی: ضرایب ----------
    fun bezanInput(s: String): List<Double> {
        val t = MahineHesab.pakeh(s).replace("،", ",")
        val jz = t.split(",").map { part ->
            part.trim().toDoubleOrNull()
                ?: throw MahineHesab.Khata("ضریب «${part.trim()}» عدد نیست")
        }
        if (jz.size < 2) throw MahineHesab.Khata("حداقل دو ضریب لازم است (مثلاً 2,-1 برای 2x − 1 = 0)")
        if (jz.first() == 0.0) throw MahineHesab.Khata("ضریب بالاترین درجه صفر است — درجه را کم کن")
        if (jz.any { abs(it) > 1e15 }) throw MahineHesab.Khata("ضریب‌ها خیلی بزرگ‌اند")
        return jz
    }

    // ---------- درجهٔ ۳: کاردانو ----------
    fun seDaji(a0: Double, b: Double, c: Double, d: Double): Hal {
        val marahel = mutableListOf<String>()
        marahel.add("۱) معادلهٔ درجهٔ سوم: ${MahineHesab.rishti(a0)}x³ + ${MahineHesab.rishti(b)}x² + ${MahineHesab.rishti(c)}x + ${MahineHesab.rishti(d)} = 0")
        if (abs(a0) < 1e-15) return doDaji(b, c, d)
        val marhaleA = "۲) با تقسیم بر ضریب x³، معادله را نرمال می‌کنیم (روش کاردانو)."
        marahel.add(marhaleA)
        val b1 = b / a0
        val c1 = c / a0
        val d1 = d / a0
        // افت‌انداز: x = t − b1/3 → t³ + pt + q = 0
        val p = c1 - b1 * b1 / 3.0
        val q = 2.0 * b1 * b1 * b1 / 27.0 - b1 * c1 / 3.0 + d1
        marahel.add("۳) با جای‌گذاری x = t − ${MahineHesab.rishti(b1 / 3.0)} شکل افت‌انداز: t³ + ${MahineHesab.rishti(p)}t + ${MahineHesab.rishti(q)} = 0")
        val delta = (q / 2.0) * (q / 2.0) + (p / 3.0) * (p / 3.0) * (p / 3.0)
        val javabHa = mutableListOf<String>()
        val tHa = mutableListOf<Double>()
        if (delta > 1e-12) {
            marahel.add("۴) مبیّن کاردانو مثبت است ⇒ یک ریشهٔ حقیقی و یک جفت مختلط.")
            val s = sqrt(delta)
            val u = Math.cbrt(-q / 2.0 + s)
            val v = Math.cbrt(-q / 2.0 - s)
            val t = u + v
            tHa.add(t)
            val qism = -t / 2.0
            val soz = (sqrt(3.0) / 2.0) * (u - v)
            javabHa.add("x₁ = ${MahineHesab.rishti(t - b1 / 3.0)}")
            javabHa.add("x₂ = ${MahineHesab.rishti(qism - b1 / 3.0)} + ${MahineHesab.rishti(abs(soz))}i")
            javabHa.add("x₃ = ${MahineHesab.rishti(qism - b1 / 3.0)} − ${MahineHesab.rishti(abs(soz))}i")
        } else {
            // سه ریشهٔ حقیقی — روش مثلثاتی
            marahel.add("۴) مبیّن منفی یا صفر است ⇒ سه ریشهٔ حقیقی دارد (فرم مثلثاتی).")
            val m = 2.0 * sqrt(-p / 3.0)
            val zaviye = acos((3.0 * q / (2.0 * p)) * sqrt(-3.0 / p)) / 3.0
            for (k in 0..2) {
                val t = m * cos(zaviye - 2.0 * Math.PI * k / 3.0)
                tHa.add(t)
            }
            tHa.forEachIndexed { k, t -> javabHa.add("x${k + 1} = ${MahineHesab.rishti(t - b1 / 3.0)}") }
        }
        marahel.add("۵) هر ریشه با یک گام نیوتون صیقل شد تا دقت ماشین بالا برود.")
        val nagha = tHa.map { t -> eyb(t - b1 / 3.0, a0, b, c, d) }
        tHa.forEachIndexed { k, t ->
            javabHa[k] = "x${k + 1} = ${MahineHesab.rishti(nagha[k])}"
        }
        return Hal(marahel, javabHa)
    }

    // ---------- درجهٔ ۲ (پشتیبان) ----------
    private fun doDaji(a: Double, b: Double, c: Double): Hal {
        val marahel = mutableListOf("ضریب x³ صفر شد ⇒ در واقع درجهٔ دوم است.")
        val delta = b * b - 4 * a * c
        marahel.add("دلتا: Δ = ${MahineHesab.rishti(delta)}")
        val javabHa = mutableListOf<String>()
        if (delta >= 0) {
            val r = sqrt(delta)
            val x1 = (-b + r) / (2 * a)
            val x2 = (-b - r) / (2 * a)
            javabHa.add("x₁ = ${MahineHesab.rishti(x1)}")
            javabHa.add(if (delta > 0) "x₂ = ${MahineHesab.rishti(x2)}" else "(مضاعف)")
        } else {
            val r = sqrt(-delta)
            val kh = -b / (2 * a)
            javabHa.add("x = ${MahineHesab.rishti(kh)} ± ${MahineHesab.rishti(r / (2 * a))}i")
        }
        return Hal(marahel, javabHa)
    }

    // ---------- درجهٔ ۴ و بالاتر: دوران-کرنر ----------
    fun polinomi(jz: List<Double>): Hal {
        val daraje = jz.size - 1
        val marahel = mutableListOf<String>()
        marahel.add("۱) چندجمله‌ای درجهٔ $daraje با ضرایب ${jz.joinToString(" ، ") { MahineHesab.rishti(it) }}")
        marahel.add("۲) روش دوران-کرنر: همهٔ ریشه‌ها هم‌زمان با تکرارِ ${"rᵏ ← rᵏ − P(rᵏ)/∏(rᵏ−rⱼ)"} به جواب نزدیک می‌شوند.")
        // مونیک‌سازی
        val a0 = jz.first()
        val n = jz.map { it / a0 }.toDoubleArray() // n[0]=1 ... n[daraje]=ثابت
        // ابتدا ریشه‌های اولیه
        var riha = Array(daraje) { k -> Morakkab(0.4 + 0.9 * k / daraje, 0.9 * ((k % 2) * 2 - 1) + 0.1) }
        var tekrar = 0
        var khataAkhar = 1.0
        while (tekrar < 300) {
            var bozorgtarin = 0.0
            val no = arrayOfNulls<Morakkab>(daraje)
            for (k in 0 until daraje) {
                var makhraj = Morakkab(1.0, 0.0)
                for (j in 0 until daraje) if (j != k) makhraj = makhraj * (riha[k] - riha[j])
                no[k] = riha[k] - polinomiSanj(riha[k], n) / makhraj
            }
            for (k in 0 until daraje) {
                val tavan = no[k]!! - riha[k]
                if (tavan.tol > bozorgtarin) bozorgtarin = tavan.tol
                riha[k] = no[k]!!
            }
            khataAkhar = bozorgtarin
            tekrar++
            if (bozorgtarin < 1e-13) break
        }
        marahel.add("۳) بعد از $tekrar تکرار، دقت به ${MahineHesab.rishti(khataAkhar)} رسید.")
        marahel.add("۴) ریشه‌ها مرتب شدند؛ حقیقی‌ها بدون i و مختلط‌ها با i نشان داده می‌شوند.")
        val javabHa = riha.sortedBy { it.re }.map { "x = ${it.harfi()}" }
        return Hal(marahel, javabHa)
    }

    private fun polinomiSanj(x: Morakkab, n: DoubleArray): Morakkab {
        var v = Morakkab(0.0, 0.0)
        for (c in n) v = v * x + Morakkab(c, 0.0)
        return v
    }

    // ---------- حل عددی f(x)=0 ----------
    fun adadi(ebarat: String): Hal {
        val m = MahineHesab.pakeh(ebarat)
        if (m.lowercase().contains('=')) throw MahineHesab.Khata("فقط سمت چپ را بنویس: f(x) — علامت = لازم نیست چون با صفر مقایسه می‌شود")
        val marahel = mutableListOf<String>()
        marahel.add("۱) تابع f(x) = $m — با پارسی زنی همان موتور ماشین‌حساب (sin، ln، √ و… کار می‌کند).")
        marahel.add("۲) توابع مثلثاتی در حل عددی همیشه با رادیان سنجیده می‌شوند (قرارداد ریاضی).")
        // پیمایش بازهٔ [−۶۰، ۶۰]
        val darajeQabli = MahineHesab.daraje
        MahineHesab.daraje = false // حل عددی فقط رادیان
        try {
        val gom = 0.005
        val shoroo = -60.0
        val payan = 60.0
        var ghabli: Double? = null
        val maghale = mutableListOf<Double>()
        var v = shoroo
        while (v <= payan) {
            val f = fSanj(v, m)
            if (f != null) {
                val q = ghabli
                if (q != null && ((f >= 0 && q < 0) || (f < 0 && q >= 0))) maghale.add(v)
                if (abs(f) < 1e-13) maghale.add(v) // صفرِ دقیق
                ghabli = f
            } else {
                ghabli = null // تابع اینجا تعریف نشده (مثل ln(x) یا تقسیم بر صفر)
            }
            v += gom
        }
        if (maghale.isEmpty()) throw MahineHesab.Khata("در بازهٔ [−۶۰، ۶۰] تغییر علامتی پیدا نکردم — بازه را با ∙ یا ضریب جابه‌جا کن")
        marahel.add("۳) پیمایش با گام ۰٫۰۰۵ انجام شد؛ ${maghale.size} نقطهٔ تغییر علامت دیدیم.")
        val javabHa = mutableListOf<String>()
        var shomar = 0
        val peydaShode = mutableListOf<Double>()
        for (gh in maghale) {
            if (shomar >= 12) break
            val r = nesfSazi(m, gh - gom, gh) ?: continue
            val deraz = peydaShode.any { abs(it - r) < 1e-6 }
            if (deraz) continue
            peydaShode.add(r)
            shomar++
            val rS = MahineHesab.rishti(r + 0.0)
            javabHa.add("x = ${if (rS == "-0") "0" else rS}")
        }
        if (javabHa.isEmpty()) throw MahineHesab.Khata("ریشه‌ها پیدا شدند ولی تکراری بودند — بازه یا گام را عوض کن")
        marahel.add("۴) هر بازه با روش نصف‌سازی تا دقت ۱۲ رقم ریز شد.")
        return Hal(marahel, javabHa)
        } finally {
            MahineHesab.daraje = darajeQabli
            MahineHesab.moteqayyerHa = emptyMap()
        }
    }

    private fun fSanj(v: Double, m: String): Double? {
        return try {
            MahineHesab.moteqayyerHa = mapOf("x" to v)
            MahineHesab.hesabKon(m)
        } catch (_: Exception) {
            null
        }
    }

    private fun nesfSazi(m: String, a0: Double, b0: Double): Double? {
        var a = a0
        var b = b0
        var fa = fSanj(a, m) ?: return null
        var fb = fSanj(b, m) ?: return null
        if (fa == 0.0) return a
        if (fb == 0.0) return b
        if ((fa < 0) == (fb < 0)) return null
        for (k in 0 until 120) {
            val vasat = (a + b) / 2.0
            val f = fSanj(vasat, m) ?: return null
            if (f == 0.0 || (b - a) < 1e-13) return vasat
            if ((f < 0) == (fa < 0)) { a = vasat; fa = f } else { b = vasat; fb = f }
        }
        return (a + b) / 2.0 + 0.0 // +0.0 تا منفی‌صفر پاک شود
    }

    // یک گام نیوتون برای صیقل ریشهٔ درجهٔ ۳
    private fun eyb(x: Double, a: Double, b: Double, c: Double, d: Double): Double {
        var t = x
        for (k in 0 until 3) {
            val f = ((a * t + b) * t + c) * t + d
            val df = (3 * a * t + 2 * b) * t + c
            if (abs(df) < 1e-14) break
            t -= f / df
        }
        return t
    }
}
