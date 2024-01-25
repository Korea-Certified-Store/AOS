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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.example.presentation.R
import com.example.presentation.ui.map.MapViewModel
import com.example.presentation.ui.map.location.LocationPermissionRequest
import com.example.presentation.ui.theme.Android_KCSTheme
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalNaverMapApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mapViewModel by viewModels<MapViewModel>()

        setContent {
            LocationPermissionRequest(mapViewModel)

            val (callStoreNumber, onCallStoreChanged) = remember { mutableStateOf("") }

            Android_KCSTheme {
                MainScreen(
                    mapViewModel,
                    onCallStoreChanged
                )
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