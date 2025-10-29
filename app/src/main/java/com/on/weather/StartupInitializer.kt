package com.on.weather

import android.content.Context
import android.util.Log
import androidx.startup.Initializer
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

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