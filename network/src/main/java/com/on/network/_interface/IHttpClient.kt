package com.on.network._interface

import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import retrofit2.Response

interface IHttpClient {
    fun<T: IApi> create(service: Class<T>): T

    suspend fun <T> sendRequest(
        @WorkerThread request: suspend () -> Response<T>,
        @UiThread onSuccess: (T) -> Unit = { },
        @UiThread onFailure: (code: Int, message: String?, throwable: Throwable?) -> Unit = { _, _, _ -> },
    )
}