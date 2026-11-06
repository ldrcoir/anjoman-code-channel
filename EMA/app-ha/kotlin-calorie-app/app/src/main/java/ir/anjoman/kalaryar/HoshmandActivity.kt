package ir.anjoman.kalaryar

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

/*
 * گفت‌وگو با دستیار هوشمند — تمام محاسبات داخل خود گوشی
 * بادکنک‌های سبز: دستیار؛ بادکنک‌های تیره: شما
 */
class HoshmandActivity : Activity() {

    private lateinit var ghabe: LinearLayout
    private lateinit var scroll: ScrollView
    private lateinit var edtSoal: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Abzar.rangVaredKon(this)

        val root = LinearLayout(this)
        root.layoutDirection = View.LAYOUT_DIRECTION_RTL
        root.setBackgroundColor(Abzar.RANG_ZAMIN)
        root.orientation = LinearLayout.VERTICAL

        // سرصفحه
        val sar = LinearLayout(this)
        sar.orientation = LinearLayout.VERTICAL
        sar.background = Abzar.kartGradient(this, 0xFF123326.toInt(), Abzar.RANG_KART, 0f)
        sar.setPadding(Abzar.dp(this, 18f), Abzar.dp(this, 14f), Abzar.dp(this, 18f), Abzar.dp(this, 14f))
        sar.addView(Abzar.tv(this, "🤖 دستیار هوشمند کالری‌یار", 18f, Abzar.RANG_SABZ, bold = true))
        sar.addView(Abzar.tv(this, "آنلاین و آفلاین؛ همه‌چیز داخل خود گوشی‌ات حساب می‌شود", 12f, Abzar.RANG_KAMRANG))
        root.addView(sar)

        // فهرست پیام‌ها
        scroll = ScrollView(this)
        scroll.isFillViewport = true
        root.addView(scroll, LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f
        ))

        ghabe = LinearLayout(this)
        ghabe.orientation = LinearLayout.VERTICAL
        ghabe.setPadding(Abzar.dp(this, 14f), Abzar.dp(this, 14f), Abzar.dp(this, 14f), Abzar.dp(this, 10f))
        scroll.addView(ghabe, ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ))

        // نوار ورودی
        val safVaroodi = LinearLayout(this)
        safVaroodi.orientation = LinearLayout.HORIZONTAL
        safVaroodi.gravity = Gravity.CENTER_VERTICAL
        safVaroodi.setBackgroundColor(Abzar.RANG_CODE)
        safVaroodi.setPadding(Abzar.dp(this, 10f), Abzar.dp(this, 8f), Abzar.dp(this, 10f), Abzar.dp(this, 8f))

        edtSoal = EditText(this)
        edtSoal.hint = "سؤالت را بنویس..."
        edtSoal.setTextColor(Abzar.RANG_MATN)
        edtSoal.setHintTextColor(Abzar.RANG_KAMRANG)
        edtSoal.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
        edtSoal.typeface = FontBox.vazir(this)
        edtSoal.inputType = InputType.TYPE_CLASS_TEXT
        edtSoal.background = Abzar.kart(this, Abzar.RANG_KART, 12f)
        edtSoal.setPadding(Abzar.dp(this, 14f), Abzar.dp(this, 10f), Abzar.dp(this, 14f), Abzar.dp(this, 10f))
        edtSoal.maxLines = 3
        safVaroodi.addView(edtSoal, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))

        val dokmeErsal = TextView(this)
        dokmeErsal.text = "➤"
        dokmeErsal.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22f)
        dokmeErsal.setTextColor(Color.WHITE)
        dokmeErsal.gravity = Gravity.CENTER
        dokmeErsal.typeface = FontBox.vazirBold(this)
        dokmeErsal.background = Abzar.kartGradient(this, Abzar.RANG_SABZ, Abzar.RANG_SABZKAM, 14f)
        dokmeErsal.isClickable = true
        Abzar.lams(dokmeErsal)
        dokmeErsal.setOnClickListener { ersalKon() }
        val pErsal = LinearLayout.LayoutParams(Abzar.dp(this, 48f), Abzar.dp(this, 48f))
        pErsal.marginStart = Abzar.dp(this, 8f)
        safVaroodi.addView(dokmeErsal, pErsal)

        root.addView(safVaroodi, LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ))

        setContentView(root)
        khooshAmad()
    }

    private fun khooshAmad() {
        javabHoshmand(
            "سلام! من دستیار هوشمند کالری‌یارم 🤖\nاز من بپرس: کالری هر غذا، «الان چی بخورم؟»، «می‌تونم پیتزا بخورم؟»، وضعیت امروزم، وزنم چطوره...",
            false
        )
        // چیپ‌های پیشنهادی
        val safChip = LinearLayout(this)
        safChip.orientation = LinearLayout.VERTICAL
        val chipHa = listOf(
            "الان چی بخورم؟",
            "وضعیت امروزم چطوره؟",
            "کالری پیتزا چقدره؟",
            "می‌تونم برنج بخورم؟"
        )
        for (chip in chipHa) {
            val d = TextView(this)
            d.text = chip
            d.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
            d.setTextColor(Abzar.RANG_SABZ)
            d.typeface = FontBox.vazirBold(this)
            d.background = Abzar.kartKhatei(this, Abzar.RANG_SABZ, 14f)
            d.setPadding(Abzar.dp(this, 14f), Abzar.dp(this, 8f), Abzar.dp(this, 14f), Abzar.dp(this, 8f))
            d.isClickable = true
            Abzar.lams(d)
            d.setOnClickListener {
                edtSoal.setText(chip)
                ersalKon()
            }
            val pD = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
            pD.topMargin = Abzar.dp(this, 6f)
            pD.gravity = Gravity.START
            safChip.addView(d, pD)
        }
        val pChip = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pChip.topMargin = Abzar.dp(this, 10f)
        ghabe.addView(safChip, pChip)
        paeenBero()
    }

    private fun ersalKon() {
        val matn = edtSoal.text.toString().trim()
        if (matn.isEmpty()) return
        edtSoal.setText("")
        javabHoshmand(matn, true)

        // کمی تأخیر طبیعی برای حس گفت‌وگو
        ghabe.postDelayed({
            val javabHa = HoshmandYar.javab(this, matn)
            for (j in javabHa) {
                javabHoshmand(j, false)
            }
        }, 350)
    }

    // افزودن یک بادکنک پیام
    private fun javabHoshmand(matn: String, azMan: Boolean) {
        val saf = LinearLayout(this)
        saf.orientation = LinearLayout.HORIZONTAL

        val badkonak = TextView(this)
        badkonak.text = matn
        badkonak.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
        badkonak.typeface = FontBox.vazir(this)
        badkonak.setPadding(Abzar.dp(this, 14f), Abzar.dp(this, 10f), Abzar.dp(this, 14f), Abzar.dp(this, 10f))
        badkonak.setTextColor(Abzar.RANG_MATN)
        badkonak.maxWidth = Abzar.dp(this, 280f)

        if (azMan) {
            // پیام شما: سمت چپ صفحه (در راست‌چین) با کارت خنثی
            badkonak.background = Abzar.kart(this, Abzar.RANG_KHAKISTARI, 16f)
            saf.gravity = Gravity.END
        } else {
            // پیام دستیار: سمت راست صفحه با کارت سبزِ تیره
            badkonak.background = Abzar.kartGradient(this, 0xFF123326.toInt(), Abzar.RANG_CODE, 16f)
            saf.gravity = Gravity.START
        }

        val pHaf = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pHaf.topMargin = Abzar.dp(this, 8f)
        saf.addView(badkonak)
        ghabe.addView(saf, pHaf)
        paeenBero()
    }

    private fun paeenBero() {
        ghabe.post {
            scroll.fullScroll(View.FOCUS_DOWN)
        }
    }
}
