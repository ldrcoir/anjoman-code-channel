# آموزش کاتلین

اپ آموزشی کاتلین برای بچه‌های **انجمن کدنویسی مدرسهٔ شهید فقیهی**.

همه‌چیز با کاتلین خالص و سیستم View اندروید ساخته شده؛ بدون WebView، بدون Jetpack Compose و بدون هیچ کتابخانهٔ بیرونی. تمام رابط کاربری به‌صورت برنامه‌نویسی‌شده داخل کد ساخته می‌شه (بدون فایل XML لی‌اوت) و زبان اپ کاملاً فارسی و راست‌چینه.

## درس‌ها

| شماره | عنوان | موضوع |
| --- | --- | --- |
| ۱ | سلام دنیا! | `fun main` و `println` و کامنت |
| ۲ | متغیرها: val و var | تغییرناپذیری و انواع Int / Double / String / Boolean |
| ۳ | تصمیم‌گیری: if و when | شرط و جایگزین switch |
| ۴ | حلقه‌ها: for و while | تکرار با `..` و `until` |
| ۵ | تابع‌ها | تعریف fun، پارامتر، خروجی و مقدار پیش‌فرض |

هر درس یه آزمون سه‌سؤالی داره. بهترین نمرهٔ هر درس توی `SharedPreferences` با اسم «zakhire» ذخیره می‌شه و توی صفحهٔ اصلی، پیشرفت هر درس و نوار پیشرفت کل نمایش داده می‌شه.

## ساختار صفحه‌ها

```
SplashActivity  ← لوگو و اسم اپ، ۱.۲ ثانیه
      ↓
HomeActivity    ← فهرست درس‌ها + پیشرفت کل + دکمهٔ درباره
      ↓
LessonActivity  ← توضیح درس + نمونه کد
      ↓
QuizActivity    ← آزمون سه‌سؤالی + صفحهٔ نتیجه
```

## ساختار پروژه

```
kotlin-amoozesh-app/
├── settings.gradle.kts
├── build.gradle.kts
├── gradle.properties
├── app/
│   ├── build.gradle.kts
│   ├── proguard-rules.pro
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── assets/fonts/            ← فونت وزیرمتن (Regular / Bold / Black)
│       ├── res/mipmap-*/ic_launcher.png
│       └── java/ir/anjoman/amoozeshkotlin/
│           ├── Data.kt              ← داده‌های درس‌ها و سؤال‌ها
│           ├── FontBox.kt           ← لود و کش فونت‌ها
│           ├── UI.kt                ← ابزارهای رابط کاربری + ذخیرهٔ نمره‌ها
│           ├── SplashActivity.kt
│           ├── HomeActivity.kt
│           ├── LessonActivity.kt
│           ├── QuizActivity.kt
│           └── AboutActivity.kt
```

## باز کردن و ساخت APK

1. پروژه رو با Android Studio (نسخهٔ Hedgehog یا جدیدتر) باز کنید: **File > Open** و پوشهٔ `kotlin-amoozesh-app` رو انتخاب کنید.
2. صبر کنید تا Gradle سینک بشه. **توجه:** فایل‌های Gradle Wrapper (پوشهٔ `gradle/wrapper`) داخل پروژه نیست؛ اندروید استودیو خودش نسخهٔ مناسب Gradle رو دانلود و تنظیم می‌کنه. برای اجرای خط فرمانی هم می‌تونید دستی `gradle wrapper` رو بسازید.
3. برای ساخت خروجی: منوی **Build > Build App Bundle(s) / APK(s) > Build APKs**.
4. APK دیباگ توی مسیر `app/build/outputs/apk/debug/` ساخته می‌شه.

پیش‌نیازها: JDK 17 (اندروید استودیو به‌صورت پیش‌فرض داره)، compileSdk 34، minSdk 24.

## نکته‌های فنی

- همهٔ اکتیویتی‌ها از `android.app.Activity` ارث‌بری می‌کنن؛ یعنی هیچ وابستگی AndroidX لازم نیست.
- پکیج: `ir.anjoman.amoozeshkotlin` (توی `app/build.gradle.kts` با `namespace` تنظیم شده چون AGP 8 ویژگی `package` توی مانیفست رو قبول نمی‌کنه).
- جهت صفحه همه‌جا portrait و `supportsRtl` فعاله؛ جهت لی‌اوت هم داخل کد روی RTL قفل شده.
- اسکریپت ساخت آیکون: `scripts/g44_app_icon.py` (با Pillow یه مربع گرد سرمه‌ای با حرف طلایی «ک» می‌سازه و توی سه تا سایز mipmap ذخیره می‌کنه).
