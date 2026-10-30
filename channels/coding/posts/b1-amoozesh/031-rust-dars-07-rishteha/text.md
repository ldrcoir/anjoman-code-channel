# رشته‌ها: متن در Rust
آموزش Rust از صفر — درس ۷
انجمن کدنویسی | مدرسهٔ شهید فقیهی

دوختنِ متن و عدد با format!

فایل «src/main.rs»:
```rust
let esm = String::from("Ali");    // reshte
let sen = 15;                     // adad
let jomle = format!("{} senash {}", esm, sen);
println!("{} | tool: {}", jomle, esm.len());
# ──── خروجی ────
Ali senash 15 ast | tool: 3
```

- **String**: رشتهٔ واقعی و قابل رشد — با String::from ساخته می‌شود
- **format!**: متن و عدد را با {} به هم می‌دوزد؛ {} جای خالی است
- **len()**: تعداد حرف‌های رشته را می‌شمارد

نکته: تمرین: با format! جمله‌ای بساز که نام و معدل‌ات را با هم نشان دهد
