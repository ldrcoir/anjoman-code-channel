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
    private lateinit var soalHaFaali: List<Soal>   // سؤال‌های این دور (درس یا جامع)
    private var azmoonJame = false
    private var shomarehZakhire = 1              // کلید ذخیرهٔ بهترین نمره
    private var soalFaali = 0
    private var emtiyaz = 0
    private var javabDadim = false
    private lateinit var ghabe: LinearLayout
    private val entekhabHa = mutableListOf<Int>() // جواب هر سؤال برای بازبینی آخر

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Abzar.rangVaredKon(this)

        azmoonJame = intent.getBooleanExtra("azmoon_jame", false)
        if (azmoonJame) {
            // آزمون جامع: ۱۵ سؤال تصادفی از همهٔ درس‌ها
            shomarehZakhire = 999
            soalHaFaali = DarsHa.darsList
                .flatMap { it.soalHa }
                .shuffled()
                .take(15)
        } else {
            dars = DarsHa.baShomareh(intent.getIntExtra("shomareh_dars", 1))
            shomarehZakhire = dars.shomareh
            soalHaFaali = dars.soalHa
        }

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

    // عنوان بالای آزمون
    private fun onvanSar(): String {
        return if (azmoonJame) "آزمون جامع — از همهٔ درس‌ها"
        else "آزمون: ${dars.onvan}"
    }

    // نمایش سؤال فعلی با گزینه‌هاش
    private fun soalNeshanBedeh() {
        javabDadim = false
        val soal = soalHaFaali[soalFaali]
        ghabe.removeAllViews()

        // سرصفحه
        val matnDars = Abzar.tv(this, onvanSar(), 14f, Abzar.RANG_KAMRANG)
        ghabe.addView(matnDars)

        val matnShomareh = Abzar.tv(
            this,
            "سؤال ${Abzar.adadFarsi(soalFaali + 1)} از ${Abzar.adadFarsi(soalHaFaali.size)}",
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

        entekhabHa.add(shomarehEntekhab)

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
        if (soalFaali < soalHaFaali.size) {
            soalNeshanBedeh()
        } else {
            natijeNeshanBedeh()
        }
    }

    // صفحهٔ نتیجه: ستاره‌ها + نمره از ۲۰ + بازبینی جواب‌ها + دکمه‌ها
    private fun natijeNeshanBedeh() {
        Zakhire.zakhireKon(this, shomarehZakhire, emtiyaz)
        ghabe.removeAllViews()

        val kol = soalHaFaali.size
        val darsad = if (kol > 0) emtiyaz * 100 / kol else 0
        val nomre = Math.round(emtiyaz * 20.0 / kol).toInt()

        // ستاره‌ها
        val setareHa = when {
            darsad >= 90 -> "★ ★ ★"
            darsad >= 50 -> "★ ★ ☆"
            darsad > 0 -> "★ ☆ ☆"
            else -> "☆ ☆ ☆"
        }
        val matnSetare = Abzar.tv(this, setareHa, 40f, Abzar.RANG_TALAEI)
        matnSetare.gravity = Gravity.CENTER
        val pSetare = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pSetare.topMargin = Abzar.dp(this, 44f)
        ghabe.addView(matnSetare, pSetare)

        // نمره: تعداد درست‌ها + نمرهٔ ۲۰
        val matnNatije = Abzar.tv(
            this,
            "${Abzar.adadFarsi(emtiyaz)} از ${Abzar.adadFarsi(kol)} درست",
            24f, Abzar.RANG_MATN, bold = true
        )
        matnNatije.gravity = Gravity.CENTER
        val pNatije = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pNatije.topMargin = Abzar.dp(this, 16f)
        ghabe.addView(matnNatije, pNatije)

        val matnNomre = Abzar.tv(
            this,
            "نمرهٔ تو: ${Abzar.adadFarsi(nomre)} از ${Abzar.adadFarsi(20)}",
            17f, Abzar.RANG_TALAEI, bold = true
        )
        matnNomre.gravity = Gravity.CENTER
        val pNomre = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pNomre.topMargin = Abzar.dp(this, 6f)
        ghabe.addView(matnNomre, pNomre)

        // جملهٔ تشویقی
        val taarif = when {
            darsad >= 90 -> "شاهکار کردی! تو یه قهرمان کاتلینی 🏆"
            darsad >= 70 -> "خیلی خوب بود! یه قدم تا کامل شدن"
            darsad >= 50 -> "خوب بود! با یک مرور کامل می‌شه"
            darsad > 0 -> "شروع خوبی بود؛ توضیحات درس را یک بار دیگه بخون"
            else -> "اشکالی نداره! درس را بخون و دوباره امتحان کن — تو می‌تونی!"
        }
        val matnTaarif = Abzar.tv(this, taarif, 15f, Abzar.RANG_KAMRANG)
        matnTaarif.gravity = Gravity.CENTER
        val pTaarif = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pTaarif.topMargin = Abzar.dp(this, 12f)
        ghabe.addView(matnTaarif, pTaarif)

        // بازبینی: کدوم درست و کدوم غلط بود؟
        val kartBazbini = LinearLayout(this)
        kartBazbini.orientation = LinearLayout.VERTICAL
        kartBazbini.background = Abzar.kart(this)
        kartBazbini.setPadding(
            Abzar.dp(this, 16f), Abzar.dp(this, 14f),
            Abzar.dp(this, 16f), Abzar.dp(this, 14f)
        )

        val sarBazbini = Abzar.tv(this, "بازبینی جواب‌ها", 14f, Abzar.RANG_TALAEI, bold = true)
        kartBazbini.addView(sarBazbini)

        for (i in soalHaFaali.indices) {
            val soal = soalHaFaali[i]
            val entekhab = entekhabHa.getOrNull(i) ?: -1
            val sahihBood = entekhab == soal.shomarehSahih

            val khat = Abzar.tv(
                this,
                (if (sahihBood) "✅" else "❌") + " سؤال ${Abzar.adadFarsi(i + 1)}: ${soal.matn.replace("\n", " ")}",
                13f,
                if (sahihBood) Abzar.RANG_SABZ else Abzar.RANG_GHERMEZ
            )
            khat.setPadding(0, Abzar.dp(this, 8f), 0, 0)
            kartBazbini.addView(khat)

            if (!sahihBood) {
                val javabSahih = soal.gozinehHa[soal.shomarehSahih]
                val khatSahih = Abzar.tv(
                    this,
                    "جواب درست: $javabSahih",
                    13f, Abzar.RANG_KAMRANG
                )
                khatSahih.setPadding(0, Abzar.dp(this, 2f), 0, 0)
                kartBazbini.addView(khatSahih)
            }
        }

        val pBazbini = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pBazbini.topMargin = Abzar.dp(this, 20f)
        ghabe.addView(kartBazbini, pBazbini)

        // بهترین نتیجهٔ ذخیره‌شده
        val behtarin = Zakhire.behtarin(this, shomarehZakhire)
        val matnBehtarin = Abzar.tv(
            this,
            "بهترین نتیجه: ${Abzar.adadFarsi(behtarin)} از ${Abzar.adadFarsi(soalHaFaali.size)}",
            13f, Abzar.RANG_KAMRANG
        )
        matnBehtarin.gravity = Gravity.CENTER
        val pBehtarin = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pBehtarin.topMargin = Abzar.dp(this, 14f)
        ghabe.addView(matnBehtarin, pBehtarin)

        // دوباره
        val dokmeDobare = Abzar.dokme(this, "دوباره", Abzar.RANG_TIZ, Abzar.RANG_TIZ, khateDor = true)
        dokmeDobare.setOnClickListener {
            soalFaali = 0
            emtiyaz = 0
            entekhabHa.clear()
            soalNeshanBedeh()
        }
        val pDobare = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pDobare.topMargin = Abzar.dp(this, 24f)
        ghabe.addView(dokmeDobare, pDobare)

        // درس بعدی یا برگشتن به فهرست
        if (!azmoonJame && dars.shomareh < DarsHa.darsList.size) {
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
