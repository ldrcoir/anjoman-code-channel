package ir.anjoman.amoozeshkotlin

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

/*
 * صفحهٔ آزمون: سؤال به سؤال می‌ریم جلو
 * جواب درست سبز می‌شه، غلط قرمز و بعد از ۷۰۰ میلی‌ثانیه می‌ریم سؤال بعد
 * دو حالت دارد: آزمون یک درس، یا آزمون جامع (۱۵ سؤال تصادفی از همهٔ درس‌ها)
 */
class QuizActivity : Activity() {

    private var dars: Dars? = null
    private var jameAst = false
    private lateinit var soalHa: List<Soal>
    private var soalFaali = 0
    private var emtiyaz = 0
    private var javabDadim = false
    private lateinit var ghabe: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Abzar.rangVaredKon(this)

        jameAst = intent.getBooleanExtra("azmoon_jame", false)
        if (jameAst) {
            // ۱۵ سؤال تصادفی از میان همهٔ درس‌ها
            val hame = DarsHa.darsList.flatMap { it.soalHa }
            soalHa = hame.shuffled().take(15)
            AmarSanj.sabt(applicationContext, "quiz_start", org.json.JSONObject().put("jame", true))
        } else {
            dars = Tarjomeh.baShomareh(Zaban.bekhon(this), intent.getIntExtra("shomareh_dars", 1))
            soalHa = dars!!.soalHa
        }

        val root = LinearLayout(this)
        root.orientation = LinearLayout.VERTICAL
        root.layoutDirection =
            if (Zaban.rastChin(this)) View.LAYOUT_DIRECTION_RTL else View.LAYOUT_DIRECTION_LTR
        root.setBackgroundColor(Abzar.RANG_ZAMIN)

        val scroll = ScrollView(this)
        scroll.isFillViewport = true
        root.addView(
            scroll,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )

        ghabe = LinearLayout(this)
        ghabe.orientation = LinearLayout.VERTICAL
        ghabe.setPadding(Abzar.dp(this, 20f), Abzar.dp(this, 24f), Abzar.dp(this, 20f), Abzar.dp(this, 24f))
        scroll.addView(
            ghabe,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )

        setContentView(root)
        soalNeshanBedeh()
    }

    // نمایش سؤال فعلی با گزینه‌هاش
    private fun soalNeshanBedeh() {
        javabDadim = false
        val soal = soalHa[soalFaali]
        ghabe.removeAllViews()

        // سرصفحه
        val sar = if (jameAst) {
            Zaban.t("azmoon_jame")
        } else {
            "${Zaban.t("azmoon")}: ${dars!!.onvan}"
        }
        val matnDars = Abzar.tv(this, sar, 14f, Abzar.RANG_KAMRANG)
        ghabe.addView(matnDars)

        val matnShomareh = Abzar.tv(
            this,
            "${Zaban.t("soal")} ${Zaban.adad(soalFaali + 1)} ${Zaban.t("az")} ${Zaban.adad(soalHa.size)}",
            14f, Abzar.RANG_TALAEI, bold = true
        )
        matnShomareh.setPadding(0, Abzar.dp(this, 4f), 0, 0)
        ghabe.addView(matnShomareh)

        // کارت سؤال
        val kartSoal = LinearLayout(this)
        kartSoal.orientation = LinearLayout.VERTICAL
        kartSoal.background = Abzar.kart(this)
        kartSoal.setPadding(
            Abzar.dp(this, 18f), Abzar.dp(this, 18f),
            Abzar.dp(this, 18f), Abzar.dp(this, 18f)
        )
        val matnSoal = Abzar.tv(this, soal.matn, 17f, Abzar.RANG_MATN, bold = true)
        kartSoal.addView(matnSoal)

        val pKartSoal = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pKartSoal.topMargin = Abzar.dp(this, 20f)
        ghabe.addView(kartSoal, pKartSoal)

        // گزینه‌ها؛ همه رو نگه می‌داریم که موقع جواب رنگشون رو عوض کنیم
        val hameGozineh = mutableListOf<TextView>()
        soal.gozinehHa.forEachIndexed { index, gozineh ->
            val dokme = TextView(this)
            dokme.text = gozineh
            dokme.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
            dokme.setTextColor(Abzar.RANG_MATN)
            dokme.typeface = FontBox.vazir(this)
            dokme.background = Abzar.kart(this)
            dokme.setPadding(
                Abzar.dp(this, 16f), Abzar.dp(this, 14f),
                Abzar.dp(this, 16f), Abzar.dp(this, 14f)
            )
            dokme.isClickable = true
            Abzar.lams(dokme)
            dokme.setOnClickListener {
                if (javabDadim) return@setOnClickListener
                javabDadim = true
                javabCheckKon(dokme, index, soal, hameGozineh)
            }
            hameGozineh.add(dokme)

            val pGozineh = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            pGozineh.topMargin = Abzar.dp(this, 12f)
            ghabe.addView(dokme, pGozineh)
        }
    }

    // رنگ درست/غلط و بازخورد کوتاه، بعدش سؤال بعدی
    private fun javabCheckKon(
        entekhab: TextView,
        shomarehEntekhab: Int,
        soal: Soal,
        hameGozineh: List<TextView>
    ) {
        val sahihBood = shomarehEntekhab == soal.shomarehSahih
        if (sahihBood) {
            emtiyaz++
            entekhab.background = Abzar.kart(this, Abzar.RANG_SABZ)
            entekhab.setTextColor(Color.WHITE)
        } else {
            entekhab.background = Abzar.kart(this, Abzar.RANG_GHERMEZ)
            entekhab.setTextColor(Color.WHITE)
            // جواب درست رو هم سبز می‌کنیم که یاد بمونه
            val dorost = hameGozineh[soal.shomarehSahih]
            dorost.background = Abzar.kart(this, Abzar.RANG_SABZ)
            dorost.setTextColor(Color.WHITE)
        }

        // آمار: هر جواب ثبت می‌شود
        AmarSanj.sabt(
            applicationContext, "quiz_answer",
            org.json.JSONObject()
                .put("sahih", sahihBood)
                .put("jame", jameAst)
        )

        // دیگه هیچ گزینه‌ای کلیک نمی‌شه
        for (g in hameGozineh) g.isClickable = false

        val tafoog = Abzar.tv(
            this,
            if (sahihBood) soal.tafoogSahih else soal.tafoogGhalat,
            14f,
            if (sahihBood) Abzar.RANG_SABZ else Abzar.RANG_GHERMEZ,
            bold = true
        )
        val pTafoog = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pTafoog.topMargin = Abzar.dp(this, 16f)
        ghabe.addView(tafoog, pTafoog)

        // یه لحظه صبر و بعد سؤال بعدی
        Handler(Looper.getMainLooper()).postDelayed({ soalBadi() }, 700)
    }

    private fun soalBadi() {
        soalFaali++
        if (soalFaali < soalHa.size) {
            soalNeshanBedeh()
        } else {
            natijeNeshanBedeh()
        }
    }

    // صفحهٔ نتیجه: ستاره‌ها + نمره + دکمه‌ها
    private fun natijeNeshanBedeh() {
        val kol = soalHa.size
        if (jameAst) {
            Zakhire.zakhireJame(this, emtiyaz)
        } else {
            Zakhire.zakhireKon(this, dars!!.shomareh, emtiyaz)
        }
        Zakhire.afzayeshTest(this)
        if (Zakhire.bayadTablighNeshanBedeh(this, EmaRoidConfig.TABLIGH_KAMEL_HAR_N_TEST)) {
            TablighYar.jaizVagoft(this) { }
            Zakhire.tablighNeshanDad(this)
        }

        AmarSanj.sabt(
            applicationContext, "quiz_done",
            org.json.JSONObject()
                .put("emtiyaz", emtiyaz).put("kol", kol).put("jame", jameAst)
        )

        ghabe.removeAllViews()

        // ستاره‌ها — نسبت نمره به کل سؤال‌ها
        val nesbat = if (kol == 0) 0f else emtiyaz.toFloat() / kol
        val setareHa = when {
            nesbat >= 0.999f -> "★ ★ ★"
            nesbat >= 0.6f -> "★ ★ ☆"
            nesbat >= 0.3f -> "★ ☆ ☆"
            else -> "☆ ☆ ☆"
        }
        val matnSetare = Abzar.tv(this, setareHa, 40f, Abzar.RANG_TALAEI)
        matnSetare.gravity = Gravity.CENTER
        val pSetare = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pSetare.topMargin = Abzar.dp(this, 48f)
        ghabe.addView(matnSetare, pSetare)

        // نمره
        val matnNatije = Abzar.tv(
            this,
            "${Zaban.adad(emtiyaz)} ${Zaban.t("az")} ${Zaban.adad(kol)} ${Zaban.t("sahih")}",
            24f, Abzar.RANG_MATN, bold = true
        )
        matnNatije.gravity = Gravity.CENTER
        val pNatije = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pNatije.topMargin = Abzar.dp(this, 16f)
        ghabe.addView(matnNatije, pNatije)

        // جملهٔ تشویقی — بر اساس نسبت
        val taarif = when {
            nesbat >= 0.999f -> Zaban.t("taarif_3")
            nesbat >= 0.6f -> Zaban.t("taarif_2")
            nesbat >= 0.3f -> Zaban.t("taarif_1")
            else -> Zaban.t("taarif_0")
        }
        val matnTaarif = Abzar.tv(this, taarif, 15f, Abzar.RANG_KAMRANG)
        matnTaarif.gravity = Gravity.CENTER
        val pTaarif = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pTaarif.topMargin = Abzar.dp(this, 12f)
        ghabe.addView(matnTaarif, pTaarif)

        // بهترین نتیجهٔ ذخیره‌شده
        val behtarin = if (jameAst) Zakhire.behtarinJame(this) else Zakhire.behtarin(this, dars!!.shomareh)
        val matnBehtarin = Abzar.tv(
            this,
            "${Zaban.t("behtarin_natije")}: ${Zaban.adad(behtarin.coerceAtLeast(0))} ${Zaban.t("az")} ${Zaban.adad(kol)}",
            13f, Abzar.RANG_KAMRANG
        )
        matnBehtarin.gravity = Gravity.CENTER
        val pBehtarin = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pBehtarin.topMargin = Abzar.dp(this, 8f)
        ghabe.addView(matnBehtarin, pBehtarin)

        // دوباره
        val dokmeDobare = Abzar.dokme(this, Zaban.t("dore_jadid"), Abzar.RANG_TIZ, Abzar.RANG_TIZ, khateDor = true)
        dokmeDobare.setOnClickListener {
            if (jameAst) {
                val hame = DarsHa.darsList.flatMap { it.soalHa }
                soalHa = hame.shuffled().take(15)
            }
            soalFaali = 0
            emtiyaz = 0
            soalNeshanBedeh()
        }
        val pDobare = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pDobare.topMargin = Abzar.dp(this, 32f)
        ghabe.addView(dokmeDobare, pDobare)

        // درس بعدی یا برگشتن به فهرست
        if (!jameAst && dars!!.shomareh < DarsHa.darsList.size) {
            val dokmeBadi = Abzar.dokme(this, "${Zaban.t("dars")} ${Zaban.t("badi")}", Abzar.RANG_TIZ, Abzar.RANG_ZAMIN)
            dokmeBadi.setOnClickListener {
                val niat = Intent(this, LessonActivity::class.java)
                niat.putExtra("shomareh_dars", dars!!.shomareh + 1)
                startActivity(niat)
                finish()
            }
            val pBadi = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            pBadi.topMargin = Abzar.dp(this, 12f)
            ghabe.addView(dokmeBadi, pBadi)
        } else {
            val dokmeFehrest = Abzar.dokme(this, Zaban.t("dars_ha"), Abzar.RANG_TIZ, Abzar.RANG_ZAMIN)
            dokmeFehrest.setOnClickListener {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
            val pFehrest = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            pFehrest.topMargin = Abzar.dp(this, 12f)
            ghabe.addView(dokmeFehrest, pFehrest)
        }
    }
}
