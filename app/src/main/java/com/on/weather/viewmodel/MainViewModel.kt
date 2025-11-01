package com.on.weather.viewmodel

import android.util.Range
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.on.network.data.ApiFailedResponse
import com.on.weather.data.CityForecastWeatherData
import com.on.weather.data.ForecastDay
import com.on.weather.data.Hour
import com.on.weather.data.UiState
import com.on.weather.repo.MainRepository
import com.on.weather.utils.LocationProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    val repository: MainRepository,
    private val locationProvider: LocationProvider,
): ViewModel() {

    fun getWeatherByCity(city: String) {
        getWeather(city)
    }

    fun getCityWeatherByLocation(lat: Double, lon: Double) {
        getWeather("$lat,$lon")
    }

    fun getWeather(q: String, days: Int = 7) {
        viewModelScope.launch {
            val res = repository.getWeather(q, days)
            if (res.hasError) {
                _errorMessageLiveData.value = res.error!!
                _uiState.value = UiState.FAILED
            } else if (res.data != null) {
                _weatherLiveData.value = res.data!!
                process24HoursForecast(res.data!!.forecast.forecastday)
                _uiState.value = UiState.SUCCESS
            }
        }
    }

    fun process24HoursForecast(forecastday: List<ForecastDay>) {
        val start = (System.currentTimeMillis() / 1000) - 60*60
        val end = start + 24*60*60
        val range = Range(start, end )
        val hours = forecastday.flatMap { it.hour }.filter { it.time_epoch in range }.toList()
        _hoursLiveData.value = hours
    }

    private val _uiState = MutableStateFlow(UiState.INIT)
    val uiState: StateFlow<UiState> = _uiState

    private val _errorMessageLiveData = MutableLiveData<ApiFailedResponse>()
    val errorMessageLiveData: LiveData<ApiFailedResponse>
        get() = _errorMessageLiveData

    private val _weatherLiveData = MutableStateFlow<CityForecastWeatherData?>(null)
    val weatherLiveData: StateFlow<CityForecastWeatherData?>
        get() = _weatherLiveData

    private val _hoursLiveData = MutableStateFlow<List<Hour>?>(null)
    val hourLiveData: StateFlow<List<Hour>?>
        get() = _hoursLiveData

    fun fetchCurrentLocation() {
        viewModelScope.launch {
            try {
                val location = locationProvider.getCurrentLocation()
                getCityWeatherByLocation(location.latitude, location.longitude)
            } catch (e: SecurityException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}