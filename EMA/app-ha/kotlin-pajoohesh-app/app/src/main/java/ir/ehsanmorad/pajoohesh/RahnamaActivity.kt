package ir.ehsanmorad.pajoohesh

import android.app.Activity
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

/*
 * راهنما — سربرگ پژوهشسرا، شناسنامهٔ رسمی کتاب و امکانات اپ
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

        // ---------- کارت پژوهشسرا (بالای همهٔ مشخصات) ----------
        val kartSara = LinearLayout(this)
        kartSara.orientation = LinearLayout.VERTICAL
        kartSara.gravity = Gravity.CENTER
        kartSara.background = Abzar.kartGradient(this, tem.asli, tem.dovom, 22f)
        kartSara.setPadding(Abzar.dp(this, 16f), Abzar.dp(this, 18f), Abzar.dp(this, 16f), Abzar.dp(this, 18f))

        val esmSara = TextView(this)
        esmSara.text = "پژوهشسرای امام هادی (ع)"
        esmSara.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f)
        esmSara.setTextColor(0xFFFFFFFF.toInt())
        esmSara.typeface = Fontha.lalezar(this)
        esmSara.gravity = Gravity.CENTER
        kartSara.addView(esmSara)

        val khatJoda = View(this)
        khatJoda.background = Abzar.kart(this, 0x55FFFFFF.toInt(), 2f)
        val pKhat = LinearLayout.LayoutParams(Abzar.dp(this, 150f), Abzar.dp(this, 1.6f))
        pKhat.topMargin = Abzar.dp(this, 10f)
        kartSara.addView(khatJoda, pKhat)

        val modir = TextView(this)
        modir.text = "مدیریت پژوهشسرا:  دکتر زهرا مروتی"
        modir.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
        modir.setTextColor(0xFFFFFFFF.toInt())
        modir.typeface = Fontha.vazir(this)
        modir.gravity = Gravity.CENTER
        modir.setPadding(0, Abzar.dp(this, 9f), 0, 0)
        kartSara.addView(modir)

        val tarrah = TextView(this)
        tarrah.text = "طراح و برنامه‌نویس:  مهندس احسان مراد"
        tarrah.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
        tarrah.setTextColor(0xFFFFFFFF.toInt())
        tarrah.typeface = Fontha.vazir(this)
        tarrah.gravity = Gravity.CENTER
        tarrah.setPadding(0, Abzar.dp(this, 4f), 0, 0)
        kartSara.addView(tarrah)

        ghabe.addView(kartSara, safP(8))

        // ---------- شناسنامهٔ کتاب ----------
        ghabe.addView(Abzar.titrBakhsh(this, "شناسنامهٔ کتاب", tem), safP(18))
        val shenasoname = listOf(
            "تهیه و تدوین" to "پژوهشگاه مطالعات آموزش‌وپرورش",
            "تحت نظر" to "دکتر علی محبی",
            "به اهتمام" to "دکتر شهره حسین‌پور",
            "طراحی و صفحه‌آرا" to "محمد امیری",
            "سال چاپ" to "۱۴۰۴",
            "تیراژ" to "۵۰ نسخه"
        )
        for ((titr, meghdar) in shenasoname) {
            ghabe.addView(radeSade(titr, meghdar, tem), safP(6))
        }

        // ---------- همکاران — متن سفید روی کارت گرادیانی ----------
        val kartHamkaran = LinearLayout(this)
        kartHamkaran.orientation = LinearLayout.VERTICAL
        kartHamkaran.background = Abzar.kartGradient(this, tem.asli, tem.dovom, 16f)
        kartHamkaran.setPadding(Abzar.dp(this, 14f), Abzar.dp(this, 12f), Abzar.dp(this, 14f), Abzar.dp(this, 12f))
        val sarHamkaran = TextView(this)
        sarHamkaran.text = "با همکاری"
        sarHamkaran.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
        sarHamkaran.setTextColor(0xFFFFFFFF.toInt())
        sarHamkaran.typeface = Fontha.vazirPorReng(this)
        kartHamkaran.addView(sarHamkaran)
        val hamkaran = TextView(this)
        hamkaran.text =
            "قدسی احقر، معصومه صمدی، فرشته سلطان‌پور، سیده مینا صفی‌نیا، محبوبه کنشلو، محمد حسنی، رضا کلانتری، مسعود کبیری، مهدی نقوی علایی، رحمت‌اله متولی و محمد امیری"
        hamkaran.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
        hamkaran.setTextColor(0xFFFFFFFF.toInt())
        hamkaran.typeface = Fontha.vazir(this)
        hamkaran.setPadding(0, Abzar.dp(this, 5f), 0, 0)
        kartHamkaran.addView(hamkaran)
        ghabe.addView(kartHamkaran, safP(10))

        // ---------- امکانات اپ ----------
        ghabe.addView(Abzar.titrBakhsh(this, "امکانات اپ", tem), safP(18))
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

        // ---------- پایین ----------
        val payin = TextView(this)
        payin.text = "EhsanMorad.ir"
        payin.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11.5f)
        payin.setTextColor(tem.kamrang)
        payin.typeface = Fontha.vazir(this)
        payin.gravity = Gravity.CENTER
        ghabe.addView(payin, safP(18))

        setContentView(rishe)
    }

    private fun radeSade(titr: String, meghdar: String, tem: Tem): View {
        val rade = LinearLayout(this)
        rade.orientation = LinearLayout.HORIZONTAL
        rade.gravity = Gravity.CENTER_VERTICAL
        rade.background = Abzar.kart(this, tem.kart, 14f)
        rade.setPadding(Abzar.dp(this, 14f), Abzar.dp(this, 10f), Abzar.dp(this, 14f), Abzar.dp(this, 10f))
        val t1 = Abzar.tv(this, titr, 13f, tem.kamrang)
        rade.addView(t1, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))
        rade.addView(Abzar.tv(this, meghdar, 13.5f, tem.matn, bold = true))
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
