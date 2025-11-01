package com.on.weather.viewmodel

import android.provider.Contacts
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.on.network.data.ApiFailedResponse
import com.on.weather.data.CityForecastWeatherData
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
                _uiState.value = UiState.SUCCESS
            }
        }
    }

    private val _uiState = MutableStateFlow(UiState.INIT)
    val uiState: StateFlow<UiState> = _uiState

    private val _errorMessageLiveData = MutableLiveData<ApiFailedResponse>()
    val errorMessageLiveData: LiveData<ApiFailedResponse>
        get() = _errorMessageLiveData

    private val _weatherLiveData = MutableStateFlow<CityForecastWeatherData?>(null)
    val weatherLiveData: StateFlow<CityForecastWeatherData?>
        get() = _weatherLiveData

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