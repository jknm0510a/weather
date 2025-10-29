package com.on.weather.api

import com.on.network._interface.IApi
import com.on.network.data.CityWeatherData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface IWeatherApi: IApi {
    @GET("current.json")
    suspend fun getCityWeather(
        @Query("key") token: String,
        @Query("q") city: String,
    ): Response<CityWeatherData>
}