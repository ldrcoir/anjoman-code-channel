<?php
/**
 * ============================================================
 *  EmaRoid — گیرندهٔ آمار اپ‌ها (سمت سرور)
 * ============================================================
 * برای هاست PHP ارزان (مثل هاست‌های cPanel ایرانی) ساخته شده.
 *
 * کارش: اپ کاتلین (AmarSanj.kt) یک JSON کوچک به این فایل POST می‌کند:
 *   { app, v_name, v_code, android, model, maker, screen,
 *     country, lang, opens, ts, brand }
 *
 * این فایل آن را در یک فایل SQLite کنار خودش ذخیره می‌کند
 * و هیچ اطلاعات شخصی (IMEI، شماره تلفن، ایمیل) نمی‌پذیرد.
 *
 * «آیا تعداد مجوزها را بالا می‌برد؟» — نه!
 * سمت اپ فقط مجوز INTERNET لازم است که همیشه بوده.
 *
 * امنیت:
 *  - طول JSON محدود است
 *  - هر کلید با فیلتر پاک‌سازی می‌شود
 *  - اگر خواستی فقط اپ خودت بفرستد: X-EmaRoid-App را در سرور بررسی کن
 * ============================================================
 */

$dbFayl = __DIR__ . '/amar.sqlite';
$hadAksarSize = 4096; // بدنهٔ JSON حداکثر ۴KB

header('Content-Type: application/json; charset=utf-8');

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    http_response_code(200);
    echo json_encode([
        'ok' => true,
        'sazande' => 'EmaRoid AmarSanj v1',
        'rahnama' => 'POST JSON be in adrs az app (AmarSanj.kt) — didan-e amar: namayesh.php'
    ], JSON_UNESCAPED_UNICODE);
    exit;
}

$badane = file_get_contents('php://input');
if ($badane === false || strlen($badane) > $hadAksarSize) {
    http_response_code(413);
    echo json_encode(['ok' => false, 'khata' => 'badane bozorg ast']);
    exit;
}

$dart = json_decode($badane, true);
if (!is_array($dart)) {
    http_response_code(400);
    echo json_encode(['ok' => false, 'khata' => 'json nist']);
    exit;
}

$pak = isset($_SERVER['HTTP_X_EMAROID_APP']) ? substr($_SERVER['HTTP_X_EMAROID_APP'], 0, 64) : '';

// کلیدهای مجاز — هر چیز دیگری دور ریخته می‌شود
$kelidha = ['app','v_name','v_code','android','model','maker','screen','country','lang','opens','ts','brand'];
$pakize = [];
foreach ($kelidha as $k) {
    $v = isset($dart[$k]) ? $dart[$k] : '';
    if (!is_scalar($v)) $v = '';
    $pakize[$k] = mb_substr(trim((string)$v), 0, 64);
}

// IP ذخیره نمی‌شود (حریم خصوصی)

try {
    $db = new PDO('sqlite:' . $dbFayl);
    $db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    $db->exec('CREATE TABLE IF NOT EXISTS amar (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        app TEXT, v_name TEXT, v_code TEXT, android TEXT,
        model TEXT, maker TEXT, screen TEXT, country TEXT,
        lang TEXT, opens TEXT, ts TEXT, brand TEXT,
        pak TEXT,
        zaman INTEGER
    )');
    $st = $db->prepare('INSERT INTO amar
        (app, v_name, v_code, android, model, maker, screen, country, lang, opens, ts, brand, pak, zaman)
        VALUES (:app,:v_name,:v_code,:android,:model,:maker,:screen,:country,:lang,:opens,:ts,:brand,:pak,:zaman)');
    $st->execute(array_merge($pakize, ['pak' => $pak, 'zaman' => time()]));

    echo json_encode(['ok' => true, 'sepas' => 'EmaRoid']);
} catch (Exception $e) {
    http_response_code(500);
    echo json_encode(['ok' => false, 'khata' => 'khataye dakheli']);
}
