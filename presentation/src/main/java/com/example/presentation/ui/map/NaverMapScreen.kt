package com.example.presentation.ui.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.presentation.R
import com.example.presentation.mapper.toUiModel
import com.example.presentation.model.Coordinate
import com.example.presentation.model.StoreDetail
import com.example.presentation.ui.MainViewModel
import com.example.presentation.ui.setNewCoordinateIfGestured
import com.example.presentation.util.UiState
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.LocationTrackingMode
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
    onNewCoordinateChanged: (Coordinate) -> Unit
) {
    val cameraPositionState = rememberCameraPositionState {
        onOriginCoordinateChanged(
            Coordinate(
                position.target.latitude,
                position.target.longitude
            )
        )
    }
    val cameraIsMoving = remember { mutableStateOf(cameraPositionState.isMoving) }

    val selectedOption =
        remember {
            mutableStateOf(
                Pair(R.drawable.icon_follow, LocationTrackingMode.Follow)
            )
        }
    if (cameraIsMoving.value) {
        selectedOption.value = Pair(R.drawable.icon_none, LocationTrackingMode.NoFollow)
    }

    NaverMap(
        modifier = Modifier.fillMaxSize(),
        uiSettings = MapUiSettings(isZoomControlEnabled = false),
        cameraPositionState = cameraPositionState.apply {
            setNewCoordinateIfGestured(this, onNewCoordinateChanged)
        },
        locationSource = rememberFusedLocationSource(),
        properties = MapProperties(
            locationTrackingMode = selectedOption.value.second
        ),
        onMapClick = { _, _ ->
            onBottomSheetChanged(false)
            selectedOption.value = Pair(R.drawable.icon_none, LocationTrackingMode.NoFollow)
        },
        onOptionChange = {
            cameraPositionState.locationTrackingMode?.let {
                selectedOption.value.second
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
    InitLocationButton(isMarkerClicked, selectedOption)
}