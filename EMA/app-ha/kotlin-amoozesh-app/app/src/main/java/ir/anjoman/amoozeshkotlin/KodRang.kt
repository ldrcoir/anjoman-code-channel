package ir.anjoman.amoozeshkotlin

import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import java.util.regex.Pattern

/*
 * رنگ‌آمیزی کد Kotlin — سبک ویرایشگر تیرهٔ EmaRoid
 * «کدها متن خام نباشد» فرمان مالک (گردش ۶۷):
 *   کلیدواژه بنفش، رشته سبز، کامنت خاکستری کج، عدد نارنجی،
 *   نام تابع آبی، @حاشیه‌نویسی طلایی
 * یک عبارتِ مرکب تا هیچ دو رنگی روی هم نیفتند.
 */
object KodRang {

    private val RANG_KELID = 0xFFC792EA.toInt()      // کلیدواژه
    private val RANG_RESHTE = 0xFFC3E88D.toInt()     // رشته
    private val RANG_SARRESH = 0xFF7986A3.toInt()    // کامنت
    private val RANG_ADAD = 0xFFF78C6C.toInt()       // عدد
    private val RANG_TABE = 0xFF82AAFF.toInt()       // نام تابع
    private val RANG_SARSHENAS = 0xFFFFCB6B.toInt()  // @حاشیه‌نویسی

    private val KELIDVAZHE = setOf(
        "package", "import", "fun", "val", "var", "class", "object", "data",
        "if", "else", "when", "for", "while", "return", "null", "true", "false",
        "this", "in", "is", "as", "private", "internal", "override", "const",
        "break", "continue", "super", "interface", "enum", "sealed", "try",
        "catch", "finally", "throw", "do", "typeof"
    )

    // گروه‌ها: ۱=کامنت ۲=رشته ۳=حاشیه‌نویسی ۴=کلمه ۵=عدد
    private val GHALIBE: Pattern = Pattern.compile(
        "(//[^\\n]*)" +
            "|(\"(?:[^\"\\\\\\n]|\\\\.)*\")" +
            "|(@[A-Za-z_][A-Za-z0-9_]*)" +
            "|([A-Za-z_][A-Za-z0-9_]*)" +
            "|([0-9]+(?:\\.[0-9]+)?[fFlL]?)"
    )

    fun rangKon(kod: String): SpannableStringBuilder {
        val sb = SpannableStringBuilder(kod)
        val m = GHALIBE.matcher(sb)
        while (m.find()) {
            val shoroo = m.start()
            val tah = m.end()
            val g1 = m.group(1)
            val g2 = m.group(2)
            val g3 = m.group(3)
            val g4 = m.group(4)
            val g5 = m.group(5)
            when {
                g1 != null -> {
                    rang(sb, shoroo, tah, RANG_SARRESH)
                    sb.setSpan(StyleSpan(android.graphics.Typeface.ITALIC), shoroo, tah, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                g2 != null -> rang(sb, shoroo, tah, RANG_RESHTE)
                g3 != null -> rang(sb, shoroo, tah, RANG_SARSHENAS)
                g4 != null -> {
                    if (KELIDVAZHE.contains(g4)) {
                        rang(sb, shoroo, tah, RANG_KELID)
                        sb.setSpan(StyleSpan(android.graphics.Typeface.BOLD), shoroo, tah, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    } else {
                        // اگر بلافاصله بعدش پرانتز باز آمد یعنی فراخوانی تابع
                        var j = tah
                        while (j < sb.length && sb[j] == ' ') j++
                        if (j < sb.length && sb[j] == '(') rang(sb, shoroo, tah, RANG_TABE)
                    }
                }
                g5 != null -> rang(sb, shoroo, tah, RANG_ADAD)
            }
        }
        return sb
    }

    private fun rang(sb: SpannableStringBuilder, shoroo: Int, tah: Int, rang: Int) {
        sb.setSpan(ForegroundColorSpan(rang), shoroo, tah, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
}
