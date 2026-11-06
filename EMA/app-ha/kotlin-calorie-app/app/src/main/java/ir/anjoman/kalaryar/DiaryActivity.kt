package ir.anjoman.kalaryar

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

/*
 * دفترچهٔ امروز: چهار وعده با ردیف‌های قابل حذف
 * هر برگشت به این صفحه، جمع‌ها تازه می‌شود
 */
class DiaryActivity : Activity() {

    private lateinit var ghabe: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Abzar.rangVaredKon(this)

        val root = LinearLayout(this)
        root.layoutDirection = View.LAYOUT_DIRECTION_RTL
        root.setBackgroundColor(Abzar.RANG_ZAMIN)
        root.orientation = LinearLayout.VERTICAL

        val ghabeSar = LinearLayout(this)
        ghabeSar.orientation = LinearLayout.VERTICAL
        ghabeSar.setPadding(Abzar.dp(this, 18f), Abzar.dp(this, 20f), Abzar.dp(this, 18f), 0)
        val onvan = Abzar.tv(this, "دفترچهٔ امروز", 26f, Abzar.RANG_MATN)
        onvan.typeface = FontBox.vazirBlack(this)
        ghabeSar.addView(onvan)
        val tozih = Abzar.tv(this, "${Abzar.emrooz()} — روی ✖ بزنی ردیف پاک می‌شود", 13f, Abzar.RANG_KAMRANG)
        tozih.setPadding(0, Abzar.dp(this, 4f), 0, 0)
        ghabeSar.addView(tozih)
        root.addView(ghabeSar)

        val scroll = ScrollView(this)
        scroll.isFillViewport = true
        root.addView(scroll, LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        ))

        ghabe = LinearLayout(this)
        ghabe.orientation = LinearLayout.VERTICAL
        ghabe.setPadding(Abzar.dp(this, 18f), Abzar.dp(this, 12f), Abzar.dp(this, 18f), Abzar.dp(this, 22f))
        scroll.addView(ghabe, ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ))

        setContentView(root)
    }

    override fun onResume() {
        super.onResume()
        safheBekon()
    }

    private fun safheBekon() {
        ghabe.removeAllViews()
        val kelid = Abzar.kelidEmrooz()
        val vorodiHa = Khazide.vorodiHaRooz(this, kelid)

        val vohedHa = listOf("sohbat" to "🌅 صبحانه", "nahaar" to "🍽 ناهار", "sham" to "🌙 شام", "miani" to "🍎 میان‌وعده")
        for ((kod, matn) in vohedHa) {
            val vohedi = vorodiHa.filter { it.vohed == kod }
            ghabe.addView(kartVohed(matn, kod, vohedi))
        }

        // جمع کل
        val majmoo = Khazide.majmooRooz(this, kelid)
        val sozand = Khazide.varTamSozande(this, kelid)
        val hadaf = Khazide.hadafKalar(this)
        val baghi = hadaf - majmoo.kalar + sozand

        val kartJam = LinearLayout(this)
        kartJam.orientation = LinearLayout.VERTICAL
        kartJam.background = Abzar.kartGradient(this, Abzar.RANG_CODE, Abzar.RANG_KART, 18f)
        kartJam.setPadding(Abzar.dp(this, 16f), Abzar.dp(this, 14f), Abzar.dp(this, 16f), Abzar.dp(this, 14f))
        kartJam.addView(Abzar.tv(this, "جمع امروز", 16f, Abzar.RANG_MATN, bold = true))
        kartJam.addView(Abzar.tv(this, "🔥 کالری: ${Abzar.adadFarsi(majmoo.kalar)} از ${Abzar.adadFarsi(hadaf)}", 14f, Abzar.RANG_TALAEI, bold = true))
        kartJam.addView(Abzar.tv(this, "💪 پروتئین: ${Abzar.adadFarsi(majmoo.protein)} گرم | 🍞 کربوهیدرات: ${Abzar.adadFarsi(majmoo.karbohidrat)} گرم | 🥑 چربی: ${Abzar.adadFarsi(majmoo.charbi)} گرم", 13f, Abzar.RANG_KAMRANG))
        kartJam.addView(Abzar.tv(this, "➡️ باقی‌مانده: ${Abzar.adadFarsi(baghi)} کالری", 14f, if (baghi >= 0) Abzar.RANG_SABZ else Abzar.RANG_GHERMEZ, bold = true))
        val pJam = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pJam.topMargin = Abzar.dp(this, 16f)
        ghabe.addView(kartJam, pJam)

        // دکمهٔ افزودن
        val dokmeEzafe = Abzar.dokme(this, "＋ افزودن غذا", Abzar.RANG_SABZ, Abzar.RANG_ZAMIN)
        dokmeEzafe.setOnClickListener { startActivity(Intent(this, FoodsActivity::class.java)) }
        val pEzafe = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pEzafe.topMargin = Abzar.dp(this, 14f)
        ghabe.addView(dokmeEzafe, pEzafe)
    }

    private fun kartVohed(matnVohed: String, kod: String, vorodiHa: List<Varedi>): View {
        val kart = LinearLayout(this)
        kart.orientation = LinearLayout.VERTICAL
        kart.background = Abzar.kart(this, Abzar.RANG_KART, 18f)
        kart.setPadding(Abzar.dp(this, 14f), Abzar.dp(this, 12f), Abzar.dp(this, 14f), Abzar.dp(this, 12f))

        val sar = LinearLayout(this)
        sar.orientation = LinearLayout.HORIZONTAL
        sar.gravity = Gravity.CENTER_VERTICAL
        sar.addView(Abzar.tv(this, matnVohed, 15f, Abzar.RANG_SABZ, bold = true),
            LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))
        val jamVohed = vorodiHa.sumOf { it.kalar }
        sar.addView(Abzar.tv(this, "${Abzar.adadFarsi(jamVohed)} کالری", 13f, Abzar.RANG_KAMRANG))
        kart.addView(sar)

        if (vorodiHa.isEmpty()) {
            val khali = Abzar.tv(this, "هنوز چیزی ثبت نشده", 13f, Abzar.RANG_KHAKISTARI)
            khali.setPadding(0, Abzar.dp(this, 8f), 0, 0)
            kart.addView(khali)
        } else {
            for ((index, v) in vorodiHa.withIndex()) {
                val khat = LinearLayout(this)
                khat.orientation = LinearLayout.HORIZONTAL
                khat.gravity = Gravity.CENTER_VERTICAL
                val pKhat = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
                )
                pKhat.topMargin = Abzar.dp(this, 10f)

                val matn = Abzar.tv(
                    this,
                    "${v.esm} — ${Abzar.adadFarsi(v.gram)} گرم",
                    14f, Abzar.RANG_MATN
                )
                khat.addView(matn, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))
                khat.addView(Abzar.tv(this, "${Abzar.adadFarsi(v.kalar)}", 13f, Abzar.RANG_TALAEI, bold = true))

                // دکمهٔ حذف
                val hazf = TextView(this)
                hazf.text = " ✖ "
                hazf.setTextColor(Abzar.RANG_GHERMEZ)
                hazf.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 14f)
                hazf.isClickable = true
                Abzar.lams(hazf)
                val indexVaghei = Khazide.vorodiHaRooz(this, Abzar.kelidEmrooz()).indexOfFirst {
                    it.esm == v.esm && it.vohed == kod && it.gram == v.gram && it.kalar == v.kalar
                }
                hazf.setOnClickListener {
                    if (indexVaghei >= 0) {
                        Khazide.vorodiHazf(this, Abzar.kelidEmrooz(), indexVaghei)
                        safheBekon()
                    }
                }
                khat.addView(hazf)

                kart.addView(khat, pKhat)
            }
        }

        val pKart = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pKart.topMargin = Abzar.dp(this, 12f)
        kart.layoutParams = pKart
        return kart
    }
}
