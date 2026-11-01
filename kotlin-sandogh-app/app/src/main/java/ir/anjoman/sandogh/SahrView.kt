package ir.anjoman.sandogh

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.view.View

/*
 * نمودار دایره‌ای (پای) سهم اعضا از صندوق — دست‌ساز با Canvas
 * دهانهٔ وسط برای خوانایی؛ لیست راهنما بیرونِ نمودار ساخته می‌شود
 */
class SahrView(context: Context) : View(context) {

    private var dadeha: List<Pair<Double, Int>> = emptyList()  // (سهم٪، رنگ)

    fun dadegan(d: List<Pair<Double, Int>>) {
        dadeha = d
        invalidate()
    }

    private val ghalem = Paint(Paint.ANTI_ALIAS_FLAG)
    private val ghalemKhat = Paint(Paint.ANTI_ALIAS_FLAG)
    private val kadr = RectF()

    override fun onDraw(canvas: Canvas) {
        val vasatX = width / 2f
        val vasatY = height / 2f
        val shoaa = minOf(width, height) / 2f - Abzar.dp(context, 6f)
        val khat = Abzar.dp(context, 42f).toFloat()

        kadr.set(
            vasatX - shoaa + khat / 2, vasatY - shoaa + khat / 2,
            vasatX + shoaa - khat / 2, vasatY + shoaa - khat / 2
        )

        // زمینهٔ حلقه
        ghalem.style = Paint.Style.STROKE
        ghalem.strokeWidth = khat
        ghalem.color = TemEnekhab.bekhon(context).kartTir
        canvas.drawArc(kadr, 0f, 360f, false, ghalem)

        // برش‌ها
        var shoru = -90f
        val majmoo = dadeha.sumOf { it.first }
        if (majmoo <= 0) return
        for ((sahm, rang) in dadeha) {
            val zaviye = 360f * (sahm / majmoo).toFloat()
            if (zaviye <= 0.1f) continue
            ghalem.color = rang
            canvas.drawArc(kadr, shoru, zaviye - 1.2f, false, ghalem)
            shoru += zaviye
        }

        // متن وسط
        ghalemKhat.style = Paint.Style.FILL
        ghalemKhat.color = TemEnekhab.bekhon(context).matn
        ghalemKhat.textAlign = Paint.Align.CENTER
        ghalemKhat.typeface = FontBox.vazirBlack(context)
        ghalemKhat.textSize = Abzar.dp(context, 16f).toFloat()
        canvas.drawText("سهم اعضا", vasatX, vasatY + ghalemKhat.textSize / 3f, ghalemKhat)
    }
}
