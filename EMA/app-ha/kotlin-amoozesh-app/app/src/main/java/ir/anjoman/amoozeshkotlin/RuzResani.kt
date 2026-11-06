package ir.anjoman.amoozeshkotlin

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

/*
 * روزرسانی — به‌روزرسانی آنلاین اپ
 *
 * آخرین Release گیت‌هاب چک می‌شود؛ اگر فایل APK این اپ با نسخهٔ بالاتر
 * آن‌جا باشد، یک گفت‌وگوی کوچک پیشنهاد دانلود می‌دهد. اگر اینترنت نبود
 * یا چیزی پیدا نشد، بی‌صدا رد می‌شود — هیچ چیز اپ را نمی‌شکند.
 */
object RuzResani {

    fun barrasi(activity: Activity) {
        thread(name = "ruzresani") {
            try {
                val con = URL(EmaRoidConfig.RUZRESANI_URL).openConnection() as HttpURLConnection
                con.connectTimeout = 7000
                con.readTimeout = 7000
                con.setRequestProperty("Accept", "application/vnd.github+json")
                con.setRequestProperty("User-Agent", "EmaRoid")
                val kalame = con.inputStream.bufferedReader().use { it.readText() }
                con.disconnect()

                val json = JSONObject(kalame)
                val assets = json.getJSONArray("assets")
                var neskhDurust: String? = null
                var linkeDanlod: String? = null

                for (i in 0 until assets.length()) {
                    val a = assets.getJSONObject(i)
                    val esm = a.optString("name", "")
                    if (esm.contains(EmaRoidConfig.RUZRESANI_ALAME) && esm.endsWith(".apk")) {
                        neskhDurust = esm
                        linkeDanlod = a.optString("browser_download_url", "")
                        break
                    }
                }

                if (neskhDurust == null || linkeDanlod == null) return@thread

                val neskhTaze = neskhDurust.split("v")
                    .getOrNull(1)?.split("-", " ")
                    ?.firstOrNull() ?: return@thread
                if (!neskhBolandtar(neskhTaze, neskhFeli(activity))) return@thread

                AmarSanj.sabt(activity.applicationContext, "update_available", JSONObject().put("v", neskhTaze))

                Handler(Looper.getMainLooper()).post {
                    try {
                        AlertDialog.Builder(activity)
                            .setTitle(Zaban.t("neskh_jadid"))
                            .setMessage(Zaban.t("neskh_jadid_matn") + "\n\nv" + neskhTaze)
                            .setPositiveButton(Zaban.t("danlod")) { _, _ ->
                                AmarSanj.sabt(activity.applicationContext, "update_download", JSONObject().put("v", neskhTaze))
                                try {
                                    activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(linkeDanlod)))
                                } catch (_: Exception) {
                                }
                            }
                            .setNegativeButton(Zaban.t("baadan"), null)
                            .show()
                    } catch (_: Exception) {
                    }
                }
            } catch (_: Exception) {
                // بی‌صدا — دفعهٔ بعد
            }
        }
    }

    // نسخهٔ فعلی اپ — از خود بسته خوانده می‌شود
    private fun neskhFeli(activity: Activity): String {
        return try {
            activity.packageManager.getPackageInfo(activity.packageName, 0).versionName ?: "1.0"
        } catch (_: Exception) {
            "1.0"
        }
    }

    // مقایسهٔ نسخه‌ها: 1.2 نسبت به 1.0 بالاتر است
    fun neskhBolandtar(a: String, b: String): Boolean {
        val pa = a.split(".").map { it.filter { ch -> ch.isDigit() }.toIntOrNull() ?: 0 }
        val pb = b.split(".").map { it.filter { ch -> ch.isDigit() }.toIntOrNull() ?: 0 }
        for (i in 0 until maxOf(pa.size, pb.size)) {
            val x = pa.getOrNull(i) ?: 0
            val y = pb.getOrNull(i) ?: 0
            if (x > y) return true
            if (x < y) return false
        }
        return false
    }
}
