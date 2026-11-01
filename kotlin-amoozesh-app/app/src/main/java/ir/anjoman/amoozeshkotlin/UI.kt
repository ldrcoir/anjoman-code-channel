package ir.anjoman.amoozeshkotlin

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

    // همهٔ رنگ‌ها از تم فعلی می‌آیند؛ تغییر تم، کل اپ را عوض می‌کند
    val RANG_ZAMIN: Int get() = TemHa.faeli.zamin       // پس‌زمینهٔ اصلی
    val RANG_KART: Int get() = TemHa.faeli.kart         // رنگ کارت‌ها
    val RANG_CODE: Int get() = TemHa.faeli.code         // پس‌زمینهٔ بلوک کد
    val RANG_TIZ: Int get() = TemHa.faeli.tiz           // تاکیدی اصلی
    val RANG_TALAEI: Int get() = TemHa.faeli.talaei     // تاکیدی دوم
    val RANG_MATN: Int get() = TemHa.faeli.matn         // متن اصلی
    val RANG_KAMRANG: Int get() = TemHa.faeli.kamrang   // متن کم‌رنگ
    val RANG_SABZ: Int get() = TemHa.faeli.sabz         // جواب درست
    val RANG_GHERMEZ: Int get() = TemHa.faeli.ghermez   // جواب غلط
    val RANG_KHAKISTARI: Int get() = TemHa.faeli.khakestari // عناصر خنثی

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

    // یکسان‌سازی خروجی برای مقایسهٔ تمرین‌ها: حذف فاصلهٔ انتهای هر خط
    fun pakKonKhoruji(s: String): String {
        val khatHa = s.split("\n")
        val pak = khatHa.joinToString("\n") { it.trimEnd() }.trim()
        return pak
    }

    // نوار وضعیت رو هم‌رنگ پس‌زمینه می‌کنیم که تمیز باشه
    // اول تم از حافظه خوانده می‌شود که همهٔ صفحه‌ها با تم درست ساخته شوند
    fun rangVaredKon(activity: Activity) {
        TemHa.barGhardan(activity)
        activity.window.statusBarColor = RANG_ZAMIN
        activity.window.navigationBarColor = RANG_ZAMIN
    }
}

/*
 * نمرهٔ بهترین آزمون هر درس اینجا ذخیره می‌شه
 * با SharedPreferences ساده، بدون دیتابیس
 */
object Zakhire {

    // نسخهٔ ۳ شمارهٔ درس‌ها را جابه‌جا کرد (دو درس مقدمه اول آمدند)
    // نمره‌های قبلی یک‌بار به شماره‌های تازه منتقل می‌شوند تا پیشرفت بچه‌ها از دست نرود
    fun mohajeratV3(context: Context) {
        val p = context.getSharedPreferences("zakhire", Context.MODE_PRIVATE)
        if (p.getBoolean("mohajerat_v3_anjam_shod", false)) return
        val negashte = mapOf(1 to 3, 2 to 4, 3 to 5, 4 to 6, 5 to 7, 6 to 8, 7 to 9, 8 to 10,
            9 to 11, 10 to 12, 11 to 13, 12 to 14, 13 to 15, 14 to 24, 15 to 25)
        val virayesh = p.edit()
        for (ghadim in negashte.keys) {
            val n = p.getInt("behtarin_dars_$ghadim", -1)
            if (n > 0) {
                val taze = negashte[ghadim]!!
                if (n > p.getInt("behtarin_dars_$taze", -1)) {
                    virayesh.putInt("behtarin_dars_$taze", n)
                }
            }
        }
        virayesh.putBoolean("mohajerat_v3_anjam_shod", true).apply()
    }

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

    // تمرین‌های زمین بازی که درست حل شده‌اند
    fun tamrinAnjamAst(context: Context, shomareh: Int): Boolean {
        val p = context.getSharedPreferences("zakhire", Context.MODE_PRIVATE)
        return p.getBoolean("tamrin_$shomareh", false)
    }

    fun tamrinAnjamKon(context: Context, shomareh: Int) {
        val p = context.getSharedPreferences("zakhire", Context.MODE_PRIVATE)
        p.edit().putBoolean("tamrin_$shomareh", true).apply()
    }

    // چند تمرین تا حالا درست حل شده؟
    fun tamrinAnjamShode(context: Context): Int {
        var tedad = 0
        for (i in DarsHa.tamrinHa.indices) {
            if (tamrinAnjamAst(context, i)) tedad++
        }
        return tedad
    }
}
