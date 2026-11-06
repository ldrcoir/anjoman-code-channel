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
 * انتخاب تم — شش تم رنگی
 * با هر لمس، تم ذخیره و اعمال می‌شود؛ همهٔ صفحه‌ها موقع برگشتن با تم تازه ساخته می‌شوند
 */
class TemActivity : Activity() {

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
        root.layoutDirection = View.LAYOUT_DIRECTION_RTL
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

        val onvan = Abzar.tv(this, "تم و ظاهر", 28f, Abzar.RANG_MATN)
        onvan.typeface = FontBox.vazirBlack(this)
        ghabe.addView(onvan)

        val zirOnvan = Abzar.tv(this, "یکی را انتخاب کن؛ کل اپ همان لحظه عوض می‌شود", 14f, Abzar.RANG_KAMRANG)
        zirOnvan.setPadding(0, Abzar.dp(this, 6f), 0, Abzar.dp(this, 8f))
        ghabe.addView(zirOnvan)

        for (tem in TemHa.temHa) {
            val p = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            p.topMargin = Abzar.dp(this, 14f)
            ghabe.addView(kartTem(tem), p)
        }

        val nasihat = Abzar.tv(
            this,
            "نکته: تم‌های تیره برای شب و چشم، تم روشن برای نور روز خوب است.",
            13f, Abzar.RANG_KAMRANG
        )
        val pNasihat = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pNasihat.topMargin = Abzar.dp(this, 20f)
        ghabe.addView(nasihat, pNasihat)

        return root
    }

    // کارت هر تم: نمونهٔ رنگ‌ها + نام + علامت انتخاب
    private fun kartTem(tem: Tem): LinearLayout {
        val faaliAst = tem.esm == TemHa.faeli.esm

        val kart = LinearLayout(this)
        kart.orientation = LinearLayout.HORIZONTAL
        kart.gravity = Gravity.CENTER_VERTICAL
        kart.background = if (faaliAst)
            Abzar.kartKhatei(this, Abzar.RANG_TIZ, 16f)
        else
            Abzar.kart(this, Abzar.RANG_KART, 16f)
        kart.setPadding(
            Abzar.dp(this, 16f), Abzar.dp(this, 16f),
            Abzar.dp(this, 16f), Abzar.dp(this, 16f)
        )
        kart.isClickable = true
        Abzar.lams(kart)
        kart.setOnClickListener {
            TemHa.entekhabKon(this, tem)
            setContentView(safheBekon())   // بازسازی فوری همین صفحه با تم تازه
        }

        // سه دایرهٔ نمونه: پس‌زمینه، تاکیدی، طلایی
        val nemooneHa = LinearLayout(this)
        nemooneHa.orientation = LinearLayout.HORIZONTAL
        val rangHa = listOf(tem.zamin, tem.tiz, tem.talaei)
        for (rang in rangHa) {
            val dayereh = View(this)
            dayereh.background = Abzar.dayereh(this, rang)
            if (rang == tem.zamin) {
                dayereh.background = Abzar.kartKhatei(this, tem.kamrang, 100f)
            }
            val pDayereh = LinearLayout.LayoutParams(Abzar.dp(this, 26f), Abzar.dp(this, 26f))
            if (rang != rangHa[0]) pDayereh.marginStart = Abzar.dp(this, 8f)
            nemooneHa.addView(dayereh, pDayereh)
        }
        kart.addView(nemooneHa)

        val sotoon = LinearLayout(this)
        sotoon.orientation = LinearLayout.VERTICAL
        val matnEsm = Abzar.tv(this, tem.esm, 17f, Abzar.RANG_MATN, bold = true)
        sotoon.addView(matnEsm)
        val matnNo = Abzar.tv(
            this,
            if (tem.tir) "تم تیره" else "تم روشن",
            12f, Abzar.RANG_KAMRANG
        )
        matnNo.setPadding(0, Abzar.dp(this, 3f), 0, 0)
        sotoon.addView(matnNo)
        val pSotoon = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
        pSotoon.marginStart = Abzar.dp(this, 14f)
        kart.addView(sotoon, pSotoon)

        val halat = TextView(this)
        halat.text = if (faaliAst) "✓ فعال" else "انتخاب"
        halat.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
        halat.setTextColor(if (faaliAst) Abzar.RANG_SABZ else Abzar.RANG_KAMRANG)
        halat.typeface = FontBox.vazirBold(this)
        kart.addView(halat)

        return kart
    }
}
