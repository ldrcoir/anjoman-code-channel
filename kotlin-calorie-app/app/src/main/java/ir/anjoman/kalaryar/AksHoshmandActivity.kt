package ir.anjoman.kalaryar

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import org.json.JSONArray
import org.json.JSONObject

/*
 * عکس هوشمند (نسخهٔ ۲ — گردش ۵۳)
 * از غذا عکس می‌گیرد یا از گالری می‌آورد؛ عکس به سرویس هوش مصنوعیِ بینایی
 * (سازگار با OpenAI — پیش‌فرض: z.ai GLM-4.5V) می‌رود و نوع و حجم غذا را
 * می‌خواند و کالری هر مورد را برآورد می‌کند؛ کاربر تیک می‌زند و در دفترچه ثبت می‌شود.
 * کلید و آدرس سرویس داخل خود اپ تنظیم می‌شود (دکمهٔ ⚙).
 */
class AksHoshmandActivity : Activity() {

    private lateinit var neshandeAks: ImageView
    private lateinit var matnWazaat: TextView
    private lateinit var ghabeNatayej: LinearLayout
    private var aks: Bitmap? = null
    private val natayej = ArrayList<Trio>()
    private var vohed = "nahaar"

    // یک برآورد هوش مصنوعی
    data class Trio(val esm: String, val gram: Int, val kalar: Int)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Abzar.rangVaredKon(this)

        val root = LinearLayout(this)
        root.layoutDirection = View.LAYOUT_DIRECTION_RTL
        root.setBackgroundColor(Abzar.RANG_ZAMIN)
        root.orientation = LinearLayout.VERTICAL

        val scroll = ScrollView(this)
        scroll.isFillViewport = true
        val ghabe = LinearLayout(this)
        ghabe.orientation = LinearLayout.VERTICAL
        ghabe.setPadding(Abzar.dp(this, 18f), Abzar.dp(this, 16f), Abzar.dp(this, 18f), Abzar.dp(this, 24f))
        scroll.addView(ghabe)
        root.addView(scroll)

        // سرصفحه
        val onvan = Abzar.tv(this, "📸 عکس هوشمند", 26f, Abzar.RANG_MATN)
        onvan.typeface = FontBox.vazirBlack(this)
        ghabe.addView(onvan)
        val tozih = Abzar.tv(this, "از غذا عکس بگیر — نوع و حجمش را هوش مصنوعی می‌خواند و کالری برآورد می‌کند", 13f, Abzar.RANG_KAMRANG)
        tozih.setPadding(0, Abzar.dp(this, 4f), 0, 0)
        ghabe.addView(tozih)

        // دکمه‌ها
        val safDokme = LinearLayout(this)
        safDokme.orientation = LinearLayout.HORIZONTAL
        ghabe.addView(safDokme, safP(12))

        val dokmeAks = Abzar.dokme(this, "📷 دوربین", Abzar.RANG_SABZ, Abzar.RANG_ZAMIN)
        dokmeAks.setOnClickListener {
            try {
                startActivityForResult(Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE), 41)
            } catch (_: Exception) {
                peygham("دوربین در دسترس نیست؛ گالری را امتحان کن")
            }
        }
        safDokme.addView(dokmeAks, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))

        val dokmeGaleri = Abzar.dokme(this, "🖼 گالری", Abzar.RANG_SABZKAM, Abzar.RANG_ZAMIN, khateDor = true)
        dokmeGaleri.setOnClickListener {
            val i = Intent(Intent.ACTION_GET_CONTENT)
            i.type = "image/*"
            startActivityForResult(Intent.createChooser(i, "عکس غذا را انتخاب کن"), 42)
        }
        val pG = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
        pG.marginStart = Abzar.dp(this, 8f)
        safDokme.addView(dokmeGaleri, pG)

        val dokmeTanzim = Abzar.dokme(this, "⚙", Abzar.RANG_KHAKISTARI, Abzar.RANG_MATN, khateDor = true)
        dokmeTanzim.setOnClickListener { sohbatTanzimat() }
        val pT = LinearLayout.LayoutParams(Abzar.dp(this, 52f), ViewGroup.LayoutParams.WRAP_CONTENT)
        pT.marginStart = Abzar.dp(this, 8f)
        safDokme.addView(dokmeTanzim, pT)

        // قاب پیش‌نمایش عکس
        val qab = LinearLayout(this)
        qab.orientation = LinearLayout.VERTICAL
        qab.gravity = Gravity.CENTER
        qab.background = Abzar.kart(this, Abzar.RANG_KART, 20f)
        qab.setPadding(Abzar.dp(this, 10f), Abzar.dp(this, 10f), Abzar.dp(this, 10f), Abzar.dp(this, 10f))
        neshandeAks = ImageView(this)
        neshandeAks.adjustViewBounds = true
        neshandeAks.scaleType = ImageView.ScaleType.FIT_CENTER
        neshandeAks.setImageDrawable(null)
        qab.addView(neshandeAks, LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, Abzar.dp(this, 210f)
        ))
        val neshaneKhali = Abzar.tv(this, "هنوز عکسی انتخاب نشده", 13f, Abzar.RANG_KAMRANG)
        neshaneKhali.gravity = Gravity.CENTER
        neshaneKhali.tag = "khali"
        qab.addView(neshaneKhali)
        ghabe.addView(qab, safP(14))

        // دکمهٔ تحلیل
        val dokmeTahlil = Abzar.dokme(this, "🧠 کالری کن!", Abzar.RANG_TALAEI, Abzar.RANG_ZAMIN)
        dokmeTahlil.setOnClickListener { tahlilKon(dokmeTahlil) }
        ghabe.addView(dokmeTahlil, safP(14))

        matnWazaat = Abzar.tv(this, "", 13f, Abzar.RANG_KAMRANG)
        ghabe.addView(matnWazaat, safP(6))

        // نتایج
        ghabeNatayej = LinearLayout(this)
        ghabeNatayej.orientation = LinearLayout.VERTICAL
        ghabe.addView(ghabeNatayej, safP(6))

        setContentView(root)
    }

    private fun safP(t: Int): LinearLayout.LayoutParams {
        val p = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        p.topMargin = Abzar.dp(this, t.toFloat())
        return p
    }

    private fun peygham(m: String) {
        matnWazaat.text = m
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK || data == null) return
        val bmp: Bitmap? = when (requestCode) {
            41 -> try {
                @Suppress("DEPRECATION")
                data.extras?.get("data") as? Bitmap
            } catch (_: Exception) { null }
            42 -> aksAzGaleri(data.data)
            else -> null
        }
        if (bmp == null) {
            peygham("عکس خوانده نشد؛ دوباره امتحان کن")
            return
        }
        aks = kamKon(bmp)
        neshandeAks.setImageBitmap(aks)
        val khali = findViewWithTag<View>("khali")
        khali?.visibility = View.GONE
        peygham("عکس آماده است — «کالری کن!» را بزن")
        natayej.clear()
        ghabeNatayej.removeAllViews()
    }

    private fun aksAzGaleri(uri: Uri?): Bitmap? {
        if (uri == null) return null
        return try {
            val inp: InputStream? = contentResolver.openInputStream(uri)
            val bmp = BitmapFactory.decodeStream(inp)
            inp?.close()
            bmp
        } catch (_: Exception) {
            null
        }
    }

    // کوچک‌سازی عکس تا ارسال سریع و کم‌مصرف باشد
    private fun kamKon(bmp: Bitmap): Bitmap {
        val had = 1024
        val nosbat = bmp.width.toFloat() / bmp.height.toFloat()
        return if (bmp.width > bmp.height) {
            Bitmap.createScaledBitmap(bmp, had, (had / nosbat).toInt().coerceAtLeast(1), true)
        } else {
            Bitmap.createScaledBitmap(bmp, (had * nosbat).toInt().coerceAtLeast(1), had, true)
        }
    }

    // ---------- تحلیل با هوش مصنوعی ----------
    private fun tahlilKon(dokme: Button) {
        val bmp = aks
        if (bmp == null) {
            peygham("اول عکس بگیر یا از گالری انتخاب کن")
            return
        }
        val kelid = Khazide.aiKelid(this)
        if (kelid.isBlank()) {
            peygham("اول با ⚙ کلید هوش مصنوعی را وارد کن (یک بار برای همیشه)")
            sohbatTanzimat()
            return
        }
        dokme.isEnabled = false
        dokme.alpha = 0.6f
        peygham("⏳ در حال خواندن عکس… چند ثانیه صبر کن")
        ghabeNatayej.removeAllViews()

        Thread {
            try {
                val javab = bekhonAzAI(bmp, kelid)
                val list = jayegahKon(javab)
                runOnUiThread {
                    dokme.isEnabled = true
                    dokme.alpha = 1f
                    if (list.isEmpty()) {
                        peygham("چیزی شناسایی نشد؛ عکس واضح‌تری امتحان کن یا مدل را با ⚙ عوض کن")
                    } else {
                        peygham("✓ ${list.size} مورد شناسایی شد — تیک بزن و ثبت کن")
                        natayejNeshanBedeh(list)
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    dokme.isEnabled = true
                    dokme.alpha = 1f
                    peygham("خطا: ${e.message?.take(120) ?: "ارتباط برقرار نشد"} — با ⚙ آدرس/کلید/مدل را چک کن")
                }
            }
        }.start()
    }

    private fun bekhonAzAI(bmp: Bitmap, kelid: String): String {
        val kh = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.JPEG, 80, kh)
        val dataUrl = "data:image/jpeg;base64," + Base64.encodeToString(kh.toByteArray(), Base64.NO_WRAP)

        val soal = JSONObject()
        soal.put("model", Khazide.aiModel(this))
        val peyghamha = JSONArray()
        val m = JSONObject()
        m.put("role", "user")
        val ajza = JSONArray()
        val aksJ = JSONObject()
        aksJ.put("type", "image_url")
        aksJ.put("image_url", JSONObject().put("url", dataUrl))
        ajza.put(aksJ)
        val matnJ = JSONObject()
        matnJ.put("type", "text")
        matnJ.put("text", "این عکس غذاست. همهٔ خوراکی‌های دیده‌شده را شناسایی کن و برای هرکدام " +
                "مقدار تقریبی روی بشقاب را بر حسب گرم و کالری تخمینی‌اش را حساب کن. " +
                "فقط و فقط JSON خالص بده بدون هیچ توضیح اضافه، به این شکل: " +
                "[{\"ghaza\":\"نام غذا\",\"gram\":150,\"kalari\":350}]")
        ajza.put(matnJ)
        m.put("content", ajza)
        peyghamha.put(m)
        soal.put("messages", peyghamha)
        soal.put("max_tokens", 1200)

        val etesal = URL(Khazide.aiAPI(this)).openConnection() as HttpURLConnection
        etesal.requestMethod = "POST"
        etesal.connectTimeout = 20000
        etesal.readTimeout = 60000
        etesal.doOutput = true
        etesal.setRequestProperty("Content-Type", "application/json")
        etesal.setRequestProperty("Authorization", "Bearer $kelid")
        etesal.outputStream.use { it.write(soal.toString().toByteArray(Charsets.UTF_8)) }
        val kod = etesal.responseCode
        val javab = if (kod in 200..299) etesal.inputStream.readBytes().toString(Charsets.UTF_8)
        else etesal.errorStream?.readBytes()?.toString(Charsets.UTF_8) ?: ""
        etesal.disconnect()
        if (kod !in 200..299) throw Exception("کد $kod: " + javab.take(140))
        return javab
    }

    // جواب مدل → فهرست غذا/گرم/کالری
    private fun jayegahKon(kham: String): List<Trio> {
        val natayeh = ArrayList<Trio>()
        try {
            var matn = kham
            // جواب در قالب چت: choices[0].message.content
            val j = JSONObject(matn)
            val entekhab = j.optJSONArray("choices")
            matn = if (entekhab != null && entekhab.length() > 0)
                entekhab.getJSONObject(0).optJSONObject("message")?.optString("content") ?: ""
            else matn
            // حذف بلوک‌های ``` و پیدا کردن آرایهٔ JSON
            matn = matn.replace("```json", " ").replace("```", " ")
            val shoru = matn.indexOf('[')
            val akhar = matn.lastIndexOf(']')
            if (shoru < 0 || akhar <= shoru) return natayeh
            val arr = JSONArray(matn.substring(shoru, akhar + 1))
            for (i in 0 until arr.length()) {
                val o = arr.getJSONObject(i)
                val esm = o.optString("ghaza", o.optString("name", "")).trim()
                if (esm.isEmpty()) continue
                var gram = o.optDouble("gram", o.optDouble("megdar", 0.0)).toInt()
                var kalar = o.optDouble("kalari", o.optDouble("kcal", Double.NaN))
                if (gram <= 0) gram = 100
                if (kalar.isNaN() || kalar <= 0) {
                    // اگر مدل کالری نداد، از پایگاه خود اپ تخمین می‌زنیم
                    val p = PayegahGhaza.baEsm(esm)
                    kalar = if (p != null) p.kalar * gram / 100.0 else gram * 1.5
                }
                natayeh.add(Trio(esm, gram, kalar.toInt()))
            }
        } catch (_: Exception) {
        }
        return natayeh
    }

    // ---------- نمایش نتایج و ثبت ----------
    private fun natayejNeshanBedeh(list: List<Trio>) {
        ghabeNatayej.removeAllViews()
        natayej.clear()
        natayej.addAll(list)

        for (t in list) {
            val radif = CheckBox(this)
            radif.text = "${t.esm} — ${Abzar.adadFarsi(t.gram)} گرم ≈ ${Abzar.adadFarsi(t.kalar)} کالری"
            radif.isChecked = true
            radif.setTextColor(Abzar.RANG_MATN)
            radif.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
            radif.typeface = FontBox.vazir(this)
            radif.buttonTintList = ColorStateList.valueOf(Abzar.RANG_SABZ)
            radif.setPadding(Abzar.dp(this, 6f), Abzar.dp(this, 6f), Abzar.dp(this, 6f), Abzar.dp(this, 6f))
            radif.background = Abzar.kart(this, Abzar.RANG_KART, 12f)
            ghabeNatayej.addView(radif, safP(8))
        }

        // انتخاب وعده
        ghabeNatayej.addView(Abzar.tv(this, "کدام وعده؟", 14f, Abzar.RANG_SABZ, bold = true), safP(10))
        val safVohed = LinearLayout(this)
        safVohed.orientation = LinearLayout.HORIZONTAL
        val dokmeHa = HashMap<String, TextView>()
        for ((kod, matn) in listOf("sohbat" to "صبحانه", "nahaar" to "ناهار", "sham" to "شام", "miani" to "میان‌وعده")) {
            val d = TextView(this)
            d.text = matn
            d.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
            d.typeface = FontBox.vazirBold(this)
            d.gravity = Gravity.CENTER
            d.setPadding(Abzar.dp(this, 6f), Abzar.dp(this, 10f), Abzar.dp(this, 6f), Abzar.dp(this, 10f))
            Abzar.lams(d)
            d.setOnClickListener {
                vohed = kod
                for ((k, dd) in dokmeHa) {
                    if (k == kod) {
                        dd.background = Abzar.kart(this, Abzar.RANG_SABZ, 12f)
                        dd.setTextColor(Color.WHITE)
                    } else {
                        dd.background = Abzar.kart(this, Abzar.RANG_CODE, 12f)
                        dd.setTextColor(Abzar.RANG_KAMRANG)
                    }
                }
            }
            safVohed.addView(d, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))
            dokmeHa[kod] = d
        }
        dokmeHa["nahaar"]?.background = Abzar.kart(this, Abzar.RANG_SABZ, 12f)
        dokmeHa["nahaar"]?.setTextColor(Color.WHITE)
        ghabeNatayej.addView(safVohed, safP(8))

        val dokmeSabt = Abzar.dokme(this, "ثبت انتخاب‌ها در دفترچه ✓", Abzar.RANG_SABZ, Abzar.RANG_ZAMIN)
        dokmeSabt.setOnClickListener {
            var sabtShod = 0
            for (i in 0 until ghabeNatayej.childCount) {
                val b = ghabeNatayej.getChildAt(i)
                if (b is CheckBox && b.isChecked && i < natayej.size) {
                    val t = natayej[i]
                    val p = PayegahGhaza.baEsm(t.esm)
                    Khazide.vorodiEzafe(
                        this, Abzar.kelidEmrooz(),
                        Varedi(
                            t.esm, t.gram, t.kalar,
                            p?.protein?.times(t.gram / 100.0) ?: 0.0,
                            p?.karbohidrat?.times(t.gram / 100.0) ?: 0.0,
                            p?.charbi?.times(t.gram / 100.0) ?: 0.0,
                            vohed
                        )
                    )
                    sabtShod++
                }
            }
            android.widget.Toast.makeText(this, "${Abzar.adadFarsi(sabtShod)} مورد در دفترچه ثبت شد ✓", android.widget.Toast.LENGTH_SHORT).show()
            if (sabtShod > 0) finish()
        }
        ghabeNatayej.addView(dokmeSabt, safP(12))
    }

    // ---------- تنظیمات سرویس هوش مصنوعی ----------
    private fun sohbatTanzimat() {
        val ghabe = LinearLayout(this)
        ghabe.orientation = LinearLayout.VERTICAL
        ghabe.setPadding(Abzar.dp(this, 22f), Abzar.dp(this, 20f), Abzar.dp(this, 22f), Abzar.dp(this, 12f))
        ghabe.setBackgroundColor(Abzar.RANG_KART)

        ghabe.addView(Abzar.tv(this, "⚙ سرویس هوش مصنوعی", 19f, Abzar.RANG_MATN, bold = true))
        ghabe.addView(Abzar.tv(this, "هر سرویسِ سازگار با OpenAI بینایی‌دار کار می‌کند: z.ai، OpenRouter و… کلید فقط روی همین گوشی ذخیره می‌شود.", 12f, Abzar.RANG_KAMRANG))

        fun edt(matn: String): EditText {
            val e = EditText(this)
            e.setText(matn)
            e.setTextColor(Abzar.RANG_MATN)
            e.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
            e.typeface = FontBox.vazir(this)
            e.background = Abzar.kart(this, Abzar.RANG_CODE, 12f)
            e.setPadding(Abzar.dp(this, 12f), Abzar.dp(this, 8f), Abzar.dp(this, 12f), Abzar.dp(this, 8f))
            return e
        }

        ghabe.addView(Abzar.tv(this, "آدرس سرویس", 12f, Abzar.RANG_SABZ, bold = true))
        val edtAPI = edt(Khazide.aiAPI(this))
        ghabe.addView(edtAPI, safP(6))

        ghabe.addView(Abzar.tv(this, "کلید (API Key)", 12f, Abzar.RANG_SABZ, bold = true))
        val edtKelid = edt(Khazide.aiKelid(this))
        ghabe.addView(edtKelid, safP(6))

        ghabe.addView(Abzar.tv(this, "نام مدل بینایی", 12f, Abzar.RANG_SABZ, bold = true))
        val edtModel = edt(Khazide.aiModel(this))
        ghabe.addView(edtModel, safP(6))

        val dialog = AlertDialog.Builder(this)
            .setView(ghabe)
            .create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val safDokme = LinearLayout(this)
        safDokme.orientation = LinearLayout.HORIZONTAL
        val dokmeEnseraf = Abzar.dokme(this, "بستن", Abzar.RANG_KHAKISTARI, Abzar.RANG_KAMRANG, khateDor = true)
        dokmeEnseraf.setOnClickListener { dialog.dismiss() }
        safDokme.addView(dokmeEnseraf, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))
        val dokmeSabt = Abzar.dokme(this, "ذخیره ✓", Abzar.RANG_SABZ, Abzar.RANG_ZAMIN)
        dokmeSabt.setOnClickListener {
            Khazide.aiZakhire(this, edtAPI.text.toString(), edtKelid.text.toString(), edtModel.text.toString())
            dialog.dismiss()
            android.widget.Toast.makeText(this, "تنظیمات ذخیره شد ✓", android.widget.Toast.LENGTH_SHORT).show()
        }
        val pSabt = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
        pSabt.marginStart = Abzar.dp(this, 8f)
        safDokme.addView(dokmeSabt, pSabt)
        ghabe.addView(safDokme, safP(14))
    }
}
