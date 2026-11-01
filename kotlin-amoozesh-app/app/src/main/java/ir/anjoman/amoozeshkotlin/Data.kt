package ir.anjoman.amoozeshkotlin

/*
 * محتوای آموزشی — ۲۵ درس از مقدمه تا پیشرفته
 * متن درس‌ها: DataMoghaddameh (مقدمه) + DataA و DataB (پایه و میانی) + DataC (شیءگرا) + DataD (پیشرفته)
 * اینجا ساختار، فصل‌ها، شماره‌گذاری تازه و تمرین‌های زمین بازی جمع شده
 */

// یه سؤال چهارگزینه‌ای با بازخورد کوتاه برای جواب درست و غلط
data class Soal(
    val matn: String,
    val gozinehHa: List<String>,
    val shomarehSahih: Int, // اندیس گزینهٔ درست، از صفر شمرده می‌شه
    val tafoogSahih: String,
    val tafoogGhalat: String
)

// یک نمونهٔ کد اضافه برای درس‌ها (عنوان + کد)
data class Nemooneh(
    val onvan: String,
    val code: String
)

// هر درس: شماره، عنوان، توضیح، نمونه کد اصلی، نمونه‌های اضافه و سؤال‌های آزمونش
data class Dars(
    val shomareh: Int,
    val onvan: String,
    val tozihat: List<String>,
    val codeNemooneh: String,
    val soalHa: List<Soal>,
    val nemooonehBish: List<Nemooneh> = emptyList()
)

// فصل: چند درس پشت هم که یک موضوع را کامل می‌کنند
data class Fasl(
    val esm: String,
    val tozih: String,
    val az: Int,
    val ta: Int
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

    // شمارهٔ تازهٔ درس‌های قدیمی: دو درس مقدمه اول آمدند و درس‌های پروژه آخر رفتند
    private val shomarehTaze = mapOf(
        1 to 3, 2 to 4, 3 to 5, 4 to 6, 5 to 7,
        6 to 8, 7 to 9, 8 to 10, 9 to 11, 10 to 12,
        11 to 13, 12 to 14, 13 to 15,
        14 to 24, 15 to 25
    )

    val darsList: List<Dars> = (
            darsHaMoghaddameh + darsHaAval + darsHaMiani + darsHaAkhar + darsHaPishrafteh
            ).map { d ->
                val taze = shomarehTaze[d.shomareh] ?: d.shomareh
                // سؤال‌ها و نمونه‌های اضافه از DataEzafe وصل می‌شوند
                d.copy(
                    shomareh = taze,
                    soalHa = d.soalHa + SoalEzafeHa.soalHaEzafe[taze].orEmpty(),
                    nemooonehBish = d.nemooonehBish + SoalEzafeHa.nemooonehBish[taze].orEmpty()
                )
            }.sortedBy { it.shomareh }

    // پیدا کردن درس با شماره‌ش
    fun baShomareh(shomareh: Int): Dars = darsList.first { it.shomareh == shomareh }

    // ---------- فصل‌ها ----------
    val faslHa: List<Fasl> = listOf(
        Fasl("فصل صفر — مقدمه", "قبل از هر کدی، مسیر را بشناس", 1, 2),
        Fasl("فصل یک — پایه", "اولین برنامه، متغیر، عملگر، متن و ورودی", 3, 7),
        Fasl("فصل دو — تصمیم و تکرار", "شرط‌ها، انتخاب‌ها، حلقه‌ها و توابع", 8, 12),
        Fasl("فصل سه — لیست، کلاس و نال", "جعبه‌های بزرگ‌تر و برنامه‌نویسی شیءگرا", 13, 15),
        Fasl("فصل چهار — پیشرفته", "سطح حرفه‌ای: الگوریتم، کلاس حرفه‌ای و پروژه", 16, 23),
        Fasl("فصل پنج — پروژه و قدم بعدی", "جمع‌بندی با دو پروژهٔ واقعی", 24, 25)
    )

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
            "ساعتِ روز",
            "متغیر saat = 9 وجود دارد. اگر بین ۶ تا ۱۱ بود «صبح بخیر»، بین ۱۲ تا ۱۸ «ظهر بخیر»، در غیر این صورت «شب بخیر» چاپ کن. (اینجا ۹ است)",
            """
            fun main() {
                val saat = 9
                // با if و in یا با when تصمیم بگیر
            }
            """.trimIndent(),
            "صبح بخیر"
        ),
        Tamrin(
            "شمارش معکوس",
            "اعداد ۵ تا ۱ را با downTo و در یک خط با فاصله چاپ کن: 5 4 3 2 1",
            """
            fun main() {
                // حلقهٔ for با downTo بزن؛ بین اعداد فاصله بگذار
            }
            """.trimIndent(),
            "5 4 3 2 1"
        ),
        Tamrin(
            "بزرگ‌ترین عدد",
            "لیست اعداد داده شده. بزرگ‌ترینشان را با حلقه پیدا کن و چاپ کن.",
            """
            fun main() {
                val adadHa = listOf(7, 19, 3, 12, 8)
                var bozorgtarin = adadHa[0]
                // با حلقه مقایسه کن
            }
            """.trimIndent(),
            "19"
        ),
        Tamrin(
            "فیبوناچی",
            "شش جملهٔ اول فیبوناچی را در یک خط با فاصله چاپ کن: 1 1 2 3 5 8 (هر جمله = مجموع دو جملهٔ قبلی)",
            """
            fun main() {
                var a = 1
                var b = 1
                // با حلقه چاپ کن و دو متغیر را جابه‌جا کن
            }
            """.trimIndent(),
            "1 1 2 3 5 8"
        ),
        Tamrin(
            "دورریزها را نگه دار",
            "از لیست اعداد، فقط اعدادی که بزرگ‌تر از ۱۰ هستند را در لیست تازه بگذار و بعد با println چاپش کن.",
            """
            fun main() {
                val asli = listOf(4, 15, 8, 22, 9, 31)
                val bozorgHa = mutableListOf<Int>()
                // با حلقه و if، هر عدد بزرگ را با add اضافه کن
            }
            """.trimIndent(),
            "[15, 22, 31]"
        ),
        Tamrin(
            "کلاس کتاب",
            "کلاسی به اسم Ketab با دو ویژگی nam (متن) و safhe (عدد) بساز و متدی به اسم tozih که این جمله را برگرداند: «کتاب کاتلین با 320 صفحه». بعد از متد استفاده کن و چاپش کن.",
            """
            class Ketab(val nam: String, val safhe: Int) {
                fun tozih(): String {
                    // جمله را با $ بساز و برگردان
                }
            }

            fun main() {
                val k = Ketab("کاتلین", 320)
                // tozih را چاپ کن
            }
            """.trimIndent(),
            "کتاب کاتلین با 320 صفحه"
        ),
        Tamrin(
            "تابع بازگشتی",
            "تابعی بازگشتی بنویس که فاکتوریل حساب کند (n! = n × (n-1)! و 1! = 1) و بعد fakt(5) را چاپ کن.",
            """
            fun fakt(n: Int): Int {
                // اگر n برابر ۱ بود ۱ برگردان؛ وگرنه n ضربدر fakt(n - 1)
            }

            fun main() {
                // fakt(5) را چاپ کن
            }
            """.trimIndent(),
            "120"
        ),
        Tamrin(
            "مرتب‌سازی حبابی",
            "لیست داده شده را با الگوریتم حبابی مرتب کن: دو حلقهٔ تودرتو، هر بار دو همسایه را مقایسه و اگر لازم شد جابه‌جا کن. بعد لیست مرتب‌شده را چاپ کن.",
            """
            fun main() {
                val adadHa = mutableListOf(5, 2, 9, 1, 7)
                // دو حلقهٔ تودرتو + مقایسه + جابه‌جایی با متغیر موقت
            }
            """.trimIndent(),
            "[1, 2, 5, 7, 9]"
        ),
        Tamrin(
            "کلاس حساب بانکی",
            "کلاسی به اسم Hesab با ویژگی mojodi (عدد) بساز. متدی به اسم variz که مبلغ بگیرد و به موجودی اضافه کند و متدی به اسم neshan که موجودی را چاپ کند. ده هزار واریز کن و بعد موجودی را نشان بده.",
            """
            class Hesab(var mojodi: Int) {
                fun variz(mablagh: Int) {
                    // موجودی را زیاد کن
                }
                fun neshan() {
                    // موجودی را چاپ کن
                }
            }

            fun main() {
                val h = Hesab(50000)
                h.variz(10000)
                h.neshan()
            }
            """.trimIndent(),
            "60000"
        ),
        Tamrin(
            "مثلث ستاره‌ای",
            "با دو حلقهٔ تودرتو مثلث ستاره بساز: خط اول یک ستاره، خط دوم دو ستاره، تا خط چهارم. خروجی باید این شود:\n*\n**\n***\n****",
            """
            fun main() {
                // حلقهٔ بیرونی برای خط‌ها، حلقهٔ درونی برای ستاره‌های هر خط
            }
            """.trimIndent(),
            "*\n**\n***\n****"
        ),
        Tamrin(
            "کلاس دانش‌آموز",
            "کلاسی به اسم DaneshAmooz با ویژگی‌های nam (متن) و nomreHa (لیست عدد) بساز. متدی به اسم moadel که میانگین را چاپ کند (مجموع نمره‌ها تقسیم بر تعدادشان) و متدی به اسم shoro. سه نمرهٔ ۲۰ و ۱۶ و ۱۸ بگذار و معدل را چاپ کن — خروجی باید این شود: «معدل علی: 18»",
            """
            class DaneshAmooz(val nam: String, val nomreHa: MutableList<Int>) {
                fun moadel() {
                    // مجموع با sum و تعداد با size؛ حواس‌ت به تقسیم باشد
                }
            }

            fun main() {
                val d = DaneshAmooz("علی", mutableListOf(20, 16, 18))
                d.moadel()
            }
            """.trimIndent(),
            "معدل علی: 18"
        )
    )
}
