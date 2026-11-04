package ir.emaroid.hesab

import android.app.Activity
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView

/*
 * درباره — برند EmaRoid
 */
class AboutActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Abzar.rangVaredKon(this)

        val root = FrameLayout(this)
        root.layoutDirection = View.LAYOUT_DIRECTION_RTL
        root.background = NagshZamin(0xFF101A33.toInt(), 0xFF0B1220.toInt())

        val vasat = LinearLayout(this)
        vasat.orientation = LinearLayout.VERTICAL
        vasat.gravity = Gravity.CENTER
        vasat.setPadding(Abzar.dp(this, 28f), Abzar.dp(this, 24f), Abzar.dp(this, 28f), Abzar.dp(this, 24f))
        root.addView(vasat, FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER))

        val kart = LinearLayout(this)
        kart.orientation = LinearLayout.VERTICAL
        kart.gravity = Gravity.CENTER_HORIZONTAL
        kart.background = Abzar.kart(this)
        kart.setPadding(Abzar.dp(this, 24f), Abzar.dp(this, 28f), Abzar.dp(this, 24f), Abzar.dp(this, 28f))

        val ghabeMark = FrameLayout(this)
        val mark = View(this)
        mark.background = Abzar.kart(this, Abzar.RANG_TIZ, 16f)
        ghabeMark.addView(mark, FrameLayout.LayoutParams(Abzar.dp(this, 64f), Abzar.dp(this, 64f), Gravity.CENTER))
        val harf = TextView(this)
        harf.text = "E"
        harf.setTextColor(Abzar.RANG_ZAMIN)
        harf.typeface = FontBox.vazirBlack(this)
        harf.setTextSize(TypedValue.COMPLEX_UNIT_SP, 34f)
        harf.gravity = Gravity.CENTER
        ghabeMark.addView(harf, FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER))
        kart.addView(ghabeMark, LinearLayout.LayoutParams(Abzar.dp(this, 80f), Abzar.dp(this, 76f)))

        val brand = Abzar.tv(this, EmaRoidConfig.BRAND, 26f, Abzar.RANG_TIZ)
        brand.typeface = FontBox.vazirBlack(this)
        brand.setPadding(0, Abzar.dp(this, 6f), 0, 0)
        kart.addView(brand)

        val tozih = Abzar.tv(this,
            "ماشین‌حساب علمی با نمودار، حل مرحله‌به‌مرحلهٔ معادله،\nکولاتز و اعداد اول؛ ساختهٔ EmaRoid برای بچه‌های پژوهشسرا",
            14f, Abzar.RANG_KAMRANG)
        tozih.gravity = Gravity.CENTER
        tozih.setPadding(0, Abzar.dp(this, 12f), 0, 0)
        kart.addView(tozih)

        try {
            val pm = packageManager.getPackageInfo(packageName, 0)
            val neskh = Abzar.tv(this, "v${pm.versionName}", 12f, Abzar.RANG_KAMRANG)
            neskh.setPadding(0, Abzar.dp(this, 14f), 0, 0)
            kart.addView(neskh)
        } catch (_: Exception) {
        }

        vasat.addView(kart)

        val dokme = Abzar.dokme(this, "بازگشت", Abzar.RANG_TIZ, Abzar.RANG_TIZ, khateDor = true)
        dokme.setOnClickListener { finish() }
        val pD = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        pD.topMargin = Abzar.dp(this, 20f)
        vasat.addView(dokme, pD)

        setContentView(root)
    }
}
