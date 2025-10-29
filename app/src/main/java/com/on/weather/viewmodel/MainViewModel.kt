package com.on.weather.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.on.network.BuildConfig
import com.on.network._interface.IHttpClient
import com.on.weather.api.IWeatherApi
import kotlinx.coroutines.launch

class MainViewModel(val client: IHttpClient): ViewModel() {
    fun test() {
        val token = BuildConfig.TOKEN
        val api = client.create(IWeatherApi::class.java)
        viewModelScope.launch {
            client.sendRequest(
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

    }
}