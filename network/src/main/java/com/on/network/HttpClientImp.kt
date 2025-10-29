package com.on.network

import com.on.network._interface.IHttpClient
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class HttpClientImp(
    private val retrofit: Retrofit,
    private val token: String,
) : IHttpClient {
    override var state: Int = 0
    override fun create() {

    }


    class Builder(val baseUrl: String, val token: String) {
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
            return HttpClientImp(builder.build(), token)
        }
    }
}