package ir.anjoman.amoozeshrust

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
 * صفحهٔ اصلی: سرصفحه، دکمهٔ زبان، نوار پیشرفت کل، کارت هر درس
 * و بنر تبلیغ EmaRoid در ته صفحه (هیچ تبلیغی وسط درس‌ها نیست)
 */
class HomeActivity : Activity() {

    private lateinit var ghabe: LinearLayout
    private lateinit var ghabePaein: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Abzar.rangVaredKon(this)

        val root = LinearLayout(this)
        root.orientation = LinearLayout.VERTICAL
        root.layoutDirection =
            if (Zaban.rastChin(this)) View.LAYOUT_DIRECTION_RTL else View.LAYOUT_DIRECTION_LTR
        root.background = NagshZamin(0xFF241610.toInt(), 0xFF140D08.toInt())

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
        ghabe.removeAllViews()

        // سرصفحه + دکمهٔ زبان
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

        // دکمهٔ زبان: پرچم فعلی
        val dokmeZaban = TextView(this)
        dokmeZaban.text = "🌐"
        dokmeZaban.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26f)
        dokmeZaban.setPadding(Abzar.dp(this, 10f), Abzar.dp(this, 6f), Abzar.dp(this, 10f), Abzar.dp(this, 6f))
        dokmeZaban.background = Abzar.kartKhatei(this, Abzar.RANG_TIZ, 12f)
        Abzar.lams(dokmeZaban)
        dokmeZaban.setOnClickListener { goftogoZaban() }
        sar.addView(dokmeZaban)
        ghabe.addView(sar)

        // کارت پیشرفت کل
        val kartPishraft = LinearLayout(this)
        kartPishraft.orientation = LinearLayout.VERTICAL
        kartPishraft.background = Abzar.kart(this)
        kartPishraft.setPadding(
            Abzar.dp(this, 16f), Abzar.dp(this, 16f),
            Abzar.dp(this, 16f), Abzar.dp(this, 16f)
        )

        val majmoo = Zakhire.majmoo(this)
        val hadaf = DarsHa.darsList.size * 3
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
        for (dars in DarsHa.darsList) {
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

        // بنر تبلیغ EmaRoid در ته صفحه
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
            Zaban.Z.EN to ParCham.IRELAND,      // انگلیسی با پرچم ایرلند (خواستهٔ سردبیر)
            Zaban.Z.AR to ParCham.PALESTIN      // عربی با پرچم فلسطین (خواستهٔ سردبیر)
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
        val t = Abzar.tv(this, "✓", 16f, Abzar.RANG_SABZ, bold = true)
        return t
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
            behtarin >= 3 -> Abzar.RANG_SABZ
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
            "${Zaban.adad(behtarin)} ${Zaban.t("az")} ${Zaban.adad(3)} ${Zaban.t("sahih")}"
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
