package com.example.presentation.ui.map.location

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.presentation.ui.theme.MediumBlue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun CreateSnackBar(data: SnackbarData) {
    Snackbar(
        actionColor = MediumBlue,
        containerColor = Color.White,
        snackbarData = data,
    )
}

fun showPermissionSnackBar(
    context: Context,
    scope: CoroutineScope,
    snackBarHost: SnackbarHostState
) {
    scope.launch {
        val snackBar = snackBarHost.showSnackbar(
            "위치 권한이 필요합니다.",
            "설정으로 이동",
            true,
            SnackbarDuration.Short
        )
        if (snackBar == SnackbarResult.ActionPerformed) {
            openAppSettings(context, context.packageName)
        }
    }
}

fun openAppSettings(context: Context, packageName: String) {
    val intent = Intent().apply {
        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        data = Uri.fromParts("package", packageName, null)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    context.startActivity(intent)
}
