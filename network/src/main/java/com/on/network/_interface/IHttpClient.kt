package com.on.network._interface

import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import com.on.network.data.ApiResponseData
import retrofit2.Response

interface IHttpClient {
    fun<T: IApi> create(service: Class<T>): T

    suspend fun <T> sendRequest(
        @WorkerThread request: suspend () -> Response<T>,
    ) : ApiResponseData<T>
}