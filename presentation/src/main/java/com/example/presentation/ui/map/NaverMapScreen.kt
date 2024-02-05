package com.example.presentation.ui.map

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Point
import android.graphics.PointF
import android.view.Gravity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.presentation.util.MainConstants.LOCATION_SIZE
import com.example.presentation.util.MainConstants.UN_MARKER
import com.example.presentation.util.UiState
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.CameraUpdateReason
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.LocationTrackingMode
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.compose.rememberFusedLocationSource

@SuppressLint("StateFlowValueCalledInComposition")
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
    currentSummaryInfoHeight: Dp,
    clickedMarkerId: Long,
    onMarkerChanged: (Long) -> Unit,
    selectedLocationButton: LocationTrackingButton,
    onLocationButtonChanged: (LocationTrackingButton) -> Unit,
    onReloadButtonChanged: (Boolean) -> Unit,
    initLocationSize: Int,
    onInitLocationChanged: (Int) -> Unit,
    screenCoordinate: ScreenCoordinate,
    onSplashScreenShowAble: (Boolean) -> Unit,
    onLoadingChanged: (Boolean) -> Unit,
    onCurrentMapChanged: (Boolean) -> Unit,
    isFilteredMarker: Boolean,
    onFilteredMarkerChanged: (Boolean) -> Unit,
    onErrorSnackBarChanged: (String) -> Unit,
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
                bottom = setReloadButtonBottomPadding(isMarkerClicked, currentSummaryInfoHeight)
            ),
            isCompassEnabled = false
        ),
        cameraPositionState = cameraPositionState.apply {
            setNewCoordinateIfGestured(this, onNewCoordinateChanged)
            if (cameraPositionState.cameraUpdateReason == CameraUpdateReason.GESTURE
                && selectedLocationButton.mode != LocationTrackingMode.None
                && selectedLocationButton.mode != LocationTrackingMode.NoFollow
            ) {
                TurnOffLocationButton(onLocationButtonChanged)
            } else {
                InitializeMarker(
                    this,
                    onReloadButtonChanged,
                    initLocationSize,
                    onInitLocationChanged,
                    onNewCoordinateChanged,
                    mapViewModel,
                    selectedLocationButton,
                    screenCoordinate
                )
            }
            GetScreenCoordinate(this, onScreenChanged)
        },
        locationSource = rememberFusedLocationSource(),
        properties = MapProperties(
            locationTrackingMode = selectedLocationButton.mode
        ),
        onMapClick = { _, _ ->
            onBottomSheetChanged(false)
            onMarkerChanged(UN_MARKER)
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

        LaunchedEffect(key1 = storeDetailData) {
            when (val state = storeDetailData) {
                is UiState.Loading -> {
                    if (mapViewModel.ableToShowSplashScreen.value.not()) {
                        onLoadingChanged(true)
                    }
                }

                is UiState.Success -> {
                    if (mapViewModel.ableToShowSplashScreen.value) {
                        onSplashScreenShowAble(false)
                    }
                    onFilteredMarkerChanged(true)
                    onLoadingChanged(false)
                    onCurrentMapChanged(false)
                }

                is UiState.Failure -> {
                    if (mapViewModel.ableToShowSplashScreen.value) {
                        onSplashScreenShowAble(false)
                    }
                    onLoadingChanged(false)
                    onErrorSnackBarChanged(state.msg)
                }
            }
        }

        if (isFilteredMarker) {
            FilteredMarkers(
                (storeDetailData as UiState.Success).data.first(),
                mapViewModel,
                onBottomSheetChanged,
                onStoreInfoChanged,
                clickedMarkerId,
                onMarkerChanged
            )
        }
    }

    CurrentLocationComponent(
        isMarkerClicked,
        selectedLocationButton,
        onLocationButtonChanged,
        mapViewModel,
        currentSummaryInfoHeight
    )
}

@Composable
private fun TurnOffLocationButton(onLocationButtonChanged: (LocationTrackingButton) -> Unit) {
    onLocationButtonChanged(LocationTrackingButton.NO_FOLLOW)
}

@ExperimentalNaverMapApi
@Composable
fun FilteredMarkers(
    storeInfo: List<com.example.domain.model.map.StoreDetail>,
    mapViewModel: MapViewModel,
    onBottomSheetChanged: (Boolean) -> Unit,
    onStoreInfoChanged: (StoreDetail) -> Unit,
    clickedMarkerId: Long,
    onMarkerChanged: (Long) -> Unit,
) {
    storeInfo.filter { info ->
        mapViewModel.getFilterSet().intersect(info.certificationName.toSet()).isNotEmpty()
    }.forEach { info ->
        val storeType =
            when (mapViewModel.getFilterSet().intersect(info.certificationName.toSet())
                .last()) {
                KIND_STORE -> StoreType.KIND
                GREAT_STORE -> StoreType.GREAT
                else -> StoreType.SAFE
            }
        StoreMarker(
            onBottomSheetChanged,
            info.toUiModel(),
            onStoreInfoChanged,
            clickedMarkerId,
            onMarkerChanged,
            storeType
        )
    }
}

@Composable
fun InitializeMarker(
    cameraPositionState: CameraPositionState,
    onReloadButtonChanged: (Boolean) -> Unit,
    initLocationSize: Int,
    onInitLocationChanged: (Int) -> Unit,
    onNewCoordinateChanged: (Coordinate) -> Unit,
    mainViewModel: MapViewModel,
    selectedLocationButton: LocationTrackingButton,
    screenCoordinate: ScreenCoordinate,
) {
    if (cameraPositionState.cameraUpdateReason == CameraUpdateReason.LOCATION) {
        if (initLocationSize == LOCATION_SIZE) {
            onNewCoordinateChanged(
                Coordinate(
                    cameraPositionState.position.target.latitude,
                    cameraPositionState.position.target.longitude
                )
            )
            onReloadButtonChanged(true)
            onInitLocationChanged(initLocationSize + 1)
        } else if (initLocationSize < LOCATION_SIZE) {
            onInitLocationChanged(initLocationSize + 1)
        }
        mainViewModel.ableToShowInitialMarker = false
    } else if (
        screenCoordinate != ScreenCoordinate(
            Coordinate(0.0, 0.0),
            Coordinate(0.0, 0.0),
            Coordinate(0.0, 0.0),
            Coordinate(0.0, 0.0)
        )
        && selectedLocationButton == LocationTrackingButton.NONE && mainViewModel.ableToShowInitialMarker
    ) {
        onReloadButtonChanged(true)
        mainViewModel.ableToShowInitialMarker = false
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