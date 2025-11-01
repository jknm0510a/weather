package com.on.weather

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.copy
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.on.weather.data.CityForecastWeatherData
import com.on.weather.data.UiState
import com.on.weather.ui.theme.WeatherTheme
import com.on.weather.viewmodel.MainViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    val mainViewModel: MainViewModel by viewModel()

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false)) {
            mainViewModel.fetchCurrentLocation()
        } else {
            println("Location permission was denied.")
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkAndRequestLocationPermission()
        enableEdgeToEdge()
        setContent {
            WeatherTheme {
                WeatherScreen()
            }
        }
    }

    private fun checkAndRequestLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mainViewModel.fetchCurrentLocation()
        } else {
            // 請求權限
            locationPermissionRequest.launch(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
            )
        }
    }
}

@Composable
fun Greeting(
    name: String,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = koinViewModel()
) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WeatherTheme {
        Greeting("Android")
    }
}

@Composable
fun WeatherScreen(
    viewmodel: MainViewModel = viewModel()
) {
    val uiState by viewmodel.uiState.collectAsState()
    val weatherData: CityForecastWeatherData? by viewmodel.weatherLiveData.collectAsState()

    when(uiState) {
        UiState.INIT -> Unit
        UiState.LOADING -> {

        }
        UiState.FAILED -> {
        }
        UiState.SUCCESS -> {
            if (weatherData != null) {
                WeatherView(weatherData!!)
            }
        }
    }
}

@Composable
fun WeatherView(
    weatherData: CityForecastWeatherData
) {
    val viewmodel: MainViewModel = viewModel()
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        //Background
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(
                    id = if (weatherData.current.is_day == 1) {
                        R.drawable.bg_full_day
                    } else {
                        R.drawable.bg_full_night
                    }
                ),
                contentDescription = "Background Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(innerPadding)
                    .padding(horizontal = 24.dp, vertical = 64.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = weatherData.location.name,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp)) // 在文字和圖示之間加一點間距

                    Icon(
                        imageVector =  Icons.Default.Refresh,
                        contentDescription = "重新整理天氣資訊",
                        tint = Color.White, // 設定圖示顏色
                        modifier = Modifier
                            .size(28.dp)
                            .clickable {
                                println("Refresh icon clicked!")
                                viewmodel.getWeatherByCity("London")
                            }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "${weatherData.current.temp_c}",
                        fontSize = 68.sp,
                        fontWeight = FontWeight.Light,
                        color = Color.White
                    )
                    Text(
                        text = "°C",
                        fontSize = 20.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = weatherData.current.condition.text,
                        fontSize = 20.sp,
                        color = Color.White
                    )
                }


                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White.copy(alpha = 0.2f))
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                ) {
                    Row {
                        InfoItem(title = stringResource(R.string.wind_speed), value = "${weatherData.current.wind_kph} kph")
                        Spacer(modifier = Modifier.width(32.dp))
                        InfoItem(title = stringResource(R.string.humidity), value = "${weatherData.current.humidity}%")
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoItem(title: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = title, color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = value, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}