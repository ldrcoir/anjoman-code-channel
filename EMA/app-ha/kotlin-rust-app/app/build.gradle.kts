import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

// کلید امضای قبلی EhsanMorad — همان امضای اپ‌های قبلی (پوشهٔ enteshar)
val klidProps = Properties().apply {
    val f = rootProject.file("enteshar/klid.properties")
    if (f.exists()) f.inputStream().use { load(it) }
}

android {
    namespace = "ir.anjoman.amoozeshrust"
    compileSdk = 34

    defaultConfig {
        applicationId = "ir.anjoman.amoozeshrust"
        minSdk = 24
        targetSdk = 34
        versionCode = 2
        versionName = "1.1"
    }

    if (klidProps.isNotEmpty()) {
        signingConfigs {
            create("enteshar") {
                storeFile = rootProject.file("enteshar/${klidProps.getProperty("klid.fail")}")
                storePassword = klidProps.getProperty("klid.ramz")
                keyAlias = klidProps.getProperty("klid.alias")
                keyPassword = klidProps.getProperty("klid.ramzKey")
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            if (klidProps.isNotEmpty()) {
                signingConfig = signingConfigs.getByName("enteshar")
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

// جیم‌تارگت کاتلین رو هم‌تراز جاوا نگه می‌داریم
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}
