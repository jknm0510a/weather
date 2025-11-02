package com.on.weather.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.on.weather.R
import com.on.weather.data.UiState
import com.on.weather.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitySelectionScreen(
    uiState: UiState,
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.fetchCities()
    }
    val cities by viewModel.citiesLiveData.collectAsState()
    val errorMessage by viewModel.errorMessageLiveData.collectAsState()

    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.bg_full_select_city),
            contentDescription = "Background Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        cities?.let { data ->
            CitySelectionView(
                data,
                viewModel,
                onNavigateBack,
            )
        }

        if (uiState == UiState.LOADING) {
            Box(modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
            )
            CircularProgressIndicator(color = Color.White)
        } else if (uiState == UiState.FAILED) {
            errorMessage?.apply {
                Toast.makeText(LocalContext.current, "code:${code} message:${message}", Toast.LENGTH_SHORT).show()
            }
        }
    }



}