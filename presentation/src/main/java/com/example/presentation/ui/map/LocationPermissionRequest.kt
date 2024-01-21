package com.example.presentation.ui.map

import android.Manifest
import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.example.presentation.ui.MainViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermissionRequest(mainViewModel: MainViewModel) {
    val coroutineScope = rememberCoroutineScope()
    val permissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )
    when {
        permissionsState.allPermissionsGranted -> {
            mainViewModel.updateLocationPermission(true)
        }

        permissionsState.shouldShowRationale -> {
            coroutineScope.launch {
                permissionsState.launchMultiplePermissionRequest()
            }
        }

        else -> {
            mainViewModel.updateLocationPermission(false)
        }
    }
}