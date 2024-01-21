package com.example.presentation.ui.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.presentation.mapper.toUiModel
import com.example.presentation.model.Coordinate
import com.example.presentation.model.LocationTrackingButton
import com.example.presentation.model.ScreenCoordinate
import com.example.presentation.model.StoreDetail
import com.example.presentation.ui.MainViewModel
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
    clickedStoreDetail: StoreDetail,
    onStoreInfoChanged: (StoreDetail) -> Unit,
    onOriginCoordinateChanged: (Coordinate) -> Unit,
    onNewCoordinateChanged: (Coordinate) -> Unit,
    onScreenChanged: (ScreenCoordinate) -> Unit,
    selectedLocationButton: LocationTrackingButton,
    onLocationButtonChanged: (LocationTrackingButton) -> Unit,
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
        uiSettings = MapUiSettings(isZoomControlEnabled = false, isCompassEnabled = false),
        cameraPositionState = cameraPositionState.apply {
            turnOffLocationButtonIfGestured(this, onLocationButtonChanged)
            setNewCoordinateIfGestured(this, onNewCoordinateChanged)
            getScreenCoordinate(this, onScreenChanged)
        },
        locationSource = rememberFusedLocationSource(),
        properties = MapProperties(
            locationTrackingMode = selectedLocationButton.mode
        ),
        onMapClick = { _, _ ->
            onBottomSheetChanged(false)
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
                state.data.forEach { storeInfo ->
                    StoreMarker(
                        onBottomSheetChanged,
                        storeInfo.toUiModel(),
                        onStoreInfoChanged
                    )
                }
            }

            else -> {}
        }
        if (isMarkerClicked) {
            ClickedStoreMarker(clickedStoreDetail)
        }
    }
    InitLocationButton(
        isMarkerClicked,
        selectedLocationButton,
        onLocationButtonChanged,
        mainViewModel
    )
}

fun turnOffLocationButtonIfGestured(
    cameraPositionState: CameraPositionState,
    onLocationButtonChanged: (LocationTrackingButton) -> Unit
) {
    if (cameraPositionState.cameraUpdateReason == CameraUpdateReason.GESTURE) {
        onLocationButtonChanged(LocationTrackingButton.NO_FOLLOW)
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

fun getScreenCoordinate(
    cameraPositionState: CameraPositionState,
    onScreenChanged: (ScreenCoordinate) -> Unit
) {
    cameraPositionState.contentBounds?.let {
        onScreenChanged(
            ScreenCoordinate(
                northWest = Coordinate(
                    it.northWest.latitude,
                    it.northWest.longitude
                ),
                southWest = Coordinate(
                    it.southWest.latitude,
                    it.southWest.longitude
                ),
                southEast = Coordinate(
                    it.southEast.latitude,
                    it.southEast.longitude
                ),
                northEast = Coordinate(
                    it.northEast.latitude,
                    it.northEast.longitude
                ),
            )
        )
    }
}