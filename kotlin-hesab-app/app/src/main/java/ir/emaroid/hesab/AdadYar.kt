package ir.emaroid.hesab

/*
 * ابزار عددی EmaRoid — اعداد اول، تجزیه، ب.م.م، Collatz، مبناها
 * همه خالص کاتلین و برای آزمون واحد جدا شده
 */
object AdadYar {

    fun avalAst(n: Long): Boolean {
        if (n < 2) return false
        if (n < 4) return true
        if (n % 2 == 0L) return false
        var i = 3L
        while (i * i <= n) {
            if (n % i == 0L) return false
            i += 2
        }
        return true
    }

    // تجزیه به عوامل اول: ۱۲ → [(2,2),(3,1)]
    fun tajzieAval(n0: Long): List<Pair<Long, Int>> {
        var n = n0
        val kh = mutableListOf<Pair<Long, Int>>()
        if (n < 2) return kh
        var c = 0
        while (n % 2 == 0L) { n /= 2; c++ }
        if (c > 0) kh.add(Pair(2L, c))
        var p = 3L
        while (p * p <= n) {
            c = 0
            while (n % p == 0L) { n /= p; c++ }
            if (c > 0) kh.add(Pair(p, c))
            p += 2
        }
        if (n > 1) kh.add(Pair(n, 1))
        return kh
    }

    fun maghsumHa(n: Long): List<Long> {
        val kh = mutableListOf<Long>()
        var i = 1L
        while (i * i <= n) {
            if (n % i == 0L) {
                kh.add(i)
                if (i != n / i) kh.add(n / i)
            }
            i++
        }
        return kh.sorted()
    }

    fun bmm(a0: Long, b0: Long): Long {
        var a = Math.abs(a0); var b = Math.abs(b0)
        while (b != 0L) { val t = a % b; a = b; b = t }
        return a
    }

    fun kmm(a: Long, b: Long): Long {
        if (a == 0L || b == 0L) return 0
        return Math.abs(a) / bmm(a, b) * Math.abs(b)
    }

    /*
     * دنبالهٔ Collatz:
     *   عدد زوج → نصفش کن
     *   عدد فرد → ۳n + ۱
     * فرض معروف: همیشه به ۱ می‌رسد (حدس کولاتز — هنوز کسی ثابت نکرده!)
     */
    fun collatz(n0: Long): List<Long> {
        if (n0 < 1) throw MahineHesab.Khata("از ۱ شروع کن!")
        val d = mutableListOf<Long>()
        var n = n0
        d.add(n)
        var mahdood = 0
        while (n != 1L && mahdood < 500) {
            n = if (n % 2 == 0L) n / 2 else 3 * n + 1
            d.add(n)
            mahdood++
        }
        return d
    }

    // تبدیل مبنا (پایه ۲..۱۶)
    fun tabdilMabna(n: Long, mabna: Int): String {
        if (mabna < 2 || mabna > 16) throw MahineHesab.Khata("مبنا بین ۲ تا ۱۶!")
        if (n == 0L) return "0"
        val ragham = "0123456789ABCDEF"
        var x = n
        var salb = false
        if (x < 0) { salb = true; x = -x }
        val sb = StringBuilder()
        while (x > 0) {
            sb.append(ragham[(x % mabna).toInt()])
            x /= mabna
        }
        if (salb) sb.append('-')
        return sb.reverse().toString()
    }

    // عدد کامل: مجموع مقسوم‌علیهای درست خودش = خودش (مثل ۶ و ۲۸)
    fun kamelAst(n: Long): Boolean {
        if (n < 2) return false
        var jam = 1L
        var i = 2L
        while (i * i <= n) {
            if (n % i == 0L) { jam += i; if (i != n / i) jam += n / i }
            i++
        }
        return jam == n
    }
}
