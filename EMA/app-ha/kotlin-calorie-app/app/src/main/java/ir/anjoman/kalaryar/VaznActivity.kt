package ir.anjoman.kalaryar

import android.app.Activity
import android.os.Bundle
import android.text.InputType
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

/*
 * وزن و BMI: ثبت وزن، نمودار روند، مسیر هدف
 */
class VaznActivity : Activity() {

    private lateinit var ghabe: LinearLayout
    private lateinit var edtVazn: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Abzar.rangVaredKon(this)

        val root = ScrollView(this)
        root.layoutDirection = View.LAYOUT_DIRECTION_RTL
        root.setBackgroundColor(Abzar.RANG_ZAMIN)
        root.isFillViewport = true

        ghabe = LinearLayout(this)
        ghabe.orientation = LinearLayout.VERTICAL
        ghabe.setPadding(Abzar.dp(this, 18f), Abzar.dp(this, 24f), Abzar.dp(this, 18f), Abzar.dp(this, 24f))
        root.addView(ghabe, ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ))

        val onvan = Abzar.tv(this, "⚖️ وزن و BMI", 26f, Abzar.RANG_MATN)
        onvan.typeface = FontBox.vazirBlack(this)
        ghabe.addView(onvan)

        val tozih = Abzar.tv(this, "هفته‌ای یک بار، صبح ناشتا وزن کنی بهترین اندازه‌گیری است.", 13f, Abzar.RANG_KAMRANG)
        tozih.setPadding(0, Abzar.dp(this, 6f), 0, 0)
        ghabe.addView(tozih)

        // فرم ثبت
        ghabe.addView(jaFazol(16))
        ghabe.addView(Abzar.tv(this, "وزن امروز (کیلوگرم)", 15f, Abzar.RANG_SABZ, bold = true))
        edtVazn = EditText(this)
        edtVazn.hint = "مثلاً ۶۰٫۵"
        edtVazn.setTextColor(Abzar.RANG_MATN)
        edtVazn.setHintTextColor(Abzar.RANG_KAMRANG)
        edtVazn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        edtVazn.typeface = FontBox.vazirBold(this)
        edtVazn.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        edtVazn.background = Abzar.kart(this, Abzar.RANG_CODE, 12f)
        edtVazn.setPadding(Abzar.dp(this, 14f), Abzar.dp(this, 12f), Abzar.dp(this, 14f), Abzar.dp(this, 12f))
        val pEdt = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pEdt.topMargin = Abzar.dp(this, 8f)
        ghabe.addView(edtVazn, pEdt)

        val dokmeSabt = Abzar.dokme(this, "ثبت وزن امروز ✓", Abzar.RANG_SABZ, Abzar.RANG_ZAMIN)
        dokmeSabt.setOnClickListener { sabtKon() }
        val pSabt = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pSabt.topMargin = Abzar.dp(this, 12f)
        ghabe.addView(dokmeSabt, pSabt)

        setContentView(root)
        safheBekon()
    }

    override fun onResume() {
        super.onResume()
        if (::ghabe.isInitialized) safheBekon()
    }

    private fun sabtKon() {
        val vazn = edtVazn.text.toString().replace("٫", ".").replace("،", ".").toDoubleOrNull()
        if (vazn == null || vazn < 25 || vazn > 250) {
            edtVazn.error = "وزن را درست بنویس"
            return
        }
        Khazide.vaznEzafe(this, Abzar.kelidEmrooz(), vazn)
        edtVazn.setText("")
        android.widget.Toast.makeText(this, "وزن ثبت شد ✓", android.widget.Toast.LENGTH_SHORT).show()
        safheBekon()
    }

    private fun safheBekon() {
        // بخش گزارش را دوباره می‌سازیم (بعد از فرم)
        // فرم پنج عنصر دارد: عنوان، توضیح، برچسب، کادر وزن، دکمهٔ ثبت
        if (ghabe.childCount > 5) {
            ghabe.removeViews(5, ghabe.childCount - 5)
        }

        val profil = Khazide.profilBekhon(this) ?: return
        val vaznHa = Khazide.vaznHa(this)

        // کارت BMI و هدف
        val kartBmi = LinearLayout(this)
        kartBmi.orientation = LinearLayout.VERTICAL
        kartBmi.background = Abzar.kartGradient(this, Abzar.RANG_CODE, Abzar.RANG_KART, 18f)
        kartBmi.setPadding(Abzar.dp(this, 16f), Abzar.dp(this, 14f), Abzar.dp(this, 16f), Abzar.dp(this, 14f))
        val b = HesabHa.bmi(profil)
        kartBmi.addView(Abzar.tv(this, "BMI: ${Abzar.adadFarsi(b)} — ${HesabHa.tosifBmi(b)}", 15f, Abzar.RANG_MATN, bold = true))
        val tafavot = profil.vazn - profil.vaznHadaf
        if (tafavot > 0.5) {
            kartBmi.addView(Abzar.tv(this, "تا هدف (${Abzar.adadFarsi(profil.vaznHadaf)} کیلو): ${Abzar.adadFarsi(tafavot)} کیلو مانده", 13f, Abzar.RANG_KAMRANG))
            val rooz = HesabHa.roozTahadaf(profil)
            if (rooz != null) {
                kartBmi.addView(Abzar.tv(this, "با سرعت ایمن، حدود ${Abzar.adadFarsi(rooz)} روز دیگر 🎯", 13f, Abzar.RANG_SABZ, bold = true))
            }
        } else if (tafavot < -0.5) {
            kartBmi.addView(Abzar.tv(this, "${Abzar.adadFarsi(-tafavot)} کیلو زیر هدف؛ سالم غذا بخور 🌿", 13f, Abzar.RANG_KAMRANG))
        } else {
            kartBmi.addView(Abzar.tv(this, "روی هدف هستی! تثبیتش هم هنر است 🏆", 13f, Abzar.RANG_SABZ, bold = true))
        }
        val pBmi = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pBmi.topMargin = Abzar.dp(this, 16f)
        ghabe.addView(kartBmi, pBmi)

        // نمودار وزن
        if (vaznHa.size >= 2) {
            ghabe.addView(jaFazol(16))
            ghabe.addView(Abzar.tv(this, "روند وزن", 16f, Abzar.RANG_TALAEI, bold = true))
            val kartChart = LinearLayout(this)
            kartChart.orientation = LinearLayout.VERTICAL
            kartChart.background = Abzar.kart(this, Abzar.RANG_KART, 18f)
            kartChart.setPadding(Abzar.dp(this, 10f), Abzar.dp(this, 10f), Abzar.dp(this, 10f), Abzar.dp(this, 10f))
            val chart = KhatChart(this)
            chart.noghteHa = vaznHa.map { it.second }
            chart.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, Abzar.dp(this, 170f)
            )
            kartChart.addView(chart)
            val pChart = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
            pChart.topMargin = Abzar.dp(this, 10f)
            ghabe.addView(kartChart, pChart)
        }

        // فهرست ثبت‌ها
        if (vaznHa.isNotEmpty()) {
            ghabe.addView(jaFazol(14))
            ghabe.addView(Abzar.tv(this, "ثبت‌ها", 15f, Abzar.RANG_MATN, bold = true))
            val kartList = LinearLayout(this)
            kartList.orientation = LinearLayout.VERTICAL
            kartList.background = Abzar.kart(this, Abzar.RANG_KART, 18f)
            kartList.setPadding(Abzar.dp(this, 14f), Abzar.dp(this, 10f), Abzar.dp(this, 14f), Abzar.dp(this, 10f))
            for ((kelid, vazn) in vaznHa.reversed()) {
                val khat = Abzar.tv(
                    this,
                    "${Abzar.tarikhShamsi(kelid.substring(0, 4).toInt(), kelid.substring(5, 7).toInt(), kelid.substring(8, 10).toInt())} — ${Abzar.adadFarsi(vazn)} کیلو",
                    13f, Abzar.RANG_MATN
                )
                khat.setPadding(0, Abzar.dp(this, 6f), 0, 0)
                kartList.addView(khat)
            }
            val pList = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
            pList.topMargin = Abzar.dp(this, 10f)
            ghabe.addView(kartList, pList)
        } else {
            val khali = Abzar.tv(this, "هنوز وزنی ثبت نشده؛ اولین ثبت را همین امروز بزن!", 13f, Abzar.RANG_KAMRANG)
            val pKhali = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
            pKhali.topMargin = Abzar.dp(this, 14f)
            ghabe.addView(khali, pKhali)
        }
    }

    private fun jaFazol(dp: Int): View {
        val v = View(this)
        v.layoutParams = LinearLayout.LayoutParams(1, Abzar.dp(this, dp.toFloat()))
        return v
    }
}
