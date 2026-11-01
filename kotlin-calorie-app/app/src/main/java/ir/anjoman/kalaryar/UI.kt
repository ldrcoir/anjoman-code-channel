package ir.anjoman.kalaryar

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.TextView

/*
 * ابزارهای رابط کاربری کالری‌یار
 * پالت سبزِ سلامت با گرادیان؛ همهٔ رنگ‌ها و کارت‌ها از همین‌جا می‌آید
 */
object Abzar {

    // ---------- پالت ----------
    val RANG_ZAMIN = 0xFF0C1512.toInt()      // سبزِ خیلی تیرهٔ پس‌زمینه
    val RANG_KART = 0xFF152420.toInt()       // کارت‌ها
    val RANG_CODE = 0xFF0F1B17.toInt()       // کارت‌های تیره‌تر
    val RANG_SABZ = 0xFF22C55E.toInt()       // سبزِ اصلی (تاکید)
    val RANG_SABZKAM = 0xFF16A34A.toInt()    // سبزِ پررنگ‌تر
    val RANG_TALAEI = 0xFFF2B134.toInt()     // طلایی (انرژی)
    val RANG_NARENJI = 0xFFFB923C.toInt()    // نارنجی (چربی/هشدار)
    val RANG_ABI = 0xFF38BDF8.toInt()        // آبی (آب)
    val RANG_BANAFSH = 0xFFA78BFA.toInt()    // بنفش (پروتئین)
    val RANG_ZARD = 0xFFFACC15.toInt()       // زرد (کربوهیدرات)
    val RANG_MATN = 0xFFEAF7F0.toInt()       // متن اصلی
    val RANG_KAMRANG = 0xFF8FAF9F.toInt()    // متن کم‌رنگ
    val RANG_GHERMEZ = 0xFFEF4444.toInt()    // خطر/بیش‌مصرف
    val RANG_KHAKISTARI = 0xFF24352E.toInt() // عناصر خنثی

    fun dp(context: Context, meghdar: Float): Int =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            meghdar,
            context.resources.displayMetrics
        ).toInt()

    // کارت گرد ساده
    fun kart(context: Context, rang: Int = RANG_KART, shoa: Float = 18f): GradientDrawable {
        val shakl = GradientDrawable()
        shakl.setColor(rang)
        shakl.cornerRadius = dp(context, shoa).toFloat()
        return shakl
    }

    // کارت گرادیانی برای سربرگ‌ها و دکمه‌های مهم
    fun kartGradient(context: Context, rang1: Int, rang2: Int, shoa: Float = 20f): GradientDrawable {
        val shakl = GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            intArrayOf(rang1, rang2)
        )
        shakl.cornerRadius = dp(context, shoa).toFloat()
        return shakl
    }

    fun dayereh(context: Context, rang: Int): GradientDrawable {
        val shakl = GradientDrawable()
        shakl.shape = GradientDrawable.OVAL
        shakl.setColor(rang)
        return shakl
    }

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

    // دکمهٔ کارتی گرد
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

    fun lams(v: View) {
        val khoroji = TypedValue()
        v.context.theme.resolveAttribute(android.R.attr.selectableItemBackground, khoroji, true)
        v.foreground = v.context.getDrawable(khoroji.resourceId)
    }

    // ---------- اعداد فارسی ----------
    private val raghamHa = listOf("۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹")

    fun adadFarsi(adad: Int): String =
        adad.toString().map { if (it == '-') '−' else raghamHa[it - '0'] }.joinToString("")

    fun adadFarsi(adad: Double): String {
        val gerd = if (adad == adad.toLong().toDouble()) {
            adad.toLong().toString()
        } else {
            String.format(java.util.Locale.US, "%.1f", adad)
        }
        val sb = StringBuilder()
        for (c in gerd) {
            when (c) {
                '-' -> sb.append('−')
                '.' -> sb.append('٫')
                else -> sb.append(raghamHa[c - '0'])
            }
        }
        return sb.toString()
    }

    // ---------- تاریخ شمسی (الگوریتم استاندارد) ----------
    // خروجی: ۱۰ آبان ۱۴۰۵
    fun tarikhShamsi(salMiladi: Int, mahMiladi: Int, roozMiladi: Int): String {
        val g_d_m = intArrayOf(0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334)
        var gy = salMiladi
        var gm = mahMiladi
        var gd = roozMiladi
        var jy: Int
        val gy2 = if (gm > 2) gy + 1 else gy
        var days = 355666 + 365 * gy + (gy2 + 3) / 4 - (gy2 + 99) / 100 +
                (gy2 + 399) / 400 + gd + g_d_m[mahMiladi - 1]
        jy = -1595 + 33 * (days / 12053)
        days %= 12053
        jy += 4 * (days / 1461)
        days %= 1461
        if (days > 365) {
            jy += (days - 1) / 365
            days = (days - 1) % 365
        }
        val mah = if (days < 186) 1 + days / 31 else 7 + (days - 186) / 30
        val rooz = if (days < 186) 1 + days % 31 else 1 + (days - 186) % 30
        val asamiMah = listOf(
            "فروردین", "اردیبهشت", "خرداد", "تیر", "مرداد", "شهریور",
            "مهر", "آبان", "آذر", "دی", "بهمن", "اسفند"
        )
        return "${adadFarsi(rooz)} ${asamiMah[mah - 1]} ${adadFarsi(jy)}"
    }

    fun emrooz(): String {
        val c = java.util.Calendar.getInstance()
        return tarikhShamsi(
            c.get(java.util.Calendar.YEAR),
            c.get(java.util.Calendar.MONTH) + 1,
            c.get(java.util.Calendar.DAY_OF_MONTH)
        )
    }

    // کلید ذخیرهٔ امروز: 2026-11-01
    fun kelidEmrooz(): String {
        val c = java.util.Calendar.getInstance()
        return String.format(
            "%04d-%02d-%02d",
            c.get(java.util.Calendar.YEAR),
            c.get(java.util.Calendar.MONTH) + 1,
            c.get(java.util.Calendar.DAY_OF_MONTH)
        )
    }

    // کلید روزهای اخیر: امروز منهای n روز
    fun kelidRooz(pas: Int): String {
        val c = java.util.Calendar.getInstance()
        c.add(java.util.Calendar.DAY_OF_MONTH, -pas)
        return String.format(
            "%04d-%02d-%02d",
            c.get(java.util.Calendar.YEAR),
            c.get(java.util.Calendar.MONTH) + 1,
            c.get(java.util.Calendar.DAY_OF_MONTH)
        )
    }

    fun rangVaredKon(activity: Activity) {
        activity.window.statusBarColor = RANG_ZAMIN
        activity.window.navigationBarColor = RANG_ZAMIN
    }
}
