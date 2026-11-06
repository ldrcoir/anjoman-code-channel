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
 * منوی اصلی اپ — همهٔ بخش‌ها یک‌جا
 */
class MenuActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Abzar.rangVaredKon(this)
        setContentView(safheBekon())
    }

    override fun onResume() {
        super.onResume()
        setContentView(safheBekon())
    }

    private fun safheBekon(): View {
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

        val ghabe = LinearLayout(this)
        ghabe.orientation = LinearLayout.VERTICAL
        ghabe.setPadding(Abzar.dp(this, 20f), Abzar.dp(this, 24f), Abzar.dp(this, 20f), Abzar.dp(this, 24f))
        scroll.addView(
            ghabe,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )

        // سرصفحه
        val onvan = Abzar.tv(this, Zaban.t("menu"), 30f, Abzar.RANG_MATN)
        onvan.typeface = FontBox.vazirBlack(this)
        ghabe.addView(onvan)

        val zirOnvan = Abzar.tv(this, Zaban.t("menu_zir"), 14f, Abzar.RANG_KAMRANG)
        zirOnvan.setPadding(0, Abzar.dp(this, 6f), 0, 0)
        ghabe.addView(zirOnvan)

        // وضعیت کلی
        val fehrest = Tarjomeh.fehrest(Zaban.bekhon(this))
        var majmoo = 0
        var hadaf = 0
        for (d in fehrest) {
            val n = Zakhire.behtarin(this, d.shomareh)
            if (n > 0) majmoo += n
            hadaf += d.soalHa.size
        }
        val kartVaziat = LinearLayout(this)
        kartVaziat.orientation = LinearLayout.VERTICAL
        kartVaziat.background = Abzar.kart(this, Abzar.RANG_CODE, 14f)
        kartVaziat.setPadding(Abzar.dp(this, 14f), Abzar.dp(this, 12f), Abzar.dp(this, 14f), Abzar.dp(this, 12f))
        val temIdx = TemHa.temHa.indexOf(TemHa.faeli).coerceAtLeast(0)
        kartVaziat.addView(
            Abzar.tv(
                this,
                "${Zaban.t("vaziat")}: ${Zaban.adad(majmoo)} ${Zaban.t("az")} ${Zaban.adad(hadaf)} ${Zaban.t("soal_sah")} • ${Zaban.t("tem_$temIdx")}",
                13f, Abzar.RANG_TALAEI, bold = true
            )
        )
        val pVaziat = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pVaziat.topMargin = Abzar.dp(this, 16f)
        ghabe.addView(kartVaziat, pVaziat)

        // آیتم‌های منو
        ghabe.addView(
            itemKart("📚", Zaban.t("dars_ha"), "${Zaban.adad(fehrest.size)} ${Zaban.t("dars")} — ${Zaban.t("menu_dars_tozih")}", "dars"),
            pKart()
        )
        ghabe.addView(
            itemKart("⌨", Zaban.t("zamin_bazi"), Zaban.t("zamin_bazi_tozih"), "zamin"),
            pKart()
        )
        ghabe.addView(
            itemKart("✏", Zaban.t("tamrin_titr"), "${Zaban.adad(Zakhire.tamrinAnjamShode(this))} ${Zaban.t("az")} ${Zaban.adad(DarsHa.tamrinHa.size)}", "tamrin"),
            pKart()
        )
        ghabe.addView(
            itemKart("🏅", Zaban.t("azmoon_jame"), Zaban.t("azmoon_jame_tozih"), "jame"),
            pKart()
        )
        ghabe.addView(
            itemKart("🚀", Zaban.t("dastan"), Zaban.t("dastan_tozih"), "dastan"),
            pKart()
        )
        ghabe.addView(
            itemKart("🎨", Zaban.t("tem_titr"), Zaban.t("tem_tozih"), "tem"),
            pKart()
        )
        ghabe.addView(
            itemKart("ℹ", Zaban.t("darbare"), "", "darbare"),
            pKart()
        )

        return root
    }

    private fun pKart(): LinearLayout.LayoutParams {
        val p = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        p.topMargin = Abzar.dp(this, 12f)
        return p
    }

    // کارت یک آیتم منو: آیکن + عنوان + توضیح + فلش
    private fun itemKart(aykoon: String, onvan: String, tozih: String, noo: String): LinearLayout {
        val kart = LinearLayout(this)
        kart.orientation = LinearLayout.HORIZONTAL
        kart.gravity = Gravity.CENTER_VERTICAL
        kart.background = Abzar.kart(this, Abzar.RANG_KART)
        kart.setPadding(
            Abzar.dp(this, 14f), Abzar.dp(this, 16f),
            Abzar.dp(this, 14f), Abzar.dp(this, 16f)
        )
        kart.isClickable = true
        Abzar.lams(kart)
        kart.setOnClickListener { bazKon(noo) }

        // دایرهٔ آیکن
        val dayereh = TextView(this)
        dayereh.text = aykoon
        dayereh.gravity = Gravity.CENTER
        dayereh.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
        dayereh.background = Abzar.dayereh(this, Abzar.RANG_CODE)
        val pDayereh = LinearLayout.LayoutParams(Abzar.dp(this, 50f), Abzar.dp(this, 50f))
        kart.addView(dayereh, pDayereh)

        val sotoon = LinearLayout(this)
        sotoon.orientation = LinearLayout.VERTICAL
        val matnOnvan = Abzar.tv(this, onvan, 17f, Abzar.RANG_MATN, bold = true)
        sotoon.addView(matnOnvan)
        if (tozih.isNotBlank()) {
            val matnTozih = Abzar.tv(this, tozih, 13f, Abzar.RANG_KAMRANG)
            matnTozih.setPadding(0, Abzar.dp(this, 3f), 0, 0)
            sotoon.addView(matnTozih)
        }
        val pSotoon = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
        pSotoon.marginStart = Abzar.dp(this, 14f)
        kart.addView(sotoon, pSotoon)

        val flash = Abzar.tv(this, "‹", 22f, Abzar.RANG_KAMRANG)
        kart.addView(flash)

        return kart
    }

    private fun bazKon(noo: String) {
        when (noo) {
            "dars" -> startActivity(Intent(this, HomeActivity::class.java))
            "zamin" -> startActivity(Intent(this, PlaygroundActivity::class.java))
            "tamrin" -> startActivity(Intent(this, TamrinActivity::class.java))
            "jame" -> {
                AmarSanj.sabt(applicationContext, "quiz_start", org.json.JSONObject().put("jame", true))
                val niat = Intent(this, QuizActivity::class.java)
                niat.putExtra("azmoon_jame", true)
                startActivity(niat)
            }
            "dastan" -> startActivity(Intent(this, DastanActivity::class.java))
            "tem" -> startActivity(Intent(this, TemActivity::class.java))
            "darbare" -> startActivity(Intent(this, AboutActivity::class.java))
        }
    }
}
