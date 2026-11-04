package ir.emaroid.hesab

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

/*
 * ابزار اعداد — اول، عوامل، مقسوم‌علیح، ب.م.م/ک.م.م، کولاتز، مبنا، عدد کامل
 */
class AdadActivity : Activity() {

    private lateinit var voroodi: EditText
    private lateinit var voroodi2: EditText
    private lateinit var ghabeNatije: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Abzar.rangVaredKon(this)

        val root = LinearLayout(this)
        root.orientation = LinearLayout.VERTICAL
        root.layoutDirection = View.LAYOUT_DIRECTION_RTL
        root.background = NagshZamin(0xFF101A33.toInt(), 0xFF0B1220.toInt())
        root.setPadding(Abzar.dp(this, 14f), Abzar.dp(this, 10f), Abzar.dp(this, 14f), Abzar.dp(this, 14f))

        val sar = LinearLayout(this)
        val bargasht = Abzar.dokme(this, "‹ خانه", Abzar.RANG_TIZ, Abzar.RANG_TIZ, khateDor = true)
        bargasht.setOnClickListener { finish() }
        sar.addView(bargasht)
        root.addView(sar)

        // دو ورودی (دومی برای ب.م.م/ک.م.م)
        voroodi = EditText(this)
        voroodi.hint = "عدد — مثلاً ۹۶"
        voroodi.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 18f)
        voroodi.setTextColor(Abzar.RANG_MATN)
        voroodi.setHintTextColor(Abzar.RANG_KAMRANG)
        voroodi.background = Abzar.kart(this, Abzar.RANG_CODE, 12f)
        voroodi.setPadding(Abzar.dp(this, 14f), Abzar.dp(this, 12f), Abzar.dp(this, 14f), Abzar.dp(this, 12f))
        voroodi.textDirection = View.TEXT_DIRECTION_LTR
        voroodi.inputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_FLAG_SIGNED
        voroodi.setSingleLine(true)
        val pV = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        pV.topMargin = Abzar.dp(this, 12f)
        root.addView(voroodi, pV)

        voroodi2 = EditText(this)
        voroodi2.hint = "عدد دوم (فقط برای ب.م.م و ک.م.م) — مثلاً ۸۴"
        voroodi2.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 14f)
        voroodi2.setTextColor(Abzar.RANG_MATN)
        voroodi2.setHintTextColor(Abzar.RANG_KAMRANG)
        voroodi2.background = Abzar.kart(this, Abzar.RANG_CODE, 12f)
        voroodi2.setPadding(Abzar.dp(this, 14f), Abzar.dp(this, 10f), Abzar.dp(this, 14f), Abzar.dp(this, 10f))
        voroodi2.textDirection = View.TEXT_DIRECTION_LTR
        voroodi2.inputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_FLAG_SIGNED
        voroodi2.setSingleLine(true)
        val pV2 = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        pV2.topMargin = Abzar.dp(this, 8f)
        root.addView(voroodi2, pV2)

        // دکمه‌های ابزار
        val dokmeHa = listOf(
            "اول؟" to { n: Long -> aval(n) },
            "عوامل اول" to { n: Long -> avamel(n) },
            "مقسوم‌علیح‌ها" to { n: Long -> maghsum(n) },
            "کولاتز" to { n: Long -> collatz(n) },
            "مبنا ۲/۸/۱۶" to { n: Long -> mabna(n) },
            "عدد کامل؟" to { n: Long -> kamel(n) },
            "ب.م.م / ک.م.م" to { n: Long -> bmm(n) }
        )

        var radif: LinearLayout? = null
        for ((index, d) in dokmeHa.withIndex()) {
            if (index % 2 == 0) {
                radif = LinearLayout(this)
                root.addView(radif, LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                    topMargin = Abzar.dp(this@AdadActivity, 8f)
                })
            }
            val dokme = Abzar.dokme(this, d.first, Abzar.RANG_TIZ, Abzar.RANG_ZAMIN)
            dokme.setOnClickListener {
                try {
                    val n = voroodi.text.toString().trim().replace("٫", ".").toLong()
                    ghabeNatije(d.second(n))
                } catch (e: NumberFormatException) {
                    ghabeNatije(listOf("عدد را درست بنویس!"))
                } catch (e: Exception) {
                    ghabeNatije(listOf("⚠️ ${e.message}"))
                }
            }
            radif!!.addView(dokme, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f).apply {
                setMargins(Abzar.dp(this@AdadActivity, 3f), 0, Abzar.dp(this@AdadActivity, 3f), 0)
            })
        }

        // نتیجه‌ها
        val scroll = ScrollView(this)
        scroll.isFillViewport = true
        val pS = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f)
        pS.topMargin = Abzar.dp(this, 12f)
        root.addView(scroll, pS)

        ghabeNatije = LinearLayout(this)
        ghabeNatije.orientation = LinearLayout.VERTICAL
        scroll.addView(ghabeNatije, ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))

        setContentView(root)
        voroodi.setText("96")
    }

    // ---------- کارت نتیجه ----------
    private fun ghabeNatije(khoroji: List<String>, sarOnvan: String = "") {
        ghabeNatije.removeAllViews()
        val kart = LinearLayout(this)
        kart.orientation = LinearLayout.VERTICAL
        kart.background = Abzar.kart(this)
        kart.setPadding(Abzar.dp(this, 16f), Abzar.dp(this, 14f), Abzar.dp(this, 16f), Abzar.dp(this, 14f))
        if (sarOnvan.isNotBlank()) {
            val t = Abzar.tv(this, sarOnvan, 16f, Abzar.RANG_TALAEI, bold = true)
            kart.addView(t)
        }
        for (s in khoroji) {
            val t = Abzar.tv(this, s, 15f, Abzar.RANG_MATN)
            t.setLineSpacing(Abzar.dp(this, 4f).toFloat(), 1f)
            t.setPadding(0, Abzar.dp(this, 8f), 0, 0)
            kart.addView(t)
        }
        ghabeNatije.addView(kart, LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
    }

    private fun nBekhon(): Long = voroodi.text.toString().trim().replace("٫", ".").toLong()

    private fun aval(n: Long): List<String> {
        val h = AdadYar.avalAst(n)
        return listOf(
            if (h) "$n عدد اول است ✓"
            else "$n اول نیست ✗",
            if (h) "مقسوم‌علیح‌هایش فقط ۱ و خودش است."
            else "کوچک‌ترین عامل: ${AdadYar.tajzieAval(Math.abs(n)).firstOrNull()?.first ?: "-"}"
        )
    }

    private fun avamel(n: Long): List<String> {
        val t = AdadYar.tajzieAval(Math.abs(n))
        if (t.isEmpty()) return listOf("برای $n تجزیه نداریم (۰ یا ۱)")
        val matn = t.joinToString(" × ") { (p, c) -> if (c == 1) "$p" else "$p^$c" }
        return listOf("تجزیهٔ $n:", matn)
    }

    private fun maghsum(n: Long): List<String> {
        val m = AdadYar.maghsumHa(Math.abs(n))
        return listOf("مقسوم‌علیح‌های $n (${m.size} تا):", m.joinToString(" ، "))
    }

    private fun collatz(n: Long): List<String> {
        val d = AdadYar.collatz(n)
        val kh = mutableListOf("دنبالهٔ کولاتز از $n — ${d.size} گام تا رسیدن به ۱:")
        kh.add(d.take(24).joinToString(" → ") + if (d.size > 24) " → …" else "")
        kh.add("بزرگ‌ترین عدد مسیر: ${d.max()}")
        return kh
    }

    private fun mabna(n: Long): List<String> = listOf(
        "دوگانی (۲): ${AdadYar.tabdilMabna(n, 2)}",
        "هشتگانی (۸): ${AdadYar.tabdilMabna(n, 8)}",
        "شانزده‌گانی (۱۶): ${AdadYar.tabdilMabna(n, 16)}"
    )

    private fun kamel(n: Long): List<String> {
        val h = AdadYar.kamelAst(n)
        return listOf(if (h) "$n عدد کامل است ✓ (مجموع مقسوم‌علیح‌هایش خودش می‌شود)"
        else "$n عدد کامل نیست ✗")
    }

    private fun bmm(n: Long): List<String> {
        val n2 = voroodi2.text.toString().trim().replace("٫", ".").toLongOrNull()
            ?: return listOf("عدد دوم را هم بنویس تا ب.م.م و ک.م.م حساب کنیم")
        return listOf(
            "ب.م.م($n ، $n2) = ${AdadYar.bmm(n, n2)}",
            "ک.م.م($n ، $n2) = ${AdadYar.kmm(n, n2)}"
        )
    }
}
