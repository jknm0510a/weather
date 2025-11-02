package com.on.weather.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.on.weather.R
import com.on.weather.data.ForecastDay
import com.on.weather.data.Hour
import com.on.weather.data.SimpleCityData

@Composable
fun InfoItem(title: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = title, color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = value, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun HourlyForecastItem(index: Int, hour: Hour) {
    val time = hour.time.substringAfter(" ")
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White.copy(alpha = 0.15f))
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = if (index == 0) stringResource(R.string.now) else time,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        Image(
            painter = rememberAsyncImagePainter("https:${hour.condition.icon}"),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .width(40.dp)
                .height(40.dp)
        )

        Text(
            text = "${hour.temp_c}°",
            color = Color.White,
            fontSize = 18.sp
        )
    }
}

@Composable
fun DailyForecastItem(index: Int, forecastDay: ForecastDay) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White.copy(alpha = 0.15f))
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Text(
            text =  if (index == 0) stringResource(R.string.today) else forecastDay.dayOfWeek,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        Image(
            painter = rememberAsyncImagePainter("https:${forecastDay.day.condition.icon}"),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .width(40.dp)
                .height(40.dp)
        )

        Text(
            text = "${forecastDay.day.daily_chance_of_rain}%",
            color = colorResource(R.color.text_color),
            fontSize = 16.sp
        )
        Spacer(Modifier.weight(1f))
        Text(
            text = "${forecastDay.day.maxtemp_c}°C - ${forecastDay.day.mintemp_c}°C",
            color = colorResource(R.color.text_color),
            fontSize = 16.sp
        )
    }
}

@Composable
fun CityItem(
    simpleCountryData: SimpleCityData,
    onClick: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick(simpleCountryData.cityName) }
            .background(Color.White.copy(alpha = 0.15f))
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text =  simpleCountryData.cityName,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        Spacer(Modifier.weight(1f))
        Text(
            text =  simpleCountryData.countryName,
            color = Color.White,
            fontSize = 14.sp,
        )
    }
}