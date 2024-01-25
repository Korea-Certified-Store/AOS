package com.example.presentation.ui.map.location

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.presentation.model.LocationTrackingButton
import com.example.presentation.ui.map.MapViewModel
import com.example.presentation.ui.map.reload.setReloadButtonBottomPadding
import kotlinx.coroutines.CoroutineScope

@Composable
fun CurrentLocationComponent(
    isMarkerClicked: Boolean,
    selectedLocationButton: LocationTrackingButton,
    onLocationButtonChanged: (LocationTrackingButton) -> Unit,
    mapViewModel: MapViewModel,
    bottomSheetHeight: Dp
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackBarHost = remember { SnackbarHostState() }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(
                bottom = setReloadButtonBottomPadding(isMarkerClicked, bottomSheetHeight)
            ),
        verticalArrangement = Arrangement.Bottom,
    ) {
        SnackbarHost(
            hostState = snackBarHost,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .background(Color.Transparent),
        ) { data ->
            CreateSnackBar(data)
        }
        CurrentLocationButton(
            context,
            selectedLocationButton,
            onLocationButtonChanged,
            mapViewModel,
            scope,
            snackBarHost,
        )

    }
}

@Composable
fun CurrentLocationButton(
    context: Context,
    selectedLocationButton: LocationTrackingButton,
    onLocationButtonChanged: (LocationTrackingButton) -> Unit,
    mapViewModel: MapViewModel,
    scope: CoroutineScope,
    snackBarHost: SnackbarHostState,
) {
    Button(
        modifier = Modifier
            .padding(start = 12.dp)
            .defaultMinSize(1.dp)
            .shadow(1.dp, CircleShape),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        onClick = {
            handleLocationButtonSelection(
                context,
                onLocationButtonChanged,
                mapViewModel,
                scope,
                snackBarHost,
                selectedLocationButton
            )
        },
    ) {
        Image(
            painter = painterResource(id = selectedLocationButton.img),
            contentDescription = "location button",
        )
    }
}

fun handleLocationButtonSelection(
    context: Context,
    onLocationButtonChanged: (LocationTrackingButton) -> Unit,
    mapViewModel: MapViewModel,
    scope: CoroutineScope,
    snackBarHost: SnackbarHostState,
    selectedLocationButton: LocationTrackingButton
) {
    mapViewModel.checkAndUpdatePermission(context)
    if (!mapViewModel.isLocationPermissionGranted.value && snackBarHost.currentSnackbarData == null) {
        showPermissionSnackBar(context, scope, snackBarHost)
    }
    onLocationButtonChanged(changeLocationTrackingMode(selectedLocationButton, mapViewModel))
}

fun changeLocationTrackingMode(
    selectedLocationButton: LocationTrackingButton, mapViewModel: MapViewModel
): LocationTrackingButton {
    return when (selectedLocationButton) {
        LocationTrackingButton.NONE -> {
            mapViewModel.getInitialLocationTrackingMode()
        }

        LocationTrackingButton.NO_FOLLOW -> LocationTrackingButton.FOLLOW
        LocationTrackingButton.FOLLOW -> LocationTrackingButton.FACE
        LocationTrackingButton.FACE -> LocationTrackingButton.FOLLOW
    }
}