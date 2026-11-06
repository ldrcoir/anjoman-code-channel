package ir.anjoman.sandogh

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

/*
 * فهرست مطالب کتابچه — فصل‌ها با توضیح؛ تم‌ها هم از همین‌جا
 */
class FehrestActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TemEnekhab.rangVaredKon(this)
        val tem = TemEnekhab.bekhon(this)
        val dane = Ketabche.bekhon(this)

        val root = LinearLayout(this)
        root.layoutDirection = View.LAYOUT_DIRECTION_RTL
        root.orientation = LinearLayout.VERTICAL
        root.setBackgroundColor(tem.zamin)

        // سربرگ
        val sar = LinearLayout(this)
        sar.orientation = LinearLayout.HORIZONTAL
        sar.gravity = Gravity.CENTER_VERTICAL
        sar.setPadding(Abzar.dp(this, 18f), Abzar.dp(this, 18f), Abzar.dp(this, 18f), Abzar.dp(this, 6f))
        val onvan = Abzar.tv(this, "📖 فهرست مطالب", 24f, tem.matn)
        onvan.typeface = FontBox.vazirBlack(this)
        sar.addView(onvan, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))

        val dokmeTem = Abzar.tv(this, "🎨 تم‌ها", 14f, tem.asli, bold = true)
        dokmeTem.setPadding(Abzar.dp(this, 14f), Abzar.dp(this, 8f), Abzar.dp(this, 14f), Abzar.dp(this, 8f))
        dokmeTem.background = Abzar.kartKhatei(this, tem.asli, 0, 14f)
        dokmeTem.gravity = Gravity.CENTER
        Abzar.lams(dokmeTem)
        dokmeTem.setOnClickListener { startActivity(Intent(this, TemHaActivity::class.java)) }
        sar.addView(dokmeTem)
        root.addView(sar)
        root.addView(Abzar.nobarZiba(this, tem.asli, tem.dovom, 3))

        val scroll = ScrollView(this)
        scroll.isFillViewport = true
        val ghabe = LinearLayout(this)
        ghabe.orientation = LinearLayout.VERTICAL
        ghabe.setPadding(Abzar.dp(this, 18f), Abzar.dp(this, 12f), Abzar.dp(this, 18f), Abzar.dp(this, 26f))
        scroll.addView(ghabe)
        root.addView(scroll)

        // کارت خلاصهٔ صندوق
        if (dane.shakhs.isNotEmpty()) {
            val kh = LinearLayout(this)
            kh.orientation = LinearLayout.VERTICAL
            kh.background = Abzar.kartGradient(this, tem.asli, tem.dovom, 22f)
            kh.setPadding(Abzar.dp(this, 18f), Abzar.dp(this, 16f), Abzar.dp(this, 18f), Abzar.dp(this, 16f))
            val sarKh = Abzar.tv(this, "💡 یک نگاه", 14f, 0xB3FFFFFF.toInt(), bold = true)
            kh.addView(sarKh)
            val megdar = Abzar.tv(
                this,
                "پس‌انداز صندوق: ${Abzar.adadJoda(dane.majmoo?.pasandaz ?: 0)} تومان",
                19f, 0xFFFFFFFF.toInt(), bold = true
            )
            megdar.setPadding(0, Abzar.dp(this, 6f), 0, 0)
            kh.addView(megdar)
            val riz = Abzar.tv(
                this,
                "${Abzar.adadFarsi(dane.shakhs.size)} عضو • تسویهٔ کل: ${Abzar.adadFarsi(dane.majmoo?.tasvie ?: 0.0)} • سهم هرکس در جدول",
                12f, 0xCCFFFFFF.toInt()
            )
            riz.setPadding(0, Abzar.dp(this, 4f), 0, 0)
            kh.addView(riz)
            ghabe.addView(kh, safP(10))

            // انیمیشن ورود
            kh.alpha = 0f
            kh.animate().alpha(1f).setDuration(300).start()
        }

        // فصل‌ها
        val faslha = listOf(
            Triple("۱. شناسنامهٔ صندوق", "جمع‌کل همهٔ ستون‌ها در یک نگاه", 0),
            Triple("۲. تسویهٔ دلار اعضا", "جدول کامل اعضا با جست‌وجو — یورو، دلار، سکه، تتر، USDC و دای", 1),
            Triple("۳. سهم و پس‌انداز", "نمودار دایره‌ای سهم اعضا و پس‌انداز تومانی هر نفر", 2),
            Triple("۴. تسویهٔ ارز دیجیتال و دلار", "چه کسانی تسویه شده‌اند و چقدر", 3),
            Triple("۵. تتر خریداری‌شده از سایرین", "یادداشت قرمز کتابچه", 4),
            Triple("۶. راهنمای کتابچه", "معنای ستون‌ها و توضیح اعداد", 5)
        )

        for ((onvanF, tozihF, shomare) in faslha) {
            ghabe.addView(radeFasl(onvanF, tozihF, shomare, tem), safP(10))
        }

        setContentView(root)
    }

    private fun radeFasl(onvan: String, tozih: String, shomare: Int, tem: Tem): View {
        val rade = LinearLayout(this)
        rade.orientation = LinearLayout.HORIZONTAL
        rade.gravity = Gravity.CENTER_VERTICAL
        rade.background = Abzar.kart(this, tem.kart, 18f)
        rade.setPadding(Abzar.dp(this, 16f), Abzar.dp(this, 14f), Abzar.dp(this, 16f), Abzar.dp(this, 14f))
        rade.isClickable = true
        Abzar.lams(rade)

        val shomareh = TextView(this)
        shomareh.text = Abzar.adadFarsi(shomare + 1)
        shomareh.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16f)
        shomareh.setTextColor(0xFFFFFFFF.toInt())
        shomareh.typeface = FontBox.vazirBlack(this)
        shomareh.gravity = Gravity.CENTER
        shomareh.background = Abzar.dayereh(this, tem.asli)
        rade.addView(shomareh, LinearLayout.LayoutParams(Abzar.dp(this, 36f), Abzar.dp(this, 36f)))

        val sotoon = LinearLayout(this)
        sotoon.orientation = LinearLayout.VERTICAL
        val matnOnvan = Abzar.tv(this, onvan, 16f, tem.matn, bold = true)
        sotoon.addView(matnOnvan)
        val matnTozih = Abzar.tv(this, tozih, 12f, tem.kamrang)
        matnTozih.setPadding(0, Abzar.dp(this, 3f), 0, 0)
        sotoon.addView(matnTozih)
        rade.addView(sotoon, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))

        val flish = Abzar.tv(this, "‹", 22f, tem.kamrang, bold = true)
        rade.addView(flish)

        rade.setOnClickListener {
            val i = Intent(this, KhavanakActivity::class.java)
            i.putExtra("safhe", shomare)
            startActivity(i)
        }
        return rade
    }

    private fun safP(t: Int): LinearLayout.LayoutParams {
        val p = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        p.topMargin = Abzar.dp(this, t.toFloat())
        return p
    }
}
