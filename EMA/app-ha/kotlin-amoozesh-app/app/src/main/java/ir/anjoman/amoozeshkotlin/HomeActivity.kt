package ir.anjoman.amoozeshkotlin

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

/*
 * صفحهٔ اصلی: سرصفحه، دکمهٔ منو، دکمهٔ زبان، کارت ماجرا،
 * نوار پیشرفت کل، کارت هر درس و بنر تبلیغ در ته صفحه
 */
class HomeActivity : Activity() {

    private lateinit var ghabe: LinearLayout
    private lateinit var ghabePaein: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Abzar.rangVaredKon(this)

        AmarSanj.sabt(applicationContext, "home_open")
        RuzResani.barrasi(this)

        val root = LinearLayout(this)
        root.orientation = LinearLayout.VERTICAL
        root.layoutDirection =
            if (Zaban.rastChin(this)) View.LAYOUT_DIRECTION_RTL else View.LAYOUT_DIRECTION_LTR
        root.background = Abzar.nagsh(this)

        val scroll = ScrollView(this)
        scroll.isFillViewport = true
        root.addView(
            scroll, LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f
            )
        )

        ghabe = LinearLayout(this)
        ghabe.orientation = LinearLayout.VERTICAL
        ghabe.setPadding(Abzar.dp(this, 20f), Abzar.dp(this, 24f), Abzar.dp(this, 20f), Abzar.dp(this, 10f))
        scroll.addView(
            ghabe, ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )

        // ناحیهٔ پایین برای بنر تبلیغ
        ghabePaein = LinearLayout(this)
        ghabePaein.orientation = LinearLayout.VERTICAL
        ghabePaein.setPadding(Abzar.dp(this, 14f), 0, Abzar.dp(this, 14f), Abzar.dp(this, 12f))
        root.addView(
            ghabePaein, LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
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
        val z = Zaban.bekhon(this)
        val fehrest = Tarjomeh.fehrest(z)
        ghabe.removeAllViews()

        // سرصفحه + دکمهٔ منو + دکمهٔ زبان
        val sar = LinearLayout(this)
        sar.orientation = LinearLayout.HORIZONTAL
        sar.gravity = Gravity.CENTER_VERTICAL

        val onvanGhabe = LinearLayout(this)
        onvanGhabe.orientation = LinearLayout.VERTICAL
        onvanGhabe.layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)

        val onvan = Abzar.tv(this, Zaban.t("onvan_app"), 30f, Abzar.RANG_MATN)
        onvan.typeface = FontBox.vazirBlack(this)
        onvanGhabe.addView(onvan)

        val zirOnvan = Abzar.tv(this, Zaban.t("zir_onvan"), 14f, Abzar.RANG_KAMRANG)
        zirOnvan.setPadding(0, Abzar.dp(this, 6f), 0, 0)
        onvanGhabe.addView(zirOnvan)

        sar.addView(onvanGhabe)

        // دکمهٔ منو
        val dokmeMenu = TextView(this)
        dokmeMenu.text = "☰"
        dokmeMenu.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f)
        dokmeMenu.setPadding(Abzar.dp(this, 10f), Abzar.dp(this, 6f), Abzar.dp(this, 6f), Abzar.dp(this, 6f))
        dokmeMenu.background = Abzar.kartKhatei(this, Abzar.RANG_TALAEI, 12f)
        Abzar.lams(dokmeMenu)
        dokmeMenu.setOnClickListener {
            AmarSanj.sabt(applicationContext, "menu_open")
            startActivity(Intent(this, MenuActivity::class.java))
        }
        sar.addView(dokmeMenu)

        // دکمهٔ زبان: چرخش بین سه زبان با یک لمس
        val dokmeZaban = TextView(this)
        dokmeZaban.text = "🌐"
        dokmeZaban.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26f)
        dokmeZaban.setPadding(Abzar.dp(this, 10f), Abzar.dp(this, 6f), Abzar.dp(this, 10f), Abzar.dp(this, 6f))
        dokmeZaban.background = Abzar.kartKhatei(this, Abzar.RANG_TIZ, 12f)
        Abzar.lams(dokmeZaban)
        dokmeZaban.setOnClickListener { goftogoZaban() }
        sar.addView(dokmeZaban)
        ghabe.addView(sar)

        // کارت ماجرای کدنورد
        val pasDastan = Dastan.pasShode(this)
        val kartDastan = LinearLayout(this)
        kartDastan.orientation = LinearLayout.HORIZONTAL
        kartDastan.gravity = Gravity.CENTER_VERTICAL
        kartDastan.background = Abzar.kart(this, Abzar.RANG_CODE, 16f)
        kartDastan.setPadding(
            Abzar.dp(this, 16f), Abzar.dp(this, 14f),
            Abzar.dp(this, 16f), Abzar.dp(this, 14f)
        )
        kartDastan.isClickable = true
        Abzar.lams(kartDastan)
        kartDastan.setOnClickListener {
            startActivity(Intent(this, DastanActivity::class.java))
        }

        val roket = Abzar.tv(this, "🚀", 30f)
        kartDastan.addView(roket)

        val sotoonDastan = LinearLayout(this)
        sotoonDastan.orientation = LinearLayout.VERTICAL
        val onvanDastan = Abzar.tv(this, Zaban.t("dastan_onvan"), 16f, Abzar.RANG_TALAEI, bold = true)
        sotoonDastan.addView(onvanDastan)
        val matnDastan = Abzar.tv(
            this,
            "${Zaban.t("dastan_episod")} ${Zaban.adad(pasDastan)} ${Zaban.t("az")} ${Zaban.adad(Dastan.episodHa.size)}",
            13f, Abzar.RANG_KAMRANG
        )
        matnDastan.setPadding(0, Abzar.dp(this, 3f), 0, 0)
        sotoonDastan.addView(matnDastan)
        val pSotoonDastan = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
        pSotoonDastan.marginStart = Abzar.dp(this, 14f)
        kartDastan.addView(sotoonDastan, pSotoonDastan)

        val flashDastan = Abzar.tv(this, "‹", 22f, Abzar.RANG_KAMRANG)
        kartDastan.addView(flashDastan)

        val pDastan = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pDastan.topMargin = Abzar.dp(this, 18f)
        ghabe.addView(kartDastan, pDastan)

        // کارت پیشرفت کل — هدف = جمع سؤال‌های همهٔ درس‌ها
        val kartPishraft = LinearLayout(this)
        kartPishraft.orientation = LinearLayout.VERTICAL
        kartPishraft.background = Abzar.kart(this)
        kartPishraft.setPadding(
            Abzar.dp(this, 16f), Abzar.dp(this, 16f),
            Abzar.dp(this, 16f), Abzar.dp(this, 16f)
        )

        val majmoo = Zakhire.majmoo(this)
        val hadaf = fehrest.sumOf { it.soalHa.size }
        val matnPishraft = Abzar.tv(
            this,
            "${Zaban.t("pishraft_kol")}: ${Zaban.adad(majmoo)} ${Zaban.t("az")} ${Zaban.adad(hadaf)}",
            14f, Abzar.RANG_KAMRANG
        )
        kartPishraft.addView(matnPishraft)

        // نوار پیشرفت دست‌ساز: یه نوار خاکستری با پرشدهٔ فیروزه‌ای
        val nobat = LinearLayout(this)
        nobat.background = Abzar.kart(this, Abzar.RANG_KHAKISTARI, 6f)
        val pishrafte = View(this)
        pishrafte.background = Abzar.kart(this, Abzar.RANG_TIZ, 6f)
        nobat.addView(pishrafte, LinearLayout.LayoutParams(0, Abzar.dp(this, 10f), majmoo.toFloat().coerceAtLeast(0.001f)))
        val khali = View(this)
        nobat.addView(khali, LinearLayout.LayoutParams(0, Abzar.dp(this, 10f), (hadaf - majmoo).toFloat().coerceAtLeast(0.001f)))

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
        val matnBakhsh = Abzar.tv(this, Zaban.t("dars_ha"), 16f, Abzar.RANG_TALAEI, bold = true)
        val pBakhsh = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pBakhsh.topMargin = Abzar.dp(this, 24f)
        ghabe.addView(matnBakhsh, pBakhsh)

        // یه کارت برای هر درس
        for (dars in fehrest) {
            val pDars = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            pDars.topMargin = Abzar.dp(this, 12f)
            ghabe.addView(kartDars(dars), pDars)
        }

        // دکمهٔ درباره در ته صفحه
        val dokmeDarbare = Abzar.dokme(this, Zaban.t("darbare"), Abzar.RANG_TIZ, Abzar.RANG_TIZ, khateDor = true)
        dokmeDarbare.setOnClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
        }
        val pDarbare = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pDarbare.topMargin = Abzar.dp(this, 28f)
        ghabe.addView(dokmeDarbare, pDarbare)

        // بنر تبلیغ در ته صفحه
        ghabePaein.removeAllViews()
        if (EmaRoidConfig.TABLIGH_MODE == "khane") {
            val ghabeBanner = FrameLayout(this)
            ghabePaein.addView(
                ghabeBanner, LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, Abzar.dp(this, 72f)
                )
            )
            TablighYar.noghteTapsellBanner(this, ghabeBanner)
        }
    }

    // گفت‌وگوی انتخاب زبان با پرچم‌ها
    private fun goftogoZaban() {
        val g = LinearLayout(this)
        g.orientation = LinearLayout.VERTICAL
        val pad = Abzar.dp(this, 20f)
        g.setPadding(pad, pad, pad, pad)

        val onvan = Abzar.tv(this, Zaban.t("entekhab_zaban"), 18f, Abzar.RANG_MATN, bold = true)
        g.addView(onvan)

        val gozineha = listOf(
            Zaban.Z.FA to ParCham.IRAN,
            Zaban.Z.EN to ParCham.IRELAND,      // انگلیسی با پرچم ایرلند
            Zaban.Z.AR to ParCham.PALESTIN      // عربی با پرچم فلسطین
        )
        for ((z, cham) in gozineha) {
            val raft = LinearLayout(this)
            raft.orientation = LinearLayout.HORIZONTAL
            raft.gravity = Gravity.CENTER_VERTICAL
            raft.isClickable = true
            Abzar.lams(raft)
            raft.setPadding(0, Abzar.dp(this, 12f), 0, Abzar.dp(this, 12f))
            raft.addView(ParCham.neshan(this, cham, 30f))
            val nam = Abzar.tv(this, Zaban.namZaban(z), 16f, Abzar.RANG_MATN)
            val pNam = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
            pNam.marginStart = Abzar.dp(this, 14f)
            raft.addView(nam, pNam)
            if (Zaban.bekhon(this) == z) {
                raft.addView(Abzat_tvTik())
            }
            raft.setOnClickListener {
                AmarSanj.sabt(applicationContext, "zaban_changed", org.json.JSONObject().put("z", z.name))
                Zaban.zakhireKon(this, z)
                recreate()
            }
            g.addView(raft)
        }

        AlertDialog.Builder(this)
            .setView(g)
            .setPositiveButton(Zaban.t("tamam"), null)
            .show()
    }

    private fun Abzat_tvTik(): TextView {
        return Abzar.tv(this, "✓", 16f, Abzar.RANG_SABZ, bold = true)
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
            AmarSanj.sabt(applicationContext, "lesson_open", org.json.JSONObject().put("d", dars.shomareh))
            val niat = Intent(this, LessonActivity::class.java)
            niat.putExtra("shomareh_dars", dars.shomareh)
            startActivity(niat)
        }

        // رنگ دایره بسته به نمرهٔ آزمون عوض می‌شه
        val behtarin = Zakhire.behtarin(this, dars.shomareh)
        val kol = dars.soalHa.size
        val rangDayereh = when {
            behtarin >= kol -> Abzar.RANG_SABZ
            behtarin > 0 -> Abzar.RANG_TALAEI
            else -> Abzar.RANG_KHAKISTARI
        }
        val dayereh = TextView(this)
        dayereh.text = Zaban.adad(dars.shomareh)
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
            "${Zaban.adad(behtarin)} ${Zaban.t("az")} ${Zaban.adad(kol)} ${Zaban.t("sahih")}"
        } else {
            Zaban.t("hanuz_azmoon")
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
