package ir.anjoman.amoozeshkotlin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView

/*
 * صفحهٔ شروع: یه لوزی طلایی با حرف «ک» و اسم اپ
 * یه‌وقتی می‌مونه و بعدش می‌ره صفحهٔ اصلی
 */
class SplashActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Abzar.rangVaredKon(this)

        val root = FrameLayout(this)
        root.layoutDirection = View.LAYOUT_DIRECTION_RTL
        root.setBackgroundColor(Abzar.RANG_ZAMIN)

        // ستون وسط صفحه
        val vasat = LinearLayout(this)
        vasat.orientation = LinearLayout.VERTICAL
        vasat.gravity = Gravity.CENTER_HORIZONTAL
        root.addView(
            vasat,
            FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER
            )
        )

        // لوزی طلایی که حرف «ک» روش نشسته
        val ghabeLozi = FrameLayout(this)
        val lozi = View(this)
        lozi.background = Abzar.kart(this, Abzar.RANG_TALAEI, 12f)
        lozi.rotation = 45f
        ghabeLozi.addView(
            lozi,
            FrameLayout.LayoutParams(Abzar.dp(this, 78f), Abzar.dp(this, 78f), Gravity.CENTER)
        )

        val harf = TextView(this)
        harf.text = "ک"
        harf.setTextColor(Abzar.RANG_ZAMIN)
        harf.typeface = FontBox.vazirBlack(this)
        harf.setTextSize(TypedValue.COMPLEX_UNIT_SP, 38f)
        harf.gravity = Gravity.CENTER
        ghabeLozi.addView(
            harf,
            FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                Gravity.CENTER
            )
        )

        vasat.addView(ghabeLozi, LinearLayout.LayoutParams(Abzar.dp(this, 120f), Abzar.dp(this, 120f)))

        // اسم اپ
        val onvan = Abzar.tv(this, "آموزش کاتلین", 30f, Abzar.RANG_MATN)
        onvan.typeface = FontBox.vazirBlack(this)
        val pOnvan = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pOnvan.topMargin = Abzar.dp(this, 28f)
        vasat.addView(onvan, pOnvan)

        // یه جملهٔ کوچیک زیر اسم
        val zirNevis = Abzar.tv(this, "قدم‌به‌قدم کاتلین یاد بگیر", 14f, Abzar.RANG_KAMRANG)
        val pZir = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pZir.topMargin = Abzar.dp(this, 8f)
        vasat.addView(zirNevis, pZir)

        setContentView(root)

        // بعد از ۱.۲ ثانیه بریم صفحهٔ اصلی
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }, 1200)
    }
}
