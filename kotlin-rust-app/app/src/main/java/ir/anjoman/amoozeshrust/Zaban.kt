package ir.anjoman.amoozeshrust

import android.content.Context

/*
 * سیستم چندزبانهٔ EmaRoid
 * سه زبان: فارسی، English (پرچم ایرلند)، العربية (پرچم فلسطین)
 * زبان انتخابی در SharedPreferences ذخیره می‌شود و همهٔ نوشته‌های
 * رابط کاربری از جدول همین‌جا می‌آیند.
 * محتوای درس‌ها فعلاً فارسی است (برای بچه‌های پژوهشسرا).
 */
object Zaban {

    enum class Z { FA, EN, AR }

    private var feli: Z? = null

    fun bekhon(c: Context): Z {
        if (feli == null) {
            val p = c.getSharedPreferences("zakhire", Context.MODE_PRIVATE)
            val n = p.getString("zaban", "FA") ?: "FA"
            feli = when (n) {
                "EN" -> Z.EN
                "AR" -> Z.AR
                else -> Z.FA
            }
        }
        return feli!!
    }

    fun zakhireKon(c: Context, z: Z) {
        feli = z
        c.getSharedPreferences("zakhire", Context.MODE_PRIVATE)
            .edit().putString("zaban", z.name).apply()
    }

    // فارسی و عربی راست‌چین‌اند، انگلیسی چپ‌چین
    fun rastChin(c: Context): Boolean = bekhon(c) != Z.EN

    fun namZaban(z: Z): String = when (z) {
        Z.FA -> "فارسی"
        Z.EN -> "English"
        Z.AR -> "العربية"
    }

    // اعداد: فارسی با رقم فارسی، عربی با رقم عربی، انگلیسی لاتین
    fun adad(n: Int): String {
        val raghamHa = when (feli) {
            Z.AR -> listOf("٠", "١", "٢", "٣", "٤", "٥", "٦", "٧", "٨", "٩")
            Z.EN -> listOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")
            else -> listOf("۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹")
        }
        return n.toString().map { if (it.isDigit()) raghamHa[it - '0'] else it }.joinToString("")
    }

    /*
     * جدول نوشته‌های رابط کاربری به سه زبان
     * کلیدها با تابع t خوانده می‌شوند
     */
    fun t(kelid: String): String {
        val jadval: Map<String, Map<Z, String>> = jadvalHa
        val z = feli ?: Z.FA
        return jadval[kelid]?.get(z) ?: jadval[kelid]?.get(Z.FA) ?: kelid
    }

    private val jadvalHa: Map<String, Map<Z, String>> = mapOf(
        "onvan_app" to mapOf(
            Z.FA to "آموزش Rust",
            Z.EN to "Learn Kotlin",
            Z.AR to "تعلّم كوتلين"
        ),
        "zir_onvan" to mapOf(
            Z.FA to "قدم‌به‌قدم راست یاد بگیر",
            Z.EN to "Learn Kotlin step by step",
            Z.AR to "تعلّم كوتلين خطوة بخطوة"
        ),
        "pishraft_kol" to mapOf(
            Z.FA to "پیشرفت کل",
            Z.EN to "Overall progress",
            Z.AR to "التقدم الكلي"
        ),
        "az" to mapOf(Z.FA to "از", Z.EN to "of", Z.AR to "من"),
        "dars_ha" to mapOf(Z.FA to "درس‌ها", Z.EN to "Lessons", Z.AR to "الدروس"),
        "darbare" to mapOf(Z.FA to "درباره", Z.EN to "About", Z.AR to "حول"),
        "bargasht" to mapOf(Z.FA to "بازگشت", Z.EN to "Back", Z.AR to "رجوع"),
        "bargasht_dars" to mapOf(
            Z.FA to "بازگشت به درس‌ها",
            Z.EN to "Back to lessons",
            Z.AR to "الرجوع إلى الدروس"
        ),
        "shoroo_azmoon" to mapOf(
            Z.FA to "شروع آزمون",
            Z.EN to "Start quiz",
            Z.AR to "ابدأ الاختبار"
        ),
        "behtarin_natije" to mapOf(
            Z.FA to "بهترین نتیجهٔ آزمون",
            Z.EN to "Best quiz result",
            Z.AR to "أفضل نتيجة اختبار"
        ),
        "sahih" to mapOf(Z.FA to "درست", Z.EN to "correct", Z.AR to "صحيح"),
        "hanuz_azmoon" to mapOf(
            Z.FA to "هنوز آزمون ندادی",
            Z.EN to "Quiz not taken yet",
            Z.AR to "لم تقم الاختبار بعد"
        ),
        "dars" to mapOf(Z.FA to "درس", Z.EN to "Lesson", Z.AR to "الدرس"),
        "azmoon" to mapOf(Z.FA to "آزمون", Z.EN to "Quiz", Z.AR to "اختبار"),
        "soal" to mapOf(Z.FA to "سؤال", Z.EN to "Question", Z.AR to "سؤال"),
        "badi" to mapOf(Z.FA to "بعدی", Z.EN to "Next", Z.AR to "التالي"),
        "ghabli" to mapOf(Z.FA to "قبلی", Z.EN to "Previous", Z.AR to "السابق"),
        "safhe" to mapOf(Z.FA to "صفحه", Z.EN to "Page", Z.AR to "صفحة"),
        "entekhab_zaban" to mapOf(
            Z.FA to "انتخاب زبان",
            Z.EN to "Choose language",
            Z.AR to "اختر اللغة"
        ),
        "tamam" to mapOf(Z.FA to "تأیید", Z.EN to "OK", Z.AR to "موافق"),
        "natije" to mapOf(Z.FA to "نتیجه", Z.EN to "Result", Z.AR to "النتيجة"),
        "afarin" to mapOf(
            Z.FA to "آفرین! عالی بود",
            Z.EN to "Great job!",
            Z.AR to "أحسنت!"
        ),
        "dobare" to mapOf(
            Z.FA to "دوباره امتحان کن",
            Z.EN to "Try again",
            Z.AR to "حاول مرة أخرى"
        ),
        "dore_jadid" to mapOf(
            Z.FA to "آزمون دوباره",
            Z.EN to "Retake quiz",
            Z.AR to "إعادة الاختبار"
        ),
        "khodahafez" to mapOf(
            Z.FA to "دوباره تلاش کن، می‌تونی!",
            Z.EN to "Try again, you can do it!",
            Z.AR to "حاول مجددًا، تستطيع!"
        )
    )
}
