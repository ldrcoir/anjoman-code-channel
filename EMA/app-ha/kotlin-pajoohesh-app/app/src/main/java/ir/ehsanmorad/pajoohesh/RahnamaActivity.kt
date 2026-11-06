package ir.ehsanmorad.pajoohesh

import android.app.Activity
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

/*
 * راهنما — امکانات اپ
 */
class RahnamaActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TemEnekhab.rangVaredKon(this)
        val tem = TemEnekhab.bekhon(this)

        val rishe = LinearLayout(this)
        rishe.layoutDirection = View.LAYOUT_DIRECTION_RTL
        rishe.orientation = LinearLayout.VERTICAL
        rishe.setBackgroundColor(tem.zamin)

        // ---------- سربرگ با آیکون ----------
        val sar = LinearLayout(this)
        sar.orientation = LinearLayout.HORIZONTAL
        sar.gravity = Gravity.CENTER_VERTICAL
        sar.setPadding(Abzar.dp(this, 18f), Abzar.dp(this, 18f), Abzar.dp(this, 18f), Abzar.dp(this, 6f))
        val neshaneSar = Abzar.neshane(this, "ic_rahnama", tem.asli, 0)
        neshaneSar.scaleType = ImageView.ScaleType.FIT_CENTER
        sar.addView(neshaneSar, LinearLayout.LayoutParams(Abzar.dp(this, 26f), Abzar.dp(this, 26f)))
        val onvan = TextView(this)
        onvan.text = "راهنما"
        onvan.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26f)
        onvan.setTextColor(tem.matn)
        onvan.typeface = Fontha.lalezar(this)
        val pOnvan = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
        pOnvan.marginStart = Abzar.dp(this, 10f)
        sar.addView(onvan, pOnvan)
        rishe.addView(sar)
        rishe.addView(Abzar.nobarZiba(this, tem.asli, tem.dovom, 3))

        val lafand = ScrollView(this)
        val ghabe = LinearLayout(this)
        ghabe.orientation = LinearLayout.VERTICAL
        ghabe.setPadding(Abzar.dp(this, 18f), Abzar.dp(this, 12f), Abzar.dp(this, 18f), Abzar.dp(this, 26f))
        lafand.addView(ghabe)
        rishe.addView(lafand, LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        ))

        // ---------- امکانات اپ ----------
        ghabe.addView(Abzar.titrBakhsh(this, "امکانات اپ", tem), safP(8))
        val amar = listOf(
            Triple("بخش‌ها", "از جلد تا فنی و حرفه‌ای، با فهرست زنده", "📑"),
            Triple("جست‌وجو", "متن کل کتاب، همیشه در جیب شما", "🔍"),
            Triple("ورق‌خوردن", "طبیعی و نرم با صدای کاغذ", "🤲"),
            Triple("شب‌خوانی", "فیلتر وارونهٔ ملایم برای تاریکی", "🌙"),
            Triple("آفلاین", "بدون اینترنت، بدون هیچ مجوزی", "🛡️")
        )
        var saf: LinearLayout? = null
        for ((i, a) in amar.withIndex()) {
            if (i % 2 == 0) {
                saf = LinearLayout(this)
                saf.orientation = LinearLayout.HORIZONTAL
            }
            val kart = LinearLayout(this)
            kart.orientation = LinearLayout.VERTICAL
            kart.gravity = Gravity.CENTER
            kart.background = Abzar.kart(this, tem.kart, 16f)
            kart.setPadding(Abzar.dp(this, 8f), Abzar.dp(this, 12f), Abzar.dp(this, 8f), Abzar.dp(this, 12f))
            val neshane = Abzar.tv(this, a.third, 20f, tem.asli)
            neshane.gravity = Gravity.CENTER
            kart.addView(neshane)
            val titr = Abzar.tv(this, a.first, 12.5f, tem.matn, bold = true)
            titr.gravity = Gravity.CENTER
            kart.addView(titr)
            val tozih = Abzar.tv(this, a.second, 10.5f, tem.kamrang)
            tozih.gravity = Gravity.CENTER
            kart.addView(tozih)
            val pK = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
            pK.marginStart = Abzar.dp(this, 4f)
            pK.marginEnd = Abzar.dp(this, 4f)
            saf!!.addView(kart, pK)
            if (i % 2 == 1) ghabe.addView(saf, safP(8))
        }

        setContentView(rishe)
    }

    private fun safP(t: Int): LinearLayout.LayoutParams {
        val p = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        p.topMargin = Abzar.dp(this, t.toFloat())
        return p
    }
}
