package ir.anjoman.pajoohesh

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.Bundle
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import kotlin.math.abs

/*
 * خواناک — ۲۱۶ صفحهٔ کتاب با ورق‌زدنِ پیوستهٔ سه‌بعدی
 * کشیدن انگشت: به راست = صفحهٔ بعد، به چپ = صفحهٔ قبل (مثل کتاب راست‌چین واقعی)
 * لمس لبهٔ چپ = بعدی، لبهٔ راست = قبلی، وسط = پنهان/نمایش نوارها
 * شب‌خوانی: وارونهٔ ملایم روشنایی صفحات برای خواندن در تاریکی
 */
class KhavanakActivity : Activity() {

    private lateinit var tem: Tem
    private lateinit var frame: GhabeSafhe
    private lateinit var ru: ImageView        // صفحهٔ فعلی (لایهٔ بالا)
    private lateinit var pust: ImageView      // صفحهٔ مقصد (لایهٔ زیر)
    private lateinit var saiyePust: View      // سایهٔ روی صفحهٔ مقصد
    private lateinit var saiyeRu: View        // سایهٔ روی صفحهٔ ورق‌خور
    private lateinit var matnOnvan: TextView
    private lateinit var matnShomare: TextView
    private lateinit var dokmeShab: TextView
    private lateinit var search: SeekBar

    private var safheFaali = 0
    private var halat = 0          // ۰ آرام / ۱ کشیدن / ۲ انیمیشن
    private var jahat = 1          // ۱ بعدی / −۱ قبلی
    private var pishraft = 0f      // ۰ تا ۱
    private var xAval = 0f
    private var yAval = 0f
    private var zamAval = 0L
    private var filterShab: ColorMatrixColorFilter? = null

    // قاب صفحه‌ها — همهٔ لمس‌ها همین‌جا مدیریت می‌شود
    inner class GhabeSafhe(context: Context) : FrameLayout(context) {
        @SuppressLint("ClickableViewAccessibility")
        override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
            when (ev.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    xAval = ev.x
                    yAval = ev.y
                    zamAval = System.currentTimeMillis()
                }
                MotionEvent.ACTION_MOVE -> {
                    if (halat == 0) {
                        val dX = ev.x - xAval
                        val dY = ev.y - yAval
                        if (abs(dX) > 48f && abs(dX) > abs(dY) * 1.3f) {
                            val j = if (dX > 0) 1 else -1
                            val mojaz = (j == 1 && safheFaali < Ketab.tedadSafhe - 1) ||
                                (j == -1 && safheFaali > 0)
                            if (mojaz) shoroKeshidan(j)
                        }
                    } else if (halat == 1) {
                        val dX = ev.x - xAval
                        pishraft = (abs(dX) / (width * 0.55f).coerceAtLeast(1f)).coerceIn(0f, 1f)
                        emalKeshidan()
                    }
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    if (halat == 1) {
                        tamamKeshidan(pishraft > 0.28f)
                    } else if (halat == 0 && ev.actionMasked == MotionEvent.ACTION_UP) {
                        val dX = abs(ev.x - xAval)
                        val dY = abs(ev.y - yAval)
                        val dt = System.currentTimeMillis() - zamAval
                        if (dX < 26f && dY < 26f && dt < 320L) lamsSafhe(ev.x)
                    }
                }
            }
            return true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TemEnekhab.rangVaredKon(this)
        tem = TemEnekhab.bekhon(this)

        if (!Ketab.mojojod(this)) {
            Toast.makeText(this, "⚠️ محتوای کتاب در این نسخه قرار نگرفته است", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        safheFaali = intent.getIntExtra("safhe", Ketab.safheAkharBekhon(this))
            .coerceIn(0, Ketab.tedadSafhe - 1)
        filterShab = matrisShab()

        val root = LinearLayout(this)
        root.layoutDirection = View.LAYOUT_DIRECTION_RTL
        root.orientation = LinearLayout.VERTICAL
        root.setBackgroundColor(tem.zamin)

        // ---------- نوار بالا ----------
        val bar = LinearLayout(this)
        bar.orientation = LinearLayout.HORIZONTAL
        bar.gravity = Gravity.CENTER_VERTICAL
        bar.setPadding(Abzar.dp(this, 10f), Abzar.dp(this, 8f), Abzar.dp(this, 10f), Abzar.dp(this, 8f))
        bar.background = Abzar.kart(this, tem.kartTir, 0f)

        val dokmePosht = Abzar.tv(this, "✕", 17f, tem.matn, bold = true)
        dokmePosht.gravity = Gravity.CENTER
        dokmePosht.background = Abzar.dayereh(this, tem.kart)
        Abzar.lams(dokmePosht)
        dokmePosht.setPadding(Abzar.dp(this, 12f), Abzar.dp(this, 6f), Abzar.dp(this, 12f), Abzar.dp(this, 6f))
        dokmePosht.setOnClickListener { finish() }
        bar.addView(dokmePosht)

        val vasat = LinearLayout(this)
        vasat.orientation = LinearLayout.VERTICAL
        vasat.gravity = Gravity.CENTER
        val pVasat = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
        pVasat.marginStart = Abzar.dp(this, 8f)
        pVasat.marginEnd = Abzar.dp(this, 8f)
        matnOnvan = Abzar.tv(this, Ketab.esmeBakhsh(safheFaali), 14f, tem.asli, bold = true)
        matnOnvan.gravity = Gravity.CENTER
        matnOnvan.maxLines = 1
        vasat.addView(matnOnvan)
        matnShomare = Abzar.tv(this, "صفحهٔ ${Abzar.adadFarsi(safheFaali + 1)} از ${Abzar.adadFarsi(Ketab.tedadSafhe)}", 11f, tem.kamrang)
        matnShomare.gravity = Gravity.CENTER
        vasat.addView(matnShomare)
        bar.addView(vasat, pVasat)

        dokmeShab = Abzar.tv(this, if (Ketab.shabBekhon(this)) "☀" else "🌙", 17f, tem.matn, bold = true)
        dokmeShab.gravity = Gravity.CENTER
        dokmeShab.background = Abzar.dayereh(this, tem.kart)
        Abzar.lams(dokmeShab)
        dokmeShab.setPadding(Abzar.dp(this, 12f), Abzar.dp(this, 6f), Abzar.dp(this, 12f), Abzar.dp(this, 6f))
        dokmeShab.setOnClickListener {
            val no = !Ketab.shabBekhon(this)
            Ketab.shabZakhire(this, no)
            filterHa()
            dokmeShab.text = if (no) "☀" else "🌙"
            Toast.makeText(
                this,
                if (no) "🌙 شب‌خوانی روشن شد" else "☀ شب‌خوانی خاموش شد",
                Toast.LENGTH_SHORT
            ).show()
        }
        bar.addView(dokmeShab)
        root.addView(bar)

        // ---------- قاب صفحه‌ها ----------
        frame = GhabeSafhe(this)
        frame.setBackgroundColor(tem.zamin)

        pust = ImageView(this)
        pust.scaleType = ImageView.ScaleType.FIT_CENTER
        pust.visibility = View.GONE
        frame.addView(pust, FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        ))

        saiyePust = View(this)
        saiyePust.setBackgroundColor(0xFF000000.toInt())
        saiyePust.alpha = 0f
        frame.addView(saiyePust, FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        ))

        ru = ImageView(this)
        ru.scaleType = ImageView.ScaleType.FIT_CENTER
        ru.cameraDistance = 12000f
        frame.addView(ru, FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        ))

        saiyeRu = View(this)
        saiyeRu.setBackgroundColor(0xFF000000.toInt())
        saiyeRu.alpha = 0f
        frame.addView(saiyeRu, FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        ))

        val pFrame = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f
        )
        root.addView(frame, pFrame)

        // ---------- نوار پایین ----------
        val payin = LinearLayout(this)
        payin.orientation = LinearLayout.HORIZONTAL
        payin.gravity = Gravity.CENTER_VERTICAL
        payin.setPadding(Abzar.dp(this, 10f), Abzar.dp(this, 8f), Abzar.dp(this, 10f), Abzar.dp(this, 8f))
        payin.background = Abzar.kart(this, tem.kartTir, 0f)

        val dokmeGhabli = Abzar.tv(this, "‹ قبلی", 14f, tem.matn, bold = true)
        dokmeGhabli.gravity = Gravity.CENTER
        dokmeGhabli.background = Abzar.kartKhatei(this, tem.asli, 0, 12f)
        Abzar.lams(dokmeGhabli)
        dokmeGhabli.setPadding(Abzar.dp(this, 12f), Abzar.dp(this, 8f), Abzar.dp(this, 12f), Abzar.dp(this, 8f))
        dokmeGhabli.setOnClickListener { ghabli() }
        payin.addView(dokmeGhabli, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))

        search = SeekBar(this)
        search.max = Ketab.tedadSafhe - 1
        search.progress = safheFaali
        search.layoutDirection = View.LAYOUT_DIRECTION_LTR
        val pSearch = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 2.4f)
        pSearch.marginStart = Abzar.dp(this, 10f)
        pSearch.marginEnd = Abzar.dp(this, 10f)
        search.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(s: SeekBar?, v: Int, azKarbar: Boolean) {
                if (azKarbar) {
                    matnShomare.text = "صفحهٔ ${Abzar.adadFarsi(v + 1)} از ${Abzar.adadFarsi(Ketab.tedadSafhe)}"
                }
            }
            override fun onStartTrackingTouch(s: SeekBar?) {}
            override fun onStopTrackingTouch(s: SeekBar?) {
                if (halat == 0) {
                    val v = search.progress.coerceIn(0, Ketab.tedadSafhe - 1)
                    if (v != safheFaali) {
                        safheFaali = v
                        neshanBedeh(avali = false)
                        piShBargozari()
                    }
                }
            }
        })
        payin.addView(search, pSearch)

        val dokmeBadi = Abzar.tv(this, "بعدی ›", 14f, tem.matn, bold = true)
        dokmeBadi.gravity = Gravity.CENTER
        dokmeBadi.background = Abzar.kartGradient(this, tem.asli, tem.dovom, 12f)
        Abzar.lams(dokmeBadi)
        dokmeBadi.setPadding(Abzar.dp(this, 12f), Abzar.dp(this, 8f), Abzar.dp(this, 12f), Abzar.dp(this, 8f))
        dokmeBadi.setOnClickListener { badi() }
        payin.addView(dokmeBadi, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))

        root.addView(payin)
        setContentView(root)

        neshanBedeh(avali = true)
        piShBargozari()
    }

    // ---------- ورق‌زدن ----------
    private fun badi() {
        if (halat == 0 && safheFaali < Ketab.tedadSafhe - 1) {
            jahat = 1
            if (shoroKeshidan(1)) tamamKeshidan(true)
        }
    }

    private fun ghabli() {
        if (halat == 0 && safheFaali > 0) {
            jahat = -1
            if (shoroKeshidan(-1)) tamamKeshidan(true)
        }
    }

    private fun shoroKeshidan(j: Int): Boolean {
        if (halat != 0) return false
        val hadaf = safheFaali + j
        if (hadaf < 0 || hadaf > Ketab.tedadSafhe - 1) return false
        val b = Ketab.bekhon(this, hadaf, pahnayeTarget())
        if (b == null) {
            Toast.makeText(this, "صفحه در دسترس نیست", Toast.LENGTH_SHORT).show()
            return false
        }
        jahat = j
        pishraft = 0f
        pust.setImageBitmap(b)
        pust.visibility = View.VISIBLE
        halat = 1
        emalKeshidan()
        return true
    }

    private fun emalKeshidan() {
        val z = pishraft
        val pahna = frame.width.coerceAtLeast(1).toFloat()
        val bolandi = frame.height.coerceAtLeast(1).toFloat()
        ru.pivotX = if (jahat == 1) pahna else 0f
        ru.pivotY = bolandi * 0.5f
        saiyeRu.pivotX = ru.pivotX
        saiyeRu.pivotY = ru.pivotY
        val zaviye = 90f * z
        ru.rotationY = if (jahat == 1) -zaviye else zaviye
        saiyeRu.rotationY = ru.rotationY
        saiyeRu.alpha = 0.42f * z
        val m = 0.94f + 0.06f * z
        pust.scaleX = m
        pust.scaleY = m
        saiyePust.alpha = 0.30f * (1f - z)
    }

    private fun tamamKeshidan(bePayan: Boolean) {
        if (halat != 1) return
        halat = 2
        val az = pishraft
        val ta = if (bePayan) 1f else 0f
        val an = ValueAnimator.ofFloat(az, ta)
        an.duration = 240L
        an.interpolator = DecelerateInterpolator()
        an.addUpdateListener { a ->
            pishraft = a.animatedValue as Float
            emalKeshidan()
        }
        an.addListener(object : android.animation.AnimatorListenerAdapter() {
            override fun onAnimationEnd(a: android.animation.Animator) {
                if (bePayan) {
                    safheFaali = (safheFaali + jahat).coerceIn(0, Ketab.tedadSafhe - 1)
                    neshanBedeh(avali = false)
                } else {
                    pust.visibility = View.GONE
                    halat = 0
                }
                ru.rotationY = 0f
                saiyeRu.alpha = 0f
                saiyePust.alpha = 0f
                pust.scaleX = 1f
                pust.scaleY = 1f
            }
        })
        an.start()
    }

    private fun lamsSafhe(x: Float) {
        val w = frame.width.coerceAtLeast(1)
        if (x < w / 3f) {
            badi()          // لبهٔ چپ = صفحهٔ بعد (کتاب راست‌چین)
        } else if (x > w * 2f / 3f) {
            ghabli()        // لبهٔ راست = صفحهٔ قبل
        } else {
            // وسط = نمایش/پنهان‌کردن نوارها
            val bar = (frame.parent as LinearLayout).getChildAt(0)
            val payin = (frame.parent as LinearLayout).getChildAt(frame.parent.childCount - 1)
            val nemayesh = bar.visibility == View.VISIBLE
            bar.visibility = if (nemayesh) View.GONE else View.VISIBLE
            payin.visibility = if (nemayesh) View.GONE else View.VISIBLE
        }
    }

    // ---------- نمایش صفحهٔ فعلی ----------
    private fun neshanBedeh(avali: Boolean) {
        val b = Ketab.bekhon(this, safheFaali, pahnayeTarget())
        if (b != null) ru.setImageBitmap(b)
        ru.rotationY = 0f
        ru.pivotX = frame.width.coerceAtLeast(1).toFloat()
        ru.pivotY = frame.height.coerceAtLeast(1).toFloat() * 0.5f
        pust.visibility = View.GONE
        saiyeRu.alpha = 0f
        saiyePust.alpha = 0f
        halat = 0
        matnOnvan.text = Ketab.esmeBakhsh(safheFaali)
        matnShomare.text = "صفحهٔ ${Abzar.adadFarsi(safheFaali + 1)} از ${Abzar.adadFarsi(Ketab.tedadSafhe)}"
        if (search.progress != safheFaali) search.progress = safheFaali
        Ketab.safheAkharZakhire(this, safheFaali)
        filterHa()
        if (avali) {
            ru.alpha = 0f
            ru.animate().alpha(1f).setDuration(300).start()
        }
    }

    private fun filterHa() {
        val f = if (Ketab.shabBekhon(this)) filterShab else null
        ru.setColorFilter(f)
        pust.setColorFilter(f)
    }

    private fun pahnayeTarget(): Int =
        if (frame.width > 0) frame.width else resources.displayMetrics.widthPixels

    private fun piShBargozari() {
        Thread {
            val pahna = pahnayeTarget()
            val lista = intArrayOf(safheFaali + 1, safheFaali - 1, safheFaali + 2)
            for (i in lista) {
                if (i in 0 until Ketab.tedadSafhe) {
                    Ketab.bekhon(this, i, pahna)
                }
            }
        }.start()
    }

    // ماتریس شب‌خوانی: وارونه‌کردن روشنایی با حفظ کنتراست + کمی گرمای رنگ
    private fun matrisShab(): ColorMatrixColorFilter {
        val m = ColorMatrix(floatArrayOf(
            -0.33f, -0.59f, -0.11f, 0f, 1f,
            -0.33f, -0.59f, -0.11f, 0f, 1f,
            -0.33f, -0.59f, -0.11f, 0f, 1f,
            0f, 0f, 0f, 1f, 0f
        ))
        val garm = ColorMatrix(floatArrayOf(
            1f, 0.05f, 0f, 0f, 0f,
            0f, 1f, 0.02f, 0f, 0f,
            0f, 0f, 1f, 0f, 0f,
            0f, 0f, 0f, 1f, 0f
        ))
        m.postConcat(garm)
        return ColorMatrixColorFilter(m)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) Ketab.azadKon()
    }
}
