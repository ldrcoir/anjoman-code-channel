package ir.anjoman.sandogh

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView

/*
 * جلد کتابچه — صفحهٔ نخست با قاب طلایی و دکمهٔ گشودن
 */
class SplashActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TemEnekhab.rangVaredKon(this)
        val tem = TemEnekhab.bekhon(this)

        val root = LinearLayout(this)
        root.layoutDirection = View.LAYOUT_DIRECTION_RTL
        root.orientation = LinearLayout.VERTICAL
        root.gravity = Gravity.CENTER
        root.setBackgroundColor(tem.zamin)

        // قاب جلد
        val ghabe = LinearLayout(this)
        ghabe.orientation = LinearLayout.VERTICAL
        ghabe.gravity = Gravity.CENTER
        ghabe.background = Abzar.kartGradient(this, tem.kart, tem.kartTir, 26f)
        ghabe.setPadding(Abzar.dp(this, 28f), Abzar.dp(this, 34f), Abzar.dp(this, 28f), Abzar.dp(this, 30f))
        ghabe.foreground = Abzar.kartKhatei(this, tem.asli, 0, 26f)

        val neshane = Abzar.tv(this, "🏦", 46f, tem.asli)
        neshane.gravity = Gravity.CENTER
        ghabe.addView(neshane)

        val onvan = Abzar.tv(this, "وضعیت صندوق", 34f, tem.matn)
        onvan.typeface = FontBox.vazirBlack(this)
        onvan.gravity = Gravity.CENTER
        ghabe.addView(onvan, LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ))

        val onvan2 = Abzar.tv(this, "پس‌انداز", 34f, tem.asli)
        onvan2.typeface = FontBox.vazirBlack(this)
        onvan2.gravity = Gravity.CENTER
        ghabe.addView(onvan2)

        // نوار طلایی تزئینی
        val nobar = View(this)
        nobar.background = Abzar.kartGradient(this, tem.asli, tem.dovom, 3f)
        nobar.layoutParams = LinearLayout.LayoutParams(Abzar.dp(this, 130f), Abzar.dp(this, 5f))
        val pNobar = LinearLayout.LayoutParams(Abzar.dp(this, 130f), Abzar.dp(this, 5f))
        pNobar.topMargin = Abzar.dp(this, 14f)
        ghabe.addView(nobar, pNobar)

        val dane = Ketabche.bekhon(this)
        val tarikh = if (dane.tarikh.isNotBlank()) dane.tarikh else "مهر ۱۴۰۵"
        val zirOnvan = Abzar.tv(this, "کتابچهٔ خانوادگی — $tarikh", 15f, tem.kamrang)
        zirOnvan.gravity = Gravity.CENTER
        val pZir = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pZir.topMargin = Abzar.dp(this, 10f)
        ghabe.addView(zirOnvan, pZir)

        val amar = if (dane.shakhs.isNotEmpty())
            "${Abzar.adadFarsi(dane.shakhs.size)} عضو • ${Abzar.adadJoda(dane.majmoo?.pasandaz ?: 0)} تومان پس‌انداز"
        else "نسخهٔ بدون داده"
        val matnAmar = Abzar.tv(this, amar, 14f, tem.asli, bold = true)
        matnAmar.gravity = Gravity.CENTER
        matnAmar.setPadding(0, Abzar.dp(this, 4f), 0, 0)
        ghabe.addView(matnAmar)

        val dokme = Abzar.dokme(this, "📖  گشودن کتابچه", tem.asli, if (tem.khater) 0xFF0B1220.toInt() else 0xFFFFFFFF.toInt())
        dokme.typeface = FontBox.vazirBold(this)
        val pDokme = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pDokme.topMargin = Abzar.dp(this, 26f)
        ghabe.addView(dokme, pDokme)
        dokme.setOnClickListener {
            startActivity(Intent(this, FehrestActivity::class.java))
        }

        val pGhabe = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pGhabe.setMargins(Abzar.dp(this, 26f), 0, Abzar.dp(this, 26f), 0)
        root.addView(ghabe, pGhabe)

        // انیمیشن ورود جلد
        ghabe.alpha = 0f
        ghabe.translationY = Abzar.dp(this, 36f).toFloat()
        ghabe.animate().alpha(1f).translationY(0f).setDuration(420).start()

        setContentView(root)
    }
}
