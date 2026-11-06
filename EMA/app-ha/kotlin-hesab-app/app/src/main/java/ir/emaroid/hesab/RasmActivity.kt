package ir.emaroid.hesab

import android.app.Activity
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView

/*
 * نمودار تابع — y = f(x) با زوم و جابه‌جایی
 */
class RasmActivity : Activity() {

    private lateinit var voroodi: EditText
    private lateinit var rasm: RasmView
    private lateinit var payam: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Abzar.rangVaredKon(this)

        val root = LinearLayout(this)
        root.orientation = LinearLayout.VERTICAL
        root.layoutDirection = if (Zaban.rastChin(this)) View.LAYOUT_DIRECTION_RTL else View.LAYOUT_DIRECTION_LTR
        root.background = NagshZamin(Abzar.RANG_SHURU, Abzar.RANG_PAYAN)
        root.setPadding(Abzar.dp(this, 12f), Abzar.dp(this, 10f), Abzar.dp(this, 12f), Abzar.dp(this, 12f))

        // نوار بالا
        val sar = LinearLayout(this)
        val bargasht = Abzar.dokme(this, "‹ خانه", Abzar.RANG_TIZ, Abzar.RANG_TIZ, khateDor = true)
        bargasht.setOnClickListener { finish() }
        sar.addView(bargasht, LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        root.addView(sar)

        // ورودی تابع
        voroodi = EditText(this)
        voroodi.hint = "مثلاً: sin(x)+x/2   یا   x^2-3"
        voroodi.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 16f)
        voroodi.setTextColor(Abzar.RANG_MATN)
        voroodi.setHintTextColor(Abzar.RANG_KAMRANG)
        voroodi.background = Abzar.kart(this, Abzar.RANG_CODE, 12f)
        voroodi.setPadding(Abzar.dp(this, 14f), Abzar.dp(this, 12f), Abzar.dp(this, 14f), Abzar.dp(this, 12f))
        voroodi.textDirection = View.TEXT_DIRECTION_LTR
        voroodi.typeface = Typeface.MONOSPACE
        voroodi.setSingleLine(true)
        val pVoroodi = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        pVoroodi.topMargin = Abzar.dp(this, 10f)
        root.addView(voroodi, pVoroodi)

        payam = Abzar.tv(this, "تابع را بنویس و «رسم کن» را بزن", 12f, Abzar.RANG_KAMRANG)
        val pPayam = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        pPayam.topMargin = Abzar.dp(this, 6f)
        root.addView(payam, pPayam)

        // نمودار
        rasm = RasmView(this)
        val pRasm = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f)
        pRasm.topMargin = Abzar.dp(this, 10f)
        root.addView(rasm, pRasm)

        // دکمه‌ها: رسم + زوم
        val paein = LinearLayout(this)
        paein.orientation = LinearLayout.HORIZONTAL
        val dokmeRasm = Abzar.dokme(this, "رسم کن", Abzar.RANG_TIZ, Abzar.RANG_ZAMIN)
        dokmeRasm.setOnClickListener { rasmKon() }
        paein.addView(dokmeRasm, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 2f).apply {
            setMargins(Abzar.dp(this@RasmActivity, 4f), Abzar.dp(this@RasmActivity, 10f), 0, 0)
        })
        val zoome = Abzar.dokme(this, "+", Abzar.RANG_DOKME, Abzar.RANG_MATN)
        zoome.setOnClickListener { rasm.zoom(true) }
        paein.addView(zoome, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f).apply {
            setMargins(Abzar.dp(this@RasmActivity, 8f), Abzar.dp(this@RasmActivity, 10f), 0, 0)
        })
        val zoomk = Abzar.dokme(this, "−", Abzar.RANG_DOKME, Abzar.RANG_MATN)
        zoomk.setOnClickListener { rasm.zoom(false) }
        paein.addView(zoomk, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f).apply {
            setMargins(Abzar.dp(this@RasmActivity, 8f), Abzar.dp(this@RasmActivity, 10f), 0, 0)
        })
        root.addView(paein, LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))

        setContentView(root)

        // پیش‌فرض: یک نمودار زیبا از ابتدا
        voroodi.setText("sin(x)")
        rasmKon()
    }

    private fun rasmKon() {
        val e = voroodi.text.toString().trim()
        if (e.isBlank()) { payam.text = "تابع را بنویس"; return }
        try {
            // راستی‌آزمایی سریع با یک نمونه — فقط «x» مستقل (نه exp)
            val alamatX = Regex("\\bx\\b")
            MahineHesab.hesabKon(alamatX.replace(e, "(1.0)"))
            rasm.tabe = { v -> MahineHesab.hesabKon(alamatX.replace(e, "(" + MahineHesab.rishti(v) + ")")) }
            rasm.invalidate()
            payam.text = "y = $e  — با انگشت جابه‌جا کن، با +/− زوم کن"
        } catch (ex: Exception) {
            payam.text = "تابع درست نیست: ${ex.message ?: ""}"
        }
    }
}
