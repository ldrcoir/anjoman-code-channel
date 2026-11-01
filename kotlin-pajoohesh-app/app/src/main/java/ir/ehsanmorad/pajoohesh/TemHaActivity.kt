package ir.ehsanmorad.pajoohesh

import android.app.Activity
import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast

/*
 * تم‌ها — ده چهره برای کتاب، با کارت‌های کوچک و مرتب در سه ستون
 * انتخاب فوراً اعمال می‌شود و در حافظهٔ گوشی می‌ماند
 */
class TemHaActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TemEnekhab.rangVaredKon(this)
        val tem = TemEnekhab.bekhon(this)
        val shomareFaali = TemEnekhab.temHa.indexOf(tem)

        val rishe = LinearLayout(this)
        rishe.layoutDirection = View.LAYOUT_DIRECTION_RTL
        rishe.orientation = LinearLayout.VERTICAL
        rishe.setBackgroundColor(tem.zamin)

        val onvan = TextView(this)
        onvan.text = "تم کتاب"
        onvan.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26f)
        onvan.setTextColor(tem.matn)
        onvan.typeface = Fontha.lalezar(this)
        onvan.setPadding(Abzar.dp(this, 18f), Abzar.dp(this, 18f), Abzar.dp(this, 18f), Abzar.dp(this, 4f))
        rishe.addView(onvan)
        val tozih = Abzar.tv(
            this,
            "ده چهره برای کتاب — در خواناک هم دکمهٔ شب‌خوانی برای تاریکیِ صفحات هست",
            13f, tem.kamrang
        )
        tozih.setPadding(Abzar.dp(this, 18f), 0, Abzar.dp(this, 18f), 0)
        rishe.addView(tozih)

        val lafand = ScrollView(this)
        val ghabe = LinearLayout(this)
        ghabe.orientation = LinearLayout.VERTICAL
        ghabe.setPadding(Abzar.dp(this, 14f), Abzar.dp(this, 14f), Abzar.dp(this, 14f), Abzar.dp(this, 24f))
        lafand.addView(ghabe)
        rishe.addView(lafand, LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        ))

        // ردیف‌های سه‌ستونه با کارت‌های کوچک
        var saf: LinearLayout? = null
        for ((i, t) in TemEnekhab.temHa.withIndex()) {
            if (i % 3 == 0) {
                saf = LinearLayout(this)
                saf.orientation = LinearLayout.HORIZONTAL
                ghabe.addView(saf, safP(8))
            }
            saf!!.addView(kartTem(t, i == shomareFaali, i), pKart())
        }

        setContentView(rishe)
    }

    // کارت کوچک تم: پیش‌نمایش گرد + نام
    private fun kartTem(t: Tem, entekhabShode: Boolean, shomare: Int): View {
        val kart = LinearLayout(this)
        kart.orientation = LinearLayout.VERTICAL
        kart.gravity = Gravity.CENTER
        kart.background = Abzar.kart(this, t.kart, 16f)
        if (entekhabShode) {
            kart.foreground = Abzar.kartKhatei(this, t.asli, 0, 16f, 2f)
        }
        kart.setPadding(Abzar.dp(this, 6f), Abzar.dp(this, 10f), Abzar.dp(this, 6f), Abzar.dp(this, 10f))
        kart.isClickable = true
        Abzar.lams(kart)

        // دایرهٔ پیش‌نمایش رنگ تم
        val ghabeDayere = FrameLayout(this)
        val dayere = View(this)
        dayere.background = Abzar.kartGradient(this, t.asli, t.dovom, 999f)
        ghabeDayere.addView(dayere, FrameLayout.LayoutParams(
            Abzar.dp(this, 46f), Abzar.dp(this, 46f)
        ))
        // کارت کوچک روی دایره — حس صفحهٔ کتاب با همین تم
        val kartKoochak = View(this)
        kartKoochak.background = Abzar.kart(this, t.zamin, 6f)
        val pKoochak = FrameLayout.LayoutParams(Abzar.dp(this, 22f), Abzar.dp(this, 15f))
        pKoochak.gravity = Gravity.CENTER
        ghabeDayere.addView(kartKoochak, pKoochak)

        // نشان انتخاب
        if (entekhabShode) {
            val mohr = TextView(this)
            mohr.text = "✓"
            mohr.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
            mohr.setTextColor(0xFFFFFFFF.toInt())
            mohr.typeface = Fontha.vazirPorReng(this)
            mohr.gravity = Gravity.CENTER
            mohr.background = Abzar.dayereh(this, t.asli)
            val pMohr = FrameLayout.LayoutParams(Abzar.dp(this, 17f), Abzar.dp(this, 17f))
            pMohr.gravity = Gravity.END or Gravity.TOP
            ghabeDayere.addView(mohr, pMohr)
        }
        kart.addView(ghabeDayere)

        // نام تم
        val esm = TextView(this)
        esm.text = t.esm
        esm.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11f)
        esm.setTextColor(if (entekhabShode) t.asli else t.matn)
        esm.typeface = Fontha.vazirPorReng(this)
        esm.gravity = Gravity.CENTER
        esm.maxLines = 1
        esm.setPadding(0, Abzar.dp(this, 7f), 0, 0)
        kart.addView(esm)

        kart.setOnClickListener {
            TemEnekhab.zakhireKon(this, shomare)
            Toast.makeText(this, "تم «${t.esm}» فعال شد ✓", Toast.LENGTH_SHORT).show()
            recreate()
        }
        return kart
    }

    private fun pKart(): LinearLayout.LayoutParams {
        val p = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
        p.marginStart = Abzar.dp(this, 4f)
        p.marginEnd = Abzar.dp(this, 4f)
        return p
    }

    private fun safP(t: Int): LinearLayout.LayoutParams {
        val p = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        p.topMargin = Abzar.dp(this, t.toFloat())
        return p
    }
}
