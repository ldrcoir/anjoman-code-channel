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
 * فهرست تمرین‌های زمین بازی: هر کارت یک تمرین است
 * با کلیک، زمین بازی در حالت تمرین باز می‌شود
 * هر بار برگردیم صفحه از نو ساخته می‌شود تا تیک‌های تازه دیده شوند
 */
class TamrinActivity : Activity() {

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

        val onvan = Abzar.tv(this, "تمرین‌های عملی", 26f, Abzar.RANG_MATN)
        onvan.typeface = FontBox.vazirBlack(this)
        ghabe.addView(onvan)

        val anjamShode = Zakhire.tamrinAnjamShode(this)
        val zirOnvan = Abzar.tv(
            this,
            "کد را خودت بنویس تا خروجی درست بدهد — ${Abzar.adadFarsi(anjamShode)} از ${Abzar.adadFarsi(DarsHa.tamrinHa.size)} حل شده",
            14f, Abzar.RANG_KAMRANG
        )
        zirOnvan.setPadding(0, Abzar.dp(this, 6f), 0, 0)
        ghabe.addView(zirOnvan)

        // یک کارت برای هر تمرین
        for ((i, tamrin) in DarsHa.tamrinHa.withIndex()) {
            val pKart = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            pKart.topMargin = Abzar.dp(this, 12f)
            ghabe.addView(kartTamrin(i, tamrin), pKart)
        }

        // یادآوری کوچک ته صفحه
        val nasihat = Abzar.tv(
            this,
            "نکته: تمرین‌ها به درس‌های مربوطشان وصل‌اند؛ اگر گیر کردی اول درس را یک بار دیگر بخوان.",
            13f, Abzar.RANG_KAMRANG
        )
        val pNasihat = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pNasihat.topMargin = Abzar.dp(this, 20f)
        ghabe.addView(nasihat, pNasihat)

        return root
    }

    // کارت هر تمرین: تیک یا شماره + عنوان + وضعیت
    private fun kartTamrin(i: Int, tamrin: Tamrin): LinearLayout {
        val kart = LinearLayout(this)
        kart.orientation = LinearLayout.HORIZONTAL
        kart.gravity = Gravity.CENTER_VERTICAL
        kart.background = Abzar.kart(this)
        kart.setPadding(
            Abzar.dp(this, 14f), Abzar.dp(this, 14f),
            Abzar.dp(this, 14f), Abzar.dp(this, 14f)
        )
        kart.isClickable = true
        Abzar.lams(kart)
        kart.setOnClickListener {
            val niat = Intent(this, PlaygroundActivity::class.java)
            niat.putExtra("tamrin", i)
            startActivity(niat)
        }

        val anjam = Zakhire.tamrinAnjamAst(this, i)
        val dayereh = TextView(this)
        dayereh.text = if (anjam) "✓" else Abzar.adadFarsi(i + 1)
        dayereh.gravity = Gravity.CENTER
        dayereh.typeface = FontBox.vazirBold(this)
        dayereh.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        dayereh.setTextColor(if (anjam) Abzar.RANG_ZAMIN else Abzar.RANG_MATN)
        dayereh.background = Abzar.dayereh(this, if (anjam) Abzar.RANG_SABZ else Abzar.RANG_KHAKISTARI)
        kart.addView(dayereh, LinearLayout.LayoutParams(Abzar.dp(this, 40f), Abzar.dp(this, 40f)))

        // عنوان و وضعیت
        val sotoon = LinearLayout(this)
        sotoon.orientation = LinearLayout.VERTICAL

        val matnOnvan = Abzar.tv(this, tamrin.onvan, 16f, Abzar.RANG_MATN, bold = true)
        sotoon.addView(matnOnvan)

        val halat = Abzar.tv(
            this,
            if (anjam) "درست حل شد" else "هنوز حل نشده",
            13f,
            if (anjam) Abzar.RANG_SABZ else Abzar.RANG_KAMRANG
        )
        halat.setPadding(0, Abzar.dp(this, 3f), 0, 0)
        sotoon.addView(halat)

        val pSotoon = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
        pSotoon.marginStart = Abzar.dp(this, 14f)
        kart.addView(sotoon, pSotoon)

        val flash = Abzar.tv(this, "‹", 22f, Abzar.RANG_KAMRANG)
        kart.addView(flash)

        return kart
    }
}
