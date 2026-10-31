package ir.anjoman.amoozeshkotlin

/*
 * محتوای آموزشی — ۱۵ درس از صفر تا صد
 * متن درس‌ها در سه فایل DataA و DataB و DataC است؛ اینجا فقط ساختار و تمرین‌ها
 */

// یه سؤال چهارگزینه‌ای با بازخورد کوتاه برای جواب درست و غلط
data class Soal(
    val matn: String,
    val gozinehHa: List<String>,
    val shomarehSahih: Int, // اندیس گزینهٔ درست، از صفر شمرده می‌شه
    val tafoogSahih: String,
    val tafoogGhalat: String
)

// هر درس: عنوان، توضیح، نمونه کد و سؤال‌های آزمونش
data class Dars(
    val shomareh: Int,
    val onvan: String,
    val tozihat: List<String>,
    val codeNemooneh: String,
    val soalHa: List<Soal>
)

// تمرین زمین بازی: کاربر کد را کامل می‌کند تا خروجی درست بدهد
data class Tamrin(
    val onvan: String,
    val tozih: String,
    val codeShoro: String,
    val khorujiMontazer: String
)

// همهٔ محتوای آموزشی اینجا جمع شده
object DarsHa {

    val darsList: List<Dars> = darsHaAval + darsHaMiani + darsHaAkhar

    // پیدا کردن درس با شماره‌ش
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
        )
    )
}
