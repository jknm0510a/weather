package com.on.weather.repo

import androidx.lifecycle.viewModelScope
import com.on.network._interface.IHttpClient
import com.on.network.data.UserInfo.TOKEN
import com.on.weather.api.ICityApi
import com.on.weather.api.IWeatherApi
import kotlinx.coroutines.launch

class MainRepository(
    private val weatherClient: IHttpClient,
    private val cityClient: IHttpClient
) {
    suspend fun test() {
        val token = TOKEN
        val api = weatherClient.create(IWeatherApi::class.java)
        weatherClient.sendRequest(
            request = {
                api.getCityWeather(token,"taoyuan")
            },
            onSuccess = {
                println(it)
            },
            onFailure = { code, _, _ ->
                code
            }
        )
    }

    suspend fun test2() {
        val api = cityClient.create(ICityApi::class.java)
        cityClient.sendRequest(
            request = {
                api.getAllCity()
            },
            onSuccess = {
                println(it)
            },
            onFailure = { code, _, _ ->
                code
            }
        )
    }

}