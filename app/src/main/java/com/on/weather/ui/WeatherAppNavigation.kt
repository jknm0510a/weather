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
    // 2. 建立 NavController
    val navController = rememberNavController()
    val viewModel: MainViewModel = koinViewModel()

    // 3. 設定 NavHost，並定義轉場動畫
    NavHost(
        navController = navController,
        startDestination = AppRoutes.WEATHER_SCREEN // 設定起始畫面
    ) {
        // --- 第一個畫面：天氣主畫面 ---
        composable(AppRoutes.WEATHER_SCREEN) {
            val uiState by viewModel.uiState.collectAsState()
            val weatherData by viewModel.weatherLiveData.collectAsState()

            when (uiState) {
                UiState.INIT -> InitScreen()
                UiState.LOADING -> LoadingScreen()
                UiState.SUCCESS -> {
                    weatherData?.let {
                        // 傳遞 navController 和 ViewModel
                        WeatherScreen(
                            weatherData = it,
                            viewModel = viewModel,
                            onNavigateToCitySelection = {
                                navController.navigate(AppRoutes.CITY_SELECTION_SCREEN)
                            }
                        )
                    }
                }
                UiState.FAILED -> { /* 處理錯誤畫面 */ }
            }
        }

        // --- 第二個畫面：城市選擇畫面 ---
        composable(
            route = AppRoutes.CITY_SELECTION_SCREEN,
            // 4. *** 核心：定義進入和退出的動畫 ***
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500) // 動畫時長 500 毫秒
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
                onNavigateBack = {
                    navController.popBackStack() // 返回上一個畫面
                }
            )
        }
    }
}