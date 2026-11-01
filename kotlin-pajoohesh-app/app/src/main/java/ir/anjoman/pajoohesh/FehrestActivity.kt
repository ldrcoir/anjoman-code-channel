package ir.anjoman.pajoohesh

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

        val root = LinearLayout(this)
        root.layoutDirection = View.LAYOUT_DIRECTION_RTL
        root.orientation = LinearLayout.VERTICAL
        root.setBackgroundColor(tem.zamin)

        // سربرگ
        val sar = LinearLayout(this)
        sar.orientation = LinearLayout.HORIZONTAL
        sar.gravity = Gravity.CENTER_VERTICAL
        sar.setPadding(Abzar.dp(this, 18f), Abzar.dp(this, 18f), Abzar.dp(this, 18f), Abzar.dp(this, 6f))
        val onvan = Abzar.tv(this, "📖 فهرست مطالب", 24f, tem.matn)
        onvan.typeface = FontBox.vazirBlack(this)
        sar.addView(onvan, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))

        val dokmeTem = Abzar.tv(this, "🎨 تم‌ها", 14f, tem.asli, bold = true)
        dokmeTem.setPadding(Abzar.dp(this, 14f), Abzar.dp(this, 8f), Abzar.dp(this, 14f), Abzar.dp(this, 8f))
        dokmeTem.background = Abzar.kartKhatei(this, tem.asli, 0, 14f)
        dokmeTem.gravity = Gravity.CENTER
        Abzar.lams(dokmeTem)
        dokmeTem.setOnClickListener { startActivity(Intent(this, TemHaActivity::class.java)) }
        sar.addView(dokmeTem)
        root.addView(sar)
        root.addView(Abzar.nobarZiba(this, tem.asli, tem.dovom, 3))

        val scroll = ScrollView(this)
        scroll.isFillViewport = true
        val ghabe = LinearLayout(this)
        ghabe.orientation = LinearLayout.VERTICAL
        ghabe.setPadding(Abzar.dp(this, 18f), Abzar.dp(this, 12f), Abzar.dp(this, 18f), Abzar.dp(this, 26f))
        scroll.addView(ghabe)
        root.addView(scroll)

        // کارت ادامهٔ خواندن
        val safheAkhar = Ketab.safheAkharBekhon(this)
        if (safheAkhar > 1) {
            val kh = LinearLayout(this)
            kh.orientation = LinearLayout.VERTICAL
            kh.background = Abzar.kartGradient(this, tem.asli, tem.dovom, 22f)
            kh.setPadding(Abzar.dp(this, 18f), Abzar.dp(this, 16f), Abzar.dp(this, 18f), Abzar.dp(this, 16f))
            kh.isClickable = true
            Abzar.lams(kh)
            val sarKh = Abzar.tv(this, "📍 ادامهٔ خواندن", 14f, 0xB3FFFFFF.toInt(), bold = true)
            kh.addView(sarKh)
            val megdar = Abzar.tv(
                this,
                "صفحهٔ ${Abzar.adadFarsi(safheAkhar + 1)} — ${Ketab.esmeBakhsh(safheAkhar)}",
                18f, 0xFFFFFFFF.toInt(), bold = true
            )
            megdar.setPadding(0, Abzar.dp(this, 6f), 0, 0)
            kh.addView(megdar)
            kh.setOnClickListener {
                startActivity(Intent(this, KhavanakActivity::class.java).putExtra("safhe", safheAkhar))
            }
            ghabe.addView(kh, safP(10))
            kh.alpha = 0f
            kh.animate().alpha(1f).setDuration(320).start()
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

        setContentView(root)
    }

    private fun radeBakhsh(b: Ketab.Bakhsh, shomare: Int, tem: Tem): View {
        val rade = LinearLayout(this)
        rade.orientation = LinearLayout.HORIZONTAL
        rade.gravity = Gravity.CENTER_VERTICAL
        rade.background = Abzar.kart(this, tem.kart, 18f)
        rade.setPadding(Abzar.dp(this, 14f), Abzar.dp(this, 13f), Abzar.dp(this, 14f), Abzar.dp(this, 13f))
        rade.isClickable = true
        Abzar.lams(rade)

        val dayere = TextView(this)
        dayere.text = b.neshane
        dayere.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17f)
        dayere.gravity = Gravity.CENTER
        dayere.background = Abzar.kartGradient(this, tem.asli, tem.dovom, 999f)
        rade.addView(dayere, LinearLayout.LayoutParams(Abzar.dp(this, 42f), Abzar.dp(this, 42f)))

        val sotoon = LinearLayout(this)
        sotoon.orientation = LinearLayout.VERTICAL
        val pS = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
        pS.marginStart = Abzar.dp(this, 12f)
        val matnOnvan = Abzar.tv(this, b.esm, 15.5f, tem.matn, bold = true)
        sotoon.addView(matnOnvan)
        val matnTozih = Abzar.tv(this, b.tozih, 11.5f, tem.kamrang)
        matnTozih.setPadding(0, Abzar.dp(this, 3f), 0, 0)
        sotoon.addView(matnTozih)
        rade.addView(sotoon, pS)

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
