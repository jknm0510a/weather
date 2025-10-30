package com.on.weather.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.on.weather.repo.MainRepository
import com.on.weather.utils.LocationProvider
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainViewModel(
    val repository: MainRepository,
    private val locationProvider: LocationProvider,
): ViewModel() {
    fun getCityWeather(city: String) {
        viewModelScope.launch {
            repository.test2()
        }
    }

    fun fetchCurrentLocation() {
        viewModelScope.launch {
            try {
                val location = locationProvider.getCurrentLocation()
                // 在這裡你可以接著呼叫 Repository 的方法去獲取天氣資訊
                 repository.getCityWeatherByLocation(location.latitude, location.longitude)

            } catch (e: SecurityException) {
//                _errorMessage.value = "定位權限未授予，無法獲取位置。"
                e.printStackTrace()
            } catch (e: Exception) {
//                _errorMessage.value = "無法獲取位置: ${e.message}"
                e.printStackTrace()
            }
        }
    }
}