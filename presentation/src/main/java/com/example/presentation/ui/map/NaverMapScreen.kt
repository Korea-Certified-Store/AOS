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
import com.example.presentation.model.StoreType
import com.example.presentation.ui.map.location.CurrentLocationComponent
import com.example.presentation.ui.map.marker.StoreMarker
import com.example.presentation.ui.map.reload.setReloadButtonBottomPadding
import com.example.presentation.util.MainConstants.GREAT_STORE
import com.example.presentation.util.MainConstants.KIND_STORE
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
fun NaverMapScreen(
    mapViewModel: MapViewModel,
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
                bottom = setReloadButtonBottomPadding(isMarkerClicked, bottomSheetHeight)
            ),
            isCompassEnabled = false
        ),
        cameraPositionState = cameraPositionState.apply {
            turnOffLocationButtonIfGestured(this, onLocationButtonChanged)
            setNewCoordinateIfGestured(this, onNewCoordinateChanged)
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
        val storeDetailData by mapViewModel.storeDetailModelData.collectAsStateWithLifecycle(
            lifecycleOwner
        )
        when (val state = storeDetailData) {
            is UiState.Loading -> {
                // 로딩 중일 때의 UI
            }

            is UiState.Success -> {
                FilteredMarkers(
                    state,
                    mapViewModel,
                    onBottomSheetChanged,
                    onStoreInfoChanged,
                    clickedMarkerId,
                    onMarkerChanged
                )
            }

            else -> {}
        }
        if (isMarkerClicked) {
            onMarkerChanged(clickedMarkerId)
        }
    }
    CurrentLocationComponent(
        isMarkerClicked,
        selectedLocationButton,
        onLocationButtonChanged,
        mapViewModel,
        bottomSheetHeight
    )
}

@ExperimentalNaverMapApi
@Composable
private fun FilteredMarkers(
    state: UiState.Success<List<com.example.domain.model.map.StoreDetail>>,
    mapViewModel: MapViewModel,
    onBottomSheetChanged: (Boolean) -> Unit,
    onStoreInfoChanged: (StoreDetail) -> Unit,
    clickedMarkerId: Long,
    onMarkerChanged: (Long) -> Unit,
) {
    state.data.filter { storeInfo ->
        mapViewModel.getFilterSet().intersect(storeInfo.certificationName.toSet()).isNotEmpty()
    }.forEach { storeInfo ->
        val storeType =
            when (mapViewModel.getFilterSet().intersect(storeInfo.certificationName.toSet())
                .last()) {
                KIND_STORE -> StoreType.KIND
                GREAT_STORE -> StoreType.GREAT
                else -> StoreType.SAFE
            }
        StoreMarker(
            onBottomSheetChanged,
            storeInfo.toUiModel(),
            onStoreInfoChanged,
            clickedMarkerId,
            onMarkerChanged,
            storeType
        )
    }
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