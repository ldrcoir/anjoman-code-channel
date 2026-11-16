# تصمیم‌گیری: شرط if
آموزش Rust از صفر — درس ۸
انجمن کدنویسی | مدرسهٔ شهید فقیهی

برنامه‌ات را دو راهی کن

فایل «src/main.rs»:
```rust
let emtiaz: i32 = 17;   // emtiaz-e bazi
if emtiaz >= 20 {
    println!("Barande shodi!");
} else if emtiaz >= 10 {
    println!("Edame bede!");
} else {
    println!("Dobara talash kon");
}
# ──── خروجی ────
Edame bede!
```

- **if**: شرط true باشد بلوکش اجرا می‌شود؛ پرانتز لازم نیست
- **else if / else**: زنجیرهٔ شرایط از بالا به پایین چک می‌شود
- **if یک عبارت است**: می‌شود نتیجه‌اش را در let ریخت

نکته: تمرین: شرطی بنویس که اگر امتیاز از رکورد گذشت پیام Rekord jadid! چاپ کند
