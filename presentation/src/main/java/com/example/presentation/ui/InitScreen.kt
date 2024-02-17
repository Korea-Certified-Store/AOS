package com.example.presentation.ui

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.presentation.ui.map.MapViewModel
import com.example.presentation.ui.map.location.LocationPermissionRequest
import com.example.presentation.ui.onboarding.OnboardingScreen
import com.example.presentation.ui.splash.SplashScreen
import com.example.presentation.util.MapScreenType
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import kotlinx.coroutines.delay

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun InitScreen(
    onCallStoreChanged: (String) -> Unit,
    onSplashScreenShowAble: (Boolean) -> Unit,
    navController: NavHostController,
    searchText: String?,
    isFirstRun: Boolean,
    isOnboardingScreenShowAble: Boolean,
    onOnboardingScreenShowAble: (Boolean) -> Unit,
    isSplashScreenShowAble: Boolean,
    mapViewModel: MapViewModel
) {
    LocationPermissionRequest(mapViewModel)

    MainScreen(
        onCallStoreChanged,
        onSplashScreenShowAble,
        navController,
        searchText,
        mapViewModel
    )

    if (isFirstRun) {
        if (isOnboardingScreenShowAble) {
            OnboardingScreen(onOnboardingScreenShowAble)
        } else {
            LocationPermissionRequest(mapViewModel)
        }
        if (isSplashScreenShowAble) {
            SplashScreen()
        } else {
            mapViewModel.updateSplashState()
        }
        LaunchedEffect(Unit) {
            delay(3000L)
            onSplashScreenShowAble(false)
        }
    } else {
        if (isSplashScreenShowAble) {
            SplashScreen()
        } else {
            mapViewModel.updateSplashState()
        }
        LocationPermissionRequest(mapViewModel)
    }

    val mapScreenType by mapViewModel.mapScreenType.collectAsStateWithLifecycle()

    BackHandler {
        if (mapScreenType == MapScreenType.SEARCH) {
            navController.popBackStack()
            mapViewModel.updateMapScreenType(MapScreenType.MAIN)
        }
    }
}