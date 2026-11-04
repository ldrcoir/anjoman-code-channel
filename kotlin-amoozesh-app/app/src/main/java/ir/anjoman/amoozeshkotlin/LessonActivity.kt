package ir.anjoman.amoozeshkotlin

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.TextView

/*
 * صفحهٔ درس — بازطراحی کامل طبق خواستهٔ سردبیر:
 * «متن اسکرول نخوره، دکمهٔ بعد و قبل داشته باشه، بگراند طرح جالب بگذار»
 *
 * درس به چند صفحهٔ هم‌قد صفحه تقسیم می‌شود:
 *   صفحه ۱..آخر-۲ : توضیح‌ها (هر صفحه ۲ پاراگراف)
 *   صفحهٔ کد       : نمونه کد توی کارت تیره
 *   صفحهٔ آخر      : بهترین نمره + دکمهٔ آزمون
 * با دکمه‌های «قبلی/بعدی»، نقطه‌های موقعیت و کشیدن انگشت هم کار می‌کند.
 * پس‌زمینه: NagshZamin (گرادیان + نشانه‌های کد کم‌رنگ)
 */
class LessonActivity : Activity() {

    private lateinit var dars: Dars
    private lateinit var ghabeSaFe: FrameLayout
    private lateinit var ghabeNoqte: LinearLayout
    private lateinit var dokmeGhabli: TextView
    private lateinit var dokmeBadi: TextView
    private val saFeHa = mutableListOf<View>()
    private var saFeFeli = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Abzar.rangVaredKon(this)

        val shomareh = intent.getIntExtra("shomareh_dars", 1)
        dars = DarsHa.baShomareh(shomareh)

        val root = LinearLayout(this)
        root.orientation = LinearLayout.VERTICAL
        root.layoutDirection =
            if (Zaban.rastChin(this)) View.LAYOUT_DIRECTION_RTL else View.LAYOUT_DIRECTION_LTR
        root.background = NagshZamin(0xFF101A33.toInt(), 0xFF0B1220.toInt())

        // نوار بالا: درس x از n + عنوان
        val sarBar = LinearLayout(this)
        sarBar.orientation = LinearLayout.VERTICAL
        sarBar.setPadding(Abzar.dp(this, 20f), Abzar.dp(this, 20f), Abzar.dp(this, 20f), Abzar.dp(this, 10f))
        val matnShomareh = Abzar.tv(
            this,
            "${Zaban.t("dars")} ${Zaban.adad(dars.shomareh)} ${Zaban.t("az")} ${Zaban.adad(DarsHa.darsList.size)}",
            14f, Abzar.RANG_TALAEI, bold = true
        )
        sarBar.addView(matnShomareh)
        val onvan = Abzar.tv(this, dars.onvan, 24f, Abzar.RANG_MATN)
        onvan.typeface = FontBox.vazirBlack(this)
        onvan.setPadding(0, Abzar.dp(this, 4f), 0, 0)
        sarBar.addView(onvan)
        root.addView(
            sarBar, LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )

        // گهوارهٔ صفحه‌ها
        ghabeSaFe = FrameLayout(this)
        root.addView(
            ghabeSaFe, LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f
            )
        )

        // نوار پایین: دکمهٔ قبلی | نقطه‌ها | دکمهٔ بعدی
        val paeinBar = LinearLayout(this)
        paeinBar.orientation = LinearLayout.HORIZONTAL
        paeinBar.gravity = Gravity.CENTER_VERTICAL
        paeinBar.setPadding(Abzar.dp(this, 16f), Abzar.dp(this, 10f), Abzar.dp(this, 16f), Abzar.dp(this, 14f))

        val dokmeGh = Abzar.dokme(this, "‹ ${Zaban.t("ghabli")}", Abzar.RANG_TIZ, Abzar.RANG_ZAMIN)
        val pGh = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
        dokmeGh.setOnClickListener { rafteKon(-1) }
        paeinBar.addView(dokmeGh, pGh)

        ghabeNoqte = LinearLayout(this)
        ghabeNoqte.gravity = Gravity.CENTER
        val pNoqte = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0.8f
        )
        pNoqte.setMargins(Abzar.dp(this, 8f), 0, Abzar.dp(this, 8f), 0)
        paeinBar.addView(ghabeNoqte, pNoqte)

        val dokmeBa = Abzar.dokme(this, "${Zaban.t("badi")} ›", Abzar.RANG_TIZ, Abzar.RANG_ZAMIN)
        val pBa = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
        dokmeBa.setOnClickListener { rafteKon(1) }
        paeinBar.addView(dokmeBa, pBa)
        dokmeGhabli = dokmeGh
        dokmeBadi = dokmeBa

        root.addView(
            paeinBar, LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )

        setContentView(root)

        // ساخت صفحه‌ها
        saFeBekon()

        // کشیدن انگشت هم صفحه را عوض می‌کند
        ghabeSaFe.setOnTouchListener(object : View.OnTouchListener {
            private var shorooX = 0f
            private var shorooY = 0f
            override fun onTouch(v: View, e: MotionEvent): Boolean {
                when (e.action) {
                    MotionEvent.ACTION_DOWN -> { shorooX = e.x; shorooY = e.y }
                    MotionEvent.ACTION_UP -> {
                        val dx = e.x - shorooX
                        val dy = e.y - shorooY
                        if (Math.abs(dx) > Abzar.dp(this@LessonActivity, 60f) &&
                            Math.abs(dx) > Math.abs(dy) * 1.5f
                        ) {
                            // RTL: کشیدن به سمت چپ = بعدی
                            val jahat = if (Zaban.rastChin(this@LessonActivity)) (if (dx < 0) 1 else -1)
                                        else (if (dx > 0) 1 else -1)
                            rafteKon(jahat)
                        }
                    }
                }
                return true
            }
        })

        saFeNeshanBedeh(0, jelo = true)
    }

    // ---------- ساخت صفحه‌های محتوا ----------
    private fun saFeBekon() {
        saFeHa.clear()
        ghabeSaFe.removeAllViews()

        // توضیح‌ها: هر صفحه دو پاراگراف تا در صفحه جا شود
        var i = 0
        while (i < dars.tozihat.size) {
            val bite = dars.tozihat.subList(i, Math.min(i + 2, dars.tozihat.size))
            saFeHa.add(saFeMatn(bite))
            i += 2
        }
        // صفحهٔ کد
        saFeHa.add(saFeKod())
        // صفحهٔ آزمون
        saFeHa.add(saFeAzmoon())

        for (s in saFeHa) {
            s.visibility = View.GONE
            ghabeSaFe.addView(
                s, FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
                )
            )
        }

        // نقطه‌های موقعیت
        ghabeNoqte.removeAllViews()
        for (j in saFeHa.indices) {
            val noqte = View(this)
            noqte.background = Abzar.dayereh(this, Abzar.RANG_KAMRANG)
            val p = LinearLayout.LayoutParams(Abzar.dp(this, 8f), Abzar.dp(this, 8f))
            p.setMargins(Abzar.dp(this, 4f), 0, Abzar.dp(this, 4f), 0)
            ghabeNoqte.addView(noqte, p)
        }
    }

    private fun saFeMatn(paragrafHa: List<String>): View {
        val ghabe = LinearLayout(this)
        ghabe.orientation = LinearLayout.VERTICAL
        ghabe.setPadding(Abzar.dp(this, 20f), Abzar.dp(this, 6f), Abzar.dp(this, 20f), 0)
        for (p in paragrafHa) {
            val t = Abzar.tv(this, p, 16f, Abzar.RANG_MATN)
            t.setLineSpacing(Abzar.dp(this, 5f).toFloat(), 1f)
            t.setPadding(0, Abzar.dp(this, 12f), 0, 0)
            ghabe.addView(t)
        }
        return ghabe
    }

    private fun saFeKod(): View {
        val ghabe = LinearLayout(this)
        ghabe.orientation = LinearLayout.VERTICAL
        ghabe.setPadding(Abzar.dp(this, 20f), Abzar.dp(this, 6f), Abzar.dp(this, 20f), 0)

        val sarNevesht = Abzar.tv(
            this,
            if (Zaban.bekhon(this@LessonActivity) == Zaban.Z.FA) "نمونهٔ کد — با دقت بخون" else "Code sample",
            14f, Abzar.RANG_TALAEI, bold = true
        )
        ghabe.addView(sarNevesht)

        val kart = LinearLayout(this)
        kart.orientation = LinearLayout.VERTICAL
        kart.background = Abzar.kart(this, Abzar.RANG_CODE, 12f)
        val pad = Abzar.dp(this, 16f)
        kart.setPadding(pad, pad, pad, pad)
        val pKart = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pKart.topMargin = Abzar.dp(this, 12f)
        ghabe.addView(kart, pKart)

        // کد بدون اسکرول عمودی؛ افقی هم تا جای ممکن جا شود
        val ghabeKod = HorizontalScrollView(this)
        ghabeKod.isHorizontalScrollBarEnabled = false
        val matnKod = TextView(this)
        matnKod.text = dars.codeNemooneh
        matnKod.typeface = Typeface.MONOSPACE
        matnKod.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
        matnKod.setTextColor(Abzar.RANG_MATN)
        matnKod.textDirection = View.TEXT_DIRECTION_LTR
        matnKod.gravity = Gravity.START
        matnKod.setLineSpacing(Abzar.dp(this, 4f).toFloat(), 1f)
        ghabeKod.addView(
            matnKod, FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )
        kart.addView(ghabeKod)
        return ghabe
    }

    private fun saFeAzmoon(): View {
        val ghabe = LinearLayout(this)
        ghabe.orientation = LinearLayout.VERTICAL
        ghabe.gravity = Gravity.CENTER_HORIZONTAL
        ghabe.setPadding(Abzar.dp(this, 20f), Abzar.dp(this, 16f), Abzar.dp(this, 20f), 0)

        val behtarin = Zakhire.behtarin(this, dars.shomareh)
        val matnHalat = if (behtarin >= 0) {
            "${Zaban.t("behtarin_natije")}: ${Zaban.adad(behtarin)} ${Zaban.t("az")} ${Zaban.adad(3)}"
        } else {
            Zaban.t("hanuz_azmoon")
        }
        val halat = Abzar.tv(this, matnHalat, 15f, Abzar.RANG_KAMRANG)
        ghabe.addView(halat)

        val dokmeAzmoon = Abzar.dokme(this, Zaban.t("shoroo_azmoon"), Abzar.RANG_SABZ, Abzar.RANG_ZAMIN)
        dokmeAzmoon.setOnClickListener {
            val niat = Intent(this, QuizActivity::class.java)
            niat.putExtra("shomareh_dars", dars.shomareh)
            startActivity(niat)
        }
        val pA = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pA.topMargin = Abzar.dp(this, 16f)
        ghabe.addView(dokmeAzmoon, pA)

        val dokmeBargasht = Abzar.dokme(
            this, Zaban.t("bargasht_dars"), Abzar.RANG_TIZ, Abzar.RANG_TIZ, khateDor = true
        )
        dokmeBargasht.setOnClickListener { finish() }
        val pB = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pB.topMargin = Abzar.dp(this, 10f)
        ghabe.addView(dokmeBargasht, pB)
        return ghabe
    }

    // ---------- جابه‌جایی بین صفحه‌ها ----------
    private fun rafteKon(jahat: Int) {
        val jadid = saFeFeli + jahat
        if (jadid < 0 || jadid >= saFeHa.size) return
        saFeNeshanBedeh(jadid, jelo = jahat > 0)
    }

    private fun saFeNeshanBedeh(index: Int, jelo: Boolean) {
        val ghabel = saFeHa[saFeFeli]
        if (ghabel !== saFeHa[index]) {
            ghabel.visibility = View.GONE
        }
        saFeFeli = index
        val saFe = saFeHa[index]
        saFe.visibility = View.VISIBLE
        // انیمیشن نرم ورود
        val jahatX = (if (jelo) 60f else -60f) * (if (Zaban.rastChin(this)) 1f else -1f)
        val anim = TranslateAnimation(jahatX, 0f, 0f, 0f)
        anim.duration = 220
        saFe.startAnimation(anim)

        // نقطه‌ها: صفحهٔ فعلی درشت و روشن
        for (j in 0 until ghabeNoqte.childCount) {
            val noqte = ghabeNoqte.getChildAt(j)
            val gharde = if (j == index) 10f else 7f
            noqte.background = Abzar.dayereh(
                this, if (j == index) Abzar.RANG_TALAEI else 0x55FFFFFF
            )
            val p = noqte.layoutParams as LinearLayout.LayoutParams
            p.width = Abzar.dp(this, gharde)
            p.height = Abzar.dp(this, gharde)
            noqte.layoutParams = p
        }

        // دکمه‌ها در لبه‌ها کم‌رنگ می‌شوند
        dokmeGhabli.alpha = if (index == 0) 0.35f else 1f
        dokmeBadi.alpha = if (index == saFeHa.size - 1) 0.35f else 1f
    }
}
