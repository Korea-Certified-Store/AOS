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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.presentation.R
import com.example.presentation.ui.MainViewModel
import com.example.presentation.util.MainConstants.BOTTOM_SHEET_HEIGHT_OFF
import com.example.presentation.util.MainConstants.BOTTOM_SHEET_HEIGHT_ON
import com.example.presentation.util.MainConstants.SEARCH_ON_CURRENT_MAP_BUTTON_DEFAULT_PADDING
import com.naver.maps.map.compose.LocationTrackingMode
import kotlinx.coroutines.CoroutineScope

@Composable
fun InitLocationButton(
    isMarkerClicked: Boolean,
    selectedLocationMode: Pair<Int, LocationTrackingMode>,
    onLocationModeChanged: (Pair<Int, LocationTrackingMode>) -> Unit,
    mainViewModel: MainViewModel,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackBarHost = remember { SnackbarHostState() }
    val isFollow = remember { mutableStateOf(true) }
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
            isFollow,
            onLocationModeChanged,
            mainViewModel,
            scope,
            snackBarHost,
            selectedLocationMode.first
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
    isFollow: MutableState<Boolean>,
    onLocationModeChanged: (Pair<Int, LocationTrackingMode>) -> Unit,
    mainViewModel: MainViewModel,
    scope: CoroutineScope,
    snackBarHost: SnackbarHostState,
    selectedLocationMode: Int
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
            handleButtonClick(
                context,
                isFollow,
                onLocationModeChanged,
                mainViewModel,
                scope,
                snackBarHost
            )
        },
    ) {
        Image(
            painter = painterResource(id = selectedLocationMode),
            contentDescription = "location button",
        )
    }
}

fun handleButtonClick(
    context: Context,
    isFollow: MutableState<Boolean>,
    onLocationModeChanged: (Pair<Int, LocationTrackingMode>) -> Unit,
    mainViewModel: MainViewModel,
    scope: CoroutineScope,
    snackBarHost: SnackbarHostState
) {
    mainViewModel.checkAndUpdatePermission(context)
    if (!mainViewModel.isLocationPermissionGranted.value && snackBarHost.currentSnackbarData == null) {
        showPermissionSnackBar(context, scope, snackBarHost)
    }
    onLocationModeChanged(getTrackingModePair(isFollow))
}


fun getTrackingModePair(isFollow: MutableState<Boolean>): Pair<Int, LocationTrackingMode> {
    isFollow.value = !isFollow.value
    return when (isFollow.value) {
        true -> Pair(R.drawable.icon_follow, LocationTrackingMode.Follow)
        false -> Pair(R.drawable.icon_face, LocationTrackingMode.Face)
    }
}