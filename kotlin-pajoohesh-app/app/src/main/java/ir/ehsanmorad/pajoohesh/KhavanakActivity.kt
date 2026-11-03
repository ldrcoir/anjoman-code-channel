package ir.ehsanmorad.pajoohesh

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast

/*
 * خواناک — ۲۱۶ صفحهٔ کتاب با ورق‌خوردن طبیعی (VaraghZanView)
 * کشیدن انگشت به راست = بعدی، به چپ = قبلی؛ لمس لبهٔ چپ = بعدی، راست = قبلی
 * لمس وسط = نمایش/پنهان‌کردن نوارها؛ دکمهٔ بی‌صدا برای صدای ورق‌خوردن
 * شب‌خوانی: وارونهٔ ملایم روشنایی صفحات برای خواندن در تاریکی — فقط در تم‌های روشن
 * (در تم تیره، وارونه‌کردن صفحه‌ها همه‌چیز را تیرهٔ بی‌تمایز می‌کند و دیده نمی‌شود)
 */
class KhavanakActivity : Activity() {

    private lateinit var tem: Tem
    private lateinit var varaghZan: VaraghZanView
    private lateinit var neshaneSeda: ImageView
    private lateinit var neshaneShab: ImageView
    private lateinit var matnOnvan: TextView
    private lateinit var matnShomare: TextView
    private lateinit var navarParsh: SeekBar
    private lateinit var nivarBala: LinearLayout
    private lateinit var nivarPayin: LinearLayout
    private var filterShab: ColorMatrixColorFilter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TemEnekhab.rangVaredKon(this)
        tem = TemEnekhab.bekhon(this)

        if (!Ketab.mojojod(this)) {
            Toast.makeText(this, "⚠️ محتوای کتاب در این نسخه قرار نگرفته است", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        val safheAval = intent.getIntExtra("safhe", Ketab.safheAkharBekhon(this))
            .coerceIn(0, Ketab.tedadSafhe - 1)
        filterShab = matrisShab()

        val rishe = LinearLayout(this)
        rishe.layoutDirection = View.LAYOUT_DIRECTION_RTL
        rishe.orientation = LinearLayout.VERTICAL
        rishe.setBackgroundColor(tem.zamin)

        // ---------- نوار بالا ----------
        val nivarBalaJadid = LinearLayout(this)
        nivarBalaJadid.orientation = LinearLayout.HORIZONTAL
        nivarBalaJadid.gravity = Gravity.CENTER_VERTICAL
        nivarBalaJadid.setPadding(Abzar.dp(this, 10f), Abzar.dp(this, 8f), Abzar.dp(this, 10f), Abzar.dp(this, 8f))
        nivarBalaJadid.background = Abzar.kart(this, tem.kartTir, 0f)

        val dokmePosht = dokmeGerd("ic_bastan")
        dokmePosht.setOnClickListener { finish() }
        nivarBalaJadid.addView(dokmePosht)

        val vasatNivar = LinearLayout(this)
        vasatNivar.orientation = LinearLayout.VERTICAL
        vasatNivar.gravity = Gravity.CENTER
        val pVasat = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
        pVasat.marginStart = Abzar.dp(this, 8f)
        pVasat.marginEnd = Abzar.dp(this, 8f)
        matnOnvan = Abzar.tv(this, Ketab.esmeBakhsh(safheAval), 14f, tem.asli, bold = true)
        matnOnvan.gravity = Gravity.CENTER
        matnOnvan.maxLines = 1
        vasatNivar.addView(matnOnvan)
        matnShomare = Abzar.tv(this, matneShomare(safheAval), 11f, tem.kamrang)
        matnShomare.gravity = Gravity.CENTER
        vasatNivar.addView(matnShomare)
        nivarBalaJadid.addView(vasatNivar, pVasat)

        // دکمهٔ بی‌صدا کردن صدای ورق‌خوردن
        neshaneSeda = dokmeGerd(if (Ketab.sedaBekhon(this)) "ic_seda_on" else "ic_seda_off")
        neshaneSeda.setOnClickListener {
            val no = !Ketab.sedaBekhon(this)
            Ketab.sedaZakhire(this, no)
            neshaneSetColor(neshaneSeda, if (no) "ic_seda_on" else "ic_seda_off")
            Toast.makeText(
                this,
                if (no) "🔊 صدای ورق‌خوردن روشن شد" else "🔇 بی‌صدا شد",
                Toast.LENGTH_SHORT
            ).show()
        }
        nivarBalaJadid.addView(neshaneSeda)

        // دکمهٔ شب‌خوانی — فقط برای تم‌های روشن؛ در تم تیره صفحه‌ها خودشان تیره‌اند
        // و وارونه‌کردنشان هیچ‌چیز دیده نمی‌شود
        neshaneShab = dokmeGerd(if (Ketab.shabBekhon(this) && !tem.khater) "ic_rooz" else "ic_shab")
        neshaneShab.setOnClickListener {
            if (tem.khater) {
                Toast.makeText(
                    this,
                    "تم تیره خودش حالِ شب دارد — شب‌خوانی برای تم‌های روشن است",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            val no = !Ketab.shabBekhon(this)
            Ketab.shabZakhire(this, no)
            neshaneSetColor(neshaneShab, if (no) "ic_rooz" else "ic_shab")
            filterHa()
            Toast.makeText(
                this,
                if (no) "🌙 شب‌خوانی روشن شد" else "☀ شب‌خوانی خاموش شد",
                Toast.LENGTH_SHORT
            ).show()
        }
        nivarBalaJadid.addView(neshaneShab)
        rishe.addView(nivarBalaJadid)
        nivarBala = nivarBalaJadid

        // ---------- قاب ورق‌زن ----------
        varaghZan = VaraghZanView(this) { shomare ->
            Ketab.bekhon(this, shomare, pahnayeTarget())
        }
        varaghZan.tedadSafhe = Ketab.tedadSafhe
        varaghZan.rangZamin = tem.zamin
        varaghZan.safheRavanKon(safheAval)
        filterHa()
        varaghZan.vaghteTaghir = { shomare ->
            matnOnvan.text = Ketab.esmeBakhsh(shomare)
            matnShomare.text = matneShomare(shomare)
            if (navarParsh.progress != shomare) navarParsh.progress = shomare
            Ketab.safheAkharZakhire(this, shomare)
            piShBargozari(shomare)
        }
        varaghZan.vaghteLamsVasat = {
            val nemayesh = nivarBala.visibility == View.VISIBLE
            nivarBala.visibility = if (nemayesh) View.GONE else View.VISIBLE
            nivarPayin.visibility = if (nemayesh) View.GONE else View.VISIBLE
        }
        rishe.addView(varaghZan, LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f
        ))

        // ---------- نوار پایین ----------
        val nivarPayinJadid = LinearLayout(this)
        nivarPayinJadid.orientation = LinearLayout.HORIZONTAL
        nivarPayinJadid.gravity = Gravity.CENTER_VERTICAL
        nivarPayinJadid.setPadding(Abzar.dp(this, 10f), Abzar.dp(this, 8f), Abzar.dp(this, 10f), Abzar.dp(this, 8f))
        nivarPayinJadid.background = Abzar.kart(this, tem.kartTir, 0f)

        val dokmeGhabli = Abzar.tv(this, "‹ قبلی", 14f, tem.matn, bold = true)
        dokmeGhabli.gravity = Gravity.CENTER
        dokmeGhabli.background = Abzar.kartKhatei(this, tem.asli, 0, 12f)
        Abzar.lams(dokmeGhabli)
        dokmeGhabli.setPadding(Abzar.dp(this, 12f), Abzar.dp(this, 8f), Abzar.dp(this, 12f), Abzar.dp(this, 8f))
        dokmeGhabli.setOnClickListener { varaghZan.ghabliNarm() }
        nivarPayinJadid.addView(dokmeGhabli, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))

        navarParsh = SeekBar(this)
        navarParsh.max = Ketab.tedadSafhe - 1
        navarParsh.progress = safheAval
        navarParsh.layoutDirection = View.LAYOUT_DIRECTION_LTR
        val pParsh = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 2.4f)
        pParsh.marginStart = Abzar.dp(this, 10f)
        pParsh.marginEnd = Abzar.dp(this, 10f)
        navarParsh.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(s: SeekBar?, meghdar: Int, azKarbar: Boolean) {
                if (azKarbar) {
                    matnShomare.text = matneShomare(meghdar)
                }
            }
            override fun onStartTrackingTouch(s: SeekBar?) {}
            override fun onStopTrackingTouch(s: SeekBar?) {
                val meghdar = navarParsh.progress.coerceIn(0, Ketab.tedadSafhe - 1)
                varaghZan.safheRavanKon(meghdar)
                Ketab.safheAkharZakhire(this@KhavanakActivity, meghdar)
                matnOnvan.text = Ketab.esmeBakhsh(meghdar)
                matnShomare.text = matneShomare(meghdar)
                piShBargozari(meghdar)
            }
        })
        nivarPayinJadid.addView(navarParsh, pParsh)

        val dokmeBadi = Abzar.tv(this, "بعدی ›", 14f, tem.matn, bold = true)
        dokmeBadi.gravity = Gravity.CENTER
        dokmeBadi.background = Abzar.kartGradient(this, tem.asli, tem.dovom, 12f)
        Abzar.lams(dokmeBadi)
        dokmeBadi.setPadding(Abzar.dp(this, 12f), Abzar.dp(this, 8f), Abzar.dp(this, 12f), Abzar.dp(this, 8f))
        dokmeBadi.setOnClickListener { varaghZan.badiNarm() }
        nivarPayinJadid.addView(dokmeBadi, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))

        rishe.addView(nivarPayinJadid)
        nivarPayin = nivarPayinJadid
        setContentView(rishe)

        piShBargozari(safheAval)
    }

    // دکمهٔ گرد با آیکون وکتوری
    private fun dokmeGerd(namNeshane: String): ImageView {
        val d = Abzar.neshane(this, namNeshane, tem.matn, Abzar.dp(this, 20f))
        d.background = Abzar.dayereh(this, tem.kart)
        d.setPadding(Abzar.dp(this, 9f), Abzar.dp(this, 9f), Abzar.dp(this, 9f), Abzar.dp(this, 9f))
        Abzar.lams(d)
        val p = LinearLayout.LayoutParams(Abzar.dp(this, 38f), Abzar.dp(this, 38f))
        p.marginStart = Abzar.dp(this, 4f)
        d.layoutParams = p
        return d
    }

    private fun neshaneSetColor(n: ImageView, namNeshane: String) {
        val h = try {
            getDrawable(resources.getIdentifier(namNeshane, "drawable", packageName))
        } catch (_: Exception) {
            null
        }
        if (h != null) {
            n.setImageDrawable(h)
            n.setColorFilter(tem.matn)
        }
    }

    private fun matneShomare(shomare: Int): String =
        "صفحهٔ ${Abzar.adadFarsi(shomare + 1)} از ${Abzar.adadFarsi(Ketab.tedadSafhe)}"

    private fun pahnayeTarget(): Int =
        if (varaghZan.width > 0) varaghZan.width else resources.displayMetrics.widthPixels

    // پیش‌بارگذاری صفحه‌های اطراف برای برگرداندن روان
    private fun piShBargozari(safheFaeli: Int) {
        Thread {
            val pahna = pahnayeTarget()
            val lista = intArrayOf(safheFaeli + 1, safheFaeli - 1, safheFaeli + 2, safheFaeli - 2)
            for (i in lista) {
                if (i in 0 until Ketab.tedadSafhe) {
                    Ketab.bekhon(this, i, pahna)
                }
            }
        }.start()
    }

    private fun filterHa() {
        // در تم تیره شب‌خوانی عملاً خاموش است تا صفحه‌ها خوانا بمانند
        val shab = Ketab.shabBekhon(this) && !tem.khater
        varaghZan.filterShab = if (shab) filterShab else null
        varaghZan.invalidate()
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
