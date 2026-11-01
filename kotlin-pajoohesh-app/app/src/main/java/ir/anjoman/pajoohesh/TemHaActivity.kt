package ir.anjoman.pajoohesh

import android.app.Activity
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast

/*
 * تم‌ها — شش چهره برای کتاب: کلاسیک کرم، شب مطالعه، کاغذ گرم، سبز آرام، بنفش شفق، طلایی سلطنتی
 * انتخاب فوراً اعمال می‌شود و در حافظهٔ گوشی می‌ماند
 */
class TemHaActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TemEnekhab.rangVaredKon(this)
        val tem = TemEnekhab.bekhon(this)
        val shomareFaali = TemEnekhab.temHa.indexOf(tem)

        val root = LinearLayout(this)
        root.layoutDirection = View.LAYOUT_DIRECTION_RTL
        root.orientation = LinearLayout.VERTICAL
        root.setBackgroundColor(tem.zamin)

        val onvan = Abzar.tv(this, "🎨 تم کتاب", 24f, tem.matn)
        onvan.typeface = FontBox.vazirBlack(this)
        onvan.setPadding(Abzar.dp(this, 18f), Abzar.dp(this, 20f), Abzar.dp(this, 18f), Abzar.dp(this, 4f))
        root.addView(onvan)
        val tozih = Abzar.tv(
            this,
            "شش چهره برای کتاب — در خواناک هم دکمهٔ 🌙 شب‌خوانی برای تاریکیِ صفحات هست",
            13f, tem.kamrang
        )
        tozih.setPadding(Abzar.dp(this, 18f), 0, Abzar.dp(this, 18f), 0)
        root.addView(tozih)

        val scroll = ScrollView(this)
        val ghabe = LinearLayout(this)
        ghabe.orientation = LinearLayout.VERTICAL
        ghabe.setPadding(Abzar.dp(this, 18f), Abzar.dp(this, 12f), Abzar.dp(this, 18f), Abzar.dp(this, 24f))
        scroll.addView(ghabe)
        root.addView(scroll, LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        ))

        for ((i, t) in TemEnekhab.temHa.withIndex()) {
            ghabe.addView(kartTem(t, i == shomareFaali, i), safP(12))
        }

        setContentView(root)
    }

    private fun kartTem(t: Tem, entekhabShode: Boolean, shomare: Int): View {
        val kart = LinearLayout(this)
        kart.orientation = LinearLayout.VERTICAL
        kart.background = Abzar.kart(this, t.kart, 20f)
        if (entekhabShode) {
            kart.foreground = Abzar.kartKhatei(this, t.asli, 0, 20f, 1.8f)
        }
        kart.setPadding(Abzar.dp(this, 14f), Abzar.dp(this, 14f), Abzar.dp(this, 14f), Abzar.dp(this, 14f))
        kart.isClickable = true
        Abzar.lams(kart)

        // پیش‌نمایش کوچک از صفحه با همین تم
        val pishnamayesh = LinearLayout(this)
        pishnamayesh.orientation = LinearLayout.VERTICAL
        pishnamayesh.background = Abzar.kart(this, t.zamin, 14f)
        pishnamayesh.setPadding(Abzar.dp(this, 10f), Abzar.dp(this, 10f), Abzar.dp(this, 10f), Abzar.dp(this, 10f))

        val khateOnvan = View(this)
        khateOnvan.background = Abzar.kartGradient(this, t.asli, t.dovom, 6f)
        pishnamayesh.addView(khateOnvan, LinearLayout.LayoutParams(Abzar.dp(this, 110f), Abzar.dp(this, 10f)))

        val kartKoochak = View(this)
        kartKoochak.background = Abzar.kart(this, t.kart, 8f)
        val pKoochak = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, Abzar.dp(this, 34f)
        )
        pKoochak.topMargin = Abzar.dp(this, 8f)
        pishnamayesh.addView(kartKoochak, pKoochak)

        val safRang = LinearLayout(this)
        safRang.orientation = LinearLayout.HORIZONTAL
        for (rang in listOf(t.asli, t.dovom, t.kamrang)) {
            val noghte = View(this)
            noghte.background = Abzar.dayereh(this, rang)
            val pN = LinearLayout.LayoutParams(Abzar.dp(this, 14f), Abzar.dp(this, 14f))
            pN.marginStart = Abzar.dp(this, 6f)
            pN.topMargin = Abzar.dp(this, 8f)
            safRang.addView(noghte, pN)
        }
        pishnamayesh.addView(safRang)
        kart.addView(pishnamayesh)

        // نام + نشان انتخاب
        val safEsm = LinearLayout(this)
        safEsm.orientation = LinearLayout.HORIZONTAL
        safEsm.gravity = Gravity.CENTER_VERTICAL
        val esm = Abzar.tv(this, t.esm, 16f, t.matn, bold = true)
        esm.typeface = FontBox.vazirBold(this)
        safEsm.addView(esm, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))
        val mohr = Abzar.tv(
            this,
            if (entekhabShode) "✓ همین حالا فعال است" else "انتخاب",
            12f, if (entekhabShode) t.asli else t.kamrang,
            bold = entekhabShode
        )
        safEsm.addView(mohr)
        kart.addView(safEsm, safP(10))

        kart.setOnClickListener {
            TemEnekhab.zakhireKon(this, shomare)
            Toast.makeText(this, "تم «${t.esm}» فعال شد ✓", Toast.LENGTH_SHORT).show()
            recreate()
        }
        return kart
    }

    private fun safP(t: Int): LinearLayout.LayoutParams {
        val p = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        p.topMargin = Abzar.dp(this, t.toFloat())
        return p
    }
}
