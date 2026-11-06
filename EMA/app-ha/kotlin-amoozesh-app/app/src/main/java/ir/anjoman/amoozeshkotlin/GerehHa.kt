package ir.anjoman.amoozeshkotlin

/*
 * گره‌ها و مقادیر زمین بازی کد
 * همهٔ اسم‌ها فینگلیش است تا برای دانش‌آموزها خوانا باشد
 */

// خطاهای زبان که به کاربر نشان داده می‌شود
class KhataKotlin(val peygham: String, val khat: Int = 0) : Exception(peygham)

// برای شکستن حلقه و برگرداندن جواب از داخل تابع
internal object ParvazeShekan : RuntimeException()
internal object ParvazeEdame : RuntimeException()
internal class ParvazeBargardan(val arzesh: Arzesh) : RuntimeException()

// ---------- مقادیر زمان اجرا ----------
sealed class Arzesh

object Hich : Arzesh() // همون null

data class Sahih(val meghdar: Int) : Arzesh()
data class Ashari(val meghdar: Double) : Arzesh()
data class Matn(val meghdar: String) : Arzesh()
data class Mantri(val meghdar: Boolean) : Arzesh()
data class Fehrest(val javabe: MutableList<Arzesh>) : Arzesh()

// بازهٔ عددی مثل 1..5 یا 5 downTo 1
data class Bazeh(val az: Int, val ta: Int, val ghadam: Int = 1) : Arzesh() {
    fun darBazeh(x: Int): Boolean {
        return if (ghadam > 0) x >= az && x <= ta else x >= ta && x <= az
    }
    fun hame(): List<Int> {
        val kh = ArrayList<Int>()
        if (ghadam > 0) {
            var i = az
            while (i <= ta) { kh.add(i); i += ghadam }
        } else {
            var i = az
            while (i >= ta) { kh.add(i); i += ghadam }
        }
        return kh
    }
}

// تعریف کلاس که موقع اجرا ساخته می‌شود
class SinfTarif(val esm: String,
                val vijegiHa: List<VijegiTarif>,
                val raveshHa: Map<String, TabTarif>)

class VijegiTarif(val esm: String, val noo: String?, val avazSho: Boolean, val pishfarz: Gereh?)

// تعریف تابع
class TabTarif(val esm: String,
               val parametrHa: List<ParamTarif>,
               val khoruji: String?,
               val badane: Gereh?,          // یا بلوک یا عبارتِ تک‌خطی
               val badaneEbarat: Gereh?)    // برای fun x() = ebarat

class ParamTarif(val esm: String, val noo: String?, val pishfarz: Gereh?)

// شیء ساخته‌شده از یک کلاس
data class Shey(val sinf: SinfTarif, val mahalHa: MutableMap<String, Arzesh>) : Arzesh()

// ---------- محیط (لایه‌های متغیرها) ----------
class Mohit(val padar: Mohit?) {
    val moteghayerHa = HashMap<String, Arzesh>()
    val ghabelTaghir = HashSet<String>()
    var daryaft: Shey? = null // برای متدهای کلاس

    fun peydaKon(esm: String): Arzesh? {
        var m: Mohit? = this
        while (m != null) {
            val v = m.moteghayerHa[esm]
            if (v != null) return v
            m = m.padar
        }
        // بعدش ویژگی‌های شیء جاری
        var m2: Mohit? = this
        while (m2 != null) {
            val d = m2.daryaft
            if (d != null && d.mahalHa.containsKey(esm)) return d.mahalHa[esm]
            m2 = m2.padar
        }
        return null
    }

    // کجا تعریف شده؟ برای خطای val و برای تغییر مقدار
    fun mohitElan(esm: String): Mohit? {
        var m: Mohit? = this
        while (m != null) {
            if (m.moteghayerHa.containsKey(esm)) return m
            m = m.padar
        }
        return null
    }

    fun sheyElan(esm: String): Shey? {
        var m: Mohit? = this
        while (m != null) {
            val d = m.daryaft
            if (d != null && d.mahalHa.containsKey(esm)) return d
            m = m.padar
        }
        return null
    }
}

// ---------- گره‌های درخت کد ----------
sealed class Gereh

// دستورها
class Blok(val dastoorHa: MutableList<Gereh>) : Gereh()
class VarVar(val esm: String, val avazSho: Boolean, val noo: String?, val meghdar: Gereh, val khat: Int) : Gereh()
class Gozashtan(val hadaf: Gereh, val amalgar: String, val meghdar: Gereh, val khat: Int) : Gereh()
class EbaratDastoor(val ebarat: Gereh) : Gereh()
class AgarGereh(val sharT: Gereh, val badane: Gereh, val vagarna: Gereh?, val khat: Int) : Gereh()
class HalateVaqti(val sharT: Gereh?, val darBazeh: Boolean, val pishfarzAst: Boolean) // شرطِ هر شاخهٔ when
class VaqtiGereh(val sojekt: Gereh?, val sharTha: List<HalateVaqti>, val badaneHa: List<Gereh>, val khat: Int) : Gereh()
class BarayeGereh(val esm: String, val bazeh: Gereh, val badane: Gereh, val khat: Int) : Gereh()
class TaVaqtiGereh(val sharT: Gereh, val badane: Gereh, val khat: Int) : Gereh()
class BargardanGereh(val meghdar: Gereh?, val khat: Int) : Gereh()
object ShekanGereh : Gereh()
object EdameGereh : Gereh()
class TabGereh(val tab: TabTarif, val khat: Int) : Gereh()
class SinfGereh(val sinf: SinfTarif, val khat: Int) : Gereh()

// عبارت‌ها
class Dooi(val chap: Gereh, val amalgar: String, val rast: Gereh, val khat: Int) : Gereh()
class YeganehPish(val amalgar: String, val sojekt: Gereh, val khat: Int) : Gereh()
class Sedazadan(val esm: String, val vargha: List<Gereh>, val khat: Int) : Gereh()
class Dastresi(val sojekt: Gereh, val esm: String, val khat: Int, val aman: Boolean = false) : Gereh()
class Andis(val sojekt: Gereh, val andis: Gereh, val khat: Int) : Gereh()
class Parche(val arzesh: Arzesh) : Gereh()
class Moteghayyer(val esm: String, val khat: Int) : Gereh()
class ParcheMatn(val posheHa: List<PosheMatn>, val khat: Int) : Gereh() // رشته با $
class PosheMatn(val sabetAst: Boolean, val sabet: String, val ebarat: Gereh?)
class BazehSaz(val az: Gereh, val ta: Gereh, val nooBazeh: String, val ghadam: Gereh?, val khat: Int) : Gereh() // nooBazeh: ".." / "until" / "downTo"
class PorsTaNolli(val sharT: Gereh, val vagarna: Gereh, val khat: Int) : Gereh() // ?:
class NemudNolli(val sojekt: Gereh, val khat: Int) : Gereh() // !!
class EbaratAgar(val sharT: Gereh, val va: Gereh, val vagarna: Gereh, val khat: Int) : Gereh() // if به‌عنوان عبارت
class InChap(val sojekt: Gereh, val bazeh: Gereh, val khat: Int) : Gereh() // x in 1..5 به‌عنوان عبارت
