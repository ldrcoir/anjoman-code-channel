package ir.anjoman.kalaryar

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

/*
 * ورود اطلاعات اولیه: جنسیت، سن، قد، وزن، فعالیت، هدف
 * از همین‌ها کالری هدف و درشت‌مغذی‌ها با فرمول میفلین-سنت‌جیور حساب می‌شود
 */
class OnboardingActivity : Activity() {

    private var jens = "mard"
    private var faaliat = 2
    private var nooHadaf = "kam"
    private lateinit var edtSen: EditText
    private lateinit var edtGhad: EditText
    private lateinit var edtVazn: EditText
    private lateinit var edtVaznHadaf: EditText
    private lateinit var dokmeJensMard: TextView
    private lateinit var dokmeJensZan: TextView
    private lateinit var dokmeHadafHa: HashMap<String, TextView>
    private lateinit var dokmeFaaliatHa: ArrayList<TextView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Abzar.rangVaredKon(this)

        val root = ScrollView(this)
        root.layoutDirection = View.LAYOUT_DIRECTION_RTL
        root.setBackgroundColor(Abzar.RANG_ZAMIN)
        root.isFillViewport = true

        val ghabe = LinearLayout(this)
        ghabe.orientation = LinearLayout.VERTICAL
        ghabe.setPadding(Abzar.dp(this, 20f), Abzar.dp(this, 28f), Abzar.dp(this, 20f), Abzar.dp(this, 28f))
        root.addView(ghabe, ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ))

        // سرصفحه
        val onvan = Abzar.tv(this, "به کالری‌یار خوش آمدی 🌿", 26f, Abzar.RANG_MATN)
        onvan.typeface = FontBox.vazirBlack(this)
        ghabe.addView(onvan)

        val tozih = Abzar.tv(
            this,
            "چند مشخصه از تو بگیرم تا کالری هدف، درشت‌مغذی‌ها و هدف آب‌ت را دقیق حساب کنم. همه‌چیز فقط روی خود گوشی ذخیره می‌شود.",
            14f, Abzar.RANG_KAMRANG
        )
        tozih.setPadding(0, Abzar.dp(this, 10f), 0, 0)
        ghabe.addView(tozih)

        // جنسیت
        ghabe.addView(sarBakhsh("جنسیت"))
        val safJens = LinearLayout(this)
        safJens.orientation = LinearLayout.HORIZONTAL
        dokmeJensMard = entekhab("پسر", jens == "mard")
        dokmeJensMard.setOnClickListener {
            jens = "mard"
            rangBeJens()
        }
        dokmeJensZan = entekhab("دختر", jens == "zan")
        dokmeJensZan.setOnClickListener {
            jens = "zan"
            rangBeJens()
        }
        dokmeJensMard.layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f).also {
            it.marginStart = Abzar.dp(this, 6f)
        }
        dokmeJensZan.layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f).also {
            it.marginStart = Abzar.dp(this, 6f)
        }
        safJens.addView(dokmeJensZan)
        safJens.addView(dokmeJensMard)
        val pJens = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pJens.topMargin = Abzar.dp(this, 10f)
        ghabe.addView(safJens, pJens)

        // ورودی‌های عددی
        ghabe.addView(sarBakhsh("سن (سال)"))
        edtSen = voroodiAdad("مثلاً ۱۵")
        ghabe.addView(edtSen)
        ghabe.addView(sarBakhsh("قد (سانتی‌متر)"))
        edtGhad = voroodiAdad("مثلاً ۱۷۰")
        ghabe.addView(edtGhad)
        ghabe.addView(sarBakhsh("وزن فعلی (کیلوگرم)"))
        edtVazn = voroodiAdad("مثلاً ۶۰")
        ghabe.addView(edtVazn)
        ghabe.addView(sarBakhsh("وزن هدف (کیلوگرم)"))
        edtVaznHadaf = voroodiAdad("مثلاً ۵۵")
        ghabe.addView(edtVaznHadaf)

        // سطح فعالیت
        ghabe.addView(sarBakhsh("سطح فعالیت روزانه"))
        dokmeFaaliatHa = ArrayList()
        for (i in 0 until 4) {
            val d = entekhab(HesabHa.asamiFaaliat[i], faaliat == i + 1)
            d.setOnClickListener {
                faaliat = i + 1
                rangBeFaaliat()
            }
            val pD = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
            pD.topMargin = Abzar.dp(this, 8f)
            ghabe.addView(d, pD)
            dokmeFaaliatHa.add(d)
        }

        // هدف
        ghabe.addView(sarBakhsh("هدف تو چیست؟"))
        dokmeHadafHa = HashMap()
        val hadafHa = listOf(
            "kam" to "🔻 کاهش وزن",
            "sabet" to "⚖️ تثبیت وزن",
            "ziaad" to "🔺 افزایش وزن"
        )
        for ((kod, matn) in hadafHa) {
            val d = entekhab(matn, nooHadaf == kod)
            d.setOnClickListener {
                nooHadaf = kod
                rangBeHadaf()
            }
            val pD = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
            pD.topMargin = Abzar.dp(this, 8f)
            ghabe.addView(d, pD)
            dokmeHadafHa[kod] = d
        }

        // دکمهٔ شروع
        val dokmeShoro = Abzar.dokme(
            this, "شروع کن! 🚀", Abzar.RANG_SABZ, Abzar.RANG_ZAMIN
        )
        dokmeShoro.setOnClickListener { zakhireVaBoro() }
        val pShoro = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pShoro.topMargin = Abzar.dp(this, 28f)
        ghabe.addView(dokmeShoro, pShoro)

        // نکتهٔ علمی
        val elemdan = Abzar.tv(
            this,
            "فرمول محاسبه: میفلین-سنت‌جیور (استاندارد علمی ۱۹۹۰ تا امروز) + کسری ۵۰۰ کالری برای کاهش وزن سالم (حدود نیم کیلو در هفته).",
            12f, Abzar.RANG_KAMRANG
        )
        elemdan.setPadding(0, Abzar.dp(this, 20f), 0, 0)
        ghabe.addView(elemdan)

        setContentView(root)
    }

    private fun rangBeJens() {
        rangDokme(dokmeJensMard, jens == "mard")
        rangDokme(dokmeJensZan, jens == "zan")
    }

    private fun rangBeFaaliat() {
        for (i in 0 until dokmeFaaliatHa.size) {
            rangDokme(dokmeFaaliatHa[i], faaliat == i + 1)
        }
    }

    private fun rangBeHadaf() {
        for ((kod, d) in dokmeHadafHa) {
            rangDokme(d, nooHadaf == kod)
        }
    }

    private fun zakhireVaBoro() {
        val sen = edtSen.text.toString().toIntOrNull()
        val ghad = edtGhad.text.toString().toIntOrNull()
        val vazn = edtVazn.text.toString().toDoubleOrNull()
        val vaznHadaf = edtVaznHadaf.text.toString().toDoubleOrNull()

        if (sen == null || sen < 8 || sen > 90) {
            edtSen.error = "سن را درست بنویس"
            return
        }
        if (ghad == null || ghad < 100 || ghad > 230) {
            edtGhad.error = "قد را درست بنویس"
            return
        }
        if (vazn == null || vazn < 25 || vazn > 250) {
            edtVazn.error = "وزن را درست بنویس"
            return
        }
        val hadaf = vaznHadaf ?: vazn

        val profil = Profil(jens, sen, ghad, vazn, faaliat, nooHadaf, hadaf)
        Khazide.profilZakhire(this, profil)
        HesabHa.hadafZakhireAzProfil(this, profil)

        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    // ---------- ابزارهای ساخت ----------
    private fun sarBakhsh(matn: String): TextView {
        val t = Abzar.tv(this, matn, 15f, Abzar.RANG_SABZ, bold = true)
        val p = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        p.topMargin = Abzar.dp(this, 22f)
        t.layoutParams = p
        return t
    }

    private fun entekhab(matn: String, faal: Boolean): TextView {
        val t = TextView(this)
        t.text = matn
        t.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
        t.typeface = FontBox.vazirBold(this)
        t.gravity = Gravity.CENTER
        t.setPadding(Abzar.dp(this, 14f), Abzar.dp(this, 12f), Abzar.dp(this, 14f), Abzar.dp(this, 12f))
        Abzar.lams(t)
        rangDokme(t, faal)
        return t
    }

    private fun rangDokme(t: TextView, faal: Boolean) {
        if (faal) {
            t.background = Abzar.kartGradient(this, Abzar.RANG_SABZ, Abzar.RANG_SABZKAM, 14f)
            t.setTextColor(Color.WHITE)
        } else {
            t.background = Abzar.kart(this, Abzar.RANG_KART, 14f)
            t.setTextColor(Abzar.RANG_KAMRANG)
        }
    }

    private fun voroodiAdad(rahnama: String): EditText {
        val e = EditText(this)
        e.hint = rahnama
        e.setTextColor(Abzar.RANG_MATN)
        e.setHintTextColor(Abzar.RANG_KAMRANG)
        e.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        e.typeface = FontBox.vazir(this)
        e.inputType = InputType.TYPE_CLASS_NUMBER
        e.background = Abzar.kart(this, Abzar.RANG_CODE, 12f)
        e.setPadding(Abzar.dp(this, 14f), Abzar.dp(this, 12f), Abzar.dp(this, 14f), Abzar.dp(this, 12f))
        val p = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        p.topMargin = Abzar.dp(this, 8f)
        e.layoutParams = p
        return e
    }
}
