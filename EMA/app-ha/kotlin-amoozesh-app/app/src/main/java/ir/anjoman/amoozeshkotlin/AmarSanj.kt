package ir.anjoman.amoozeshkotlin

import android.content.Context
import android.os.Build
import android.telephony.TelephonyManager
import android.util.DisplayMetrics
import org.json.JSONArray
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

/*
 * آمارگیر سبک EmaRoid — نسخهٔ «حرمت‌دار»
 *
 * فقط از مجوز INTERNET استفاده می‌کند که اپ‌های آنلاین همیشه دارند.
 * IMEI / شماره تلفن / مخاطبین / موقعیت GPS → هیچ‌کدام جمع نمی‌شود.
 *
 * دو نوع آمار:
 *   ۱) ضربان: هر ۲۴ ساعت یک گزارش از وضعیت اپ (باز شدن، دستگاه، کشور...)
 *   ۲) رویدادها: هر کاری که کاربر می‌کند — درس باز شده، هر جواب آزمون،
 *      تمرین حل‌شده، اجرای کد، تغییر زبان/تم، دیدن داستان — همه با sabt()
 *      ثبت و همراه ضربان بعدی یک‌جا فرستاده می‌شود.
 * ارسال کاملاً بی‌صدا؛ اگر اینترنت نبود، بی‌سروصدا دفعهٔ بعد.
 */
object AmarSanj {

    private const val FILE = "amar_emaroid"
    private const val KEY_AKHARIN = "akharin_ersal_ms"
    private const val KEY_TEDAD_BAZ = "tedad_baz_shodan"
    private const val KEY_RUYDADHA = "ruydadha"
    private const val HAD_RUYDAD = 400

    fun bego(appContext: Context) {
        try {
            val p = appContext.getSharedPreferences(FILE, Context.MODE_PRIVATE)
            val tedad = p.getInt(KEY_TEDAD_BAZ, 0) + 1
            p.edit().putInt(KEY_TEDAD_BAZ, tedad).apply()

            val alan = System.currentTimeMillis()
            val fasele = EmaRoidConfig.AMAR_FASELE_SAAT * 3600_000L
            val akharin = p.getLong(KEY_AKHARIN, 0L)
            if (akharin != 0L && alan - akharin < fasele) return  // همین تازگی فرستاده‌ایم
            p.edit().putLong(KEY_AKHARIN, alan).apply()

            val json = besaz(appContext, tedad)
            thread(name = "amar-emaroid") {
                ersal(appContext, json)
            }
        } catch (_: Exception) {
            // آمارگیر هرگز نباید اپ را بترکاند
        }
    }

    // ثبت یک رویداد — بی‌صدا و سبک؛ همراه ضربان بعدی می‌رود
    fun sabt(appContext: Context, rooydad: String, etelaat: JSONObject? = null) {
        try {
            val p = appContext.getSharedPreferences(FILE, Context.MODE_PRIVATE)
            val list = JSONArray(p.getString(KEY_RUYDADHA, "[]") ?: "[]")
            val e = JSONObject()
            e.put("e", rooydad)
            e.put("ts", System.currentTimeMillis())
            if (etelaat != null) e.put("d", etelaat)
            list.put(e)
            // نگه‌داشتن سقف — رویدادهای کهنه دور ریخته می‌شوند
            val bishtar = list.length() - HAD_RUYDAD
            if (bishtar > 0) {
                val taze = JSONArray()
                for (i in bishtar until list.length()) taze.put(list.get(i))
                p.edit().putString(KEY_RUYDADHA, taze.toString()).apply()
            } else {
                p.edit().putString(KEY_RUYDADHA, list.toString()).apply()
            }
        } catch (_: Exception) {
        }
    }

    // ساخت JSON آمار — برای آزمون واحد جدا شده
    fun besaz(
        esmPake: String,
        neskhEsm: String,
        neskhAndruin: Int,
        model: String,
        sazande: String,
        boltKaf: Int,
        bolandiKaf: Int,
        neskhKod: Int,
        keshvar: String,
        zaban: String,
        tedadBaz: Int,
        zamandar: Long,
        ruydadHa: JSONArray = JSONArray()
    ): JSONObject {
        return JSONObject().apply {
            put("app", esmPake)
            put("v_name", neskhEsm)
            put("v_code", neskhKod)
            put("android", neskhAndruin)
            put("model", model)
            put("maker", sazande)
            put("screen", "$boltKaf x $bolandiKaf")
            put("country", keshvar)
            put("lang", zaban)
            put("opens", tedadBaz)
            put("ts", zamandar)
            put("brand", EmaRoidConfig.BRAND)
            put("events", ruydadHa)
        }
    }

    private fun besaz(c: Context, tedad: Int): JSONObject {
        val pm = c.packageManager.getPackageInfo(c.packageName, 0)
        val tm = c.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager
        val keshvar = listOf(
            tm?.networkCountryIso, tm?.simCountryIso,
            c.resources.configuration.locales[0].country
        ).firstOrNull { !it.isNullOrBlank() } ?: "ir"
        val zaban = c.resources.configuration.locales[0].language
        val m: DisplayMetrics = c.resources.displayMetrics

        // رویدادهای ذخیره‌شده را می‌فرستیم و خالی می‌کنیم
        val p = c.getSharedPreferences(FILE, Context.MODE_PRIVATE)
        val ruydadHa = JSONArray(p.getString(KEY_RUYDADHA, "[]") ?: "[]")
        p.edit().putString(KEY_RUYDADHA, "[]").apply()

        return besaz(
            esmPake = c.packageName,
            neskhEsm = pm.versionName ?: "?",
            neskhAndruin = Build.VERSION.SDK_INT,
            model = Build.MODEL ?: "?",
            sazande = Build.MANUFACTURER ?: "?",
            boltKaf = m.widthPixels,
            bolandiKaf = m.heightPixels,
            neskhKod = if (Build.VERSION.SDK_INT >= 28) {
                @Suppress("DEPRECATION_ERROR") pm.longVersionCode.toInt()
            } else {
                @Suppress("DEPRECATION") pm.versionCode
            },
            keshvar = keshvar.lowercase(),
            zaban = zaban,
            tedadBaz = tedad,
            zamandar = System.currentTimeMillis(),
            ruydadHa = ruydadHa
        )
    }

    private fun ersal(c: Context, json: JSONObject) {
        try {
            val con = URL(EmaRoidConfig.AMAR_URL).openConnection() as HttpURLConnection
            con.requestMethod = "POST"
            con.connectTimeout = 6000
            con.readTimeout = 6000
            con.doOutput = true
            con.setRequestProperty("Content-Type", "application/json; charset=utf-8")
            con.setRequestProperty("X-EmaRoid-App", c.packageName)
            val out = OutputStreamWriter(con.outputStream, Charsets.UTF_8)
            out.write(json.toString())
            out.flush()
            out.close()
            con.responseCode // خواندن کد پاسخ تا درخواست واقعاً برود
            con.disconnect()
        } catch (_: Exception) {
            // بی‌صدا — دفعهٔ بعد دوباره تلاش می‌شود
        }
    }
}
