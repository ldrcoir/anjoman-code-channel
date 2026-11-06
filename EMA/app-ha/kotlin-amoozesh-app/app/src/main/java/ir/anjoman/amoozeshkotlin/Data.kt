package ir.anjoman.amoozeshkotlin

// یه سؤال چهارگزینه‌ای با بازخورد کوتاه برای جواب درست و غلط
data class Soal(
    val matn: String,
    val gozinehHa: List<String>,
    val shomarehSahih: Int, // اندیس گزینهٔ درست، از صفر شمرده می‌شه
    val tafoogSahih: String,
    val tafoogGhalat: String
)

// یک نمونهٔ کد عنوان‌دار — برای درس‌هایی که چند مثال جدا دارند
data class Nemooneh(
    val onvan: String,
    val code: String
)

// هر درس: عنوان، توضیح، نمونه کد و سؤال‌های آزمونش
data class Dars(
    val shomareh: Int,
    val onvan: String,
    val tozihat: List<String>,
    val codeNemooneh: String,
    val soalHa: List<Soal>,
    val nemoonehHa: List<Nemooneh> = emptyList()
)

// تمرین زمین بازی: کاربر کد را کامل می‌کند تا خروجی درست بدهد
data class Tamrin(
    val onvan: String,
    val tozih: String,
    val codeShoro: String,
    val khorujiMontazer: String
)

/*
 * همهٔ محتوای آموزشی اینجا جمع می‌شود.
 * درس‌ها از پنج فصل می‌آیند: مقدمه + اول + میانی + آخر + پیشرفته
 * و شماره‌ها یک‌دست می‌شوند (۱ تا ۲۵).
 * سؤال‌ها و نمونه‌کدهای اضافهٔ SoalEzafeHa هم به هر درس وصل می‌شوند.
 */
object DarsHa {

    val darsList: List<Dars> by lazy {
        (darsHaMoghaddameh + darsHaAval + darsHaMiani + darsHaAkhar + darsHaPishrafteh)
            .mapIndexed { i, d -> d.copy(shomareh = i + 1) }
            .map { d ->
                d.copy(
                    soalHa = d.soalHa + (SoalEzafeHa.soalHaEzafe[d.shomareh] ?: emptyList()),
                    nemoonehHa = d.nemoonehHa + (SoalEzafeHa.nemooonehBish[d.shomareh] ?: emptyList())
                )
            }
    }

    fun baShomareh(shomareh: Int): Dars = darsList.first { it.shomareh == shomareh }

    // ---------- تمرین‌های زمین بازی ----------
    val tamrinHa: List<Tamrin> = listOf(
        Tamrin(
            "اولین سلام",
            "برنامه‌ای بنویس که عبارت «سلام دنیا!» را چاپ کند.",
            """
            fun main() {
                // با println شروع کن
            }
            """.trimIndent(),
            "سلام دنیا!"
        ),
        Tamrin(
            "جمع دو عدد",
            "دو متغیر a = 12 و b = 30 بساز و مجموعشان را چاپ کن.",
            """
            fun main() {
                val a = 12
                // متغیر b را بساز و بعد مجموع را چاپ کن
            }
            """.trimIndent(),
            "42"
        ),
        Tamrin(
            "زوج یا فرد",
            "متغیر adad = 7 وجود دارد. اگر زوج بود «زوج» و اگر فرد بود «فرد» چاپ کن.",
            """
            fun main() {
                val adad = 7
                // با if و % تصمیم بگیر
            }
            """.trimIndent(),
            "فرد"
        ),
        Tamrin(
            "شمارش اعداد",
            "اعداد ۱ تا ۵ را با حلقهٔ for چاپ کن؛ هر عدد در یک خط.",
            """
            fun main() {
                // یک حلقهٔ for از ۱ تا ۵ بزن و هر عدد را println کن
            }
            """.trimIndent(),
            "1\n2\n3\n4\n5"
        ),
        Tamrin(
            "جدول ضرب سه",
            "مضرب‌های ۳ از ۳ تا ۱۵ را در یک خط با فاصله چاپ کن: 3 6 9 12 15",
            """
            fun main() {
                // حلقه بزن، ولی این‌بار با print و فاصله، نه println
            }
            """.trimIndent(),
            "3 6 9 12 15"
        ),
        Tamrin(
            "تابع مربع",
            "تابعی به اسم morabaa بنویس که مربع عدد بگیرد و برگرداند؛ بعد morabaa(6) را چاپ کن.",
            """
            fun morabaa(x: Int): Int {
                // جواب را برگردان
            }

            fun main() {
                // morabaa(6) را چاپ کن
            }
            """.trimIndent(),
            "36"
        ),
        Tamrin(
            "مجموع نمره‌ها",
            "لیست نمره‌ها داده شده. مجموع همه را با حلقه حساب کن و چاپ کن.",
            """
            fun main() {
                val nomreHa = listOf(18, 15, 20, 12)
                var majmoo = 0
                // با حلقه روی لیست برو و جمع کن
            }
            """.trimIndent(),
            "65"
        ),
        Tamrin(
            "بزرگ‌ترین عدد",
            "لیست adadHa داده شده؛ بزرگ‌ترین عدد داخلش را پیدا کن و چاپ کن.",
            """
            fun main() {
                val adadHa = listOf(12, 7, 25, 3, 19)
                // بزرگ‌ترین را با حلقه یا max پیدا کن و println کن
            }
            """.trimIndent(),
            "25"
        ),
        Tamrin(
            "شمارش معکوس",
            "از ۳ تا ۱ را هر عدد در یک خط چاپ کن و بعد «پرتاب!» را چاپ کن.",
            """
            fun main() {
                // با حلقه یا سه بار println، از ۳ تا ۱ و در پایان «پرتاب!»
            }
            """.trimIndent(),
            "3\n2\n1\nپرتاب!"
        ),
        Tamrin(
            "سلام به همه",
            "لیست esmHa داده شده؛ برای هر اسم یک خط «سلام X» چاپ کن؛ X اسم است.",
            """
            fun main() {
                val esmHa = listOf("علی", "سارا", "رضا")
                // با حلقه روی esmHa برو و «سلام X» چاپ کن
            }
            """.trimIndent(),
            "سلام علی\nسلام سارا\nسلام رضا"
        )
    )
}
