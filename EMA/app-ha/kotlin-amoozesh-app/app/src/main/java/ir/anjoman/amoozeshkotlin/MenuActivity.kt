package ir.anjoman.amoozeshkotlin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

/*
 * منوی اصلی اپ — همهٔ بخش‌ها یک‌جا
 * از دکمهٔ ☰ صفحهٔ اصلی باز می‌شود
 */
class MenuActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Abzar.rangVaredKon(this)
        setContentView(safheBekon())
    }

    override fun onResume() {
        super.onResume()
        setContentView(safheBekon())
    }

    private fun safheBekon(): View {
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
        val onvan = Abzar.tv(this, "منو", 30f, Abzar.RANG_MATN)
        onvan.typeface = FontBox.vazirBlack(this)
        ghabe.addView(onvan)

        val zirOnvan = Abzar.tv(this, "همهٔ بخش‌های اپ اینجاست", 14f, Abzar.RANG_KAMRANG)
        zirOnvan.setPadding(0, Abzar.dp(this, 6f), 0, 0)
        ghabe.addView(zirOnvan)

        // وضعیت کلی
        var majmoo = 0
        var hadaf = 0
        for (d in DarsHa.darsList) {
            val n = Zakhire.behtarin(this, d.shomareh)
            if (n > 0) majmoo += n
            hadaf += d.soalHa.size
        }
        val kartVaziat = LinearLayout(this)
        kartVaziat.orientation = LinearLayout.VERTICAL
        kartVaziat.background = Abzar.kart(this, Abzar.RANG_CODE, 14f)
        kartVaziat.setPadding(Abzar.dp(this, 14f), Abzar.dp(this, 12f), Abzar.dp(this, 14f), Abzar.dp(this, 12f))
        kartVaziat.addView(
            Abzar.tv(
                this,
                "وضعیت تو: ${Abzar.adadFarsi(majmoo)} از ${Abzar.adadFarsi(hadaf)} سؤال درست • تم: ${TemHa.faeli.esm}",
                13f, Abzar.RANG_TALAEI, bold = true
            )
        )
        val pVaziat = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pVaziat.topMargin = Abzar.dp(this, 16f)
        ghabe.addView(kartVaziat, pVaziat)

        // آیتم‌های منو
        ghabe.addView(
            itemKart("📚", "درس‌ها", "${Abzar.adadFarsi(DarsHa.darsList.size)} درس در ۶ فصل — از مقدمه تا پیشرفته", "dars"),
            pKart()
        )
        ghabe.addView(
            itemKart("⌨", "زمین بازی کد", "هر کدی بنویسی همان‌جا اجرا می‌شود؛ با خطایابی فارسی", "zamin"),
            pKart()
        )
        ghabe.addView(
            itemKart("✏", "تمرین‌های عملی", "${Abzar.adadFarsi(Zakhire.tamrinAnjamShode(this))} از ${Abzar.adadFarsi(DarsHa.tamrinHa.size)} تمرین حل شده", "tamrin"),
            pKart()
        )
        ghabe.addView(
            itemKart("🏅", "آزمون جامع", "۱۵ سؤال تصادفی از همهٔ درس‌ها؛ نمره از ۲۰", "jame"),
            pKart()
        )
        ghabe.addView(
            itemKart("🎨", "تم و ظاهر", "۶ تم رنگی؛ کل اپ با یک لمس عوض می‌شود", "tem"),
            pKart()
        )
        ghabe.addView(
            itemKart("ℹ", "دربارهٔ ما", "انجمن کدنویسی مدرسهٔ شهید فقیهی", "darbare"),
            pKart()
        )

        return root
    }

    private fun pKart(): LinearLayout.LayoutParams {
        val p = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        p.topMargin = Abzar.dp(this, 12f)
        return p
    }

    // کارت یک آیتم منو: آیکن + عنوان + توضیح + فلش
    private fun itemKart(aykoon: String, onvan: String, tozih: String, noo: String): LinearLayout {
        val kart = LinearLayout(this)
        kart.orientation = LinearLayout.HORIZONTAL
        kart.gravity = Gravity.CENTER_VERTICAL
        kart.background = Abzar.kart(this, Abzar.RANG_KART)
        kart.setPadding(
            Abzar.dp(this, 14f), Abzar.dp(this, 16f),
            Abzar.dp(this, 14f), Abzar.dp(this, 16f)
        )
        kart.isClickable = true
        Abzar.lams(kart)
        kart.setOnClickListener { bazKon(noo) }

        // دایرهٔ آیکن
        val dayereh = TextView(this)
        dayereh.text = aykoon
        dayereh.gravity = Gravity.CENTER
        dayereh.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
        dayereh.background = Abzar.dayereh(this, Abzar.RANG_CODE)
        val pDayereh = LinearLayout.LayoutParams(Abzar.dp(this, 50f), Abzar.dp(this, 50f))
        kart.addView(dayereh, pDayereh)

        val sotoon = LinearLayout(this)
        sotoon.orientation = LinearLayout.VERTICAL
        val matnOnvan = Abzar.tv(this, onvan, 17f, Abzar.RANG_MATN, bold = true)
        sotoon.addView(matnOnvan)
        val matnTozih = Abzar.tv(this, tozih, 13f, Abzar.RANG_KAMRANG)
        matnTozih.setPadding(0, Abzar.dp(this, 3f), 0, 0)
        sotoon.addView(matnTozih)
        val pSotoon = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
        pSotoon.marginStart = Abzar.dp(this, 14f)
        kart.addView(sotoon, pSotoon)

        val flash = Abzar.tv(this, "‹", 22f, Abzar.RANG_KAMRANG)
        kart.addView(flash)

        return kart
    }

    private fun bazKon(noo: String) {
        when (noo) {
            "dars" -> startActivity(Intent(this, HomeActivity::class.java))
            "zamin" -> startActivity(Intent(this, PlaygroundActivity::class.java))
            "tamrin" -> startActivity(Intent(this, TamrinActivity::class.java))
            "jame" -> {
                val niat = Intent(this, QuizActivity::class.java)
                niat.putExtra("azmoon_jame", true)
                startActivity(niat)
            }
            "tem" -> startActivity(Intent(this, TemActivity::class.java))
            "darbare" -> startActivity(Intent(this, AboutActivity::class.java))
        }
    }
}
