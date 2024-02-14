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
import com.example.domain.util.ErrorMessage.ERROR_MESSAGE_STORE_IS_EMPTY
import com.example.presentation.mapper.toUiModel
import com.example.presentation.model.Coordinate
import com.example.presentation.model.LocationTrackingButton
import com.example.presentation.model.ScreenCoordinate
import com.example.presentation.model.StoreDetail
import com.example.presentation.model.StoreType
import com.example.presentation.ui.map.location.CurrentLocationComponent
import com.example.presentation.ui.map.marker.StoreMarker
import com.example.presentation.ui.map.reload.setReloadButtonBottomPadding
import com.example.presentation.util.MainConstants.DEFAULT_LATITUDE
import com.example.presentation.util.MainConstants.DEFAULT_LONGITUDE
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
    isMarkerClicked: Boolean,
    onBottomSheetChanged: (Boolean) -> Unit,
    onStoreInfoChanged: (StoreDetail) -> Unit,
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
    isReloadButtonClicked: Boolean,
    onGetNewScreenCoordinateChanged: (Boolean) -> Unit,
    mapViewModel: MapViewModel = hiltViewModel()
) {
    val cameraPositionState = rememberCameraPositionState {}

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
            InitializeMarker(
                this,
                onReloadButtonChanged,
                onScreenChanged,
                onReloadOrShowMoreChanged,
                onLocationButtonChanged,
                selectedLocationButton.mode,
                onCurrentMapChanged
            )
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
                                || (mapViewModel.storeInitializeState.value == INITIALIZE_DONE)
                    if (isInitializationLocation && mapViewModel.ableToShowSplashScreen.value) {
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
                    if (state.msg == ERROR_MESSAGE_STORE_IS_EMPTY) {
                        onReloadOrShowMoreChanged(false)
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

        if (isReloadButtonClicked) {
            GetScreenCoordinate(cameraPositionState, onScreenChanged)
            onGetNewScreenCoordinateChanged(true)
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
    onReloadOrShowMoreChanged: (Boolean) -> Unit,
    onLocationButtonChanged: (LocationTrackingButton) -> Unit,
    selectedLocationButtonMode: LocationTrackingMode,
    onCurrentMapChanged: (Boolean) -> Unit,
    mainViewModel: MapViewModel = hiltViewModel()
) {
    val (isInitialLocationSet, onInitialLocationSetChanged) = remember { mutableStateOf(false) }
    val (isMapGestured, onMapGestureChanged) = remember { mutableStateOf(false) }

    LaunchedEffect(cameraPositionState.isMoving) {
        if (cameraPositionState.isMoving.not() && mainViewModel.storeInitializeState.value == INITIALIZE_ABLE
            && cameraPositionState.position.target == LatLng(DEFAULT_LATITUDE, DEFAULT_LONGITUDE)
        ) {
            initializeStoreInDefaultLocation(
                onScreenChanged, mainViewModel, onInitialLocationSetChanged
            )
        }
        if (cameraPositionState.cameraUpdateReason == CameraUpdateReason.LOCATION && cameraPositionState.isMoving
            && mainViewModel.storeInitializeState.value == INITIALIZE_DEFAULT_DONE
        ) {
            checkInitialMoveByLocation(mainViewModel)
        }
        if (cameraPositionState.isMoving.not() && mainViewModel.storeInitializeState.value == INITIALIZE_MOVE_ONCE) {
            initializeStoreInCurrentLocation(mainViewModel, onInitialLocationSetChanged)
        }

        if ((mainViewModel.storeInitializeState.value == INITIALIZE_DONE || mainViewModel.storeInitializeState.value == INITIALIZE_DEFAULT_DONE)
            && cameraPositionState.isMoving
            && cameraPositionState.cameraUpdateReason == CameraUpdateReason.GESTURE
        ) {
            checkMapGestured(onReloadOrShowMoreChanged, onCurrentMapChanged, onMapGestureChanged)
        }
    }
    if (isInitialLocationSet) {
        GetScreenCoordinate(cameraPositionState, onScreenChanged)
        onInitialLocationSetChanged(false)
        onReloadButtonChanged(true)
    }

    if (isMapGestured) {
        if (selectedLocationButtonMode != LocationTrackingMode.None && selectedLocationButtonMode != LocationTrackingMode.NoFollow) {
            TurnOffLocationButton(onLocationButtonChanged)
        }
        onMapGestureChanged(false)
    }
}

private fun initializeStoreInDefaultLocation(
    onScreenChanged: (ScreenCoordinate) -> Unit,
    mainViewModel: MapViewModel,
    onInitialLocationSetChanged: (Boolean) -> Unit
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
    mainViewModel.updateStoreInitializeState(INITIALIZE_DEFAULT_DONE)
    onInitialLocationSetChanged(true)
}

private fun checkInitialMoveByLocation(mainViewModel: MapViewModel) {
    mainViewModel.updateStoreInitializeState(INITIALIZE_MOVE_ONCE)
}

private fun initializeStoreInCurrentLocation(
    mainViewModel: MapViewModel,
    onInitialLocationSetChanged: (Boolean) -> Unit
) {
    mainViewModel.updateStoreInitializeState(INITIALIZE_DONE)
    onInitialLocationSetChanged(true)
}

private fun checkMapGestured(
    onReloadOrShowMoreChanged: (Boolean) -> Unit,
    onCurrentMapChanged: (Boolean) -> Unit,
    onMapGestureChanged: (Boolean) -> Unit
) {
    onReloadOrShowMoreChanged(true)
    onCurrentMapChanged(true)
    onMapGestureChanged(true)
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