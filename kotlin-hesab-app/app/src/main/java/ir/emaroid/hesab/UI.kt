package ir.emaroid.hesab

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.TypedValue
import android.view.Gravity
import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import android.view.View
import android.widget.TextView

/*
 * ابزارهای رابط کاربری EmaRoid — پالت «سنگ‌زغالی»
 * تیرهٔ جدی و بی‌شلوغ؛ فیروزه‌ای ملایم تنها رنگ تأکید؛ بدون ستاره و نئون
 */
object Abzar {

    // پس‌زمینهٔ سنگ‌زغالی
    val RANG_SHURU = 0xFF151A21.toInt()      // گرافیت بالا
    val RANG_PAYAN = 0xFF0C0F13.toInt()      // عمق پایین

    val RANG_ZAMIN = 0xFF101318.toInt()
    val RANG_KART = 0xFF1B212A.toInt()
    val RANG_CODE = 0xFF161B22.toInt()
    val RANG_TIZ = 0xFF2FA8C9.toInt()        // فیروزه‌ای مه‌گرفته — تنها تأکید
    val RANG_GOLABI = 0xFF7A92AB.toInt()     // فولاد آبی سرد
    val RANG_BANAFSH = 0xFF5B6B7C.toInt()    // سیلایی
    val RANG_TALAEI = 0xFFD9A441.toInt()     // کهربایی خاکی
    val RANG_MATN = 0xFFE8ECF1.toInt()
    val RANG_KAMRANG = 0xFF8A94A1.toInt()
    val RANG_SABZ = 0xFF2FA36B.toInt()       // سبز خاموش
    val RANG_GHERMEZ = 0xFFD96459.toInt()    // سرخ خاموش
    val RANG_KHAKISTARI = 0xFF2A313B.toInt()
    val RANG_DOKME = 0xFF1F2630.toInt()      // کلید سنگی
    val RANG_DOKME_AMAL = 0xFF31404E.toInt() // عملگرها فولادی

    fun dp(c: Context, v: Float): Int =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, v, c.resources.displayMetrics).toInt()

    fun kart(c: Context, rang: Int = RANG_KART, shoa: Float = 16f): GradientDrawable {
        val g = GradientDrawable()
        g.setColor(rang)
        g.cornerRadius = dp(c, shoa).toFloat()
        return g
    }

    // کارت شیشه‌ای — پس‌زمینهٔ نیمه‌شفاف + خط درخشان
    fun shisheh(
        c: Context, rang: Int = RANG_DOKME, rangKhat: Int = 0x2EFFFFFF,
        shoa: Float = 20f, setorKhat: Float = 1.2f
    ): GradientDrawable {
        val g = GradientDrawable()
        g.setColor(rang)
        g.setStroke(dp(c, setorKhat), rangKhat)
        g.cornerRadius = dp(c, shoa).toFloat()
        return g
    }

    // دایرهٔ درخشان (نشان بخش‌ها)
    fun dayereh(c: Context, rang: Int): GradientDrawable {
        val g = GradientDrawable()
        g.shape = GradientDrawable.OVAL
        g.setColor(rang)
        return g
    }

    // گرادیان مورب فانتزی — برای کلید = و کارت‌های ویژه
    fun gradian(c: Context, rang1: Int, rang2: Int, shoa: Float = 18f): GradientDrawable {
        val g = GradientDrawable(
            GradientDrawable.Orientation.TL_BR, intArrayOf(rang1, rang2))
        g.cornerRadius = dp(c, shoa).toFloat()
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

    // فشردن: کلید موقع لمس کمی کوچک می‌شود + لرزش ظریف — حس کلید واقعی
    fun fasor(v: View) {
        v.setOnTouchListener { vv, ev ->
            when (ev.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    vv.animate().scaleX(0.90f).scaleY(0.90f).setDuration(50).start()
                    vv.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL ->
                    vv.animate().scaleX(1f).scaleY(1f).setDuration(130).start()
            }
            false // کلیک عادی همچنان اجرا می‌شود
        }
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
