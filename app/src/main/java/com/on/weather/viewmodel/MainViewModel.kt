package com.on.weather.viewmodel

import androidx.lifecycle.ViewModel
import com.on.network._interface.IHttpClient

class MainViewModel(val client: IHttpClient): ViewModel() {
    fun test() {
        client
    }
}