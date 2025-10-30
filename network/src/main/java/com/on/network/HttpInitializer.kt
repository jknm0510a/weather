package com.on.network

import android.content.Context
import android.util.Log
import androidx.startup.Initializer
import com.on.network._interface.IHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.context.loadKoinModules
import org.koin.core.qualifier.named
import org.koin.dsl.module

class HttpInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        loadKoinModules(httpModule)
    }

    override fun dependencies(): List<Class<out Initializer<*>?>?> {
        return emptyList()
    }

    val httpModule = module {
        single<IHttpClient>(named("weather")) {
            val baseUrl = BuildConfig.WETHER_BASE_URL
            val enableLog = BuildConfig.ENABLE_LOG
            HttpClientImp.Builder(baseUrl)
                .setTimeout(1000L)
                .enableLog(enableLog, HttpLoggingInterceptor.Level.BODY)
                .build()
        }
        single<IHttpClient>(named("city")) {
            val baseUrl = BuildConfig.CITY_BASE_URL
            val enableLog = BuildConfig.ENABLE_LOG
            HttpClientImp.Builder(baseUrl)
                .setTimeout(1000L)
                .enableLog(enableLog, HttpLoggingInterceptor.Level.BODY)
                .build()
        }
    }

}