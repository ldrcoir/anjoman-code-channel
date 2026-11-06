package ir.anjoman.amoozeshkotlin

import android.app.Activity
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

/*
 * صفحهٔ ماجرای کدنورد — فصل‌های داستان با قفل
 * فصلی باز می‌شود که آزمون درس مربوط به آن پاس شده باشد
 */
class DastanActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Abzar.rangVaredKon(this)

        AmarSanj.sabt(applicationContext, "dastan_open")

        val root = LinearLayout(this)
        root.orientation = LinearLayout.VERTICAL
        root.layoutDirection =
            if (Zaban.rastChin(this)) View.LAYOUT_DIRECTION_RTL else View.LAYOUT_DIRECTION_LTR
        root.background = Abzar.nagsh(this)

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

        val onvan = Abzar.tv(this, Zaban.t("dastan_onvan"), 28f, Abzar.RANG_MATN)
        onvan.typeface = FontBox.vazirBlack(this)
        ghabe.addView(onvan)

        val pas = Dastan.pasShode(this)
        val zirOnvan = Abzar.tv(
            this,
            "${Zaban.t("dastan_episod")} ${Zaban.adad(pas)} ${Zaban.t("az")} ${Zaban.adad(Dastan.episodHa.size)}",
            14f, Abzar.RANG_TALAEI, bold = true
        )
        zirOnvan.setPadding(0, Abzar.dp(this, 6f), 0, 0)
        ghabe.addView(zirOnvan)

        val z = Zaban.bekhon(this)
        for (ep in Dastan.episodHa) {
            val pKart = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            pKart.topMargin = Abzar.dp(this, 14f)
            ghabe.addView(kartEpisod(ep, z), pKart)
        }

        val dokmeBargasht = Abzar.dokme(this, Zaban.t("bargasht"), Abzar.RANG_TIZ, Abzar.RANG_TIZ, khateDor = true)
        dokmeBargasht.setOnClickListener { finish() }
        val pBargasht = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pBargasht.topMargin = Abzar.dp(this, 20f)
        ghabe.addView(dokmeBargasht, pBargasht)

        setContentView(root)
    }

    private fun kartEpisod(ep: Dastan.Episod, z: Zaban.Z): LinearLayout {
        val baz = Dastan.bazAst(this, ep)
        val kart = LinearLayout(this)
        kart.orientation = LinearLayout.VERTICAL
        kart.background = Abzar.kart(this, if (baz) Abzar.RANG_KART else Abzar.RANG_KHAKISTARI, 16f)
        kart.setPadding(
            Abzar.dp(this, 16f), Abzar.dp(this, 16f),
            Abzar.dp(this, 16f), Abzar.dp(this, 16f)
        )
        if (baz) {
            kart.isClickable = true
            Abzar.lams(kart)
            kart.setOnClickListener { AmarSanj.sabt(applicationContext, "dastan_read", org.json.JSONObject().put("ep", ep.shomareh)) }
        }

        val sar = LinearLayout(this)
        sar.orientation = LinearLayout.HORIZONTAL
        sar.gravity = Gravity.CENTER_VERTICAL

        val noqte = TextView(this)
        noqte.text = if (baz) "🚀" else "🔒"
        noqte.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
        sar.addView(noqte)

        val esm = Abzar.tv(
            this,
            "${Zaban.t("dastan_episod")} ${Zaban.adad(ep.shomareh)}: ${ep.onvan[z] ?: ep.onvan[Zaban.Z.FA]}",
            16f,
            if (baz) Abzar.RANG_MATN else Abzar.RANG_KAMRANG,
            bold = true
        )
        val pEsm = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
        pEsm.marginStart = Abzar.dp(this, 10f)
        sar.addView(esm, pEsm)
        kart.addView(sar)

        val matn = Abzar.tv(
            this,
            if (baz) (ep.matn[z] ?: ep.matn.getValue(Zaban.Z.FA))
            else "${Zaban.t("dastan_ghofl")} — ${Zaban.t("dastan_bad_az")} ${Zaban.adad(ep.darDars)}",
            14f,
            if (baz) Abzar.RANG_MATN else Abzar.RANG_KAMRANG
        )
        matn.setLineSpacing(Abzar.dp(this, 4f).toFloat(), 1f)
        matn.setPadding(0, Abzar.dp(this, 10f), 0, 0)
        kart.addView(matn)

        return kart
    }
}
