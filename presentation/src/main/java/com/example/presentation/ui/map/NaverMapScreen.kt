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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.domain.model.map.ShowMoreCount
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
import com.example.presentation.util.MainConstants.INITIALIZE_ABLE
import com.example.presentation.util.MainConstants.INITIALIZE_DEFAULT_DONE
import com.example.presentation.util.MainConstants.INITIALIZE_DONE
import com.example.presentation.util.MainConstants.INITIALIZE_MOVE_ONCE
import com.example.presentation.util.MainConstants.KIND_STORE
import com.example.presentation.util.MainConstants.UN_MARKER
import com.example.presentation.util.UiState
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.CameraUpdateReason
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.LocationTrackingMode
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.compose.rememberFusedLocationSource
import kotlinx.coroutines.launch


@SuppressLint("StateFlowValueCalledInComposition", "CoroutineCreationDuringComposition")
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
    onSplashScreenShowAble: (Boolean) -> Unit,
    onLoadingChanged: (Boolean) -> Unit,
    onCurrentMapChanged: (Boolean) -> Unit,
    isFilteredMarker: Boolean,
    onFilteredMarkerChanged: (Boolean) -> Unit,
    onErrorSnackBarChanged: (String) -> Unit,
    isListItemClicked: Boolean,
    onListItemChanged: (Boolean) -> Unit,
    clickedStoreLocation: Coordinate,
    onShowMoreCountChanged: (ShowMoreCount) -> Unit,
    onReloadOrShowMoreChanged: (Boolean) -> Unit,
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

    val scope = rememberCoroutineScope()

    NaverMap(
        modifier = Modifier.fillMaxSize(),
        uiSettings = MapUiSettings(
            isZoomControlEnabled = false,
            logoGravity = Gravity.BOTTOM or Gravity.END,
            logoMargin = PaddingValues(
                end = 12.dp,
                bottom = setReloadButtonBottomPadding(isMarkerClicked, currentSummaryInfoHeight)
            ),
            isScaleBarEnabled = false
        ),
        cameraPositionState = cameraPositionState.apply {
            setNewCoordinateAndShowReloadButtonIfGestured(
                this,
                onNewCoordinateChanged,
                onReloadOrShowMoreChanged
            )
            if (cameraPositionState.cameraUpdateReason == CameraUpdateReason.GESTURE
                && selectedLocationButton.mode != LocationTrackingMode.None
                && selectedLocationButton.mode != LocationTrackingMode.NoFollow
            ) {
                TurnOffLocationButton(onLocationButtonChanged)
            } else {
                InitializeMarker(
                    this,
                    onReloadButtonChanged,
                    onScreenChanged,
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
                    val isInitializationLocation =
                        mapViewModel.isLocationPermissionGranted.value.not()
                                || (mapViewModel.storeInitializeState == INITIALIZE_DONE)
                    if (isInitializationLocation && mapViewModel.ableToShowSplashScreen.value && state.data.isNotEmpty()) {
                        onSplashScreenShowAble(false)
                    }
                    onFilteredMarkerChanged(true)
                    onLoadingChanged(false)
                    onCurrentMapChanged(false)
                    onShowMoreCountChanged(ShowMoreCount(0, state.data.size))
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
                mapViewModel.flattenedStoreDetailList.value,
                mapViewModel,
                onBottomSheetChanged,
                onStoreInfoChanged,
                clickedMarkerId,
                onMarkerChanged
            )
        }

        if (isListItemClicked) {
            scope.launch {
                cameraPositionState.animate(
                    CameraUpdate.scrollTo(
                        LatLng(
                            clickedStoreLocation.latitude,
                            clickedStoreLocation.longitude
                        )
                    ), CameraAnimation.Easing, 1000
                )
            }
            onListItemChanged(false)
            if (selectedLocationButton == LocationTrackingButton.FOLLOW || selectedLocationButton == LocationTrackingButton.FACE) {
                onLocationButtonChanged(LocationTrackingButton.NO_FOLLOW)
            }
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
    onScreenChanged: (ScreenCoordinate) -> Unit,
    mainViewModel: MapViewModel = hiltViewModel()
) {
    val (initialLocationSetting, onInitialLocationSetting) = remember { mutableStateOf(false) }
    LaunchedEffect(cameraPositionState.isMoving) {
        if (cameraPositionState.isMoving.not() && mainViewModel.storeInitializeState == INITIALIZE_ABLE
            && cameraPositionState.position.target == LatLng(37.5666102, 126.9783881)
        ) {
            initializeStoreInDefaultLocation(
                onScreenChanged, mainViewModel, onInitialLocationSetting
            )
        }
        if (cameraPositionState.cameraUpdateReason == CameraUpdateReason.LOCATION && cameraPositionState.isMoving && mainViewModel.storeInitializeState == INITIALIZE_DEFAULT_DONE) {
            checkInitialMoveByLocation(mainViewModel)
        }
        if (cameraPositionState.isMoving.not() && mainViewModel.storeInitializeState == INITIALIZE_MOVE_ONCE) {
            initializeStoreInCurrentLocation(mainViewModel, onInitialLocationSetting)
        }
    }
    if (initialLocationSetting) {
        GetScreenCoordinate(cameraPositionState, onScreenChanged)
        onInitialLocationSetting(false)
        onReloadButtonChanged(true)
    }
}

private fun initializeStoreInDefaultLocation(
    onScreenChanged: (ScreenCoordinate) -> Unit,
    mainViewModel: MapViewModel,
    onInitialLocationSetting: (Boolean) -> Unit
) {
    onScreenChanged(
        ScreenCoordinate(
            northWest = Coordinate(
                37.57985850148559,
                126.97014835366224
            ),
            southWest = Coordinate(
                37.550844553375555,
                126.97014835366224
            ),
            southEast = Coordinate(
                37.550844553375555,
                126.98662784633694
            ),
            northEast = Coordinate(
                37.57985850148559,
                126.98662784633694
            ),
        )
    )
    mainViewModel.storeInitializeState = INITIALIZE_DEFAULT_DONE
    onInitialLocationSetting(true)
}

private fun checkInitialMoveByLocation(mainViewModel: MapViewModel) {
    mainViewModel.storeInitializeState = INITIALIZE_MOVE_ONCE
}

private fun initializeStoreInCurrentLocation(
    mainViewModel: MapViewModel,
    onInitialLocationSetting: (Boolean) -> Unit
) {
    mainViewModel.storeInitializeState = INITIALIZE_DONE
    onInitialLocationSetting(true)
}

fun setNewCoordinateAndShowReloadButtonIfGestured(
    cameraPositionState: CameraPositionState,
    onNewCoordinateChanged: (Coordinate) -> Unit,
    onReloadOrShowMoreChanged: (Boolean) -> Unit
) {
    if (cameraPositionState.cameraUpdateReason == CameraUpdateReason.GESTURE) {
        onReloadOrShowMoreChanged(true)
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