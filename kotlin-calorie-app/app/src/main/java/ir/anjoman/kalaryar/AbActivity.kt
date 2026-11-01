package ir.anjoman.kalaryar

import android.app.Activity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

/*
 * آب‌شمار: هدف بر اساس وزن (هر کیلو ۳۵ میلی‌لیتر)
 * هر لیوان ۲۵۰ میلی‌لیتر؛ با + و − ثبت می‌شود
 */
class AbActivity : Activity() {

    private lateinit var ghabe: LinearLayout
    private lateinit var halghe: HalgheView
    private lateinit var matnTedad: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Abzar.rangVaredKon(this)

        val root = ScrollView(this)
        root.layoutDirection = View.LAYOUT_DIRECTION_RTL
        root.setBackgroundColor(Abzar.RANG_ZAMIN)
        root.isFillViewport = true

        ghabe = LinearLayout(this)
        ghabe.orientation = LinearLayout.VERTICAL
        ghabe.gravity = Gravity.CENTER_HORIZONTAL
        ghabe.setPadding(Abzar.dp(this, 18f), Abzar.dp(this, 24f), Abzar.dp(this, 18f), Abzar.dp(this, 24f))
        root.addView(ghabe, ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ))

        val onvan = Abzar.tv(this, "💧 آب‌شمار", 26f, Abzar.RANG_MATN)
        onvan.typeface = FontBox.vazirBlack(this)
        ghabe.addView(onvan)

        val tozih = Abzar.tv(
            this,
            "آب یعنی سوخت‌وساز بهتر، اشتها کمتر و پوست بهتر. هدف تو بر اساس وزنت حساب شده است.",
            13f, Abzar.RANG_KAMRANG
        )
        tozih.setPadding(0, Abzar.dp(this, 8f), 0, 0)
        tozih.gravity = Gravity.CENTER
        ghabe.addView(tozih)

        // حلقهٔ بزرگ
        halghe = HalgheView(this)
        val pHalghe = LinearLayout.LayoutParams(Abzar.dp(this, 230f), Abzar.dp(this, 230f))
        pHalghe.topMargin = Abzar.dp(this, 22f)
        halghe.rangAval = Abzar.RANG_ABI
        halghe.rangDovom = 0xFF0284C7.toInt()
        ghabe.addView(halghe, pHalghe)

        matnTedad = Abzar.tv(this, "", 16f, Abzar.RANG_MATN, bold = true)
        matnTedad.setPadding(0, Abzar.dp(this, 10f), 0, 0)
        ghabe.addView(matnTedad)

        // دکمه‌ها
        val safDokme = LinearLayout(this)
        safDokme.orientation = LinearLayout.HORIZONTAL
        safDokme.gravity = Gravity.CENTER

        val dokmeKam = Abzar.dokme(this, "−  حذف لیوان", Abzar.RANG_KHAKISTARI, Abzar.RANG_MATN, khateDor = true)
        dokmeKam.setOnClickListener {
            Khazide.abTaghir(this, Abzar.kelidEmrooz(), -1)
            bekoon()
        }
        safDokme.addView(dokmeKam, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))

        val dokmeEzafe = Abzar.dokme(this, "＋  یک لیوان (۲۵۰ میلی)", Abzar.RANG_ABI, Abzar.RANG_ZAMIN)
        dokmeEzafe.setOnClickListener {
            Khazide.abTaghir(this, Abzar.kelidEmrooz(), 1)
            bekoon()
        }
        val pEzafe = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
        pEzafe.marginStart = Abzar.dp(this, 10f)
        safDokme.addView(dokmeEzafe, pEzafe)

        val pSafDokme = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pSafDokme.topMargin = Abzar.dp(this, 22f)
        ghabe.addView(safDokme, pSafDokme)

        // نکته‌های آب
        val kartNasih = LinearLayout(this)
        kartNasih.orientation = LinearLayout.VERTICAL
        kartNasih.background = Abzar.kart(this, Abzar.RANG_KART, 18f)
        kartNasih.setPadding(Abzar.dp(this, 16f), Abzar.dp(this, 14f), Abzar.dp(this, 16f), Abzar.dp(this, 14f))
        kartNasih.addView(Abzar.tv(this, "چرا آب؟", 15f, Abzar.RANG_ABI, bold = true))
        kartNasih.addView(Abzar.tv(this, "• گاهی بدن تشنگی را گرسنگی می‌فهمد؛ قبل از میان‌وعده یک لیوان بنوش", 13f, Abzar.RANG_MATN))
        kartNasih.addView(Abzar.tv(this, "• هر کیلو وزن، ۳۵ میلی‌لیتر آب در روز می‌خواهد", 13f, Abzar.RANG_MATN))
        kartNasih.addView(Abzar.tv(this, "• آب سرد سوخت‌وساز را کمی بیشتر می‌کند", 13f, Abzar.RANG_MATN))
        val pNasih = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pNasih.topMargin = Abzar.dp(this, 22f)
        ghabe.addView(kartNasih, pNasih)

        setContentView(root)
        bekoon()
    }

    private fun bekoon() {
        val ab = Khazide.abRooz(this, Abzar.kelidEmrooz())
        val hadaf = Khazide.hadafAb(this)
        halghe.pishraft = ab.toFloat() / hadaf.coerceAtLeast(1)
        halghe.matnVasat = "${Abzar.adadFarsi(ab)}/${Abzar.adadFarsi(hadaf)}"
        halghe.matnZir = "لیوان آب"
        val mandeh = (hadaf - ab).coerceAtLeast(0)
        matnTedad.text = if (mandeh == 0) "هدف آب را پر کردی؛ آفرین! 🌊" else "${Abzar.adadFarsi(mandeh)} لیوان دیگر تا هدف"
    }
}
