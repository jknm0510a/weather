package com.on.weather.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.on.weather.data.UiState
import com.on.weather.viewmodel.MainViewModel
import org.koin.androidx.compose.koinViewModel

object AppRoutes {
    const val WEATHER_SCREEN = "weather_screen"
    const val CITY_SELECTION_SCREEN = "city_selection_screen"
}

@Composable
fun WeatherAppNavigation() {
    val navController = rememberNavController()
    val viewModel: MainViewModel = koinViewModel()

    NavHost(
        navController = navController,
        startDestination = AppRoutes.WEATHER_SCREEN
    ) {
        composable(AppRoutes.WEATHER_SCREEN) {
            val uiState by viewModel.uiState.collectAsState()
            val weatherData by viewModel.weatherLiveData.collectAsState()

            when (uiState) {
                UiState.INIT -> InitScreen()
                UiState.LOADING -> LoadingScreen()
                UiState.SUCCESS -> {
                    weatherData?.let {
                        WeatherScreen(
                            weatherData = it,
                            viewModel = viewModel,
                            onNavigateToCitySelection = {
                                navController.navigate(AppRoutes.CITY_SELECTION_SCREEN)
                            }
                        )
                    }
                }
                UiState.FAILED -> { }
            }
        }

        composable(
            route = AppRoutes.CITY_SELECTION_SCREEN,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            }
        ) {
            CitySelectionScreen(
                viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}