package com.on.network

import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import com.on.network._interface.IApi
import com.on.network._interface.IHttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.SocketException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

class HttpClientImp(
    private val retrofit: Retrofit,
) : IHttpClient {

    override fun <T: IApi> create(service: Class<T>): T {
        return retrofit.create(service)
    }

    override suspend fun <T> sendRequest(
        @WorkerThread request: suspend () -> Response<T>,
        @UiThread onSuccess: (T) -> Unit,
        @UiThread onFailure: (code: Int, message: String?, throwable: Throwable?) -> Unit,
    ) {
        try {
            val response = request()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    withContext(Dispatchers.Main) {
                        onSuccess(body)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        onFailure(response.code(), "Body is Empty!", null)
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    onFailure(response.code(), response.message(), null)
                }
            }
        } catch (e: Exception) {
            when(e) {
                is SocketException,
                is UnknownHostException,
                is IOException -> {
                    withContext(Dispatchers.Main) {
                        onFailure(-1, "Network Error!", null)
                    }
                }
                else -> {
                    withContext(Dispatchers.Main) {
                        onFailure(-1, "Unknown Error!", null)
                    }
                }
            }
            onFailure(0, null, e)
        }
    }

    class Builder(val baseUrl: String) {
        private var enableLog: Boolean = false
        private var logLevel: Level = Level.BODY

        private var timeout: Long = 1500
        private var interceptors = mutableListOf<Interceptor>()

        fun enableLog(enable: Boolean, level: Level = Level.BODY) = apply {
            this.enableLog = enable
            this.logLevel = level
        }

        fun addInterceptor(interceptor: Interceptor) = apply {
            interceptors.add(interceptor)
        }

        fun setTimeout(timeout: Long) = apply {
            this.timeout = timeout
        }

        fun build(): HttpClientImp {
            val okHttpBuilder = OkHttpClient.Builder().apply {
                connectTimeout(timeout, TimeUnit.SECONDS)
                readTimeout(timeout, TimeUnit.SECONDS)
                writeTimeout(timeout, TimeUnit.SECONDS)
                interceptors.forEach { addInterceptor(it) }
            }
            if (enableLog) {
                okHttpBuilder.addInterceptor(HttpLoggingInterceptor().apply {
                    level = logLevel
                })
            }
            val builder = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
            return HttpClientImp(builder.build())
        }
    }
}