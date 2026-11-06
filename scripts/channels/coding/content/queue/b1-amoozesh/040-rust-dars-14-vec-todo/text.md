# بردار (Vec)
آموزش Rust از صفر — درس ۱۴
انجمن کدنویسی | مدرسهٔ شهید فقیهی

لیستی که نفس می‌کشد + پروژهٔ کوچک: فهرست کارها

فایل «src/main.rs»:
```rust
struct Kar { onvan: String, anjamShod: bool }

fn main() {
    let mut list: Vec<Kar> = Vec::new();
    list.push(Kar { onvan: String::from("tamrin"),
                    anjamShod: false });
    println!("tedad: {}", list.len());
    for kar in list {
        let alamat = if kar.anjamShod { "[x]" } else { "[ ]" };
        println!("{} {}", alamat, kar.onvan);
    }
}
# ──── خروجی ────
tedad: 1
[ ] tamrin
```

- **mut**: جعبهٔ تغییرپذیر — بردار وسط برنامه می‌تواند بزرگ شود
- **list.push(kar)**: عضو تازه به انتهای بردار اضافه می‌شود؛ len طولش را می‌گوید

نکته: پروژهٔ آخر مسیر: «فهرست کارها» با struct + Vec — اگر همین برنامهٔ کوچک را خودت بنویسی، یعنی از درس ۱ تا ۱۴ را واقعاً یاد گرفته‌ای!
