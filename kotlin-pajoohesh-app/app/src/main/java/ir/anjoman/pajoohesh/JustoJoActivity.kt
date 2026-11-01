package ir.anjoman.pajoohesh

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import kotlin.math.max
import kotlin.math.min

/*
 * جست‌وجو در کل ۲۱۶ صفحه — نام دانش‌آموز، استان، مدرسه، مسابقه، هر چیزی
 */
class JustoJoActivity : Activity() {

    private lateinit var tem: Tem
    private lateinit var ghabeNatije: LinearLayout
    private lateinit var matnHal: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TemEnekhab.rangVaredKon(this)
        tem = TemEnekhab.bekhon(this)

        val root = LinearLayout(this)
        root.layoutDirection = View.LAYOUT_DIRECTION_RTL
        root.orientation = LinearLayout.VERTICAL
        root.setBackgroundColor(tem.zamin)

        // سربرگ
        val sar = LinearLayout(this)
        sar.orientation = LinearLayout.HORIZONTAL
        sar.gravity = Gravity.CENTER_VERTICAL
        sar.setPadding(Abzar.dp(this, 18f), Abzar.dp(this, 18f), Abzar.dp(this, 18f), Abzar.dp(this, 6f))
        val onvan = Abzar.tv(this, "🔍 جست‌وجو", 24f, tem.matn)
        onvan.typeface = FontBox.vazirBlack(this)
        sar.addView(onvan, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))
        val dokmePosht = Abzar.tv(this, "بستن ✕", 13f, tem.kamrang)
        dokmePosht.setPadding(Abzar.dp(this, 12f), Abzar.dp(this, 6f), Abzar.dp(this, 12f), Abzar.dp(this, 6f))
        dokmePosht.background = Abzar.kartKhatei(this, tem.kamrang, 0, 12f)
        Abzar.lams(dokmePosht)
        dokmePosht.setOnClickListener { finish() }
        sar.addView(dokmePosht)
        root.addView(sar)
        root.addView(Abzar.nobarZiba(this, tem.asli, tem.dovom, 3))

        val scroll = ScrollView(this)
        val ghabe = LinearLayout(this)
        ghabe.orientation = LinearLayout.VERTICAL
        ghabe.setPadding(Abzar.dp(this, 18f), Abzar.dp(this, 12f), Abzar.dp(this, 18f), Abzar.dp(this, 26f))
        scroll.addView(ghabe)
        root.addView(scroll)

        // کادر جست‌وجو
        val edt = EditText(this)
        edt.hint = "مثلاً: نجف آباد، شطرنج، جابر، المپیاد ریاضی…"
        edt.setTextColor(tem.matn)
        edt.setHintTextColor(tem.kamrang)
        edt.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 14.5f)
        edt.typeface = FontBox.vazir(this)
        edt.background = Abzar.kart(this, tem.kart, 16f)
        edt.setPadding(Abzar.dp(this, 16f), Abzar.dp(this, 12f), Abzar.dp(this, 16f), Abzar.dp(this, 12f))
        ghabe.addView(edt)

        matnHal = Abzar.tv(this, "حداقل دو حرف بنویس تا جست‌وجو شروع شود", 12f, tem.kamrang)
        matnHal.setPadding(Abzar.dp(this, 4f), Abzar.dp(this, 8f), 0, 0)
        ghabe.addView(matnHal)

        ghabeNatije = LinearLayout(this)
        ghabeNatije.orientation = LinearLayout.VERTICAL
        ghabe.addView(ghabeNatije, safP(4))

        setContentView(root)

        edt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, a: Int, b: Int, c: Int) {}
            override fun onTextChanged(s: CharSequence?, a: Int, b: Int, c: Int) {}
            override fun afterTextChanged(s: Editable?) {
                jostojo(s?.toString() ?: "")
            }
        })
    }

    private fun jostojo(poorsesh: String) {
        ghabeNatije.removeAllViews()
        val soal = Ketab.sazeMatn(poorsesh)
        if (soal.length < 2) {
            matnHal.text = "حداقل دو حرف بنویس تا جست‌وجو شروع شود"
            return
        }
        val matnHa = Ketab.matnSafheha(this)
        var tedad = 0
        for (i in 0 until Ketab.tedadSafhe) {
            val t = Ketab.sazeMatn(matnHa[i])
            val j = t.indexOf(soal)
            if (j < 0) continue
            tedad++
            if (tedad <= 40) ghabeNatije.addView(radeNatije(i, t, j, soal.length), safP(8))
            if (tedad >= 60) break
        }
        matnHal.text = if (tedad == 0) "چیزی پیدا نشد — کوتاه‌تر بنویس"
        else "${Abzar.adadFarsi(tedad)} صفحه پیدا شد" + if (tedad > 40) " (۴۰ تای اول)" else ""
    }

    private fun radeNatije(shomare: Int, t: String, j: Int, tool: Int): View {
        val tem = tem()
        val rade = LinearLayout(this)
        rade.orientation = LinearLayout.HORIZONTAL
        rade.gravity = Gravity.CENTER_VERTICAL
        rade.background = Abzar.kart(this, tem.kart, 16f)
        rade.setPadding(Abzar.dp(this, 14f), Abzar.dp(this, 12f), Abzar.dp(this, 14f), Abzar.dp(this, 12f))
        rade.isClickable = true
        Abzar.lams(rade)

        val mohr = Abzar.tv(this, Abzar.adadFarsi(shomare + 1), 13f, 0xFFFFFFFF.toInt(), bold = true)
        mohr.gravity = Gravity.CENTER
        mohr.typeface = FontBox.vazirBold(this)
        mohr.background = Abzar.kartGradient(this, tem.asli, tem.dovom, 12f)
        rade.addView(mohr, LinearLayout.LayoutParams(Abzar.dp(this, 46f), Abzar.dp(this, 46f)))

        val sotoon = LinearLayout(this)
        sotoon.orientation = LinearLayout.VERTICAL
        val pS = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
        pS.marginStart = Abzar.dp(this, 12f)
        sotoon.addView(Abzar.tv(this, Ketab.esmeBakhsh(shomare), 13.5f, tem.asli, bold = true))

        val az = max(0, j - 42)
        val ta = min(t.length, j + tool + 62)
        var ghetee = t.substring(az, ta).trim()
        if (az > 0) ghetee = "…$ghetee"
        if (ta < t.length) ghetee = "$ghetee…"
        val matnGhetee = Abzar.tv(this, ghetee, 11.5f, tem.matn)
        matnGhetee.maxLines = 3
        matnGhetee.setPadding(0, Abzar.dp(this, 3f), 0, 0)
        sotoon.addView(matnGhetee)
        rade.addView(sotoon, pS)

        rade.setOnClickListener {
            startActivity(Intent(this, KhavanakActivity::class.java).putExtra("safhe", shomare))
        }
        return rade
    }

    private fun tem(): Tem = TemEnekhab.bekhon(this)

    private fun safP(t: Int): LinearLayout.LayoutParams {
        val p = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        p.topMargin = Abzar.dp(this, t.toFloat())
        return p
    }
}
