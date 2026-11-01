package ir.anjoman.sandogh

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

/*
 * خواناک — کتابچهٔ ورق‌خور با انیمیشن سه‌بعدی
 * ۶ صفحه: شناسنامه / جدول اعضا با جست‌وجو / سهم و پس‌انداز / تسویهٔ ارز / تتر از سایرین / راهنما
 * ورق‌زدن با کشیدن انگشت (چپ↔راست) یا دکمه‌ها؛ ۴ تم از TemEnekhab
 */
class KhavanakActivity : Activity() {

    private lateinit var tem: Tem
    private lateinit var dane: DaneHa
    private lateinit var safheDan: FrameLayout
    private lateinit var matnOnvanSafhe: TextView
    private lateinit var matnShomareSafhe: TextView
    private lateinit var safDots: LinearLayout

    private var safheFaali = 0
    private var darHarekat = false

    private val onvanSafheha = listOf(
        "شناسنامهٔ صندوق", "تسویهٔ دلار اعضا", "سهم و پس‌انداز",
        "تسویهٔ ارز دیجیتال و دلار", "تتر خریداری‌شده از سایرین", "راهنمای کتابچه"
    )

    // قاب صفحه‌ها با تشخیص کشیدن انگشت — بدون قاپیدن لمسِ بچه‌ها
    inner class SafheDan(context: Context) : FrameLayout(context) {
        private var xAval = 0f
        private var yAval = 0f

        @SuppressLint("ClickableViewAccessibility")
        override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
            when (ev.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    xAval = ev.x
                    yAval = ev.y
                }
                MotionEvent.ACTION_UP -> {
                    val dX = ev.x - xAval
                    val dY = ev.y - yAval
                    if (!darHarekat && Math.abs(dX) > 130 && Math.abs(dX) > Math.abs(dY) * 1.4f) {
                        if (dX > 0) badi() else ghabli()
                    }
                }
            }
            return super.dispatchTouchEvent(ev)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TemEnekhab.rangVaredKon(this)
        tem = TemEnekhab.bekhon(this)
        dane = Ketabche.bekhon(this)
        safheFaali = intent.getIntExtra("safhe", 0).coerceIn(0, 5)

        val root = LinearLayout(this)
        root.layoutDirection = View.LAYOUT_DIRECTION_RTL
        root.orientation = LinearLayout.VERTICAL
        root.setBackgroundColor(tem.zamin)

        // نوار بالای کتاب
        val bar = LinearLayout(this)
        bar.orientation = LinearLayout.HORIZONTAL
        bar.gravity = Gravity.CENTER_VERTICAL
        bar.setPadding(Abzar.dp(this, 12f), Abzar.dp(this, 10f), Abzar.dp(this, 12f), Abzar.dp(this, 10f))
        bar.background = Abzar.kart(this, tem.kartTir, 0f)

        val dokmeGhabli = Abzar.tv(this, "قبلی ‹", 14f, tem.matn, bold = true)
        dokmeGhabli.setPadding(Abzar.dp(this, 14f), Abzar.dp(this, 8f), Abzar.dp(this, 14f), Abzar.dp(this, 8f))
        dokmeGhabli.background = Abzar.kartKhatei(this, tem.asli, 0, 12f)
        Abzar.lams(dokmeGhabli)
        dokmeGhabli.setOnClickListener { ghabli() }
        bar.addView(dokmeGhabli, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))

        val sotoonOnvan = LinearLayout(this)
        sotoonOnvan.orientation = LinearLayout.VERTICAL
        sotoonOnvan.gravity = Gravity.CENTER
        matnOnvanSafhe = Abzar.tv(this, onvanSafheha[safheFaali], 15f, tem.asli, bold = true)
        sotoonOnvan.addView(matnOnvanSafhe)
        matnShomareSafhe = Abzar.tv(this, "صفحهٔ ${Abzar.adadFarsi(safheFaali + 1)} از ${Abzar.adadFarsi(6)}", 11f, tem.kamrang)
        sotoonOnvan.addView(matnShomareSafhe)
        bar.addView(sotoonOnvan, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.6f))

        val dokmeBadi = Abzar.tv(this, "› بعدی", 14f, tem.matn, bold = true)
        dokmeBadi.setPadding(Abzar.dp(this, 14f), Abzar.dp(this, 8f), Abzar.dp(this, 14f), Abzar.dp(this, 8f))
        dokmeBadi.background = Abzar.kartGradient(this, tem.asli, tem.dovom, 12f)
        Abzar.lams(dokmeBadi)
        dokmeBadi.setOnClickListener { badi() }
        bar.addView(dokmeBadi, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))

        root.addView(bar)

        // نقطه‌های جای‌نما
        safDots = LinearLayout(this)
        safDots.orientation = LinearLayout.HORIZONTAL
        safDots.gravity = Gravity.CENTER
        safDots.setPadding(0, Abzar.dp(this, 8f), 0, Abzar.dp(this, 4f))
        root.addView(safDots)

        // قاب صفحه‌ها
        safheDan = SafheDan(this)
        root.addView(safheDan, LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f
        ))

        setContentView(root)
        dotsNeshanBedeh()
        safheBeKon(safheFaali, avali = true)
    }

    // ---------- ورق‌زدن ----------
    private fun badi() {
        if (safheFaali < 5) safheBeKon(safheFaali + 1, pish = true)
    }

    private fun ghabli() {
        if (safheFaali > 0) safheBeKon(safheFaali - 1, pish = false)
    }

    private fun safheBeKon(shomare: Int, pish: Boolean, avali: Boolean = false) {
        if (darHarekat) return
        darHarekat = true
        val no = sakhtSafhe(shomare)
        val kohane = if (safheDan.childCount > 0) safheDan.getChildAt(safheDan.childCount - 1) else null
        val pivotX = if (pish) safheDan.width.coerceAtLeast(1).toFloat() else 0f
        val pivotY = if (safheDan.height > 0) safheDan.height / 2f else 1100f

        fun tanzirKon() {
            safheFaali = shomare
            matnOnvanSafhe.text = onvanSafheha[shomare]
            matnShomareSafhe.text = "صفحهٔ ${Abzar.adadFarsi(shomare + 1)} از ${Abzar.adadFarsi(6)}"
            dotsNeshanBedeh()
            darHarekat = false
        }

        if (avali || kohane == null) {
            safheDan.addView(no, FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
            ))
            no.alpha = 0f
            no.animate().alpha(1f).setDuration(260).withEndAction { tanzirKon() }.start()
            return
        }

        val khoroji = Charkhesh3D(0f, if (pish) -90f else 90f, pivotX, pivotY)
        khoroji.duration = 300
        khoroji.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(a: Animation?) {}
            override fun onAnimationRepeat(a: Animation?) {}
            override fun onAnimationEnd(a: Animation?) {
                safheDan.removeAllViews()
                safheDan.addView(no, FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
                ))
                val voroodi = Charkhesh3D(if (pish) 90f else -90f, 0f, pivotX, pivotY)
                voroodi.duration = 300
                voroodi.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(a: Animation?) {}
                    override fun onAnimationRepeat(a: Animation?) {}
                    override fun onAnimationEnd(a: Animation?) { tanzirKon() }
                })
                no.startAnimation(voroodi)
            }
        })
        kohane.startAnimation(khoroji)
    }

    private fun dotsNeshanBedeh() {
        safDots.removeAllViews()
        for (i in 0 until 6) {
            val dot = View(this)
            dot.background = Abzar.dayereh(this, if (i == safheFaali) tem.asli else tem.kart)
            val p = LinearLayout.LayoutParams(Abzar.dp(this, 9f), Abzar.dp(this, 9f))
            p.marginStart = Abzar.dp(this, 4f)
            p.marginEnd = Abzar.dp(this, 4f)
            safDots.addView(dot, p)
        }
    }

    // ---------- سازندهٔ صفحه‌ها ----------
    private fun sakhtSafhe(shomare: Int): View {
        val scroll = ScrollView(this)
        scroll.isFillViewport = true
        val ghabe = LinearLayout(this)
        ghabe.orientation = LinearLayout.VERTICAL
        ghabe.setPadding(Abzar.dp(this, 14f), Abzar.dp(this, 12f), Abzar.dp(this, 14f), Abzar.dp(this, 24f))
        scroll.addView(ghabe)

        // سربرگ صفحه با نوار رنگی
        val sar = LinearLayout(this)
        sar.orientation = LinearLayout.VERTICAL
        sar.background = Abzar.kartGradient(this, tem.asli, tem.dovom, 18f)
        sar.setPadding(Abzar.dp(this, 16f), Abzar.dp(this, 12f), Abzar.dp(this, 16f), Abzar.dp(this, 12f))
        val matnSar = Abzar.tv(this, onvanSafheha[shomare], 19f, 0xFFFFFFFF.toInt(), bold = true)
        sar.addView(matnSar)
        val zirSar = Abzar.tv(
            this,
            when (shomare) {
                0 -> "جمع‌کل ستون‌های کتابچه — مهر ۱۴۰۵"
                1 -> "دلار هر عضو: کل، نقسیم‌شده و باقیمانده + بقیهٔ ارزها"
                2 -> "سهم درصدی از صندوق و پس‌انداز تومانی هر عضو"
                3 -> "مبلغ تسویه‌شدهٔ هر عضو از ارز دیجیتال و دلار"
                4 -> "یادداشت قرمز پای جدول کتابچه"
                else -> "این اعداد عین فایل PDF کتابچه است"
            },
            11f, 0xCCFFFFFF.toInt()
        )
        zirSar.setPadding(0, Abzar.dp(this, 3f), 0, 0)
        sar.addView(zirSar)
        ghabe.addView(sar, safP(6))

        if (dane.shakhs.isEmpty()) {
            val khali = Abzar.tv(this, "⚠️ دادهٔ کتابچه در این نسخه قرار نگرفته است.", 15f, tem.matn)
            khali.gravity = Gravity.CENTER
            val pKh = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
            pKh.topMargin = Abzar.dp(this, 40f)
            ghabe.addView(khali, pKh)
            return scroll
        }

        when (shomare) {
            0 -> safheShenasoname(ghabe)
            1 -> safheJadval(ghabe)
            2 -> safheSahmPasandaz(ghabe)
            3 -> safheTasvieArz(ghabe)
            4 -> safheTetr(ghabe)
            5 -> safheRahnama(ghabe)
        }

        // پانویس کتابی
        val payin = Abzar.tv(
            this,
            "— صفحهٔ ${Abzar.adadFarsi(shomare + 1)} از ${Abzar.adadFarsi(6)} —",
            11f, tem.kamrang
        )
        payin.gravity = Gravity.CENTER
        ghabe.addView(payin, safP(14))
        return scroll
    }

    // صفحهٔ ۰: شناسنامه — کارت‌های جمع‌کل
    private fun safheShenasoname(ghabe: LinearLayout) {
        val m = dane.majmoo
        if (m == null) return

        val khaneha = listOf(
            Triple("یورو", Abzar.adadFarsi(m.yuro), tem.asli),
            Triple("دلار", Abzar.adadFarsi(m.dolar), 0xFF38BDF8.toInt()),
            Triple("دلار نقسیم‌شده", Abzar.adadFarsi(m.naqsim), 0xFFFB923C.toInt()),
            Triple("دلار باقیمانده", Abzar.adadFarsi(m.baghimande), 0xFFFACC15.toInt()),
            Triple("تمام سکه", Abzar.adadFarsi(m.seke), 0xFFF2B134.toInt()),
            Triple("تتر", Abzar.adadFarsi(m.tetr), 0xFF4ADE80.toInt()),
            Triple("USDC", Abzar.adadFarsi(m.usdc), 0xFF2775CA.toInt()),
            Triple("دای", Abzar.adadFarsi(m.dai), 0xFFF58439.toInt()),
            Triple("تسویهٔ کل", Abzar.adadFarsi(m.tasvie), 0xFFE879F9.toInt()),
            Triple("سهم اعضا (٪)", Abzar.adadFarsi(m.sahm), tem.dovom)
        )

        var saf = LinearLayout(this)
        saf.orientation = LinearLayout.HORIZONTAL
        for ((i, k) in khaneha.withIndex()) {
            saf.addView(khaneAmar(k.first, k.second, k.third), LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))
            if (i % 2 == 1) {
                val p = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
                )
                p.topMargin = Abzar.dp(this, 10f)
                ghabe.addView(saf, p)
                saf = LinearLayout(this)
                saf.orientation = LinearLayout.HORIZONTAL
            }
        }
        // کارت بزرگ پس‌انداز
        val kartBozorg = LinearLayout(this)
        kartBozorg.orientation = LinearLayout.VERTICAL
        kartBozorg.gravity = Gravity.CENTER
        kartBozorg.background = Abzar.kartGradient(this, tem.asli, tem.dovom, 20f)
        kartBozorg.setPadding(Abzar.dp(this, 16f), Abzar.dp(this, 18f), Abzar.dp(this, 16f), Abzar.dp(this, 18f))
        val sarBozorg = Abzar.tv(this, "پس‌انداز صندوق (تومان)", 14f, 0xB3FFFFFF.toInt())
        sarBozorg.gravity = Gravity.CENTER
        kartBozorg.addView(sarBozorg)
        val megdarBozorg = Abzar.tv(this, Abzar.adadJoda(m.pasandaz), 30f, 0xFFFFFFFF.toInt(), bold = true)
        megdarBozorg.gravity = Gravity.CENTER
        megdarBozorg.typeface = FontBox.vazirBlack(this)
        kartBozorg.addView(megdarBozorg)
        val rizBozorg = Abzar.tv(this, "${Abzar.adadFarsi(dane.shakhs.size)} عضو — عین مجموع کتابچه", 12f, 0xCCFFFFFF.toInt())
        rizBozorg.gravity = Gravity.CENTER
        kartBozorg.addView(rizBozorg)
        val pBozorg = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pBozorg.topMargin = Abzar.dp(this, 12f)
        ghabe.addView(kartBozorg, pBozorg)
    }

    // صفحهٔ ۱: جدول اعضا با جست‌وجو
    private fun safheJadval(ghabe: LinearLayout) {
        // جست‌وجو
        val edt = EditText(this)
        edt.hint = "🔍 جست‌وجوی نام عضو…"
        edt.setTextColor(tem.matn)
        edt.setHintTextColor(tem.kamrang)
        edt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
        edt.typeface = FontBox.vazir(this)
        edt.background = Abzar.kart(this, tem.kart, 14f)
        edt.setPadding(Abzar.dp(this, 14f), Abzar.dp(this, 10f), Abzar.dp(this, 14f), Abzar.dp(this, 10f))
        ghabe.addView(edt, safP(8))

        // جدول افقی کامل
        val scrollAfoqi = android.widget.HorizontalScrollView(this)
        scrollAfoqi.isHorizontalScrollBarEnabled = false
        val jadval = LinearLayout(this)
        jadval.orientation = LinearLayout.VERTICAL
        scrollAfoqi.addView(jadval)
        ghabe.addView(scrollAfoqi, safP(8))

        fun besaz(filter: String) {
            jadval.removeAllViews()
            val sorat = dane.shakhs.filter { filter.isBlank() || it.esm.contains(filter) }

            // سرستون‌ها
            val titrha = listOf(
                "نام و نام خانوادگی", "دلار", "نقسیم‌شده", "باقی‌مانده",
                "یورو", "تمام سکه", "تتر", "USDC", "دای"
            )
            val safSar = LinearLayout(this)
            safSar.orientation = LinearLayout.HORIZONTAL
            for ((i, t) in titrha.withIndex()) {
                val k = khane(t, tem.asli, true, i == 0)
                k.background = Abzar.kart(this, tem.kartTir, 0f)
                safSar.addView(k)
            }
            jadval.addView(safSar)

            for ((d, s) in sorat.withIndex()) {
                val saf = LinearLayout(this)
                saf.orientation = LinearLayout.HORIZONTAL
                saf.background = if (d % 2 == 0) Abzar.kart(this, tem.kart, 0f)
                else Abzar.kart(this, tem.kartTir, 0f)
                saf.addView(khane(s.esm, tem.matn, true, true))
                saf.addView(khane(shomare(s.dolar), tem.matn, false, false))
                saf.addView(khane(shomare(s.naqsim), tem.matn, false, false))
                saf.addView(khane(shomare(s.baghimande), tem.matn, false, false))
                saf.addView(khane(shomare(s.yuro), tem.matn, false, false))
                saf.addView(khane(shomare(s.seke), tem.matn, false, false))
                saf.addView(khane(shomare(s.tetr), tem.matn, false, false))
                saf.addView(khane(shomare(s.usdc), tem.matn, false, false))
                saf.addView(khane(shomare(s.dai), tem.matn, false, false))
                jadval.addView(saf)
            }

            // ردیف مجموع
            val m = dane.majmoo
            if (m != null) {
                val safM = LinearLayout(this)
                safM.orientation = LinearLayout.HORIZONTAL
                safM.background = Abzar.kartGradient(this, tem.asli, tem.dovom, 0f)
                safM.addView(khane("مجموع", 0xFFFFFFFF.toInt(), true, true))
                safM.addView(khane(shomare(m.dolar), 0xFFFFFFFF.toInt(), true, false))
                safM.addView(khane(shomare(m.naqsim), 0xFFFFFFFF.toInt(), true, false))
                safM.addView(khane(shomare(m.baghimande), 0xFFFFFFFF.toInt(), true, false))
                safM.addView(khane(shomare(m.yuro), 0xFFFFFFFF.toInt(), true, false))
                safM.addView(khane(shomare(m.seke), 0xFFFFFFFF.toInt(), true, false))
                safM.addView(khane(shomare(m.tetr), 0xFFFFFFFF.toInt(), true, false))
                safM.addView(khane(shomare(m.usdc), 0xFFFFFFFF.toInt(), true, false))
                safM.addView(khane(shomare(m.dai), 0xFFFFFFFF.toInt(), true, false))
                jadval.addView(safM)
            }

            if (sorat.isEmpty()) {
                val khali = Abzar.tv(this, "چیزی پیدا نشد", 13f, tem.kamrang)
                khali.gravity = Gravity.CENTER
                jadval.addView(khali, safP(18))
            }
        }

        besaz("")
        edt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, a: Int, b: Int, c: Int) {}
            override fun onTextChanged(s: CharSequence?, a: Int, b: Int, c: Int) {}
            override fun afterTextChanged(s: Editable?) {
                besaz(s?.toString()?.trim() ?: "")
            }
        })
    }

    // صفحهٔ ۲: نمودار سهم + پس‌انداز
    private fun safheSahmPasandaz(ghabe: LinearLayout) {
        val rangha = listOf(
            0xFF2DD4BF.toInt(), 0xFF0EA5E9.toInt(), 0xFF22C55E.toInt(), 0xFFFACC15.toInt(),
            0xFFFB923C.toInt(), 0xFFEF4444.toInt(), 0xFFA78BFA.toInt(), 0xFF38BDF8.toInt(),
            0xFF4ADE80.toInt(), 0xFFE879F9.toInt(), 0xFFF97316.toInt(), 0xFF14B8A6.toInt()
        )

        val moratab = dane.shakhs.filter { it.sahm > 0 }.sortedByDescending { it.sahm }
        val dadeSahr = ArrayList<Pair<Double, Int>>()
        val ghabeLegend = LinearLayout(this)
        ghabeLegend.orientation = LinearLayout.VERTICAL
        for ((i, s) in moratab.withIndex()) {
            dadeSahr.add(s.sahm to rangha[i % rangha.size])
            val radif = LinearLayout(this)
            radif.orientation = LinearLayout.HORIZONTAL
            radif.gravity = Gravity.CENTER_VERTICAL
            val noghte = View(this)
            noghte.background = Abzar.dayereh(this, rangha[i % rangha.size])
            radif.addView(noghte, LinearLayout.LayoutParams(Abzar.dp(this, 10f), Abzar.dp(this, 10f)))
            val esm = Abzar.tv(this, "  ${s.esm}", 12f, tem.matn)
            radif.addView(esm, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))
            val sahm = Abzar.tv(this, "${Abzar.adadFarsi(s.sahm)}٪", 12f, tem.matn, bold = true)
            radif.addView(sahm)
            ghabeLegend.addView(radif, safP(3))
        }

        val sahr = SahrView(this)
        sahr.dadegan(dadeSahr)
        val pSahr = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, Abzar.dp(this, 230f)
        )
        pSahr.topMargin = Abzar.dp(this, 10f)
        ghabe.addView(sahr, pSahr)
        ghabe.addView(ghabeLegend, safP(8))

        // پس‌انداز تومانی اعضا — نوار نسبت به بیشترین
        val sarPasandaz = Abzar.tv(this, "💰 پس‌انداز (تومان)", 16f, tem.asli, bold = true)
        ghabe.addView(sarPasandaz, safP(14))
        val haddBala = dane.shakhs.maxOfOrNull { it.pasandaz } ?: 0L
        for (s in dane.shakhs.sortedByDescending { it.pasandaz }) {
            val saf = LinearLayout(this)
            saf.orientation = LinearLayout.HORIZONTAL
            saf.gravity = Gravity.CENTER_VERTICAL
            val esm = Abzar.tv(this, s.esm, 12f, tem.matn)
            saf.addView(esm, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.4f))
            val ghabeBar = LinearLayout(this)
            ghabeBar.orientation = LinearLayout.HORIZONTAL
            val bar = View(this)
            bar.background = Abzar.kartGradient(this, tem.asli, tem.dovom, 6f)
            val nosbat = if (haddBala > 0) s.pasandaz.toFloat() / haddBala else 0f
            val neshane = LinearLayout.LayoutParams(0, Abzar.dp(this, 12f), (nosbat * 10f).coerceAtLeast(if (s.pasandaz > 0) 0.4f else 0f))
            ghabeBar.addView(bar, neshane)
            val faazegi = LinearLayout.LayoutParams(0, Abzar.dp(this, 12f), (10f - nosbat * 10f))
            ghabeBar.addView(View(this), faazegi)
            saf.addView(ghabeBar, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 2f))
            val megdar = Abzar.tv(this, Abzar.adadJoda(s.pasandaz), 11f, tem.kamrang, bold = true)
            megdar.setPadding(Abzar.dp(this, 6f), 0, 0, 0)
            saf.addView(megdar)
            ghabe.addView(saf, safP(5))
        }
    }

    // صفحهٔ ۳: تسویهٔ ارز دیجیتال و دلار
    private fun safheTasvieArz(ghabe: LinearLayout) {
        val moratab = dane.shakhs.filter { it.tasvie > 0 }.sortedByDescending { it.tasvie }
        for (s in moratab) {
            val kart = LinearLayout(this)
            kart.orientation = LinearLayout.HORIZONTAL
            kart.gravity = Gravity.CENTER_VERTICAL
            kart.background = Abzar.kart(this, tem.kart, 16f)
            kart.setPadding(Abzar.dp(this, 14f), Abzar.dp(this, 10f), Abzar.dp(this, 14f), Abzar.dp(this, 10f))

            val sotoon = LinearLayout(this)
            sotoon.orientation = LinearLayout.VERTICAL
            sotoon.addView(Abzar.tv(this, s.esm, 14f, tem.matn, bold = true))
            val zir = if (s.tasvieShode != null && s.tasvieShode > 0)
                "تسویهٔ ارز دیجیتال و دلار: ${Abzar.adadFarsi(s.tasvieShode)}"
            else "تسویه از صندوق و دلار"
            val matnZir = Abzar.tv(this, zir, 11f, tem.kamrang)
            matnZir.setPadding(0, Abzar.dp(this, 2f), 0, 0)
            sotoon.addView(matnZir)
            kart.addView(sotoon, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))

            val megdar = Abzar.tv(this, Abzar.adadFarsi(s.tasvie), 17f, tem.asli, bold = true)
            kart.addView(megdar)
            ghabe.addView(kart, safP(7))
        }

        val m = dane.majmoo
        if (m != null) {
            val kartM = LinearLayout(this)
            kartM.orientation = LinearLayout.HORIZONTAL
            kartM.gravity = Gravity.CENTER
            kartM.background = Abzar.kartGradient(this, tem.asli, tem.dovom, 16f)
            kartM.setPadding(Abzar.dp(this, 14f), Abzar.dp(this, 12f), Abzar.dp(this, 14f), Abzar.dp(this, 12f))
            kartM.addView(Abzar.tv(this, "مجموع تسویه", 15f, 0xB3FFFFFF.toInt(), bold = true),
                LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))
            kartM.addView(Abzar.tv(this, Abzar.adadFarsi(m.tasvie), 18f, 0xFFFFFFFF.toInt(), bold = true))
            ghabe.addView(kartM, safP(10))
        }
    }

    // صفحهٔ ۴: تتر خریداری‌شده از سایرین
    private fun safheTetr(ghabe: LinearLayout) {
        val tozih = Abzar.tv(
            this,
            "این فهرست، یادداشت قرمزِ کنار جدول کتابچه است: تتری که از افراد دیگری خارج از صندوق خریداری شده.",
            13f, tem.kamrang
        )
        ghabe.addView(tozih, safP(8))

        for ((i, esm) in dane.tetrEsm.withIndex()) {
            val megdar = dane.tetrMegdar.getOrNull(i) ?: 0.0
            val kart = LinearLayout(this)
            kart.orientation = LinearLayout.HORIZONTAL
            kart.gravity = Gravity.CENTER_VERTICAL
            kart.background = Abzar.kartKhatei(this, 0xFFEF4444.toInt(), tem.kart, 16f)
            kart.setPadding(Abzar.dp(this, 14f), Abzar.dp(this, 12f), Abzar.dp(this, 14f), Abzar.dp(this, 12f))
            val neshane = Abzar.tv(this, "₮", 20f, 0xFFEF4444.toInt(), bold = true)
            kart.addView(neshane)
            val esmTv = Abzar.tv(this, "  $esm", 15f, tem.matn, bold = true)
            esmTv.typeface = FontBox.vazirBold(this)
            kart.addView(esmTv, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))
            kart.addView(Abzar.tv(this, Abzar.adadFarsi(megdar), 17f, tem.matn, bold = true))
            ghabe.addView(kart, safP(7))
        }

        val kartM = LinearLayout(this)
        kartM.orientation = LinearLayout.HORIZONTAL
        kartM.gravity = Gravity.CENTER
        kartM.background = Abzar.kartGradient(this, 0xFFEF4444.toInt(), 0xFFB91C1C.toInt(), 16f)
        kartM.setPadding(Abzar.dp(this, 14f), Abzar.dp(this, 12f), Abzar.dp(this, 14f), Abzar.dp(this, 12f))
        kartM.addView(Abzar.tv(this, "مجموع", 15f, 0xFFFFFFFF.toInt(), bold = true),
            LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))
        kartM.addView(Abzar.tv(this, Abzar.adadFarsi(dane.tetrMajmoo), 18f, 0xFFFFFFFF.toInt(), bold = true))
        ghabe.addView(kartM, safP(10))
    }

    // صفحهٔ ۵: راهنما
    private fun safheRahnama(ghabe: LinearLayout) {
        val bakhsha = listOf(
            "دلار — کل دلاری که هر عضو در صندوق داشته است.",
            "نقسیم‌شده — بخشی از دلار که تسویه/تقسیم شده. باقیمانده — چیزی که هنوز نرسیده به دست عضو. جمع این دو باید برابر کل دلار باشد (در خود PDF ممکن است یک سطر جابه‌جا نوشته شده باشد).",
            "تمام سکه — شمار سکهٔ تمام هر عضو. تتر — تتر نزد صندوق. USDC و دای — استیبل‌کوین‌های دیگر.",
            "سهم به درصد از صندوق — عین ستون کتابچه؛ جمعش ۱۰۰٪.",
            "پس‌انداز (تومان) — معادل تومانیِ حساب هر عضو در صندوق.",
            "تسویهٔ ارز دیجیتال و دلار — مبلغی که تا امروز برای هر عضو تسویه شده؛ ستون «تسویه شده» زیرمجموعهٔ آن است.",
            "تتر خریداری‌شده از سایرین — یادداشت قرمز پای جدول: تترهایی که از افراد بیرون از صندوق خریداری شده.",
            "تاریخ کتابچه: ${dane.tarikh} — اعداد بدون هیچ تغییری از فایل PDF اصلی منتقل شده‌اند."
        )
        for (b in bakhsha) {
            val kart = LinearLayout(this)
            kart.orientation = LinearLayout.HORIZONTAL
            kart.background = Abzar.kart(this, tem.kart, 14f)
            kart.setPadding(Abzar.dp(this, 12f), Abzar.dp(this, 10f), Abzar.dp(this, 12f), Abzar.dp(this, 10f))
            val noghte = Abzar.tv(this, "◈", 13f, tem.asli, bold = true)
            kart.addView(noghte)
            val matn = Abzar.tv(this, "  $b", 13f, tem.matn)
            kart.addView(matn)
            ghabe.addView(kart, safP(7))
        }
    }

    // ---------- ابزار صفحه ----------
    private fun shomare(v: Double): String =
        if (v == Math.floor(v) && !v.isInfinite()) Abzar.adadFarsi(v.toLong()) else Abzar.adadFarsi(v)

    private fun khane(matn: String, rang: Int, bold: Boolean, boland: Boolean): TextView {
        val t = TextView(this)
        t.text = matn
        t.setTextSize(TypedValue.COMPLEX_UNIT_SP, if (boland) 12.5f else 12f)
        t.setTextColor(rang)
        t.typeface = if (bold) FontBox.vazirBold(this) else FontBox.vazir(this)
        t.gravity = Gravity.CENTER
        t.setPadding(Abzar.dp(this, 8f), Abzar.dp(this, 8f), Abzar.dp(this, 8f), Abzar.dp(this, 8f))
        t.minWidth = Abzar.dp(this, if (boland) 150f else 86f)
        return t
    }

    private fun khaneAmar(onvan: String, megdar: String, rang: Int): View {
        val kart = LinearLayout(this)
        kart.orientation = LinearLayout.VERTICAL
        kart.gravity = Gravity.CENTER
        kart.background = Abzar.kart(this, tem.kart, 16f)
        kart.setPadding(Abzar.dp(this, 8f), Abzar.dp(this, 12f), Abzar.dp(this, 8f), Abzar.dp(this, 12f))
        val matnOnvan = Abzar.tv(this, onvan, 11f, tem.kamrang)
        matnOnvan.gravity = Gravity.CENTER
        kart.addView(matnOnvan)
        val matnMegdar = Abzar.tv(this, megdar, 18f, rang, bold = true)
        matnMegdar.gravity = Gravity.CENTER
        matnMegdar.typeface = FontBox.vazirBlack(this)
        matnMegdar.setPadding(0, Abzar.dp(this, 3f), 0, 0)
        kart.addView(matnMegdar)
        val pKart = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
        pKart.marginStart = Abzar.dp(this, 4f)
        pKart.marginEnd = Abzar.dp(this, 4f)
        kart.layoutParams = pKart
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
