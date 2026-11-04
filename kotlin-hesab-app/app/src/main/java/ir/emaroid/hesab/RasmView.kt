package ir.emaroid.hesab

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View

/*
 * نمودارکش EmaRoid — رسم y = f(x) با جابه‌جایی و بزرگ‌نمایی
 * محورها، شبکه، برچسب اعداد و منحنی نارنجی زیبا
 * کشیدن انگشت = جابه‌جایی، دکمه‌های بیرون = زوم
 */
class RasmView(c: Context) : View(c) {

    var tabe: ((Double) -> Double)? = null
    var kaf = 0.0        // مرکز افقی در مختصات ریاضی
    var bala = 0.0       // مرکز عمودی
    var maghyas = 40.0   // پیکسل به‌ازای یک واحد

    private val qalamShabake = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0x2200FF88; strokeWidth = 1f
    }
    private val qalamMohor = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0x5500FF88; strokeWidth = 1.6f
    }
    private val qalamManhani = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE; strokeWidth = 1.6f
    }
    private val qalamKhan = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFFB2F3C2.toInt(); strokeWidth = 2.4f; textSize = 22f
    }
    private val qalamManhaniKhamosh = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE; strokeWidth = 3.2f
    }
    private val qalamManhani2 = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFFFFA94D.toInt(); strokeWidth = 3.4f; style = Paint.Style.STROKE
    }

    private var angoshtShoroo: Pair<Float, Float>? = null
    private var kafShoroo = 0.0
    private var balaShoroo = 0.0

    override fun onTouchEvent(e: MotionEvent): Boolean {
        when (e.action) {
            MotionEvent.ACTION_DOWN -> {
                angoshtShoroo = Pair(e.x, e.y)
                kafShoroo = kaf; balaShoroo = bala
            }
            MotionEvent.ACTION_MOVE -> {
                val (sx, sy) = angoshtShoroo ?: return false
                kaf = kafShoroo - (e.x - sx) / maghyas
                bala = balaShoroo + (e.y - sy) / maghyas
                invalidate()
            }
        }
        return true
    }

    fun zoom(darBolandi: Boolean) {
        val d = if (darBolandi) 1.25 else 0.8
        maghyas = (maghyas * d).coerceIn(4.0, 400.0)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val w = width.toFloat()
        val h = height.toFloat()
        val ox = (w / 2 - kaf * maghyas).toFloat()
        val oy = (h / 2 + bala * maghyas).toFloat()

        // شبکه: هر گام بر اساس مقیاس (۱،۲،۵×۱۰^k)
        val gham = ghamMunasib(maghyas)
        var x = Math.floor((0 - ox) / maghyas / gham) * gham
        while ((ox + x * maghyas) < w) {
            val px = (ox + x * maghyas).toFloat()
            canvas.drawLine(px, 0f, px, h, if (Math.abs(x) < 1e-9) qalamMohor else qalamShabake)
            x += gham
        }
        var y = Math.floor((oy - h) / maghyas / gham) * gham
        while ((oy - y * maghyas) > 0) {
            val py = (oy - y * maghyas).toFloat()
            canvas.drawLine(0f, py, w, py, if (Math.abs(y) < 1e-9) qalamMohor else qalamShabake)
            y += gham
        }

        // برچسب‌های محور
        x = Math.floor((0 - ox) / maghyas / gham) * gham
        while ((ox + x * maghyas) < w) {
            val px = (ox + x * maghyas).toFloat()
            if (Math.abs(x) > 1e-9 && px > 8 && px < w - 8) {
                canvas.drawText(kootah(x), px + 3f, (oy + 20f).coerceIn(20f, h - 4f), qalamKhan)
            }
            x += gham
        }
        y = Math.floor((oy - h) / maghyas / gham) * gham
        while ((oy - y * maghyas) > 0) {
            val py = (oy - y * maghyas).toFloat()
            if (Math.abs(y) > 1e-9 && py > 14 && py < h - 4) {
                canvas.drawText(kootah(y), (ox + 6f).coerceIn(2f, w - 40f), py - 5f, qalamKhan)
            }
            y += gham
        }

        // منحنی
        val t = tabe ?: return
        val manhani = android.graphics.Path()
        var shorooShod = false
        var pxGh = 0f
        var pyGh = 0f
        var xMath = (0 - ox) / maghyas
        val ghamFaz = 1.0 / maghyas  // هر پیکسل
        while (pxGh < w) {
            val yy = t(xMath)
            val py = (oy - yy * maghyas).toFloat()
            val px = pxGh.toFloat()
            val salim = java.lang.Double.isFinite(yy) && py > -2000 && py < h + 2000
            if (salim) {
                if (!shorooShod) { manhani.moveTo(px, py); shorooShod = true }
                else {
                    // پرش‌های بزرگ (مثل tan در ۹۰ درجه) را قطع کن
                    if (Math.abs(py - pyGh) > h * 1.5) { manhani.moveTo(px, py) }
                    else manhani.lineTo(px, py)
                }
                pyGh = py
            } else {
                shorooShod = false
            }
            pxGh += 1
            xMath += ghamFaz
        }
        canvas.drawPath(manhani, qalamManhani2)
    }

    private fun ghamMunasib(maghyas: Double): Double {
        val kh = Math.pow(10.0, Math.floor(Math.log10(50.0 / maghyas)))
        val n = (50.0 / maghyas / kh)
        val zarib = when {
            n < 1.5 -> 1.0
            n < 3.5 -> 2.0
            n < 7.5 -> 5.0
            else -> 10.0
        }
        return kh * zarib
    }

    private fun kootah(v: Double): String {
        val s = if (Math.abs(v - v.toLong()) < 1e-9) v.toLong().toString()
        else String.format(java.util.Locale.US, "%.1f", v)
        return s
    }
}
