package ir.anjoman.sandogh

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.TextView

/*
 * جعبه‌ابزار کتابچه + پالت تم‌ها
 * ۴ تم: شب فیروزه‌ای، کلاسیک طلایی (کاغذ کرم)، سبز بانکی، بنفش کهکشانی
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
        Tem("شب فیروزه‌ای", 0xFF0B1220.toInt(), 0xFF141E31.toInt(), 0xFF0F1728.toInt(),
            0xFFE6F1FF.toInt(), 0xFF7E93B5.toInt(), 0xFF2DD4BF.toInt(), 0xFF0EA5E9.toInt(), true),
        Tem("کلاسیک طلایی", 0xFFF5EEDC.toInt(), 0xFFFCF7EA.toInt(), 0xFFF1E7CE.toInt(),
            0xFF3B2F1A.toInt(), 0xFF8A7A57.toInt(), 0xFFB8860B.toInt(), 0xFFD4A017.toInt(), false),
        Tem("سبز بانکی", 0xFF0C1512.toInt(), 0xFF152420.toInt(), 0xFF0F1B17.toInt(),
            0xFFEAF7F0.toInt(), 0xFF8FAF9F.toInt(), 0xFF22C55E.toInt(), 0xFF16A34A.toInt(), true),
        Tem("بنفش کهکشانی", 0xFF120E1F.toInt(), 0xFF1C1631.toInt(), 0xFF151026.toInt(),
            0xFFF0EAFF.toInt(), 0xFF9B8FC0.toInt(), 0xFFA78BFA.toInt(), 0xFF8B5CF6.toInt(), true)
    )

    fun bekhon(context: Context): Tem {
        val shomare = context.getSharedPreferences("sandogh", Context.MODE_PRIVATE)
            .getInt("tem", 0)
        return temHa[shomare.coerceIn(0, temHa.size - 1)]
    }

    fun zakhireKon(context: Context, shomare: Int) {
        context.getSharedPreferences("sandogh", Context.MODE_PRIVATE)
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
        t.typeface = if (bold) FontBox.vazirBold(context) else FontBox.vazir(context)
        return t
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

    fun kartKhatei(context: Context, rangKhat: Int, rangPosht: Int = Color.TRANSPARENT, shoa: Float = 14f): GradientDrawable {
        val shakl = GradientDrawable()
        shakl.setColor(rangPosht)
        shakl.setStroke(dp(context, 1.2f), rangKhat)
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
        t.typeface = FontBox.vazirBold(context)
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

    // عدد بزرگ با جداکنندهٔ هزارگان: 88203200 → ۸۸,۲۰۳,۲۰۰
    fun adadJoda(adad: Long): String {
        val salim = if (adad < 0) adad.toString().substring(1) else adad.toString()
        val sb = StringBuilder()
        var shomar = 0
        for (i in salim.length - 1 downTo 0) {
            sb.append(raghamHa[salim[i] - '0'])
            shomar++
            if (shomar % 3 == 0 && i > 0) sb.append(',')
        }
        val natij = sb.reverse().toString()
        return if (adad < 0) "−$natij" else natij
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
}
