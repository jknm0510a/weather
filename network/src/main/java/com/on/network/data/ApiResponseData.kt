package com.on.network.data

data class ApiResponseData<T>(
    val data : T?,
    val hasError: Boolean = false,
    val error: ApiFailedResponse? = null,
)

data class ApiFailedResponse(
    val code: Int,
    val message: String?,
    val throwable: Throwable?
)