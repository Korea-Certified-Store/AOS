package com.example.presentation.ui.map

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
import androidx.compose.ui.unit.dp
import com.example.presentation.model.LocationTrackingButton
import com.example.presentation.ui.MainViewModel
import com.example.presentation.util.MainConstants.BOTTOM_SHEET_HEIGHT_OFF
import com.example.presentation.util.MainConstants.BOTTOM_SHEET_HEIGHT_ON
import com.example.presentation.util.MainConstants.SEARCH_ON_CURRENT_MAP_BUTTON_DEFAULT_PADDING
import kotlinx.coroutines.CoroutineScope

@Composable
fun InitLocationButton(
    isMarkerClicked: Boolean,
    selectedLocationButton: LocationTrackingButton,
    onLocationButtonChanged: (LocationTrackingButton) -> Unit,
    mainViewModel: MainViewModel,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackBarHost = remember { SnackbarHostState() }
    val bottomPadding = getBottomPaddingByMarkerStatus(isMarkerClicked)

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(
                bottom = bottomPadding
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
        CreateLocationButton(
            context,
            selectedLocationButton,
            onLocationButtonChanged,
            mainViewModel,
            scope,
            snackBarHost,
        )

    }
}

@Composable
private fun getBottomPaddingByMarkerStatus(isMarkerClicked: Boolean) =
    if (isMarkerClicked) (BOTTOM_SHEET_HEIGHT_ON + SEARCH_ON_CURRENT_MAP_BUTTON_DEFAULT_PADDING).dp
    else (BOTTOM_SHEET_HEIGHT_OFF + SEARCH_ON_CURRENT_MAP_BUTTON_DEFAULT_PADDING + 11).dp

@Composable
fun CreateLocationButton(
    context: Context,
    selectedLocationButton: LocationTrackingButton,
    onLocationButtonChanged: (LocationTrackingButton) -> Unit,
    mainViewModel: MainViewModel,
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
            selectedLocationButton(
                context,
                onLocationButtonChanged,
                mainViewModel,
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

fun selectedLocationButton(
    context: Context,
    onLocationButtonChanged: (LocationTrackingButton) -> Unit,
    mainViewModel: MainViewModel,
    scope: CoroutineScope,
    snackBarHost: SnackbarHostState,
    selectedLocationButton: LocationTrackingButton
) {
    mainViewModel.checkAndUpdatePermission(context)
    if (!mainViewModel.isLocationPermissionGranted.value && snackBarHost.currentSnackbarData == null) {
        showPermissionSnackBar(context, scope, snackBarHost)
    }
    onLocationButtonChanged(changeLocationTrackingMode(selectedLocationButton, mainViewModel))
}

fun changeLocationTrackingMode(
    selectedLocationButton: LocationTrackingButton, mainViewModel: MainViewModel
): LocationTrackingButton {
    return when (selectedLocationButton) {
        LocationTrackingButton.NONE -> {
            mainViewModel.getInitialLocationTrackingMode()
        }

        LocationTrackingButton.NO_FOLLOW -> LocationTrackingButton.FOLLOW
        LocationTrackingButton.FOLLOW -> LocationTrackingButton.FACE
        LocationTrackingButton.FACE -> LocationTrackingButton.FOLLOW
    }
}