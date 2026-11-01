package ir.anjoman.pajoohesh

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import kotlin.math.sin

/*
 * جلد کتاب — صفحهٔ نخست با جلد واقعی کتاب، پس‌زمینهٔ متحرک و منوی اصلی
 */
class SplashActivity : Activity() {

    private lateinit var tem: Tem

    // پس‌زمینهٔ متحرک — حلقه‌های شناور ملایم
    class DriftView(context: Context, rangAsli: Int) : View(context) {
        private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        private var zaman = 0f

        private val mahal = listOf(
            Triple(0.12f, 0.18f, 110f), Triple(0.88f, 0.12f, 90f),
            Triple(0.15f, 0.72f, 130f), Triple(0.85f, 0.80f, 100f),
            Triple(0.50f, 0.05f, 70f), Triple(0.70f, 0.45f, 60f)
        )

        init {
            paint.color = rangAsli
            paint.alpha = 26
        }

        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)
            zaman += 0.016f
            val w = width.coerceAtLeast(1).toFloat()
            val h = height.coerceAtLeast(1).toFloat()
            for ((i, m) in mahal.withIndex()) {
                val jax = sin(zaman * (0.5f + i * 0.13f) + i * 1.7f) * w * 0.05f
                val jay = sin(zaman * (0.4f + i * 0.11f) + i * 2.3f) * h * 0.03f
                canvas.drawCircle(
                    m.first * w + jax,
                    m.second * h + jay,
                    m.third * (1f + 0.08f * sin(zaman * 0.6f + i)),
                    paint
                )
            }
            postInvalidateDelayed(33)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TemEnekhab.rangVaredKon(this)
        tem = TemEnekhab.bekhon(this)
        val mojojod = Ketab.mojojod(this)

        val root = FrameLayout(this)
        root.layoutDirection = View.LAYOUT_DIRECTION_RTL
        root.setBackgroundColor(tem.zamin)
        root.addView(DriftView(this, tem.asli))

        val scroll = ScrollView(this)
        scroll.isVerticalScrollBarEnabled = false
        val ghabe = LinearLayout(this)
        ghabe.orientation = LinearLayout.VERTICAL
        ghabe.setPadding(Abzar.dp(this, 22f), Abzar.dp(this, 24f), Abzar.dp(this, 22f), Abzar.dp(this, 24f))
        scroll.addView(ghabe)
        root.addView(scroll, FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        ))

        // ---------- سربرگ ----------
        val onvan = Abzar.tv(this, "دستاوردهای پژوهشی", 27f, tem.matn)
        onvan.typeface = FontBox.vazirBlack(this)
        ghabe.addView(onvan)

        val zirOnvan = Abzar.tv(
            this,
            "دانش‌آموزان و هنرجویان پژوهشگر و فناور برگزیده — هفتهٔ پژوهش ۱۴۰۴",
            12.5f, tem.kamrang
        )
        ghabe.addView(zirOnvan)
        ghabe.addView(Abzar.nobarZiba(this, tem.asli, tem.dovom, 3), safP(10))

        // ---------- جلد کتاب ----------
        val kartJeld = LinearLayout(this)
        kartJeld.orientation = LinearLayout.VERTICAL
        kartJeld.gravity = android.view.Gravity.CENTER
        kartJeld.background = Abzar.kart(this, 0xFFFFFFFF.toInt(), 22f)
        kartJeld.foreground = Abzar.kartKhatei(this, tem.asli, 0, 22f, 1.6f)
        kartJeld.setPadding(Abzar.dp(this, 8f), Abzar.dp(this, 8f), Abzar.dp(this, 8f), Abzar.dp(this, 8f))

        if (mojojod) {
            val tasvir = ImageView(this)
            tasvir.scaleType = ImageView.ScaleType.FIT_CENTER
            val b = Ketab.jeld(this)
            if (b != null) tasvir.setImageBitmap(b)
            kartJeld.addView(tasvir, LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, Abzar.dp(this, 400f)
            ))
            kartJeld.setOnClickListener {
                startActivity(Intent(this, KhavanakActivity::class.java).putExtra("safhe", 1))
            }
            Abzar.lams(kartJeld)
        } else {
            val hoshdar = Abzar.tv(
                this,
                "⚠️ محتوای کتاب در این نسخه قرار نگرفته است",
                15f, tem.kamrang
            )
            hoshdar.gravity = android.view.Gravity.CENTER
            kartJeld.addView(hoshdar, LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, Abzar.dp(this, 200f)
            ))
        }
        ghabe.addView(kartJeld, safP(6))

        // ---------- دکمهٔ اصلی ----------
        val safheAkhar = Ketab.safheAkharBekhon(this)
        val matnDokme = if (mojojod && safheAkhar > 1) "📖  ادامهٔ خواندن — صفحهٔ ${Abzar.adadFarsi(safheAkhar + 1)}"
        else "📖  گشودن کتاب"
        val dokmeAsli = Abzar.dokme(this, matnDokme, tem.asli, 0xFFFFFFFF.toInt())
        dokmeAsli.background = Abzar.kartGradient(this, tem.asli, tem.dovom, 18f)
        dokmeAsli.setOnClickListener {
            if (!mojojod) {
                Toast.makeText(this, "محتوا در این نسخه نیست", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            startActivity(Intent(this, KhavanakActivity::class.java).putExtra("safhe", safheAkhar))
        }
        ghabe.addView(dokmeAsli, safP(14))

        // ---------- منوی اصلی ۲×۲ ----------
        val menu = listOf(
            Triple("📑", "فهرست مطالب", { startActivity(Intent(this, FehrestActivity::class.java)) }),
            Triple("🔍", "جست‌وجو در کتاب", { startActivity(Intent(this, JustoJoActivity::class.java)) }),
            Triple("🎨", "تم‌ها", { startActivity(Intent(this, TemHaActivity::class.java)) }),
            Triple("ℹ️", "دربارهٔ کتاب", { startActivity(Intent(this, DarbareActivity::class.java)) })
        )
        var saf = LinearLayout(this)
        saf.orientation = LinearLayout.HORIZONTAL
        for ((i, m) in menu.withIndex()) {
            val dokme = LinearLayout(this)
            dokme.orientation = LinearLayout.VERTICAL
            dokme.gravity = android.view.Gravity.CENTER
            dokme.background = Abzar.kart(this, tem.kart, 18f)
            dokme.setPadding(Abzar.dp(this, 8f), Abzar.dp(this, 12f), Abzar.dp(this, 8f), Abzar.dp(this, 12f))
            dokme.isClickable = true
            Abzar.lams(dokme)
            val neshane = Abzar.tv(this, m.first, 24f, tem.asli)
            neshane.gravity = android.view.Gravity.CENTER
            dokme.addView(neshane)
            val esm = Abzar.tv(this, m.second, 12.5f, tem.matn, bold = true)
            esm.gravity = android.view.Gravity.CENTER
            dokme.addView(esm)
            val pK = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
            pK.marginStart = Abzar.dp(this, 4f)
            pK.marginEnd = Abzar.dp(this, 4f)
            saf.addView(dokme, pK)
            dokme.setOnClickListener { m.third() }
            if (i % 2 == 1) {
                ghabe.addView(saf, safP(10))
                saf = LinearLayout(this)
                saf.orientation = LinearLayout.HORIZONTAL
            }
        }

        // ---------- خط پایانی ----------
        val payin = Abzar.tv(
            this,
            "۲۱۶ صفحه • ۱۱ بخش • کاملاً آفلاین • بدون هیچ مجوزی",
            11.5f, tem.kamrang
        )
        payin.gravity = android.view.Gravity.CENTER
        ghabe.addView(payin, safP(18))

        setContentView(root)

        // ---------- انیمیشن ورود ----------
        val bekha = listOf<View>(onvan, kartJeld, dokmeAsli)
        for ((i, v) in bekha.withIndex()) {
            v.alpha = 0f
            v.translationY = Abzar.dp(this, 26f).toFloat()
            v.animate().alpha(1f).translationY(0f).setDuration(420).setStartDelay((i * 120).toLong()).start()
        }
    }

    private fun safP(t: Int): LinearLayout.LayoutParams {
        val p = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        p.topMargin = Abzar.dp(this, t.toFloat())
        return p
    }
}
