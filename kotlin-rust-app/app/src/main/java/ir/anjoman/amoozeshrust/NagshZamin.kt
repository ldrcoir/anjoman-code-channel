package ir.anjoman.amoozeshrust

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.graphics.drawable.Drawable

/*
 * پس‌زمینهٔ طرح‌دار EmaRoid
 * گرادیان عمقی + نشانه‌های کم‌رنگ دنیای کد ({ } ; < > //)
 * دیگه پس‌زمینه فقط یه رنگ ساده نیست؛ یه طرح جالب و زنده‌ست
 */
class NagshZamin(private val rangBala: Int, private val rangPaein: Int) : Drawable() {

    private val qalam = Paint(Paint.ANTI_ALIAS_FLAG)
    private val qalamNeveshteh = Paint(Paint.ANTI_ALIAS_FLAG)

    // نشانه‌هایی که در پس‌زمینه پراکنده می‌شوند
    private val alamatHa = listOf("{ }", "fun", "val", ";", "</>", "//", "( )", "→", "if", "==", "#", "&&")

    override fun draw(canvas: Canvas) {
        val b = bounds
        // گرادیان عمودی
        val grad = LinearGradient(
            0f, b.top.toFloat(), 0f, b.bottom.toFloat(),
            rangBala, rangPaein, Shader.TileMode.CLAMP
        )
        qalam.shader = grad
        canvas.drawRect(0f, 0f, b.width().toFloat(), b.height().toFloat(), qalam)
        qalam.shader = null

        // حلقه‌های بزرگ کم‌رنگ در گوشه‌ها (عمق می‌دهد)
        qalam.style = Paint.Style.STROKE
        qalam.color = 0x0AFFFFFF
        qalam.strokeWidth = 40f
        canvas.drawCircle(b.width() * 0.9f, b.height() * 0.12f, 160f, qalam)
        canvas.drawCircle(b.width() * 0.9f, b.height() * 0.12f, 240f, qalam)
        canvas.drawCircle(b.width() * 0.08f, b.height() * 0.85f, 130f, qalam)
        qalam.style = Paint.Style.FILL

        // پراکنده‌کردن نشانه‌های کد با شفافیت کم
        qalamNeveshteh.textSize = 34f
        qalamNeveshteh.color = 0x14FFFFFF
        var i = 0
        var y = 90f
        while (y < b.height()) {
            var x = if (i % 2 == 0) 30f else 90f
            while (x < b.width()) {
                val alamat = alamatHa[i % alamatHa.size]
                canvas.save()
                canvas.rotate((i % 3 - 1) * 8f, x, y)
                canvas.drawText(alamat, x, y, qalamNeveshteh)
                canvas.restore()
                x += 210f
                i++
            }
            y += 170f
        }
    }

    override fun setAlpha(alpha: Int) {}
    override fun setColorFilter(cf: ColorFilter?) {}
    @Deprecated("Deprecated in Java")
    override fun getOpacity(): Int = android.graphics.PixelFormat.TRANSLUCENT
}
