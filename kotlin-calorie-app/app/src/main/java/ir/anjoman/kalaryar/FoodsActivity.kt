package ir.anjoman.kalaryar

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

/*
 * افزودن غذا: جست‌وجوی فازی، فیلتر دسته، انتخاب سهم و وعده
 * ثبت در دفترچهٔ امروز با محاسبهٔ کالری و درشت‌مغذی به نسبت گرم
 */
class FoodsActivity : Activity() {

    private lateinit var ghabe: LinearLayout
    private var dasteFaali = "همه"
    private var jostoJoo = ""
    private val dokmeDasteHa = ArrayList<Pair<String, TextView>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Abzar.rangVaredKon(this)

        val root = LinearLayout(this)
        root.layoutDirection = View.LAYOUT_DIRECTION_RTL
        root.setBackgroundColor(Abzar.RANG_ZAMIN)
        root.orientation = LinearLayout.VERTICAL

        // سرصفحه با دکمهٔ بازگشت
        root.addView(sarOnvan("افزودن غذا", "به‌ازای ۱۰۰ گرم — غذای خودت را هم می‌توانی اضافه کنی ⭐"))

        // جست‌وجو
        val edtJostOJoo = EditText(this)
        edtJostOJoo.hint = "🔍 اسم غذا را بنویس: برنج، پیتزا، ماست..."
        edtJostOJoo.setTextColor(Abzar.RANG_MATN)
        edtJostOJoo.setHintTextColor(Abzar.RANG_KAMRANG)
        edtJostOJoo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
        edtJostOJoo.typeface = FontBox.vazir(this)
        edtJostOJoo.background = Abzar.kart(this, Abzar.RANG_KART, 14f)
        edtJostOJoo.setPadding(Abzar.dp(this, 14f), Abzar.dp(this, 12f), Abzar.dp(this, 14f), Abzar.dp(this, 12f))
        val pJostOJoo = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pJostOJoo.setMargins(Abzar.dp(this, 18f), Abzar.dp(this, 12f), Abzar.dp(this, 18f), 0)
        root.addView(edtJostOJoo, pJostOJoo)

        // چیپ‌های دسته‌ها
        val scrollDaste = HorizontalScrollView(this)
        scrollDaste.isHorizontalScrollBarEnabled = false
        val safDaste = LinearLayout(this)
        safDaste.orientation = LinearLayout.HORIZONTAL
        safDaste.setPadding(Abzar.dp(this, 14f), Abzar.dp(this, 10f), Abzar.dp(this, 14f), 0)
        for (daste in PayegahGhaza.dasteHa) {
            val d = TextView(this)
            d.text = daste
            d.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
            d.typeface = FontBox.vazirBold(this)
            d.setPadding(Abzar.dp(this, 14f), Abzar.dp(this, 8f), Abzar.dp(this, 14f), Abzar.dp(this, 8f))
            Abzar.lams(d)
            d.setOnClickListener {
                dasteFaali = daste
                rangDasteHa()
                safheBekon()
            }
            val pD = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
            pD.marginStart = Abzar.dp(this, 6f)
            safDaste.addView(d, pD)
            dokmeDasteHa.add(daste to d)
        }

        // چیپ غذاهای دلخواه + دکمهٔ افزودن (نسخهٔ ۲)
        val chipMan = TextView(this)
        chipMan.text = "⭐ غذای من"
        chipMan.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
        chipMan.typeface = FontBox.vazirBold(this)
        chipMan.setPadding(Abzar.dp(this, 14f), Abzar.dp(this, 8f), Abzar.dp(this, 14f), Abzar.dp(this, 8f))
        Abzar.lams(chipMan)
        chipMan.setOnClickListener {
            dasteFaali = "غذای من"
            rangDasteHa()
            safheBekon()
        }
        val pMan = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pMan.marginStart = Abzar.dp(this, 6f)
        safDaste.addView(chipMan, pMan)
        dokmeDasteHa.add("غذای من" to chipMan)

        val chipEzafe = TextView(this)
        chipEzafe.text = "＋ افزودن"
        chipEzafe.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
        chipEzafe.typeface = FontBox.vazirBold(this)
        chipEzafe.setTextColor(Color.WHITE)
        chipEzafe.background = Abzar.kartGradient(this, Abzar.RANG_TALAEI, Abzar.RANG_NARENJI, 12f)
        chipEzafe.setPadding(Abzar.dp(this, 14f), Abzar.dp(this, 8f), Abzar.dp(this, 14f), Abzar.dp(this, 8f))
        Abzar.lams(chipEzafe)
        chipEzafe.setOnClickListener { sohbatGhazaJadid() }
        val pEzafe = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pEzafe.marginStart = Abzar.dp(this, 6f)
        safDaste.addView(chipEzafe, pEzafe)

        scrollDaste.addView(safDaste)
        root.addView(scrollDaste)

        // فهرست
        val scroll = ScrollView(this)
        scroll.isFillViewport = true
        root.addView(scroll, LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        ))

        ghabe = LinearLayout(this)
        ghabe.orientation = LinearLayout.VERTICAL
        ghabe.setPadding(Abzar.dp(this, 18f), Abzar.dp(this, 10f), Abzar.dp(this, 18f), Abzar.dp(this, 22f))
        scroll.addView(ghabe, ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ))

        edtJostOJoo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, a: Int, b: Int, c: Int) {}
            override fun onTextChanged(s: CharSequence?, a: Int, b: Int, c: Int) {}
            override fun afterTextChanged(s: Editable?) {
                jostoJoo = s?.toString() ?: ""
                safheBekon()
            }
        })

        setContentView(root)
        rangDasteHa()
        safheBekon()
    }

    private fun rangDasteHa() {
        for ((daste, d) in dokmeDasteHa) {
            if (daste == dasteFaali) {
                d.background = Abzar.kartGradient(this, Abzar.RANG_SABZ, Abzar.RANG_SABZKAM, 12f)
                d.setTextColor(Color.WHITE)
            } else {
                d.background = Abzar.kart(this, Abzar.RANG_KART, 12f)
                d.setTextColor(Abzar.RANG_KAMRANG)
            }
        }
    }

    private fun safheBekon() {
        ghabe.removeAllViews()

        // پایگاه اصلی + غذاهای دلخواه کاربر (نسخهٔ ۲)
        val khodamHa = Khazide.ghazayeManBekhon(this)
        val hame = PayegahGhaza.ghazaHa + khodamHa
        var fehrrest: List<Ghaza> = when {
            jostoJoo.isNotBlank() -> {
                val pak = jostoJoo.trim()
                val daghigh = hame.filter { it.esm == pak }
                if (daghigh.isNotEmpty()) daghigh
                else {
                    val ebteda = hame.filter { it.esm.startsWith(pak) }
                    if (ebteda.isNotEmpty()) ebteda
                    else hame.filter { it.esm.contains(pak) }
                }
            }
            dasteFaali == "غذای من" -> khodamHa
            dasteFaali != "همه" -> hame.filter { it.daste == dasteFaali }
            else -> hame
        }

        if (fehrrest.isEmpty()) {
            val khali = Abzar.tv(this, "چیزی پیدا نشد؛ اسم دیگری امتحان کن 🤔", 15f, Abzar.RANG_KAMRANG)
            khali.gravity = Gravity.CENTER
            val pKhali = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
            pKhali.topMargin = Abzar.dp(this, 40f)
            ghabe.addView(khali, pKhali)
            return
        }

        val esmeKhodamHa = khodamHa.map { it.esm }.toSet()
        for (g in fehrrest) {
            val pKart = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
            pKart.topMargin = Abzar.dp(this, 10f)
            ghabe.addView(kartGhaza(g, g.esm in esmeKhodamHa), pKart)
        }
    }

    // کارت هر غذا — اگر غذای دلخواه کاربر باشد مهر «من» می‌گیرد و با لمسِ طولانی حذف می‌شود
    private fun kartGhaza(g: Ghaza, khodam: Boolean = false): LinearLayout {
        val kart = LinearLayout(this)
        kart.orientation = LinearLayout.HORIZONTAL
        kart.gravity = Gravity.CENTER_VERTICAL
        kart.background = Abzar.kart(this, Abzar.RANG_KART)
        kart.setPadding(Abzar.dp(this, 14f), Abzar.dp(this, 12f), Abzar.dp(this, 14f), Abzar.dp(this, 12f))
        kart.isClickable = true
        Abzar.lams(kart)
        kart.setOnClickListener { sohbatDialog(g) }

        val sotoon = LinearLayout(this)
        sotoon.orientation = LinearLayout.VERTICAL
        val matnEsm = Abzar.tv(this, g.esm, 16f, Abzar.RANG_MATN, bold = true)
        sotoon.addView(matnEsm)
        val matnRiz = Abzar.tv(
            this,
            "۱۰۰ گرم: ${Abzar.adadFarsi(g.kalar)} کالری • پ ${Abzar.adadFarsi(g.protein)} • ک ${Abzar.adadFarsi(g.karbohidrat)} • چ ${Abzar.adadFarsi(g.charbi)}",
            12f, Abzar.RANG_KAMRANG
        )
        matnRiz.setPadding(0, Abzar.dp(this, 3f), 0, 0)
        sotoon.addView(matnRiz)
        kart.addView(sotoon, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))

        // نمرهٔ سلامت
        val nomre = HoshmandYar.nomreSalamat(g)
        val rangNomre = when (nomre) {
            "A", "B" -> Abzar.RANG_SABZ
            "C" -> Abzar.RANG_TALAEI
            else -> Abzar.RANG_GHERMEZ
        }
        val mohr = TextView(this)
        mohr.text = nomre
        mohr.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17f)
        mohr.typeface = FontBox.vazirBlack(this)
        mohr.gravity = Gravity.CENTER
        mohr.setTextColor(Color.WHITE)
        mohr.background = Abzar.dayereh(this, rangNomre)
        kart.addView(mohr, LinearLayout.LayoutParams(Abzar.dp(this, 38f), Abzar.dp(this, 38f)))

        if (khodam) {
            val mohrMan = TextView(this)
            mohrMan.text = "⭐"
            mohrMan.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
            mohrMan.gravity = Gravity.CENTER
            kart.addView(mohrMan, LinearLayout.LayoutParams(Abzar.dp(this, 26f), Abzar.dp(this, 26f)))
            kart.setOnLongClickListener {
                taeedHazfKhodam(g.esm)
                true
            }
        }

        return kart
    }

    // ---------- افزودن غذای دلخواه (نسخهٔ ۲) ----------
    private fun sohbatGhazaJadid() {
        val ghabe = LinearLayout(this)
        ghabe.orientation = LinearLayout.VERTICAL
        ghabe.setPadding(Abzar.dp(this, 22f), Abzar.dp(this, 20f), Abzar.dp(this, 22f), Abzar.dp(this, 12f))
        ghabe.setBackgroundColor(Abzar.RANG_KART)

        ghabe.addView(Abzar.tv(this, " غذای من ⭐", 20f, Abzar.RANG_MATN, bold = true))
        ghabe.addView(Abzar.tv(this, "اسم غذا و کالری‌اش را خودت تعیین کن؛ برای هر مقدار که می‌خواهی.", 13f, Abzar.RANG_KAMRANG))

        fun edtedad(hint: String, adadi: Boolean): EditText {
            val e = EditText(this)
            e.hint = hint
            e.setTextColor(Abzar.RANG_MATN)
            e.setHintTextColor(Abzar.RANG_KAMRANG)
            e.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
            e.typeface = FontBox.vazir(this)
            e.background = Abzar.kart(this, Abzar.RANG_CODE, 12f)
            e.setPadding(Abzar.dp(this, 14f), Abzar.dp(this, 10f), Abzar.dp(this, 14f), Abzar.dp(this, 10f))
            if (adadi) e.inputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
            return e
        }

        val pE = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pE.topMargin = Abzar.dp(this, 8f)

        ghabe.addView(Abzar.tv(this, "اسم غذا", 13f, Abzar.RANG_SABZ, bold = true))
        val edtEsm = edtedad("مثلاً: خورشت مامان‌بزرگ", false)
        ghabe.addView(edtEsm, pE)

        ghabe.addView(Abzar.tv(this, "کالری هر سهم (کیلوکالری)", 13f, Abzar.RANG_SABZ, bold = true))
        val edtKalar = edtedad("مثلاً: ۳۵۰", true)
        ghabe.addView(edtKalar, pE)

        ghabe.addView(Abzar.tv(this, "سهم رایج چقدر است؟ (گرم یا عدد)", 13f, Abzar.RANG_SABZ, bold = true))
        val edtSahm = edtedad("مثلاً: ۲۵۰", true)
        edtSahm.setText("100")
        ghabe.addView(edtSahm, pE)

        val dialog = AlertDialog.Builder(this)
            .setView(ghabe)
            .create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val safDokme = LinearLayout(this)
        safDokme.orientation = LinearLayout.HORIZONTAL
        val dokmeEnseraf = Abzar.dokme(this, "انصراف", Abzar.RANG_KHAKISTARI, Abzar.RANG_KAMRANG, khateDor = true)
        dokmeEnseraf.setOnClickListener { dialog.dismiss() }
        safDokme.addView(dokmeEnseraf, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))

        val dokmeSabt = Abzar.dokme(this, "اضافه کن ⭐", Abzar.RANG_TALAEI, Abzar.RANG_ZAMIN)
        dokmeSabt.setOnClickListener {
            val esm = edtEsm.text.toString().trim()
            val kalar = edtKalar.text.toString().toDoubleOrNull()
            val sahm = edtSahm.text.toString().toIntOrNull()
            if (esm.isEmpty() || kalar == null || kalar <= 0) {
                android.widget.Toast.makeText(this, "اسم و کالری را درست پر کن", android.widget.Toast.LENGTH_SHORT).show()
            } else {
                val sahmeGhaza = if (sahm != null && sahm > 0) sahm else 100
                val ghazaJadid = Ghaza(esm, "غذای من", 0, 0.0, 0.0, 0.0, "سهم من", sahmeGhaza)
                // کالری هر ۱۰۰ گرم از روی سهم تعریف‌شده برمی‌گردد تا محاسبهٔ سهم دقیق بماند
                val kalar100 = (kalar * 100.0 / sahmeGhaza).toInt()
                val ghazaNahaei = ghazaJadid.copy(kalar = kalar100)
                Khazide.ghazayeManEzafe(this, ghazaNahaei)
                dialog.dismiss()
                dasteFaali = "غذای من"
                rangDasteHa()
                safheBekon()
                android.widget.Toast.makeText(this, "$esm به غذاهای من اضافه شد ⭐", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
        val pSabt = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
        pSabt.marginStart = Abzar.dp(this, 8f)
        safDokme.addView(dokmeSabt, pSabt)

        val pSafDokme = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pSafDokme.topMargin = Abzar.dp(this, 16f)
        ghabe.addView(safDokme, pSafDokme)
    }

    private fun taeedHazfKhodam(esm: String) {
        AlertDialog.Builder(this)
            .setTitle("حذف غذا؟")
            .setMessage("«$esm» از غذاهای من پاک شود؟")
            .setPositiveButton("حذف") { _, _ ->
                Khazide.ghazayeManHazf(this, esm)
                safheBekon()
                android.widget.Toast.makeText(this, "حذف شد", android.widget.Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("نه", null)
            .show()
    }

    // ---------- دیالوگ سهم و وعده ----------
    private fun sohbatDialog(g: Ghaza) {
        val ghabe = LinearLayout(this)
        ghabe.orientation = LinearLayout.VERTICAL
        ghabe.setPadding(Abzar.dp(this, 22f), Abzar.dp(this, 20f), Abzar.dp(this, 22f), Abzar.dp(this, 12f))
        ghabe.setBackgroundColor(Abzar.RANG_KART)

        // اطلاعات غذا
        ghabe.addView(Abzar.tv(this, g.esm, 20f, Abzar.RANG_MATN, bold = true))
        val matnInfo = Abzar.tv(
            this,
            "۱۰۰ گرم = ${Abzar.adadFarsi(g.kalar)} کالری | سهم رایج: ${g.porzionEsm} (${Abzar.adadFarsi(g.porzionGram)} گرم)",
            13f, Abzar.RANG_KAMRANG
        )
        matnInfo.setPadding(0, Abzar.dp(this, 4f), 0, 0)
        ghabe.addView(matnInfo)

        // پیش‌فرض‌های سهم
        ghabe.addView(jaFazol(14))
        ghabe.addView(Abzar.tv(this, "چقدر می‌خوری؟ (گرم)", 14f, Abzar.RANG_SABZ, bold = true))
        val edtGram = EditText(this)
        edtGram.setText(g.porzionGram.toString())
        edtGram.setTextColor(Abzar.RANG_MATN)
        edtGram.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        edtGram.typeface = FontBox.vazirBold(this)
        edtGram.inputType = android.text.InputType.TYPE_CLASS_NUMBER
        edtGram.background = Abzar.kart(this, Abzar.RANG_CODE, 12f)
        edtGram.setPadding(Abzar.dp(this, 14f), Abzar.dp(this, 10f), Abzar.dp(this, 14f), Abzar.dp(this, 10f))
        val pGram = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pGram.topMargin = Abzar.dp(this, 8f)
        ghabe.addView(edtGram, pGram)

        // پیش‌نمایش زندهٔ کالری
        val matnPishnamayesh = Abzar.tv(this, "", 14f, Abzar.RANG_TALAEI, bold = true)
        matnPishnamayesh.setPadding(0, Abzar.dp(this, 10f), 0, 0)
        ghabe.addView(matnPishnamayesh)
        val tozirKon = {
            val gr = edtGram.text.toString().toIntOrNull() ?: 0
            val k = (g.kalar * gr / 100.0).toInt()
            matnPishnamayesh.text = "≈ ${Abzar.adadFarsi(k)} کالری • پ ${Abzar.adadFarsi(g.protein * gr / 100.0)} • ک ${Abzar.adadFarsi(g.karbohidrat * gr / 100.0)} • چ ${Abzar.adadFarsi(g.charbi * gr / 100.0)}"
        }
        edtGram.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, a: Int, b: Int, c: Int) {}
            override fun onTextChanged(s: CharSequence?, a: Int, b: Int, c: Int) {}
            override fun afterTextChanged(s: Editable?) { tozirKon() }
        })
        tozirKon()

        // انتخاب وعده
        ghabe.addView(jaFazol(14))
        ghabe.addView(Abzar.tv(this, "کدام وعده؟", 14f, Abzar.RANG_SABZ, bold = true))
        val vohedHa = listOf("sohbat" to "صبحانه", "nahaar" to "ناهار", "sham" to "شام", "miani" to "میان‌وعده")
        var vohed = "nahaar"
        val safVohed = LinearLayout(this)
        safVohed.orientation = LinearLayout.HORIZONTAL
        val dokmeVohedHa = HashMap<String, TextView>()
        for ((kod, matn) in vohedHa) {
            val d = TextView(this)
            d.text = matn
            d.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
            d.typeface = FontBox.vazirBold(this)
            d.gravity = Gravity.CENTER
            d.setPadding(Abzar.dp(this, 6f), Abzar.dp(this, 10f), Abzar.dp(this, 6f), Abzar.dp(this, 10f))
            Abzar.lams(d)
            d.setOnClickListener {
                vohed = kod
                for ((k, dd) in dokmeVohedHa) {
                    if (k == kod) {
                        dd.background = Abzar.kart(this, Abzar.RANG_SABZ, 12f)
                        dd.setTextColor(Color.WHITE)
                    } else {
                        dd.background = Abzar.kart(this, Abzar.RANG_CODE, 12f)
                        dd.setTextColor(Abzar.RANG_KAMRANG)
                    }
                }
            }
            val pD = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
            pD.marginStart = Abzar.dp(this, 4f)
            safVohed.addView(d, pD)
            dokmeVohedHa[kod] = d
        }
        // پیش‌فرض ناهار روشن
        dokmeVohedHa["nahaar"]?.background = Abzar.kart(this, Abzar.RANG_SABZ, 12f)
        dokmeVohedHa["nahaar"]?.setTextColor(Color.WHITE)
        val pSafVohed = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pSafVohed.topMargin = Abzar.dp(this, 8f)
        ghabe.addView(safVohed, pSafVohed)

        val dialog = AlertDialog.Builder(this)
            .setView(ghabe)
            .create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        // دکمه‌ها
        val safDokme = LinearLayout(this)
        safDokme.orientation = LinearLayout.HORIZONTAL
        val dokmeEnseraf = Abzar.dokme(this, "انصراف", Abzar.RANG_KHAKISTARI, Abzar.RANG_KAMRANG, khateDor = true)
        dokmeEnseraf.setOnClickListener { dialog.dismiss() }
        safDokme.addView(dokmeEnseraf, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))

        val dokmeSabt = Abzar.dokme(this, "ثبت کن ✓", Abzar.RANG_SABZ, Abzar.RANG_ZAMIN)
        dokmeSabt.setOnClickListener {
            val gr = edtGram.text.toString().toIntOrNull() ?: 0
            if (gr > 0) {
                Khazide.vorodiEzafe(
                    this, Abzar.kelidEmrooz(),
                    Varedi(
                        g.esm, gr,
                        (g.kalar * gr / 100.0).toInt(),
                        g.protein * gr / 100.0,
                        g.karbohidrat * gr / 100.0,
                        g.charbi * gr / 100.0,
                        vohed
                    )
                )
                dialog.dismiss()
                android.widget.Toast.makeText(this, "${g.esm} ثبت شد ✓", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
        val pSabt = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
        pSabt.marginStart = Abzar.dp(this, 8f)
        safDokme.addView(dokmeSabt, pSabt)

        val pSafDokme = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pSafDokme.topMargin = Abzar.dp(this, 16f)
        ghabe.addView(safDokme, pSafDokme)
    }

    private fun sarOnvan(onvan: String, tozih: String): View {
        val ghabe = LinearLayout(this)
        ghabe.orientation = LinearLayout.VERTICAL
        ghabe.setPadding(Abzar.dp(this, 18f), Abzar.dp(this, 20f), Abzar.dp(this, 18f), 0)

        val matnOnvan = Abzar.tv(this, onvan, 26f, Abzar.RANG_MATN)
        matnOnvan.typeface = FontBox.vazirBlack(this)
        ghabe.addView(matnOnvan)
        val matnTozih = Abzar.tv(this, tozih, 13f, Abzar.RANG_KAMRANG)
        matnTozih.setPadding(0, Abzar.dp(this, 4f), 0, 0)
        ghabe.addView(matnTozih)
        return ghabe
    }

    private fun jaFazol(dp: Int): View {
        val v = View(this)
        v.layoutParams = LinearLayout.LayoutParams(1, Abzar.dp(this, dp.toFloat()))
        return v
    }
}
