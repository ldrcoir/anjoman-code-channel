# نصب و اولین برنامه
آموزش Rust از صفر — درس ۲

با rustup نصب کن، با cargo اجرا کن

فایل «terminal»:
```python
$ cargo new salam
$ cd salam
$ cargo run
```

فایل «src/main.rs»:
```python
fn main() {
    println!("Salam, donya!");
}
```

- **rustup**: نصب‌کنندهٔ رسمی؛ از نشانی rustup.rs
- **cargo**: دستیار همیشگی؛ ساخت پروژه، build و اجرا
- **src/main.rs**: فایل اصلی کد پروژه همین‌جاست

نکته: خروجی اولین برنامه‌ات را بفرست تا در جمعه معرفی کنیم
