package ir.anjoman.amoozeshkotlin

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

/*
 * صفحهٔ هر درس: توضیح + نمونه کد توی کارت تیره + دکمهٔ آزمون
 * شمارهٔ درس رو با Intent اکسترا می‌گیریم
 */
class LessonActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Abzar.rangVaredKon(this)

        val shomareh = intent.getIntExtra("shomareh_dars", 1)
        val dars = DarsHa.baShomareh(shomareh)

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

        // «درس ۱ از ۵»
        val matnShomareh = Abzar.tv(
            this,
            "درس ${Abzar.adadFarsi(dars.shomareh)} از ${Abzar.adadFarsi(DarsHa.darsList.size)}",
            14f, Abzar.RANG_TALAEI, bold = true
        )
        ghabe.addView(matnShomareh)

        // عنوان درس
        val onvan = Abzar.tv(this, dars.onvan, 24f, Abzar.RANG_MATN)
        onvan.typeface = FontBox.vazirBlack(this)
        onvan.setPadding(0, Abzar.dp(this, 6f), 0, 0)
        ghabe.addView(onvan)

        // پاراگراف‌های توضیح
        for (paragraf in dars.tozihat) {
            val t = Abzar.tv(this, paragraf, 15f, Abzar.RANG_MATN)
            t.setLineSpacing(Abzar.dp(this, 4f).toFloat(), 1f)
            t.setPadding(0, Abzar.dp(this, 14f), 0, 0)
            ghabe.addView(t)
        }

        // کارت کد با فونت mono و چپ‌چین
        val pCode = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pCode.topMargin = Abzar.dp(this, 20f)
        ghabe.addView(kartCode(dars.codeNemooneh), pCode)

        // اگه قبلاً آزمون دادیم، بهترین نمره رو نشون بده
        val behtarin = Zakhire.behtarin(this, dars.shomareh)
        if (behtarin >= 0) {
            val matnBehtarin = Abzar.tv(
                this,
                "بهترین نتیجهٔ آزمون: ${Abzar.adadFarsi(behtarin)} از ۳",
                13f, Abzar.RANG_KAMRANG
            )
            val pBehtarin = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            pBehtarin.topMargin = Abzar.dp(this, 16f)
            ghabe.addView(matnBehtarin, pBehtarin)
        }

        // دکمهٔ شروع آزمون
        val dokmeAzmoon = Abzar.dokme(this, "شروع آزمون", Abzar.RANG_TIZ, Abzar.RANG_ZAMIN)
        dokmeAzmoon.setOnClickListener {
            val niat = Intent(this, QuizActivity::class.java)
            niat.putExtra("shomareh_dars", dars.shomareh)
            startActivity(niat)
        }
        val pAzmoon = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pAzmoon.topMargin = Abzar.dp(this, 24f)
        ghabe.addView(dokmeAzmoon, pAzmoon)

        // دکمهٔ برگشت
        val dokmeBargasht = Abzar.dokme(this, "بازگشت به درس‌ها", Abzar.RANG_TIZ, Abzar.RANG_TIZ, khateDor = true)
        dokmeBargasht.setOnClickListener { finish() }
        val pBargasht = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pBargasht.topMargin = Abzar.dp(this, 12f)
        ghabe.addView(dokmeBargasht, pBargasht)

        setContentView(root)
    }

    // کارت تیرهٔ نمونه کد؛ LTR تا کد به‌هم نریزه
    private fun kartCode(code: String): LinearLayout {
        val kart = LinearLayout(this)
        kart.orientation = LinearLayout.VERTICAL
        kart.background = Abzar.kart(this, Abzar.RANG_CODE, 12f)
        kart.setPadding(
            Abzar.dp(this, 16f), Abzar.dp(this, 14f),
            Abzar.dp(this, 16f), Abzar.dp(this, 14f)
        )

        val ghabeCode = HorizontalScrollView(this)
        ghabeCode.isHorizontalScrollBarEnabled = false

        val matnCode = TextView(this)
        matnCode.text = code
        matnCode.typeface = Typeface.MONOSPACE
        matnCode.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
        matnCode.setTextColor(Abzar.RANG_MATN)
        matnCode.textDirection = View.TEXT_DIRECTION_LTR
        matnCode.gravity = Gravity.START
        matnCode.setLineSpacing(Abzar.dp(this, 3f).toFloat(), 1f)

        ghabeCode.addView(
            matnCode,
            FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )
        kart.addView(ghabeCode)
        return kart
    }
}
