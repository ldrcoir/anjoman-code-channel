# تکرار: حلقه‌های while و loop
آموزش Rust از صفر — درس ۹
انجمن کدنویسی | مدرسهٔ شهید فقیهی

همان کار را هزار بار، بدون کپی‌پیست

فایل «src/main.rs»:
```rust
let mut shomare = 1;   // shomare-ye hafe
while shomare <= 3 {  // ta shart dorost ast
    println!("hafe-ye {}", shomare);
    shomare = shomare + 1;
}
loop {                 // bi-payan ta break
    shomare = shomare + 1;
    if shomare >= 6 { break; }
}
# ──── خروجی ────
hafe-ye 1
hafe-ye 2
hafe-ye 3
```

- **while**: اول شرط را چک می‌کند؛ تا وقتی درست است تکرار می‌شود
- **loop**: حلقهٔ بی‌پایان — تا وقتی break نگویی می‌چرخد
- **break**: کلید خروج از هر حلقه‌ای، همان‌جا که گفتی

نکته: تمرین: با while اعداد ۱ تا ۱۰ را چاپ کن؛ بعد همان کار را با loop و break انجام بده
