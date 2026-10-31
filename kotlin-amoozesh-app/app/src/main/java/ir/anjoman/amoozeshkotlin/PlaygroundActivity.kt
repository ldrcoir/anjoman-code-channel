package ir.anjoman.amoozeshkotlin

import android.app.Activity
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.InputType
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

/*
 * زمین بازی کد — مثل Tryit Editor سایت‌های آموزشی:
 * کاربر کد کاتلین می‌نویسد، دکمهٔ اجرا را می‌زند، خروجی را می‌بیند
 * و اگر خطا داشته باشد، با شمارهٔ خط و زبان ساده گزارش می‌شود.
 * با اکستراهای Intent دو حالت دارد: آزاد یا حل یک تمرین مشخص
 */
class PlaygroundActivity : Activity() {

    private var tamrinIndex = -1
    private lateinit var virayeshgar: EditText
    private lateinit var voroodi: EditText
    private lateinit var kartKhoroji: LinearLayout
    private lateinit var matnKhoroji: TextView
    private lateinit var bakhshNatije: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Abzar.rangVaredKon(this)

        tamrinIndex = intent.getIntExtra("tamrin", -1)
        val tamrin = if (tamrinIndex >= 0 && tamrinIndex < DarsHa.tamrinHa.size) {
            DarsHa.tamrinHa[tamrinIndex]
        } else null

        val root = LinearLayout(this)
        root.orientation = LinearLayout.VERTICAL
        root.layoutDirection = View.LAYOUT_DIRECTION_RTL
        root.setBackgroundColor(Abzar.RANG_ZAMIN)

        val scroll = ScrollView(this)
        scroll.isFillViewport = true
        root.addView(
            scroll,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )

        val ghabe = LinearLayout(this)
        ghabe.orientation = LinearLayout.VERTICAL
        ghabe.setPadding(Abzar.dp(this, 20f), Abzar.dp(this, 24f), Abzar.dp(this, 20f), Abzar.dp(this, 24f))
        scroll.addView(
            ghabe,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )

        // سرصفحه
        if (tamrin != null) {
            val matnShomareh = Abzar.tv(
                this,
                "تمرین ${Abzar.adadFarsi(tamrinIndex + 1)} از ${Abzar.adadFarsi(DarsHa.tamrinHa.size)}",
                14f, Abzar.RANG_TALAEI, bold = true
            )
            ghabe.addView(matnShomareh)

            val onvan = Abzar.tv(this, tamrin.onvan, 24f, Abzar.RANG_MATN)
            onvan.typeface = FontBox.vazirBlack(this)
            onvan.setPadding(0, Abzar.dp(this, 6f), 0, 0)
            ghabe.addView(onvan)

            val tozih = Abzar.tv(this, tamrin.tozih, 15f, Abzar.RANG_KAMRANG)
            tozih.setLineSpacing(Abzar.dp(this, 4f).toFloat(), 1f)
            tozih.setPadding(0, Abzar.dp(this, 10f), 0, 0)
            ghabe.addView(tozih)
        } else {
            val onvan = Abzar.tv(this, "زمین بازی کد", 26f, Abzar.RANG_MATN)
            onvan.typeface = FontBox.vazirBlack(this)
            ghabe.addView(onvan)

            val zirOnvan = Abzar.tv(this, "کد کاتلین بنویس، اجرا کن و نتیجه را ببین", 14f, Abzar.RANG_KAMRANG)
            zirOnvan.setPadding(0, Abzar.dp(this, 6f), 0, 0)
            ghabe.addView(zirOnvan)
        }

        // نوار مثال‌های آماده (فقط حالت آزاد)
        if (tamrin == null) {
            val pNamune = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            pNamune.topMargin = Abzar.dp(this, 18f)
            ghabe.addView(namuneHa(), pNamune)
        }

        // ویرایشگر کد
        virayeshgar = EditText(this)
        virayeshgar.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
        virayeshgar.typeface = Typeface.MONOSPACE
        virayeshgar.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
        virayeshgar.setTextColor(Abzar.RANG_MATN)
        virayeshgar.hint = "کد کاتلین را اینجا بنویس..."
        virayeshgar.setHintTextColor(Abzar.RANG_KHAKISTARI)
        virayeshgar.background = Abzar.kart(this, Abzar.RANG_CODE, 12f)
        virayeshgar.setPadding(
            Abzar.dp(this, 14f), Abzar.dp(this, 12f),
            Abzar.dp(this, 14f), Abzar.dp(this, 12f)
        )
        virayeshgar.textDirection = View.TEXT_DIRECTION_LTR
        virayeshgar.gravity = Gravity.START or Gravity.TOP
        virayeshgar.minLines = 9
        virayeshgar.isVerticalScrollBarEnabled = true

        val kod = intent.getStringExtra("kod")
        virayeshgar.setText(kod ?: tamrin?.codeShoro ?: nemuneShoro())

        val pVirayeshgar = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pVirayeshgar.topMargin = Abzar.dp(this, 16f)
        ghabe.addView(virayeshgar, pVirayeshgar)

        // کادر ورودی برای readln
        val matnVoroodi = Abzar.tv(this, "ورودی (هر خط، جواب یک readln)", 13f, Abzar.RANG_KAMRANG)
        val pMatnVoroodi = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pMatnVoroodi.topMargin = Abzar.dp(this, 14f)
        ghabe.addView(matnVoroodi, pMatnVoroodi)

        voroodi = EditText(this)
        voroodi.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
        voroodi.typeface = Typeface.MONOSPACE
        voroodi.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
        voroodi.setTextColor(Abzar.RANG_MATN)
        voroodi.hint = "مثلاً:\nعلی\n17"
        voroodi.setHintTextColor(Abzar.RANG_KHAKISTARI)
        voroodi.background = Abzar.kart(this, Abzar.RANG_KART, 12f)
        voroodi.setPadding(
            Abzar.dp(this, 14f), Abzar.dp(this, 10f),
            Abzar.dp(this, 14f), Abzar.dp(this, 10f)
        )
        voroodi.textDirection = View.TEXT_DIRECTION_LTR
        voroodi.gravity = Gravity.START or Gravity.TOP
        voroodi.minLines = 2
        val pVoroodi = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pVoroodi.topMargin = Abzar.dp(this, 6f)
        ghabe.addView(voroodi, pVoroodi)

        // دکمهٔ اجرا
        val dokmeEjra = Abzar.dokme(this, "▶  اجرای کد", Abzar.RANG_TIZ, Abzar.RANG_ZAMIN)
        dokmeEjra.setOnClickListener { ejraKon(tamrin) }
        val pEjra = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pEjra.topMargin = Abzar.dp(this, 18f)
        ghabe.addView(dokmeEjra, pEjra)

        // بخش نتیجه (بعد از اجرا پر می‌شود)
        bakhshNatije = LinearLayout(this)
        bakhshNatije.orientation = LinearLayout.VERTICAL
        val pNatije = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pNatije.topMargin = Abzar.dp(this, 18f)
        ghabe.addView(bakhshNatije, pNatije)

        // کارت خروجی
        kartKhoroji = LinearLayout(this)
        kartKhoroji.orientation = LinearLayout.VERTICAL
        kartKhoroji.background = Abzar.kart(this, Abzar.RANG_CODE, 12f)
        kartKhoroji.setPadding(
            Abzar.dp(this, 14f), Abzar.dp(this, 12f),
            Abzar.dp(this, 14f), Abzar.dp(this, 12f)
        )

        val sarKhoroji = Abzar.tv(this, "خروجی برنامه", 12f, Abzar.RANG_KAMRANG)
        kartKhoroji.addView(sarKhoroji)

        matnKhoroji = TextView(this)
        matnKhoroji.typeface = Typeface.MONOSPACE
        matnKhoroji.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
        matnKhoroji.setTextColor(Abzar.RANG_MATN)
        matnKhoroji.text = "دکمهٔ اجرا را بزن تا نتیجه اینجا نمایش داده شود"
        matnKhoroji.setPadding(0, Abzar.dp(this, 8f), 0, Abzar.dp(this, 4f))
        kartKhoroji.addView(matnKhoroji)

        val pKartKhoroji = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pKartKhoroji.topMargin = Abzar.dp(this, 12f)
        bakhshNatije.addView(kartKhoroji, pKartKhoroji)

        // دکمهٔ برگشت
        val dokmeBargasht = Abzar.dokme(this, "بازگشت", Abzar.RANG_TIZ, Abzar.RANG_TIZ, khateDor = true)
        dokmeBargasht.setOnClickListener { finish() }
        val pBargasht = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pBargasht.topMargin = Abzar.dp(this, 16f)
        ghabe.addView(dokmeBargasht, pBargasht)

        setContentView(root)
    }

    // چیپ‌های مثال آمادهٔ حالت آزاد
    private fun namuneHa(): LinearLayout {
        val sotoon = LinearLayout(this)
        sotoon.orientation = LinearLayout.HORIZONTAL

        val nemuneHa = listOf(
            "سلام دنیا" to """fun main() {
    println("سلام دنیا!")
}""",
            "متغیرها" to """fun main() {
    val esm = "سارا"
    var emtiaz = 10
    emtiaz += 5
    println("${'$'}esm: ${'$'}emtiaz")
}""",
            "شرط" to """fun main() {
    val sen = 15
    if (sen >= 18) println("مجازه!")
    else println("هنوز نه!")
}""",
            "حلقه" to """fun main() {
    for (i in 1..5) print("${'$'}i ")
}""",
            "تابع" to """fun jam(a: Int, b: Int): Int {
    return a + b
}

fun main() {
    println(jam(2, 3))
}""",
            "لیست" to """fun main() {
    val nomreHa = mutableListOf(18, 15)
    nomreHa.add(20)
    println(nomreHa.size)
    println(nomreHa.sum())
}""",
            "کلاس" to """class Daneshamooz(val esm: String, var nomre: Int) {
    fun taarif(): String {
        return "${'$'}esm: ${'$'}nomre"
    }
}

fun main() {
    val d = Daneshamooz("علی", 18)
    println(d.taarif())
}"""
        )

        for ((onvan, kod) in nemuneHa) {
            val chip = Abzar.dokme(this, onvan, Abzar.RANG_KHAKISTARI, Abzar.RANG_MATN)
            chip.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
            chip.setOnClickListener { virayeshgar.setText(kod); matnKhoroji.text = "کد آماده لود شد؛ اجرا کن!" }
            val pChip = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            pChip.marginEnd = Abzar.dp(this, 8f)
            sotoon.addView(chip, pChip)
        }

        val ghabe = HorizontalScrollView(this)
        ghabe.isHorizontalScrollBarEnabled = false
        ghabe.addView(sotoon)
        return ghabe
    }

    private fun nemuneShoro(): String {
        return """fun main() {
    println("سلام دنیا!")
}"""
    }

    // اجرای کد کاربر با مفسر آموزشی
    private fun ejraKon(tamrin: Tamrin?) {
        val kod = virayeshgar.text.toString()
        val matnVoroodi = voroodi.text.toString()
        val voroodiHa = if (matnVoroodi.isBlank()) emptyList() else matnVoroodi.split("\n")

        val n = Tafsir.ejra(kod, voroodiHa)
        bakhshNatije.removeAllViews()
        bakhshNatije.addView(kartKhoroji)

        if (n.movafagh) {
            val khoroji = n.khoruji.ifEmpty { "(برنامه بدون خروجی تمام شد)" }
            matnKhoroji.text = khoroji
            matnKhoroji.setTextColor(Abzar.RANG_MATN)

            // اگر تمرین هست، خروجی را با جواب مورد نظر مقایسه کن
            if (tamrin != null) {
                if (Abzar.pakKonKhoruji(n.khoruji) == Abzar.pakKonKhoruji(tamrin.khorujiMontazer)) {
                    Zakhire.tamrinAnjamKon(this, tamrinIndex)

                    val taborrik = Abzar.tv(
                        this,
                        "✅ آفرین! تمرین درست حل شد 🎉",
                        15f, Abzar.RANG_SABZ, bold = true
                    )
                    taborrik.gravity = Gravity.CENTER
                    val pTaborrik = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    pTaborrik.topMargin = Abzar.dp(this, 14f)
                    bakhshNatije.addView(taborrik, pTaborrik)

                    // اگر تمرین بعدی وجود دارد، دکمه‌اش را بگذار
                    if (tamrinIndex + 1 < DarsHa.tamrinHa.size) {
                        val dokmeBadi = Abzar.dokme(this, "تمرین بعدی", Abzar.RANG_TIZ, Abzar.RANG_ZAMIN)
                        dokmeBadi.setOnClickListener {
                            val niat = intent
                            niat.putExtra("tamrin", tamrinIndex + 1)
                            niat.removeExtra("kod")
                            startActivity(niat)
                            finish()
                        }
                        val pBadi = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                        pBadi.topMargin = Abzar.dp(this, 10f)
                        bakhshNatije.addView(dokmeBadi, pBadi)
                    } else {
                        val payan = Abzar.tv(this, "همهٔ تمرین‌ها را حل کردی! قهرمان کاتلین 🏆", 14f, Abzar.RANG_TALAEI)
                        payan.gravity = Gravity.CENTER
                        val pPayan = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                        pPayan.topMargin = Abzar.dp(this, 10f)
                        bakhshNatije.addView(payan, pPayan)
                    }
                } else {
                    val ghalat = Abzar.tv(
                        this,
                        "کد اجرا شد ولی خروجی با جواب مورد نظر تمرین فرق دارد.",
                        14f, Abzar.RANG_TALAEI, bold = true
                    )
                    val pGhalat = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    pGhalat.topMargin = Abzar.dp(this, 14f)
                    bakhshNatije.addView(ghalat, pGhalat)

                    val montazer = Abzar.tv(
                        this,
                        "خروجی منتظر:\n${tamrin.khorujiMontazer}",
                        13f, Abzar.RANG_KAMRANG
                    )
                    montazer.typeface = Typeface.MONOSPACE
                    val pMontazer = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    pMontazer.topMargin = Abzar.dp(this, 8f)
                    bakhshNatije.addView(montazer, pMontazer)
                }
            }
        } else {
            matnKhoroji.text = n.peyghamKhata
            matnKhoroji.setTextColor(Abzar.RANG_GHERMEZ)
        }
    }
}
