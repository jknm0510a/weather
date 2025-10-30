package com.on.weather.api

import com.on.network._interface.IApi
import com.on.weather.data.CityWeatherData
import com.on.weather.data.CountryData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ICityApi : IApi {
    @GET("all?fields=name,capitalInfo,cca2,capital")
    suspend fun getAllCity(): Response<List<CountryData>>
}