package ir.emaroid.hesab

import android.app.Activity
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView

/*
 * ماشین‌حساب علمی — پالت سنگ‌زغالی جدی
 * کلیدهای سنگی مات؛ عملگرها فولادی، توابع فیروزه‌ای مه‌گرفته، = تأکید واحد
 * نمایش تیره با نتیجهٔ زندهٔ کهربایی + تاریخچهٔ مات
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
        root.background = NagshZamin(Abzar.RANG_SHURU, Abzar.RANG_PAYAN)
        root.setPadding(Abzar.dp(this, 14f), Abzar.dp(this, 12f), Abzar.dp(this, 14f), Abzar.dp(this, 12f))

        // نوار بالا: خانه + زبان؟ نه — خانه + درجه/رادیان
        val sar = LinearLayout(this)
        sar.orientation = LinearLayout.HORIZONTAL
        sar.layoutDirection = View.LAYOUT_DIRECTION_RTL
        val bargasht = Abzar.dokme(this, Zaban.t("khane"), Abzar.RANG_TIZ, Abzar.RANG_TIZ, khateDor = true)
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

        // نمایش شیشه‌ای
        val kartNemayesh = LinearLayout(this)
        kartNemayesh.orientation = LinearLayout.VERTICAL
        kartNemayesh.background = Abzar.shisheh(this, 0x331B212A, 0x402FA8C9, 22f, 1.5f)
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
        nemayesh.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 36f)
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

        // تاریخچهٔ شیشه‌ای
        ghabeTarikhche = LinearLayout(this)
        ghabeTarikhche.orientation = LinearLayout.VERTICAL
        val pT = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        pT.topMargin = Abzar.dp(this, 8f)
        root.addView(ghabeTarikhche, pT)

        // صفحه‌کلید فانتزی
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

                // پوشش شیشه‌ای بر اساس نقش کلید
                val posht: android.graphics.drawable.Drawable
                val rangMatn: Int
                var size = 18f
                when {
                    label == "=" -> {
                        posht = Abzar.gradian(this, 0xFF256B85.toInt(), 0xFF2FA8C9.toInt(), 22f)
                        rangMatn = 0xFF08121A.toInt()
                    }
                    label == "C" -> {
                        posht = Abzar.shisheh(this, 0x33D96459.toInt(), 0x66D96459.toInt(), 22f, 1.5f)
                        rangMatn = Abzar.RANG_GHERMEZ
                    }
                    no == 'B' -> {
                        posht = Abzar.shisheh(this, 0x33D9A441.toInt(), 0x66D9A441.toInt(), 22f, 1.5f)
                        rangMatn = Abzar.RANG_TALAEI
                    }
                    no in "+-*/^!%" -> {
                        posht = Abzar.shisheh(this, 0x2E7A92AB.toInt(), 0x557A92AB.toInt(), 22f, 1.5f)
                        rangMatn = Abzar.RANG_GOLABI
                        size = 21f
                    }
                    no == 'f' -> {
                        posht = Abzar.shisheh(this, 0x262FA8C9.toInt(), 0x552FA8C9.toInt(), 22f, 1.2f)
                        rangMatn = Abzar.RANG_TIZ
                        size = 16f
                    }
                    else -> {
                        // اعداد و پرانتز — کلید سنگی مات
                        posht = Abzar.shisheh(this, Abzar.RANG_DOKME, 0x22FFFFFF, 22f, 1.2f)
                        rangMatn = Abzar.RANG_MATN
                        size = 22f
                    }
                }
                val dokme = TextView(this)
                dokme.text = label
                dokme.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, size)
                dokme.setTextColor(rangMatn)
                dokme.typeface = FontBox.vazirBold(this)
                dokme.gravity = Gravity.CENTER
                dokme.background = posht
                dokme.isClickable = true
                Abzar.fasor(dokme)
                dokme.setOnClickListener { bezan(label, no) }
                r.addView(dokme, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f).apply {
                    setMargins(Abzar.dp(this@HesabActivity, 3.5f), Abzar.dp(this@HesabActivity, 3.5f),
                        Abzar.dp(this@HesabActivity, 3.5f), Abzar.dp(this@HesabActivity, 3.5f))
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
                    natijeZende.text = Zaban.t("khata")
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
            val t = TextView(this)
            t.text = s
            t.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 12f)
            t.setTextColor(Abzar.RANG_KAMRANG)
            t.typeface = Typeface.MONOSPACE
            t.gravity = Gravity.END
            t.background = Abzar.shisheh(this, 0x261B212A, 0x1FFFFFFF, 12f, 1f)
            t.setPadding(Abzar.dp(this, 10f), Abzar.dp(this, 4f), Abzar.dp(this, 10f), Abzar.dp(this, 4f))
            val p = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            p.topMargin = Abzar.dp(this, 4f)
            ghabeTarikhche.addView(t, p)
        }
    }
}
