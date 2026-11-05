package ir.emaroid.hesab

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView

/*
 * اسپلش ماشین‌حساب EmaRoid — نشان E با رنگ فیروزه‌ای
 */
class SplashActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Abzar.rangVaredKon(this)

        val root = FrameLayout(this)
        root.layoutDirection = if (Zaban.rastChin(this)) View.LAYOUT_DIRECTION_RTL else View.LAYOUT_DIRECTION_LTR
        root.background = NagshZamin(Abzar.RANG_SHURU, Abzar.RANG_PAYAN)

        val vasat = LinearLayout(this)
        vasat.orientation = LinearLayout.VERTICAL
        vasat.gravity = Gravity.CENTER_HORIZONTAL
        root.addView(vasat, FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER))

        val ghabeMark = FrameLayout(this)
        val mark = View(this)
        mark.background = Abzar.kart(this, Abzar.RANG_TIZ, 18f)
        ghabeMark.addView(mark, FrameLayout.LayoutParams(
            Abzar.dp(this, 78f), Abzar.dp(this, 78f), Gravity.CENTER))
        val harf = TextView(this)
        harf.text = "🧮"
        harf.setTextSize(TypedValue.COMPLEX_UNIT_SP, 36f)
        harf.gravity = Gravity.CENTER
        ghabeMark.addView(harf, FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER))
        vasat.addView(ghabeMark, LinearLayout.LayoutParams(Abzar.dp(this, 120f), Abzar.dp(this, 120f)))

        val onvan = Abzar.tv(this, Zaban.t("onvan_app"), 26f, Abzar.RANG_MATN)
        onvan.typeface = FontBox.vazirBlack(this)
        val pO = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        pO.topMargin = Abzar.dp(this, 24f)
        vasat.addView(onvan, pO)

        val zir = Abzar.tv(this, Zaban.t("zir_onvan"), 13f, Abzar.RANG_KAMRANG)
        val pZ = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        pZ.topMargin = Abzar.dp(this, 6f)
        vasat.addView(zir, pZ)

        val brand = Abzar.tv(this, "by EmaRoid", 12f, Abzar.RANG_TIZ)
        val pB = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        pB.topMargin = Abzar.dp(this, 6f)
        vasat.addView(brand, pB)

        setContentView(root)

        // آمارگیر بی‌صدای EmaRoid — فقط مجوز INTERNET
        AmarSanj.bego(applicationContext)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }, 1100)
    }
}
