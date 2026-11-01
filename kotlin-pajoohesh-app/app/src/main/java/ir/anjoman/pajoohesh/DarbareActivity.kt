package ir.anjoman.pajoohesh

import android.app.Activity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

/*
 * دربارهٔ کتاب — شناسنامهٔ رسمی کتاب + امکانات اپ
 */
class DarbareActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TemEnekhab.rangVaredKon(this)
        val tem = TemEnekhab.bekhon(this)

        val root = LinearLayout(this)
        root.layoutDirection = View.LAYOUT_DIRECTION_RTL
        root.orientation = LinearLayout.VERTICAL
        root.setBackgroundColor(tem.zamin)

        val onvan = Abzar.tv(this, "ℹ️ دربارهٔ کتاب", 24f, tem.matn)
        onvan.typeface = FontBox.vazirBlack(this)
        onvan.setPadding(Abzar.dp(this, 18f), Abzar.dp(this, 20f), Abzar.dp(this, 18f), Abzar.dp(this, 4f))
        root.addView(onvan)

        val scroll = ScrollView(this)
        val ghabe = LinearLayout(this)
        ghabe.orientation = LinearLayout.VERTICAL
        ghabe.setPadding(Abzar.dp(this, 18f), Abzar.dp(this, 10f), Abzar.dp(this, 18f), Abzar.dp(this, 26f))
        scroll.addView(ghabe)
        root.addView(scroll, LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        ))

        // کارت معرفی
        val kartMo = LinearLayout(this)
        kartMo.orientation = LinearLayout.VERTICAL
        kartMo.background = Abzar.kartGradient(this, tem.asli, tem.dovom, 22f)
        kartMo.setPadding(Abzar.dp(this, 18f), Abzar.dp(this, 16f), Abzar.dp(this, 18f), Abzar.dp(this, 16f))
        kartMo.addView(Abzar.tv(this, "معرفی دستاوردهای پژوهشی", 19f, 0xFFFFFFFF.toInt(), bold = true))
        val zirMo = Abzar.tv(
            this,
            "دانش‌آموزان و هنرجویان پژوهشگر و فناور برگزیدهٔ وزارت آموزش‌وپرورش",
            13f, 0xCCFFFFFF.toInt()
        )
        zirMo.setPadding(0, Abzar.dp(this, 4f), 0, 0)
        kartMo.addView(zirMo)
        val rizMo = Abzar.tv(
            this,
            "پژوهشگاه مطالعات آموزش‌وپرورش — هفتهٔ پژوهش ۱۴۰۴",
            12.5f, 0xFFFFFFCC.toInt()
        )
        rizMo.setPadding(0, Abzar.dp(this, 4f), 0, 0)
        kartMo.addView(rizMo)
        ghabe.addView(kartMo, safP(8))

        // شناسنامهٔ کتاب
        ghabe.addView(titrBakhsh("🪪 شناسنامهٔ کتاب", tem), safP(16))
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
        val hamkaran = Abzar.tv(
            this,
            "با همکاری: قدسی احقر، معصومه صمدی، فرشته سلطان‌پور، سیده مینا صفی‌نیا، محبوبه کنشلو، محمد حسنی، رضا کلانتری، مسعود کبیری، مهدی نقوی علایی، رحمت‌اله متولی و محمد امیری",
            11.5f, tem.kamrang
        )
        hamkaran.background = Abzar.kart(this, tem.kart, 14f)
        hamkaran.setPadding(Abzar.dp(this, 14f), Abzar.dp(this, 10f), Abzar.dp(this, 14f), Abzar.dp(this, 10f))
        ghabe.addView(hamkaran, safP(6))

        // آمار کتاب در اپ
        ghabe.addView(titrBakhsh("📊 کتاب در این اپ", tem), safP(16))
        val amar = listOf(
            Triple("صفحات", "۲۱۶ صفحهٔ رنگی با کیفیت بالا", "📄"),
            Triple("بخش‌ها", "۱۱ بخش — از جلد تا فنی و حرفه‌ای", "📑"),
            Triple("جست‌وجو", "متن کل کتاب، همیشه در جیب شما", "🔍"),
            Triple("ورق‌زدن", "انیمیشن سه‌بعدی با کشیدن انگشت", "🤲"),
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

        // یادداشت پایانی
        val payan = Abzar.tv(
            this,
            "این اپ، همان کتابچهٔ رسمی هفتهٔ پژوهش است — صفحه‌به‌صفحه و بدون هیچ تغییری، فقط همیشه همراه شما. ساخته‌شده برای هفتهٔ پژوهش ۱۴۰۴ 🌟",
            12f, tem.matn
        )
        payan.background = Abzar.kartGradient(this, tem.asli, tem.dovom, 16f)
        payan.setPadding(Abzar.dp(this, 16f), Abzar.dp(this, 14f), Abzar.dp(this, 16f), Abzar.dp(this, 14f))
        ghabe.addView(payan, safP(16))

        setContentView(root)
    }

    private fun titrBakhsh(matn: String, tem: Tem): TextView {
        val t = Abzar.tv(this, matn, 16f, tem.asli, bold = true)
        t.typeface = FontBox.vazirBold(this)
        return t
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
