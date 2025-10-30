package com.on.weather.initializer

import android.content.Context
import android.util.Log
import androidx.startup.Initializer
import com.on.network.HttpInitializer
import org.koin.android.ext.koin.androidContext
import com.on.weather.repo.MainRepository
import com.on.weather.utils.LocationProvider
import com.on.weather.viewmodel.MainViewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

class AppInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        loadKoinModules(moduleList)
    }

    override fun dependencies(): List<Class<out Initializer<*>?>?> {
        return listOf(
            StartupInitializer::class.java,
            HttpInitializer::class.java
        )
    }

    val viewmodelModule = module {
        viewModel { MainViewModel(get(), get()) }
    }
    val repoModule = module {
        factory {
            MainRepository(
                get(named("weather")),
                get(named("city"))
            )
        }
    }

    val utilsModule = module {
        factory { LocationProvider(androidContext()) }
    }

    val moduleList = arrayListOf(
        utilsModule, viewmodelModule, repoModule
    )
}