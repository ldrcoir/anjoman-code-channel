package ir.anjoman.amoozeshkotlin

/*
 * تنظیمات سراسری EmaRoid
 * همهٔ آدرس‌ها و کلیدها اینجاست تا بعداً با خواستهٔ مالک عوض شود
 */
object EmaRoidConfig {

    // برند سازنده
    const val BRAND = "EmaRoid"

    // آدرس گیرندهٔ آمار (وقتی مالک هاست PHP گرفت، همین را عوض کن)
    // فایل سمت سرور: web-emaroid-amar/amar.php
    const val AMAR_URL = "https://emaroid.example.com/amar/amar.php"

    // هر چند ساعت یک‌بار حداکثر یک گزارش بفرستیم
    const val AMAR_FASELE_SAAT = 24

    // تبلیغات: فعلاً «خانه» (تبلیغ خودمان) است؛
    // بعد از گرفتن اکانت تپسل، MODE را «tapsell» بگذار و کلیدها را پر کن
    const val TABLIGH_MODE = "khane" // "khane" | "tapsell"
    const val TAPSELL_APP_KEY = "PUT_TAPSELL_APP_KEY"
    const val TAPSELL_BANNER_ZONE = "PUT_BANNER_ZONE"
    const val TAPSELL_VIDEO_ZONE = "PUT_VIDEO_ZONE"

    // تبلیغ کامل (بینابین سبکِ خانه) بعد از هر چند آزمون
    const val TABLIGH_KAMEL_HAR_N_TEST = 3
}
