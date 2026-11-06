package ir.anjoman.kalaryar

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

/*
 * گزارش هفته: نمودار ۷ روز کالری، میانگین، استریک و تحلیل هوشمند
 */
class GozareshActivity : Activity() {

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

        val onvan = Abzar.tv(this, "📊 گزارش هفته", 26f, Abzar.RANG_MATN)
        onvan.typeface = FontBox.vazirBlack(this)
        ghabe.addView(onvan)

        val tozih = Abzar.tv(this, "۷ روز اخیر — خط طلایی همان هدف تو است", 13f, Abzar.RANG_KAMRANG)
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

        // داده‌های ۷ روز: از ۶ روز قبل تا امروز
        val meghdarHa = ArrayList<Int>()
        var roozHayeSabtShode = 0
        for (pas in 6 downTo 0) {
            val k = Khazide.majmooRooz(this, Abzar.kelidRooz(pas)).kalar
            if (k > 0) roozHayeSabtShode++
            meghdarHa.add(k)
        }
        val hadaf = Khazide.hadafKalar(this)

        // نمودار
        val kartChart = LinearLayout(this)
        kartChart.orientation = LinearLayout.VERTICAL
        kartChart.background = Abzar.kart(this, Abzar.RANG_KART, 18f)
        kartChart.setPadding(Abzar.dp(this, 12f), Abzar.dp(this, 12f), Abzar.dp(this, 12f), Abzar.dp(this, 8f))
        val chart = SotoonChart(this)
        chart.meghdarHa = meghdarHa
        chart.hadaf = hadaf
        chart.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, Abzar.dp(this, 210f)
        )
        kartChart.addView(chart)
        val pChart = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pChart.topMargin = Abzar.dp(this, 14f)
        ghabe.addView(kartChart, pChart)

        // راهنمای رنگ‌ها
        val rahnama = Abzar.tv(
            this,
            "🟢 نزدیک یا زیر هدف  🟡 کمی بیشتر  🔴 خیلی بیشتر  ⬜ ثبت نشده",
            12f, Abzar.RANG_KAMRANG
        )
        val pRahnama = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pRahnama.topMargin = Abzar.dp(this, 8f)
        ghabe.addView(rahnama, pRahnama)

        // آمار هفته
        val sabtShodeHa = meghdarHa.filter { it > 0 }
        val miangin = if (sabtShodeHa.isNotEmpty()) sabtShodeHa.sum() / sabtShodeHa.size else 0
        val estric = Khazide.estrik(this)

        val kartAmar = LinearLayout(this)
        kartAmar.orientation = LinearLayout.HORIZONTAL
        kartAmar.background = Abzar.kart(this, Abzar.RANG_KART, 18f)
        kartAmar.setPadding(Abzar.dp(this, 10f), Abzar.dp(this, 14f), Abzar.dp(this, 10f), Abzar.dp(this, 14f))

        kartAmar.addView(hoosh("میانگین روز", "${Abzar.adadFarsi(miangin)}"))
        kartAmar.addView(hoosh("هدف روز", "${Abzar.adadFarsi(hadaf)}"))
        kartAmar.addView(hoosh("روزهای ثبت", "${Abzar.adadFarsi(roozHayeSabtShode)} از ۷"))
        kartAmar.addView(hoosh("استریک", "${Abzar.adadFarsi(estric)} روز"))

        val pAmar = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pAmar.topMargin = Abzar.dp(this, 12f)
        ghabe.addView(kartAmar, pAmar)

        // تحلیل هوشمند هفته
        ghabe.addView(jaFazol(14))
        ghabe.addView(Abzar.tv(this, "🤖 تحلیل دستیار هوشمند", 16f, Abzar.RANG_SABZ, bold = true))

        val kartTahlil = LinearLayout(this)
        kartTahlil.orientation = LinearLayout.VERTICAL
        kartTahlil.background = Abzar.kartGradient(this, 0xFF123326.toInt(), Abzar.RANG_KART, 18f)
        kartTahlil.setPadding(Abzar.dp(this, 16f), Abzar.dp(this, 14f), Abzar.dp(this, 16f), Abzar.dp(this, 14f))

        if (sabtShodeHa.isEmpty()) {
            kartTahlil.addView(Abzar.tv(this, "هنوز داده‌ای برای هفته ندارم؛ چند روز غذا ثبت کن تا تحلیل واقعی بگیری.", 14f, Abzar.RANG_MATN))
        } else {
            // مقایسهٔ میانگین با هدف
            if (miangin > hadaf + 150) {
                kartTahlil.addView(Abzar.tv(this, "📈 میانگین کالری هفته ${Abzar.adadFarsi(miangin - hadaf)} کالری بالاتر از هدف است. برای کاهش پایدار وزن، این فاصله باید کم شود؛ با حذف یک میان‌وعدهٔ قندی شروع کن.", 14f, Abzar.RANG_MATN))
            } else if (miangin < hadaf - 350) {
                kartTahlil.addView(Abzar.tv(this, "📉 میانگین هفته ${Abzar.adadFarsi(hadaf - miangin)} کالری زیر هدف است. کاهش خیلی سریع، عضله را هم می‌سوزاند؛ کمی بیشتر بخور تا مسیر سالم بماند.", 14f, Abzar.RANG_MATN))
            } else {
                kartTahlil.addView(Abzar.tv(this, "✅ میانگین هفته دقیقاً در محدودهٔ سالم هدف است؛ همین ریتم را حفظ کن!", 14f, Abzar.RANG_MATN))
            }

            // پیوستگی
            if (estric >= 3) {
                kartTahlil.addView(Abzar.tv(this, "🔥 ${Abzar.adadFarsi(estric)} روز پیوسته ثبت کرده‌ای؛ بهترین عادت ممکن. کالری‌شماری وقتی جواب می‌دهد که پیوسته باشد.", 14f, Abzar.RANG_MATN))
            } else {
                kartTahlil.addView(Abzar.tv(this, "📌 ثبت‌هایت پراکنده است؛ ۳ روز پیوسته ثبت کن تا الگوی واقعی خوردنت معلوم شود.", 14f, Abzar.RANG_MATN))
            }

            // توصیهٔ عمومی هوشمند
            val nasih = HoshmandYar.nasihHaRooz(this)
            if (nasih.isNotEmpty() && nasih[0] != "امروز در همهٔ ردیف‌ها خوب پیش می‌روی؛ ادامه بده! 🌟") {
                kartTahlil.addView(Abzar.tv(this, "• ${nasih[0]}", 14f, Abzar.RANG_TALAEI))
            }
        }

        val pTahlil = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pTahlil.topMargin = Abzar.dp(this, 10f)
        ghabe.addView(kartTahlil, pTahlil)

        // یادداشت علمی
        val elemdan = Abzar.tv(
            this,
            "نکتهٔ علمی: ۷۷۰۰ کالری تقریباً برابر یک کیلوگرم چربی است؛ کسری روزانهٔ ۵۰۰ کالری یعنی حدود نیم کیلو کاهش در هفته — سرعتی که عضله‌ات را حفظ می‌کند.",
            12f, Abzar.RANG_KAMRANG
        )
        val pElemdan = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pElemdan.topMargin = Abzar.dp(this, 18f)
        ghabe.addView(elemdan, pElemdan)
    }

    private fun hoosh(matn: String, meghdar: String): LinearLayout {
        val s = LinearLayout(this)
        s.orientation = LinearLayout.VERTICAL
        s.gravity = android.view.Gravity.CENTER
        s.setPadding(Abzar.dp(this, 8f), 0, Abzar.dp(this, 8f), 0)
        s.layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
        val m1 = Abzar.tv(this, meghdar, 17f, Abzar.RANG_TALAEI, bold = true)
        m1.gravity = android.view.Gravity.CENTER
        s.addView(m1)
        val m2 = Abzar.tv(this, matn, 11f, Abzar.RANG_KAMRANG)
        m2.gravity = android.view.Gravity.CENTER
        s.addView(m2)
        return s
    }

    private fun jaFazol(dp: Int): View {
        val v = View(this)
        v.layoutParams = LinearLayout.LayoutParams(1, Abzar.dp(this, dp.toFloat()))
        return v
    }
}
