package ir.anjoman.amoozeshkotlin

import android.content.Context

/*
 * سیستم چندزبانهٔ EmaRoid
 * سه زبان: فارسی، English (پرچم ایرلند)، العربية (پرچم فلسطین)
 * زبان انتخابی در SharedPreferences ذخیره می‌شود و همهٔ نوشته‌های
 * رابط کاربری از جدول همین‌جا می‌آیند.
 * محتوای درس‌ها و سؤال‌ها هم سه‌زبانه است — جدول ترجمه در Tarjomeh.
 */
object Zaban {

    enum class Z { FA, EN, AR }

    private var feli: Z? = null

    fun bekhon(c: Context): Z {
        if (feli == null) {
            val p = c.getSharedPreferences("zakhire", Context.MODE_PRIVATE)
            val n = p.getString("zaban", "FA") ?: "FA"
            feli = when (n) {
                "EN" -> Z.EN
                "AR" -> Z.AR
                else -> Z.FA
            }
        }
        return feli!!
    }

    fun zakhireKon(c: Context, z: Z) {
        feli = z
        c.getSharedPreferences("zakhire", Context.MODE_PRIVATE)
            .edit().putString("zaban", z.name).apply()
    }

    // فارسی و عربی راست‌چین‌اند، انگلیسی چپ‌چین
    fun rastChin(c: Context): Boolean = bekhon(c) != Z.EN

    fun namZaban(z: Z): String = when (z) {
        Z.FA -> "فارسی"
        Z.EN -> "English"
        Z.AR -> "العربية"
    }

    // اعداد: فارسی با رقم فارسی، عربی با رقم عربی، انگلیسی لاتین
    fun adad(n: Int): String {
        val raghamHa = when (feli) {
            Z.AR -> listOf("٠", "١", "٢", "٣", "٤", "٥", "٦", "٧", "٨", "٩")
            Z.EN -> listOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")
            else -> listOf("۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹")
        }
        return n.toString().map { if (it.isDigit()) raghamHa[it - '0'] else it }.joinToString("")
    }

    /*
     * جدول نوشته‌های رابط کاربری به سه زبان
     * کلیدها با تابع t خوانده می‌شوند
     */
    fun t(kelid: String): String {
        val jadval: Map<String, Map<Z, String>> = jadvalHa
        val z = feli ?: Z.FA
        return jadval[kelid]?.get(z) ?: jadval[kelid]?.get(Z.FA) ?: kelid
    }

    private val jadvalHa: Map<String, Map<Z, String>> = mapOf(
        "onvan_app" to mapOf(
            Z.FA to "آموزش کاتلین",
            Z.EN to "Learn Kotlin",
            Z.AR to "تعلّم كوتلين"
        ),
        "zir_onvan" to mapOf(
            Z.FA to "قدم‌به‌قدم کاتلین یاد بگیر",
            Z.EN to "Learn Kotlin step by step",
            Z.AR to "تعلّم كوتلين خطوة بخطوة"
        ),
        "pishraft_kol" to mapOf(
            Z.FA to "پیشرفت کل",
            Z.EN to "Overall progress",
            Z.AR to "التقدم الكلي"
        ),
        "az" to mapOf(Z.FA to "از", Z.EN to "of", Z.AR to "من"),
        "dars_ha" to mapOf(Z.FA to "درس‌ها", Z.EN to "Lessons", Z.AR to "الدروس"),
        "darbare" to mapOf(Z.FA to "درباره", Z.EN to "About", Z.AR to "حول"),
        "bargasht" to mapOf(Z.FA to "بازگشت", Z.EN to "Back", Z.AR to "رجوع"),
        "bargasht_dars" to mapOf(
            Z.FA to "بازگشت به درس‌ها",
            Z.EN to "Back to lessons",
            Z.AR to "الرجوع إلى الدروس"
        ),
        "shoroo_azmoon" to mapOf(
            Z.FA to "شروع آزمون",
            Z.EN to "Start quiz",
            Z.AR to "ابدأ الاختبار"
        ),
        "behtarin_natije" to mapOf(
            Z.FA to "بهترین نتیجهٔ آزمون",
            Z.EN to "Best quiz result",
            Z.AR to "أفضل نتيجة اختبار"
        ),
        "sahih" to mapOf(Z.FA to "درست", Z.EN to "correct", Z.AR to "صحيح"),
        "hanuz_azmoon" to mapOf(
            Z.FA to "هنوز آزمون ندادی",
            Z.EN to "Quiz not taken yet",
            Z.AR to "لم تقم الاختبار بعد"
        ),
        "dars" to mapOf(Z.FA to "درس", Z.EN to "Lesson", Z.AR to "الدرس"),
        "azmoon" to mapOf(Z.FA to "آزمون", Z.EN to "Quiz", Z.AR to "اختبار"),
        "soal" to mapOf(Z.FA to "سؤال", Z.EN to "Question", Z.AR to "سؤال"),
        "badi" to mapOf(Z.FA to "بعدی", Z.EN to "Next", Z.AR to "التالي"),
        "ghabli" to mapOf(Z.FA to "قبلی", Z.EN to "Previous", Z.AR to "السابق"),
        "safhe" to mapOf(Z.FA to "صفحه", Z.EN to "Page", Z.AR to "صفحة"),
        "entekhab_zaban" to mapOf(
            Z.FA to "انتخاب زبان",
            Z.EN to "Choose language",
            Z.AR to "اختر اللغة"
        ),
        "tamam" to mapOf(Z.FA to "تأیید", Z.EN to "OK", Z.AR to "موافق"),
        "natije" to mapOf(Z.FA to "نتیجه", Z.EN to "Result", Z.AR to "النتيجة"),
        "afarin" to mapOf(
            Z.FA to "آفرین! عالی بود",
            Z.EN to "Great job!",
            Z.AR to "أحسنت!"
        ),
        "dobare" to mapOf(
            Z.FA to "دوباره امتحان کن",
            Z.EN to "Try again",
            Z.AR to "حاول مرة أخرى"
        ),
        "dore_jadid" to mapOf(
            Z.FA to "آزمون دوباره",
            Z.EN to "Retake quiz",
            Z.AR to "إعادة الاختبار"
        ),
        "khodahafez" to mapOf(
            Z.FA to "دوباره تلاش کن، می‌تونی!",
            Z.EN to "Try again, you can do it!",
            Z.AR to "حاول مجددًا، تستطيع!"
        ),

        // ---------- منو ----------
        "menu" to mapOf(Z.FA to "منو", Z.EN to "Menu", Z.AR to "القائمة"),
        "menu_zir" to mapOf(
            Z.FA to "همهٔ بخش‌های اپ اینجاست",
            Z.EN to "All parts of the app live here",
            Z.AR to "جميع أقسام التطبيق هنا"
        ),
        "vaziat" to mapOf(Z.FA to "وضعیت تو", Z.EN to "Your status", Z.AR to "حالتك"),
        "soal_sah" to mapOf(Z.FA to "سؤال درست", Z.EN to "correct answers", Z.AR to "إجابات صحيحة"),
        "menu_dars_tozih" to mapOf(
            Z.FA to "از مقدمه تا پیشرفته — قدم‌به‌قدم",
            Z.EN to "From intro to advanced — step by step",
            Z.AR to "من المقدمة إلى المتقدم — خطوة بخطوة"
        ),
        "zamin_bazi" to mapOf(
            Z.FA to "زمین بازی کد",
            Z.EN to "Code playground",
            Z.AR to "ساحة تجربة الكود"
        ),
        "zamin_bazi_tozih" to mapOf(
            Z.FA to "هر کدی بنویسی همان‌جا اجرا می‌شود؛ با خطایابی فارسی",
            Z.EN to "Write any code and run it right there; friendly error messages",
            Z.AR to "اكتب أي كود و نفّذه هناك؛ مع رسائل خطأ سهلة"
        ),
        "tamrin_titr" to mapOf(
            Z.FA to "تمرین‌های عملی",
            Z.EN to "Hands-on exercises",
            Z.AR to "التمارين العملية"
        ),
        "azmoon_jame" to mapOf(
            Z.FA to "آزمون جامع",
            Z.EN to "Comprehensive quiz",
            Z.AR to "الاختبار الشامل"
        ),
        "azmoon_jame_tozih" to mapOf(
            Z.FA to "۱۵ سؤال تصادفی از همهٔ درس‌ها",
            Z.EN to "15 random questions from all lessons",
            Z.AR to "١٥ سؤالاً عشوائياً من كل الدروس"
        ),
        "tem_titr" to mapOf(
            Z.FA to "تم و ظاهر",
            Z.EN to "Theme and look",
            Z.AR to "المظهر والألوان"
        ),
        "tem_tozih" to mapOf(
            Z.FA to "۶ تم رنگی؛ کل اپ با یک لمس عوض می‌شود",
            Z.EN to "6 color themes; the whole app changes with one tap",
            Z.AR to "٦ مواضيع ملونة؛ التطبيق كله يتغير بلمسة واحدة"
        ),
        "dastan" to mapOf(Z.FA to "ماجرا", Z.EN to "Adventure", Z.AR to "المغامرة"),
        "dastan_tozih" to mapOf(
            Z.FA to "داستان کدنورد — با هر درس جلوتر برو",
            Z.EN to "The Code Voyager story — advance with every lesson",
            Z.AR to "قصة مستكشف الكود — تقدّم مع كل درس"
        ),

        // ---------- نتیجهٔ آزمون ----------
        "taarif_3" to mapOf(
            Z.FA to "کامل بود! آفرین",
            Z.EN to "Perfect! Well done",
            Z.AR to "ممتاز! أحسنت"
        ),
        "taarif_2" to mapOf(
            Z.FA to "خوب بود! یه بار دیگه بزن تا کامل شه",
            Z.EN to "Good! Try once more to make it perfect",
            Z.AR to "جيد! حاول مرة أخرى ليكتمل"
        ),
        "taarif_1" to mapOf(
            Z.FA to "شروع خوبی بود، توضیحات درس رو یه بار دیگه بخون",
            Z.EN to "A good start; read the lesson again",
            Z.AR to "بداية جيدة؛ اقرأ الدرس مرة أخرى"
        ),
        "taarif_0" to mapOf(
            Z.FA to "اشکالی نداره! درس رو بخون و دوباره امتحان کن",
            Z.EN to "No worries! Read the lesson and try again",
            Z.AR to "لا بأس! اقرأ الدرس وحاول مجدداً"
        ),
        "azmoon_jame_natije" to mapOf(
            Z.FA to "آزمون جامع تمام شد",
            Z.EN to "Comprehensive quiz finished",
            Z.AR to "انتهى الاختبار الشامل"
        ),

        // ---------- درس ----------
        "nemune_kod" to mapOf(
            Z.FA to "نمونهٔ کد — با دقت بخون",
            Z.EN to "Code sample — read it carefully",
            Z.AR to "نموذج الكود — اقرأه بعناية"
        ),

        // ---------- تمرین و زمین بازی ----------
        "tamrin" to mapOf(Z.FA to "تمرین", Z.EN to "Exercise", Z.AR to "تمرين"),
        "tamrin_ha_titr" to mapOf(
            Z.FA to "تمرین‌های عملی",
            Z.EN to "Hands-on exercises",
            Z.AR to "التمارين العملية"
        ),
        "tamrin_zir_onvan" to mapOf(
            Z.FA to "کد را خودت بنویس تا خروجی درست بدهد",
            Z.EN to "Write the code yourself so it prints the right output",
            Z.AR to "اكتب الكود بنفسك ليكون الناتج صحيحاً"
        ),
        "hal_shod" to mapOf(Z.FA to "درست حل شد", Z.EN to "Solved correctly", Z.AR to "حُلّ بشكل صحيح"),
        "hal_nashode" to mapOf(Z.FA to "هنوز حل نشده", Z.EN to "Not solved yet", Z.AR to "لم يُحلّ بعد"),
        "nasihat_tamrin" to mapOf(
            Z.FA to "نکته: تمرین‌ها به درس‌های مربوطشان وصل‌اند؛ اگر گیر کردی اول درس را یک بار دیگر بخوان.",
            Z.EN to "Tip: exercises are tied to their lessons; if you get stuck, read the lesson again first.",
            Z.AR to "نصيحة: التمارين مرتبطة بدروسها؛ إذا علقت فاقرأ الدرس أولاً."
        ),
        "tamrin_badi" to mapOf(Z.FA to "تمرین بعدی", Z.EN to "Next exercise", Z.AR to "التمرين التالي"),
        "hame_tamrin" to mapOf(
            Z.FA to "همهٔ تمرین‌ها را حل کردی! قهرمان کاتلین 🏆",
            Z.EN to "You solved all the exercises! Kotlin champion 🏆",
            Z.AR to "حللت كل التمارين! بطل كوتلين 🏆"
        ),
        "khoruji_montazer" to mapOf(
            Z.FA to "خروجی منتظر:",
            Z.EN to "Expected output:",
            Z.AR to "الناتج المطلوب:"
        ),
        "kod_ejra_shod_ghalat" to mapOf(
            Z.FA to "کد اجرا شد ولی خروجی با جواب مورد نظر تمرین فرق دارد.",
            Z.EN to "The code ran, but the output doesn't match the expected answer.",
            Z.AR to "تنفيذ الكود تم لكن الناتج لا يطابق المطلوب."
        ),
        "zamin_bazi_zir" to mapOf(
            Z.FA to "کد کاتلین بنویس، اجرا کن و نتیجه را ببین",
            Z.EN to "Write Kotlin code, run it and see the result",
            Z.AR to "اكتب كود كوتلين ونفّذه وشاهد النتيجة"
        ),
        "hint_kod" to mapOf(
            Z.FA to "کد کاتلین را اینجا بنویس...",
            Z.EN to "Write your Kotlin code here...",
            Z.AR to "اكتب كود كوتلين هنا..."
        ),
        "voroodi" to mapOf(
            Z.FA to "ورودی (هر خط، جواب یک readln)",
            Z.EN to "Input (each line answers one readln)",
            Z.AR to "المدخلات (كل سطر يجيب readln واحداً)"
        ),
        "ejra" to mapOf(Z.FA to "▶  اجرای کد", Z.EN to "▶  Run code", Z.AR to "▶  نفّذ الكود"),
        "khoruji_barname" to mapOf(
            Z.FA to "خروجی برنامه",
            Z.EN to "Program output",
            Z.AR to "ناتج البرنامج"
        ),
        "bekon_ejra" to mapOf(
            Z.FA to "دکمهٔ اجرا را بزن تا نتیجه اینجا نمایش داده شود",
            Z.EN to "Press Run and the result will show up here",
            Z.AR to "اضغط نفّذ ليظهر الناتج هنا"
        ),
        "namune_lod" to mapOf(
            Z.FA to "کد آماده لود شد؛ اجرا کن!",
            Z.EN to "Sample code loaded; run it!",
            Z.AR to "تم تحميل كود جاهز؛ نفّذه!"
        ),
        "khoruji_nadarad" to mapOf(
            Z.FA to "(برنامه بدون خروجی تمام شد)",
            Z.EN to "(the program finished with no output)",
            Z.AR to "(انتهى البرنامج بدون ناتج)"
        ),
        "taborrik" to mapOf(
            Z.FA to "✅ آفرین! تمرین درست حل شد 🎉",
            Z.EN to "✅ Great! The exercise is solved 🎉",
            Z.AR to "✅ أحسنت! حُلّ التمرين 🎉"
        ),
        "namune_1" to mapOf(Z.FA to "سلام دنیا", Z.EN to "Hello world", Z.AR to "مرحبا بالعالم"),
        "namune_2" to mapOf(Z.FA to "متغیرها", Z.EN to "Variables", Z.AR to "المتغيرات"),
        "namune_3" to mapOf(Z.FA to "شرط", Z.EN to "Condition", Z.AR to "الشرط"),
        "namune_4" to mapOf(Z.FA to "حلقه", Z.EN to "Loop", Z.AR to "الحلقة"),
        "namune_5" to mapOf(Z.FA to "تابع", Z.EN to "Function", Z.AR to "الدالة"),
        "namune_6" to mapOf(Z.FA to "لیست", Z.EN to "List", Z.AR to "القائمة"),
        "namune_7" to mapOf(Z.FA to "کلاس", Z.EN to "Class", Z.AR to "الصنف"),

        // ---------- تم ----------
        "tem_zironvan" to mapOf(
            Z.FA to "یکی را انتخاب کن؛ کل اپ همان لحظه عوض می‌شود",
            Z.EN to "Pick one; the whole app changes instantly",
            Z.AR to "اختر واحداً؛ التطبيق كله يتغير فوراً"
        ),
        "tem_nasihat" to mapOf(
            Z.FA to "نکته: تم‌های تیره برای شب و چشم، تم روشن برای نور روز خوب است.",
            Z.EN to "Tip: dark themes suit the night, the light theme suits daylight.",
            Z.AR to "نصيحة: المظالم الداكنة لليل والفاتحة للنهار."
        ),
        "tem_tir" to mapOf(Z.FA to "تم تیره", Z.EN to "Dark theme", Z.AR to "مظهر داكن"),
        "tem_roshan" to mapOf(Z.FA to "تم روشن", Z.EN to "Light theme", Z.AR to "مظهر فاتح"),
        "faal" to mapOf(Z.FA to "✓ فعال", Z.EN to "✓ Active", Z.AR to "✓ نشط"),
        "entekhab" to mapOf(Z.FA to "انتخاب", Z.EN to "Choose", Z.AR to "اختيار"),
        "tem_0" to mapOf(Z.FA to "شب فیروزه‌ای", Z.EN to "Turquoise night", Z.AR to "ليلة فيروزية"),
        "tem_1" to mapOf(Z.FA to "جنگل سبز", Z.EN to "Green forest", Z.AR to "غابة خضراء"),
        "tem_2" to mapOf(Z.FA to "کهکشان بنفش", Z.EN to "Purple galaxy", Z.AR to "مجرة بنفسجية"),
        "tem_3" to mapOf(Z.FA to "غروب نارنجی", Z.EN to "Orange sunset", Z.AR to "غروب برتقالي"),
        "tem_4" to mapOf(Z.FA to "دریای آبی", Z.EN to "Blue sea", Z.AR to "بحر أزرق"),
        "tem_5" to mapOf(Z.FA to "روز روشن", Z.EN to "Bright day", Z.AR to "نهار مشرق"),

        // ---------- درباره ----------
        "darbare_matn" to mapOf(
            Z.FA to "این اپ برای یادگیری قدم‌به‌قدم کاتلین ساخته شده؛ ساختهٔ EmaRoid",
            Z.EN to "This app was built for step-by-step Kotlin learning; made by EmaRoid",
            Z.AR to "هذا التطبيق لتعلّم كوتلين خطوة بخطوة؛ صناعة EmaRoid"
        ),

        // ---------- تبلیغ خانهٔ EmaRoid ----------
        "tabligh" to mapOf(Z.FA to "تبلیغ", Z.EN to "Ad", Z.AR to "إعلان"),
        "didan" to mapOf(Z.FA to "دیدن", Z.EN to "See", Z.AR to "شاهد"),
        "bastan" to mapOf(Z.FA to "بستن", Z.EN to "Close", Z.AR to "إغلاق"),
        "tab_1_onvan" to mapOf(
            Z.FA to "آموزش Rust بازی‌وار",
            Z.EN to "Game-style Rust course",
            Z.AR to "دورة Rust بأسلوب اللعبة"
        ),
        "tab_1_matn" to mapOf(
            Z.FA to "همان سبک دوست‌داشتنی، این‌بار برای Rust",
            Z.EN to "The same friendly style, now for Rust",
            Z.AR to "الأسلوب الودّي نفسه، الآن لـ Rust"
        ),
        "tab_2_onvan" to mapOf(
            Z.FA to "ماشین‌حساب کهکشانی",
            Z.EN to "Galaxy calculator",
            Z.AR to "آلة حاسبة مجرّية"
        ),
        "tab_2_matn" to mapOf(
            Z.FA to "کلیدهای شیشه‌ای نئون روی آسمان بنفش",
            Z.EN to "Neon glass keys on a purple sky",
            Z.AR to "أزرار نيون زجاجية على سماء بنفسجية"
        ),
        "tab_3_onvan" to mapOf(
            Z.FA to "خانوادهٔ EmaRoid",
            Z.EN to "The EmaRoid family",
            Z.AR to "عائلة EmaRoid"
        ),
        "tab_3_matn" to mapOf(
            Z.FA to "اپ‌های آموزشی و بازی‌های بیشتر در راه است",
            Z.EN to "More learning apps and games are on the way",
            Z.AR to "المزيد من تطبيقات التعلم والألعاب قادمة"
        ),

        // ---------- ماجرای کدنورد ----------
        "dastan_onvan" to mapOf(
            Z.FA to "ماجرای کدنورد",
            Z.EN to "The Code Voyager",
            Z.AR to "مستكشف الكود"
        ),
        "dastan_episod" to mapOf(
            Z.FA to "فصل",
            Z.EN to "Chapter",
            Z.AR to "الفصل"
        ),
        "dastan_ghofl" to mapOf(
            Z.FA to "قفل است",
            Z.EN to "Locked",
            Z.AR to "مقفل"
        ),
        "dastan_bad_az" to mapOf(
            Z.FA to "بعد از آزمون درس",
            Z.EN to "after the quiz of lesson",
            Z.AR to "بعد اختبار الدرس"
        ),
        "dastan_edame" to mapOf(
            Z.FA to "ادامهٔ ماجرا",
            Z.EN to "Continue the story",
            Z.AR to "تابع المغامرة"
        ),

        // ---------- به‌روزرسانی ----------
        "neskh_jadid" to mapOf(
            Z.FA to "نسخهٔ جدید",
            Z.EN to "New version",
            Z.AR to "نسخة جديدة"
        ),
        "neskh_jadid_matn" to mapOf(
            Z.FA to "نسخهٔ تازهٔ اپ آماده است؛ می‌خواهی بگیری؟",
            Z.EN to "A new version of the app is ready; get it?",
            Z.AR to "نسخة جديدة من التطبيق جاهزة؛ هل تريدها؟"
        ),
        "danlod" to mapOf(Z.FA to "دانلود", Z.EN to "Download", Z.AR to "تحميل"),
        "baadan" to mapOf(Z.FA to "بعداً", Z.EN to "Later", Z.AR to "لاحقاً")
    )
}
