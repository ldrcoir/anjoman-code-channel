package ir.emaroid.hesab

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.TextView

/*
 * ابزارهای رابط کاربری EmaRoid — ماشین‌حساب
 */
object Abzar {

    val RANG_ZAMIN = 0xFF0D1526.toInt()
    val RANG_KART = 0xFF18233D.toInt()
    val RANG_CODE = 0xFF0B1220.toInt()
    val RANG_TIZ = 0xFF2AA8B8.toInt()
    val RANG_TALAEI = 0xFFF2B134.toInt()
    val RANG_MATN = 0xFFEAF2FF.toInt()
    val RANG_KAMRANG = 0xFF94A3B8.toInt()
    val RANG_SABZ = 0xFF22C55E.toInt()
    val RANG_GHERMEZ = 0xFFEF4444.toInt()
    val RANG_KHAKISTARI = 0xFF334155.toInt()
    val RANG_DOKME = 0xFF223052.toInt()
    val RANG_DOKME_AMAL = 0xFF2AA8B8.toInt()

    fun dp(c: Context, v: Float): Int =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, v, c.resources.displayMetrics).toInt()

    fun kart(c: Context, rang: Int = RANG_KART, shoa: Float = 16f): GradientDrawable {
        val g = GradientDrawable()
        g.setColor(rang)
        g.cornerRadius = dp(c, shoa).toFloat()
        return g
    }

    fun dayereh(c: Context, rang: Int): GradientDrawable {
        val g = GradientDrawable()
        g.shape = GradientDrawable.OVAL
        g.setColor(rang)
        return g
    }

    fun kartKhatei(c: Context, rangKhat: Int, shoa: Float = 14f): GradientDrawable {
        val g = GradientDrawable()
        g.setColor(Color.TRANSPARENT)
        g.setStroke(dp(c, 1.5f), rangKhat)
        g.cornerRadius = dp(c, shoa).toFloat()
        return g
    }

    fun tv(c: Context, matn: String, size: Float, rang: Int = RANG_MATN, bold: Boolean = false): TextView {
        val t = TextView(c)
        t.text = matn
        t.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
        t.setTextColor(rang)
        t.typeface = if (bold) FontBox.vazirBold(c) else FontBox.vazir(c)
        return t
    }

    fun dokme(c: Context, matn: String, rang: Int, rangMatn: Int, khateDor: Boolean = false, size: Float = 16f): TextView {
        val t = TextView(c)
        t.text = matn
        t.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
        t.setTextColor(rangMatn)
        t.typeface = FontBox.vazirBold(c)
        t.gravity = Gravity.CENTER
        t.background = if (khateDor) kartKhatei(c, rang) else kart(c, rang)
        t.setPadding(dp(c, 14f), dp(c, 12f), dp(c, 14f), dp(c, 12f))
        lams(t)
        return t
    }

    fun lams(v: View) {
        val tv = TypedValue()
        v.context.theme.resolveAttribute(android.R.attr.selectableItemBackground, tv, true)
        v.foreground = v.context.getDrawable(tv.resourceId)
    }

    fun adadFarsi(n: Int): String {
        val r = listOf("۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹")
        return n.toString().map { if (it.isDigit()) r[it - '0'] else it }.joinToString("")
    }

    fun rangVaredKon(a: Activity) {
        a.window.statusBarColor = RANG_ZAMIN
        a.window.navigationBarColor = RANG_ZAMIN
    }
}

/*
 * ذخیرهٔ تنظیم‌ها (درجه/رادیان، تاریخچهٔ ماشین‌حساب)
 */
object Zakhire {
    fun daraje(c: Context): Boolean =
        c.getSharedPreferences("zakhire", Context.MODE_PRIVATE).getBoolean("daraje", true)

    fun darajeKon(c: Context, v: Boolean) {
        c.getSharedPreferences("zakhire", Context.MODE_PRIVATE).edit().putBoolean("daraje", v).apply()
    }
}
