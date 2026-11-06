package ir.emaroid.hesab

import android.app.Activity
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

/*
 * معادلات سنگین — درجهٔ ۳ (کاردانو)، درجهٔ ۴ و بالاتر (دوران-کرنر)، حل عددی f(x)=0
 * پالت سنگ‌زغالی؛ ورودی ضرایب با کاما از بالاترین درجه؛ خروجی: مراحل + ریشه‌ها
 */
class MoadelatSanginActivity : Activity() {

    private lateinit var ghabeVoroodi: LinearLayout
    private lateinit var ghabeNatije: LinearLayout
    private lateinit var dokmeHa: List<TextView>
    private var hal = 0 // 0=درجه۳ 1=درجه۴+ 2=عددی

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Abzar.rangVaredKon(this)

        val root = LinearLayout(this)
        root.orientation = LinearLayout.VERTICAL
        root.layoutDirection = View.LAYOUT_DIRECTION_LTR
        root.background = NagshZamin(Abzar.RANG_SHURU, Abzar.RANG_PAYAN)
        root.setPadding(Abzar.dp(this, 14f), Abzar.dp(this, 12f), Abzar.dp(this, 14f), Abzar.dp(this, 14f))

        val sar = LinearLayout(this)
        sar.layoutDirection = if (Zaban.rastChin(this)) View.LAYOUT_DIRECTION_RTL else View.LAYOUT_DIRECTION_LTR
        val bargasht = Abzar.dokme(this, Zaban.t("khane"), Abzar.RANG_TIZ, Abzar.RANG_TIZ, khateDor = true)
        bargasht.setOnClickListener { finish() }
        sar.addView(bargasht)
        val onvan = Abzar.tv(this, Zaban.t("b5t"), 20f, Abzar.RANG_MATN, bold = true)
        val pO = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
        pO.marginStart = Abzar.dp(this, 12f)
        onvan.gravity = Gravity.CENTER_VERTICAL
        sar.addView(onvan, pO)
        root.addView(sar, LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))

        // انتخاب حالت
        val radifHalat = LinearLayout(this)
        dokmeHa = listOf("درجهٔ ۳", "درجهٔ ۴+", "عددی f(x)=0").map { matn ->
            val d = Abzar.dokme(this, matn, Abzar.RANG_KHAKISTARI, Abzar.RANG_MATN, size = 14f)
            d.setOnClickListener { halDajKon(matn) }
            d
        }
        for (d in dokmeHa) {
            radifHalat.addView(d, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f).apply {
                setMargins(Abzar.dp(this@MoadelatSanginActivity, 3f), 0, Abzar.dp(this@MoadelatSanginActivity, 3f), 0)
            })
        }
        val pH = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        pH.topMargin = Abzar.dp(this, 14f)
        root.addView(radifHalat, pH)

        ghabeVoroodi = LinearLayout(this)
        ghabeVoroodi.orientation = LinearLayout.VERTICAL
        val pV = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        pV.topMargin = Abzar.dp(this, 12f)
        root.addView(ghabeVoroodi, pV)

        // دکمهٔ حل
        val dokmeHal = Abzar.dokme(this, "حل کن", Abzar.RANG_TIZ, 0xFF08121A.toInt())
        dokmeHal.setOnClickListener { hal() }
        val pHal = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        pHal.topMargin = Abzar.dp(this, 12f)
        root.addView(dokmeHal, pHal)

        // نتیجه‌ها
        val scroll = ScrollView(this)
        scroll.isFillViewport = true
        val pS = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f)
        pS.topMargin = Abzar.dp(this, 12f)
        root.addView(scroll, pS)

        ghabeNatije = LinearLayout(this)
        ghabeNatije.orientation = LinearLayout.VERTICAL
        scroll.addView(ghabeNatije, ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))

        setContentView(root)
        halDajKon("درجهٔ ۳")
    }

    private fun voroodiSaz(hint: String, matn: String): EditText {
        val e = EditText(this)
        e.hint = hint
        e.setText(matn)
        e.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 16f)
        e.setTextColor(Abzar.RANG_MATN)
        e.setHintTextColor(Abzar.RANG_KAMRANG)
        e.background = Abzar.kart(this, Abzar.RANG_CODE, 12f)
        e.setPadding(Abzar.dp(this, 14f), Abzar.dp(this, 12f), Abzar.dp(this, 14f), Abzar.dp(this, 12f))
        e.textDirection = View.TEXT_DIRECTION_LTR
        e.inputType = android.text.InputType.TYPE_CLASS_TEXT
        e.setSingleLine(true)
        e.typeface = Typeface.MONOSPACE
        return e
    }

    private fun halDajKon(matn: String) {
        for (d in dokmeHa) d.background = Abzar.kart(this, Abzar.RANG_KHAKISTARI, 14f)
        val faal = dokmeHa.first { it.text == matn }
        faal.background = Abzar.kart(this, Abzar.RANG_TIZ, 14f)
        hal = when (matn) {
            "درجهٔ ۳" -> 0
            "درجهٔ ۴+" -> 1
            else -> 2
        }
        ghabeVoroodi.removeAllViews()
        val hint = TextView(this)
        hint.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 12f)
        hint.setTextColor(Abzar.RANG_KAMRANG)
        when (hal) {
            0 -> {
                hint.text = "ضرایب ax³ + bx² + cx + d را با کاما بنویس (از a تا d) — مثلاً:  2,-3,0,1"
                ghabeVoroodi.addView(hint)
                ghabeVoroodi.addView(voroodiSaz("ضرایب با کاما — مثلاً 2,-3,0,1", "2,-3,0,1"))
            }
            1 -> {
                hint.text = "ضرایب چندجمله‌ای هر درجه‌ای با کاما (از بالاترین تا ثابت) — مثلاً درجهٔ ۴:  1,0,-5,0,4"
                ghabeVoroodi.addView(hint)
                ghabeVoroodi.addView(voroodiSaz("ضرایب با کاما — مثلاً 1,0,-5,0,4", "1,0,-5,0,4"))
            }
            else -> {
                hint.text = "فقط سمت چپ معادله (بدون =) با متغیر x — توابع کار می‌کنند:  sin(x)−x/2  یا  x^3−2x−5  یا  ln(x)−1"
                ghabeVoroodi.addView(hint)
                ghabeVoroodi.addView(voroodiSaz("مثلاً sin(x)−x/2 یا x^3−2x−5", "sin(x)−x/2"))
            }
        }
        hint.setPadding(0, 0, 0, Abzar.dp(this, 6f))
    }

    private fun hal() {
        try {
            val halJadid: MoadelatSangin.Hal = when (hal) {
                0 -> {
                    val t = (ghabeVoroodi.getChildAt(1) as EditText).text.toString()
                    val jz = MoadelatSangin.bezanInput(t)
                    if (jz.size != 4) throw MahineHesab.Khata("برای درجهٔ ۳ دقیقاً ۴ ضریب لازم است")
                    MoadelatSangin.seDaji(jz[0], jz[1], jz[2], jz[3])
                }
                1 -> {
                    val t = (ghabeVoroodi.getChildAt(1) as EditText).text.toString()
                    MoadelatSangin.polinomi(MoadelatSangin.bezanInput(t))
                }
                else -> {
                    val t = (ghabeVoroodi.getChildAt(1) as EditText).text.toString()
                    MoadelatSangin.adadi(t)
                }
            }
            namayesh(halJadid)
        } catch (e: Exception) {
            ghabeNatije(listOf("⚠️ ${e.message}"))
        }
    }

    private fun namayesh(hal: MoadelatSangin.Hal) {
        ghabeNatije.removeAllViews()

        val kartJavab = LinearLayout(this)
        kartJavab.orientation = LinearLayout.VERTICAL
        kartJavab.background = Abzar.shisheh(this, 0x331B212A, 0x402FA8C9, 16f, 1.5f)
        kartJavab.setPadding(Abzar.dp(this, 16f), Abzar.dp(this, 14f), Abzar.dp(this, 16f), Abzar.dp(this, 14f))
        kartJavab.addView(Abzar.tv(this, "ریشه‌ها", 15f, Abzar.RANG_TIZ, bold = true))
        for (j in hal.javabHa) {
            val t = TextView(this)
            t.text = j
            t.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 17f)
            t.setTextColor(Abzar.RANG_MATN)
            t.typeface = Typeface.MONOSPACE
            t.textDirection = View.TEXT_DIRECTION_LTR
            t.setPadding(0, Abzar.dp(this, 6f), 0, 0)
            kartJavab.addView(t)
        }
        ghabeNatije.addView(kartJavab, LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
            topMargin = Abzar.dp(this@MoadelatSanginActivity, 10f)
        })

        val kartMarahel = LinearLayout(this)
        kartMarahel.orientation = LinearLayout.VERTICAL
        kartMarahel.background = Abzar.kart(this)
        kartMarahel.setPadding(Abzar.dp(this, 16f), Abzar.dp(this, 14f), Abzar.dp(this, 16f), Abzar.dp(this, 14f))
        kartMarahel.addView(Abzar.tv(this, "مراحل", 14f, Abzar.RANG_TALAEI, bold = true))
        for (m in hal.marahel) {
            val t = Abzar.tv(this, m, 13f, Abzar.RANG_KAMRANG)
            t.setLineSpacing(Abzar.dp(this, 3f).toFloat(), 1f)
            t.setPadding(0, Abzar.dp(this, 6f), 0, 0)
            kartMarahel.addView(t)
        }
        ghabeNatije.addView(kartMarahel, LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
            topMargin = Abzar.dp(this@MoadelatSanginActivity, 10f)
        })
    }

    private fun ghabeNatije(khoroji: List<String>) {
        ghabeNatije.removeAllViews()
        val kart = LinearLayout(this)
        kart.orientation = LinearLayout.VERTICAL
        kart.background = Abzar.kart(this)
        kart.setPadding(Abzar.dp(this, 16f), Abzar.dp(this, 14f), Abzar.dp(this, 16f), Abzar.dp(this, 14f))
        for (s in khoroji) {
            val t = Abzar.tv(this, s, 14f, Abzar.RANG_MATN)
            t.setPadding(0, Abzar.dp(this, 6f), 0, 0)
            kart.addView(t)
        }
        ghabeNatije.addView(kart)
    }
}
