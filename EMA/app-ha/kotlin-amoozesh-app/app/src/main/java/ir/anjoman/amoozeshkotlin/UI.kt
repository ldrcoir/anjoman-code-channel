package ir.anjoman.amoozeshkotlin

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.TextView

/*
 * ابزارهای کوچک برای ساختن رابط کاربری با کد
 * همهٔ رنگ‌ها از TemHa می‌آیند؛ یعنی با عوض‌کردن تم، کل اپ رنگ عوض می‌کند
 */
object Abzar {

    var RANG_ZAMIN = 0xFF0F172A.toInt()      // پس‌زمینهٔ اصلی (از تم)
    var RANG_KART = 0xFF1E293B.toInt()       // رنگ کارت‌ها (از تم)
    var RANG_CODE = 0xFF0B1220.toInt()       // پس‌زمینهٔ بلوک کد (از تم)
    var RANG_TIZ = 0xFF2AA8B8.toInt()        // تاکیدی اصلی (از تم)
    var RANG_TALAEI = 0xFFF2B134.toInt()     // تاکیدی دوم (از تم)
    var RANG_MATN = 0xFFEAF2FF.toInt()       // متن اصلی (از تم)
    var RANG_KAMRANG = 0xFF94A3B8.toInt()    // متن کم‌رنگ (از تم)
    var RANG_SABZ = 0xFF22C55E.toInt()       // جواب درست (از تم)
    var RANG_GHERMEZ = 0xFFEF4444.toInt()    // جواب غلط (از تم)
    var RANG_KHAKISTARI = 0xFF334155.toInt() // عناصر خالی/خنثی (از تم)

    fun dp(context: Context, meghdar: Float): Int =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            meghdar,
            context.resources.displayMetrics
        ).toInt()

    // رنگ‌های تم فعلی را داخل ابزارها می‌نشاند؛ در شروع هر صفحه صدا زده می‌شود
    fun rangVaredKon(activity: Activity) {
        TemHa.barGhardan(activity)
        val tem = TemHa.faeli
        RANG_ZAMIN = tem.zamin
        RANG_KART = tem.kart
        RANG_CODE = tem.code
        RANG_TIZ = tem.tiz
        RANG_TALAEI = tem.talaei
        RANG_MATN = tem.matn
        RANG_KAMRANG = tem.kamrang
        RANG_SABZ = tem.sabz
        RANG_GHERMEZ = tem.ghermez
        RANG_KHAKISTARI = tem.khakestari
        activity.window.statusBarColor = tem.zamin
        activity.window.navigationBarColor = tem.zamin
    }

    // پس‌زمینهٔ طرح‌دار هم‌خانوادهٔ تم فعلی
    fun nagsh(context: Context): NagshZamin =
        NagshZamin(TemHa.faeli.zamin, TemHa.faeli.code)

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

    // تبدیل عدد به رقم زبان فعلی
    fun adadFarsi(adad: Int): String = Zaban.adad(adad)

    /*
     * رنگ‌آمیزی کد کاتلین — دیگر کد متن خام و زشت نیست:
     * کامنت سبز-خاکستری، رشتهٔ سبز، کلیدواژه بنفش، عدد نارنجی
     */
    fun kodRengin(kod: String, rangPae: Int = RANG_MATN): SpannableString {
        val sp = SpannableString(kod)
        val rangComment = if (TemHa.faeli.tir) 0xFF6A9955.toInt() else 0xFF3D7A4A.toInt()
        val rangReshteh = if (TemHa.faeli.tir) 0xFF98C379.toInt() else 0xFF22813A.toInt()
        val rangKolidozhe = if (TemHa.faeli.tir) 0xFFC678DD.toInt() else 0xFF8A2BB8.toInt()
        val rangAdad = if (TemHa.faeli.tir) 0xFFD19A66.toInt() else 0xFFB25C10.toInt()

        val kolidozheHa = setOf(
            "fun", "val", "var", "if", "else", "when", "for", "while", "return", "in",
            "class", "data", "object", "true", "false", "null", "Int", "Double",
            "String", "Boolean", "List", "MutableList", "it"
        )
        val tabeHa = setOf(
            "println", "print", "readln", "toInt", "toIntOrNull", "sum", "size", "max",
            "min", "add", "listOf", "mutableListOf", "main", "joinToString", "repeat", "until"
        )

        var i = 0
        val n = kod.length
        while (i < n) {
            val c = kod[i]
            // کامنت //
            if (c == '/' && i + 1 < n && kod[i + 1] == '/') {
                var payan = kod.indexOf('\n', i)
                if (payan == -1) payan = n
                sp.setSpan(ForegroundColorSpan(rangComment), i, payan, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                i = payan
                continue
            }
            // رشتهٔ داخل گیومه
            if (c == '"') {
                var j = i + 1
                while (j < n) {
                    if (kod[j] == '\\') { j += 2; continue }
                    if (kod[j] == '"') { j++; break }
                    j++
                }
                val payan = minOf(j, n)
                sp.setSpan(ForegroundColorSpan(rangReshteh), i, payan, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                i = payan
                continue
            }
            // عدد
            if (c.isDigit()) {
                var j = i
                while (j < n && (kod[j].isDigit() || kod[j] == '.')) j++
                sp.setSpan(ForegroundColorSpan(rangAdad), i, j, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                i = j
                continue
            }
            // کلمه
            if (c.isLetter() || c == '_') {
                var j = i
                while (j < n && (kod[j].isLetterOrDigit() || kod[j] == '_')) j++
                val kalame = kod.substring(i, j)
                val rang = when {
                    kalame in kolidozheHa -> rangKolidozhe
                    kalame in tabeHa -> rangPae
                    else -> -1
                }
                if (rang != -1) {
                    sp.setSpan(ForegroundColorSpan(rang), i, j, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    if (kalame in kolidozheHa) {
                        sp.setSpan(StyleSpan(android.graphics.Typeface.BOLD), i, j, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    }
                }
                i = j
                continue
            }
            i++
        }
        return sp
    }

    // نرمال‌سازی خروجی برای مقایسهٔ تمرین‌ها: حذف فاصله‌های لبه و خط‌های خالی
    fun pakKonKhoruji(s: String): String {
        return s.trim().lines().map { it.trim() }.filter { it.isNotEmpty() }.joinToString("\n")
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

    // بهترین نمرهٔ آزمون جامع
    fun behtarinJame(context: Context): Int {
        val p = context.getSharedPreferences("zakhire", Context.MODE_PRIVATE)
        return p.getInt("behtarin_jame", -1)
    }

    fun zakhireJame(context: Context, emtiyaz: Int) {
        val p = context.getSharedPreferences("zakhire", Context.MODE_PRIVATE)
        val ghabli = p.getInt("behtarin_jame", -1)
        if (emtiyaz > ghabli) p.edit().putInt("behtarin_jame", emtiyaz).apply()
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

    // ---------- تمرین‌های حل‌شدهٔ زمین بازی ----------
    fun tamrinAnjamAst(context: Context, shomareh: Int): Boolean {
        val p = context.getSharedPreferences("zakhire", Context.MODE_PRIVATE)
        return p.getStringSet("tamrin_anjam", emptySet())?.contains(shomareh.toString()) ?: false
    }

    fun tamrinAnjamShode(context: Context): Int {
        val p = context.getSharedPreferences("zakhire", Context.MODE_PRIVATE)
        return p.getStringSet("tamrin_anjam", emptySet())?.size ?: 0
    }

    fun tamrinAnjamKon(context: Context, shomareh: Int) {
        val p = context.getSharedPreferences("zakhire", Context.MODE_PRIVATE)
        val s = (p.getStringSet("tamrin_anjam", emptySet()) ?: emptySet()).toMutableSet()
        s.add(shomareh.toString())
        p.edit().putStringSet("tamrin_anjam", s).apply()
    }

    // ---------- فصل‌های داستان خوانده‌شده ----------
    fun dastanDidehAst(context: Context, shomareh: Int): Boolean {
        val p = context.getSharedPreferences("zakhire", Context.MODE_PRIVATE)
        return p.getStringSet("dastan_dideh", emptySet())?.contains(shomareh.toString()) ?: false
    }

    fun dastanDidehKon(context: Context, shomareh: Int) {
        val p = context.getSharedPreferences("zakhire", Context.MODE_PRIVATE)
        val s = (p.getStringSet("dastan_dideh", emptySet()) ?: emptySet()).toMutableSet()
        s.add(shomareh.toString())
        p.edit().putStringSet("dastan_dideh", s).apply()
    }

    fun dastanDorehPasShode(context: Context): Int {
        var n = 0
        for (dars in DarsHa.darsList) if (behtarin(context, dars.shomareh) > 0) n++
        return n
    }
}
