package com.on.network

import android.content.Context
import android.util.Log
import androidx.startup.Initializer
import com.on.network._interface.IHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

class HttpInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        loadKoinModules(httpModule)
    }

    override fun dependencies(): List<Class<out Initializer<*>?>?> {
        return emptyList()
    }

    val httpModule = module {
        single<IHttpClient> {
            val baseUrl = BuildConfig.BASE_URL
            val token = BuildConfig.TOKEN
            val enableLog = BuildConfig.ENABLE_LOG
            val builder = HttpClientImp.Builder(baseUrl, token)
                .setTimeout(1000L)
                .enableLog(enableLog, HttpLoggingInterceptor.Level.BODY)
            builder.build()
        }
    }

}