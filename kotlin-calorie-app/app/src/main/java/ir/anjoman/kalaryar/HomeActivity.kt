package ir.anjoman.kalaryar

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

/*
 * داشبورد کالری‌یار: حلقهٔ کالری، درشت‌مغذی‌ها، آب، ورزش،
 * کارت دستیار هوشمند و میان‌بُرهای همهٔ بخش‌ها
 */
class HomeActivity : Activity() {

    private lateinit var ghabe: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Abzar.rangVaredKon(this)

        val root = LinearLayout(this)
        root.layoutDirection = View.LAYOUT_DIRECTION_RTL
        root.setBackgroundColor(Abzar.RANG_ZAMIN)

        val scroll = ScrollView(this)
        scroll.isFillViewport = true
        root.addView(scroll, ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        ))

        ghabe = LinearLayout(this)
        ghabe.orientation = LinearLayout.VERTICAL
        ghabe.setPadding(Abzar.dp(this, 18f), Abzar.dp(this, 22f), Abzar.dp(this, 18f), Abzar.dp(this, 22f))
        scroll.addView(ghabe, ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ))

        setContentView(root)
    }

    override fun onResume() {
        super.onResume()
        safheBekon()
    }

    private fun safheBekon() {
        ghabe.removeAllViews()
        val profil = Khazide.profilBekhon(this) ?: run {
            startActivity(Intent(this, OnboardingActivity::class.java))
            finish()
            return
        }
        val kelid = Abzar.kelidEmrooz()
        val majmoo = Khazide.majmooRooz(this, kelid)
        val hadafKalar = Khazide.hadafKalar(this)
        val sozand = Khazide.varTamSozande(this, kelid)
        val baghi = hadafKalar - majmoo.kalar + sozand

        // ---------- سرصفحه ----------
        val salam = if (profil.jens == "mard") "سلام پهلوان!" else "سلام خانم!"
        val matnSalam = Abzar.tv(this, salam, 26f, Abzar.RANG_MATN)
        matnSalam.typeface = FontBox.vazirBlack(this)
        ghabe.addView(matnSalam)

        val safSar = LinearLayout(this)
        safSar.orientation = LinearLayout.HORIZONTAL
        safSar.gravity = Gravity.CENTER_VERTICAL
        val tarikh = Abzar.tv(this, Abzar.emrooz(), 13f, Abzar.RANG_KAMRANG)
        safSar.addView(tarikh, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))
        val estric = Khazide.estrik(this)
        val shiron = Abzar.tv(
            this, "🔥 ${Abzar.adadFarsi(estric)} روز پیوسته", 13f, Abzar.RANG_TALAEI, bold = true
        )
        shiron.background = Abzar.kart(this, Abzar.RANG_CODE, 12f)
        shiron.setPadding(Abzar.dp(this, 10f), Abzar.dp(this, 6f), Abzar.dp(this, 10f), Abzar.dp(this, 6f))
        safSar.addView(shiron)
        val pSafSar = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pSafSar.topMargin = Abzar.dp(this, 4f)
        ghabe.addView(safSar, pSafSar)

        // ---------- کارت اصلی: حلقهٔ کالری ----------
        val kartAsli = LinearLayout(this)
        kartAsli.orientation = LinearLayout.VERTICAL
        kartAsli.background = Abzar.kartGradient(
            this,
            if (baghi >= 0) Abzar.RANG_CODE else 0xFF2B1414.toInt(),
            Abzar.RANG_KART, 24f
        )
        kartAsli.setPadding(Abzar.dp(this, 16f), Abzar.dp(this, 16f), Abzar.dp(this, 16f), Abzar.dp(this, 16f))

        val halghe = HalgheView(this)
        halghe.layoutParams = LinearLayout.LayoutParams(
            Abzar.dp(this, 210f), Abzar.dp(this, 210f)
        ).also { it.gravity = Gravity.CENTER_HORIZONTAL }
        halghe.pishraft = (majmoo.kalar.toFloat() / hadafKalar.coerceAtLeast(1))
        halghe.matnVasat = Abzar.adadFarsi(baghi.coerceAtLeast(0))
        halghe.matnZir = if (baghi >= 0) "کالری باقی‌مانده" else "کالری اضافه"
        halghe.rangAval = if (baghi >= 0) Abzar.RANG_SABZ else Abzar.RANG_TALAEI
        halghe.rangDovom = if (baghi >= 0) Abzar.RANG_SABZKAM else Abzar.RANG_NARENJI
        kartAsli.addView(halghe)

        // سه‌خطی زیر حلقه: خورده / سوزانده / هدف
        val safAmar = LinearLayout(this)
        safAmar.orientation = LinearLayout.HORIZONTAL
        safAmar.gravity = Gravity.CENTER
        safAmar.addView(hooshAmar("خورده", majmoo.kalar, Abzar.RANG_TALAEI))
        safAmar.addView(hooshAmar("سوزانده", sozand, Abzar.RANG_NARENJI))
        safAmar.addView(hooshAmar("هدف", hadafKalar, Abzar.RANG_SABZ))
        val pAmar = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pAmar.topMargin = Abzar.dp(this, 6f)
        kartAsli.addView(safAmar, pAmar)

        // ---------- درشت‌مغذی‌ها ----------
        kartAsli.addView(jaFazol(16))
        val sarMakro = Abzar.tv(this, "درشت‌مغذی‌های امروز", 14f, Abzar.RANG_MATN, bold = true)
        kartAsli.addView(sarMakro)
        kartAsli.addView(nobar("💪 پروتئین", majmoo.protein, Khazide.hadafProtein(this).toDouble(), Abzar.RANG_BANAFSH))
        kartAsli.addView(nobar("🍞 کربوهیدرات", majmoo.karbohidrat, Khazide.hadafKarb(this).toDouble(), Abzar.RANG_ZARD))
        kartAsli.addView(nobar("🥑 چربی", majmoo.charbi, Khazide.hadafCharbi(this).toDouble(), Abzar.RANG_NARENJI))

        val pAsli = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pAsli.topMargin = Abzar.dp(this, 18f)
        ghabe.addView(kartAsli, pAsli)

        // ---------- آب + BMI ----------
        val safAb = LinearLayout(this)
        safAb.orientation = LinearLayout.HORIZONTAL
        safAb.gravity = Gravity.CENTER_VERTICAL

        val kartAb = LinearLayout(this)
        kartAb.orientation = LinearLayout.HORIZONTAL
        kartAb.gravity = Gravity.CENTER_VERTICAL
        kartAb.background = Abzar.kart(this, Abzar.RANG_KART, 18f)
        kartAb.setPadding(Abzar.dp(this, 14f), Abzar.dp(this, 12f), Abzar.dp(this, 14f), Abzar.dp(this, 12f))
        kartAb.isClickable = true
        Abzar.lams(kartAb)
        kartAb.setOnClickListener { startActivity(Intent(this, AbActivity::class.java)) }

        val ab = Khazide.abRooz(this, kelid)
        val hadafAb = Khazide.hadafAb(this)
        val halgheAb = HalgheKoochak(this)
        halgheAb.layoutParams = LinearLayout.LayoutParams(Abzar.dp(this, 44f), Abzar.dp(this, 44f))
        halgheAb.pishraft = ab.toFloat() / hadafAb.coerceAtLeast(1)
        kartAb.addView(halgheAb)
        val sotoonAb = LinearLayout(this)
        sotoonAb.orientation = LinearLayout.VERTICAL
        val matnAb = Abzar.tv(this, "آب", 14f, Abzar.RANG_MATN, bold = true)
        sotoonAb.addView(matnAb)
        val matnAb2 = Abzar.tv(this, "${Abzar.adadFarsi(ab)} از ${Abzar.adadFarsi(hadafAb)} لیوان", 12f, Abzar.RANG_KAMRANG)
        sotoonAb.addView(matnAb2)
        kartAb.addView(sotoonAb, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f).also {
            it.marginStart = Abzar.dp(this, 10f)
        })
        safAb.addView(kartAb, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))

        val kartBmi = LinearLayout(this)
        kartBmi.orientation = LinearLayout.VERTICAL
        kartBmi.background = Abzar.kart(this, Abzar.RANG_KART, 18f)
        kartBmi.setPadding(Abzar.dp(this, 14f), Abzar.dp(this, 12f), Abzar.dp(this, 14f), Abzar.dp(this, 12f))
        kartBmi.isClickable = true
        Abzar.lams(kartBmi)
        kartBmi.setOnClickListener { startActivity(Intent(this, VaznActivity::class.java)) }
        val b = HesabHa.bmi(profil)
        val matnBmi = Abzar.tv(this, "BMI ${Abzar.adadFarsi(b)}", 14f, Abzar.RANG_MATN, bold = true)
        kartBmi.addView(matnBmi)
        val matnBmi2 = Abzar.tv(this, HesabHa.tosifBmi(b), 12f, Abzar.RANG_KAMRANG)
        kartBmi.addView(matnBmi2)
        val pBmi = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
        pBmi.marginStart = Abzar.dp(this, 12f)
        safAb.addView(kartBmi, pBmi)

        val pSafAb = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pSafAb.topMargin = Abzar.dp(this, 14f)
        ghabe.addView(safAb, pSafAb)

        // ---------- کارت دستیار هوشمند ----------
        val kartAI = LinearLayout(this)
        kartAI.orientation = LinearLayout.VERTICAL
        kartAI.background = Abzar.kartGradient(this, 0xFF123326.toInt(), Abzar.RANG_KART, 20f)
        kartAI.setPadding(Abzar.dp(this, 16f), Abzar.dp(this, 14f), Abzar.dp(this, 16f), Abzar.dp(this, 14f))
        kartAI.isClickable = true
        Abzar.lams(kartAI)
        kartAI.setOnClickListener { startActivity(Intent(this, HoshmandActivity::class.java)) }

        val sarAI = Abzar.tv(this, "🤖 دستیار هوشمند می‌گوید:", 14f, Abzar.RANG_SABZ, bold = true)
        kartAI.addView(sarAI)
        val nasihHa = HoshmandYar.nasihHaRooz(this)
        val matnAI = Abzar.tv(
            this,
            if (nasihHa.isNotEmpty()) nasihHa[0] else "امروز چیزی ثبت نکرده‌ای؛ از «افزودن غذا» شروع کن!",
            14f, Abzar.RANG_MATN
        )
        matnAI.setPadding(0, Abzar.dp(this, 8f), 0, 0)
        kartAI.addView(matnAI)
        val matnAI2 = Abzar.tv(this, "برای گفت‌وگو لمس کن ›", 12f, Abzar.RANG_SABZ)
        matnAI2.setPadding(0, Abzar.dp(this, 8f), 0, 0)
        kartAI.addView(matnAI2)

        val pAI = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pAI.topMargin = Abzar.dp(this, 14f)
        ghabe.addView(kartAI, pAI)

        // ---------- میان‌بُرها ----------
        ghabe.addView(jaFazol(6))
        val sarMenu = Abzar.tv(this, "بخش‌های اپ", 16f, Abzar.RANG_TALAEI, bold = true)
        ghabe.addView(sarMenu)

        ghabe.addView(khoroo("＋  افزودن غذا", "جست‌وجو در ${Abzar.adadFarsi(PayegahGhaza.ghazaHa.size)} خوراکی با ریزمغذی", FoodsActivity::class.java))
        ghabe.addView(khoroo("📖  دفترچهٔ امروز", "${Abzar.adadFarsi(Khazide.vorodiHaRooz(this, kelid).size)} مورد ثبت‌شده", DiaryActivity::class.java))
        ghabe.addView(khoroo("⚖️  وزن و BMI", "ثبت وزن، نمودار روند و مسیر هدف", VaznActivity::class.java))
        ghabe.addView(khoroo("🏃  ورزش", "${Abzar.adadFarsi(sozand)} کالری امروز سوزانده‌ای", VartamActivity::class.java))
        ghabe.addView(khoroo("📊  گزارش هفته", "نمودار کالری ۷ روز + تحلیل", GozareshActivity::class.java))
        ghabe.addView(khoroo("🤖  دستیار هوشمند", "سؤال بپرس: «الان چی بخورم؟»", HoshmandActivity::class.java))
    }

    // یک آمار کوچک زیر حلقه
    private fun hooshAmar(matn: String, meghdar: Int, rang: Int): LinearLayout {
        val s = LinearLayout(this)
        s.orientation = LinearLayout.VERTICAL
        s.gravity = Gravity.CENTER
        s.setPadding(Abzar.dp(this, 14f), 0, Abzar.dp(this, 14f), 0)
        val m1 = Abzar.tv(this, Abzar.adadFarsi(meghdar), 18f, rang, bold = true)
        s.addView(m1)
        val m2 = Abzar.tv(this, matn, 11f, Abzar.RANG_KAMRANG)
        s.addView(m2)
        return s
    }

    // نوار پیشرفت درشت‌مغذی
    private fun nobar(matn: String, mizaan: Double, hadaf: Double, rang: Int): LinearLayout {
        val saf = LinearLayout(this)
        saf.orientation = LinearLayout.VERTICAL
        val pSaf = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pSaf.topMargin = Abzar.dp(this, 10f)
        saf.layoutParams = pSaf

        val sar = Abzar.tv(
            this,
            "$matn — ${Abzar.adadFarsi(mizaan)} از ${Abzar.adadFarsi(hadaf)} گرم",
            12f, Abzar.RANG_KAMRANG
        )
        saf.addView(sar)

        val nobat = LinearLayout(this)
        nobat.background = Abzar.kart(this, Abzar.RANG_KHAKISTARI, 6f)
        val pNobat = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, Abzar.dp(this, 8f)
        )
        pNobat.topMargin = Abzar.dp(this, 4f)

        val darsad = (mizaan / hadaf.coerceAtLeast(1.0)).coerceIn(0.0, 1.0)
        val por = View(this)
        por.background = Abzar.kart(this, rang, 6f)
        nobat.addView(por, LinearLayout.LayoutParams(0, Abzar.dp(this, 8f), darsad.toFloat()))
        val khali = View(this)
        nobat.addView(khali, LinearLayout.LayoutParams(0, Abzar.dp(this, 8f), (1 - darsad).toFloat()))
        saf.addView(nobat, pNobat)
        return saf
    }

    // کارت میان‌بُر
    private fun khoroo(onvan: String, tozih: String, hadaf: Class<out Activity>): LinearLayout {
        val kart = LinearLayout(this)
        kart.orientation = LinearLayout.HORIZONTAL
        kart.gravity = Gravity.CENTER_VERTICAL
        kart.background = Abzar.kart(this, Abzar.RANG_KART)
        kart.setPadding(Abzar.dp(this, 14f), Abzar.dp(this, 14f), Abzar.dp(this, 14f), Abzar.dp(this, 14f))
        kart.isClickable = true
        Abzar.lams(kart)
        kart.setOnClickListener { startActivity(Intent(this, hadaf)) }

        val sotoon = LinearLayout(this)
        sotoon.orientation = LinearLayout.VERTICAL
        val matnOnvan = Abzar.tv(this, onvan, 16f, Abzar.RANG_MATN, bold = true)
        sotoon.addView(matnOnvan)
        val matnTozih = Abzar.tv(this, tozih, 12f, Abzar.RANG_KAMRANG)
        matnTozih.setPadding(0, Abzar.dp(this, 3f), 0, 0)
        sotoon.addView(matnTozih)
        kart.addView(sotoon, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))

        val flash = Abzar.tv(this, "‹", 22f, Abzar.RANG_KAMRANG)
        kart.addView(flash)

        val p = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        p.topMargin = Abzar.dp(this, 10f)
        kart.layoutParams = p
        return kart
    }

    private fun jaFazol(dp: Int): View {
        val v = View(this)
        v.layoutParams = LinearLayout.LayoutParams(1, Abzar.dp(this, dp.toFloat()))
        return v
    }
}
