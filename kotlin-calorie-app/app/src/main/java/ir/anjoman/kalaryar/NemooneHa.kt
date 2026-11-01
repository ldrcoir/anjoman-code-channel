package ir.anjoman.kalaryar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.SweepGradient
import android.view.View

/*
 * نماهای سفارشی دست‌ساز — بدون هیچ کتابخانهٔ بیرونی
 * حلقهٔ پیشرفت، نمودار میله‌ای هفتگی و نمودار خطی وزن
 */

// حلقهٔ گرد پیشرفت با گرادیان و متن وسط
class HalgheView(context: Context) : View(context) {

    var pishraft: Float = 0f          // ۰ تا ۱
        set(value) {
            field = value.coerceIn(0f, 1f)
            invalidate()
        }
    var rangAval = Abzar.RANG_SABZ
    var rangDovom = Abzar.RANG_SABZKAM
    var matnVasat = ""
    var matnZir = ""
    var andazeKhat = 22f              // ضخامت حلقه به dp

    private val ghalem = Paint(Paint.ANTI_ALIAS_FLAG)
    private val ghalemMatn = Paint(Paint.ANTI_ALIAS_FLAG)
    private val ghalemZir = Paint(Paint.ANTI_ALIAS_FLAG)
    private val kadr = RectF()

    override fun onDraw(canvas: Canvas) {
        val vasatX = width / 2f
        val vasatY = height / 2f
        val shoaa = (minOf(width, height) / 2f) - Abzar.dp(context, andazeKhat)
        val khat = Abzar.dp(context, andazeKhat).toFloat()

        // زمینهٔ حلقه
        ghalem.style = Paint.Style.STROKE
        ghalem.strokeWidth = khat
        ghalem.strokeCap = Paint.Cap.ROUND
        ghalem.shader = null
        ghalem.color = Abzar.RANG_KHAKISTARI
        kadr.set(vasatX - shoaa, vasatY - shoaa, vasatX + shoaa, vasatY + shoaa)
        canvas.drawArc(kadr, 0f, 360f, false, ghalem)

        // حلقهٔ پیشرفت با گرادیان
        if (pishraft > 0.005f) {
            val gradient = SweepGradient(vasatX, vasatY, rangAval, rangDovom)
            ghalem.shader = gradient
            // از ساعت ۱۲ شروع می‌شود
            canvas.drawArc(kadr, -90f, 360f * pishraft, false, ghalem)
            ghalem.shader = null
        }

        // متن وسط
        ghalemMatn.color = Abzar.RANG_MATN
        ghalemMatn.textAlign = Paint.Align.CENTER
        ghalemMatn.typeface = FontBox.vazirBlack(context)
        ghalemMatn.textSize = Abzar.dp(context, 30f).toFloat()
        val yVasat = vasatY - (if (matnZir.isEmpty()) 0 else Abzar.dp(context, 4f))
        canvas.drawText(matnVasat, vasatX, yVasat + ghalemMatn.textSize / 3f, ghalemMatn)

        if (matnZir.isNotEmpty()) {
            ghalemZir.color = Abzar.RANG_KAMRANG
            ghalemZir.textAlign = Paint.Align.CENTER
            ghalemZir.typeface = FontBox.vazir(context)
            ghalemZir.textSize = Abzar.dp(context, 13f).toFloat()
            canvas.drawText(matnZir, vasatX, yVasat + Abzar.dp(context, 26f), ghalemZir)
        }
    }
}

// حلقهٔ کوچک برای آب
class HalgheKoochak(context: Context) : View(context) {

    var pishraft: Float = 0f
        set(value) {
            field = value.coerceIn(0f, 1f)
            invalidate()
        }
    var rang = Abzar.RANG_ABI

    private val ghalem = Paint(Paint.ANTI_ALIAS_FLAG)
    private val kadr = RectF()

    override fun onDraw(canvas: Canvas) {
        val vasatX = width / 2f
        val vasatY = height / 2f
        val shoaa = (minOf(width, height) / 2f) - Abzar.dp(context, 8f)
        val khat = Abzar.dp(context, 8f).toFloat()

        ghalem.style = Paint.Style.STROKE
        ghalem.strokeWidth = khat
        ghalem.strokeCap = Paint.Cap.ROUND
        ghalem.color = Abzar.RANG_KHAKISTARI
        kadr.set(vasatX - shoaa, vasatY - shoaa, vasatX + shoaa, vasatY + shoaa)
        canvas.drawArc(kadr, 0f, 360f, false, ghalem)

        if (pishraft > 0.005f) {
            ghalem.color = rang
            canvas.drawArc(kadr, -90f, 360f * pishraft, false, ghalem)
        }
    }
}

// نمودار میله‌ای هفتگی با خط هدف
class SotoonChart(context: Context) : View(context) {

    var meghdarHa: List<Int> = emptyList()   // ۷ مقدار؛ ۰ یعنی ثبت نشده
    var hadaf = 2000

    private val ghalemSotoon = Paint(Paint.ANTI_ALIAS_FLAG)
    private val ghalemKhat = Paint(Paint.ANTI_ALIAS_FLAG)
    private val ghalemMatn = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas: Canvas) {
        if (meghdarHa.isEmpty()) return
        val pad = Abzar.dp(context, 10f).toFloat()
        val balandiMatn = Abzar.dp(context, 16f).toFloat()
        val vaziatBalandi = height - pad - balandiMatn * 2f
        val maxMeghdar = (maxOf(meghdarHa.maxOrNull() ?: 1, hadaf) * 1.15f).coerceAtLeast(1f)

        // خط هدف
        val yHadaf = vaziatBalandi - (hadaf / maxMeghdar) * vaziatBalandi
        ghalemKhat.style = Paint.Style.STROKE
        ghalemKhat.strokeWidth = Abzar.dp(context, 1.5f).toFloat()
        ghalemKhat.color = Abzar.RANG_TALAEI
        ghalemKhat.pathEffect = android.graphics.DashPathEffect(floatArrayOf(12f, 10f), 0f)
        canvas.drawLine(pad, yHadaf, width - pad, yHadaf, ghalemKhat)
        ghalemKhat.pathEffect = null

        val fazele = Abzar.dp(context, 10f).toFloat()
        val arzSotoon = (width - pad * 2 - fazele * (meghdarHa.size - 1)) / meghdarHa.size

        for ((i, meghdar) in meghdarHa.withIndex()) {
            val x = pad + i * (arzSotoon + fazele)
            // رنگ میله: سبز نزدیک هدف، طلایی کمی بیشتر، قرمز خیلی بیشتر
            ghalemSotoon.color = when {
                meghdar == 0 -> Abzar.RANG_KHAKISTARI
                meghdar <= hadaf * 1.05 -> Abzar.RANG_SABZ
                meghdar <= hadaf * 1.2 -> Abzar.RANG_TALAEI
                else -> Abzar.RANG_GHERMEZ
            }
            val balandi = if (meghdar == 0) Abzar.dp(context, 6f).toFloat()
            else (meghdar / maxMeghdar) * vaziatBalandi
            val rect = RectF(x, vaziatBalandi - balandi, x + arzSotoon, vaziatBalandi)
            canvas.drawRoundRect(rect, Abzar.dp(context, 8f).toFloat(), Abzar.dp(context, 8f).toFloat(), ghalemSotoon)

            // برچسب روز هفتهٔ واقعی زیر هر میله
            ghalemMatn.color = Abzar.RANG_KAMRANG
            ghalemMatn.textAlign = Paint.Align.CENTER
            ghalemMatn.typeface = FontBox.vazir(context)
            ghalemMatn.textSize = Abzar.dp(context, 11f).toFloat()
            val rozHa = listOf("ی", "د", "س", "چ", "پ", "ج", "ش") // یک‌شنبه تا شنبه
            val cal = java.util.Calendar.getInstance()
            cal.add(java.util.Calendar.DAY_OF_MONTH, -(meghdarHa.size - 1 - i))
            val rooz = rozHa[cal.get(java.util.Calendar.DAY_OF_WEEK) - 1]
            canvas.drawText(rooz, x + arzSotoon / 2f, height - pad, ghalemMatn)
        }
    }
}

// نمودار خطی وزن
class KhatChart(context: Context) : View(context) {

    var noghteHa: List<Double> = emptyList()

    private val ghalemKhat = Paint(Paint.ANTI_ALIAS_FLAG)
    private val ghalemNoghte = Paint(Paint.ANTI_ALIAS_FLAG)
    private val ghalemMatn = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas: Canvas) {
        if (noghteHa.size < 2) return
        val pad = Abzar.dp(context, 18f).toFloat()
        val minV = noghteHa.minOrNull() ?: return
        val maxV = noghteHa.maxOrNull() ?: return
        val fazele = (maxV - minV).coerceAtLeast(1.0)
        val balandiMobtaz = height - pad * 2

        ghalemKhat.style = Paint.Style.STROKE
        ghalemKhat.strokeWidth = Abzar.dp(context, 3f).toFloat()
        ghalemKhat.strokeCap = Paint.Cap.ROUND
        ghalemKhat.color = Abzar.RANG_SABZ

        var xGhabli = 0f
        var yGhabli = 0f
        for ((i, v) in noghteHa.withIndex()) {
            val x = pad + i * (width - pad * 2) / (noghteHa.size - 1)
            val y = (pad + (1 - ((v - minV) / fazele)) * balandiMobtaz).toFloat()
            if (i > 0) {
                canvas.drawLine(xGhabli, yGhabli, x, y, ghalemKhat)
            }
            xGhabli = x
            yGhabli = y
        }

        // نقاط
        ghalemNoghte.color = Abzar.RANG_MATN
        for ((i, v) in noghteHa.withIndex()) {
            val x = pad + i * (width - pad * 2) / (noghteHa.size - 1)
            val y = (pad + (1 - ((v - minV) / fazele)) * balandiMobtaz).toFloat()
            ghalemNoghte.color = Abzar.RANG_SABZ
            canvas.drawCircle(x, y, Abzar.dp(context, 5f).toFloat(), ghalemNoghte)
            ghalemNoghte.color = Abzar.RANG_MATN
            canvas.drawCircle(x, y, Abzar.dp(context, 2.5f).toFloat(), ghalemNoghte)

            // عدد آخر را نشان بده
            if (i == noghteHa.size - 1) {
                ghalemMatn.color = Abzar.RANG_MATN
                ghalemMatn.textAlign = Paint.Align.CENTER
                ghalemMatn.typeface = FontBox.vazirBold(context)
                ghalemMatn.textSize = Abzar.dp(context, 12f).toFloat()
                canvas.drawText(Abzar.adadFarsi(v), x, y - Abzar.dp(context, 12f).toFloat(), ghalemMatn)
            }
        }
    }
}
