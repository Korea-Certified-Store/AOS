package com.example.presentation.ui.map

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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

@Composable
fun InitLocationButton(
    isMarkerClicked: Boolean,
    selectedLocationMode: Pair<Int, LocationTrackingMode>,
    onLocationModeChanged: (Pair<Int, LocationTrackingMode>) -> Unit,
    onLocationButtonClicked: (Boolean) -> Unit,
    mainViewModel: MainViewModel,
    ) {
    val isFollow = remember { mutableStateOf(true) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(
                start = 12.dp,
                bottom = getBottomPaddingByMarkerStatus(isMarkerClicked)
            ),
        verticalArrangement = Arrangement.Bottom,
    ) {
        Button(
            modifier = Modifier
                .defaultMinSize(1.dp)
                .shadow(1.dp, CircleShape),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            ),
            onClick = {
                mainViewModel.checkAndUpdatePermission(context)
                onLocationButtonClicked(true)
                onLocationModeChanged(getTrackingModePair(isFollow))
            },
        ) {
            Image(
                painter = painterResource(id = selectedLocationMode.first),
                contentDescription = "location button",
            )
        }
    }
}

@Composable
private fun getBottomPaddingByMarkerStatus(isMarkerClicked: Boolean) =
    if (isMarkerClicked) (BOTTOM_SHEET_HEIGHT_ON + SEARCH_ON_CURRENT_MAP_BUTTON_DEFAULT_PADDING).dp else (BOTTOM_SHEET_HEIGHT_OFF + SEARCH_ON_CURRENT_MAP_BUTTON_DEFAULT_PADDING + 11).dp

fun getTrackingModePair(isFollow: MutableState<Boolean>): Pair<Int, LocationTrackingMode> {
    isFollow.value = !isFollow.value
    return when (isFollow.value) {
        true -> Pair(R.drawable.icon_follow, LocationTrackingMode.Follow)
        false -> Pair(R.drawable.icon_face, LocationTrackingMode.Face)
    }
}