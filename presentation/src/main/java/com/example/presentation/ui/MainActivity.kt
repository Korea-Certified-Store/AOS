package com.example.presentation.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.presentation.R
import com.example.presentation.ui.map.MapViewModel
import com.example.presentation.ui.navigation.Screen
import com.example.presentation.ui.search.SearchScreen
import com.example.presentation.ui.theme.Android_KCSTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mapViewModel by viewModels<MapViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val (callStoreNumber, onCallStoreChanged) = remember { mutableStateOf("") }
            val (isSplashScreenShowAble, onSplashScreenShowAble) = remember { mutableStateOf(true) }
            val (isOnboardingScreenShowAble, onOnboardingScreenShowAble) = remember {
                mutableStateOf(
                    true
                )
            }
            val context = LocalContext.current
            val preferences = context.getSharedPreferences("KCS_Onboarding", Context.MODE_PRIVATE)
            val isFirstRun = preferences.getBoolean("isFirstRun", true)

            Android_KCSTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Screen.Main.route,
                    enterTransition = {
                        EnterTransition.None
                    },
                    exitTransition = {
                        ExitTransition.None
                    }
                ) {
                    composable(
                        route = Screen.Main.route
                    ) {
                        InitScreen(
                            onCallStoreChanged,
                            onSplashScreenShowAble,
                            navController,
                            isFirstRun,
                            isOnboardingScreenShowAble,
                            onOnboardingScreenShowAble,
                            isSplashScreenShowAble,
                            mapViewModel
                        )
                    }
                    composable(
                        route = Screen.Search.route
                    ) {
                        SearchScreen(navController, mapViewModel)
                    }
                }
            }

            if (callStoreNumber.isNotEmpty()) {
                callStore(callStoreNumber)
                onCallStoreChanged("")
            }
        }

        if (isLocationPermissionGranted(this)) {
            lifecycleScope.launch {
                mapViewModel.updateLocationPermission(true)
            }
        }
    }

    private fun callStore(storeNumber: String) {
        startActivity(
            Intent(
                "android.intent.action.DIAL",
                Uri.parse(String.format(resources.getString(R.string.tel), storeNumber))
            )
        )
    }

    private fun isLocationPermissionGranted(context: Context) =
        !(ActivityCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED)
}