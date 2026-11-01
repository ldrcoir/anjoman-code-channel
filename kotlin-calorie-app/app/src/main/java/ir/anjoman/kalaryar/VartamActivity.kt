package ir.anjoman.kalaryar

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

/*
 * ورزش: کالری سوزانده با فرمول MET × وزن
 * هر ورزش ثبت‌شده به بودجهٔ غذایی امروز اضافه می‌شود
 */
class VartamActivity : Activity() {

    private lateinit var ghabe: LinearLayout

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

        val onvan = Abzar.tv(this, "🏃 ورزش و تحرک", 26f, Abzar.RANG_MATN)
        onvan.typeface = FontBox.vazirBlack(this)
        ghabe.addView(onvan)

        val tozih = Abzar.tv(
            this,
            "کالری ورزش با فرمول استاندارد MET حساب می‌شود و به بودجهٔ خوراک امروز اضافه می‌شود؛ یعنی ورزش یعنی خوراک بیشتر یا لاغری سریع‌تر!",
            13f, Abzar.RANG_KAMRANG
        )
        tozih.setPadding(0, Abzar.dp(this, 6f), 0, 0)
        ghabe.addView(tozih)

        setContentView(root)
        safheBekon()
    }

    override fun onResume() {
        super.onResume()
        if (::ghabe.isInitialized) safheBekon()
    }

    private fun safheBekon() {
        if (ghabe.childCount > 2) ghabe.removeViews(2, ghabe.childCount - 2)

        // جمع امروز
        val sozand = Khazide.varTamSozande(this, Abzar.kelidEmrooz())
        val kartJam = LinearLayout(this)
        kartJam.orientation = LinearLayout.VERTICAL
        kartJam.background = Abzar.kartGradient(this, Abzar.RANG_CODE, Abzar.RANG_KART, 18f)
        kartJam.setPadding(Abzar.dp(this, 16f), Abzar.dp(this, 14f), Abzar.dp(this, 16f), Abzar.dp(this, 14f))
        kartJam.addView(Abzar.tv(this, "امروز سوزاندی: ${Abzar.adadFarsi(sozand)} کالری 🔥", 16f, Abzar.RANG_TALAEI, bold = true))
        val varTam = Khazide.varTamRooz(this, Abzar.kelidEmrooz())
        for (v in varTam) {
            val khat = Abzar.tv(this, "• ${v.esm} — ${Abzar.adadFarsi(v.daghighe)} دقیقه = ${Abzar.adadFarsi(v.kalar)} کالری", 13f, Abzar.RANG_KAMRANG)
            khat.setPadding(0, Abzar.dp(this, 4f), 0, 0)
            kartJam.addView(khat)
        }
        val pJam = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pJam.topMargin = Abzar.dp(this, 16f)
        ghabe.addView(kartJam, pJam)

        ghabe.addView(jaFazol(8))
        ghabe.addView(Abzar.tv(this, "کدام فعالیت را انجام داده‌ای؟", 15f, Abzar.RANG_SABZ, bold = true))

        // فهرست فعالیت‌ها
        val vazn = Khazide.vaznAkhari(this) ?: Khazide.profilBekhon(this)?.vazn ?: 60.0
        for (faaliat in HesabHa.varTamHa) {
            val kart = LinearLayout(this)
            kart.orientation = LinearLayout.HORIZONTAL
            kart.gravity = Gravity.CENTER_VERTICAL
            kart.background = Abzar.kart(this, Abzar.RANG_KART)
            kart.setPadding(Abzar.dp(this, 14f), Abzar.dp(this, 12f), Abzar.dp(this, 14f), Abzar.dp(this, 12f))
            kart.isClickable = true
            Abzar.lams(kart)
            kart.setOnClickListener { daghigheDialog(faaliat, vazn) }

            val aykoon = Abzar.tv(this, faaliat.aykoon, 22f, Abzar.RANG_MATN)
            kart.addView(aykoon)

            val sotoon = LinearLayout(this)
            sotoon.orientation = LinearLayout.VERTICAL
            val pSotoon = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
            pSotoon.marginStart = Abzar.dp(this, 12f)
            sotoon.addView(Abzar.tv(this, faaliat.esm, 15f, Abzar.RANG_MATN, bold = true))
            val namoon = Abzar.tv(
                this,
                "۳۰ دقیقه برای ${Abzar.adadFarsi(vazn)} کیلو ≈ ${Abzar.adadFarsi(HesabHa.kalarVarzesi(faaliat.met, vazn, 30))} کالری",
                12f, Abzar.RANG_KAMRANG
            )
            namoon.setPadding(0, Abzar.dp(this, 2f), 0, 0)
            sotoon.addView(namoon)
            kart.addView(sotoon, pSotoon)

            val flash = Abzar.tv(this, "‹", 20f, Abzar.RANG_KAMRANG)
            kart.addView(flash)

            val pKart = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
            pKart.topMargin = Abzar.dp(this, 8f)
            ghabe.addView(kart, pKart)
        }
    }

    private fun daghigheDialog(faaliat: HesabHa.FaaliatVarzesi, vazn: Double) {
        val ghabe = LinearLayout(this)
        ghabe.orientation = LinearLayout.VERTICAL
        ghabe.setPadding(Abzar.dp(this, 22f), Abzar.dp(this, 20f), Abzar.dp(this, 22f), Abzar.dp(this, 12f))
        ghabe.setBackgroundColor(Abzar.RANG_KART)

        ghabe.addView(Abzar.tv(this, "${faaliat.aykoon} ${faaliat.esm}", 20f, Abzar.RANG_MATN, bold = true))
        ghabe.addView(Abzar.tv(this, "چند دقیقه؟", 14f, Abzar.RANG_KAMRANG))

        val edt = EditText(this)
        edt.setText("30")
        edt.setTextColor(Abzar.RANG_MATN)
        edt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        edt.typeface = FontBox.vazirBold(this)
        edt.inputType = android.text.InputType.TYPE_CLASS_NUMBER
        edt.background = Abzar.kart(this, Abzar.RANG_CODE, 12f)
        edt.setPadding(Abzar.dp(this, 14f), Abzar.dp(this, 10f), Abzar.dp(this, 14f), Abzar.dp(this, 10f))
        val pEdt = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pEdt.topMargin = Abzar.dp(this, 8f)
        ghabe.addView(edt, pEdt)

        val matnPishnamayesh = Abzar.tv(this, "", 14f, Abzar.RANG_TALAEI, bold = true)
        matnPishnamayesh.setPadding(0, Abzar.dp(this, 10f), 0, 0)
        ghabe.addView(matnPishnamayesh)
        val tozirKon = {
            val d = edt.text.toString().toIntOrNull() ?: 0
            matnPishnamayesh.text = "≈ ${Abzar.adadFarsi(HesabHa.kalarVarzesi(faaliat.met, vazn, d))} کالری"
        }
        edt.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, a: Int, b: Int, c: Int) {}
            override fun onTextChanged(s: CharSequence?, a: Int, b: Int, c: Int) {}
            override fun afterTextChanged(s: android.text.Editable?) { tozirKon() }
        })
        tozirKon()

        val dialog = AlertDialog.Builder(this).setView(ghabe).create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val safDokme = LinearLayout(this)
        val enseraf = Abzar.dokme(this, "انصراف", Abzar.RANG_KHAKISTARI, Abzar.RANG_KAMRANG, khateDor = true)
        enseraf.setOnClickListener { dialog.dismiss() }
        safDokme.addView(enseraf, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))

        val sabt = Abzar.dokme(this, "ثبت ✓", Abzar.RANG_SABZ, Abzar.RANG_ZAMIN)
        sabt.setOnClickListener {
            val d = edt.text.toString().toIntOrNull() ?: 0
            if (d > 0) {
                Khazide.varTamEzafe(
                    this, Abzar.kelidEmrooz(),
                    VartamVaredi(faaliat.esm, d, HesabHa.kalarVarzesi(faaliat.met, vazn, d))
                )
                dialog.dismiss()
                android.widget.Toast.makeText(this, "ورزش ثبت شد ✓", android.widget.Toast.LENGTH_SHORT).show()
                safheBekon()
            }
        }
        val pSabt = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
        pSabt.marginStart = Abzar.dp(this, 8f)
        safDokme.addView(sabt, pSabt)
        val pSaf = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pSaf.topMargin = Abzar.dp(this, 14f)
        ghabe.addView(safDokme, pSaf)
    }

    private fun jaFazol(dp: Int): View {
        val v = View(this)
        v.layoutParams = LinearLayout.LayoutParams(1, Abzar.dp(this, dp.toFloat()))
        return v
    }
}
