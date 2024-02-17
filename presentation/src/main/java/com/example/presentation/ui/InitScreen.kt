package com.example.presentation.ui

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
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

    PressBack(mapViewModel, navController)
}

@Composable
fun PressBack(mapViewModel: MapViewModel, navController: NavHostController) {
    val mapScreenType by mapViewModel.mapScreenType.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var backPressedTime = 0L

    BackHandler {
        if (mapScreenType == MapScreenType.SEARCH) {
            navController.popBackStack()
            mapViewModel.updateMapScreenType(MapScreenType.MAIN)
        } else {
            if (System.currentTimeMillis() - backPressedTime <= 2000L) {
                (context as Activity).finish()
            } else {
                Toast.makeText(context, "한 번 더 누르면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show()
            }
            backPressedTime = System.currentTimeMillis()
        }
    }
}