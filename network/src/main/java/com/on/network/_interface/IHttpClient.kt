package com.on.network._interface

import com.on.network.BuildConfig

interface IHttpClient {
    var state: Int
    fun create()
}