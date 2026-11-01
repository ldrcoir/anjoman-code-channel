package ir.anjoman.pajoohesh

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.LruCache
import org.json.JSONArray

/*
 * لایهٔ دادهٔ کتاب — ۲۱۶ صفحهٔ رندرشدهٔ PDF رسمی هفتهٔ پژوهش ۱۴۰۴
 * صفحات در assets/pages/ هستند (تزریق‌شده در زمان بیلد)؛ جلد بزرگ assets/cover.webp
 * شاخص متن برای جست‌وجو assets/matn.json
 * اگر محتوا در بیلد نباشد، اپ با پیام «محتوا نیست» باز می‌شود (بدون کرش)
 */
object Ketab {

    const val tedadSafhe = 216

    data class Bakhsh(val neshane: String, val esm: String, val tozih: String, val safhe: Int)

    // «safhe» شمارهٔ صفحه در اپ است: صفر-پایه (صفحهٔ PDF منهای ۱)
    val bakhshHa = listOf(
        Bakhsh("📕", "جلد کتاب", "معرفی دستاوردهای پژوهشی دانش‌آموزان و هنرجویان", 1),
        Bakhsh("🪪", "شناسنامهٔ کتاب", "تهیه‌کنندگان، همکاران و اطلاعات نشر", 2),
        Bakhsh("📜", "پیام‌های پیش رو", "سخن امام خمینی(ره) و مقام معظم رهبری", 3),
        Bakhsh("📑", "فهرست مطالب", "نقشهٔ راه کتاب", 4),
        Bakhsh("🗂️", "گزارش هفتهٔ پژوهش", "کمیته‌های علمی و فرایند انتخاب برگزیدگان", 6),
        Bakhsh("✍️", "دیباچه", "دکتر علی محبی — رئیس پژوهشگاه مطالعات آموزش‌وپرورش", 9),
        Bakhsh("🌱", "دورهٔ اول ابتدایی", "پایه‌های اول تا سوم — سراسر کشور", 11),
        Bakhsh("🌿", "دورهٔ دوم ابتدایی", "پایه‌های چهارم تا ششم — سراسر کشور", 30),
        Bakhsh("🎓", "دورهٔ اول متوسطه", "پایه‌های هفتم تا نهم — سراسر کشور", 70),
        Bakhsh("🎓", "دورهٔ دوم متوسطه", "پایه‌های دهم تا دوازدهم — سراسر کشور", 133),
        Bakhsh("🛠️", "فنی و حرفه‌ای", "هنرجویان فنی، حرفه‌ای و کاردانش", 196)
    )

    private val kash = object : LruCache<Int, Bitmap>(24 * 1024) {
        override fun sizeOf(key: Int, value: Bitmap): Int = value.byteCount / 1024
    }

    fun mojojod(c: Context): Boolean = try {
        c.assets.open("cover.webp").close()
        true
    } catch (_: Exception) {
        false
    }

    // بارگذاری یک صفحه با مقیاس مناسب — کش‌شده
    fun bekhon(c: Context, shomare: Int, pahnaHadaf: Int): Bitmap? {
        val ind = shomare.coerceIn(0, tedadSafhe - 1)
        val mawjod = kash.get(ind)
        if (mawjod != null && !mawjod.isRecycled) return mawjod
        return try {
            val esm = String.format(java.util.Locale.US, "pages/p%03d.webp", ind + 1)
            val had = BitmapFactory.Options()
            had.inJustDecodeBounds = true
            c.assets.open(esm).use { BitmapFactory.decodeStream(it, null, had) }
            var nomune = 1
            val hadPahna = pahnaHadaf.coerceAtLeast(320)
            while (had.outWidth / (nomune * 2) >= hadPahna) nomune *= 2
            val opt = BitmapFactory.Options()
            opt.inSampleSize = nomune
            opt.inPreferredConfig = Bitmap.Config.RGB_565
            val b = c.assets.open(esm).use { BitmapFactory.decodeStream(it, null, opt) }
                ?: return null
            kash.put(ind, b)
            b
        } catch (_: Exception) {
            null
        }
    }

    // جلد بزرگ برای صفحهٔ نخست
    fun jeld(c: Context): Bitmap? = try {
        val opt = BitmapFactory.Options()
        opt.inPreferredConfig = Bitmap.Config.RGB_565
        c.assets.open("cover.webp").use { BitmapFactory.decodeStream(it, null, opt) }
    } catch (_: Exception) {
        null
    }

    fun azadKon() {
        kash.evictAll()
    }

    // ---------- آخرین صفحهٔ خوانده‌شده ----------
    fun safheAkharBekhon(c: Context): Int =
        c.getSharedPreferences("pajoohesh", Context.MODE_PRIVATE).getInt("safheAkhar", 1)

    fun safheAkharZakhire(c: Context, shomare: Int) {
        c.getSharedPreferences("pajoohesh", Context.MODE_PRIVATE)
            .edit().putInt("safheAkhar", shomare.coerceIn(0, tedadSafhe - 1)).apply()
    }

    // ---------- شب‌خوانی (فیلتر وارونهٔ ملایم روی صفحات) ----------
    fun shabBekhon(c: Context): Boolean =
        c.getSharedPreferences("pajoohesh", Context.MODE_PRIVATE).getBoolean("shab", false)

    fun shabZakhire(c: Context, v: Boolean) {
        c.getSharedPreferences("pajoohesh", Context.MODE_PRIVATE)
            .edit().putBoolean("shab", v).apply()
    }

    // ---------- جست‌وجوی متن ----------
    private var matnHa: List<String>? = null

    fun matnSafheha(c: Context): List<String> {
        matnHa?.let { return it }
        val l = ArrayList<String>(tedadSafhe)
        try {
            val s = c.assets.open("matn.json").bufferedReader().readText()
            val j = JSONArray(s)
            for (i in 0 until j.length()) l.add(j.getString(i))
        } catch (_: Exception) {
        }
        while (l.size < tedadSafhe) l.add("")
        matnHa = l
        return l
    }

    // یکسان‌سازی حروف عربی/فارسی برای جست‌وجوی راحت
    fun sazeMatn(s: String): String {
        val sb = StringBuilder(s.length)
        for (ch in s) {
            when (ch) {
                'ي' -> sb.append('ی')
                'ك' -> sb.append('ک')
                'ة' -> sb.append('ه')
                '\u200c' -> sb.append(' ')
                ' ' -> sb.append(' ')
                else -> if (!Character.isWhitespace(ch)) sb.append(ch)
            }
        }
        val r = sb.toString()
        return r.replace(Regex(" +"), " ").trim()
    }

    // نام بخشِ یک صفحه (برای سربرگ خواناک و نتایج جست‌وجو)
    fun esmeBakhsh(shomare: Int): String {
        var natij = "جلد کتاب"
        for (b in bakhshHa) {
            if (shomare >= b.safhe) natij = b.esm
        }
        return natij
    }
}
