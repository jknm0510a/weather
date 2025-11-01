package com.on.weather.repo

import androidx.lifecycle.viewModelScope
import com.on.network._interface.IHttpClient
import com.on.network.data.ApiResponseData
import com.on.network.data.UserInfo.TOKEN
import com.on.weather.api.ICityApi
import com.on.weather.api.IWeatherApi
import com.on.weather.data.CityForecastWeatherData
import com.on.weather.data.Day

class MainRepository(
    private val weatherClient: IHttpClient,
    private val cityClient: IHttpClient
) {
    suspend fun getWeather(q: String, day: Int): ApiResponseData<CityForecastWeatherData> {
        val token = TOKEN
        val api = weatherClient.create(IWeatherApi::class.java)
        return weatherClient.sendRequest(
            request = {
                api.getCityForecastWeather(
                    token = token,
                    q = q,
                    days = 7,
                )
            }
        )
    }

    suspend fun test2() {
        val api = cityClient.create(ICityApi::class.java)
//        cityClient.sendRequest(
//            request = {
//                api.getAllCity()
//            },
//            onSuccess = {
//                println(it)
//            },
//            onFailure = { code, _, _ ->
//                code
//            }
//        )
    }

}