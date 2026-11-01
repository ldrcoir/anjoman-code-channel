package ir.anjoman.sandogh

import android.content.Context
import org.json.JSONObject

/*
 * بارگذار کتابچهٔ صندوق از assets/ledger.json
 * داده از فایل PDF رسمی کتابچه (مهر ۱۴۰۵) استخراج و در زمان ساخت اپ تزریق می‌شود.
 * اگر فایل نباشد، اپ با پیام «داده‌ای نیست» باز می‌شود (بدون کرش).
 */

data class Shakhs(
    val esm: String,
    val yuro: Double,
    val dolar: Double,
    val naqsim: Double,
    val baghimande: Double,
    val seke: Double,
    val tetr: Double,
    val usdc: Double,
    val dai: Double,
    val sahm: Double,
    val pasandaz: Long,
    val tasvie: Double,
    val tasvieShode: Double?
)

data class DaneHa(
    val onvan: String,
    val tarikh: String,
    val tozih: String,
    val shakhs: List<Shakhs>,
    val majmoo: Shakhs?,
    val tetrEsm: List<String>,
    val tetrMegdar: List<Double>,
    val tetrMajmoo: Double
)

object Ketabche {

    private var cache: DaneHa? = null

    fun bekhon(context: Context): DaneHa {
        cache?.let { return it }
        try {
            val matn = context.assets.open("ledger.json").bufferedReader().use { it.readText() }
            val j = JSONObject(matn)
            val meta = j.getJSONObject("meta")

            val shakhs = ArrayList<Shakhs>()
            val jShakhs = j.getJSONArray("shakhs")
            for (i in 0 until jShakhs.length()) {
                val o = jShakhs.getJSONObject(i)
                shakhs.add(
                    Shakhs(
                        o.getString("esm"),
                        o.optDouble("yuro", 0.0),
                        o.optDouble("dolar", 0.0),
                        o.optDouble("naqsim", 0.0),
                        o.optDouble("baghimande", 0.0),
                        o.optDouble("seke", 0.0),
                        o.optDouble("tetr", 0.0),
                        o.optDouble("usdc", 0.0),
                        o.optDouble("dai", 0.0),
                        o.optDouble("sahm", 0.0),
                        o.optLong("pasandaz", 0),
                        o.optDouble("tasvie", 0.0),
                        if (o.isNull("tasvieShode")) null else o.optDouble("tasvieShode", 0.0)
                    )
                )
            }

            var majmoo: Shakhs? = null
            if (j.has("majmoo") && !j.isNull("majmoo")) {
                val m = j.getJSONObject("majmoo")
                majmoo = Shakhs(
                    "مجموع",
                    m.optDouble("yuro", 0.0),
                    m.optDouble("dolar", 0.0),
                    m.optDouble("naqsim", 0.0),
                    m.optDouble("baghimande", 0.0),
                    m.optDouble("seke", 0.0),
                    m.optDouble("tetr", 0.0),
                    m.optDouble("usdc", 0.0),
                    m.optDouble("dai", 0.0),
                    m.optDouble("sahm", 0.0),
                    m.optLong("pasandaz", 0),
                    m.optDouble("tasvie", 0.0),
                    null
                )
            }

            val tetrEsm = ArrayList<String>()
            val tetrMegdar = ArrayList<Double>()
            var tetrMajmoo = 0.0
            if (j.has("tetrKharidariShodeAzSayerin")) {
                val t = j.getJSONObject("tetrKharidariShodeAzSayerin")
                val daneha = t.getJSONArray("daneha")
                for (i in 0 until daneha.length()) {
                    val o = daneha.getJSONObject(i)
                    tetrEsm.add(o.getString("esm"))
                    tetrMegdar.add(o.getDouble("megdar"))
                }
                tetrMajmoo = t.optDouble("majmoo", 0.0)
            }

            val d = DaneHa(
                meta.optString("onvan", "وضعیت صندوق پس‌انداز"),
                meta.optString("tarikh", ""),
                meta.optString("tozih", ""),
                shakhs,
                majmoo,
                tetrEsm,
                tetrMegdar,
                tetrMajmoo
            )
            cache = d
            return d
        } catch (_: Exception) {
            val d = DaneHa(
                "وضعیت صندوق پس‌انداز", "", "داده‌ای در این نسخه قرار نگرفته است.",
                emptyList(), null, emptyList(), emptyList(), 0.0
            )
            cache = d
            return d
        }
    }
}
