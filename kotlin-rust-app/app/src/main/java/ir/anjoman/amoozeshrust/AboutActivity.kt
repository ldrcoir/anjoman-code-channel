package ir.anjoman.amoozeshrust

import android.app.Activity
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView

/*
 * صفحهٔ درباره — برند EmaRoid طبق خواستهٔ مالک
 */
class AboutActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Abzar.rangVaredKon(this)

        val root = FrameLayout(this)
        root.layoutDirection =
            if (Zaban.rastChin(this)) View.LAYOUT_DIRECTION_RTL else View.LAYOUT_DIRECTION_LTR
        root.background = NagshZamin(0xFF241610.toInt(), 0xFF140D08.toInt())

        val vasat = LinearLayout(this)
        vasat.orientation = LinearLayout.VERTICAL
        vasat.gravity = Gravity.CENTER
        vasat.setPadding(
            Abzar.dp(this, 28f), Abzar.dp(this, 24f),
            Abzar.dp(this, 28f), Abzar.dp(this, 24f)
        )
        root.addView(
            vasat, FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER
            )
        )

        // کارت اصلی
        val kart = LinearLayout(this)
        kart.orientation = LinearLayout.VERTICAL
        kart.gravity = Gravity.CENTER_HORIZONTAL
        kart.background = Abzar.kart(this)
        kart.setPadding(
            Abzar.dp(this, 24f), Abzar.dp(this, 28f),
            Abzar.dp(this, 24f), Abzar.dp(this, 28f)
        )

        // نشان EmaRoid: مربع گرد با حرف E
        val ghabeMark = FrameLayout(this)
        val mark = View(this)
        mark.background = Abzar.kart(this, Abzar.RANG_TIZ, 16f)
        ghabeMark.addView(
            mark, FrameLayout.LayoutParams(Abzar.dp(this, 64f), Abzar.dp(this, 64f), Gravity.CENTER)
        )
        val harf = TextView(this)
        harf.text = "E"
        harf.setTextColor(Abzar.RANG_ZAMIN)
        harf.typeface = FontBox.vazirBlack(this)
        harf.setTextSize(TypedValue.COMPLEX_UNIT_SP, 34f)
        harf.gravity = Gravity.CENTER
        ghabeMark.addView(
            harf, FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER
            )
        )
        kart.addView(ghabeMark, LinearLayout.LayoutParams(Abzar.dp(this, 80f), Abzar.dp(this, 76f)))

        // برند
        val brand = Abzar.tv(this, EmaRoidConfig.BRAND, 26f, Abzar.RANG_TIZ)
        brand.typeface = FontBox.vazirBlack(this)
        brand.setPadding(0, Abzar.dp(this, 6f), 0, 0)
        kart.addView(brand)

        val onvan = Abzar.tv(this, "انجمن کدنویسی مدرسهٔ شهید فقیهی", 16f, Abzar.RANG_MATN, bold = true)
        onvan.gravity = Gravity.CENTER
        onvan.setPadding(0, Abzar.dp(this, 12f), 0, 0)
        kart.addView(onvan)

        val tozih = Abzar.tv(
            this, "این اپ برای یادگیری راست ساخته شده؛\nساختهٔ $EmaRoidBrand با عشق برای بچه‌های پژوهشسرا",
            14f, Abzar.RANG_KAMRANG
        )
        tozih.gravity = Gravity.CENTER
        tozih.setPadding(0, Abzar.dp(this, 10f), 0, 0)
        kart.addView(tozih)

        // نسخه
        try {
            val pm = packageManager.getPackageInfo(packageName, 0)
            val neskh = Abzar.tv(this, "v${pm.versionName}", 12f, Abzar.RANG_KAMRANG)
            neskh.setPadding(0, Abzar.dp(this, 14f), 0, 0)
            kart.addView(neskh)
        } catch (_: Exception) {
        }

        vasat.addView(kart)

        // دکمهٔ برگشت
        val dokmeBargasht = Abzar.dokme(this, Zaban.t("bargasht"), Abzar.RANG_TIZ, Abzar.RANG_TIZ, khateDor = true)
        dokmeBargasht.setOnClickListener { finish() }
        val pDokme = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pDokme.topMargin = Abzar.dp(this, 20f)
        vasat.addView(dokmeBargasht, pDokme)

        setContentView(root)
    }
}

// رشتهٔ برند برای استفادهٔ متن‌ها
const val EmaRoidBrand = "EmaRoid"
