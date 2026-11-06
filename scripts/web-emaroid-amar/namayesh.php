<?php
/**
 * ============================================================
 *  EmaRoid — داشبورد مشاهدهٔ آمار اپ‌ها
 * ============================================================
 * آدرس: https://host-shoma/amar/namayesh.php?ramz=RAMZ_SHOMA
 * رمز را همین بالا در RAMZ عوض کن!
 * از این آدرس می‌فهمی: چند باز شدن، کدوم کشور، کدوم گوشی، کدوم نسخه.
 * ============================================================
 */

$RAMZ = 'emaroid-1405';          // ← رمز را عوض کن
$dbFayl = __DIR__ . '/amar.sqlite';

header('Content-Type: text/html; charset=utf-8');

if (!isset($_GET['ramz']) || $_GET['ramz'] !== $RAMZ) {
    http_response_code(403);
    echo '<meta name="viewport" content="width=device-width,initial-scale=1">';
    echo '<body style="font-family:sans-serif;text-align:center;padding:40px;direction:rtl">';
    echo '<h1>EmaRoid</h1><p>ramz dorost nist — be shekl: <code>?ramz=RAMZ</code></p>';
    exit;
}

if (!file_exists($dbFayl)) {
    echo '<meta name="viewport" content="width=device-width,initial-scale=1">';
    echo '<body style="font-family:sans-serif;text-align:center;padding:40px;direction:rtl">';
    echo '<h1>EmaRoid — هنوز هیچ آماری نرسیده</h1>';
    echo '<p>اولین گزارش اپ که بیاید، همین‌جا می‌بینیش.</p>';
    exit;
}

$db = new PDO('sqlite:' . $dbFayl);
$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

$jam = (int)$db->query('SELECT COUNT(*) FROM amar')->fetchColumn();

function jadval($db, $sql, $onvan) {
    echo "<h2 style='direction:rtl'>$onvan</h2>";
    echo '<table border="1" cellpadding="6" style="border-collapse:collapse;direction:rtl">';
    foreach ($db->query($sql) as $r) {
        echo '<tr><td>' . htmlspecialchars($r[0]) . '</td><td><b>' . $r[1] . '</b></td></tr>';
    }
    echo '</table>';
}
?>
<meta name="viewport" content="width=device-width,initial-scale=1">
<body style="font-family:sans-serif;background:#0f172a;color:#eaf2ff;padding:24px;direction:rtl">
<h1 style="color:#2aa8b8">📊 EmaRoid — آمار اپ‌ها</h1>
<p>کل گزارش‌های رسیده: <b style="color:#f2b134;font-size:24px"><?= $jam ?></b></p>

<?php
jadval($db, "SELECT COALESCE(NULLIF(app,''),'?') app, COUNT(*) c FROM amar GROUP BY app ORDER BY c DESC LIMIT 20", 'اپ‌ها');
jadval($db, "SELECT COALESCE(NULLIF(country,''),'?') k, COUNT(*) c FROM amar GROUP BY k ORDER BY c DESC LIMIT 20", 'کشورها');
jadval($db, "SELECT COALESCE(NULLIF(maker || ' ' || model,' '),'?') m, COUNT(*) c FROM amar GROUP BY m ORDER BY c DESC LIMIT 20", 'گوشی‌ها');
jadval($db, "SELECT COALESCE(NULLIF(lang,''),'?') l, COUNT(*) c FROM amar GROUP BY l ORDER BY c DESC LIMIT 10", 'زبان رابط');
jadval($db, "SELECT COALESCE(NULLIF(v_name,''),'?') v, COUNT(*) c FROM amar GROUP BY v ORDER BY c DESC LIMIT 10", 'نسخه‌ها');
jadval($db, "SELECT COALESCE(NULLIF(screen,''),'?') s, COUNT(*) c FROM amar GROUP BY s ORDER BY c DESC LIMIT 10", 'صفحه‌نمایش‌ها');
?>

<p style="color:#94a3b8">IP هیچ‌وقت ذخیره نمی‌شود. آخرین گزارش: <?php
    $akhirin = $db->query('SELECT zaman FROM amar ORDER BY id DESC LIMIT 1')->fetchColumn();
    echo $akhirin ? date('Y-m-d H:i:s', (int)$akhirin) : '-';
?></p>
</body>
