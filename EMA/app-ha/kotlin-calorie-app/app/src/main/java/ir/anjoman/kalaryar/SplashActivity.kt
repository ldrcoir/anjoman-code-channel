package ir.anjoman.kalaryar

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView

/*
 * صفحهٔ شروع: لوزی سبز با قلب و اسم اپ
 */
class SplashActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Abzar.rangVaredKon(this)

        val root = FrameLayout(this)
        root.layoutDirection = android.view.View.LAYOUT_DIRECTION_RTL
        root.setBackgroundColor(Abzar.RANG_ZAMIN)

        val vasat = LinearLayout(this)
        vasat.orientation = LinearLayout.VERTICAL
        vasat.gravity = Gravity.CENTER
        val pVasat = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        pVasat.gravity = Gravity.CENTER
        root.addView(vasat, pVasat)

        // لوگو: دایرهٔ گرادیانی با قلب
        val ghabeLozi = FrameLayout(this)
        val lozi = TextView(this)
        lozi.text = "🥗"
        lozi.textSize = 34f
        lozi.gravity = Gravity.CENTER
        lozi.background = Abzar.kartGradient(this, Abzar.RANG_SABZ, Abzar.RANG_SABZKAM, 30f)
        lozi.layoutParams = FrameLayout.LayoutParams(
            Abzar.dp(this, 84f), Abzar.dp(this, 84f), Gravity.CENTER
        )
        ghabeLozi.addView(lozi)
        vasat.addView(ghabeLozi, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
        ))

        val onvan = Abzar.tv(this, "کالری‌یار", 34f, Abzar.RANG_MATN)
        onvan.typeface = FontBox.vazirBlack(this)
        onvan.setPadding(0, Abzar.dp(this, 28f), 0, 0)
        vasat.addView(onvan)

        val zirNevis = Abzar.tv(this, "کالری شمار، آب‌شمار و دستیار هوشمند رژیم", 14f, Abzar.RANG_KAMRANG)
        zirNevis.setPadding(0, Abzar.dp(this, 10f), 0, 0)
        vasat.addView(zirNevis)

        setContentView(root)

        Handler(Looper.getMainLooper()).postDelayed({
            val hadaf = if (Khazide.profilHast(this)) HomeActivity::class.java else OnboardingActivity::class.java
            startActivity(Intent(this, hadaf))
            finish()
        }, 1300)
    }
}
