package ir.emaroid.hesab

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RadialGradient
import android.graphics.Shader
import android.graphics.drawable.Drawable

/*
 * پس‌زمینهٔ «سنگ‌زغالی» — تیره، جدی و بی‌شلوغ
 * گرادیان گرافیت + شبکهٔ خیلی کم‌رنگ خطوط ابزار دقیق + یک هالهٔ نرم فیروزه‌ای
 * هیچ ستاره و نماد شناوری نیست — حواس کاربر روی محاسبه می‌ماند
 */
class NagshZamin(private val rangBala: Int, private val rangPaein: Int) : Drawable() {

    private val qalam = Paint(Paint.ANTI_ALIAS_FLAG)
    private val qalamKhat = Paint(Paint.ANTI_ALIAS_FLAG)
    private val qalamNur = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun draw(canvas: Canvas) {
        val b = bounds
        val w = b.width().toFloat()
        val h = b.height().toFloat()

        // گرادیان عمودی گرافیت
        qalam.shader = LinearGradient(
            0f, 0f, w * 0.20f, h,
            rangBala, rangPaein, Shader.TileMode.CLAMP
        )
        canvas.drawRect(0f, 0f, w, h, qalam)
        qalam.shader = null

        // شبکهٔ ابزار دقیق — خطوط عمودی و افقی خیلی کم‌رنگ
        qalamKhat.style = Paint.Style.STROKE
        qalamKhat.strokeWidth = 1f
        qalamKhat.color = 0x0DFFFFFF
        val gom = 56f
        var x = gom
        while (x < w) { canvas.drawLine(x, 0f, x, h, qalamKhat); x += gom }
        var y = gom
        while (y < h) { canvas.drawLine(0f, y, w, y, qalamKhat); y += gom }

        // یک هالهٔ نرم فیروزه‌ای بالا-راست — نفسِ ملایم، نه نئون
        nur(canvas, w * 0.92f, h * 0.06f, w * 0.60f, 0x1A2FA8C9)

        // خط تأکید ظریف زیر نوار بالا — امضای پالت
        qalamKhat.strokeWidth = 2f
        qalamKhat.color = 0x2E2FA8C9
        canvas.drawLine(0f, 2f, w, 2f, qalamKhat)
    }

    // هالهٔ نور نرم (گرادیان شعاعی محوشونده)
    private fun nur(canvas: Canvas, cx: Float, cy: Float, shoa: Float, rang: Int) {
        qalamNur.shader = RadialGradient(cx, cy, shoa,
            intArrayOf(rang, 0x00000000 or (rang and 0x00FFFFFF)),
            floatArrayOf(0f, 1f), Shader.TileMode.CLAMP)
        canvas.drawRect(cx - shoa, cy - shoa, cx + shoa, cy + shoa, qalamNur)
        qalamNur.shader = null
    }

    override fun setAlpha(alpha: Int) {}
    override fun setColorFilter(cf: ColorFilter?) {}
    @Deprecated("Deprecated in Java")
    override fun getOpacity(): Int = android.graphics.PixelFormat.TRANSLUCENT
}
