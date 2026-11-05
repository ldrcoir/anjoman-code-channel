package ir.emaroid.hesab

import android.content.Context

/*
 * سیستم چندزبانهٔ EmaRoid — ۱۲ زبان
 * فارسی، English، العربية، 中文، 日本語، हिन्दी، Español، Français، Deutsch، Русский، Português، Türkçe
 * زبان انتخابی در SharedPreferences ذخیره می‌شود؛ نوشته‌های صفحهٔ خانه و منوها از جدول می‌آیند.
 * محتوای ابزارهای تخصصی فعلاً فارسی است (مثل اپ آموزش).
 */
object Zaban {

    enum class Z { FA, EN, AR, ZH, JA, HI, ES, FR, DE, RU, PT, TR }

    private var feli: Z? = null

    fun bekhon(c: Context): Z {
        if (feli == null) {
            val p = c.getSharedPreferences("zakhire", Context.MODE_PRIVATE)
            feli = when (p.getString("zaban", "FA") ?: "FA") {
                "EN" -> Z.EN; "AR" -> Z.AR; "ZH" -> Z.ZH; "JA" -> Z.JA
                "HI" -> Z.HI; "ES" -> Z.ES; "FR" -> Z.FR; "DE" -> Z.DE
                "RU" -> Z.RU; "PT" -> Z.PT; "TR" -> Z.TR
                else -> Z.FA
            }
        }
        return feli!!
    }

    fun zakhireKon(c: Context, z: Z) {
        feli = z
        c.getSharedPreferences("zakhire", Context.MODE_PRIVATE)
            .edit().putString("zaban", z.name).apply()
    }

    // فارسی و عربی راست‌چین‌اند، بقیه چپ‌چین
    fun rastChin(c: Context): Boolean = bekhon(c) == Z.FA || bekhon(c) == Z.AR

    fun namZaban(z: Z): String = when (z) {
        Z.FA -> "فارسی 🇮🇷"
        Z.EN -> "English 🇮🇪"
        Z.AR -> "العربية 🇵🇸"
        Z.ZH -> "中文 🇨🇳"
        Z.JA -> "日本語 🇯🇵"
        Z.HI -> "हिन्दी 🇮🇳"
        Z.ES -> "Español 🇪🇸"
        Z.FR -> "Français 🇫🇷"
        Z.DE -> "Deutsch 🇩🇪"
        Z.RU -> "Русский 🇷🇺"
        Z.PT -> "Português 🇧🇷"
        Z.TR -> "Türkçe 🇹🇷"
    }

    fun hameZabanHa(): List<Z> = Z.values().toList()

    fun t(kelid: String): String {
        val z = feli ?: Z.FA
        return jadval[kelid]?.get(z) ?: jadval[kelid]?.get(Z.FA) ?: kelid
    }

    private val jadval: Map<String, Map<Z, String>> = mapOf(

        "onvan_app" to mapOf(
            Z.FA to "ماشین‌حساب EmaRoid", Z.EN to "EmaRoid Calculator",
            Z.AR to "آلة EmaRoid الحاسبة", Z.ZH to "EmaRoid 计算器",
            Z.JA to "EmaRoid 電卓", Z.HI to "EmaRoid कैलकुलेटर",
            Z.ES to "Calculadora EmaRoid", Z.FR to "Calculatrice EmaRoid",
            Z.DE to "EmaRoid Taschenrechner", Z.RU to "Калькулятор EmaRoid",
            Z.PT to "Calculadora EmaRoid", Z.TR to "EmaRoid Hesap Makinesi"
        ),
        "zir_onvan" to mapOf(
            Z.FA to "نمودار • حل مرحله‌به‌مرحله • کولاتز و اعداد اول",
            Z.EN to "Graphs • Step-by-step solver • Collatz & primes",
            Z.AR to "الرسوم البيانية • الحل خطوة بخطوة • كولاتز والأعداد الأولية",
            Z.ZH to "函数图像 • 逐步解方程 • 科拉茨与质数",
            Z.JA to "グラフ • 段階的な解法 • コラッツと素数",
            Z.HI to "ग्राफ़ • चरण-दर-चरण हल • कोलैट्ज़ और अभाज्य",
            Z.ES to "Gráficas • Solución paso a paso • Collatz y primos",
            Z.FR to "Graphiques • Résolution pas à pas • Collatz et nombres premiers",
            Z.DE to "Graphen • Schritt-für-Schritt-Lösung • Collatz & Primzahlen",
            Z.RU to "Графики • Пошаговое решение • Коллатц и простые числа",
            Z.PT to "Gráficos • Solução passo a passo • Collatz e primos",
            Z.TR to "Grafik • Adım adım çözüm • Collatz ve asal sayılar"
        ),
        "b1t" to mapOf(
            Z.FA to "ماشین‌حساب علمی", Z.EN to "Scientific Calculator",
            Z.AR to "حاسبة علمية", Z.ZH to "科学计算器", Z.JA to "関数電卓",
            Z.HI to "वैज्ञानिक कैलकुलेटर", Z.ES to "Calculadora científica",
            Z.FR to "Calculatrice scientifique", Z.DE to "Wissenschaftlicher Rechner",
            Z.RU to "Инженерный калькулятор", Z.PT to "Calculadora científica",
            Z.TR to "Bilimsel Hesap Makinesi"
        ),
        "b1d" to mapOf(
            Z.FA to "چهارعملگر، توان، فاکتوریل، مثلثاتی",
            Z.EN to "Arithmetic, powers, factorial, trigonometry",
            Z.AR to "العمليات الأربع، الأس، المضروب، المثلثات",
            Z.ZH to "四则运算、幂、阶乘、三角函数",
            Z.JA to "四則演算・べき乗・階乗・三角関数",
            Z.HI to "अंकगणित, घात, क्रमगुणित, त्रिकोणमिति",
            Z.ES to "Aritmética, potencias, factorial, trigonometría",
            Z.FR to "Arithmétique, puissances, factorielle, trigonométrie",
            Z.DE to "Grundrechenarten, Potenzen, Fakultät, Trigonometrie",
            Z.RU to "Арифметика, степени, факториал, тригонометрия",
            Z.PT to "Aritmética, potências, fatorial, trigonometria",
            Z.TR to "Dört işlem, üs, faktöriyel, trigonometri"
        ),
        "b2t" to mapOf(
            Z.FA to "نمودار تابع", Z.EN to "Function Graph",
            Z.AR to "رسم الدوال", Z.ZH to "函数图像", Z.JA to "関数グラフ",
            Z.HI to "फलन ग्राफ़", Z.ES to "Gráfica de funciones",
            Z.FR to "Graphique de fonctions", Z.DE to "Funktionsgraph",
            Z.RU to "График функции", Z.PT to "Gráfico de funções",
            Z.TR to "Fonksiyon Grafiği"
        ),
        "b2d" to mapOf(
            Z.FA to "رسم y = f(x) با جابه‌جایی و زوم",
            Z.EN to "Plot y = f(x) with pan and zoom",
            Z.AR to "رسم y = f(x) مع التحريك والتكبير",
            Z.ZH to "绘制 y = f(x)，支持平移缩放",
            Z.JA to "y = f(x) を移動・ズーム付きで描画",
            Z.HI to "y = f(x) प्लॉट, पैन और ज़ूम के साथ",
            Z.ES to "Dibuja y = f(x) con desplazamiento y zoom",
            Z.FR to "Trace y = f(x) avec déplacement et zoom",
            Z.DE to "Zeichnet y = f(x) mit Verschieben und Zoom",
            Z.RU to "Строит y = f(x) с перемещением и зумом",
            Z.PT to "Plota y = f(x) com deslocamento e zoom",
            Z.TR to "Kaydırma ve yakınlaştırmayla y = f(x) çizer"
        ),
        "b3t" to mapOf(
            Z.FA to "حل معادله", Z.EN to "Equation Solver",
            Z.AR to "حل المعادلات", Z.ZH to "方程求解", Z.JA to "方程式ソルバー",
            Z.HI to "समीकरण हल", Z.ES to "Solucionador de ecuaciones",
            Z.FR to "Résolveur d'équations", Z.DE to "Gleichungslöser",
            Z.RU to "Решение уравнений", Z.PT to "Solucionador de equações",
            Z.TR to "Denklem Çözücü"
        ),
        "b3d" to mapOf(
            Z.FA to "خطی و درجهٔ دوم — مرحله‌به‌مرحله",
            Z.EN to "Linear & quadratic — step by step",
            Z.AR to "خطية وتربيعية — خطوة بخطوة",
            Z.ZH to "一次与二次方程 — 逐步求解",
            Z.JA to "一次・二次方程式を段階的に解く",
            Z.HI to "रैखिक और द्विघात — चरण दर चरण",
            Z.ES to "Lineales y cuadráticas — paso a paso",
            Z.FR to "Linéaires et quadratiques — pas à pas",
            Z.DE to "Linear & quadratisch — Schritt für Schritt",
            Z.RU to "Линейные и квадратные — шаг за шагом",
            Z.PT to "Lineares e quadráticas — passo a passo",
            Z.TR to "Birinci ve ikinci derece — adım adım"
        ),
        "b4t" to mapOf(
            Z.FA to "ابزار اعداد", Z.EN to "Number Tools",
            Z.AR to "أدوات الأعداد", Z.ZH to "数字工具", Z.JA to "数のツール",
            Z.HI to "संख्या उपकरण", Z.ES to "Herramientas numéricas",
            Z.FR to "Outils numériques", Z.DE to "Zahlenwerkzeuge",
            Z.RU to "Инструменты чисел", Z.PT to "Ferramentas numéricas",
            Z.TR to "Sayı Araçları"
        ),
        "b4d" to mapOf(
            Z.FA to "اول، عوامل، ب.م.م، Collatz، مبنا",
            Z.EN to "Primes, factors, GCD, Collatz, bases",
            Z.AR to "الأعداد الأولية، العوامل، القاسم المشترك، كولاتز، الأسس",
            Z.ZH to "质数、因数、最大公约数、Collatz、进制",
            Z.JA to "素数・因数・最大公約数・コラッツ・基数",
            Z.HI to "अभाज्य, गुणनखंड, म.स.प., Collatz, आधार",
            Z.ES to "Primos, factores, MCD, Collatz, bases",
            Z.FR to "Premiers, facteurs, PGCD, Collatz, bases",
            Z.DE to "Primzahlen, Faktoren, ggT, Collatz, Basen",
            Z.RU to "Простые, множители, НОД, Коллатц, системы счисления",
            Z.PT to "Primos, fatores, MDC, Collatz, bases",
            Z.TR to "Asallar, çarpanlar, EBOB, Collatz, tabanlar"
        ),
        "khane" to mapOf(
            Z.FA to "‹ خانه", Z.EN to "‹ Home", Z.AR to "‹ الرئيسية",
            Z.ZH to "‹ 主页", Z.JA to "‹ ホーム", Z.HI to "‹ होम",
            Z.ES to "‹ Inicio", Z.FR to "‹ Accueil", Z.DE to "‹ Start",
            Z.RU to "‹ Главная", Z.PT to "‹ Início", Z.TR to "‹ Ana Sayfa"
        ),
        "bazgasht" to mapOf(
            Z.FA to "بازگشت", Z.EN to "Back", Z.AR to "رجوع",
            Z.ZH to "返回", Z.JA to "戻る", Z.HI to "वापस",
            Z.ES to "Atrás", Z.FR to "Retour", Z.DE to "Zurück",
            Z.RU to "Назад", Z.PT to "Voltar", Z.TR to "Geri"
        ),
        "darbare" to mapOf(
            Z.FA to "دربارهٔ EmaRoid", Z.EN to "About EmaRoid",
            Z.AR to "حول EmaRoid", Z.ZH to "关于 EmaRoid", Z.JA to "EmaRoid について",
            Z.HI to "EmaRoid के बारे में", Z.ES to "Acerca de EmaRoid",
            Z.FR to "À propos d'EmaRoid", Z.DE to "Über EmaRoid",
            Z.RU to "О EmaRoid", Z.PT to "Sobre o EmaRoid", Z.TR to "EmaRoid Hakkında"
        ),
        "darbare_matn" to mapOf(
            Z.FA to "ماشین‌حساب علمی با نمودار، حل مرحله‌به‌مرحلهٔ معادله،\nکولاتز و اعداد اول؛ ساختهٔ EmaRoid برای بچه‌های پژوهشسرا",
            Z.EN to "Scientific calculator with graphs, step-by-step solver,\nCollatz and primes — made by EmaRoid for young coders",
            Z.AR to "آلة حاسبة علمية مع رسوم بيانية، حل خطوة بخطوة،\nكولاتز والأعداد الأولية — من صناعة EmaRoid",
            Z.ZH to "科学计算器：函数图像、逐步解方程、\n科拉茨与质数 — EmaRoid 出品",
            Z.JA to "グラフ・段階的な解法・コラッツ・素数を備えた\n関数電卓 — EmaRoid 制作",
            Z.HI to "ग्राफ़, चरण-दर-चरण हल, कोलैट्ज़ और अभाज्य संख्याओं\nवाला वैज्ञानिक कैलकुलेटर — EmaRoid द्वारा",
            Z.ES to "Calculadora científica con gráficas, solución paso a paso,\nCollatz y primos — hecha por EmaRoid",
            Z.FR to "Calculatrice scientifique avec graphiques, résolution pas à pas,\nCollatz et nombres premiers — par EmaRoid",
            Z.DE to "Wissenschaftlicher Rechner mit Graphen, Schritt-für-Schritt-Lösung,\nCollatz und Primzahlen — von EmaRoid",
            Z.RU to "Инженерный калькулятор: графики, пошаговое решение,\nКоллатц и простые числа — от EmaRoid",
            Z.PT to "Calculadora científica com gráficos, solução passo a passo,\nCollatz e primos — feita pela EmaRoid",
            Z.TR to "Grafik, adım adım çözüm, Collatz ve asal sayılarla\nbilimsel hesap makinesi — EmaRoid yapımı"
        ),
        "khata" to mapOf(
            Z.FA to "خطا", Z.EN to "Error", Z.AR to "خطأ", Z.ZH to "错误",
            Z.JA to "エラー", Z.HI to "त्रुटि", Z.ES to "Error", Z.FR to "Erreur",
            Z.DE to "Fehler", Z.RU to "Ошибка", Z.PT to "Erro", Z.TR to "Hata"
        ),
        "entekhab_zaban" to mapOf(
            Z.FA to "انتخاب زبان", Z.EN to "Choose language", Z.AR to "اختر اللغة",
            Z.ZH to "选择语言", Z.JA to "言語を選択", Z.HI to "भाषा चुनें",
            Z.ES to "Elegir idioma", Z.FR to "Choisir la langue", Z.DE to "Sprache wählen",
            Z.RU to "Выберите язык", Z.PT to "Escolher idioma", Z.TR to "Dil seçin"
        ),
        "tamam" to mapOf(
            Z.FA to "تأیید", Z.EN to "OK", Z.AR to "موافق", Z.ZH to "确定",
            Z.JA to "OK", Z.HI to "ठीक है", Z.ES to "Aceptar", Z.FR to "OK",
            Z.DE to "OK", Z.RU to "OK", Z.PT to "OK", Z.TR to "Tamam"
        )
    )
}
