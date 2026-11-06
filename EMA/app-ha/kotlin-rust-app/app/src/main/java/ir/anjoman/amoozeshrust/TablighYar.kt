package ir.anjoman.amoozeshrust

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView

/*
 * تبلیغ‌یار EmaRoid — سیستم درآمدزایی تبلیغاتی
 *
 * طراحی دو مرحله‌ای:
 *  ۱) الان: «تبلیغ خانه» — کارت‌های تبلیغی خودمان (کانال باله، اپ‌های دیگر
 *     EmaRoid، نشریه) می‌چرخند؛ صفر هزینه، صفر مجوز اضافه، بدون سرور.
 *  ۲) بعداً: وقتی سردبیر اکانت تپسل گرفت، فقط TABLIGH_MODE را در
 *     EmaRoidConfig روی "tapsell" بگذارد و کلیدها را پر کند — جای اتصال
 *     SDK از همین‌جا مشخص است (jaizVagoft / noghteTapsell).
 *
 * جای تبلیغ‌ها (طبق تحلیل بازار): بنر پایین صفحهٔ فهرست + کارت کامل
 * بعد از هر ۳ آزمون (کامل‌بازِ قابل بستن) — هیچ تبلیغی وسط مطالعه نیست.
 */
object TablighYar {

    // ---------- دادهٔ تبلیغ خانه ----------
    data class Tabligh(
        val onvan: String,
        val matn: String,
        val emuz: String,   // ایموجی/نشان
        val link: String    // برای کانال: ble.ir/...
    )

    fun tablighHa(): List<Tabligh> = listOf(
        Tabligh(
            "کانال انجمن کدنویسی",
            "درس‌های Rust و چیزهای باحال دیگر",
            "🦀",
            "https://ble.ir/anjomanfaghihi"
        ),
        Tabligh(
            "دستاوردهای پژوهش",
            "کتابچهٔ پژوهش‌های ۱۴۰۴ — مال بچه‌های پژوهشسرا",
            "📗",
            "https://cafebazaar.ir/dev?id=emaroid"
        ),
        Tabligh(
            "نشریهٔ آبان",
            "مجلهٔ انجمن — هر ماه با خواندنی‌های نو",
            "📰",
            "https://ble.ir/anjomanfaghihi"
        )
    )

    // ---------- بنر چرخان پایین صفحه ----------
    fun banner(c: Context, bolandiDp: Float = 64f): FrameLayout {
        val ghabe = FrameLayout(c)
        ghabe.background = Abzar.kart(c, 0xFF16213A.toInt(), 14f)
        val pad = Abzar.dp(c, 8f)
        ghabe.setPadding(pad, pad, pad, pad)

        val etiket = Abzar.tv(c, "تبلیغ", 10f, 0x66FFFFFF)
        val pEtiket = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
            Gravity.TOP or Gravity.START
        )
        ghabe.addView(etiket, pEtiket)

        val matnGhabe = LinearLayout(c)
        matnGhabe.orientation = LinearLayout.HORIZONTAL
        matnGhabe.gravity = Gravity.CENTER_VERTICAL
        val pGhabe = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,
            Gravity.CENTER
        )
        ghabe.addView(matnGhabe, pGhabe)

        val handler = Handler(Looper.getMainLooper())
        val ha = tablighHa()
        var index = 0

        fun neshanBedeh(i: Int) {
            matnGhabe.removeAllViews()
            val t = ha[i % ha.size]
            matnGhabe.isClickable = true
            Abzar.lams(matnGhabe)
            matnGhabe.setOnClickListener {
                try {
                    c.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(t.link)))
                } catch (_: Exception) {
                }
            }
            val emuz = Abzar.tv(c, t.emuz, 26f)
            emuz.setPadding(0, 0, Abzar.dp(c, 12f), 0)
            matnGhabe.addView(emuz)
            val sotoon = LinearLayout(c)
            sotoon.orientation = LinearLayout.VERTICAL
            val onvan = Abzar.tv(c, t.onvan, 14f, Abzar.RANG_MATN, bold = true)
            sotoon.addView(onvan)
            val matn = Abzar.tv(c, t.matn, 11f, Abzar.RANG_KAMRANG)
            sotoon.addView(matn)
            matnGhabe.addView(sotoon)
        }
        neshanBedeh(0)

        val charkhesh = object : Runnable {
            override fun run() {
                index++
                neshanBedeh(index)
                handler.postDelayed(this, 6000)
            }
        }
        handler.postDelayed(charkhesh, 6000)
        ghabe.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {}
            override fun onViewDetachedFromWindow(v: View) {
                handler.removeCallbacks(charkhesh)
            }
        })
        return ghabe
    }

    // ---------- کارت کامل بعد از آزمون (قابل بستن) ----------
    fun kartKamel(c: Context, bastan: () -> Unit): FrameLayout {
        val root = FrameLayout(c)
        root.setBackgroundColor(0xCC05080F.toInt())

        val kart = LinearLayout(c)
        kart.orientation = LinearLayout.VERTICAL
        kart.background = Abzar.kart(c, 0xFF16213A.toInt(), 20f)
        val pad = Abzar.dp(c, 22f)
        kart.setPadding(pad, pad, pad, pad)
        val pKart = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,
            Gravity.CENTER
        )
        pKart.marginStart = Abzar.dp(c, 24f)
        pKart.marginEnd = Abzar.dp(c, 24f)
        root.addView(kart, pKart)

        val ha = tablighHa()
        val t = ha[(System.currentTimeMillis() / 60000L).toInt() % ha.size]

        val emuz = Abzar.tv(c, t.emuz, 44f)
        emuz.gravity = Gravity.CENTER
        kart.addView(emuz)

        val onvan = Abzar.tv(c, t.onvan, 20f, Abzar.RANG_MATN, bold = true)
        onvan.gravity = Gravity.CENTER
        onvan.setPadding(0, Abzar.dp(c, 10f), 0, 0)
        kart.addView(onvan)

        val matn = Abzar.tv(c, t.matn, 14f, Abzar.RANG_KAMRANG)
        matn.gravity = Gravity.CENTER
        matn.setPadding(0, Abzar.dp(c, 8f), 0, 0)
        kart.addView(matn)

        val berav = Abzar.dokme(c, "دیدن", Abzar.RANG_TIZ, Abzar.RANG_ZAMIN)
        berav.setOnClickListener {
            try {
                c.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(t.link)))
            } catch (_: Exception) {
            }
            bastan()
        }
        val pBerav = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pBerav.topMargin = Abzar.dp(c, 18f)
        kart.addView(berav, pBerav)

        val bastanDokme = Abzar.dokme(c, "بستن", Abzar.RANG_KAMRANG, Abzar.RANG_KAMRANG, khateDor = true)
        bastanDokme.setOnClickListener { bastan() }
        val pBastan = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pBastan.topMargin = Abzar.dp(c, 10f)
        kart.addView(bastanDokme, pBastan)

        return root
    }

    // ---------- نقطهٔ اتصال تپسل (فعلاً خاموش) ----------
    /*
     * بعداً با تپسل‌پلاس:
     *   TapsellPlus.init(context, EmaRoidConfig.TAPSELL_APP_KEY)
     *   TapsellPlus.requestBannerAd(..., EmaRoidConfig.TAPSELL_BANNER_ZONE, ...)
     *   برای ویدیوی جایزه‌دار: zone همان TAPSELL_VIDEO_ZONE
     * جایگزینی این دو تابع کافی است؛ بقیهٔ اپ دست نمی‌خورد.
     */
    fun noghteTapsellBanner(c: Context, ghabe: FrameLayout) {
        // فعلاً تبلیغ خانه؛ تبلیغ تپسل همان‌جا نشانده می‌شود
        ghabe.addView(banner(c))
    }

    fun jaizVagoft(c: Context, movafagh: () -> Unit) {
        // ویدیوی جایزه‌دار فعلاً تبلیغ خانه است؛ با تپسل جایگزین می‌شود
        val ghabe = FrameLayout(c)
        setContentViewFull(c, ghabe)
        ghabe.addView(kartKamel(c) { (ghabe.parent as? ViewGroup)?.removeView(ghabe) ; movafagh() })
    }

    private fun setContentViewFull(c: Context, v: View) {
        (c as? android.app.Activity)?.addContentView(
            v,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
    }
}
