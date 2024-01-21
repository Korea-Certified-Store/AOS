package com.example.presentation.ui.map

import android.app.Activity
import android.graphics.Point
import android.graphics.PointF
import android.view.Gravity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.presentation.mapper.toUiModel
import com.example.presentation.model.Coordinate
import com.example.presentation.model.LocationTrackingButton
import com.example.presentation.model.ScreenCoordinate
import com.example.presentation.model.StoreDetail
import com.example.presentation.ui.MainViewModel
import com.example.presentation.util.MainConstants.LOCATION_SIZE
import com.example.presentation.util.UiState
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.CameraUpdateReason
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.compose.rememberFusedLocationSource

@ExperimentalNaverMapApi
@Composable
fun InitMap(
    mainViewModel: MainViewModel,
    isMarkerClicked: Boolean,
    onBottomSheetChanged: (Boolean) -> Unit,
    onStoreInfoChanged: (StoreDetail) -> Unit,
    onOriginCoordinateChanged: (Coordinate) -> Unit,
    onNewCoordinateChanged: (Coordinate) -> Unit,
    onScreenChanged: (ScreenCoordinate) -> Unit,
    bottomSheetHeight: Dp,
    clickedMarkerId: Long,
    onMarkerChanged: (Long) -> Unit,
    selectedLocationButton: LocationTrackingButton,
    onLocationButtonChanged: (LocationTrackingButton) -> Unit,
    onSearchOnCurrentMapButtonChanged: (Boolean) -> Unit,
    initLocationSize: Int,
    onInitLocationChanged: (Int) -> Unit
) {
    val cameraPositionState = rememberCameraPositionState {
        onOriginCoordinateChanged(
            Coordinate(
                position.target.latitude,
                position.target.longitude
            )
        )
        onNewCoordinateChanged(
            Coordinate(
                position.target.latitude,
                position.target.longitude
            )
        )
    }

    NaverMap(
        modifier = Modifier.fillMaxSize(),
        uiSettings = MapUiSettings(
            isZoomControlEnabled = false,
            logoGravity = Gravity.BOTTOM or Gravity.END,
            logoMargin = PaddingValues(
                end = 12.dp,
                bottom = setSearchOnCurrentMapBottomPadding(isMarkerClicked, bottomSheetHeight)
            ),
            isCompassEnabled = false
        ),
        cameraPositionState = cameraPositionState.apply {
            setNewCoordinateIfGestured(this, onNewCoordinateChanged)
            TurnOffLocationButtonIfGestured(
                this,
                onLocationButtonChanged,
                onSearchOnCurrentMapButtonChanged,
                initLocationSize,
                onInitLocationChanged,
                onNewCoordinateChanged
            )
            GetScreenCoordinate(this, onScreenChanged)
        },
        locationSource = rememberFusedLocationSource(),
        properties = MapProperties(
            locationTrackingMode = selectedLocationButton.mode
        ),
        onMapClick = { _, _ ->
            onBottomSheetChanged(false)
            onMarkerChanged(-1)
        },
        onOptionChange = {
            cameraPositionState.locationTrackingMode?.let {
                selectedLocationButton.mode
            }
        },
    ) {

        val lifecycleOwner = LocalLifecycleOwner.current
        val storeDetailData by mainViewModel.storeDetailModelData.collectAsStateWithLifecycle(
            lifecycleOwner
        )
        when (val state = storeDetailData) {
            is UiState.Loading -> {
                // 로딩 중일 때의 UI
            }

            is UiState.Success -> {
                state.data.forEach { storeDetail ->
                    StoreMarker(
                        onBottomSheetChanged,
                        storeDetail.toUiModel(),
                        onStoreInfoChanged,
                        clickedMarkerId,
                        onMarkerChanged
                    )
                }
            }

            else -> {}
        }
        if (isMarkerClicked) {
            onMarkerChanged(clickedMarkerId)
        }
    }
    InitLocationButton(
        isMarkerClicked,
        selectedLocationButton,
        onLocationButtonChanged,
        mainViewModel,
        bottomSheetHeight
    )
}

@Composable
fun TurnOffLocationButtonIfGestured(
    cameraPositionState: CameraPositionState,
    onLocationButtonChanged: (LocationTrackingButton) -> Unit,
    onSearchOnCurrentMapButtonChanged: (Boolean) -> Unit,
    initLocationSize: Int,
    onInitLocationChanged: (Int) -> Unit,
    onNewCoordinateChanged: (Coordinate) -> Unit
) {
    if (cameraPositionState.cameraUpdateReason == CameraUpdateReason.GESTURE) {
        onLocationButtonChanged(LocationTrackingButton.NO_FOLLOW)
    } else if (cameraPositionState.cameraUpdateReason == CameraUpdateReason.LOCATION) {
        if (initLocationSize == LOCATION_SIZE) {
            onNewCoordinateChanged(
                Coordinate(
                    cameraPositionState.position.target.latitude,
                    cameraPositionState.position.target.longitude
                )
            )
            onSearchOnCurrentMapButtonChanged(true)
            onInitLocationChanged(initLocationSize + 1)
        } else if (initLocationSize < LOCATION_SIZE) {
            onInitLocationChanged(initLocationSize + 1)
        }
    }
}

fun setNewCoordinateIfGestured(
    cameraPositionState: CameraPositionState,
    onNewCoordinateChanged: (Coordinate) -> Unit
) {
    if (cameraPositionState.cameraUpdateReason == CameraUpdateReason.GESTURE) {
        onNewCoordinateChanged(
            Coordinate(
                cameraPositionState.position.target.latitude,
                cameraPositionState.position.target.longitude
            )
        )
    }
}

@Composable
fun GetScreenCoordinate(
    cameraPositionState: CameraPositionState,
    onScreenChanged: (ScreenCoordinate) -> Unit
) {
    val context = LocalContext.current as Activity
    val display = context.windowManager.defaultDisplay
    val size = Point()
    display.getRealSize(size)
    val width = size.x.toFloat()
    val height = size.y.toFloat()

    cameraPositionState.projection?.let {
        val northWest = it.fromScreenLocation(PointF(0f, 0f))
        val southWest = it.fromScreenLocation(PointF(0f, height))
        val southEast = it.fromScreenLocation(PointF(width, height))
        val northEast = it.fromScreenLocation(PointF(width, 0f))

        onScreenChanged(
            ScreenCoordinate(
                northWest = Coordinate(
                    northWest.latitude,
                    northWest.longitude
                ),
                southWest = Coordinate(
                    southWest.latitude,
                    southWest.longitude
                ),
                southEast = Coordinate(
                    southEast.latitude,
                    southEast.longitude
                ),
                northEast = Coordinate(
                    northEast.latitude,
                    northEast.longitude
                ),
            )
        )
    }
}