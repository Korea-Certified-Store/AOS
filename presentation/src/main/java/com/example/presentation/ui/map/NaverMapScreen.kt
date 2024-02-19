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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.domain.model.map.ShowMoreCount
import com.example.domain.util.ErrorMessage.ERROR_MESSAGE_STORE_IS_EMPTY
import com.example.presentation.mapper.toUiModel
import com.example.presentation.model.Coordinate
import com.example.presentation.model.LocationTrackingButton
import com.example.presentation.model.ScreenCoordinate
import com.example.presentation.model.StoreDetail
import com.example.presentation.model.StoreType
import com.example.presentation.ui.map.filter.FilterViewModel
import com.example.presentation.ui.map.location.CurrentLocationComponent
import com.example.presentation.ui.map.marker.StoreMarker
import com.example.presentation.ui.map.reload.setReloadButtonBottomPadding
import com.example.presentation.ui.navigation.Screen
import com.example.presentation.util.MainConstants.DEFAULT_LATITUDE
import com.example.presentation.util.MainConstants.DEFAULT_LONGITUDE
import com.example.presentation.util.MainConstants.GREAT_STORE
import com.example.presentation.util.MainConstants.INITIALIZE_ABLE
import com.example.presentation.util.MainConstants.INITIALIZE_DEFAULT_DONE
import com.example.presentation.util.MainConstants.INITIALIZE_DONE
import com.example.presentation.util.MainConstants.INITIALIZE_MOVE_ONCE
import com.example.presentation.util.MainConstants.KIND_STORE
import com.example.presentation.util.MainConstants.UN_MARKER
import com.example.presentation.util.MapScreenType
import com.example.presentation.util.UiState
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraPosition
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
    onErrorToastChanged: (String) -> Unit,
    isListItemClicked: Boolean,
    onListItemChanged: (Boolean) -> Unit,
    clickedStoreLocation: Coordinate,
    onShowMoreCountChanged: (ShowMoreCount) -> Unit,
    onReloadOrShowMoreChanged: (Boolean) -> Unit,
    isReloadButtonClicked: Boolean,
    onGetNewScreenCoordinateChanged: (Boolean) -> Unit,
    isSearchComponentClicked: Boolean,
    onSearchComponentChanged: (Boolean) -> Unit,
    isSearchTerminationButtonClicked: Boolean,
    onSearchTerminationButtonChanged: (Boolean) -> Unit,
    isBackPressed: Boolean,
    onBackPressedChanged: (Boolean) -> Unit,
    mapViewModel: MapViewModel,
    navController: NavHostController,
    isReSearchButtonClicked: Boolean,
    filterViewModel:FilterViewModel = hiltViewModel()
) {
    val cameraPositionState = rememberCameraPositionState {}

    val scope = rememberCoroutineScope()

    val mapCenterCoordinate by mapViewModel.mapCenterCoordinate.collectAsStateWithLifecycle()
    val mapZoomLevel by mapViewModel.mapZoomLevel.collectAsStateWithLifecycle()

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
                onCurrentMapChanged,
                mapViewModel
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

        val storeDetailData by mapViewModel.storeDetailModelData.collectAsStateWithLifecycle()
        val mapScreenType by mapViewModel.mapScreenType.collectAsStateWithLifecycle()

        if (mapScreenType == MapScreenType.MAIN) {
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
                        mapViewModel.updateIsFilteredMarker(true)
                        onLoadingChanged(false)
                        onCurrentMapChanged(false)
                        onShowMoreCountChanged(ShowMoreCount(0, state.data.size))
                    }

                    is UiState.Failure -> {
                        if (mapViewModel.ableToShowSplashScreen.value) {
                            onSplashScreenShowAble(false)
                        }
                        showErrorToastMsg(state, onReloadOrShowMoreChanged, onErrorToastChanged)
                        onLoadingChanged(false)
                    }
                }
            }
        } else {
            val padding = with(LocalDensity.current) {
                Dp(35F).roundToPx()
            }
            val searchStore by mapViewModel.searchStoreModelData.collectAsStateWithLifecycle()
            val searchBounds by mapViewModel.searchBounds.collectAsStateWithLifecycle()

            LaunchedEffect(key1 = searchStore) {
                val bounds = LatLngBounds(
                    searchBounds.first,
                    searchBounds.second
                )

                when (val state = searchStore) {
                    is UiState.Loading -> {
                        // Todo : 검색 시 로딩 뷰 구현
                    }

                    is UiState.Success -> {
                        mapViewModel.updateIsFilteredMarker(true)
                        onCurrentMapChanged(false)
                        onReloadOrShowMoreChanged(false)

                        cameraPositionState.animate(
                            if (bounds.southWest.latitude == 0.0) {
                                val position = CameraPosition(bounds.northEast, 16.0)
                                CameraUpdate.toCameraPosition(position)
                            } else {
                                CameraUpdate.fitBounds(bounds, padding)
                            },
                            animation = CameraAnimation.Fly,
                            durationMs = 500
                        )

                        if (bounds.southWest.latitude == 0.0) {
                            onMarkerChanged(state.data.first().id)
                            onStoreInfoChanged(state.data.first().toUiModel())
                            onBottomSheetChanged(true)
                        }
                    }

                    is UiState.Failure -> {
                        movePrevCamera(cameraPositionState, mapCenterCoordinate, mapZoomLevel)
                        showErrorToastMsg(state, onReloadOrShowMoreChanged, onErrorToastChanged)
                    }
                }
            }
        }

        val isFilteredMarker by mapViewModel.isFilteredMarker.collectAsStateWithLifecycle()

        if (isFilteredMarker) {
            FilteredMarkers(
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
        if (isReSearchButtonClicked) {
            mapViewModel.updateMapCenterCoordinate(
                Coordinate(
                    cameraPositionState.position.target.latitude,
                    cameraPositionState.position.target.longitude
                )
            )
            onGetNewScreenCoordinateChanged(true)
        }
        if (isReloadButtonClicked) {
            GetScreenCoordinate(cameraPositionState, onScreenChanged)
            onGetNewScreenCoordinateChanged(true)
        }
        if (isSearchComponentClicked) {
            mapViewModel.updateMapCenterCoordinate(
                Coordinate(
                    cameraPositionState.position.target.latitude,
                    cameraPositionState.position.target.longitude,
                )
            )
            mapViewModel.updateMapZoomLevel(cameraPositionState.position.zoom)
            navController.navigate(Screen.Search.route)
            filterViewModel.updateAllFilterUnClicked()
            mapViewModel.initializeFilterSet()
            onSearchComponentChanged(false)
        }
        if (isBackPressed) {
            mapViewModel.updateMapCenterCoordinate(
                Coordinate(
                    cameraPositionState.position.target.latitude,
                    cameraPositionState.position.target.longitude,
                )
            )
            mapViewModel.updateMapZoomLevel(cameraPositionState.position.zoom)
            onBackPressedChanged(false)
            navController.popBackStack()
        }
    }

    CurrentLocationComponent(
        isMarkerClicked,
        selectedLocationButton,
        onLocationButtonChanged,
        mapViewModel,
        currentSummaryInfoHeight
    )

    CheckSearchTerminationButtonClicked(
        isSearchTerminationButtonClicked,
        mapViewModel,
        cameraPositionState,
        navController,
        onSearchTerminationButtonChanged,
        onReloadButtonChanged,
        mapCenterCoordinate,
        mapZoomLevel
    )
}

private fun showErrorToastMsg(
    state: UiState.Failure,
    onReloadOrShowMoreChanged: (Boolean) -> Unit,
    onErrorToastChanged: (String) -> Unit
) {
    if (state.msg == ERROR_MESSAGE_STORE_IS_EMPTY) {
        onReloadOrShowMoreChanged(false)
    }
    onErrorToastChanged(state.msg)
}

@Composable
private fun TurnOffLocationButton(onLocationButtonChanged: (LocationTrackingButton) -> Unit) {
    onLocationButtonChanged(LocationTrackingButton.NO_FOLLOW)
}

@ExperimentalNaverMapApi
@Composable
fun FilteredMarkers(
    mapViewModel: MapViewModel,
    onBottomSheetChanged: (Boolean) -> Unit,
    onStoreInfoChanged: (StoreDetail) -> Unit,
    clickedMarkerId: Long,
    onMarkerChanged: (Long) -> Unit,
) {
    val storeDetailData by mapViewModel.flattenedStoreDetailList.collectAsStateWithLifecycle()
    storeDetailData.filter { info ->
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
    mainViewModel: MapViewModel
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

@Composable
private fun CheckSearchTerminationButtonClicked(
    isSearchTerminationButtonClicked: Boolean,
    mapViewModel: MapViewModel,
    cameraPositionState: CameraPositionState,
    navController: NavHostController,
    onSearchTerminationButtonChanged: (Boolean) -> Unit,
    onReloadButtonChanged: (Boolean) -> Unit,
    mapCenterCoordinate: Coordinate,
    mapZoomLevel: Double,
    filterViewModel: FilterViewModel = hiltViewModel()
) {
    if (isSearchTerminationButtonClicked) {
        mapViewModel.updateMapCenterCoordinate(
            Coordinate(
                cameraPositionState.position.target.latitude,
                cameraPositionState.position.target.longitude,
            )
        )
        mapViewModel.updateMapZoomLevel(cameraPositionState.position.zoom)
        mapViewModel.updateMapScreenType(MapScreenType.MAIN)
        navController.navigate(Screen.Main.route) {
            popUpTo(navController.graph.id) {
                inclusive = true
            }
        }
        onSearchTerminationButtonChanged(false)
        mapViewModel.updateIsSearchTerminated(true)
    }

    val isSearchTerminated by mapViewModel.isSearchTerminated.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = isSearchTerminated) {
        if (isSearchTerminated) {
            filterViewModel.updateAllFilterUnClicked()
            mapViewModel.initializeFilterSet()
            movePrevCamera(cameraPositionState, mapCenterCoordinate, mapZoomLevel)
            onReloadButtonChanged(true)
            mapViewModel.updateIsSearchTerminated(false)
        }
    }
}

@OptIn(ExperimentalNaverMapApi::class)
private suspend fun movePrevCamera(
    cameraPositionState: CameraPositionState,
    mapCenterCoordinate: Coordinate,
    mapZoomLevel: Double
) {

    val position = CameraPosition(
        LatLng(
            mapCenterCoordinate.latitude,
            mapCenterCoordinate.longitude
        ), mapZoomLevel
    )

    cameraPositionState.animate(
        CameraUpdate.toCameraPosition(position),
        animation = CameraAnimation.None,
        durationMs = 500
    )
}