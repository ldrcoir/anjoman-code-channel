# تصمیم‌گیری: شرط if
آموزش Rust از صفر — درس ۶
انجمن کدنویسی | مدرسهٔ شهید فقیهی

کدِ تو هم مثل تو تصمیم می‌گیرد

فایل «src/main.rs»:
```rust
let emtiaz = 17;  // emtiaz-e bazi
if emtiaz >= 20 {
    println!("Barande shodi!");
} else if emtiaz >= 10 {
    println!("Edame bede!");
} else {
    println!("Dobara talash kon");
}
```

- **شرط = true/false**: پرانتز لازم نیست؛ آکولاد اجباری است
- **else if و else**: زنجیرهٔ شرایط از بالا به پایین چک می‌شود
- **if یک عبارت است**: در Rust می‌شود نتیجهٔ if را در let ریخت!

نکته: تمرین: در بازی رفیق مار، اگر امتیاز از رکورد بیشتر شد پیام «Rekord jadid!» چاپ کن
