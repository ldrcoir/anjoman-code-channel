package ir.emaroid.hesab

/*
 * موتور ماشین‌حساب EmaRoid — خالص کاتلین، بدون هیچ وابستگی
 * تجزیه‌گر بازگشتی نزولی:
 *   عبارت → جمله [± جمله]*
 *   جمله  → عامل [×÷% عامل]*
 *   عامل  → توان [! یا ^ عامل]*
 *   یکه   → -یکه | تابع(عبارت) | عدد | ثابت | (عبارت)
 * پشتیبانی: sin cos tan asin acos atan ln log sqrt abs exp
 * ثابت‌ها: pi , e   |  کاراکتر «!» فاکتوریل  |  ^
 * حالت درجه/رادیان برای توابع مثلثاتی
 */
object MahineHesab {

    var daraje: Boolean = true // true یعنی توابع مثلثاتی با درجه کار می‌کنند

    // ---------- ابزار عمومی ----------
    fun faghatAdad(s: String): Boolean {
        if (s.isBlank()) return false
        return runCatching { pakeh(s).toDouble() }.isSuccess
    }

    fun pakeh(s: String): String {
        val sb = StringBuilder()
        for (ch in s) {
            when (ch) {
                in '۰'..'۹' -> sb.append(('0' + (ch - '۰')).toChar())
                in '٠'..'٩' -> sb.append(('0' + (ch - '٠')).toChar())
                '×', '∗' -> sb.append('*')
                '÷' -> sb.append('/')
                '−', '–', '—' -> sb.append('-')
                '،' -> sb.append(',')
                ' ' -> {}
                else -> sb.append(ch)
            }
        }
        return sb.toString()
    }

    fun hesabKon(mathn: String): Double {
        val e = PakehKar(pakeh(mathn))
        val natije = e.eybarat()
        e.tab_oruj("پایان عبارت")
        return natije
    }

    fun natijeRishti(mathn: String): String {
        return rishti(hesabKon(mathn))
    }

    fun rishti(d: Double): String {
        return if (d == d.toLong().toDouble() && Math.abs(d) < 1e15) {
            d.toLong().toString()
        } else {
            val s = String.format(java.util.Locale.US, "%.8f", d).trimEnd('0').trimEnd('.')
            s
        }
    }

    // ---------- خطاها ----------
    class Khata(peygham: String) : Exception(peygham)

    // ---------- تجزیه‌گر ----------
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

        fun bekhon(): Char {
            val c = forsooht() ?: throw Khata("انتظار نویسه داشتم ولی متن تمام شد")
            i++
            return c
        }

        fun ha_roukhdan(c: Char): Boolean {
            if (forsooht() == c) { i++; return true }
            return false
        }

        // عبارت → جمله [± جمله]*
        fun eybarat(): Double {
            var v = jomle()
            while (true) {
                val c = forsooht() ?: break
                if (c == '+') { i++; v += jomle() }
                else if (c == '-') { i++; v -= jomle() }
                else break
            }
            return v
        }

        // جمله → عامل [* / % عامل]*
        fun jomle(): Double {
            var v = amel()
            while (true) {
                val c = forsooht() ?: break
                if (c == '*') { i++; v *= amel() }
                else if (c == '/') {
                    i++
                    val b = amel()
                    if (b == 0.0) throw Khata("تقسیم بر صفر!")
                    v /= b
                }
                else if (c == '%') {
                    i++
                    val b = amel()
                    if (b == 0.0) throw Khata("باقیمانده بر صفر!")
                    v %= b
                }
                else break
            }
            return v
        }

        // عامل → یکه [!]* [^ عامل]? — توان راست‌گرا: 2^3^2 = 2^(3^2) = 512
        fun amel(): Double {
            var v = yeke()
            while (forsooht() == '!') {
                i++
                v = faktoriel(v)
            }
            if (forsooht() == '^') {
                i++
                val b = amel()  // بقیهٔ زنجیرهٔ توان اول ارزیابی می‌شود
                return Math.pow(v, b)
            }
            return v
        }

        // یکه → -یکه | +یکه | تابع(...) | عدد | ثابت | (عبارت)
        fun yeke(): Double {
            val c = forsooht() ?: throw Khata("عبارت ناتمام است")
            return when {
                c == '-' -> { i++; -yeke() }
                c == '+' -> { i++; yeke() }
                c == '(' -> { i++; val v = eybarat(); if (!ha_roukhdan(')')) throw Khata("پرانتز بسته نگذاشتی"); v }
                c.isDigit() || c == '.' -> adad()
                c.isLetter() -> esm()
                else -> throw Khata("کاراکتر ناشناخته «$c»")
            }
        }

        fun esm(): Double {
            val shoroo = i
            while (i < m.length && (m[i].isLetter())) i++
            val esm = m.substring(shoroo, i).lowercase()
            // فاکتوریل پارامتری مثل 5! از عامل می‌آید
            return when (esm) {
                "pi", "π" -> Math.PI
                "e" -> Math.E
                "sin", "cos", "tan", "asin", "acos", "atan", "ln", "log", "sqrt", "abs", "exp" -> {
                    // پرانتز اختیاری نیست
                    if (!ha_roukhdan('(')) throw Khata("بعد از $esm باید پرانتز بگذاری")
                    val x = eybarat()
                    if (!ha_roukhdan(')')) throw Khata("پرانتز بسته نگذاشتی")
                    tabe(esm, x)
                }
                else -> throw Khata("چیزی به اسم «$esm» نمی‌شناسم")
            }
        }

        fun tabe(esm: String, x: Double): Double {
            fun beRad(v: Double) = if (daraje) Math.toRadians(v) else v
            fun azRad(v: Double) = if (daraje) Math.toDegrees(v) else v
            return when (esm) {
                "sin" -> Math.sin(beRad(x))
                "cos" -> Math.cos(beRad(x))
                "tan" -> Math.tan(beRad(x))
                "asin" -> azRad(Math.asin(x))
                "acos" -> azRad(Math.acos(x))
                "atan" -> azRad(Math.atan(x))
                "ln" -> { if (x <= 0) throw Khata("لگاریتم فقط برای عدد مثبت!"); Math.log(x) }
                "log" -> { if (x <= 0) throw Khata("لگاریتم فقط برای عدد مثبت!"); Math.log10(x) }
                "sqrt" -> { if (x < 0) throw Khata("جذر عدد منفی در اعداد واقعی نیست"); Math.sqrt(x) }
                "abs" -> Math.abs(x)
                "exp" -> Math.exp(x)
                else -> throw Khata("تابع ناشناخته $esm")
            }
        }

        fun adad(): Double {
            val shoroo = i
            while (i < m.length && (m[i].isDigit() || m[i] == '.')) i++
            val s = m.substring(shoroo, i)
            return s.toDoubleOrNull() ?: throw Khata("عدد بد: $s")
        }
    }

    fun faktoriel(x: Double): Double {
        if (x < 0 || x != x.toLong().toDouble()) throw Khata("فاکتوریل فقط برای عدد کامل نا منفی!")
        if (x > 170) throw Khata("خیلی بزرگ شد!")
        var v = 1.0
        for (k in 2..x.toLong()) v *= k
        return v
    }
}
