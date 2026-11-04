package ir.emaroid.hesab

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ScrollView

/*
 * خانهٔ ماشین‌حساب EmaRoid — چهار بخش + درباره
 */
class HomeActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Abzar.rangVaredKon(this)

        val root = LinearLayout(this)
        root.orientation = LinearLayout.VERTICAL
        root.layoutDirection = View.LAYOUT_DIRECTION_RTL
        root.background = NagshZamin(0xFF101A33.toInt(), 0xFF0B1220.toInt())

        val scroll = ScrollView(this)
        scroll.isFillViewport = true
        root.addView(scroll, LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f))

        val g = LinearLayout(this)
        g.orientation = LinearLayout.VERTICAL
        g.setPadding(Abzar.dp(this, 20f), Abzar.dp(this, 28f), Abzar.dp(this, 20f), Abzar.dp(this, 20f))
        scroll.addView(g, ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))

        val onvan = Abzar.tv(this, "ماشین‌حساب EmaRoid", 28f, Abzar.RANG_MATN)
        onvan.typeface = FontBox.vazirBlack(this)
        g.addView(onvan)

        val zir = Abzar.tv(this, "نمودار • حل مرحله‌به‌مرحله • کولاتز و اعداد اول", 13f, Abzar.RANG_KAMRANG)
        zir.setPadding(0, Abzar.dp(this, 6f), 0, Abzar.dp(this, 20f))
        g.addView(zir)

        // چهار بخش
        val bakhsha = listOf(
            Triple("🧮", "ماشین‌حساب علمی", "چهارعملگر، توان، فاکتوریل، مثلثاتی" to HesabActivity::class.java),
            Triple("📈", "نمودار تابع", "رسم y = f(x) با جابه‌جایی و زوم" to RasmActivity::class.java),
            Triple("🪜", "حل معادله", "خطی و درجهٔ دوم — مرحله‌به‌مرحله" to HalActivity::class.java),
            Triple("🔢", "ابزار اعداد", "اول، عوامل، ب.م.م، Collatz، مبنا" to AdadActivity::class.java)
        )
        for ((emuz, onv, matnNiat) in bakhsha) {
            val matn = matnNiat.first
            val niat = matnNiat.second
            val kart = LinearLayout(this)
            kart.orientation = LinearLayout.HORIZONTAL
            kart.gravity = Gravity.CENTER_VERTICAL
            kart.background = Abzar.kart(this)
            kart.setPadding(Abzar.dp(this, 16f), Abzar.dp(this, 18f), Abzar.dp(this, 16f), Abzar.dp(this, 18f))
            kart.isClickable = true
            Abzar.lams(kart)
            kart.setOnClickListener { startActivity(Intent(this, niat)) }

            val e = Abzar.tv(this, emuz, 30f)
            kart.addView(e)
            val sotoon = LinearLayout(this)
            sotoon.orientation = LinearLayout.VERTICAL
            val o = Abzar.tv(this, onv, 17f, Abzar.RANG_MATN, bold = true)
            sotoon.addView(o)
            val m = Abzar.tv(this, matn, 12f, Abzar.RANG_KAMRANG)
            m.setPadding(0, Abzar.dp(this, 3f), 0, 0)
            sotoon.addView(m)
            val p = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
            p.marginStart = Abzar.dp(this, 14f)
            kart.addView(sotoon)
            kart.addView(Abzar.tv(this, "‹", 22f, Abzar.RANG_KAMRANG))

            val pK = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            pK.topMargin = Abzar.dp(this, 12f)
            g.addView(kart, pK)
        }

        // درباره
        val dokme = Abzar.dokme(this, "دربارهٔ EmaRoid", Abzar.RANG_TIZ, Abzar.RANG_TIZ, khateDor = true)
        dokme.setOnClickListener { startActivity(Intent(this, AboutActivity::class.java)) }
        val pD = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        pD.topMargin = Abzar.dp(this, 24f)
        g.addView(dokme, pD)

        setContentView(root)
    }
}
