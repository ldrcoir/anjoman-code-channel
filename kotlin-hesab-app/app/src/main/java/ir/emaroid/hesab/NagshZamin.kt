package ir.emaroid.hesab

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RadialGradient
import android.graphics.Shader
import android.graphics.drawable.Drawable

/*
 * پس‌زمینهٔ «خیره» ✨ — کهکشان بنفش با شفق
 * گرادیان مورب سه‌رنگ + هاله‌های نور صورتی/فیروزه‌ای + ستاره‌های ریز
 * + نمادهای ریاضی کم‌رنگ پراکنده (π √ ∑ ∞)
 */
class NagshZamin(private val rangBala: Int, private val rangPaein: Int) : Drawable() {

    private val qalam = Paint(Paint.ANTI_ALIAS_FLAG)
    private val qalamNeveshteh = Paint(Paint.ANTI_ALIAS_FLAG)
    private val qalamNur = Paint(Paint.ANTI_ALIAS_FLAG)

    // نمادهای ریاضی که در پس‌زمینه شناورند
    private val alamatHa = listOf("π", "√", "∑", "∞", "ƒ", "≈", "≠", "∆", "∫", "%", "±", "Ω")

    override fun draw(canvas: Canvas) {
        val b = bounds
        val w = b.width().toFloat()
        val h = b.height().toFloat()

        // گرادیان مورب کهکشانی
        qalam.shader = LinearGradient(
            0f, 0f, w * 0.35f, h,
            rangBala, rangPaein, Shader.TileMode.CLAMP
        )
        canvas.drawRect(0f, 0f, w, h, qalam)
        qalam.shader = null

        // هالهٔ صورتی بالا-راست
        nur(canvas, w * 0.88f, h * 0.10f, w * 0.55f, 0x2EFF4D9E)
        // هالهٔ فیروزه‌ای پایین-چپ
        nur(canvas, w * 0.06f, h * 0.92f, w * 0.50f, 0x2A22D3EE)
        // هالهٔ بنفش وسط
        nur(canvas, w * 0.75f, h * 0.55f, w * 0.42f, 0x228B5CF6)

        // ستاره‌های ریز ثابت (چیدمان قطعی تا هر فریم یکسان بماند)
        qalam.style = Paint.Style.FILL
        qalam.color = 0x66FFFFFF
        val star = floatArrayOf(0.08f, 0.14f, 0.22f, 0.31f, 0.38f, 0.47f, 0.56f, 0.63f, 0.72f, 0.81f, 0.9f, 0.95f)
        var i = 0
        for (fy in star) {
            val fx = if (i % 2 == 0) (i * 0.137f) % 0.9f + 0.05f else (i * 0.293f) % 0.85f + 0.1f
            val shoa = if (i % 3 == 0) 4.5f else 2.5f
            canvas.drawCircle(w * fx, h * fy, shoa, qalam)
            i++
        }

        // نمادهای ریاضی شناور
        qalamNeveshteh.textSize = 40f
        qalamNeveshteh.color = 0x16FFFFFF
        var j = 0
        var y = 100f
        while (y < h) {
            var x = if (j % 2 == 0) 36f else 110f
            while (x < w) {
                val alamat = alamatHa[j % alamatHa.size]
                canvas.save()
                canvas.rotate((j % 3 - 1) * 9f, x, y)
                canvas.drawText(alamat, x, y, qalamNeveshteh)
                canvas.restore()
                x += 230f
                j++
            }
            y += 180f
        }
    }

    // هالهٔ نور نرم (گرادیان شعاعی محو شونده)
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
