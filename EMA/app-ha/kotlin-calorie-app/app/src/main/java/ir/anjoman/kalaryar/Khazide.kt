package ir.anjoman.kalaryar

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

/*
 * حافظهٔ اپ — همه‌چیز آفلاین با SharedPreferences و JSON
 * پروفایل، دفترچهٔ روزانه، آب، ورزش و وزن
 */

data class Profil(
    val jens: String,        // "mard" یا "zan"
    val sen: Int,
    val ghad: Int,           // سانتی‌متر
    val vazn: Double,        // کیلوگرم
    val faaliat: Int,        // ۱ تا ۴ (کم‌تحرک تا خیلی فعال)
    val nooHadaf: String,    // "kam" کاهش وزن، "sabet" تثبیت، "ziaad" افزایش
    val vaznHadaf: Double
)

// یک ردیف در دفترچه: چه غذایی، چند گرم، چه وعده‌ای
data class Varedi(
    val esm: String,
    val gram: Int,
    val kalar: Int,
    val protein: Double,
    val karbohidrat: Double,
    val charbi: Double,
    val vohed: String        // sohbat / nahaar / sham / miani
)

data class VartamVaredi(
    val esm: String,
    val daghighe: Int,
    val kalar: Int
)

object Khazide {

    private fun p(context: Context) =
        context.getSharedPreferences("kalaryar", Context.MODE_PRIVATE)

    // ---------- پروفایل ----------
    fun profilZakhire(context: Context, profil: Profil) {
        val j = JSONObject()
        j.put("jens", profil.jens)
        j.put("sen", profil.sen)
        j.put("ghad", profil.ghad)
        j.put("vazn", profil.vazn)
        j.put("faaliat", profil.faaliat)
        j.put("nooHadaf", profil.nooHadaf)
        j.put("vaznHadaf", profil.vaznHadaf)
        p(context).edit().putString("profil", j.toString()).apply()
    }

    fun profilHast(context: Context): Boolean =
        p(context).contains("profil")

    fun profilBekhon(context: Context): Profil? {
        val s = p(context).getString("profil", null) ?: return null
        val j = JSONObject(s)
        return Profil(
            j.getString("jens"),
            j.getInt("sen"),
            j.getInt("ghad"),
            j.getDouble("vazn"),
            j.getInt("faaliat"),
            j.getString("nooHadaf"),
            j.getDouble("vaznHadaf")
        )
    }

    // ---------- اهداف محاسبه‌شده ----------
    fun hadafZakhire(context: Context, kalar: Int, protein: Int, karb: Int, charbi: Int, ab: Int) {
        p(context).edit()
            .putInt("hadaf_kalar", kalar)
            .putInt("hadaf_protein", protein)
            .putInt("hadaf_karb", karb)
            .putInt("hadaf_charbi", charbi)
            .putInt("hadaf_ab", ab)
            .apply()
    }

    fun hadafKalar(context: Context): Int = p(context).getInt("hadaf_kalar", 2000)
    fun hadafProtein(context: Context): Int = p(context).getInt("hadaf_protein", 100)
    fun hadafKarb(context: Context): Int = p(context).getInt("hadaf_karb", 220)
    fun hadafCharbi(context: Context): Int = p(context).getInt("hadaf_charbi", 65)
    fun hadafAb(context: Context): Int = p(context).getInt("hadaf_ab", 8) // لیوان ۲۵۰ میلی

    // ---------- دفترچهٔ روزانه ----------
    private fun vorodiHaJson(context: Context, kelid: String): JSONArray {
        val s = p(context).getString("log_$kelid", null) ?: return JSONArray()
        return JSONArray(s)
    }

    fun vorodiHaRooz(context: Context, kelid: String): List<Varedi> {
        val j = vorodiHaJson(context, kelid)
        val kh = ArrayList<Varedi>()
        for (i in 0 until j.length()) {
            val o = j.getJSONObject(i)
            kh.add(
                Varedi(
                    o.getString("esm"),
                    o.getInt("gram"),
                    o.getInt("kalar"),
                    o.getDouble("p"),
                    o.getDouble("c"),
                    o.getDouble("f"),
                    o.getString("vohed")
                )
            )
        }
        return kh
    }

    fun vorodiEzafe(context: Context, kelid: String, v: Varedi) {
        val j = vorodiHaJson(context, kelid)
        val o = JSONObject()
        o.put("esm", v.esm)
        o.put("gram", v.gram)
        o.put("kalar", v.kalar)
        o.put("p", v.protein)
        o.put("c", v.karbohidrat)
        o.put("f", v.charbi)
        o.put("vohed", v.vohed)
        j.put(o)
        p(context).edit().putString("log_$kelid", j.toString()).apply()
    }

    fun vorodiHazf(context: Context, kelid: String, index: Int) {
        val j = vorodiHaJson(context, kelid)
        if (index in 0 until j.length()) {
            val jTaze = JSONArray()
            for (i in 0 until j.length()) {
                if (i != index) jTaze.put(j.getJSONObject(i))
            }
            p(context).edit().putString("log_$kelid", jTaze.toString()).apply()
        }
    }

    // جمع امروز: کالری خورده‌شده و درشت‌مغذی‌ها
    data class MajmooRooz(
        val kalar: Int,
        val protein: Double,
        val karbohidrat: Double,
        val charbi: Double
    )

    fun majmooRooz(context: Context, kelid: String): MajmooRooz {
        var k = 0
        var pr = 0.0
        var ka = 0.0
        var ch = 0.0
        for (v in vorodiHaRooz(context, kelid)) {
            k += v.kalar
            pr += v.protein
            ka += v.karbohidrat
            ch += v.charbi
        }
        return MajmooRooz(k, pr, ka, ch)
    }

    // کالری سوزانده‌شده با ورزش امروز
    fun varTamRooz(context: Context, kelid: String): List<VartamVaredi> {
        val s = p(context).getString("var_$kelid", null) ?: return emptyList()
        val j = JSONArray(s)
        val kh = ArrayList<VartamVaredi>()
        for (i in 0 until j.length()) {
            val o = j.getJSONObject(i)
            kh.add(VartamVaredi(o.getString("esm"), o.getInt("d"), o.getInt("k")))
        }
        return kh
    }

    fun varTamEzafe(context: Context, kelid: String, v: VartamVaredi) {
        val s = p(context).getString("var_$kelid", null)
        val j = if (s == null) JSONArray() else JSONArray(s)
        val o = JSONObject()
        o.put("esm", v.esm)
        o.put("d", v.daghighe)
        o.put("k", v.kalar)
        j.put(o)
        p(context).edit().putString("var_$kelid", j.toString()).apply()
    }

    fun varTamSozande(context: Context, kelid: String): Int {
        var jam = 0
        for (v in varTamRooz(context, kelid)) jam += v.kalar
        return jam
    }

    // ---------- آب ----------
    fun abRooz(context: Context, kelid: String): Int = p(context).getInt("ab_$kelid", 0)

    fun abTaghir(context: Context, kelid: String, taghir: Int) {
        val p = p(context)
        val fel = (p.getInt("ab_$kelid", 0) + taghir).coerceAtLeast(0)
        p.edit().putInt("ab_$kelid", fel).apply()
    }

    // ---------- وزن ----------
    fun vaznEzafe(context: Context, kelidRooz: String, vazn: Double) {
        val s = p(context).getString("vaznha", null)
        val j = if (s == null) JSONArray() else JSONArray(s)
        // اگر همان روز قبلاً ثبت شده، فقط عددش را عوض کن
        var jaYgazehShod = false
        for (i in 0 until j.length()) {
            val o = j.getJSONObject(i)
            if (o.getString("r") == kelidRooz) {
                o.put("v", vazn)
                jaYgazehShod = true
            }
        }
        if (!jaYgazehShod) {
            val o = JSONObject()
            o.put("r", kelidRooz)
            o.put("v", vazn)
            j.put(o)
        }
        p(context).edit().putString("vaznha", j.toString()).apply()
        // وزن پروفایل هم به‌روز شود
        val profil = profilBekhon(context)
        if (profil != null && vazn != profil.vazn) {
            profilZakhire(
                context,
                Profil(profil.jens, profil.sen, profil.ghad, vazn, profil.faaliat, profil.nooHadaf, profil.vaznHadaf)
            )
        }
    }

    // فهرست وزن‌ها: قدیمی به جدید
    fun vaznHa(context: Context): List<Pair<String, Double>> {
        val s = p(context).getString("vaznha", null) ?: return emptyList()
        val j = JSONArray(s)
        val kh = ArrayList<Pair<String, Double>>()
        for (i in 0 until j.length()) {
            val o = j.getJSONObject(i)
            kh.add(o.getString("r") to o.getDouble("v"))
        }
        return kh.sortedBy { it.first }
    }

    fun vaznAkhari(context: Context): Double? = vaznHa(context).lastOrNull()?.second

    // ---------- استریک: چند روز پیوسته ثبت کرده؟ ----------
    fun estrik(context: Context): Int {
        var tedad = 0
        for (pas in 0 until 60) {
            val kelid = Abzar.kelidRooz(pas)
            val khord = vorodiHaRooz(context, kelid).isNotEmpty()
            val ab = abRooz(context, kelid) > 0
            val varTam = varTamRooz(context, kelid).isNotEmpty()
            if (khord || ab || varTam) tedad++ else break
        }
        return tedad
    }

    // ---------- غذاهای دلخواه کاربر (نسخهٔ ۲ — گردش ۵۳) ----------
    // هرکس غذای خودش را با کالری و مقدار مشخص اضافه می‌کند؛ در همین حافظهٔ آفلاین می‌ماند
    fun ghazayeManBekhon(context: Context): List<Ghaza> {
        val kh = ArrayList<Ghaza>()
        try {
            val matn = p(context).getString("ghazaye_man", "[]") ?: "[]"
            val j = JSONArray(matn)
            for (i in 0 until j.length()) {
                val o = j.getJSONObject(i)
                kh.add(
                    Ghaza(
                        o.getString("esm"),
                        "غذای من",
                        o.optInt("kalar", 0),
                        o.optDouble("protein", 0.0),
                        o.optDouble("karbohidrat", 0.0),
                        o.optDouble("charbi", 0.0),
                        o.optString("porzionEsm", "یک سهم"),
                        o.optInt("porzionGram", 100)
                    )
                )
            }
        } catch (_: Exception) {
        }
        return kh
    }

    fun ghazayeManEzafe(context: Context, g: Ghaza) {
        try {
            val j = JSONArray(p(context).getString("ghazaye_man", "[]") ?: "[]")
            val o = JSONObject()
            o.put("esm", g.esm)
            o.put("kalar", g.kalar)
            o.put("protein", g.protein)
            o.put("karbohidrat", g.karbohidrat)
            o.put("charbi", g.charbi)
            o.put("porzionEsm", g.porzionEsm)
            o.put("porzionGram", g.porzionGram)
            j.put(o)
            p(context).edit().putString("ghazaye_man", j.toString()).apply()
        } catch (_: Exception) {
        }
    }

    fun ghazayeManHazf(context: Context, esm: String) {
        try {
            val j = JSONArray(p(context).getString("ghazaye_man", "[]") ?: "[]")
            val no = JSONArray()
            for (i in 0 until j.length()) {
                val o = j.getJSONObject(i)
                if (o.getString("esm") != esm) no.put(o)
            }
            p(context).edit().putString("ghazaye_man", no.toString()).apply()
        } catch (_: Exception) {
        }
    }

    // ---------- تنظیمات هوش مصنوعی عکس (نسخهٔ ۲) ----------
    fun aiAPI(context: Context): String =
        p(context).getString("ai_api", "https://api.z.ai/api/paas/v4/chat/completions") ?: ""

    fun aiKelid(context: Context): String = p(context).getString("ai_kelid", "") ?: ""
    fun aiModel(context: Context): String = p(context).getString("ai_model", "glm-4.5v") ?: ""

    fun aiZakhire(context: Context, api: String, kelid: String, model: String) {
        p(context).edit().putString("ai_api", api.trim())
            .putString("ai_kelid", kelid.trim())
            .putString("ai_model", model.trim()).apply()
    }
}
