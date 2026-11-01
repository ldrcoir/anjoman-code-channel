package ir.anjoman.kalaryar

import android.content.Context

/*
 * دستیار هوشمند کالری‌یار — مغز آفلاین اپ
 * یک موتور متخصص داخل خود گوشی: نیت سؤال را تشخیص می‌دهد،
 * با پایگاه غذا و دفترچهٔ روز شما محاسبه می‌کند و جواب شخصی می‌دهد.
 * بدون اینترنت، بدون سرور؛ همه‌چیز داخل اپ.
 */

object HoshmandYar {

    // ---------- پاسخ به یک پیام کاربر ----------
    fun javab(context: Context, voroodi: String): List<String> {
        val pak = PayegahGhaza.pakKon(voroodi.trim())
        if (pak.isEmpty()) return listOf("چیزی بنویس تا کمکت کنم! مثلاً: «کالری پیتزا چقدره؟»")

        // نیت: سلام و احوال‌پرسی
        if (pak.contains("سلام") || pak == "هی" || pak.contains("درود")) {
            return listOf(
                "سلام! من دستیار هوشمند کالری‌یارم 🤖",
                "می‌توانم کالری و پروتئین هر غذا را بگویم، بگویم با کالری باقی‌ماندهٔ امروز چه می‌شود خورد، وضعیت امروزت را تحلیل کنم و برای شام پیشنهاد بدهم.",
                "سؤالت را بپرس یا از دکمه‌های بالا استفاده کن!"
            )
        }

        // نیت: تشکر
        if (pak.contains("مرسی") || pak.contains("ممنون") || pak.contains("تشکر") || pak.contains("دمت")) {
            return listOf("خواهش می‌کنم! 🌿 من همیشه اینجاییم. سؤال بعدی چیست؟")
        }

        // نیت: چه بخورم / پیشنهاد غذا
        if (pak.contains("چه بخورم") || pak.contains("چی بخورم") || pak.contains("چیبخورم") || pak.contains("پیشنهاد") ||
            pak.contains("گشن") || pak.contains("گرسن")
        ) {
            return pishnehadKon(context)
        }

        // نیت: می‌توانم X بخورم؟
        if (pak.contains("میتونم") || pak.contains("می تونم") || pak.contains("می توانم") || pak.contains("اجازه")) {
            val ghaza = ghazaAzMatn(pak)
            if (ghaza != null) return mitoonamBekhoram(context, ghaza)
        }

        // نیت: کالری غذا
        if (pak.contains("کالری") || pak.contains("انرژی")) {
            val ghaza = ghazaAzMatn(pak)
            if (ghaza != null) return etelatGhaza(ghaza, "کالری")
        }

        // نیت: پروتئین
        if (pak.contains("پروتئین") || pak.contains("پروتین")) {
            val ghaza = ghazaAzMatn(pak)
            if (ghaza != null) return etelatGhaza(ghaza, "پروتئین")
        }

        // نیت: وضعیت امروز
        if (pak.contains("وضعیت") || pak.contains("امروز چطور") || pak.contains("خلاصه") ||
            pak.contains("تا الان")
        ) {
            return vaziatEmrooz(context)
        }

        // نیت: آب (پس از نرمال‌سازی، «آب» به «اب» تبدیل شده)
        if (pak.contains("اب") || pak.contains("لیوان")) {
            return vaziatAb(context)
        }

        // نیت: وزن و BMI
        if (pak.contains("وزن") || pak.contains("bmi") || pak.contains("چاق") || pak.contains("لاغر")) {
            return vaziatVazn(context)
        }

        // نیت: هدف
        if (pak.contains("هدف") || pak.contains("باقیمانده") || pak.contains("چقدر مونده")) {
            return vaziatHadaf(context)
        }

        // نیت: ورزش
        if (pak.contains("ورزش") || pak.contains("کالری سوزاند") || pak.contains("پیاده")) {
            return vaziatVarTam(context)
        }

        // نیت: اسم غذا بدون کلمهٔ کلیدی — اطلاعات غذا
        val ghaza = ghazaAzMatn(pak)
        if (ghaza != null) return etelatGhaza(ghaza, "همه")

        // نیت: نامشخص — راهنما
        return listOf(
            "این را کامل نفهمیدم 🤔 ولی این کارها را بلدم:",
            "• «کالری پیتزا چقدره؟» — کالری و ریزمغذی هر غذا",
            "• «الان چی بخورم؟» — پیشنهاد بر اساس کالری باقی‌مانده",
            "• «می‌تونم برنج بخورم؟» — محاسبهٔ سهم مجاز",
            "• «وضعیت امروزم چطوره؟» — تحلیل کامل روز",
            "• «وزنم چطوره؟» — BMI و مسیر هدف"
        )
    }

    // ---------- پیدا کردن غذا در متن ----------
    private fun ghazaAzMatn(matn: String): Ghaza? {
        val pak = PayegahGhaza.pakKon(matn)
        // اول جست‌وجوی دقیق با اسم کامل
        val daghigh = PayegahGhaza.ghazaHa.firstOrNull { pak.contains(PayegahGhaza.pakKon(it.esm)) }
        if (daghigh != null) return daghigh
        // بعد جست‌وجوی بخشی از اسم (حداقل ۳ حرف)
        for (g in PayegahGhaza.ghazaHa) {
            val esmPak = PayegahGhaza.pakKon(g.esm)
            // تکه‌های اسم غذا: برنج، پیتزا، ماست...
            for (teke in esmPak.split(" ")) {
                if (teke.length >= 3 && pak.contains(teke)) return g
            }
        }
        return null
    }

    // ---------- اطلاعات یک غذا ----------
    private fun etelatGhaza(g: Ghaza, noo: String): List<String> {
        val kalarPorzion = (g.kalar * g.porzionGram / 100.0).toInt()
        val proteinPorzion = g.protein * g.porzionGram / 100.0
        val karbPorzion = g.karbohidrat * g.porzionGram / 100.0
        val charbiPorzion = g.charbi * g.porzionGram / 100.0

        val javab = ArrayList<String>()
        javab.add("${g.esm} (${g.daste}):")
        if (noo == "کالری" || noo == "همه") {
            javab.add("🔥 ${Abzar.adadFarsi(g.kalar)} کالری در هر ۱۰۰ گرم")
        }
        if (noo == "پروتئین") {
            javab.add("💪 ${Abzar.adadFarsi(g.protein)} گرم پروتئین در هر ۱۰۰ گرم")
        }
        if (noo == "همه") {
            javab.add(
                "درشت‌مغذی‌های ۱۰۰ گرم: پروتئین ${Abzar.adadFarsi(g.protein)} • کربوهیدرات ${Abzar.adadFarsi(g.karbohidrat)} • چربی ${Abzar.adadFarsi(g.charbi)}"
            )
        }
        javab.add(
            "سهم رایج (${g.porzionEsm} ≈ ${Abzar.adadFarsi(g.porzionGram)} گرم): ${Abzar.adadFarsi(kalarPorzion)} کالری و ${Abzar.adadFarsi(proteinPorzion)} گرم پروتئین"
        )
        if (noo == "همه") {
            val nomre = nomreSalamat(g)
            javab.add("نمرهٔ سلامت: $nomre ${tozihNomre(nomre)}")
            javab.add("برای ثبت در دفترچه، از بخش «افزودن غذا» اسمش را جست‌وجو کن.")
        }
        return javab
    }

    // نمرهٔ سلامت A تا E بر اساس چگالی پروتئین، چربی اشباع تقریبی و کالری
    fun nomreSalamat(g: Ghaza): String {
        val emtiyaz = 100 -
                (g.charbi / (g.protein + g.karbohidrat + 1.0)) * 28 -
                (if (g.kalar > 350) 20 else if (g.kalar > 250) 10 else 0) +
                (if (g.protein > 15) 12 else if (g.protein > 8) 6 else 0) +
                (if (g.daste == "میوه" || g.daste == "سبزیجات") 15 else 0)
        return when {
            emtiyaz >= 80 -> "A"
            emtiyaz >= 62 -> "B"
            emtiyaz >= 45 -> "C"
            emtiyaz >= 30 -> "D"
            else -> "E"
        }
    }

    fun tozihNomre(n: String): String = when (n) {
        "A" -> "انتخاب عالی!"
        "B" -> "خوب و متعادل"
        "C" -> "متوسط؛ در حد اعتدال"
        "D" -> "کمتر بخورش"
        else -> "بهتره جایگزین سبک‌تر پیدا کنی"
    }

    // ---------- «می‌توانم X بخورم؟» ----------
    private fun mitoonamBekhoram(context: Context, g: Ghaza): List<String> {
        val baghi = HesabHa.baghiMandeh(context, Abzar.kelidEmrooz())
        val kalarPorzion = (g.kalar * g.porzionGram / 100.0).toInt()
        return if (baghi >= kalarPorzion && baghi > 0) {
            val maxGram = (baghi * 100 / g.kalar)
            listOf(
                "بله! ✅ کالری باقی‌ماندهٔ امروز: ${Abzar.adadFarsi(baghi)}",
                "${g.esm} با سهم رایج (${g.porzionEsm} ≈ ${Abzar.adadFarsi(g.porzionGram)} گرم) ${Abzar.adadFarsi(kalarPorzion)} کالری دارد؛ جا دارد!",
                "حداکثر ${Abzar.adadFarsi(maxGram)} گرم از آن جا می‌شود. حواست به پروتئین هم باشد 😉"
            )
        } else {
            listOf(
                "با احتیاط! ⚠️ کالری باقی‌ماندهٔ امروز: ${Abzar.adadFarsi(baghi.coerceAtLeast(0))}",
                "${g.esm} فقط ${Abzar.adadFarsi(g.porzionGram)} گرمش ${Abzar.adadFarsi(kalarPorzion)} کالری دارد؛ از بودجهٔ امروزت بیشتر است.",
                "یا نصف سهم بخور، یا با کمی ورزش جایش را باز کن 🏃"
            )
        }
    }

    // ---------- پیشنهاد غذا بر اساس بودجهٔ امروز ----------
    private fun pishnehadKon(context: Context): List<String> {
        val profil = Khazide.profilBekhon(context)
            ?: return listOf("اول از صفحهٔ اصلی پروفایلت را کامل کن تا پیشنهادهایم شخصی شود 🙂")
        val baghi = HesabHa.baghiMandeh(context, Abzar.kelidEmrooz())
        val majmoo = Khazide.majmooRooz(context, Abzar.kelidEmrooz())
        val kambodProtein = (HesabHa.proteinHadaf(profil) - majmoo.protein).coerceAtLeast(0.0)

        if (baghi <= 50) {
            return listOf(
                "بودجهٔ کالری امروزت تقریباً تمام شد (${Abzar.adadFarsi(baghi.coerceAtLeast(0))} کالری مانده).",
                "اگر گشنه‌ای: سبزی خوردن، خیار یا یک لیوان دوغ — سبک و بی‌دردسر 🥒",
                "و کمی آب بنوش؛ گاهی بدن تشنگی را گرسنگی می‌فهمد!"
            )
        }

        // از پایگاه، غذاهایی که در بودجه جا می‌شوند و پروتئین خوب دارند
        val moratab = PayegahGhaza.ghazaHa
            .filter { it.kalar > 20 && (it.kalar * 1.5) <= baghi * 1.5 }
            .sortedByDescending { it.protein / (it.kalar.coerceAtLeast(30)).toDouble() }
            .take(40)
        val entekhabHa = ArrayList<Ghaza>()
        for (g in moratab) {
            if (entekhabHa.size >= 3) break
            if (g.daste == "چربی و سس" || g.daste == "شیرینی و تنقلات") continue
            entekhabHa.add(g)
        }

        val javab = ArrayList<String>()
        javab.add("با ${Abzar.adadFarsi(baghi)} کالری باقی‌مانده، این‌ها به صرفه‌ترین‌هاست:")
        for (g in entekhabHa) {
            val porzion = g.porzionGram
            val kalarPorzion = (g.kalar * porzion / 100.0).toInt()
            if (kalarPorzion <= baghi) {
                javab.add("• ${g.esm} — ${g.porzionEsm} (${Abzar.adadFarsi(porzion)} گرم) = ${Abzar.adadFarsi(kalarPorzion)} کالری و ${Abzar.adadFarsi(g.protein * porzion / 100.0)} گرم پروتئین")
            }
        }
        if (kambodProtein > 10) {
            javab.add("پروتئین امروزت ${Abzar.adadFarsi(kambodProtein)} گرم کم است؛ تخم‌مرغ، مرغ، ماست یا تن ماهی خوب جواب می‌دهند 💪")
        }
        javab.add("کدامش را می‌خواهی؟ در بخش «افزودن غذا» ثبتش کن.")
        return javab
    }

    // ---------- وضعیت امروز ----------
    fun vaziatEmrooz(context: Context): List<String> {
        val profil = Khazide.profilBekhon(context)
            ?: return listOf("اول پروفایلت را از صفحهٔ اصلی کامل کن تا تحلیل شخصی بگیری 🙂")
        val kelid = Abzar.kelidEmrooz()
        val majmoo = Khazide.majmooRooz(context, kelid)
        val hadaf = Khazide.hadafKalar(context)
        val sozand = Khazide.varTamSozande(context, kelid)
        val baghi = hadaf - majmoo.kalar + sozand
        val darsad = (majmoo.kalar * 100 / hadaf.coerceAtLeast(1))

        val javab = ArrayList<String>()
        javab.add("خلاصهٔ امروز (${Abzar.emrooz()}):")
        javab.add("🔥 خورده: ${Abzar.adadFarsi(majmoo.kalar)} از ${Abzar.adadFarsi(hadaf)} کالری (${Abzar.adadFarsi(darsad)}٪)")
        javab.add("💪 پروتئین: ${Abzar.adadFarsi(majmoo.protein)} از ${Abzar.adadFarsi(HesabHa.proteinHadaf(profil))} گرم")
        javab.add("🍞 کربوهیدرات: ${Abzar.adadFarsi(majmoo.karbohidrat)} از ${Abzar.adadFarsi(HesabHa.karbHadaf(profil))} گرم")
        javab.add("🥑 چربی: ${Abzar.adadFarsi(majmoo.charbi)} از ${Abzar.adadFarsi(HesabHa.charbiHadaf(profil))} گرم")
        if (sozand > 0) javab.add("🏃 با ورزش ${Abzar.adadFarsi(sozand)} کالری سوزاندی")
        javab.add("➡️ باقی‌مانده: ${Abzar.adadFarsi(baghi)} کالری")

        // تحلیل هوشمند: ۲ توصیهٔ اولویت‌دار
        val nasih = nasihHaRooz(context)
        if (nasih.isNotEmpty()) {
            javab.add("تحلیل من:")
            for (n in nasih.take(2)) javab.add("• $n")
        }
        return javab
    }

    // ---------- توصیه‌های هوشمند (برای صفحهٔ اصلی هم استفاده می‌شود) ----------
    fun nasihHaRooz(context: Context): List<String> {
        val profil = Khazide.profilBekhon(context) ?: return emptyList()
        val kelid = Abzar.kelidEmrooz()
        val majmoo = Khazide.majmooRooz(context, kelid)
        val ab = Khazide.abRooz(context, kelid)
        val hadafAb = Khazide.hadafAb(context)
        val nasih = ArrayList<String>()

        // پروتئین
        val hadafP = HesabHa.proteinHadaf(profil)
        if (majmoo.protein < hadafP * 0.5 && majmoo.kalar > hadafP * 0.6) {
            nasih.add("پروتئین امروزت کم است ولی کالری رفته؛ به‌جای برنج و نان، تخم‌مرغ یا ماست پروتئین‌دار بخور 💪")
        } else if (majmoo.protein < hadafP * 0.6) {
            nasih.add("پروتئین کم آورده‌ای؛ مرغ، تخم‌مرغ، لبنیات یا حبوبات فراموش نشود")
        }

        // کربوهیدرات
        val hadafK = HesabHa.karbHadaf(profil)
        if (majmoo.karbohidrat > hadafK * 1.15) {
            nasih.add("کربوهیدرات امروز زیاد شد؛ امشب سبک‌تر بمان (سالاد + پروتئین)")
        }

        // چربی
        val hadafCh = HesabHa.charbiHadaf(profil)
        if (majmoo.charbi > hadafCh * 1.2) {
            nasih.add("چربی امروز بالاست؛ خوراک سرخ‌کردنی را با گریل یا آب‌پز عوض کن 🥗")
        }

        // آب
        if (ab < hadafAb * 0.5) {
            nasih.add("آب هنوز نصف هدف نیست؛ یک لیوان همین حالا 🚰")
        }

        // بیش‌مصرف
        val baghi = HesabHa.baghiMandeh(context, kelid)
        if (baghi < -200) {
            nasih.add("از بودجهٔ امروز ${Abzar.adadFarsi(-baghi)} کالری بیشتر رفته؛ ۳۰ دقیقه پیاده‌روی جبرانش می‌کند 🚶")
        }

        // همه خوب
        if (nasih.isEmpty()) {
            nasih.add("امروز در همهٔ ردیف‌ها خوب پیش می‌روی؛ ادامه بده! 🌟")
        }
        return nasih
    }

    // ---------- وضعیت آب ----------
    private fun vaziatAb(context: Context): List<String> {
        val ab = Khazide.abRooz(context, Abzar.kelidEmrooz())
        val hadaf = Khazide.hadafAb(context)
        val mandeh = (hadaf - ab).coerceAtLeast(0)
        val javab = ArrayList<String>()
        javab.add("💧 آب امروز: ${Abzar.adadFarsi(ab)} لیوان از ${Abzar.adadFarsi(hadaf)}")
        if (mandeh > 0) {
            javab.add("${Abzar.adadFarsi(mandeh)} لیوان دیگر تا هدف — هر لیوان ۲۵۰ میلی‌لیتر.")
        } else {
            javab.add("هدف آب را پر کردی؛ عالی بود! 🌊")
        }
        return javab
    }

    // ---------- وضعیت وزن و BMI ----------
    private fun vaziatVazn(context: Context): List<String> {
        val profil = Khazide.profilBekhon(context)
            ?: return listOf("اول پروفایلت را کامل کن تا BMI و مسیر وزنت را حساب کنم 🙂")
        val b = HesabHa.bmi(profil)
        val javab = ArrayList<String>()
        javab.add("⚖️ وزن فعلی: ${Abzar.adadFarsi(profil.vazn)} کیلوگرم — BMI: ${Abzar.adadFarsi(b)} (${HesabHa.tosifBmi(b)})")
        val tafavot = profil.vazn - profil.vaznHadaf
        if (tafavot > 0.5) {
            javab.add("تا وزن هدف (${Abzar.adadFarsi(profil.vaznHadaf)} کیلو) ${Abzar.adadFarsi(tafavot)} کیلو مانده.")
            val rooz = HesabHa.roozTahadaf(profil)
            if (rooz != null) {
                javab.add("با سرعت ایمن (${Abzar.adadFarsi(0.5)} کیلو در هفته) حدود ${Abzar.adadFarsi(rooz)} روز دیگر به هدف می‌رسی 🎯")
            }
        } else if (tafavot < -0.5) {
            javab.add("وزنت ${Abzar.adadFarsi(-tafavot)} کیلو کمتر از هدف است؛ دقت کن سالم غذا بخوری 🌿")
        } else {
            javab.add("دقیقاً روی وزن هدف هستی؛ تثبیتش هم هنر می‌خواهد! 🏆")
        }
        // روند از ثبت‌های وزن
        val vaznHa = Khazide.vaznHa(context)
        if (vaznHa.size >= 2) {
            val aval = vaznHa.first().second
            val akhar = vaznHa.last().second
            val taghir = akhar - aval
            if (taghir < -0.3) {
                javab.add("روند از اول ثبت‌ها: ${Abzar.adadFarsi(-taghir)} کیلو کاهش — رو به پایین، همان که می‌خواستی 📉")
            } else if (taghir > 0.3) {
                javab.add("روند از اول ثبت‌ها: ${Abzar.adadFarsi(taghir)} کیلو افزایش — حواست به کالری باشد 📈")
            }
        }
        return javab
    }

    // ---------- وضعیت هدف کالری ----------
    private fun vaziatHadaf(context: Context): List<String> {
        val baghi = HesabHa.baghiMandeh(context, Abzar.kelidEmrooz())
        return if (baghi >= 0) {
            listOf(
                "کالری باقی‌ماندهٔ امروز: ${Abzar.adadFarsi(baghi)} 🔥",
                "می‌توانی با خیال راحت تا همین مقدار بخوری؛ پیشنهادهایم را هم ببین: بنویس «چه بخورم؟»"
            )
        } else {
            listOf(
                "امروز ${Abzar.adadFarsi(-baghi)} کالری از هدف بیشتر شده ⚠️",
                "یک راه: کمی ورزش (بنویس «ورزش» تا وضعیت را بگویم). یک راه دیگر: شام را سبک کن."
            )
        }
    }

    // ---------- وضعیت ورزش ----------
    private fun vaziatVarTam(context: Context): List<String> {
        val varTam = Khazide.varTamRooz(context, Abzar.kelidEmrooz())
        if (varTam.isEmpty()) {
            return listOf(
                "امروز هنوز ورزشی ثبت نکرده‌ای 🏃",
                "فقط ۳۰ دقیقه پیاده‌روی تند، حدود ۱۵۰ تا ۲۰۰ کالری می‌سوزاند و بودجهٔ غذایت را باز می‌کند. از بخش «ورزش» ثبتش کن!"
            )
        }
        val jam = varTam.sumOf { it.kalar }
        val javab = ArrayList<String>()
        javab.add("امروز ${Abzar.adadFarsi(jam)} کالری سوزاندی: 🔥")
        for (v in varTam) {
            javab.add("• ${v.esm} — ${Abzar.adadFarsi(v.daghighe)} دقیقه = ${Abzar.adadFarsi(v.kalar)} کالری")
        }
        javab.add("این کالری‌ها به بودجهٔ خوراک امروزت اضافه شدند ✅")
        return javab
    }
}
