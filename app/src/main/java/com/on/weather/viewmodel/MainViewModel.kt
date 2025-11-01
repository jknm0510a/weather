package com.on.weather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.on.network.data.ApiFailedResponse
import com.on.weather.repo.MainRepository
import com.on.weather.utils.LocationProvider
import kotlinx.coroutines.launch

class MainViewModel(
    val repository: MainRepository,
    private val locationProvider: LocationProvider,
): ViewModel() {
    fun getCityWeather(city: String) {
        viewModelScope.launch {
            repository.test2()
        }
    }

    private val _errorMessageLiveData = MutableLiveData<ApiFailedResponse>()
    val errorMessageLiveData: LiveData<ApiFailedResponse>
        get() = _errorMessageLiveData


    fun fetchCurrentLocation() {
        viewModelScope.launch {
            try {
                val location = locationProvider.getCurrentLocation()

                val res = repository.getCityWeatherByLocation(location.latitude, location.longitude)
                if (res.hasError) {
                    _errorMessageLiveData.value = res.error!!
                } else if (res.data != null){
                    val data = res.data!!
                }

            } catch (e: SecurityException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}