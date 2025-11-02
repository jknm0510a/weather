package com.on.weather.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.on.weather.R
import com.on.weather.data.CityForecastWeatherData
import com.on.weather.data.UiState
import com.on.weather.viewmodel.MainViewModel

@Composable
fun WeatherView(
    weatherData: CityForecastWeatherData,
    viewModel: MainViewModel,
    onNavigateToCitySelection: () -> Unit
) {
    val hours by viewModel.hourLiveData.collectAsState()
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(
                    if (weatherData.current.is_day == 1) {
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
                    .padding(horizontal = 24.dp, vertical = 24.dp),
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
                    Spacer(modifier = Modifier.width(8.dp))

                    Icon(
                        imageVector =  Icons.Default.Refresh,
                        contentDescription = "Refresh",
                        tint = Color.White,
                        modifier = Modifier
                            .size(28.dp)
                            .clickable {
                                viewModel.refreshCurrentCityWeather()
                            }
                    )
                    Spacer(Modifier.weight(1f))
                    Icon(
                        imageVector =  Icons.Default.AddCircle,
                        contentDescription = "Select Other City",
                        tint = Color.White,
                        modifier = Modifier
                            .size(28.dp)
                            .clickable {
                                println("selected icon clicked!")
                                onNavigateToCitySelection()
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

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White.copy(alpha = 0.2f))
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                ) {
                    InfoItem(title = stringResource(R.string.wind_speed), value = "${weatherData.current.wind_kph} kph")
                    Spacer(modifier = Modifier.width(32.dp))
                    InfoItem(title = stringResource(R.string.humidity), value = "${weatherData.current.humidity}%")
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 0.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(top = 24.dp)
                ) {
                    itemsIndexed(hours?:listOf()) { index, hour ->
                        HourlyForecastItem(index, hour = hour)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Box(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 0.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.padding(top = 24.dp)
                    ) {
                        itemsIndexed(weatherData!!.forecast.forecastday) { index, forecastDay ->
                            DailyForecastItem(index, forecastDay)
                        }
                    }
                }
            }
        }
    }
}