package ir.anjoman.amoozeshkotlin

import android.content.Context

/*
 * ماجرای کدنورد — لایهٔ داستانی اپ (بازیِ قصه)
 *
 * قصه: یک کاوشگر جوان در «دنیای کد» گیر می‌افتد؛ شاتل او خراب است و
 * هر درس کاتلین یک قطعهٔ شاتل را دوباره روشن می‌کند. با هر آزمونی که
 * پاس شود، یک فصل داستان باز می‌شود؛ فصل آخر: پرواز به خانه.
 * همهٔ متن‌ها سه‌زبانه‌اند.
 */
object Dastan {

    class Episod(
        val shomareh: Int,          // فصل چندم
        val darDars: Int,           // با پاس‌شدن آزمون این درس باز می‌شود
        val onvan: Map<Zaban.Z, String>,
        val matn: Map<Zaban.Z, String>
    )

    val episodHa: List<Episod> = listOf(
        Episod(
            1, 2,
            mapOf(
                Zaban.Z.FA to "فرود در دنیای کد",
                Zaban.Z.EN to "Landing in the Code World",
                Zaban.Z.AR to "الهبوط في عالم الكود"
            ),
            mapOf(
                Zaban.Z.FA to "شب که شد، کاوشگر کوچکِ ما با شاتلش در دنیای کد فرود آمد؛ دنیایی از نورها که شکل آکولاد داشتند. شاتل خاموش بود و صفحهٔ دستگاه فقط یک جمله نشان می‌داد: «برای روشن‌شدن، fun main() را پیدا کن». فهمیدی؟ کلید استارتِ این دنیا، همان اولین تابعی است که یاد گرفتی!",
                Zaban.Z.EN to "Night fell and our little explorer landed in the Code World, a place of glowing braces and lights. The shuttle was off and the dashboard showed one line: \"to power up, find fun main()\". Do you get it? The ignition key of this world is the very first function you learned!",
                Zaban.Z.AR to "أمسى الليل ونزل مستكشفنا الصغير في عالم الكود، عالم من الأقواس والأنوار المتوهجة. المكوك مطفأ واللوحة تعرض سطراً واحداً: «للتشغيل، ابحث عن fun main()». هل فهمت؟ مفتاح تشغيل هذا العالم هو أول دالة تعلمتها!"
            )
        ),
        Episod(
            2, 5,
            mapOf(
                Zaban.Z.FA to "جعبه‌های سوخت",
                Zaban.Z.EN to "The fuel boxes",
                Zaban.Z.AR to "صناديق الوقود"
            ),
            mapOf(
                Zaban.Z.FA to "دنياي کد بي سوخت نیست، فقط جعبه‌هایش بی‌برچسب‌اند! کدنورد فهمید هر جعبه باید val باشد یا var: سوختِ همیشگی را val بگذاری که قفل شود، و سوختِ قابل‌دستی را var تا در مسیر کم و زیاد کنی. قفل‌کردن جعبه‌های درست، شاتل را یک‌قدم به پرواز نزدیک کرد.",
                Zaban.Z.EN to "The Code World isn't out of fuel — its boxes just lost their labels! The voyager realized every box is val or var: permanent fuel goes in val so it stays locked, flexible fuel goes in var so you can adjust it. Locking the right boxes moved the shuttle one step closer to flight.",
                Zaban.Z.AR to "عالم الكود ليس خالياً من الوقود، صناديقه فقط فقدت ملصقاتها! أدرك المستكشف أن كل صندوق إما val أو var: الوقود الدائم في val ليُقفل، والوقود القابل للتعديل في var. قفل الصناديق الصحيحة قرّب المكوك خطوة من الطيران."
            )
        ),
        Episod(
            3, 8,
            mapOf(
                Zaban.Z.FA to "مغز ناوبری",
                Zaban.Z.EN to "The navigation brain",
                Zaban.Z.AR to "عقل الملاحة"
            ),
            mapOf(
                Zaban.Z.FA to "دستگاه ناوبری شاتل گم می‌شد؛ باید تصمیم می‌گرفت! کدنورد با if و when بهش یاد داد: «اگر سیارک دیدی بپیچ، اگر برچسب مسیر ۱ بود این‌جا، وگرنه آن‌جا». حالا مغز کشتی مثل یک نگهبان هوشمند، هر مسیر را چک می‌کند.",
                Zaban.Z.EN to "The shuttle's navigator kept getting lost; it had to make decisions! With if and when the voyager taught it: \"if you see an asteroid, swerve; if the route label is 1, go here, else there\". Now the ship's brain checks every route like a smart guard.",
                Zaban.Z.AR to "كان ملاح المكوك يتوه؛ عليه أن يقرّر! علّمه المستكشف بالـ if والـ when: «إن رأيت كويكباً فالتف، إن كان اسم المسار ١ فمن هنا، وإلا فمن هناك». الآن يعقل السفينة يفحص كل مسار كحارس ذكي."
            )
        ),
        Episod(
            4, 12,
            mapOf(
                Zaban.Z.FA to "بازوهای رباتیک",
                Zaban.Z.EN to "The robotic arms",
                Zaban.Z.AR to "الأذرع الآلية"
            ),
            mapOf(
                Zaban.Z.FA to "چهل تکه سنگ فضایی باید تمیز می‌شد؛ بازوی ربات فقط یک دستور می‌فهمید. کدنورد با حلقهٔ for گفت: «این کار را پنج بار تکرار کن» و با while گفت: «تا وقتی کثیف است ادامه بده». بازوها مثل یک ارکستر چرخیدند و شاتل برق افتاد.",
                Zaban.Z.EN to "Forty space rocks needed cleaning; the robot arm understood only one command. With a for loop the voyager said \"repeat this five times\", and with while \"keep going as long as it's dirty\". The arms spun like an orchestra and the shuttle sparkled.",
                Zaban.Z.AR to "أربعون صخرة فضائية تحتاج تنظيفاً، وذراع الروبوت لا تفهم أمراً واحداً إلا. قال المستكشف بحلقة for: «كرّر هذا خمس مرات»، وبـ while: «استمر ما دامت متسخة». لفّت الأذرع كأوركسترا وتلألأ المكوك."
            )
        ),
        Episod(
            5, 15,
            mapOf(
                Zaban.Z.FA to "خدمهٔ کوچک، کارهای بزرگ",
                Zaban.Z.EN to "A small crew with big jobs",
                Zaban.Z.AR to "طاقم صغير وأعمال كبيرة"
            ),
            mapOf(
                Zaban.Z.FA to "کاپیتان شاتل همهٔ کارها را خودش انجام می‌داد و خسته بود. کدنورد برای هر کار یک fun نوشت: یکی برای روشن‌کردن موتور، یکی برای بستن درها؛ حتی به بعضی‌ها ورودی پیش‌فرض داد. حالا هر وقت اسم خدمه صدا زده شود، همان کار با نظم انجام می‌شود.",
                Zaban.Z.EN to "The shuttle captain did every job alone and was exhausted. The voyager wrote a fun for each task: one to start the engine, one to close the doors, even default inputs for some. Now whenever a crew member is called, the job gets done in order.",
                Zaban.Z.AR to "كان قائد المكوك يفعل كل شيء وحده حتى تعب. كتب المستكشف لكل عمل دالة fun: واحدة لتشغيل المحرك وواحدة لإغلاق الأبواب، بل أعطى بعضها مدخلات افتراضية. الآن كلما نادى أحد الطاقم أُنجز العمل بنظام."
            )
        ),
        Episod(
            6, 18,
            mapOf(
                Zaban.Z.FA to "انبار بارگیری",
                Zaban.Z.EN to "The cargo bay",
                Zaban.Z.AR to "مخزن الحمولة"
            ),
            mapOf(
                Zaban.Z.FA to "انبار شاتل از بسته‌های پراکنده پر شده بود؛ پیدا کردن یک قطعه نیم‌روز طول می‌کشید! کدنورد همهٔ قطعه‌ها را در یک listOf چید و با حلقه رویشان رفت؛ حتی با add قطعهٔ تازه اضافه کرد. انبار حالا مثل یک کتابخانهٔ مرتب است.",
                Zaban.Z.EN to "The cargo bay was a mess of scattered parts; finding one took half a day! The voyager lined every part up in a listOf and walked through them with a loop, even adding new parts with add. The bay is now like a tidy library.",
                Zaban.Z.AR to "امتلأ مخزن المكوك بقطع متفرقة؛ إيجاد قطعة يستغرق نصف يوم! صفّ المستكشف كل القطع في listOf ومرّ عليها بحلقة، بل أضاف قطعاً جديدة بـ add. صار المخزن مثل مكتبة مرتبة."
            )
        ),
        Episod(
            7, 22,
            mapOf(
                Zaban.Z.FA to "نقشهٔ سازنده",
                Zaban.Z.EN to "The builder's blueprint",
                Zaban.Z.AR to "مخطّط الباني"
            ),
            mapOf(
                Zaban.Z.FA to "برای تعویض قطعهٔ معیوب، باید قطعهٔ تازه «هم‌شکلِ قبلی» ساخته می‌شد. کدنورد یک class نوشت: نقشه‌ای که هر قطعه‌ای با آن ساخته شود، همان خواص را دارد و همان کارها را بلد است. با یک نقشه، سه قطعهٔ تازه ساخت — بدون هیچ خطایی.",
                Zaban.Z.EN to "To replace a broken part, the new one had to be built exactly like it. The voyager wrote a class: a blueprint whose parts all share the same properties and skills. With one blueprint he built three new parts — without a single error.",
                Zaban.Z.AR to "لتعويض قطعة معطلة كان لا بد من بناء الجديدة مطابقةً للأصل. كتب المستكشف صفاً class: مخططاً تَخرج منه قطع تشترك في الخصائص وتعرف الأعمال نفسها. بمخطط واحد بنى ثلاث قطع جديدة بلا خطأ واحد."
            )
        ),
        Episod(
            8, 25,
            mapOf(
                Zaban.Z.FA to "پرواز به خانه",
                Zaban.Z.EN to "The flight home",
                Zaban.Z.AR to "الطيران إلى البيت"
            ),
            mapOf(
                Zaban.Z.FA to "همهٔ چراغ‌های شاتل سبز شد؛ موتور main روشن، سوخت در جعبه‌های val، ناوبری تصمیم می‌گیرد، بازوها تکرار می‌کنند، خدمه در fun ها مرتب‌اند و انبار مرتب است. کدنورد رو به آسمان کد لبخند زد: «برگشتی چون یاد گرفتی». شاتل با صدای یک println بلند... پرواز کرد. 🚀",
                Zaban.Z.EN to "Every shuttle light turned green: the main engine is on, fuel locked in val boxes, the navigator decides, the arms repeat, the crew lives in funs and the cargo is tidy. The voyager smiled at the code sky: \"you made it home because you learned\". The shuttle launched with one loud println... 🚀",
                Zaban.Z.AR to "أخضرّت كل أضواء المكوك: المحرك main يعمل، والوقود مقفل في صناديق val، والملاحة تقرّر، والأذرع تكرّر، والطاقم منظم في الدوال والمخزن مرتب. ابتسم المستكشف لسماء الكود: «عدمت لأنك تعلمت». أطلق المكوك بـ println واحدة عالية... 🚀"
            )
        )
    )

    // فصل آخرین، باز؟ (آزمون درس مربوط پاس شده باشد)
    fun bazAst(c: Context, ep: Episod): Boolean =
        Zakhire.behtarin(c, ep.darDars) > 0

    fun pasShode(c: Context): Int = episodHa.count { bazAst(c, it) }
}
