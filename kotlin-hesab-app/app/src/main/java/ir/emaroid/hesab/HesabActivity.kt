package ir.emaroid.hesab

import android.app.Activity
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

/*
 * ماشین‌حساب علمی — صفحهٔ اصلی حساب
 * نمایش عبارت + نتیجهٔ زنده + صفحه‌کلید بزرگ + تاریخچه
 */
class HesabActivity : Activity() {

    private lateinit var nemayesh: TextView
    private lateinit var natijeZende: TextView
    private lateinit var ghabeTarikhche: LinearLayout
    private val tarikhche = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Abzar.rangVaredKon(this)
        MahineHesab.daraje = Zakhire.daraje(this)

        val root = LinearLayout(this)
        root.orientation = LinearLayout.VERTICAL
        root.layoutDirection = View.LAYOUT_DIRECTION_LTR
        root.background = NagshZamin(0xFF101A33.toInt(), 0xFF0B1220.toInt())
        root.setPadding(Abzar.dp(this, 14f), Abzar.dp(this, 12f), Abzar.dp(this, 14f), Abzar.dp(this, 12f))

        // نوار بالا: بازگشت + درجه/رادیان
        val sar = LinearLayout(this)
        sar.orientation = LinearLayout.HORIZONTAL
        sar.layoutDirection = View.LAYOUT_DIRECTION_RTL
        val bargasht = Abzar.dokme(this, "‹ خانه", Abzar.RANG_TIZ, Abzar.RANG_TIZ, khateDor = true)
        bargasht.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        bargasht.setOnClickListener { finish() }
        sar.addView(bargasht)

        val pZaban = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
        sar.addView(View(this), pZaban)
        val dokmeDaraje = Abzar.dokme(this, if (MahineHesab.daraje) "DEG" else "RAD",
            Abzar.RANG_TALAEI, Abzar.RANG_ZAMIN)
        dokmeDaraje.setOnClickListener {
            Zakhire.darajeKon(this, !MahineHesab.daraje)
            MahineHesab.daraje = Zakhire.daraje(this)
            dokmeDaraje.text = if (MahineHesab.daraje) "DEG" else "RAD"
            natijeBekon()
        }
        sar.addView(dokmeDaraje)
        root.addView(sar, LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))

        // نمایش
        val kartNemayesh = LinearLayout(this)
        kartNemayesh.orientation = LinearLayout.VERTICAL
        kartNemayesh.background = Abzar.kart(this, Abzar.RANG_CODE, 14f)
        kartNemayesh.setPadding(Abzar.dp(this, 16f), Abzar.dp(this, 14f), Abzar.dp(this, 16f), Abzar.dp(this, 14f))

        natijeZende = TextView(this)
        natijeZende.text = "0"
        natijeZende.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 22f)
        natijeZende.setTextColor(Abzar.RANG_TALAEI)
        natijeZende.typeface = Typeface.MONOSPACE
        natijeZende.gravity = Gravity.END
        kartNemayesh.addView(natijeZende)

        nemayesh = TextView(this)
        nemayesh.text = ""
        nemayesh.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 34f)
        nemayesh.setTextColor(Abzar.RANG_MATN)
        nemayesh.typeface = Typeface.MONOSPACE
        nemayesh.gravity = Gravity.END
        nemayesh.setSingleLine(false)
        kartNemayesh.addView(nemayesh, LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
            topMargin = Abzar.dp(this@HesabActivity, 6f)
        })
        root.addView(kartNemayesh, LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
            topMargin = Abzar.dp(this@HesabActivity, 12f)
        })

        // تاریخچه
        ghabeTarikhche = LinearLayout(this)
        ghabeTarikhche.orientation = LinearLayout.VERTICAL
        val pT = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        pT.topMargin = Abzar.dp(this, 8f)
        root.addView(ghabeTarikhche, pT)

        // صفحه‌کلید
        root.addView(safheKelid(), LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f))

        setContentView(root)
    }

    // نتیجهٔ زنده: هر تغییر عبارت، محاسبهٔ بی‌صدا
    private fun natijeBekon() {
        val e = nemayesh.text.toString()
        if (e.isBlank()) { natijeZende.text = "0"; return }
        natijeZende.text = try {
            "= " + MahineHesab.natijeRishti(e)
        } catch (_: Exception) { "" }
    }

    private fun safheKelid(): View {
        val g = LinearLayout(this)
        g.orientation = LinearLayout.VERTICAL
        g.setPadding(0, Abzar.dp(this, 10f), 0, 0)

        // ردیف‌ها: هر ردیف آرایه‌ای از (برچسب، عمل)
        val safha = listOf(
            listOf("C" to 'C', "(" to '(', ")" to ')', "÷" to '/'),
            listOf("7" to 'd', "8" to 'd', "9" to 'd', "×" to '*'),
            listOf("4" to 'd', "5" to 'd', "6" to 'd', "−" to '-'),
            listOf("1" to 'd', "2" to 'd', "3" to 'd', "+" to '+'),
            listOf("0" to 'd', "." to '.', "^" to '^', "!" to '!'),
            listOf("sin" to 'f', "cos" to 'f', "tan" to 'f', "√" to 'f'),
            listOf("ln" to 'f', "log" to 'f', "π" to 'f', "e" to 'f'),
            listOf("⌫" to 'B', "%" to '%', "=" to '=')
        )
        for (radif in safha) {
            val r = LinearLayout(this)
            for ((label, no) in radif) {
                val amali = no in "+-*/^!%=" || no == 'C' || no == 'B'
                val tavalod = if (no == 'd') Abzar.RANG_DOKME
                else if (no == 'f') 0xFF1B2C4F.toInt()
                else if (label == "=") Abzar.RANG_SABZ
                else if (label == "C") Abzar.RANG_GHERMEZ
                else Abzar.RANG_DOKME_AMAL
                val dokme = Abzar.dokme(this, label, tavalod,
                    if (label == "=" || label == "C") Abzar.RANG_ZAMIN else Abzar.RANG_MATN,
                    size = if (no == 'd') 20f else 16f)
                dokme.setOnClickListener { bezan(label, no) }
                r.addView(dokme, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f).apply {
                    setMargins(Abzar.dp(this@HesabActivity, 3f), Abzar.dp(this@HesabActivity, 3f),
                        Abzar.dp(this@HesabActivity, 3f), Abzar.dp(this@HesabActivity, 3f))
                })
            }
            g.addView(r, LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f))
        }
        return g
    }

    private fun bezan(label: String, no: Char) {
        val alan = nemayesh.text.toString()
        when (no) {
            'C' -> nemayesh.text = ""
            'B' -> nemayesh.text = if (alan.isNotEmpty()) alan.dropLast(1) else ""
            '=' -> {
                if (alan.isBlank()) return
                try {
                    val natije = MahineHesab.natijeRishti(alan)
                    tarikhche.add(0, "$alan = $natije")
                    tarikhcheKhata()
                    nemayesh.text = natije
                } catch (e: Exception) {
                    natijeZende.text = e.message ?: "خطا"
                }
            }
            'f' -> nemayesh.text = alan + when (label) {
                "√" -> "sqrt(" ; "π" -> "pi" ; else -> "$label("
            }
            else -> nemayesh.text = alan + label
        }
        natijeBekon()
    }

    private fun tarikhcheKhata() {
        ghabeTarikhche.removeAllViews()
        for (s in tarikhche.take(3)) {
            val t = Abzar.tv(this, s, 12f, Abzar.RANG_KAMRANG)
            t.typeface = Typeface.MONOSPACE
            t.gravity = Gravity.END
            ghabeTarikhche.addView(t)
        }
    }
}
