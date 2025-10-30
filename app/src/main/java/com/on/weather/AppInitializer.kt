package com.on.weather

import android.content.Context
import androidx.startup.Initializer
import com.on.network.HttpInitializer
import com.on.weather.repo.MainRepository
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
        viewModel { MainViewModel(get()) }
    }
    val repoModule = module {
        factory { MainRepository(
            get(named("weather")),
            get(named("city"))
        ) }
    }

    val moduleList = arrayListOf(
        viewmodelModule, repoModule
    )
}