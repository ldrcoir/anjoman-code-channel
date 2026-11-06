package ir.anjoman.amoozeshkotlin

/*
 * ترجمهٔ محتوا — درس‌ها و تمرین‌ها به سه زبان
 * فارسی منبع اصلی است؛ جدول‌های انگلیسی و عربی در DataEn و DataAr
 * با همین ترتیب ساخته شده‌اند. هر چیزی ترجمه‌اش نبود، فارسی نشان داده می‌شود.
 *
 *   index درس در darsList == index در جدول ترجمه
 *   index سؤال در soalHa == index در soalT
 */
class SoalT(
    val matn: String,
    val gozineh: List<String>,
    val tafoogS: String,
    val tafoogG: String
)

class DarsT(
    val onvan: String,
    val tozihat: List<String>,
    val soalT: List<SoalT>,
    val nemT: List<String> = emptyList()
)

class TamrinT(
    val onvan: String,
    val tozih: String
)

object Tarjomeh {

    private var fekerEn: List<Dars>? = null
    private var fekerAr: List<Dars>? = null

    fun fehrest(z: Zaban.Z): List<Dars> = when (z) {
        Zaban.Z.FA -> DarsHa.darsList
        Zaban.Z.EN -> fekerEn ?: bazsazi(Zaban.Z.EN).also { fekerEn = it }
        Zaban.Z.AR -> fekerAr ?: bazsazi(Zaban.Z.AR).also { fekerAr = it }
    }

    fun baShomareh(z: Zaban.Z, shomareh: Int): Dars =
        fehrest(z).first { it.shomareh == shomareh }

    fun tamrinHa(z: Zaban.Z): List<Tamrin> {
        val jadval = when (z) {
            Zaban.Z.EN -> TarjomehEn.tamrinHa
            Zaban.Z.AR -> TarjomehAr.tamrinHa
            else -> return DarsHa.tamrinHa
        }
        return DarsHa.tamrinHa.mapIndexed { i, t ->
            val tr = jadval.getOrNull(i) ?: return@mapIndexed t
            Tamrin(tr.onvan, tr.tozih, t.codeShoro, t.khorujiMontazer)
        }
    }

    private fun bazsazi(z: Zaban.Z): List<Dars> {
        val jadval: List<DarsT> = when (z) {
            Zaban.Z.EN -> TarjomehEn.darsHa
            else -> TarjomehAr.darsHa
        }
        return DarsHa.darsList.mapIndexed { i, d ->
            val t = jadval.getOrNull(i) ?: return@mapIndexed d
            Dars(
                d.shomareh,
                t.onvan,
                t.tozihat,
                d.codeNemooneh,
                d.soalHa.mapIndexed { j, s ->
                    val st = t.soalT.getOrNull(j) ?: return@mapIndexed s
                    Soal(st.matn, st.gozineh, s.shomarehSahih, st.tafoogS, st.tafoogG)
                },
                d.nemoonehHa.mapIndexed { j, nem ->
                    val nemT = t.nemT.getOrNull(j)
                    if (nemT == null) nem else Nemooneh(nemT, nem.code)
                }
            )
        }
    }
}
