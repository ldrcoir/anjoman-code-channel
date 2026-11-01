package ir.anjoman.kalaryar

import kotlin.math.abs
import kotlin.math.roundToInt

/*
 * محاسبات سلامت — فرمول استاندارد میفلین-سنت‌جیور
 * BMR → TDEE → کالری هدف → درشت‌مغذی‌ها + BMI و پیشرفت هدف
 */

object HesabHa {

    // ضریب فعالیت: ۱ کم‌تحرک، ۲ سبک، ۳ متوسط، ۴ سنگین
    val zarayebFaaliat = listOf(1.2, 1.375, 1.55, 1.725)
    val asamiFaaliat = listOf("کم‌تحرک", "کم‌تحرک با فعالیت سبک", "متوسط (۳-۴ روز ورزش)", "فعال یا ورزش منظم")

    fun bmr(profil: Profil): Int {
        // میفلین-سنت‌جیور: ۱۰×وزن + ۶٫۲۵×قد − ۵×سن
        val asl = 10 * profil.vazn + 6.25 * profil.ghad - 5 * profil.sen
        return (if (profil.jens == "mard") asl + 5 else asl - 161).roundToInt()
    }

    fun tdee(profil: Profil): Int =
        (bmr(profil) * zarayebFaaliat[profil.faaliat - 1]).roundToInt()

    // کالری هدف با کفِ ایمن
    fun kalarHadaf(profil: Profil): Int {
        val t = tdee(profil)
        val kham = when (profil.nooHadaf) {
            "kam" -> t - 500
            "ziaad" -> t + 400
            else -> t
        }
        val kaf = if (profil.jens == "mard") 1500 else 1200
        return kham.coerceAtLeast(kaf)
    }

    // درشت‌مغذی‌ها: ۳۰٪ پروتئین، ۴۰٪ کربوهیدرات، ۳۰٪ چربی
    fun proteinHadaf(profil: Profil): Int = (kalarHadaf(profil) * 0.30 / 4).roundToInt()
    fun karbHadaf(profil: Profil): Int = (kalarHadaf(profil) * 0.40 / 4).roundToInt()
    fun charbiHadaf(profil: Profil): Int = (kalarHadaf(profil) * 0.30 / 9).roundToInt()

    // آب: هر کیلو ۳۵ میلی‌لیتر → تقسیم بر ۲۵۰ = تعداد لیوان
    fun abHadaf(profil: Profil): Int =
        ((profil.vazn * 35 / 250).roundToInt()).coerceIn(6, 14)

    fun hadafZakhireAzProfil(context: android.content.Context, profil: Profil) {
        Khazide.hadafZakhire(
            context,
            kalarHadaf(profil),
            proteinHadaf(profil),
            karbHadaf(profil),
            charbiHadaf(profil),
            abHadaf(profil)
        )
    }

    // ---------- BMI ----------
    fun bmi(profil: Profil): Double {
        val ghadMetr = profil.ghad / 100.0
        return profil.vazn / (ghadMetr * ghadMetr)
    }

    fun tosifBmi(b: Double): String = when {
        b < 18.5 -> "کمبود وزن"
        b < 25 -> "وزن نرمال ✓"
        b < 30 -> "اضافه وزن"
        else -> "چاقی"
    }

    // چند روز تا رسیدن به وزن هدف (با سرعت ایمن ۰٫۵ کیلو در هفته)
    fun roozTahadaf(profil: Profil): Int? {
        val tafavot = abs(profil.vazn - profil.vaznHadaf)
        if (tafavot < 0.5) return null
        val kamShavande = if (profil.nooHadaf == "kam") 0.5 else
            if (profil.nooHadaf == "ziaad") 0.4 else 0.0
        if (kamShavande == 0.0) return null
        return (tafavot / kamShavande * 7).roundToInt()
    }

    // ---------- ورزش: کالری با MET ----------
    data class FaaliatVarzesi(val esm: String, val met: Double, val aykoon: String)

    val varTamHa = listOf(
        FaaliatVarzesi("پیاده‌روی تند", 4.3, "🚶"),
        FaaliatVarzesi("دویدن آرام", 8.0, "🏃"),
        FaaliatVarzesi("دویدن سریع", 11.0, "💨"),
        FaaliatVarzesi("دوچرخه‌سواری", 7.0, "🚲"),
        FaaliatVarzesi("شنا", 8.0, "🏊"),
        FaaliatVarzesi("فوتبال", 7.0, "⚽"),
        FaaliatVarzesi("والیبال", 4.0, "🏐"),
        FaaliatVarzesi("بسکتبال", 8.0, "🏀"),
        FaaliatVarzesi("طناب زدن", 11.0, "🪢"),
        FaaliatVarzesi("تمرین با وزنه", 5.0, "🏋"),
        FaaliatVarzesi("تمرین در خانه", 5.0, "🤸"),
        FaaliatVarzesi("یوگا و کشش", 3.0, "🧘"),
        FaaliatVarzesi("کوهپیمایی", 6.0, "⛰"),
        FaaliatVarzesi("بدمینتون", 5.5, "🏸")
    )

    // کالری = MET × ۳٫۵ × وزن(kg) ÷ ۲۰۰ × دقیقه
    fun kalarVarzesi(met: Double, vazn: Double, daghighe: Int): Int =
        (met * 3.5 * vazn / 200 * daghighe).roundToInt()

    // جمع نهایی امروز: هدف منفی خورده‌شده + سوزانده‌شده
    fun baghiMandeh(context: android.content.Context, kelid: String): Int {
        val profil = Khazide.profilBekhon(context) ?: return 2000
        val khord = Khazide.majmooRooz(context, kelid).kalar
        val sozand = Khazide.varTamSozande(context, kelid)
        return kalarHadaf(profil) - khord + sozand
    }
}
