package ir.anjoman.amoozeshrust

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.TextView

/*
 * ابزارهای کوچک برای ساختن رابط کاربری با کد
 * همهٔ رنگ‌ها و کارت‌ها و متن‌ها از همین‌جا می‌ان
 */
object Abzar {

    val RANG_ZAMIN = 0xFF1A120E.toInt()      // قهوه‌ای تیرهٔ گرم پس‌زمینه
    val RANG_KART = 0xFF2A1D14.toInt()       // رنگ کارت‌ها
    val RANG_CODE = 0xFF140D08.toInt()       // پس‌زمینهٔ تیره‌تر برای کد
    val RANG_TIZ = 0xFFE86A33.toInt()        // نارنجی راست
    val RANG_TALAEI = 0xFFF2B134.toInt()     // طلایی
    val RANG_MATN = 0xFFEAF2FF.toInt()       // رنگ اصلی متن
    val RANG_KAMRANG = 0xFF94A3B8.toInt()    // متن کم‌رنگ
    val RANG_SABZ = 0xFF22C55E.toInt()       // جواب درست
    val RANG_GHERMEZ = 0xFFEF4444.toInt()    // جواب غلط
    val RANG_KHAKISTARI = 0xFF334155.toInt() // برای چیزهای خالی

    fun dp(context: Context, meghdar: Float): Int =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            meghdar,
            context.resources.displayMetrics
        ).toInt()

    // کارت با گوشهٔ گرد
    fun kart(context: Context, rang: Int = RANG_KART, shoa: Float = 16f): GradientDrawable {
        val shakl = GradientDrawable()
        shakl.setColor(rang)
        shakl.cornerRadius = dp(context, shoa).toFloat()
        return shakl
    }

    // دایرهٔ ساده برای شمارهٔ درس‌ها
    fun dayereh(context: Context, rang: Int): GradientDrawable {
        val shakl = GradientDrawable()
        shakl.shape = GradientDrawable.OVAL
        shakl.setColor(rang)
        return shakl
    }

    // کارت شیشه‌ای با خط دور، برای دکمه‌های فرعی
    fun kartKhatei(context: Context, rangKhat: Int, shoa: Float = 14f): GradientDrawable {
        val shakl = GradientDrawable()
        shakl.setColor(Color.TRANSPARENT)
        shakl.setStroke(dp(context, 1.5f), rangKhat)
        shakl.cornerRadius = dp(context, shoa).toFloat()
        return shakl
    }

    // TextView با فونت وزیرمتن
    fun tv(
        context: Context,
        matn: String,
        size: Float,
        rang: Int = RANG_MATN,
        bold: Boolean = false
    ): TextView {
        val t = TextView(context)
        t.text = matn
        t.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
        t.setTextColor(rang)
        t.typeface = if (bold) FontBox.vazirBold(context) else FontBox.vazir(context)
        return t
    }

    // دکمهٔ کارتی گرد؛ اگه khateDor باشه فقط خط دورش می‌مونه
    fun dokme(
        context: Context,
        matn: String,
        rang: Int,
        rangMatn: Int,
        khateDor: Boolean = false
    ): TextView {
        val t = TextView(context)
        t.text = matn
        t.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        t.setTextColor(rangMatn)
        t.typeface = FontBox.vazirBold(context)
        t.gravity = Gravity.CENTER
        t.background = if (khateDor) kartKhatei(context, rang) else kart(context, rang)
        t.setPadding(dp(context, 16f), dp(context, 14f), dp(context, 16f), dp(context, 14f))
        lams(t)
        return t
    }

    // افکت لمسی سادهٔ خود اندروید
    fun lams(v: View) {
        val khoroji = TypedValue()
        v.context.theme.resolveAttribute(android.R.attr.selectableItemBackground, khoroji, true)
        v.foreground = v.context.getDrawable(khoroji.resourceId)
    }

    // تبدیل عدد انگلیسی به رقم فارسی
    fun adadFarsi(adad: Int): String {
        val raghamHa = listOf("۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹")
        return adad.toString().map { raghamHa[it - '0'] }.joinToString("")
    }

    // نوار وضعیت رو هم‌رنگ پس‌زمینه می‌کنیم که تمیز باشه
    fun rangVaredKon(activity: Activity) {
        activity.window.statusBarColor = RANG_ZAMIN
        activity.window.navigationBarColor = RANG_ZAMIN
    }
}

/*
 * نمرهٔ بهترین آزمون هر درس اینجا ذخیره می‌شه
 * با SharedPreferences ساده، بدون دیتابیس
 */
object Zakhire {

    fun behtarin(context: Context, shomarehDars: Int): Int {
        val p = context.getSharedPreferences("zakhire", Context.MODE_PRIVATE)
        return p.getInt("behtarin_dars_$shomarehDars", -1)
    }

    fun zakhireKon(context: Context, shomarehDars: Int, emtiyaz: Int) {
        val p = context.getSharedPreferences("zakhire", Context.MODE_PRIVATE)
        val ghabli = p.getInt("behtarin_dars_$shomarehDars", -1)
        if (emtiyaz > ghabli) {
            p.edit().putInt("behtarin_dars_$shomarehDars", emtiyaz).apply()
        }
    }

    // جمع نمره‌های همهٔ درس‌ها برای نوار پیشرفت کل
    fun majmoo(context: Context): Int {
        var jam = 0
        for (dars in DarsHa.darsList) {
            val n = behtarin(context, dars.shomareh)
            if (n > 0) jam += n
        }
        return jam
    }

    // ---------- شمارش آزمون‌ها برای تبلیغ کامل (هر N آزمون یک‌بار) ----------
    fun afzayeshTest(context: Context) {
        val p = context.getSharedPreferences("zakhire", Context.MODE_PRIVATE)
        p.edit().putInt("tedad_test", p.getInt("tedad_test", 0) + 1).apply()
    }

    fun bayadTablighNeshanBedeh(context: Context, harN: Int): Boolean {
        val p = context.getSharedPreferences("zakhire", Context.MODE_PRIVATE)
        val t = p.getInt("tedad_test", 0)
        return harN > 0 && t > 0 && t % harN == 0
    }

    fun tablighNeshanDad(context: Context) {
        val p = context.getSharedPreferences("zakhire", Context.MODE_PRIVATE)
        p.edit().putLong("tabligh_akharin", System.currentTimeMillis()).apply()
    }
}
