package ir.anjoman.amoozeshkotlin

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View

/*
 * پرچم‌های دست‌ساز EmaRoid — همه با کد کشیده می‌شوند، بدون عکس
 * ایران (فارسی) / ایرلند (English) / فلسطین (العربية)
 * طبق خواستهٔ سردبیر: به‌جای پرچم انگلیس «ایرلند» و به‌جای پرچم عربستان «فلسطین»
 */
object ParCham {

    const val IRAN = 1
    const val IRELAND = 2
    const val PALESTIN = 3

    fun neshan(c: Context, no: Int, bolandiDp: Float = 34f): View {
        return ParChamView(c, no, bolandiDp)
    }
}

class ParChamView(c: Context, private val no: Int, bolandiDp: Float) : View(c) {

    private val qalam = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bolandiPx = Abzar.dp(c, bolandiDp).toFloat()

    override fun onMeasure(w: Int, h: Int) {
        setMeasuredDimension((bolandiPx * 1.5f).toInt(), bolandiPx.toInt())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val w = width.toFloat()
        val h = height.toFloat()
        when (no) {
            ParCham.IRAN -> {
                // سه نوار افقی: سبز / سفید / قرمز + نشان سادهٔ وسط
                qalam.color = 0xFF239F40.toInt()
                canvas.drawRect(0f, 0f, w, h / 3f, qalam)
                qalam.color = Color.WHITE
                canvas.drawRect(0f, h / 3f, w, 2f * h / 3f, qalam)
                qalam.color = 0xFFDA0000.toInt()
                canvas.drawRect(0f, 2f * h / 3f, w, h, qalam)
                // نشان سادهٔ لاله‌ای وسط
                qalam.color = 0xFFDA0000.toInt()
                qalam.style = Paint.Style.STROKE
                qalam.strokeWidth = h / 18f
                canvas.drawCircle(w / 2f, h / 2f, h / 7f, qalam)
                qalam.style = Paint.Style.FILL
            }
            ParCham.IRELAND -> {
                // سه نوار عمودی: سبز / سفید / نارنجی
                qalam.color = 0xFF169B62.toInt()
                canvas.drawRect(0f, 0f, w / 3f, h, qalam)
                qalam.color = Color.WHITE
                canvas.drawRect(w / 3f, 0f, 2f * w / 3f, h, qalam)
                qalam.color = 0xFFFF883E.toInt()
                canvas.drawRect(2f * w / 3f, 0f, w, h, qalam)
            }
            ParCham.PALESTIN -> {
                // سه نوار افقی: سیاه / سفید / سبز + مثلث قرمز سمت میله
                qalam.color = Color.BLACK
                canvas.drawRect(0f, 0f, w, h / 3f, qalam)
                qalam.color = Color.WHITE
                canvas.drawRect(0f, h / 3f, w, 2f * h / 3f, qalam)
                qalam.color = 0xFF007A3D.toInt()
                canvas.drawRect(0f, 2f * h / 3f, w, h, qalam)
                // مثلث قرمز از سمت چپ (سمت میلهٔ پرچم)
                qalam.color = 0xFFCE1126.toInt()
                val p = android.graphics.Path()
                p.moveTo(0f, 0f)
                p.lineTo(w * 0.38f, h / 2f)
                p.lineTo(0f, h)
                p.close()
                canvas.drawPath(p, qalam)
            }
        }
        // قاب ظریف دور پرچم
        qalam.style = Paint.Style.STROKE
        qalam.color = 0x33FFFFFF
        qalam.strokeWidth = 2f
        canvas.drawRect(1f, 1f, w - 1f, h - 1f, qalam)
        qalam.style = Paint.Style.FILL
    }
}
