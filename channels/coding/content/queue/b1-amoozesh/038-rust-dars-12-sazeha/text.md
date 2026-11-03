# سازه‌ها (struct)
آموزش Rust از صفر — درس ۱۲
انجمن کدنویسی | مدرسهٔ شهید فقیهی

داده‌های مرتبط، یک‌جا و با نام

فایل «src/main.rs»:
```rust
struct DaneshAmooz {        // ghalab-e jadid
    esm: String,
    sen: i32,
}
fn main() {
    let d1 = DaneshAmooz {
        esm: String::from("Sara"),
        sen: 15,
    };
    println!("{} - {}", d1.esm, d1.sen);
}
# ──── خروجی ────
Sara - 15
```

- **struct**: قالبی که خودت می‌سازی؛ هر فیلد نام و نوع دارد
- **DaneshAmooz { .. }**: نمونه‌سازی از روی قالب؛ همهٔ فیلدها باید پر شوند
- **d1.esm**: با نقطه به هر فیلدِ نمونه می‌رسی

نکته: تمرین: سازه‌ای برای کتاب بساز: esm، nevisande، sale-chap؛ یک نمونه بساز و فیلدهایش را چاپ کن
