package ir.emaroid.hesab

import org.junit.Test
import org.junit.Assert.*
import kotlin.math.abs
import kotlin.math.pow

/*
 * آزمون‌های واحد موتور ماشین‌حساب EmaRoid — روی JVM اجرا می‌شود
 */
class MahineHesabTest {

    private fun nazdik(a: Double, b: Double, tol: Double = 1e-9) =
        assertTrue("a=$a b=$b", abs(a - b) < tol)

    @Test
    fun `جاگذاری پایه`() {
        nazdik(MahineHesab.hesabKon("2+3*4"), 14.0)
        nazdik(MahineHesab.hesabKon("(2+3)*4"), 20.0)
        nazdik(MahineHesab.hesabKon("2^3^2"), 512.0)      // توان راست‌گرا
        nazdik(MahineHesab.hesabKon("10/4"), 2.5)
        nazdik(MahineHesab.hesabKon("10%3"), 1.0)
        nazdik(MahineHesab.hesabKon("-5+2"), -3.0)
        nazdik(MahineHesab.hesabKon("2*-3"), -6.0)
        nazdik(MahineHesab.hesabKon("10%2B2".replace("%2B", "+")), 12.0)
    }

    @Test
    fun `توابع و ثابت‌ها`() {
        MahineHesab.daraje = false
        nazdik(MahineHesab.hesabKon("sin(pi/2)"), 1.0, 1e-9)
        nazdik(MahineHesab.hesabKon("cos(0)"), 1.0)
        nazdik(MahineHesab.hesabKon("sqrt(16)"), 4.0)
        nazdik(MahineHesab.hesabKon("ln(e)"), 1.0, 1e-9)
        nazdik(MahineHesab.hesabKon("log(100)"), 2.0, 1e-9)
        MahineHesab.daraje = true
        nazdik(MahineHesab.hesabKon("sin(30)"), 0.5, 1e-9)
        nazdik(MahineHesab.hesabKon("tan(45)"), 1.0, 1e-9)
    }

    @Test
    fun `فاکتوریل و ارقام فارسی`() {
        nazdik(MahineHesab.hesabKon("5!"), 120.0)
        nazdik(MahineHesab.hesabKon("۳+۴"), 7.0)
        // pakeh ارقام فارسی را به لاتین تبدیل می‌کند
        assertEquals("123", MahineHesab.pakeh("۱۲۳"))
    }

    @Test(expected = MahineHesab.Khata::class)
    fun `تقسیم بر صفر خطا`() {
        MahineHesab.hesabKon("5/0")
    }

    @Test(expected = MahineHesab.Khata::class)
    fun `جذر منفی خطا`() {
        MahineHesab.hesabKon("sqrt(-4)")
    }
}

class HalKonTest {

    @Test
    fun `معادله خطی`() {
        val h = HalKon.halKon("2x+4=10")
        assertEquals("x = 3", h.javabHa[0])
    }

    @Test
    fun `معادله خطی منفی`() {
        val h = HalKon.halKon("-x+3=0")
        assertEquals("x = 3", h.javabHa[0])
    }

    @Test
    fun `درجه دوم دو جواب`() {
        val h = HalKon.halKon("x^2-5x+6=0")
        assertTrue(h.javabHa.any { it.contains("3") })
        assertTrue(h.javabHa.any { it.contains("2") })
    }

    @Test
    fun `درجه دوم مضاعف`() {
        val h = HalKon.halKon("x^2+2x+1=0")
        assertTrue(h.javabHa[0].startsWith("x = -1"))
    }

    @Test
    fun `تجزیه ضریب پرانتز`() {
        val (a, b) = HalKon.tajzie("2(x+3)-4")
        assertEquals(2.0, a, 1e-9)
        assertEquals(2.0, b, 1e-9)
    }
}

class AdadYarTest {

    @Test
    fun `اعداد اول`() {
        assertTrue(AdadYar.avalAst(2))
        assertTrue(AdadYar.avalAst(97))
        assertFalse(AdadYar.avalAst(1))
        assertFalse(AdadYar.avalAst(100))
        assertTrue(AdadYar.avalAst(7919L))
    }

    @Test
    fun `تجزیه`() {
        val t = AdadYar.tajzieAval(360)
        assertEquals(listOf(2L to 3, 3L to 2, 5L to 1), t)
    }

    @Test
    fun `بمم و کرمم`() {
        assertEquals(6L, AdadYar.bmm(18, 12))
        assertEquals(36L, AdadYar.kmm(18, 12))
    }

    @Test
    fun `کولاتز`() {
        val d = AdadYar.collatz(6)
        assertEquals(6L, d.first())
        assertEquals(1L, d.last())
        // ۶ → ۳ → ۱۰ → ۵ → ۱۶ → ۸ → ۴ → ۲ → ۱
        assertEquals(9, d.size)
    }

    @Test
    fun `تبدیل مبنا`() {
        assertEquals("1010", AdadYar.tabdilMabna(10, 2))
        assertEquals("FF", AdadYar.tabdilMabna(255, 16))
        assertEquals("17", AdadYar.tabdilMabna(15, 8))
    }

    @Test
    fun `عدد کامل`() {
        assertTrue(AdadYar.kamelAst(6))
        assertTrue(AdadYar.kamelAst(28))
        assertFalse(AdadYar.kamelAst(12))
    }
}
