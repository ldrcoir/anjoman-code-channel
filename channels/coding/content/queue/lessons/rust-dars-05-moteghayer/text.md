# متغیرها: let و let mut
آموزش Rust از صفر — درس ۵

پیش‌فرضِ Rust: هیچ‌چیز بی‌اجازه عوض نمی‌شود

فایل «src/main.rs»:
```rust
let rekord = 12;      // rekord-e bazi
let mut emtiaz = 10;    // emtiaz-e bazi
emtiaz = emtiaz + 5;
# ──── خروجی ────
emtiaz: 15
# ──── اگر rekord را عوض کنی؟ ────
error[E0384]: cannot assign twice
  to immutable variable `rekord`
```

- **let = قفل**: هر متغیر پیش‌فرض تغییرناپذیر است
- **let mut**: با اجازهٔ mut مقدار عوض می‌شود
- **کامپایلر حواسش هست**: بدون mut خطای E0384 می‌گیری!

نکته: تمرین: امتیاز رفیق مار را در متغیری mut دار نگه دار و سه بار به‌روزش کن
