package com.on.weather.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.on.weather.repo.MainRepository
import kotlinx.coroutines.launch

class MainViewModel(val repository: MainRepository): ViewModel() {
    fun getCityWeather(city: String) {
        viewModelScope.launch {
            repository.test()
        }
        viewModelScope.launch {
            repository.test2()
        }
    }
}