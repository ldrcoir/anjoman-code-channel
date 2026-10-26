# انواع داده: i32 و f64
آموزش Rust از صفر — درس ۶
انجمن کدنویسی | مدرسهٔ شهید فقیهی

به هر داده یک جعبهٔ اندازه‌درست

فایل «src/main.rs»:
```rust
let sen: i32 = 15;      // sen-e daneshamooz
let miangin: f64 = 18.75; // miangin-e nomre
let ghabol: bool = true;   // ghabol shodi?
# ──── خروجی ────
sen: 15
miangin: 18.75
ghabol: true
```

- **i32**: عدد صحیح — مثل ۱۵ یا -۴۰؛ پیش‌فرضِ راست
- **f64**: عدد اعشاری — مثل 18.75؛ دقتِ دوبرابر
- **bool**: فقط true یا false؛ جوابِ بله/نه

نکته: تمرین: سن و معدلت را در دو متغیرِ نوع‌دار نگه دار و با println! چاپشان کن
