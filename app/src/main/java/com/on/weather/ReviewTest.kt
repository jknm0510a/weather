package com.on.weather

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URL

/**
 * 這是為了測試 Gemini Android Code Review GitHub Action 而故意撰寫的「嚴重違規代碼」。
 * 包含了多種 Android 開發上的常見地雷與不規範寫法。
 */
class ReviewTestActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 違規 1：在主執行緒（Main Thread）直接進行耗時的網路同步請求與 I/O，會導致 App ANR (Application Not Responding)
        val response = getWeatherDataFromInternet()
        println("Response: $response")

        // 違規 2：記憶體洩漏！將 Activity Context 賦值給靜態（Companion Object）變數，導致 Activity 銷毀時無法回收
        badStaticContext = this

        // 違規 3：安全風險！硬編碼 (Hardcode) 敏感金鑰 API Key
        val openWeatherApiKey = "AIzaSyD-aBc123XyZ_DUMMY_WEATHER_KEY_FOR_TESTING"
        println("Key: $openWeatherApiKey")

        // 違規 4：不當使用協程 Dispatchers。耗時的操作應該在 Dispatchers.IO 執行，這裡卻用 Dispatchers.Main
        CoroutineScope(Dispatchers.Main).launch {
            // 模擬在主執行緒進行耗時計算
            var result = 0
            for (i in 1..100000000) {
                result += i
            }
        }
    }

    // 同步網路請求函數
    private fun getWeatherDataFromInternet(): String {
        return try {
            // 同步讀取網頁
            URL("https://api.openweathermap.org/data/2.5/weather?q=Taipei").readText()
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    companion object {
        // 記憶體洩漏根源
        var badStaticContext: Context? = null
    }
}

// 違規 5：Jetpack Compose 效能地雷！
@Composable
fun BadWeatherList() {
    // 每次 Composable 重組 (Recomposition) 時，都會在 UI 執行緒上重新執行高昂的篩選與物件創建，且沒有使用 remember 包裹！
    val largeDataList = (1..50000).map { "City Weather Report #$it" }
    val filteredList = largeDataList.filter { it.contains("Taipei") }

    // 違規 6：非 Idiomatic Kotlin 寫法。使用了傳統的 for 迴圈與手動非空檢查，而非 Kotlin 的 .forEach 或安全呼叫
    if (filteredList != null) {
        for (i in 0 until filteredList.size) {
            val item = filteredList.get(i)
            Text(text = item)
        }
    }
}
