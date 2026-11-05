package ir.emaroid.hesab

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

/*
 * خانهٔ ماشین‌حساب EmaRoid — نسخهٔ خیره ✨
 * چهار کارت شیشه‌ای درخشان روی کهکشان + دکمهٔ زبان (۱۲ زبان)
 */
class HomeActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Abzar.rangVaredKon(this)

        val root = LinearLayout(this)
        root.orientation = LinearLayout.VERTICAL
        root.layoutDirection = if (Zaban.rastChin(this)) View.LAYOUT_DIRECTION_RTL else View.LAYOUT_DIRECTION_LTR
        root.background = NagshZamin(Abzar.RANG_SHURU, Abzar.RANG_PAYAN)

        val scroll = ScrollView(this)
        scroll.isFillViewport = true
        root.addView(scroll, LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f))

        val g = LinearLayout(this)
        g.orientation = LinearLayout.VERTICAL
        g.setPadding(Abzar.dp(this, 20f), Abzar.dp(this, 26f), Abzar.dp(this, 20f), Abzar.dp(this, 20f))
        scroll.addView(g, ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))

        // ردیف عنوان: متن + دکمهٔ زبان
        val sar = LinearLayout(this)
        sar.orientation = LinearLayout.HORIZONTAL
        sar.gravity = Gravity.CENTER_VERTICAL
        val sotoonOnvan = LinearLayout(this)
        sotoonOnvan.orientation = LinearLayout.VERTICAL
        val onvan = Abzar.tv(this, Zaban.t("onvan_app"), 28f, Abzar.RANG_MATN)
        onvan.typeface = FontBox.vazirBlack(this)
        sotoonOnvan.addView(onvan)
        val zir = Abzar.tv(this, Zaban.t("zir_onvan"), 13f, Abzar.RANG_KAMRANG)
        zir.setPadding(0, Abzar.dp(this, 6f), 0, 0)
        sotoonOnvan.addView(zir)
        val pSotoon = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
        sar.addView(sotoonOnvan, pSotoon)

        val dokmeZaban = TextView(this)
        dokmeZaban.text = "🌐"
        dokmeZaban.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 22f)
        dokmeZaban.gravity = Gravity.CENTER
        dokmeZaban.background = Abzar.shisheh(this, 0x40231B4E, 0x6622D3EE, 20f, 1.3f)
        dokmeZaban.setPadding(Abzar.dp(this, 10f), Abzar.dp(this, 8f), Abzar.dp(this, 10f), Abzar.dp(this, 8f))
        dokmeZaban.isClickable = true
        Abzar.fasor(dokmeZaban)
        dokmeZaban.setOnClickListener { gozinehZaban() }
        sar.addView(dokmeZaban)
        g.addView(sar)
        g.addView(View(this), LinearLayout.LayoutParams(1, Abzar.dp(this, 18f)))

        // چهار بخش — هر کارت با هالهٔ رنگ خودش
        val bakhsha = listOf(
            Quad("🧮", Zaban.t("b1t"), Zaban.t("b1d"), Abzar.RANG_GOLABI, HesabActivity::class.java),
            Quad("📈", Zaban.t("b2t"), Zaban.t("b2d"), Abzar.RANG_TIZ, RasmActivity::class.java),
            Quad("🪜", Zaban.t("b3t"), Zaban.t("b3d"), Abzar.RANG_SABZ, HalActivity::class.java),
            Quad("🔢", Zaban.t("b4t"), Zaban.t("b4d"), Abzar.RANG_BANAFSH, AdadActivity::class.java)
        )
        for (b in bakhsha) {
            val kart = LinearLayout(this)
            kart.orientation = LinearLayout.HORIZONTAL
            kart.gravity = Gravity.CENTER_VERTICAL
            kart.background = Abzar.shisheh(this, 0x52231B4E, haloeRang(b.halo), 22f, 1.5f)
            kart.setPadding(Abzar.dp(this, 14f), Abzar.dp(this, 18f), Abzar.dp(this, 16f), Abzar.dp(this, 18f))
            kart.isClickable = true
            Abzar.lams(kart)
            kart.setOnClickListener { startActivity(Intent(this, b.niat)) }

            // نشانِ دایره‌ای درخشان برای ایموجی
            val ghabe = FrameLayout(this)
            val halo = View(this)
            halo.background = Abzar.dayereh(this, (b.halo and 0x00FFFFFF) or 0x2E000000)
            ghabe.addView(halo, FrameLayout.LayoutParams(
                Abzar.dp(this, 50f), Abzar.dp(this, 50f), Gravity.CENTER))
            val e = Abzar.tv(this, b.emuz, 26f)
            e.gravity = Gravity.CENTER
            ghabe.addView(e, FrameLayout.LayoutParams(
                Abzar.dp(this, 50f), Abzar.dp(this, 50f), Gravity.CENTER))
            kart.addView(ghabe)

            val sotoon = LinearLayout(this)
            sotoon.orientation = LinearLayout.VERTICAL
            val o = Abzar.tv(this, b.onv, 17f, Abzar.RANG_MATN, bold = true)
            sotoon.addView(o)
            val m = Abzar.tv(this, b.matn, 12f, Abzar.RANG_KAMRANG)
            m.setPadding(0, Abzar.dp(this, 3f), 0, 0)
            sotoon.addView(m)
            val p = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
            p.marginStart = Abzar.dp(this, 14f)
            kart.addView(sotoon)
            kart.addView(Abzar.tv(this, "‹", 22f, b.halo))

            val pK = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            pK.topMargin = Abzar.dp(this, 12f)
            g.addView(kart, pK)
        }

        // درباره
        val dokme = Abzar.dokme(this, Zaban.t("darbare"), Abzar.RANG_TIZ, Abzar.RANG_TIZ, khateDor = true)
        dokme.setOnClickListener { startActivity(Intent(this, AboutActivity::class.java)) }
        val pD = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        pD.topMargin = Abzar.dp(this, 24f)
        g.addView(dokme, pD)

        setContentView(root)
    }

    // هالهٔ دور کارت‌ها را ملایم نگه می‌دارد (آلفای پایین)
    private fun haloeRang(rang: Int): Int = (rang and 0x00FFFFFF) or 0x59000000


    // گفت‌وگوی انتخاب زبان — ۱۲ زبان
    private fun gozinehZaban() {
        val zabanHa = Zaban.hameZabanHa()
        val namHa = zabanHa.map { Zaban.namZaban(it) }.toTypedArray()
        AlertDialog.Builder(this)
            .setTitle(Zaban.t("entekhab_zaban"))
            .setItems(namHa) { _, i ->
                Zaban.zakhireKon(this, zabanHa[i])
                recreate() // همهٔ نوشته‌ها با زبان تازه
            }
            .setNegativeButton(Zaban.t("bazgasht"), null)
            .show()
    }

    // چهارگوشهٔ ساده چون Triple سه‌تایی کم داشت
    private data class Quad(
        val emuz: String, val onv: String, val matn: String,
        val halo: Int, val niat: Class<out Activity>
    )
}
