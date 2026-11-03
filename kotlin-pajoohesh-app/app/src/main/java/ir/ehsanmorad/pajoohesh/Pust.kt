package ir.ehsanmorad.pajoohesh

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

/*
 * جعبه‌ابزار کتاب + پالت تم‌ها
 * ۱۲ تم: کلاسیک کرم، شب مطالعه، کاغذ گرم، سبز آرام، بنفش شفق، طلایی سلطنتی،
 *        رزگلد دلنشین، نیلی اقیانوس، روز روشن، زیتون آرام، آسمان سحر، زغالی گرم
 * ۶ روشن + ۶ تیره با رنگ‌های خانواده‌های متفاوت (کرم/سفید/صورتی/زیتونی/آبی/فیروزه‌ای
 * و سرمه‌ای/سبز/بنفش/طلایی/نیلی/زغالی) تا یکدست نباشد
 * شش تم نخست با نسخهٔ ۱.۰ هم‌ترازند تا انتخاب قدیمی کاربران حفظ شود
 * تم انتخابی در حافظهٔ گوشی ذخیره می‌شود
 */

data class Tem(
    val esm: String,
    val zamin: Int,
    val kart: Int,
    val kartTir: Int,
    val matn: Int,
    val kamrang: Int,
    val asli: Int,
    val dovom: Int,
    val khater: Boolean  // تم تیره؟ (برای رنگ نوار وضعیت)
)

object TemEnekhab {

    val temHa = listOf(
        Tem("کلاسیک کرم", 0xFFF4EDDE.toInt(), 0xFFFDF9EE.toInt(), 0xFFEBE2CC.toInt(),
            0xFF33301F.toInt(), 0xFF8A7A57.toInt(), 0xFF0F766E.toInt(), 0xFF14B8A6.toInt(), false),
        Tem("شب مطالعه", 0xFF0B1220.toInt(), 0xFF141E31.toInt(), 0xFF0F1728.toInt(),
            0xFFE6F1FF.toInt(), 0xFF7E93B5.toInt(), 0xFF2DD4BF.toInt(), 0xFF0EA5E9.toInt(), true),
        Tem("کاغذ گرم", 0xFFF6E8D3.toInt(), 0xFFFDF4E5.toInt(), 0xFFECDBC0.toInt(),
            0xFF45301C.toInt(), 0xFF9A7B52.toInt(), 0xFFB45309.toInt(), 0xFFD97706.toInt(), false),
        Tem("سبز آرام", 0xFF0C1512.toInt(), 0xFF152420.toInt(), 0xFF0F1B17.toInt(),
            0xFFEAF7F0.toInt(), 0xFF8FAF9F.toInt(), 0xFF22C55E.toInt(), 0xFF16A34A.toInt(), true),
        Tem("بنفش شفق", 0xFF120E1F.toInt(), 0xFF1C1631.toInt(), 0xFF151026.toInt(),
            0xFFF0EAFF.toInt(), 0xFF9B8FC0.toInt(), 0xFFA78BFA.toInt(), 0xFF8B5CF6.toInt(), true),
        Tem("طلایی سلطنتی", 0xFF191307.toInt(), 0xFF241B0C.toInt(), 0xFF1C150A.toInt(),
            0xFFF5EEDC.toInt(), 0xFFB3A172.toInt(), 0xFFD4A017.toInt(), 0xFFF59E0B.toInt(), true),
        Tem("رزگلد دلنشین", 0xFFFBEFEF.toInt(), 0xFFFDF6F4.toInt(), 0xFFF3DFDD.toInt(),
            0xFF4A2C3A.toInt(), 0xFFB08793.toInt(), 0xFFD2627E.toInt(), 0xFFF0A3B5.toInt(), false),
        Tem("نیلی اقیانوس", 0xFF0A1628.toInt(), 0xFF12233D.toInt(), 0xFF0E1C31.toInt(),
            0xFFE0F2FE.toInt(), 0xFF7FA6C9.toInt(), 0xFF38BDF8.toInt(), 0xFF0284C7.toInt(), true),
        Tem("روز روشن", 0xFFF7F8FA.toInt(), 0xFFFFFFFF.toInt(), 0xFFEDF0F4.toInt(),
            0xFF1E293B.toInt(), 0xFF64748B.toInt(), 0xFF2563EB.toInt(), 0xFF3B82F6.toInt(), false),
        Tem("زیتون آرام", 0xFFF2F5EC.toInt(), 0xFFFAFCF5.toInt(), 0xFFE7EDDE.toInt(),
            0xFF2F3A26.toInt(), 0xFF7C8A66.toInt(), 0xFF65A30D.toInt(), 0xFF84CC16.toInt(), false),
        Tem("آسمان سحر", 0xFFE9F3FB.toInt(), 0xFFF8FBFE.toInt(), 0xFFDCEAF5.toInt(),
            0xFF173247.toInt(), 0xFF6C90A8.toInt(), 0xFF0284C7.toInt(), 0xFF06B6D4.toInt(), false),
        Tem("زغالی گرم", 0xFF171412.toInt(), 0xFF221E1A.toInt(), 0xFF1C1815.toInt(),
            0xFFF4EEE5.toInt(), 0xFFA79B8B.toInt(), 0xFFF97316.toInt(), 0xFFFB923C.toInt(), true)
    )

    fun bekhon(context: Context): Tem {
        val shomare = context.getSharedPreferences("pajoohesh", Context.MODE_PRIVATE)
            .getInt("tem", 0)
        return temHa[shomare.coerceIn(0, temHa.size - 1)]
    }

    fun zakhireKon(context: Context, shomare: Int) {
        context.getSharedPreferences("pajoohesh", Context.MODE_PRIVATE)
            .edit().putInt("tem", shomare.coerceIn(0, temHa.size - 1)).apply()
    }

    fun rangVaredKon(activity: Activity) {
        val t = bekhon(activity)
        activity.window.statusBarColor = if (t.khater) t.zamin else t.asli
        activity.window.navigationBarColor = t.zamin
    }
}

object Abzar {

    private val raghamHa = listOf("۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹")

    fun dp(context: Context, meghdar: Float): Int =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            meghdar,
            context.resources.displayMetrics
        ).toInt()

    fun tv(
        context: Context,
        matn: String,
        size: Float,
        rang: Int,
        bold: Boolean = false
    ): TextView {
        val t = TextView(context)
        t.text = matn
        t.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
        t.setTextColor(rang)
        t.typeface = if (bold) Fontha.vazirPorReng(context) else Fontha.vazir(context)
        return t
    }

    // آیکون وکتوری با رنگ دلخواه — برای منو و دکمه‌ها
    fun neshane(context: Context, nam: String, rang: Int, boorg: Int = 26): ImageView {
        val n = ImageView(context)
        val h = try {
            context.getDrawable(
                context.resources.getIdentifier(nam, "drawable", context.packageName)
            )
        } catch (_: Exception) {
            null
        }
        if (h != null) {
            n.setImageDrawable(h)
            n.setColorFilter(rang)
        }
        n.scaleType = ImageView.ScaleType.FIT_CENTER
        return n
    }

    fun kart(context: Context, rang: Int, shoa: Float = 18f): GradientDrawable {
        val shakl = GradientDrawable()
        shakl.setColor(rang)
        shakl.cornerRadius = dp(context, shoa).toFloat()
        return shakl
    }

    fun kartGradient(context: Context, rang1: Int, rang2: Int, shoa: Float = 20f): GradientDrawable {
        val shakl = GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            intArrayOf(rang1, rang2)
        )
        shakl.cornerRadius = dp(context, shoa).toFloat()
        return shakl
    }

    fun kartKhatei(context: Context, rangKhat: Int, rangPosht: Int = Color.TRANSPARENT, shoa: Float = 14f, toghe: Float = 1.2f): GradientDrawable {
        val shakl = GradientDrawable()
        shakl.setColor(rangPosht)
        shakl.setStroke(dp(context, toghe), rangKhat)
        shakl.cornerRadius = dp(context, shoa).toFloat()
        return shakl
    }

    fun dayereh(context: Context, rang: Int): GradientDrawable {
        val shakl = GradientDrawable()
        shakl.shape = GradientDrawable.OVAL
        shakl.setColor(rang)
        return shakl
    }

    fun lams(v: View) {
        val khoroji = TypedValue()
        v.context.theme.resolveAttribute(android.R.attr.selectableItemBackground, khoroji, true)
        try {
            v.foreground = v.context.getDrawable(khoroji.resourceId)
        } catch (_: Exception) {
        }
    }

    fun dokme(
        context: Context,
        matn: String,
        rangPosht: Int,
        rangMatn: Int,
        khateDor: Boolean = false
    ): TextView {
        val t = TextView(context)
        t.text = matn
        t.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        t.setTextColor(rangMatn)
        t.typeface = Fontha.vazirPorReng(context)
        t.gravity = Gravity.CENTER
        t.background = if (khateDor) kartKhatei(context, rangPosht) else kart(context, rangPosht)
        t.setPadding(dp(context, 16f), dp(context, 14f), dp(context, 16f), dp(context, 14f))
        lams(t)
        return t
    }

    // ---------- اعداد فارسی ----------
    fun adadFarsi(adad: Int): String =
        adad.toString().map { if (it == '-') '−' else raghamHa[it - '0'] }.joinToString("")

    fun adadFarsi(adad: Double): String {
        val gerd = if (adad == adad.toLong().toDouble()) {
            adad.toLong().toString()
        } else {
            String.format(java.util.Locale.US, "%.2f", adad)
        }
        val sb = StringBuilder()
        for (c in gerd) {
            when (c) {
                '-' -> sb.append('−')
                '.' -> sb.append('٫')
                else -> sb.append(raghamHa[c - '0'])
            }
        }
        return sb.toString()
    }

    // نوار باریک گرادیانی برای زیبایی سربرگ‌ها
    fun nobarZiba(context: Context, rang1: Int, rang2: Int, bolandi: Int = 4): View {
        val v = View(context)
        v.layoutParams = android.view.ViewGroup.LayoutParams(
            android.view.ViewGroup.LayoutParams.MATCH_PARENT,
            dp(context, bolandi.toFloat())
        )
        v.background = kartGradient(context, rang1, rang2, 0f)
        return v
    }

    // تیتر بخش با میلهٔ رنگی کنارش — به‌جای شکلک
    fun titrBakhsh(context: Context, matn: String, tem: Tem): LinearLayout {
        val saf = LinearLayout(context)
        saf.orientation = LinearLayout.HORIZONTAL
        saf.gravity = Gravity.CENTER_VERTICAL
        val mikh = View(context)
        mikh.background = kartGradient(context, tem.asli, tem.dovom, 3f)
        saf.addView(mikh, LinearLayout.LayoutParams(dp(context, 4.5f), dp(context, 19f)))
        val t = TextView(context)
        t.text = matn
        t.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16.5f)
        t.setTextColor(tem.matn)
        t.typeface = Fontha.vazirPorReng(context)
        val p = LinearLayout.LayoutParams(0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
        p.marginStart = dp(context, 9f)
        saf.addView(t, p)
        return saf
    }
}

/*
 * فونت‌ها — وزیرمتن برای متن و لاله‌زار برای عنوان‌ها؛ یک بار از assets لود و کش می‌شود
 */
object Fontha {

    private var vazir: Typeface? = null
    private var vazirPorReng: Typeface? = null
    private var vazirSiaah: Typeface? = null
    private var lalezar: Typeface? = null

    fun vazir(c: Context): Typeface {
        if (vazir == null) {
            vazir = Typeface.createFromAsset(c.assets, "fonts/Vazirmatn-Regular.ttf")
        }
        return vazir!!
    }

    fun vazirPorReng(c: Context): Typeface {
        if (vazirPorReng == null) {
            vazirPorReng = Typeface.createFromAsset(c.assets, "fonts/Vazirmatn-Bold.ttf")
        }
        return vazirPorReng!!
    }

    fun vazirSiaah(c: Context): Typeface {
        if (vazirSiaah == null) {
            vazirSiaah = Typeface.createFromAsset(c.assets, "fonts/Vazirmatn-Black.ttf")
        }
        return vazirSiaah!!
    }

    // فونت نمایشی عنوان‌ها — زیبا و درشت
    fun lalezar(c: Context): Typeface {
        if (lalezar == null) {
            lalezar = Typeface.createFromAsset(c.assets, "fonts/Lalezar-Regular.ttf")
        }
        return lalezar!!
    }
}
