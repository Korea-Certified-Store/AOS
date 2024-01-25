package com.example.presentation.ui.map.location

import android.Manifest
import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.example.presentation.ui.map.MapViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermissionRequest(mapViewModel: MapViewModel) {
    val coroutineScope = rememberCoroutineScope()
    val permissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )
    when {
        permissionsState.allPermissionsGranted -> {
            mapViewModel.updateLocationPermission(true)
        }

        permissionsState.shouldShowRationale -> {
            coroutineScope.launch {
                permissionsState.launchMultiplePermissionRequest()
            }
        }

        else -> {
            mapViewModel.updateLocationPermission(false)
        }
    }
}