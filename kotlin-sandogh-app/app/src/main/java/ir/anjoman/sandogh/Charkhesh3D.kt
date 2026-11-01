package ir.anjoman.sandogh

import android.graphics.Camera
import android.graphics.Matrix
import android.view.animation.Animation
import android.view.animation.Transformation

/*
 * انیمیشن ورق‌زدن سه‌بعدی (چرخش حول محور Y)
 * الگوی کلاسیک Camera/Matrix اندروید — بدون هیچ کتابخانهٔ بیرونی
 */
class Charkhesh3D(
    private val az: Float,
    private val ta: Float,
    private val pivotX: Float,
    private val pivotY: Float
) : Animation() {

    override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
        val daraje = az + (ta - az) * interpolatedTime
        val camera = Camera()
        val matrix = Matrix()
        camera.save()
        camera.rotateY(daraje)
        camera.getMatrix(matrix)
        camera.restore()
        matrix.preTranslate(-pivotX, -pivotY)
        matrix.postTranslate(pivotX, pivotY)
        t.matrix = matrix
    }
}
