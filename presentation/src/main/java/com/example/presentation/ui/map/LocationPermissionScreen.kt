package com.example.presentation.ui.map

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.presentation.ui.MainViewModel

@Composable
fun MakeSnackBarForPermission(
    mainViewModel: MainViewModel,
    onLocationButtonClicked: (Boolean) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val locationPermissionGranted by mainViewModel.isLocationPermissionGranted.collectAsStateWithLifecycle(
        lifecycleOwner
    )
    if (!locationPermissionGranted) {
        ShowSnackBar("위치 권한이 거부되었습니다")
    } else {
        ShowSnackBar("위치 권한이 허용되었습니다")
    }
}

@Composable
private fun ShowSnackBar(message: String) {
    Snackbar(
        modifier = Modifier.padding(16.dp),
        action = {
        }
    ) {
        Text(message)
    }
}