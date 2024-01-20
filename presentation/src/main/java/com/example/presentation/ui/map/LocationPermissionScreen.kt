package com.example.presentation.ui.map

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.presentation.ui.MainViewModel
import kotlinx.coroutines.launch

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

@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun ShowSnackBarWhenPushButton(mainViewModel: MainViewModel,
                               onLocationButtonClicked: (Boolean) -> Unit) {
    val cScope = rememberCoroutineScope()
    val snackBarHost = remember { SnackbarHostState() }
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .padding(top = 50.dp, start = 15.dp)
    ) {
        Button(onClick = {
            cScope.launch {
                val snackBar =
                    snackBarHost.showSnackbar(
                        "권한 여부 : ${mainViewModel.isLocationPermissionGranted.value}",
                        "권한 설정",
                        true,
                        SnackbarDuration.Short
                    )
                when (snackBar) {
                    SnackbarResult.ActionPerformed -> {}
                    SnackbarResult.Dismissed -> {}
                }
            }
        }) {
            Text("스낵바 보이기")
        }
        SnackbarHost(
            hostState = snackBarHost,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 50.dp)
        )
    }

    onLocationButtonClicked(false)
}

@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
fun ShowSnackBarInCScope(mainViewModel: MainViewModel) {
    val cScope = rememberCoroutineScope()
    val snackBarHost = remember { SnackbarHostState() }
    cScope.launch {
        val snackBar =
            snackBarHost.showSnackbar(
                "권한 여부 : ${mainViewModel.isLocationPermissionGranted.value}",
                "권한 설정",
                true,
                SnackbarDuration.Short
            )
        when (snackBar) {
            SnackbarResult.ActionPerformed -> {}
            SnackbarResult.Dismissed -> {}
        }
    }
}