package com.on.weather.initializer

import android.content.Context
import android.util.Log
import androidx.startup.Initializer
import com.on.weather.utils.LocationProvider
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

/***
 * All of Initializer startup
 * */
class StartupInitializer: Initializer<Unit> {
    override fun create(context: Context) {
        startKoin {
            androidContext(context)
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>?>?> {
        return emptyList()
    }
}