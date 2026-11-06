package ir.anjoman.amoozeshkotlin

/*
 * موتور تم‌ها — شش تم رنگی
 * هر تم یک پالت کامل است؛ همهٔ صفحه‌ها رنگ‌ها را از TemHa.faeli می‌گیرند
 * تم انتخابی در SharedPreferences ذخیره می‌شود و با هر باز شدن اپ برمی‌گردد
 */

data class Tem(
    val esm: String,
    val zamin: Int,       // پس‌زمینهٔ اصلی
    val kart: Int,        // رنگ کارت‌ها
    val code: Int,        // پس‌زمینهٔ بلوک کد
    val tiz: Int,         // رنگ تاکیدی اصلی (دکمه‌ها)
    val talaei: Int,      // رنگ تاکیدی دوم (عنوان‌های بخش)
    val matn: Int,        // متن اصلی
    val kamrang: Int,     // متن کم‌رنگ
    val sabz: Int,        // جواب درست
    val ghermez: Int,     // جواب غلط
    val khakestari: Int,  // عناصر خالی/خنثی
    val tir: Boolean      // تم تیره است؟
)

object TemHa {

    // هر تم یک شخصیت دارد؛ اسم‌ها برای دانش‌آموزها آشناست
    val temHa: List<Tem> = listOf(
        Tem(
            "شب فیروزه‌ای",
            zamin = 0xFF0F172A.toInt(), kart = 0xFF1E293B.toInt(), code = 0xFF0B1220.toInt(),
            tiz = 0xFF2AA8B8.toInt(), talaei = 0xFFF2B134.toInt(),
            matn = 0xFFEAF2FF.toInt(), kamrang = 0xFF94A3B8.toInt(),
            sabz = 0xFF22C55E.toInt(), ghermez = 0xFFEF4444.toInt(),
            khakestari = 0xFF334155.toInt(), tir = true
        ),
        Tem(
            "جنگل سبز",
            zamin = 0xFF0B1F16.toInt(), kart = 0xFF143324.toInt(), code = 0xFF091812.toInt(),
            tiz = 0xFF34D399.toInt(), talaei = 0xFFA3E635.toInt(),
            matn = 0xFFE7FFF3.toInt(), kamrang = 0xFF86A99A.toInt(),
            sabz = 0xFF4ADE80.toInt(), ghermez = 0xFFF87171.toInt(),
            khakestari = 0xFF1D4A38.toInt(), tir = true
        ),
        Tem(
            "کهکشان بنفش",
            zamin = 0xFF191033.toInt(), kart = 0xFF241A47.toInt(), code = 0xFF120B26.toInt(),
            tiz = 0xFFA78BFA.toInt(), talaei = 0xFFF472B6.toInt(),
            matn = 0xFFF3EFFF.toInt(), kamrang = 0xFFA79EC9.toInt(),
            sabz = 0xFF34D399.toInt(), ghermez = 0xFFFB7185.toInt(),
            khakestari = 0xFF382A66.toInt(), tir = true
        ),
        Tem(
            "غروب نارنجی",
            zamin = 0xFF2A1508.toInt(), kart = 0xFF3D2010.toInt(), code = 0xFF1F0F05.toInt(),
            tiz = 0xFFFB923C.toInt(), talaei = 0xFFFACC15.toInt(),
            matn = 0xFFFFF3E7.toInt(), kamrang = 0xFFC4A183.toInt(),
            sabz = 0xFF4ADE80.toInt(), ghermez = 0xFFF87171.toInt(),
            khakestari = 0xFF5A3218.toInt(), tir = true
        ),
        Tem(
            "دریای آبی",
            zamin = 0xFF0A1A2F.toInt(), kart = 0xFF12294A.toInt(), code = 0xFF071224.toInt(),
            tiz = 0xFF38BDF8.toInt(), talaei = 0xFFFBBF24.toInt(),
            matn = 0xFFE6F4FF.toInt(), kamrang = 0xFF8FA9C9.toInt(),
            sabz = 0xFF34D399.toInt(), ghermez = 0xFFF87171.toInt(),
            khakestari = 0xFF1E3A5F.toInt(), tir = true
        ),
        Tem(
            "روز روشن",
            zamin = 0xFFF4F7FB.toInt(), kart = 0xFFFFFFFF.toInt(), code = 0xFFE8EEF6.toInt(),
            tiz = 0xFF0E7490.toInt(), talaei = 0xFFB45309.toInt(),
            matn = 0xFF0F172A.toInt(), kamrang = 0xFF64748B.toInt(),
            sabz = 0xFF16A34A.toInt(), ghermez = 0xFFDC2626.toInt(),
            khakestari = 0xFFCBD5E1.toInt(), tir = false
        )
    )

    // تم فعلی؛ اولین بار از حافظه خوانده می‌شود
    var faeli: Tem = temHa[0]
        private set

    fun barGhardan(context: android.content.Context) {
        val p = context.getSharedPreferences("zakhire", android.content.Context.MODE_PRIVATE)
        val esm = p.getString("tem", "شب فیروزه‌ای") ?: "شب فیروزه‌ای"
        faeli = temHa.firstOrNull { it.esm == esm } ?: temHa[0]
    }

    fun entekhabKon(context: android.content.Context, tem: Tem) {
        val p = context.getSharedPreferences("zakhire", android.content.Context.MODE_PRIVATE)
        p.edit().putString("tem", tem.esm).apply()
        faeli = tem
    }
}
