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
 */
class QuizActivity : Activity() {

    private lateinit var dars: Dars
    private var soalFaali = 0
    private var emtiyaz = 0
    private var javabDadim = false
    private lateinit var ghabe: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Abzar.rangVaredKon(this)

        dars = DarsHa.baShomareh(intent.getIntExtra("shomareh_dars", 1))

        val root = LinearLayout(this)
        root.orientation = LinearLayout.VERTICAL
        root.layoutDirection = View.LAYOUT_DIRECTION_RTL
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
        val soal = dars.soalHa[soalFaali]
        ghabe.removeAllViews()

        // سرصفحه
        val matnDars = Abzar.tv(this, "آزمون: ${dars.onvan}", 14f, Abzar.RANG_KAMRANG)
        ghabe.addView(matnDars)

        val matnShomareh = Abzar.tv(
            this,
            "سؤال ${Abzar.adadFarsi(soalFaali + 1)} از ${Abzar.adadFarsi(dars.soalHa.size)}",
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
        if (soalFaali < dars.soalHa.size) {
            soalNeshanBedeh()
        } else {
            natijeNeshanBedeh()
        }
    }

    // صفحهٔ نتیجه: ستاره‌ها + نمره + دکمه‌ها
    private fun natijeNeshanBedeh() {
        Zakhire.zakhireKon(this, dars.shomareh, emtiyaz)
        ghabe.removeAllViews()

        // ستاره‌ها
        val setareHa = when (emtiyaz) {
            3 -> "★ ★ ★"
            2 -> "★ ★ ☆"
            1 -> "★ ☆ ☆"
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
            "${Abzar.adadFarsi(emtiyaz)} از ${Abzar.adadFarsi(dars.soalHa.size)} درست",
            24f, Abzar.RANG_MATN, bold = true
        )
        matnNatije.gravity = Gravity.CENTER
        val pNatije = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pNatije.topMargin = Abzar.dp(this, 16f)
        ghabe.addView(matnNatije, pNatije)

        // جملهٔ تشویقی
        val taarif = when (emtiyaz) {
            3 -> "کامل بود! آفرین"
            2 -> "خوب بود! یه بار دیگه بزن تا کامل شه"
            1 -> "شروع خوبی بود، توضیحات درس رو یه بار دیگه بخون"
            else -> "اشکالی نداره! درس رو بخون و دوباره امتحان کن"
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
        val behtarin = Zakhire.behtarin(this, dars.shomareh)
        val matnBehtarin = Abzar.tv(
            this,
            "بهترین نتیجه: ${Abzar.adadFarsi(behtarin)} از ۳",
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
        val dokmeDobare = Abzar.dokme(this, "دوباره", Abzar.RANG_TIZ, Abzar.RANG_TIZ, khateDor = true)
        dokmeDobare.setOnClickListener {
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
        if (dars.shomareh < DarsHa.darsList.size) {
            val dokmeBadi = Abzar.dokme(this, "درس بعدی", Abzar.RANG_TIZ, Abzar.RANG_ZAMIN)
            dokmeBadi.setOnClickListener {
                val niat = Intent(this, LessonActivity::class.java)
                niat.putExtra("shomareh_dars", dars.shomareh + 1)
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
            val dokmeFehrest = Abzar.dokme(this, "فهرست درس‌ها", Abzar.RANG_TIZ, Abzar.RANG_ZAMIN)
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
