# EmaRoid — آمارگیر اپ‌ها (سمت سرور، PHP)

برای هاست PHP ارزان (هر هاستی که PHP 7.4+ و SQLite داشته باشد — تقریباً همهٔ هاست‌های ایرانی cPanel).

## نصب (۳ دقیقه)

۱. فایل‌های `amar.php` و `namayesh.php` را در یک پوشهٔ هاست بگذار، مثلاً:
   `public_html/amar/`

۲. در `namayesh.php` رمز `$RAMZ` را عوض کن.

۳. در اپ (فایل `EmaRoidConfig.kt`) آدرس `AMAR_URL` را بگذار:
   `https://دامنه‌ات/amar/amar.php`

همین! اپ‌ها خودشان هر ۲۴ ساعت یک گزارش بی‌صدا می‌فرستند.

## مجوزها؟

اپ فقط به `INTERNET` نیاز دارد (که برای تبلیغ هم لازم است).
IMEI، شماره تلفن، مخاطبین، موقعیت — هیچ‌کدام گرفته نمی‌شود؛
پس هیچ مجوز جدیدی به اپ اضافه نمی‌شود و در بازار هم مشکلی نیست.

## دیدن آمار

`https://دامنه‌ات/amar/namayesh.php?ramz=رمز`

می‌بینی: تعداد باز شدن‌ها، اپ، کشور، مدل گوشی، زبان رابط، نسخه، رزولوشن.

## نکته‌های امنیتی

- IP ذخیره نمی‌شود.
- طول داده‌ها سقف دارد (۴KB) و کلیدهای ناشناس دور ریخته می‌شوند.
- پایگاه‌داده فایل `amar.sqlite` کنار اسکریپت است؛
  اگر هاستت پوشهٔ listing دارد، یک فایل خالی `index.html` هم بگذار
  (یا در `.htaccess` این را بگذار):
  ```
  <Files "amar.sqlite">
      Require all denied
  </Files>
  ```

## تست سریع بدون گوشی

```
curl -X POST https://دامنه/amar/amar.php \
  -H "Content-Type: application/json" \
  -H "X-EmaRoid-App: test" \
  -d '{"app":"test","v_name":"1.0","v_code":"1","android":"34","model":"Tst","maker":"Tst","screen":"1080 x 2400","country":"ir","lang":"fa","opens":"1","ts":"1700000000","brand":"EmaRoid"}'
```

پاسخ باید `{"ok":true,"sepas":"EmaRoid"}` باشد.
