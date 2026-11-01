package ir.anjoman.amoozeshkotlin

/*
 * سؤال‌ها و نمونه‌کدهای اضافه برای درس‌های قدیمی (نسخهٔ ۳)
 * کلیدها شمارهٔ تازهٔ درس‌ها هستند (مقدمه دو درس اول آمد)
 * این‌ها موقع ساخت darsList به هر درس وصل می‌شوند
 */

object SoalEzafeHa {

    // ---------- سؤال‌های اضافه ----------
    val soalHaEzafe: Map<Int, List<Soal>> = mapOf(
        3 to listOf(
            Soal(
                "چرا برنامه‌نویسی «مثل دستور پخت» است؟",
                listOf(
                    "چون هر دو خوشمزه‌اند",
                    "چون هر دو دنبالهٔ دستورهای مرتب با نتیجهٔ مشخص‌اند",
                    "چون هر دو در آشپزخانه انجام می‌شوند",
                    "هیچ شباهتی ندارند"
                ),
                1,
                "دقیقاً! ترتیب و دقت در هر دو حیاتی است.",
                "همهٔ برنامه‌ها مثل دستور پخت، دنبالهٔ دستورهای مرتب‌اند؛ به هم ریختن ترتیب یعنی خراب شدن نتیجه."
            ),
            Soal(
                "برای دیدن نتیجهٔ یک کد کوچک در این اپ، کجا باید بروی؟",
                listOf(
                    "زمین بازی کد",
                    "صفحهٔ درباره",
                    "منوی تم",
                    "صفحهٔ آزمون"
                ),
                0,
                "آفرین! زمین بازی همان‌جاست که کد اجرا و خروجی می‌بینی.",
                "زمین بازی کد جایی است که کد می‌نویسی، اجرا می‌کنی و خروجی می‌بینی."
            )
        ),
        4 to listOf(
            Soal(
                "خروجی این کد چیست؟\nvar emtiyaz = 10\nemtiyaz = emtiyaz + 5\nprintln(emtiyaz.toString())",
                listOf(
                    "۱۰",
                    "۱۵",
                    "خطا؛ val قابل تغییر نیست",
                    "۵"
                ),
                1,
                "آفرین! var قابل تغییر است؛ ۱۰+۵=۱۵.",
                "var یعنی قابل تغییر؛ مقدار جدید ۱۵ می‌شود."
            ),
            Soal(
                "اگر بنویسی val x = 5 و بعد x = 8 چه می‌شود؟",
                listOf(
                    "x برابر ۸ می‌شود",
                    "خطا می‌دهد؛ val قفل است",
                    "x دو مقدار می‌گیرد",
                    "هیچ‌چیز"
                ),
                1,
                "درسته! val بعد از مقدار اولیه قفل می‌شود.",
                "val فقط یک بار مقدار می‌گیرد؛ تغییرش خطا است. برای تغییر از var استفاده کن."
            )
        ),
        5 to listOf(
            Soal(
                "نتیجهٔ عبارت 7 % 3 چند است؟",
                listOf(
                    "۲",
                    "۱",
                    "۰",
                    "۲٫۳"
                ),
                1,
                "آفرین! باقیماندهٔ ۷ بر ۳ برابر ۱ است.",
                "% باقیماندهٔ تقسیم را می‌دهد؛ ۷ = ۲×۳ + ۱ پس جواب ۱ است."
            ),
            Soal(
                "برای چک کردن زوج بودن یک عدد، کدام شرط درست است؟",
                listOf(
                    "n % 2 == 0",
                    "n / 2 == 0",
                    "n == 2",
                    "n + 2 == 0"
                ),
                0,
                "دقیقاً! باقیماندهٔ تقسیم بر ۲ صفر یعنی زوج.",
                "n % 2 == 0 یعنی n بر ۲ بخش‌پذیر است؛ یعنی زوج."
            )
        ),
        6 to listOf(
            Soal(
                "طول رشتهٔ «kotlin» با کدام مشخص می‌شود؟",
                listOf(
                    "kotlin.size",
                    "kotlin.length",
                    "length(kotlin)",
                    "kotlin.count"
                ),
                1,
                "آفرین! رشته length دارد؛ لیست size دارد.",
                "برای متن از .length استفاده می‌شود؛ .size مال لیست‌هاست."
            ),
            Soal(
                "خروجی \"سلام\".repeat(2) چیست؟",
                listOf(
                    "سلام سلام",
                    "سلام۲",
                    "خطا می‌دهد",
                    "سلامسلام (چسبیده)"
                ),
                3,
                "دقیقاً! repeat تکرار را چسبیده کنار هم می‌گذارد.",
                "repeat بدون فاصله تکرار می‌کند: سلامسلام. فاصله را باید خودت اضافه کنی."
            )
        ),
        7 to listOf(
            Soal(
                "readln چه چیزی برمی‌گرداند؟",
                listOf(
                    "عدد",
                    "متن (String)",
                    "لیست",
                    "Boolean"
                ),
                1,
                "آفرین! همه‌چیزِ ورودی اول متن است؛ با toInt عددش کن.",
                "readln همیشه متن می‌دهد؛ برای عدد باید .toInt() بزنی."
            ),
            Soal(
                "برای خواندن یک عدد کامل از کاربر، کدام خط درست است؟",
                listOf(
                    "val n = readln()",
                    "val n = readln().toInt()",
                    "val n = toInt(readln())",
                    "val n = readln.toInt()"
                ),
                1,
                "دقیقاً! اول متن خوانده می‌شود، بعد به Int تبدیل می‌شود.",
                "readln().toInt() یعنی بخوان و به عدد کامل تبدیل کن."
            )
        ),
        8 to listOf(
            Soal(
                "خروجی این کد چیست؟\nval sen = 20\nif (sen >= 18) {\n    println(\"بزرگسال\")\n} else {\n    println(\"نوجوان\")\n}",
                listOf(
                    "نوجوان",
                    "بزرگسال",
                    "هیچ‌کدام",
                    "خطا"
                ),
                1,
                "آفرین! ۲۰ از ۱۸ بزرگ‌تر یا مساوی است.",
                "شرط sen >= 18 درست است؛ پس شاخهٔ if اجرا می‌شود: بزرگسال."
            ),
            Soal(
                "else if چه زمانی به کار می‌آید؟",
                listOf(
                    "وقتی می‌خواهی چند شرط پشت سر هم چک شود و اولین درست اجرا شود",
                    "وقتی هیچ شرطی درست نیست",
                    "فقط برای اعداد",
                    "برای حلقه‌ها"
                ),
                0,
                "دقیقاً! زنجیرهٔ if / else if / else از بالا به پایین چک می‌کند.",
                "else if یعنی «وگرنه اگر»؛ زنجیرهٔ شرط‌ها اولین درست را اجرا می‌کند و بقیه را رد می‌کند."
            )
        ),
        9 to listOf(
            Soal(
                "در when چه چیزی اجرا می‌شود اگر دو شرط درست باشند؟",
                listOf(
                    "هر دو شاخه",
                    "فقط شاخهٔ اولی که درست بود",
                    "شاخهٔ آخر",
                    "هیچ‌کدام"
                ),
                1,
                "آفرین! when مثل آسانسور می‌ماند: اولین طبقهٔ درست، همان است.",
                "when اولین شرط درست را اجرا می‌کند و بقیه را نادیده می‌گیرد."
            ),
            Soal(
                "else در آخر when چه نقشی دارد؟",
                listOf(
                    "تکرار شرط اول",
                    "هر حالتی که با شرط‌ها نخورد",
                    "فقط عدد صفر",
                    "پایان برنامه"
                ),
                1,
                "درسته! else تور ایمنی when است.",
                "else وقتی اجرا می‌شود که هیچ شرط دیگری درست نباشد."
            )
        ),
        10 to listOf(
            Soal(
                "خروجی این کد چیست؟\nfor (i in 1..3) {\n    print(i.toString())\n}",
                listOf(
                    "1 2 3 (با فاصله)",
                    "123 (چسبیده)",
                    "۱۲۳",
                    "321"
                ),
                1,
                "دقیقاً! print به خط بعد نمی‌رود؛ پس چسبیده چاپ می‌شود.",
                "print بدون ln می‌ماند همان خط؛ پس ۱۲۳ چسبیده چاپ می‌شود."
            ),
            Soal(
                "فرق 1..5 و 1 until 5 چیست؟",
                listOf(
                    "هیچ فرقی ندارند",
                    "اولی تا ۵ را هم می‌گیرد، دومی تا ۴",
                    "اولی از صفر شروع می‌شود",
                    "دومی معکوس می‌شمارد"
                ),
                1,
                "آفرین! until سرِ آخر را نمی‌شمارد.",
                ".. شامل آخر است؛ until آخر را نمی‌گیرد: 1 until 5 یعنی ۱ تا ۴."
            )
        ),
        11 to listOf(
            Soal(
                "حلقهٔ while چه زمانی تمام می‌شود؟",
                listOf(
                    "بعد از ۱۰ دور",
                    "وقتی شرطش نادرست شود",
                    "وقتی break ببینیم فقط",
                    "هیچ‌وقت"
                ),
                1,
                "دقیقاً! تا شرط درست است می‌چرخد؛ برای همین باید جایی شرط را خراب کنی.",
                "while تا وقتی شرط درست است ادامه دارد؛ شرط باید بالاخره نادرست شود."
            ),
            Soal(
                "برای خروج از وسط حلقه از کدام کلمه استفاده می‌شود؟",
                listOf(
                    "return",
                    "break",
                    "stop",
                    "end"
                ),
                1,
                "آفرین! break حلقه را همان‌جا می‌شکند.",
                "break حلقه را همان لحظه تمام می‌کند؛ continue فقط این دور را رد می‌کند."
            )
        ),
        12 to listOf(
            Soal(
                "تابعی که return دارد چه کمکی می‌کند؟",
                listOf(
                    "برنامه را تمام می‌کند",
                    "جواب محاسبه را به جای فراخوان می‌فرستد تا ادامه استفاده شود",
                    "حافظه را پاک می‌کند",
                    "خطا را پنهان می‌کند"
                ),
                1,
                "دقیقاً! خروجی تابع مثل نمرهٔ امتحان است: برمی‌گردد تا استفاده شود.",
                "return مقدار را به جای صدا زدن برمی‌گرداند: val m = moadel(18, 20)."
            ),
            Soal(
                "چرا تابع می‌نویسیم و کد را تکرار نمی‌کنیم؟",
                listOf(
                    "برای زیاد شدن حجم فایل",
                    "یک بار می‌نویسیم، هزار بار صدا می‌زنیم؛ اصلاحش هم فقط یک‌جاست",
                    "چون کاتلین تابع اجباری است",
                    "برای کند شدن برنامه"
                ),
                1,
                "آفرین! تابع یعنی قالب: یک بار بساز، بی‌نهایت استفاده کن.",
                "تابع از تکرار جلوگیری می‌کند؛ اصلاح هم فقط در یک جا انجام می‌شود."
            )
        ),
        13 to listOf(
            Soal(
                "خروجی این کد چیست؟\nval list = listOf(5, 8, 2)\nprintln(list[1].toString())",
                listOf(
                    "۵",
                    "۸",
                    "۲",
                    "خطا"
                ),
                1,
                "آفرین! اندیس ۱ یعنی عضو دوم.",
                "اندیس‌ها از صفر شروع می‌شوند؛ list[1] عضو دوم یعنی ۸."
            ),
            Soal(
                "list.size چه چیزی را می‌دهد؟",
                listOf(
                    "مجموع اعضا",
                    "تعداد اعضا",
                    "عضو آخر",
                    "بزرگ‌ترین عضو"
                ),
                1,
                "درسته! size یعنی چند عضو داخل لیست است.",
                "size تعداد اعضا را می‌دهد؛ sum مجموعشان را."
            )
        ),
        14 to listOf(
            Soal(
                "شیء (Object) از کجا ساخته می‌شود؟",
                listOf(
                    "از فایل",
                    "با صدا زدن اسم کلاس و پرانتز: Ketab(\"نام\", 100)",
                    "از println",
                    "از val کپی می‌شود"
                ),
                1,
                "آفرین! کلاس قالب است و صدا زدنش شیء می‌سازد.",
                "اسم کلاس با پرانتز یعنی ساختن یک شیء تازه از آن قالب."
            ),
            Soal(
                "فرق ویژگی (property) و متد (method) چیست؟",
                listOf(
                    "هیچ فرقی ندارند",
                    "ویژگی داده نگه می‌دارد؛ متد کاری انجام می‌دهد",
                    "ویژگی سریع‌تر است",
                    "متد فقط برای متن است"
                ),
                1,
                "دقیقاً! نام و نمره ویژگی‌اند؛ محاسبهٔ معدل متد.",
                "ویژگی مثل پرونده است، متد مثل کارهایی که کارمند بلد است."
            )
        ),
        15 to listOf(
            Soal(
                "null یعنی چه؟",
                listOf(
                    "صفر",
                    "هیچ؛ عدم وجود مقدار",
                    "متن خالی",
                    "عدد منفی"
                ),
                1,
                "آفرین! null یعنی «هیچی داخل جعبه نیست».",
                "null یعنی هیچ مقداری وجود ندارد؛ نه صفر است نه متن خالی."
            ),
            Soal(
                "علامت ?. (نقطهٔ سؤالی) چه کمکی می‌کند؟",
                listOf(
                    "برنامه را سریع‌تر می‌کند",
                    "اگر مقدار نال نبود متد را اجرا می‌کند؛ وگرنه کاری نمی‌کند",
                    "متن را حذف می‌کند",
                    "خطا را بزرگ می‌کند"
                ),
                1,
                "دقیقاً! ?. مثل محافظ جلوی نال است.",
                "?. فقط وقتی مقدار واقعی است متد را صدا می‌زند؛ با نال به آرامی رد می‌شود."
            )
        ),
        24 to listOf(
            Soal(
                "در پروژهٔ ماشین‌حساب، چرا when بهتر از if های تودرتو بود؟",
                listOf(
                    "چون حافظه می‌گیرد",
                    "چون چهار عمل (+ - * /) را تمیز و مرتب پوشش می‌دهد",
                    "چون سریع‌تر است",
                    "چون خطا ندارد"
                ),
                1,
                "آفرین! انتخاب‌های چندگانه = کار اصلی when.",
                "برای انتخاب بین چند گزینه (چهار عمل)، when خواناترین راه است."
            ),
            Soal(
                "در ماشین‌حساب، تقسیم بر صفر چه خطر دارد؟",
                listOf(
                    "هیچ خطر ندارد",
                    "خطای اجرا؛ باید قبلش چک کنی",
                    "جواب ۱۰۰ می‌دهد",
                    "برنامه تندتر می‌شود"
                ),
                1,
                "درسته! برنامهٔ خوب قبل از تقسیم، مخرج را چک می‌کند.",
                "تقسیم بر صفر خطا می‌دهد؛ قبلش if (b == 0) چک کن."
            )
        ),
        25 to listOf(
            Soal(
                "بعد از تمام شدن این دوره، بهترین راه ادامهٔ مسیر چیست؟",
                listOf(
                    "اپ را پاک کنی",
                    "پروژه‌های شخصی بسازی: بازی، ابزار مدرسه، ماشین‌حساب حرفه‌ای‌تر",
                    "فقط ویدیو تماشا کنی",
                    "منتظر معجزه بمانی"
                ),
                1,
                "آفرین! ساخت پروژهٔ واقعی، بزرگ‌ترین معلم است.",
                "ادامهٔ مسیر = پروژهٔ شخصی ساختن؛ هر پروژه یک درس تازه است."
            ),
            Soal(
                "برای نوشتن کاتلین روی کامپیوتر واقعی، چه ابزاری لازم است؟",
                listOf(
                    "فقط گوشی",
                    "Android Studio یا IntelliJ IDEA",
                    "فقط مرورگر",
                    "ماشین‌حساب"
                ),
                1,
                "دقیقاً! Android Studio همان محیط رسمی ساخت اپ اندروید است.",
                "Android Studio (برای اپ) و IntelliJ IDEA (برای برنامهٔ معمولی) محیط‌های رسمی کاتلین‌اند."
            )
        )
    ),

    // ---------- نمونه‌کدهای اضافه ----------
    val nemooonehBish: Map<Int, List<Nemooneh>> = mapOf(
        3 to listOf(
            Nemooneh(
                "برنامهٔ «کارنامهٔ کوچک»",
                """
                fun main() {
                    println("کارنامهٔ علی")
                    println("ریاضی: 20")
                    println("عربی: 17")
                    println("معدل: 18.5")
                }
                // یک برنامه = چند println مرتب
                """.trimIndent()
            ),
            Nemooneh(
                "کامنت‌ها: یادداشت‌های برنامه‌نویس",
                """
                fun main() {
                    // این خط توضیح است؛ اجرا نمی‌شود
                    println("سلام!") // این هم کامنت بعد از کد
                    /* کامنت بلند
                       چند خطی */
                    println("خداحافظ!")
                }
                """.trimIndent()
            )
        ),
        4 to listOf(
            Nemooneh(
                "جعبه‌های پروفایل",
                """
                fun main() {
                    val nam = "سارا"
                    val sen: Int = 15
                    val moadel = 19.5
                    val daneshAmooz = true

                    println("نام: " + nam)
                    println("سن: " + sen.toString())
                    println("معدل: " + moadel.toString())
                    println("دانش‌آموز است؟ " + daneshAmooz.toString())
                }
                """.trimIndent()
            ),
            Nemooneh(
                "تبدیل نوع: متن به عدد و برعکس",
                """
                fun main() {
                    val matn = "42"
                    val adad = matn.toInt()
                    println(adad + 8)              // 50 — الان عدد است
                    val matnTaze = adad.toString()
                    println(matnTaze.length)       // 2 — الان متن است
                }
                """.trimIndent()
            )
        ),
        5 to listOf(
            Nemooneh(
                "عملگرهای منطقی: && و ||",
                """
                fun main() {
                    val nomre = 17
                    val hozor = true
                    println(nomre >= 10 && hozor)  // هر دو درست → true
                    println(nomre < 10 || hozor)   // یکی درست → true
                    println(!(nomre >= 10))        // برعکسِ درست → false
                }
                """.trimIndent()
            ),
            Nemooneh(
                "ترتیب اجرای عملگرها: پرانتز همه‌چیز را عوض می‌کند",
                """
                fun main() {
                    println((2 + 3) * 4)   // 20 — اول پرانتز
                    println(2 + 3 * 4)     // 14 — اول ضرب
                }
                """.trimIndent()
            )
        ),
        6 to listOf(
            Nemooneh(
                "قالب متن با \$: جای چسباندن تمیزتر",
                """
                fun main() {
                    val nam = "رضا"
                    val sen = 15
                    println("سلام، من \$nam هستم و \$sen سال دارم")
                    println("سال بعد: \${sen + 1} سال")
                }
                """.trimIndent()
            ),
            Nemooneh(
                "کار با حروف: بزرگ، کوچک، معکوس",
                """
                fun main() {
                    val harf = "kotlin"
                    println(harf.uppercase())   // KOTLIN
                    println(harf.reversed())    // niltok
                    println(harf.length)        // 6
                }
                """.trimIndent()
            )
        ),
        7 to listOf(
            Nemooneh(
                "دو ورودی پشت هم: جمع دو عدد کاربر",
                """
                fun main() {
                    println("عدد اول را بنویس:")
                    val a = readln().toInt()
                    println("عدد دوم را بنویس:")
                    val b = readln().toInt()
                    println("جمع: " + (a + b).toString())
                }
                // در کادر ورودی بنویس:
                // 12
                // 30
                """.trimIndent()
            ),
            Nemooneh(
                "بازی سلام با نام کاربر",
                """
                fun main() {
                    println("اسمت چیه؟")
                    val esm = readln()
                    println("سلام \$esm! خوش آمدی به دنیای برنامه‌نویسی 🎉")
                }
                """.trimIndent()
            )
        ),
        8 to listOf(
            Nemooneh(
                "شرط تودرتو: داخل شرط، شرط دیگر",
                """
                fun main() {
                    val nomre = 16
                    if (nomre >= 10) {
                        if (nomre >= 17) {
                            println("عالی!")
                        } else {
                            println("قبول؛ ولی بهتر می‌شود")
                        }
                    } else {
                        println("مردودی — دوباره تلاش کن")
                    }
                }
                """.trimIndent()
            ),
            Nemooneh(
                "چک کردن کلمهٔ عبور",
                """
                fun main() {
                    val ramz = "kotlin1405"
                    val voroodi = readln()
                    if (voroodi == ramz) {
                        println("خوش آمدی! 🔓")
                    } else {
                        println("رمز اشتباه است 🔒")
                    }
                }
                """.trimIndent()
            )
        ),
        9 to listOf(
            Nemooneh(
                "چند مقدار در یک شاخه: روزهای هفته",
                """
                fun main() {
                    val rooz = 5
                    when (rooz) {
                        1, 2, 3, 4 -> println("روز مدرسه")
                        5, 6 -> println("تعطیل!")
                        else -> println("شمار هفته از ۱ تا ۶ است!")
                    }
                }
                """.trimIndent()
            ),
            Nemooneh(
                "when برای متن‌ها",
                """
                fun main() {
                    val ghaza = "پیتزا"
                    when (ghaza) {
                        "پیتزا" -> println("ایتالیایی 🍕")
                        "چلوکباب" -> println("ایرانی 🍢")
                        "سوشی" -> println("ژاپنی 🍣")
                        else -> println("نمی‌شناسمش!")
                    }
                }
                """.trimIndent()
            )
        ),
        10 to listOf(
            Nemooneh(
                "حلقه با step: پرش دو تا دو تا",
                """
                fun main() {
                    for (i in 2..10 step 2) {
                        print(i.toString() + " ")
                    }
                    println()
                    for (j in 5 downTo 1) {
                        print(j.toString() + " ")
                    }
                }
                // 2 4 6 8 10
                // 5 4 3 2 1
                """.trimIndent()
            ),
            Nemooneh(
                "حلقه روی لیست اسامی",
                """
                fun main() {
                    val bacheHa = listOf("علی", "سارا", "رضا")
                    for (nam in bacheHa) {
                        println("سلام \$nam!")
                    }
                }
                """.trimIndent()
            )
        ),
        11 to listOf(
            Nemooneh(
                "شمارنده با while: تا کاربر صفر بزند",
                """
                fun main() {
                    var jam = 0
                    var adad = readln().toInt()
                    while (adad != 0) {
                        jam = jam + adad
                        adad = readln().toInt()
                    }
                    println("جمع همه: " + jam.toString())
                }
                // ورودی: 5 ، 8 ، 0  →  خروجی: 13
                """.trimIndent()
            ),
            Nemooneh(
                "دور زدن یکی از دورها با continue",
                """
                fun main() {
                    var i = 0
                    while (i < 5) {
                        i = i + 1
                        if (i == 3) {
                            continue   // سه را رد کن
                        }
                        print(i.toString() + " ")
                    }
                }
                // 1 2 4 5
                """.trimIndent()
            )
        ),
        12 to listOf(
            Nemooneh(
                "تابع با دو پارامتر: مساحت مستطیل",
                """
                fun masahat(tool: Int, arz: Int): Int {
                    return tool * arz
                }

                fun main() {
                    val m = masahat(5, 3)
                    println("مساحت: " + m.toString())
                    println("مساحت دیگر: " + masahat(7, 2).toString())
                }
                """.trimIndent()
            ),
            Nemooneh(
                "تابع بدون خروجی: فقط انجام دادن",
                """
                fun khatEksosi(onvan: String) {
                    println("======== " + onvan + " ========")
                }

                fun main() {
                    khatEksosi("شروع")
                    println("کارهای اصلی...")
                    khatEksosi("پایان")
                }
                """.trimIndent()
            )
        ),
        13 to listOf(
            Nemooneh(
                "add و removeAt روی لیست قابل تغییر",
                """
                fun main() {
                    val saf = mutableListOf("علی", "سارا")
                    saf.add("رضا")
                    println(saf.toString())      // [علی, سارا, رضا]
                    saf.removeAt(0)
                    println(saf.toString())      // [سارا, رضا]
                    println("تعداد: " + saf.size)
                }
                """.trimIndent()
            ),
            Nemooneh(
                "sum و reversed آماده",
                """
                fun main() {
                    val adad = listOf(10, 20, 30)
                    println(adad.sum())          // 60
                    println(adad.reversed().toString())  // [30, 20, 10]
                }
                """.trimIndent()
            )
        ),
        14 to listOf(
            Nemooneh(
                "کلاس با دو متد: ماشین کوچک",
                """
                class Mashin(var sorat: Int) {
                    fun gazBedeh() {
                        sorat = sorat + 10
                    }
                    fun termoz() {
                        sorat = sorat - 10
                        if (sorat < 0) {
                            sorat = 0
                        }
                    }
                    fun neshan() {
                        println("سرعت: " + sorat.toString())
                    }
                }

                fun main() {
                    val m = Mashin(0)
                    m.gazBedeh()
                    m.gazBedeh()
                    m.neshan()     // 20
                    m.termoz()
                    m.neshan()     // 10
                }
                """.trimIndent()
            ),
            Nemooneh(
                "چند شیء از یک کلاس؛ هر کدام زندگی خودش",
                """
                class BaziKon(var emtiyaz: Int)

                fun main() {
                    val a = BaziKon(10)
                    val b = BaziKon(50)
                    a.emtiyaz = a.emtiyaz + 5
                    println("a: " + a.emtiyaz.toString())  // 15
                    println("b: " + b.emtiyaz.toString())  // 50
                }
                """.trimIndent()
            )
        ),
        15 to listOf(
            Nemooneh(
                "?: مقدار جایگزین برای نال",
                """
                fun main() {
                    val esm: String? = null
                    println((esm ?: "بی‌نام").length)
                    // چون نال است، «بی‌نام» به کار رفت؛ طولش ۵
                }
                """.trimIndent()
            ),
            Nemooneh(
                "چک کردن با if: راه کلاسیک",
                """
                fun main() {
                    val nomre: Int? = null
                    if (nomre != null) {
                        println("نمره: " + nomre.toString())
                    } else {
                        println("هنوز نمره‌ای ثبت نشده")
                    }
                }
                """.trimIndent()
            )
        ),
        24 to listOf(
            Nemooneh(
                "ماشین‌حساب با ورودی کاربر",
                """
                fun main() {
                    println("عدد اول:")
                    val a = readln().toInt()
                    println("عدد دوم:")
                    val b = readln().toInt()
                    println("عمل (+ - * /):")
                    val amal = readln()

                    when (amal) {
                        "+" -> println(a + b)
                        "-" -> println(a - b)
                        "*" -> println(a * b)
                        "/" -> {
                            if (b == 0) {
                                println("تقسیم بر صفر!")
                            } else {
                                println(a / b)
                            }
                        }
                        else -> println("عمل نامعتبر")
                    }
                }
                """.trimIndent()
            )
        ),
        25 to listOf(
            Nemooneh(
                "ایده‌های پروژه برای تو",
                """
                // ۱) ماشین‌حساب با توان و جذر
                // ۲) بازی سنگ-کاغذ-قیچی با readln
                // ۳) دفتر تلفن: add، جست‌وجو، حذف
                // ۴) آزمون‌ساز: سؤال بپرس، نمره بده
                // هر کدام را در زمین بازی بساز و به دوست‌هایت نشان بده!
                """.trimIndent()
            )
        )
    )
}
