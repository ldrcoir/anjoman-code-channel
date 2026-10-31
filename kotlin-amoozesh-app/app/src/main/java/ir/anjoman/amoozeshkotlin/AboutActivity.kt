package ir.anjoman.amoozeshkotlin

import android.app.Activity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout

/*
 * صفحهٔ درباره: نام پژوهش‌سرا، انجمن و استاد راهنما
 */
class AboutActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Abzar.rangVaredKon(this)

        val root = FrameLayout(this)
        root.layoutDirection = View.LAYOUT_DIRECTION_RTL
        root.setBackgroundColor(Abzar.RANG_ZAMIN)

        val vasat = LinearLayout(this)
        vasat.orientation = LinearLayout.VERTICAL
        vasat.gravity = Gravity.CENTER
        vasat.setPadding(
            Abzar.dp(this, 28f), Abzar.dp(this, 24f),
            Abzar.dp(this, 28f), Abzar.dp(this, 24f)
        )
        root.addView(
            vasat,
            FrameLayout.LayoutParams(
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

        // لوزی کوچک طلایی بالای کارت
        val ghabeLozi = FrameLayout(this)
        val lozi = View(this)
        lozi.background = Abzar.kart(this, Abzar.RANG_TALAEI, 7f)
        lozi.rotation = 45f
        ghabeLozi.addView(
            lozi,
            FrameLayout.LayoutParams(Abzar.dp(this, 30f), Abzar.dp(this, 30f), Gravity.CENTER)
        )
        val pGhabe = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            Abzar.dp(this, 48f)
        )
        pGhabe.gravity = Gravity.CENTER_HORIZONTAL
        kart.addView(ghabeLozi, pGhabe)

        // خط اول: پژوهش‌سرا
        val pajoheshSara = Abzar.tv(this, "پژوهش‌سرای امام هادی (علیه‌السلام)", 15f, Abzar.RANG_TALAEI, bold = true)
        pajoheshSara.gravity = Gravity.CENTER
        kart.addView(pajoheshSara)

        // خط دوم: انجمن
        val onvan = Abzar.tv(this, "انجمن کدنویسی مدرسهٔ شهید فقیهی", 19f, Abzar.RANG_MATN, bold = true)
        onvan.gravity = Gravity.CENTER
        onvan.setPadding(0, Abzar.dp(this, 8f), 0, 0)
        kart.addView(onvan)

        val tozih = Abzar.tv(this, "این اپ برای یادگیری کاتلین ساخته شده", 14f, Abzar.RANG_KAMRANG)
        tozih.gravity = Gravity.CENTER
        tozih.setPadding(0, Abzar.dp(this, 10f), 0, 0)
        kart.addView(tozih)

        // خط آخر: استاد راهنما
        val ostad = Abzar.tv(this, "استاد راهنما: مهندس احسان مراد", 14f, Abzar.RANG_MATN, bold = true)
        ostad.gravity = Gravity.CENTER
        val pOstad = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pOstad.topMargin = Abzar.dp(this, 18f)
        kart.addView(ostad, pOstad)

        vasat.addView(kart)

        // دکمهٔ برگشت
        val dokmeBargasht = Abzar.dokme(this, "بازگشت", Abzar.RANG_TIZ, Abzar.RANG_TIZ, khateDor = true)
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
