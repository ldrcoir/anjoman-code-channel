package ir.anjoman.amoozeshkotlin

import android.content.Context
import android.graphics.Typeface

/*
 * فونت‌های وزیرمتن رو یه بار از assets لود می‌کنیم و کش نگه می‌داریم
 * تا هربار که متن می‌سازیم دوباره لود نشن
 */
object FontBox {

    private var vazir: Typeface? = null
    private var vazirBold: Typeface? = null
    private var vazirBlack: Typeface? = null

    fun vazir(c: Context): Typeface {
        if (vazir == null) {
            vazir = Typeface.createFromAsset(c.assets, "fonts/Vazirmatn-Regular.ttf")
        }
        return vazir!!
    }

    fun vazirBold(c: Context): Typeface {
        if (vazirBold == null) {
            vazirBold = Typeface.createFromAsset(c.assets, "fonts/Vazirmatn-Bold.ttf")
        }
        return vazirBold!!
    }

    fun vazirBlack(c: Context): Typeface {
        if (vazirBlack == null) {
            vazirBlack = Typeface.createFromAsset(c.assets, "fonts/Vazirmatn-Black.ttf")
        }
        return vazirBlack!!
    }
}
