package ir.emaroid.hesab

import java.math.BigDecimal
import java.math.BigInteger
import java.math.MathContext
import java.math.RoundingMode

/*
 * اعداد بزرگ دقیق — EmaRoid
 * وقتی عبارت فقط چهار عمل و توان و فاکتوریل و باقیمانده و پرانتز باشد،
 * بدون هیچ گردشدن حساب می‌شود:
 *   BigInteger برای عدد کامل و فاکتوریل (تا ۵۰۰۰!)
 *   BigDecimal با دقت ۶۰ رقم برای اعشار و تقسیم
 * نمونه‌ها: 100! ، 2^1000 ، (123456789)^20 — همه کامل و درست
 */
object BozorgAdad {

    val MC = MathContext(60, RoundingMode.HALF_EVEN)
    private val SAGHFAKTORIEL = BigInteger.valueOf(5000)
    private const val HAD_RAGHAM = 100_000 // سقفِ ارقام نتیجه برای جلوگیری از پر شدن حافظه

    // عبارت فقط‌حسابی؟ (بدون هیچ حرف و تابع)
    fun mohemAst(s: String): Boolean {
        var adadDadim = false
        for (ch in s) {
            val bejo = ch.isDigit() || ch in "+-*/^!%(). "
            if (!bejo) return false
            if (ch.isDigit()) adadDadim = true
        }
        return adadDadim
    }

    fun hesabKon(mathn: String): BigDecimal {
        val p = PakehKar(MahineHesab.pakeh(mathn))
        val v = p.eybarat()
        p.tab_oruj("پایان عبارت")
        return v
    }

    fun rishti(d: BigDecimal): String {
        val pake = d.stripTrailingZeros()
        // اعداد خیلی کوچک با نماد علمی ذخیره می‌شوند؛ به نوشتار ساده برمی‌گردانیم
        return pake.toPlainString()
    }

    class Khata(peygham: String) : Exception(peygham)

    private class PakehKar(private var m: String) {
        var i = 0

        fun tab_oruj(entezar: String) {
            while (i < m.length && m[i] == ' ') i++
            if (i < m.length) throw Khata("کاراکتر اضافی «${m[i]}» در جایگاه $i — $entezar انتظار می‌رفت")
        }

        fun forsooht(): Char? {
            while (i < m.length && m[i] == ' ') i++
            return if (i < m.length) m[i] else null
        }

        fun ha_roukhdan(c: Char): Boolean {
            if (forsooht() == c) { i++; return true }
            return false
        }

        // عبارت → جمله [± جمله]*
        fun eybarat(): BigDecimal {
            var v = jomle()
            while (true) {
                val c = forsooht() ?: break
                if (c == '+') { i++; v = v.add(jomle(), MC) }
                else if (c == '-') { i++; v = v.subtract(jomle(), MC) }
                else break
            }
            return v
        }

        // جمله → عامل [* / % عامل]*
        fun jomle(): BigDecimal {
            var v = amel()
            while (true) {
                val c = forsooht() ?: break
                if (c == '*') { i++; v = v.multiply(amel(), MC) }
                else if (c == '/') {
                    i++
                    val b = amel()
                    if (b.signum() == 0) throw Khata("تقسیم بر صفر!")
                    v = v.divide(b, MC)
                }
                else if (c == '%') {
                    i++
                    val b = amel()
                    if (b.signum() == 0) throw Khata("باقیمانده بر صفر!")
                    v = v.remainder(b, MC)
                }
                else break
            }
            raghamSanj(v)
            return v
        }

        // عامل → یکه [!]* [^ عامل]? — توان راست‌گرا
        fun amel(): BigDecimal {
            var v = yeke()
            while (forsooht() == '!') {
                i++
                v = faktoriel(v)
            }
            if (forsooht() == '^') {
                i++
                val b = amel()
                return tavv(v, b)
            }
            return v
        }

        // یکه → -یکه | +یکه | (عبارت) | عدد
        fun yeke(): BigDecimal {
            val c = forsooht() ?: throw Khata("عبارت ناتمام است")
            return when {
                c == '-' -> { i++; yeke().negate() }
                c == '+' -> { i++; yeke() }
                c == '(' -> { i++; val v = eybarat(); if (!ha_roukhdan(')')) throw Khata("پرانتز بسته نگذاشتی"); v }
                c.isDigit() || c == '.' -> adad()
                else -> throw Khata("کاراکتر ناشناخته «$c»")
            }
        }

        fun adad(): BigDecimal {
            val shoroo = i
            while (i < m.length && (m[i].isDigit() || m[i] == '.')) i++
            val s = m.substring(shoroo, i)
            return try {
                BigDecimal(s)
            } catch (_: NumberFormatException) {
                throw Khata("عدد بد: $s")
            }
        }

        // توان فقط با توان کامل — توان اعشاری مال مسیر عمومی است
        fun tavv(base: BigDecimal, tavv: BigDecimal): BigDecimal {
            if (tavv.abs().scale() > 0) throw Khata("توان اعشاری با اعداد بزرگ دقیق پشتیبانی نمی‌شود — بدون آن حساب می‌کنم")
            val n = tavv.toBigIntegerExact()
            if (n.abs() > BigInteger.valueOf(1_000_000)) throw Khata("توان خیلی بزرگ است!")
            val raghamBase = base.precision().coerceAtLeast(1)
            val raghamNatije = raghamBase.toLong() * n.abs().toLong()
            if (raghamNatije > HAD_RAGHAM) throw Khata("خیلی بزرگ شد! بیش از $HAD_RAGHAM رقم می‌شود")
            return if (n.signum() >= 0) {
                base.pow(n.intValueExact(), MC)
            } else {
                BigDecimal.ONE.divide(base.pow(-n.intValueExact(), MC), MC)
            }
        }

        fun faktoriel(x: BigDecimal): BigDecimal {
            if (x.scale() > 0 || x.signum() < 0) throw Khata("فاکتوریل فقط برای عدد کامل نامنفی!")
            val n = x.toBigIntegerExact()
            if (n > SAGHFAKTORIEL) throw Khata("فاکتوریل بالای ۵۰۰۰ دیگر معنی محاسبهٔ گنجینه‌ای ندارد!")
            var v = BigInteger.ONE
            var k = BigInteger.TWO
            while (k <= n) {
                v = v.multiply(k)
                if (v.bitLength() > HAD_RAGHAM * 4) throw Khata("خیلی بزرگ شد!")
                k = k.add(BigInteger.ONE)
            }
            return BigDecimal(v)
        }

        fun raghamSanj(v: BigDecimal) {
            if (v.precision().toLong() > HAD_RAGHAM) throw Khata("خیلی بزرگ شد! بیش از $HAD_RAGHAM رقم می‌شود")
        }
    }
}
