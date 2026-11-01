package ir.anjoman.amoozeshkotlin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

/*
 * صفحهٔ اصلی نسخهٔ ۳: سرصفحه با دکمهٔ منو، کارت پیشرفت، میان‌بُرها
 * و درس‌ها مرتب‌شده داخل فصل‌ها
 * چون نمره‌ها ممکنه عوض بشن، هر بار برگردیم صفحه از نو ساخته می‌شه
 */
class HomeActivity : Activity() {

    private lateinit var ghabe: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Abzar.rangVaredKon(this)

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
    }

    // هر بار که برمی‌گردیم، نمره‌های تازه خونده می‌شن
    override fun onResume() {
        super.onResume()
        safheBekon()
    }

    private fun safheBekon() {
        ghabe.removeAllViews()

        // ---------- سرصفحه با دکمهٔ منو ----------
        val sar = LinearLayout(this)
        sar.orientation = LinearLayout.HORIZONTAL
        sar.gravity = Gravity.CENTER_VERTICAL

        val sotoonOnvan = LinearLayout(this)
        sotoonOnvan.orientation = LinearLayout.VERTICAL
        val onvan = Abzar.tv(this, "آموزش کاتلین", 30f, Abzar.RANG_MATN)
        onvan.typeface = FontBox.vazirBlack(this)
        sotoonOnvan.addView(onvan)
        val zirOnvan = Abzar.tv(this, "از مقدمه تا پیشرفته — ۲۵ درس کامل", 14f, Abzar.RANG_KAMRANG)
        zirOnvan.setPadding(0, Abzar.dp(this, 6f), 0, 0)
        sotoonOnvan.addView(zirOnvan)
        sar.addView(sotoonOnvan, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))

        val dokmeMenu = TextView(this)
        dokmeMenu.text = "☰"
        dokmeMenu.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26f)
        dokmeMenu.setTextColor(Abzar.RANG_TIZ)
        dokmeMenu.typeface = FontBox.vazirBold(this)
        dokmeMenu.gravity = Gravity.CENTER
        dokmeMenu.background = Abzar.kart(this, Abzar.RANG_KART)
        dokmeMenu.isClickable = true
        Abzar.lams(dokmeMenu)
        dokmeMenu.setOnClickListener { startActivity(Intent(this, MenuActivity::class.java)) }
        val pMenu = LinearLayout.LayoutParams(Abzar.dp(this, 52f), Abzar.dp(this, 52f))
        pMenu.marginStart = Abzar.dp(this, 12f)
        sar.addView(dokmeMenu, pMenu)

        ghabe.addView(sar)

        // ---------- کارت پیشرفت کل ----------
        val kartPishraft = LinearLayout(this)
        kartPishraft.orientation = LinearLayout.VERTICAL
        kartPishraft.background = Abzar.kart(this)
        kartPishraft.setPadding(
            Abzar.dp(this, 16f), Abzar.dp(this, 16f),
            Abzar.dp(this, 16f), Abzar.dp(this, 16f)
        )

        var majmoo = 0
        var hadaf = 0
        var darsTam = 0
        for (d in DarsHa.darsList) {
            val n = Zakhire.behtarin(this, d.shomareh)
            if (n > 0) majmoo += n
            hadaf += d.soalHa.size
            if (n >= d.soalHa.size) darsTam++
        }
        val matnPishraft = Abzar.tv(
            this,
            "پیشرفت کل: ${Abzar.adadFarsi(majmoo)} از ${Abzar.adadFarsi(hadaf)} سؤال — ${Abzar.adadFarsi(darsTam)} درس کامل",
            14f, Abzar.RANG_KAMRANG
        )
        kartPishraft.addView(matnPishraft)

        val nobat = LinearLayout(this)
        nobat.background = Abzar.kart(this, Abzar.RANG_KHAKISTARI, 6f)
        val pishrafte = View(this)
        pishrafte.background = Abzar.kart(this, Abzar.RANG_TIZ, 6f)
        nobat.addView(pishrafte, LinearLayout.LayoutParams(0, Abzar.dp(this, 10f), majmoo.toFloat()))
        val khali = View(this)
        nobat.addView(khali, LinearLayout.LayoutParams(0, Abzar.dp(this, 10f), (hadaf - majmoo).toFloat()))

        val pNobat = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pNobat.topMargin = Abzar.dp(this, 12f)
        kartPishraft.addView(nobat, pNobat)

        val pKart = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pKart.topMargin = Abzar.dp(this, 20f)
        ghabe.addView(kartPishraft, pKart)

        // ---------- منوی میان‌بُر (۴ کارت دوتایی) ----------
        val matnMenu = Abzar.tv(this, "منوی سریع", 16f, Abzar.RANG_TALAEI, bold = true)
        val pMenuSari = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pMenuSari.topMargin = Abzar.dp(this, 24f)
        ghabe.addView(matnMenu, pMenuSari)

        ghabe.addView(safDota("⌨  زمین بازی کد", "کد بنویس و اجرا کن", zaminKlik()),
            pDota())
        ghabe.addView(safDota("✏  تمرین‌های عملی", "${Abzar.adadFarsi(Zakhire.tamrinAnjamShode(this))} از ${Abzar.adadFarsi(DarsHa.tamrinHa.size)} حل شده", tamrinKlik()),
            pDota())
        ghabe.addView(safDota("🏅  آزمون جامع", "سؤال از همهٔ درس‌ها", jameKlik()),
            pDota())
        ghabe.addView(safDota("🎨  تم و ظاهر", TemHa.faeli.esm, temKlik()),
            pDota())

        // ---------- فصل‌ها و درس‌ها ----------
        val matnBakhsh = Abzar.tv(this, "درس‌ها به تفکیک فصل", 16f, Abzar.RANG_TALAEI, bold = true)
        val pBakhsh = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pBakhsh.topMargin = Abzar.dp(this, 24f)
        ghabe.addView(matnBakhsh, pBakhsh)

        for (fasl in DarsHa.faslHa) {
            // سربرگ فصل
            val kartFasl = LinearLayout(this)
            kartFasl.orientation = LinearLayout.VERTICAL
            kartFasl.background = Abzar.kart(this, Abzar.RANG_CODE, 14f)
            kartFasl.setPadding(
                Abzar.dp(this, 14f), Abzar.dp(this, 12f),
                Abzar.dp(this, 14f), Abzar.dp(this, 12f)
            )
            val matnFasl = Abzar.tv(this, fasl.esm, 15f, Abzar.RANG_TALAEI, bold = true)
            kartFasl.addView(matnFasl)
            val matnTozih = Abzar.tv(this, fasl.tozih, 12f, Abzar.RANG_KAMRANG)
            matnTozih.setPadding(0, Abzar.dp(this, 3f), 0, 0)
            kartFasl.addView(matnTozih)
            val pFasl = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            pFasl.topMargin = Abzar.dp(this, 16f)
            ghabe.addView(kartFasl, pFasl)

            // درس‌های این فصل
            val darsHaFasl = DarsHa.darsList.filter { it.shomareh in fasl.az..fasl.ta }
            for (dars in darsHaFasl) {
                val pDars = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                pDars.topMargin = Abzar.dp(this, 12f)
                ghabe.addView(kartDars(dars), pDars)
            }
        }

        // دکمهٔ درباره در ته صفحه
        val dokmeDarbare = Abzar.dokme(this, "دربارهٔ ما", Abzar.RANG_TIZ, Abzar.RANG_TIZ, khateDor = true)
        dokmeDarbare.setOnClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
        }
        val pDarbare = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pDarbare.topMargin = Abzar.dp(this, 28f)
        ghabe.addView(dokmeDarbare, pDarbare)
    }

    // ---------- کمکی‌های منوی سریع ----------
    private fun pDota(): LinearLayout.LayoutParams {
        val p = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        p.topMargin = Abzar.dp(this, 12f)
        return p
    }

    private fun zaminKlik(): (Activity) -> Unit = {
        it.startActivity(Intent(it, PlaygroundActivity::class.java))
    }

    private fun tamrinKlik(): (Activity) -> Unit = {
        it.startActivity(Intent(it, TamrinActivity::class.java))
    }

    private fun jameKlik(): (Activity) -> Unit = {
        val niat = Intent(it, QuizActivity::class.java)
        niat.putExtra("azmoon_jame", true)
        it.startActivity(niat)
    }

    private fun temKlik(): (Activity) -> Unit = {
        it.startActivity(Intent(it, TemActivity::class.java))
    }

    private fun safDota(onvan: String, tozih: String, klik: (Activity) -> Unit): LinearLayout {
        val kart = LinearLayout(this)
        kart.orientation = LinearLayout.HORIZONTAL
        kart.gravity = Gravity.CENTER_VERTICAL
        kart.background = Abzar.kart(this, Abzar.RANG_KART)
        kart.setPadding(
            Abzar.dp(this, 14f), Abzar.dp(this, 14f),
            Abzar.dp(this, 14f), Abzar.dp(this, 14f)
        )
        kart.isClickable = true
        Abzar.lams(kart)
        kart.setOnClickListener { klik(this) }

        val sotoon = LinearLayout(this)
        sotoon.orientation = LinearLayout.VERTICAL
        val matnOnvan = Abzar.tv(this, onvan, 16f, Abzar.RANG_MATN, bold = true)
        sotoon.addView(matnOnvan)
        val matnTozih = Abzar.tv(this, tozih, 13f, Abzar.RANG_KAMRANG)
        matnTozih.setPadding(0, Abzar.dp(this, 3f), 0, 0)
        sotoon.addView(matnTozih)
        kart.addView(sotoon, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))

        val flash = Abzar.tv(this, "‹", 22f, Abzar.RANG_KAMRANG)
        kart.addView(flash)
        return kart
    }

    // کارت هر درس: دایرهٔ شماره + عنوان + وضعیت آزمون
    private fun kartDars(dars: Dars): LinearLayout {
        val kart = LinearLayout(this)
        kart.orientation = LinearLayout.HORIZONTAL
        kart.gravity = Gravity.CENTER_VERTICAL
        kart.background = Abzar.kart(this)
        kart.setPadding(
            Abzar.dp(this, 14f), Abzar.dp(this, 14f),
            Abzar.dp(this, 14f), Abzar.dp(this, 14f)
        )
        kart.isClickable = true
        Abzar.lams(kart)
        kart.setOnClickListener {
            val niat = Intent(this, LessonActivity::class.java)
            niat.putExtra("shomareh_dars", dars.shomareh)
            startActivity(niat)
        }

        // رنگ دایره بسته به نمرهٔ آزمون عوض می‌شه
        val behtarin = Zakhire.behtarin(this, dars.shomareh)
        val rangDayereh = when {
            behtarin >= dars.soalHa.size -> Abzar.RANG_SABZ
            behtarin > 0 -> Abzar.RANG_TALAEI
            else -> Abzar.RANG_KHAKISTARI
        }
        val dayereh = TextView(this)
        dayereh.text = Abzar.adadFarsi(dars.shomareh)
        dayereh.gravity = Gravity.CENTER
        dayereh.typeface = FontBox.vazirBold(this)
        dayereh.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        dayereh.setTextColor(if (rangDayereh == Abzar.RANG_KHAKISTARI) Abzar.RANG_MATN else Abzar.RANG_ZAMIN)
        dayereh.background = Abzar.dayereh(this, rangDayereh)
        kart.addView(dayereh, LinearLayout.LayoutParams(Abzar.dp(this, 44f), Abzar.dp(this, 44f)))

        // عنوان درس و وضعیت نمره‌ش
        val sotoon = LinearLayout(this)
        sotoon.orientation = LinearLayout.VERTICAL

        val matnDars = Abzar.tv(this, dars.onvan, 16f, Abzar.RANG_MATN, bold = true)
        sotoon.addView(matnDars)

        val halat = if (behtarin >= 0) {
            "${Abzar.adadFarsi(behtarin)} از ${Abzar.adadFarsi(dars.soalHa.size)} درست"
        } else {
            "هنوز آزمون ندادی"
        }
        val matnHalat = Abzar.tv(this, halat, 13f, Abzar.RANG_KAMRANG)
        matnHalat.setPadding(0, Abzar.dp(this, 3f), 0, 0)
        sotoon.addView(matnHalat)

        val pSotoon = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
        pSotoon.marginStart = Abzar.dp(this, 14f)
        kart.addView(sotoon, pSotoon)

        val flash = Abzar.tv(this, "‹", 22f, Abzar.RANG_KAMRANG)
        kart.addView(flash)

        return kart
    }
}
