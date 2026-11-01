package ir.ehsanmorad.pajoohesh

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
 * فهرست مطالب — ۱۱ بخش کتاب با شمارهٔ صفحه؛ تم‌ها هم از همین‌جا
 */
class FehrestActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TemEnekhab.rangVaredKon(this)
        val tem = TemEnekhab.bekhon(this)

        val rishe = LinearLayout(this)
        rishe.layoutDirection = View.LAYOUT_DIRECTION_RTL
        rishe.orientation = LinearLayout.VERTICAL
        rishe.setBackgroundColor(tem.zamin)

        // سربرگ
        val sarBar = LinearLayout(this)
        sarBar.orientation = LinearLayout.HORIZONTAL
        sarBar.gravity = Gravity.CENTER_VERTICAL
        sarBar.setPadding(Abzar.dp(this, 18f), Abzar.dp(this, 18f), Abzar.dp(this, 18f), Abzar.dp(this, 6f))
        val onvan = Abzar.tv(this, "📖 فهرست مطالب", 24f, tem.matn)
        onvan.typeface = Fontha.vazirSiaah(this)
        sarBar.addView(onvan, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))

        val dokmeTem = Abzar.tv(this, "🎨 تم‌ها", 14f, tem.asli, bold = true)
        dokmeTem.setPadding(Abzar.dp(this, 14f), Abzar.dp(this, 8f), Abzar.dp(this, 14f), Abzar.dp(this, 8f))
        dokmeTem.background = Abzar.kartKhatei(this, tem.asli, 0, 14f)
        dokmeTem.gravity = Gravity.CENTER
        Abzar.lams(dokmeTem)
        dokmeTem.setOnClickListener { startActivity(Intent(this, TemHaActivity::class.java)) }
        sarBar.addView(dokmeTem)
        rishe.addView(sarBar)
        rishe.addView(Abzar.nobarZiba(this, tem.asli, tem.dovom, 3))

        val lafand = ScrollView(this)
        lafand.isFillViewport = true
        val ghabe = LinearLayout(this)
        ghabe.orientation = LinearLayout.VERTICAL
        ghabe.setPadding(Abzar.dp(this, 18f), Abzar.dp(this, 12f), Abzar.dp(this, 18f), Abzar.dp(this, 26f))
        lafand.addView(ghabe)
        rishe.addView(lafand)

        // کارت ادامهٔ خواندن
        val safheAkhar = Ketab.safheAkharBekhon(this)
        if (safheAkhar > 1) {
            val kartEdame = LinearLayout(this)
            kartEdame.orientation = LinearLayout.VERTICAL
            kartEdame.background = Abzar.kartGradient(this, tem.asli, tem.dovom, 22f)
            kartEdame.setPadding(Abzar.dp(this, 18f), Abzar.dp(this, 16f), Abzar.dp(this, 18f), Abzar.dp(this, 16f))
            kartEdame.isClickable = true
            Abzar.lams(kartEdame)
            val sarKh = Abzar.tv(this, "📍 ادامهٔ خواندن", 14f, 0xB3FFFFFF.toInt(), bold = true)
            kartEdame.addView(sarKh)
            val megdar = Abzar.tv(
                this,
                "صفحهٔ ${Abzar.adadFarsi(safheAkhar + 1)} — ${Ketab.esmeBakhsh(safheAkhar)}",
                18f, 0xFFFFFFFF.toInt(), bold = true
            )
            megdar.setPadding(0, Abzar.dp(this, 6f), 0, 0)
            kartEdame.addView(megdar)
            kartEdame.setOnClickListener {
                startActivity(Intent(this, KhavanakActivity::class.java).putExtra("safhe", safheAkhar))
            }
            ghabe.addView(kartEdame, safP(10))
            kartEdame.alpha = 0f
            kartEdame.animate().alpha(1f).setDuration(320).start()
        }

        // کارت جست‌وجو
        val jostojo = LinearLayout(this)
        jostojo.orientation = LinearLayout.HORIZONTAL
        jostojo.gravity = Gravity.CENTER_VERTICAL
        jostojo.background = Abzar.kartKhatei(this, tem.asli, tem.kart, 20f)
        jostojo.setPadding(Abzar.dp(this, 16f), Abzar.dp(this, 14f), Abzar.dp(this, 16f), Abzar.dp(this, 14f))
        jostojo.isClickable = true
        Abzar.lams(jostojo)
        val neshaneJo = Abzar.tv(this, "🔍", 22f, tem.asli)
        jostojo.addView(neshaneJo)
        val sotoonJo = LinearLayout(this)
        sotoonJo.orientation = LinearLayout.VERTICAL
        sotoonJo.setPadding(Abzar.dp(this, 10f), 0, 0, 0)
        sotoonJo.addView(Abzar.tv(this, "جست‌وجو در کل کتاب", 15f, tem.matn, bold = true))
        sotoonJo.addView(Abzar.tv(this, "نام دانش‌آموز، استان، مدرسه یا موضوع", 11.5f, tem.kamrang))
        jostojo.addView(sotoonJo, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))
        jostojo.setOnClickListener { startActivity(Intent(this, JustoJoActivity::class.java)) }
        ghabe.addView(jostojo, safP(10))

        // بخش‌ها
        for ((i, b) in Ketab.bakhshHa.withIndex()) {
            ghabe.addView(radeBakhsh(b, i, tem), safP(10))
        }

        setContentView(rishe)
    }

    private fun radeBakhsh(b: Ketab.Bakhsh, shomare: Int, tem: Tem): View {
        val rade = LinearLayout(this)
        rade.orientation = LinearLayout.HORIZONTAL
        rade.gravity = Gravity.CENTER_VERTICAL
        rade.background = Abzar.kart(this, tem.kart, 18f)
        rade.setPadding(Abzar.dp(this, 14f), Abzar.dp(this, 13f), Abzar.dp(this, 14f), Abzar.dp(this, 13f))
        rade.isClickable = true
        Abzar.lams(rade)

        val dayereNeshane = TextView(this)
        dayereNeshane.text = b.neshane
        dayereNeshane.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17f)
        dayereNeshane.gravity = Gravity.CENTER
        dayereNeshane.background = Abzar.kartGradient(this, tem.asli, tem.dovom, 999f)
        rade.addView(dayereNeshane, LinearLayout.LayoutParams(Abzar.dp(this, 42f), Abzar.dp(this, 42f)))

        val sotoonMatn = LinearLayout(this)
        sotoonMatn.orientation = LinearLayout.VERTICAL
        val pS = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
        pS.marginStart = Abzar.dp(this, 12f)
        val matnOnvan = Abzar.tv(this, b.esm, 15.5f, tem.matn, bold = true)
        sotoonMatn.addView(matnOnvan)
        val matnTozih = Abzar.tv(this, b.tozih, 11.5f, tem.kamrang)
        matnTozih.setPadding(0, Abzar.dp(this, 3f), 0, 0)
        sotoonMatn.addView(matnTozih)
        rade.addView(sotoonMatn, pS)

        val mohr = Abzar.tv(this, "ص ${Abzar.adadFarsi(b.safhe + 1)}", 12f, tem.asli, bold = true)
        mohr.background = Abzar.kartKhatei(this, tem.asli, 0, 10f)
        mohr.setPadding(Abzar.dp(this, 8f), Abzar.dp(this, 4f), Abzar.dp(this, 8f), Abzar.dp(this, 4f))
        rade.addView(mohr)

        rade.setOnClickListener {
            startActivity(Intent(this, KhavanakActivity::class.java).putExtra("safhe", b.safhe))
        }
        return rade
    }

    private fun safP(t: Int): LinearLayout.LayoutParams {
        val p = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        p.topMargin = Abzar.dp(this, t.toFloat())
        return p
    }
}
