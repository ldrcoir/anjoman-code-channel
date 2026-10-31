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
 * صفحهٔ اصلی: سرصفحه، نوار پیشرفت کل و کارت هر درس
 * چون نمره‌ها ممکنه عوض بشن، اینجا هر بار برگردیم صفحه از نو ساخته می‌شه
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

        // سرصفحه
        val onvan = Abzar.tv(this, "آموزش کاتلین", 30f, Abzar.RANG_MATN)
        onvan.typeface = FontBox.vazirBlack(this)
        ghabe.addView(onvan)

        val zirOnvan = Abzar.tv(this, "قدم‌به‌قدم کاتلین یاد بگیر", 14f, Abzar.RANG_KAMRANG)
        zirOnvan.setPadding(0, Abzar.dp(this, 6f), 0, 0)
        ghabe.addView(zirOnvan)

        // کارت پیشرفت کل
        val kartPishraft = LinearLayout(this)
        kartPishraft.orientation = LinearLayout.VERTICAL
        kartPishraft.background = Abzar.kart(this)
        kartPishraft.setPadding(
            Abzar.dp(this, 16f), Abzar.dp(this, 16f),
            Abzar.dp(this, 16f), Abzar.dp(this, 16f)
        )

        val majmoo = Zakhire.majmoo(this)
        var hadaf = 0
        for (d in DarsHa.darsList) hadaf += d.soalHa.size
        val matnPishraft = Abzar.tv(
            this,
            "پیشرفت کل: ${Abzar.adadFarsi(majmoo)} از ${Abzar.adadFarsi(hadaf)}",
            14f, Abzar.RANG_KAMRANG
        )
        kartPishraft.addView(matnPishraft)

        // نوار پیشرفت دست‌ساز: یه نوار خاکستری با پرشدهٔ فیروزه‌ای
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

        // عنوان بخش درس‌ها
        val matnBakhsh = Abzar.tv(this, "درس‌ها", 16f, Abzar.RANG_TALAEI, bold = true)
        val pBakhsh = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pBakhsh.topMargin = Abzar.dp(this, 24f)
        ghabe.addView(matnBakhsh, pBakhsh)

        // یه کارت برای هر درس
        for (dars in DarsHa.darsList) {
            val pDars = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            pDars.topMargin = Abzar.dp(this, 12f)
            ghabe.addView(kartDars(dars), pDars)
        }

        // بخش زمین بازی: کدنویسی زنده و تمرین‌ها
        val matnTalar = Abzar.tv(this, "زمین بازی کد", 16f, Abzar.RANG_TALAEI, bold = true)
        val pTalar = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pTalar.topMargin = Abzar.dp(this, 24f)
        ghabe.addView(matnTalar, pTalar)

        val pZamin = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pZamin.topMargin = Abzar.dp(this, 12f)
        ghabe.addView(kartTalar("⌨  زمین بازی کد", "کد بنویس، اجرا کن، خطاها را ببین", "zamin"), pZamin)

        val pTamrin = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pTamrin.topMargin = Abzar.dp(this, 12f)
        val tedadTamrin = Zakhire.tamrinAnjamShode(this)
        ghabe.addView(
            kartTalar(
                "✏  تمرین‌های عملی",
                "${Abzar.adadFarsi(tedadTamrin)} از ${Abzar.adadFarsi(DarsHa.tamrinHa.size)} حل شده",
                "tamrin"
            ),
            pTamrin
        )

        // دکمهٔ درباره در ته صفحه
        val dokmeDarbare = Abzar.dokme(this, "درباره", Abzar.RANG_TIZ, Abzar.RANG_TIZ, khateDor = true)
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

    // کارت‌های زمین بازی: بازکردن Playground یا Tamrin
    private fun kartTalar(onvan: String, tozih: String, noo: String): LinearLayout {
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
        kart.setOnClickListener {
            val niat = Intent(
                this,
                if (noo == "tamrin") TamrinActivity::class.java else PlaygroundActivity::class.java
            )
            startActivity(niat)
        }

        val sotoon = LinearLayout(this)
        sotoon.orientation = LinearLayout.VERTICAL

        val matnOnvan = Abzar.tv(this, onvan, 16f, Abzar.RANG_MATN, bold = true)
        sotoon.addView(matnOnvan)

        val matnTozih = Abzar.tv(this, tozih, 13f, Abzar.RANG_KAMRANG)
        matnTozih.setPadding(0, Abzar.dp(this, 3f), 0, 0)
        sotoon.addView(matnTozih)

        val pSotoon = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
        kart.addView(sotoon, pSotoon)

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

        // فلش کوچیک سمت چپ
        val flash = Abzar.tv(this, "‹", 22f, Abzar.RANG_KAMRANG)
        kart.addView(flash)

        return kart
    }
}
