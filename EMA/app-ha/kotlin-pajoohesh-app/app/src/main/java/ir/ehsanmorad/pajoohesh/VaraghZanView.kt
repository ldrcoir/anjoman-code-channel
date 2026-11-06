package ir.ehsanmorad.pajoohesh

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Shader
import android.media.AudioAttributes
import android.media.SoundPool
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

/*
 * ورق‌زن — موتور ورق‌خوردن طبیعی کتاب
 * مدل فیزیکی کاغذ با یک «برگه» و یک «زیر»:
 *   • برگرداندن به جلو: برگه = صفحهٔ فعلی است که از لبهٔ چپ بلند می‌شود؛
 *     زیرِ آن صفحهٔ بعد کم‌کم پیدا می‌شود
 *   • برگرداندن به عقب: برگه = صفحهٔ مقصد است که از عطف باز می‌شود و
 *     روی صفحهٔ فعلی می‌افتد (دقیقاً زمانِ معکوسِ حرکت جلو)
 *   • بخش تختِ برگه: محتوای خود برگه کنار عطف
 *   • بخش تاشده: پشتِ کاغذ — کاغذ ساده با سایه؛ هیچ متنی آینه‌ای نمی‌شود
 *   • استوانهٔ خم در خط تاشدن + سایه + برجستگی ملایم = حس کاغذ واقعی
 * کشیدن انگشت به راست = بعدی، به چپ = قبلی (کتاب راست‌چین)
 * لمس لبهٔ چپ = بعدی، لبهٔ راست = قبلی، وسط = خبر به اکتیویتی (نمایش/پنهان نوارها)
 * صدا: صدای ورق‌خوردن هنگام شروع هر برگرداندن (با کلید بی‌صدا)
 */
class VaraghZanView(
    context: Context,
    private val bargozar: (shomare: Int) -> Bitmap?
) : View(context) {

    var tedadSafhe = 216
    var safheFaeli = 0
        private set
    var rangZamin: Int = Color.WHITE
    var filterShab: android.graphics.ColorFilter? = null
    var vaghteTaghir: ((shomare: Int) -> Unit)? = null   // تغییر صفحه (برای سربرگ/نوار پرش/ذخیره)
    var vaghteLamsVasat: (() -> Unit)? = null            // لمس وسط صفحه

    // حالت‌ها: ۰ آرام / ۱ کشیدن / ۲ انیمیشن
    private var halat = 0
    private var jahat = 1          // ۱ بعدی / −۱ قبلی
    private var pishraft = 0f      // ۰ تا ۱
    private var tasvirRu: Bitmap? = null    // صفحهٔ فعلی
    private var tasvirPust: Bitmap? = null  // صفحهٔ مقصد (زیرِ برگه)
    private var xAval = 0f
    private var yAval = 0f
    private var zamAval = 0L
    private var animesh: ValueAnimator? = null

    // قلم‌ها — boresh برای صفحه‌ها، boreshKaghaz برای پشتِ کاغذ (هر دو فیلتر شب می‌گیرند)
    private val boresh = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
    private val boreshRang = Paint(Paint.ANTI_ALIAS_FLAG)
    private val boreshKaghaz = Paint(Paint.ANTI_ALIAS_FLAG)

    fun safheRavanKon(shomare: Int) {
        // گردش ۶۷ — رفع ایراد: لغو انیمیشن موقع پرش نباید شنوندهٔ پایانش اجرا شود
        // (قبلاً صفجه یکی جلو/عقب می‌پرید و «آخرین صفحه» اشتباهی ذخیره می‌شد)
        animesh?.let { an ->
            an.removeAllUpdateListeners()
            an.removeAllListeners()
            an.cancel()
        }
        animesh = null
        halat = 0
        safheFaeli = shomare.coerceIn(0, tedadSafhe - 1)
        tasvirRu = bargozar(safheFaeli)
        tasvirPust = null
        pishraft = 0f
        invalidate()
    }

    // ---------- شروع برگرداندن ----------
    private fun shoroKeshidan(j: Int): Boolean {
        if (halat != 0) return false
        val hadaf = safheFaeli + j
        if (hadaf < 0 || hadaf > tedadSafhe - 1) return false
        val b = bargozar(hadaf) ?: return false
        jahat = j
        pishraft = 0f
        tasvirPust = b
        halat = 1
        Seda.begozaron(context)
        invalidate()
        return true
    }

    private fun harekatKeshidan(dX: Float) {
        val pahna = pahnayeSafhe().coerceAtLeast(1f)
        pishraft = (abs(dX) / (pahna * 0.62f)).coerceIn(0f, 1f)
        invalidate()
    }

    private fun tamamKeshidan(bePayan: Boolean) {
        if (halat != 1) return
        halat = 2
        val az = pishraft
        val ta = if (bePayan) 1f else 0f
        val mandeh = if (bePayan) 1f - az else az
        val an = ValueAnimator.ofFloat(az, ta)
        an.duration = if (bePayan) (300L + 640L * mandeh).toLong() else 400L
        an.interpolator = android.view.animation.DecelerateInterpolator(1.6f)
        an.addUpdateListener { a ->
            pishraft = a.animatedValue as Float
            invalidate()
        }
        animesh = an
        an.addListener(object : android.animation.AnimatorListenerAdapter() {
            override fun onAnimationEnd(a: android.animation.Animator) {
                if (bePayan) {
                    safheFaeli = (safheFaeli + jahat).coerceIn(0, tedadSafhe - 1)
                    tasvirRu = bargozar(safheFaeli)
                    vaghteTaghir?.invoke(safheFaeli)
                }
                tasvirPust = null
                pishraft = 0f
                halat = 0
                invalidate()
            }
        })
        an.start()
    }

    // برگرداندن خودکار کامل با زمان طبیعی (مثل ورق زدن دستی)
    fun badiNarm() {
        if (halat != 0) return
        if (!shoroKeshidan(1)) return
        halat = 2
        val an = ValueAnimator.ofFloat(0f, 1f)
        an.duration = 950L
        an.interpolator = android.view.animation.AccelerateDecelerateInterpolator()
        an.addUpdateListener { a ->
            pishraft = a.animatedValue as Float
            invalidate()
        }
        animesh = an
        an.addListener(object : android.animation.AnimatorListenerAdapter() {
            override fun onAnimationEnd(a: android.animation.Animator) {
                safheFaeli = (safheFaeli + jahat).coerceIn(0, tedadSafhe - 1)
                tasvirRu = bargozar(safheFaeli)
                vaghteTaghir?.invoke(safheFaeli)
                tasvirPust = null
                pishraft = 0f
                halat = 0
                invalidate()
            }
        })
        an.start()
    }

    fun ghabliNarm() {
        if (halat != 0) return
        if (!shoroKeshidan(-1)) return
        halat = 2
        val an = ValueAnimator.ofFloat(0f, 1f)
        an.duration = 950L
        an.interpolator = android.view.animation.AccelerateDecelerateInterpolator()
        an.addUpdateListener { a ->
            pishraft = a.animatedValue as Float
            invalidate()
        }
        animesh = an
        an.addListener(object : android.animation.AnimatorListenerAdapter() {
            override fun onAnimationEnd(a: android.animation.Animator) {
                safheFaeli = (safheFaeli + jahat).coerceIn(0, tedadSafhe - 1)
                tasvirRu = bargozar(safheFaeli)
                vaghteTaghir?.invoke(safheFaeli)
                tasvirPust = null
                pishraft = 0f
                halat = 0
                invalidate()
            }
        })
        an.start()
    }

    // ---------- لمس ----------
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        when (ev.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                xAval = ev.x
                yAval = ev.y
                zamAval = System.currentTimeMillis()
            }
            MotionEvent.ACTION_MOVE -> {
                if (halat == 0) {
                    val dX = ev.x - xAval
                    val dY = ev.y - yAval
                    if (abs(dX) > 30f && abs(dX) > abs(dY) * 1.2f) {
                        val j = if (dX > 0) 1 else -1
                        shoroKeshidan(j)
                    }
                } else if (halat == 1) {
                    harekatKeshidan(ev.x - xAval)
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (halat == 1) {
                    val dX = abs(ev.x - xAval)
                    val dt = System.currentTimeMillis() - zamAval
                    // پرتاب سریع انگشت = کامل، وگرنه بر اساس پیشرفت
                    val porTaz = dX > 90f && dt < 280L
                    tamamKeshidan(pishraft > 0.30f || porTaz)
                } else if (halat == 0 && ev.actionMasked == MotionEvent.ACTION_UP) {
                    val dX = abs(ev.x - xAval)
                    val dY = abs(ev.y - yAval)
                    val dt = System.currentTimeMillis() - zamAval
                    if (dX < 26f && dY < 26f && dt < 320L) lams(ev.x)
                }
            }
        }
        return true
    }

    private fun lams(x: Float) {
        val w = width.coerceAtLeast(1)
        if (x < w / 3f) {
            badiNarm()          // لبهٔ چپ = صفحهٔ بعد (کتاب راست‌چین)
        } else if (x > w * 2f / 3f) {
            ghabliNarm()        // لبهٔ راست = صفحهٔ قبل
        } else {
            vaghteLamsVasat?.invoke()
        }
    }

    // ---------- هندسهٔ صفحه ----------
    private fun mostatilSafhe(b: Bitmap?): RectF {
        val w = width.toFloat()
        val h = height.toFloat()
        if (b == null || w <= 1f || h <= 1f) return RectF(0f, 0f, w, h)
        val nesbe = b.width.toFloat() / b.height.toFloat()
        val nesbeGhabe = w / h
        return if (nesbe > nesbeGhabe) {
            val hh = w / nesbe
            RectF(0f, (h - hh) / 2f, w, (h + hh) / 2f)
        } else {
            val ww = h * nesbe
            RectF((w - ww) / 2f, 0f, (w + ww) / 2f, h)
        }
    }

    private fun pahnayeSafhe(): Float = mostatilSafhe(tasvirRu ?: tasvirPust).width()

    // ---------- نقاشی ----------
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(rangZamin)
        val ru = tasvirRu
        if (ru == null || ru.isRecycled) return
        boresh.colorFilter = filterShab
        boreshKaghaz.colorFilter = filterShab

        if (halat == 0 || pishraft <= 0.001f || tasvirPust == null) {
            resmSade(canvas, ru)
            return
        }
        if (jahat == 1) {
            // جلو: برگه = صفحهٔ فعلی، زیرِ آن = صفحهٔ بعد
            sahneCharkhesh(canvas, ru, tasvirPust!!, 1)
        } else {
            // عقب: برگه = صفحهٔ مقصد که از عطف باز می‌شود، زیرِ آن = صفحهٔ فعلی
            sahneCharkhesh(canvas, tasvirPust!!, ru, -1)
        }
    }

    /*
     * صحنهٔ ورق‌خوردن — برای هر دو جهت با یک نقشه:
     * barg = برگهٔ در حال ورق‌خوردن، zir = صفحه‌ای که زیرِ آن پیدا می‌شود
     * جلو: خط تاشدن از چپ به راست می‌رود؛ عقب: از راست به چپ (زمان معکوس)
     */
    private fun sahneCharkhesh(canvas: Canvas, barg: Bitmap, zir: Bitmap, jahat: Int) {
        val rect = mostatilSafhe(barg)
        val L = rect.left
        val R = rect.right
        val T = rect.top
        val B = rect.bottom
        val pahna = rect.width()
        val boland = rect.height()
        val cy = pahna * 0.05f                  // شعاع استوانهٔ خم
        val te = pishraft
        val c = if (jahat == 1) L - cy + (pahna + 2f * cy) * te
        else R + cy - (pahna + 2f * cy) * te    // خط تاشدن
        val maxLift = boland * 0.055f * (1f - 0.55f * te)
        val dp1 = Abzar.dp(context, 1f).toFloat()

        // ۱) صفحهٔ زیرِ برگه
        resmDarMostatil(canvas, zir, rect)

        // ۲) سایهٔ برگهٔ بلندشده روی صفحهٔ زیر (سمت چپ خط تاشدن)
        if (c > L && c < R + cy) {
            val x0 = max(L, c - 1.9f * cy)
            boreshRang.shader = LinearGradient(
                x0, 0f, c, 0f,
                Color.TRANSPARENT, Color.argb(96, 0, 0, 0), Shader.TileMode.CLAMP
            )
            canvas.drawRect(x0, T, c, B, boreshRang)
            boreshRang.shader = null
        }

        // ۳) بخش تختِ برگه (از خط تاشدن تا عطف) + سایهٔ خمیده‌شدن
        if (c < R && c > L - cy) {
            canvas.save()
            canvas.clipRect(max(L, c), T, R, B)
            resmDarMostatil(canvas, barg, rect)
            canvas.restore()
            if (c > L) {
                val x1 = min(R, c + cy)
                boreshRang.shader = LinearGradient(
                    c, 0f, x1, 0f,
                    Color.argb(60, 0, 0, 0), Color.TRANSPARENT, Shader.TileMode.CLAMP
                )
                canvas.drawRect(c, T, x1, B, boreshRang)
                boreshRang.shader = null
            }
        }

        // ۴) بخش تاشده — پشتِ کاغذ (کاغذ ساده؛ هیچ متنی آینه‌ای نمی‌شود)
        if (c > L && c < R) {
            resmPoshtKaghaz(canvas, rect, c, cy, maxLift)
        }

        // ۵) سایهٔ لبهٔ آزاد برگه روی صفحهٔ زیر
        val labe = L + 2f * (c - L)
        if (labe < R - dp1 && c > L + dp1) {
            boreshRang.shader = LinearGradient(
                labe - dp1 * 7f, 0f, labe + dp1 * 4f, 0f,
                Color.TRANSPARENT, Color.argb(70, 0, 0, 0), Shader.TileMode.CLAMP
            )
            canvas.drawRect(labe - dp1 * 7f, T, labe + dp1 * 4f, B, boreshRang)
            boreshRang.shader = null
        }

        // ۶) درخشش باریک روی خط تاشدن — نور روی لبهٔ کاغذ
        if (c > L && c < R) {
            boreshRang.shader = LinearGradient(
                c - dp1 * 1.5f, 0f, c + dp1 * 2.5f, 0f,
                intArrayOf(Color.TRANSPARENT, Color.argb(58, 255, 255, 255), Color.TRANSPARENT),
                null, Shader.TileMode.CLAMP
            )
            canvas.drawRect(c - dp1 * 1.5f, T, c + dp1 * 2.5f, B, boreshRang)
            boreshRang.shader = null
        }
    }

    // برجستگی استوانهٔ خم — بلندشدن لبهٔ کاغذ نسبت به خط تاشدن
    private fun liftDar(x: Float, c: Float, cy: Float, maxLift: Float): Float {
        if (x <= c) return 0f
        return maxLift * sin((Math.PI * min(1.0, ((x - c) / (2.2f * cy)).toDouble())).toFloat())
    }

    /*
     * پشتِ برگهٔ تاشده — کاغذ ساده از خط تاشدن تا لبهٔ آزاد، با برجستگی خم
     * پشتِ برگهٔ چاپی خالی است؛ پس هیچ متنی این‌جا کشیده نمی‌شود
     */
    private fun resmPoshtKaghaz(canvas: Canvas, rect: RectF, c: Float, cy: Float, maxLift: Float) {
        val T = rect.top
        val B = rect.bottom
        val labe = min(rect.left + 2f * (c - rect.left), rect.right)   // لبهٔ آزاد برگه
        if (labe <= c) return
        val rah = 14
        val shake = Path()
        shake.moveTo(c, T)
        for (i in 1..rah) {
            val x = c + (labe - c) * i / rah.toFloat()
            shake.lineTo(x, T - liftDar(x, c, cy, maxLift))
        }
        shake.lineTo(labe, B - liftDar(labe, c, cy, maxLift))
        for (i in rah - 1 downTo 0) {
            val x = c + (labe - c) * i / rah.toFloat()
            shake.lineTo(x, B - liftDar(x, c, cy, maxLift))
        }
        shake.close()
        boreshKaghaz.shader = LinearGradient(
            c, 0f, labe, 0f,
            intArrayOf(0xFFE7DFC9.toInt(), 0xFFFAF6EA.toInt(), 0xFFF2ECDC.toInt()),
            floatArrayOf(0f, 0.35f, 1f), Shader.TileMode.CLAMP
        )
        canvas.drawPath(shake, boreshKaghaz)
        boreshKaghaz.shader = null
        // سایهٔ خمِ استوانه نزدیک خط تاشدن — فقط داخل خودِ برگه
        val x1 = min(rect.right, c + 2.4f * cy)
        if (x1 > c) {
            canvas.save()
            canvas.clipPath(shake)
            boreshRang.shader = LinearGradient(
                c, 0f, x1, 0f,
                Color.argb(78, 0, 0, 0), Color.TRANSPARENT, Shader.TileMode.CLAMP
            )
            canvas.drawRect(c, T - maxLift, x1, B, boreshRang)
            boreshRang.shader = null
            canvas.restore()
        }
    }

    // نقاشی سادهٔ یک صفحه در قاب
    private fun resmSade(canvas: Canvas, b: Bitmap) {
        val rect = mostatilSafhe(b)
        resmDarMostatil(canvas, b, rect)
    }

    private fun resmDarMostatil(canvas: Canvas, b: Bitmap, rect: RectF) {
        canvas.drawBitmap(b, null, rect, boresh)
    }
}

/*
 * صدای ورق‌خوردن — یک بار بارگذاری و همیشه آماده؛ کلید بی‌صدا در حافظهٔ گوشی
 */
object Seda {

    private var hawz: SoundPool? = null
    private var shenakhte = 0
    private var amade = false

    private fun amadeKon(c: Context) {
        if (hawz != null) return
        val sifat = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        hawz = SoundPool.Builder().setAudioAttributes(sifat).setMaxStreams(2).build()
        hawz?.setOnLoadCompleteListener { _, _, status ->
            if (status == 0) amade = true
        }
        shenakhte = hawz?.load(c, R.raw.varagh, 1) ?: 0
    }

    fun begozaron(c: Context) {
        if (!Ketab.sedaBekhon(c)) return
        amadeKon(c)
        if (amade && shenakhte != 0) {
            hawz?.play(shenakhte, 0.85f, 0.85f, 1, 0, 1f)
        }
    }
}
