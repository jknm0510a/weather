package com.on.weather.viewmodel

import android.util.Range
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.on.network.data.ApiFailedResponse
import com.on.weather.data.CityForecastWeatherData
import com.on.weather.data.ForecastDay
import com.on.weather.data.Hour
import com.on.weather.data.SimpleCityData
import com.on.weather.data.UiState
import com.on.weather.repo.MainRepository
import com.on.weather.utils.LocationProvider
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    val repository: MainRepository,
    private val locationProvider: LocationProvider,
): ViewModel() {
    private var currentLocationName = "Taipei"

    private var currentSelectCityName = currentLocationName
    private val _uiState = MutableStateFlow(UiState.INIT)
    val uiState: StateFlow<UiState> = _uiState

    private val _errorMessageLiveData = MutableStateFlow<ApiFailedResponse?>(null)
    val errorMessageLiveData: MutableStateFlow<ApiFailedResponse?>
        get() = _errorMessageLiveData

    private val _weatherLiveData = MutableStateFlow<CityForecastWeatherData?>(null)
    val weatherLiveData: StateFlow<CityForecastWeatherData?>
        get() = _weatherLiveData

    private val _hoursLiveData = MutableStateFlow<List<Hour>?>(null)
    val hourLiveData: StateFlow<List<Hour>?>
        get() = _hoursLiveData

    private val _citiesLiveData = MutableStateFlow<List<SimpleCityData>?>(null)
    val citiesLiveData: StateFlow<List<SimpleCityData>?>
        get() = _citiesLiveData


    fun fetchCities() {
        viewModelScope.launch {
            _uiState.value = UiState.LOADING
            val res = repository.getCities()
            if (res.hasError) {
                _uiState.value = UiState.FAILED
                _errorMessageLiveData.value = res.error!!
            } else if (res.data != null) {
                val list = arrayListOf<SimpleCityData>()
                res.data!!.forEach { countryData ->
                    countryData.capital?.forEach { capital ->
                        if (capital != currentLocationName) {
                            list.add(
                                SimpleCityData(
                                    cityName = capital,
                                    capitalInfo = countryData.capitalInfo,
                                    countryName = countryData.name.common,
                                )
                            )
                        }
                    }
                }
                _citiesLiveData.value = list.sortedBy { it.cityName }.toMutableList().apply {
                    if (currentLocationName.isNotEmpty()) {
                        add(
                            index = 0,
                            element = SimpleCityData(
                                cityName = currentLocationName,
                                capitalInfo = null,
                                countryName = "LOCATION",
                            )
                        )
                    }
                }
                _uiState.value = UiState.SUCCESS
            }
        }
    }

    fun getWeatherByCity(city: String) {
        viewModelScope.launch {
            getWeather(city)
        }
    }

    fun refreshCurrentCityWeather() {
        getWeatherByCity(currentSelectCityName)
    }

    fun changeSelectCity(city: String) {
        currentSelectCityName = city
    }


    suspend fun getCityWeatherByLocation(lat: Double, lon: Double) : String {
        return viewModelScope.async {
            getWeather("$lat,$lon")
        }.await()
    }

    suspend fun getWeather(q: String, days: Int = 7): String {
        _uiState.value = UiState.LOADING
        val res = repository.getWeather(q, days)
        if (res.hasError) {
            _errorMessageLiveData.value = res.error!!
            _uiState.value = UiState.FAILED
            return ""
        } else if (res.data != null) {
            _weatherLiveData.value = res.data!!
            process24HoursForecast(res.data!!.forecast.forecastday)
            _uiState.value = UiState.SUCCESS
            return res.data!!.location.name
        }
        return ""
    }

    fun process24HoursForecast(forecastday: List<ForecastDay>) {
        val start = (System.currentTimeMillis() / 1000) - 60*60
        val end = start + 24*60*60
        val range = Range(start, end )
        val hours = forecastday.flatMap { it.hour }.filter { it.time_epoch in range }.toList()
        _hoursLiveData.value = hours
    }

    fun fetchCurrentLocation() {
        viewModelScope.launch {
            try {
                val location = locationProvider.getCurrentLocation()
                val locationName = getCityWeatherByLocation(location.latitude, location.longitude)
                if (locationName.isNotEmpty()) {
                    currentLocationName = locationName
                    currentSelectCityName = locationName
                }
            } catch (e: SecurityException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}