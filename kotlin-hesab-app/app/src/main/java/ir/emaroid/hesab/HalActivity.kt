package ir.emaroid.hesab

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

/*
 * حل معادلهٔ مرحله‌به‌مرحله — خطی و درجهٔ دوم
 */
class HalActivity : Activity() {

    private lateinit var voroodi: EditText
    private lateinit var ghabeMarahel: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Abzar.rangVaredKon(this)

        val root = LinearLayout(this)
        root.orientation = LinearLayout.VERTICAL
        root.layoutDirection = View.LAYOUT_DIRECTION_RTL
        root.background = NagshZamin(0xFF101A33.toInt(), 0xFF0B1220.toInt())
        root.setPadding(Abzar.dp(this, 14f), Abzar.dp(this, 10f), Abzar.dp(this, 14f), Abzar.dp(this, 14f))

        val sar = LinearLayout(this)
        val bargasht = Abzar.dokme(this, "‹ خانه", Abzar.RANG_TIZ, Abzar.RANG_TIZ, khateDor = true)
        bargasht.setOnClickListener { finish() }
        sar.addView(bargasht)
        root.addView(sar)

        val onvan = Abzar.tv(this, "معادله را بنویس — مرحله‌به‌مرحله حلش می‌کنیم", 15f, Abzar.RANG_MATN, bold = true)
        val pO = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        pO.topMargin = Abzar.dp(this, 12f)
        root.addView(onvan, pO)

        voroodi = EditText(this)
        voroodi.hint = "مثلاً: 2x+4=10  یا  x^2-5x+6=0"
        voroodi.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 17f)
        voroodi.setTextColor(Abzar.RANG_MATN)
        voroodi.setHintTextColor(Abzar.RANG_KAMRANG)
        voroodi.background = Abzar.kart(this, Abzar.RANG_CODE, 12f)
        voroodi.setPadding(Abzar.dp(this, 14f), Abzar.dp(this, 12f), Abzar.dp(this, 14f), Abzar.dp(this, 12f))
        voroodi.textDirection = View.TEXT_DIRECTION_LTR
        voroodi.setSingleLine(true)
        val pV = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        pV.topMargin = Abzar.dp(this, 10f)
        root.addView(voroodi, pV)

        val dokme = Abzar.dokme(this, "حل کن 🪜", Abzar.RANG_SABZ, Abzar.RANG_ZAMIN)
        dokme.setOnClickListener { halKon() }
        val pD = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        pD.topMargin = Abzar.dp(this, 12f)
        root.addView(dokme, pD)

        val scroll = ScrollView(this)
        scroll.isFillViewport = true
        val pS = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f)
        pS.topMargin = Abzar.dp(this, 12f)
        root.addView(scroll, pS)

        ghabeMarahel = LinearLayout(this)
        ghabeMarahel.orientation = LinearLayout.VERTICAL
        scroll.addView(ghabeMarahel, ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))

        setContentView(root)

        // نمونهٔ پیش‌فرض
        voroodi.setText("x^2-5x+6=0")
    }

    private fun halKon() {
        ghabeMarahel.removeAllViews()
        try {
            val hal = HalKon.halKon(voroodi.text.toString())
            // کارت جواب
            val kartJavab = LinearLayout(this)
            kartJavab.orientation = LinearLayout.VERTICAL
            kartJavab.background = Abzar.kart(this, 0xFF143024.toInt(), 14f)
            kartJavab.setPadding(Abzar.dp(this, 16f), Abzar.dp(this, 14f), Abzar.dp(this, 16f), Abzar.dp(this, 14f))
            for (j in hal.javabHa) {
                val t = Abzar.tv(this, j, 20f, Abzar.RANG_SABZ, bold = true)
                kartJavab.addView(t)
            }
            val pK = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            ghabeMarahel.addView(kartJavab, pK)

            // مراحل
            for (m in hal.marahel) {
                val kart = TextView(this)
                kart.text = m
                kart.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 15f)
                kart.setTextColor(Abzar.RANG_MATN)
                kart.typeface = FontBox.vazir(this)
                kart.setLineSpacing(Abzar.dp(this, 4f).toFloat(), 1f)
                kart.background = Abzar.kart(this)
                kart.setPadding(Abzar.dp(this, 14f), Abzar.dp(this, 12f), Abzar.dp(this, 14f), Abzar.dp(this, 12f))
                val pM = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                pM.topMargin = Abzar.dp(this, 8f)
                ghabeMarahel.addView(kart, pM)
            }
        } catch (e: Exception) {
            val t = Abzar.tv(this, "⚠️ ${e.message ?: "خطا"}", 15f, Abzar.RANG_GHERMEZ, bold = true)
            ghabeMarahel.addView(t, LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        }
    }
}
