package com.example.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.presentation.R
import com.example.presentation.mapper.toUiModel
import com.example.presentation.model.Contact
import com.example.presentation.model.Coordinate
import com.example.presentation.model.StoreDetail
import com.example.presentation.ui.MainUtils.BOTTOM_SHEET_HEIGHT_OFF
import com.example.presentation.ui.MainUtils.BOTTOM_SHEET_HEIGHT_ON
import com.example.presentation.ui.MainUtils.SEARCH_ON_CURRENT_MAP_BUTTON_DEFAULT_PADDING
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.CameraUpdateReason
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.LocationTrackingMode
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.compose.rememberFusedLocationSource
import com.naver.maps.map.overlay.OverlayImage

@ExperimentalNaverMapApi
@Composable
fun MainScreen(
    mainViewModel: MainViewModel,
    onCallStoreChanged: (String) -> Unit,
    onSaveStoreNumberChanged: (Contact) -> Unit,
    onClipboardChanged: (String) -> Unit,
) {

    val (clickedStoreInfo, onStoreInfoChanged) = remember {
        mutableStateOf(
            StoreDetail(
                id = 0,
                displayName = "",
                primaryTypeDisplayName = "",
                formattedAddress = "",
                regularOpeningHours = emptyList(),
                location = Coordinate(0.0, 0.0),
                phoneNumber = "",
                certificationName = listOf(),
                localPhotos = listOf("")
            )
        )
    }

    val (isMarkerClicked, onBottomSheetChanged) = remember { mutableStateOf(false) }

    val (isCallClicked, onCallDialogChanged) = remember { mutableStateOf(false) }
    val (isCallDialogCancelClicked, onCallDialogCanceled) = remember { mutableStateOf(false) }

    val (originCoordinate, onOriginCoordinateChanged) = remember {
        mutableStateOf(
            Coordinate(
                0.0,
                0.0
            )
        )
    }
    val (newCoordinate, onNewCoordinateChanged) = remember {
        mutableStateOf(
            Coordinate(
                0.0,
                0.0
            )
        )
    }
 
    val (isMapGestured, onCurrentMapChanged) = remember { mutableStateOf(false) }
    val (isSearchOnCurrentMapButtonClicked, onSearchOnCurrentMapButtonChanged) = remember {
        mutableStateOf(false)
    }

    val (isKindFilterClicked, onKindFilterChanged) = remember { mutableStateOf(false) }
    val (isGreatFilterClicked, onGreatFilterChanged) = remember { mutableStateOf(false) }
    val (isSafeFilterClicked, onSafeFilterChanged) = remember { mutableStateOf(false) }

    InitMap(
        mainViewModel,
        isMarkerClicked,
        onBottomSheetChanged,
        clickedStoreInfo,
        onStoreInfoChanged,
        onOriginCoordinateChanged,
        onNewCoordinateChanged
    )

    StoreSummaryBottomSheet(
        if (isMarkerClicked) BOTTOM_SHEET_HEIGHT_ON else BOTTOM_SHEET_HEIGHT_OFF,
        clickedStoreInfo,
        onCallDialogChanged
    )
    FilterButtons(
        isKindFilterClicked,
        onKindFilterChanged,
        isGreatFilterClicked,
        onGreatFilterChanged,
        isSafeFilterClicked,
        onSafeFilterChanged
    )

    if (isCallClicked && isCallDialogCancelClicked.not() && clickedStoreInfo.phoneNumber != null) {
        StoreCallDialog(
            Contact(
                clickedStoreInfo.displayName,
                clickedStoreInfo.phoneNumber,
                clickedStoreInfo.formattedAddress
            ),
            onCallDialogCanceled,
            onCallStoreChanged,
            onSaveStoreNumberChanged,
            onClipboardChanged
        )
    }

    if (isCallDialogCancelClicked) {
        onCallDialogCanceled(false)
        onCallDialogChanged(false)
    }

    if (originCoordinate != newCoordinate) {
        onCurrentMapChanged(true)
    }

    if (isMapGestured) {
        SearchOnCurrentMapButton(isMarkerClicked, onSearchOnCurrentMapButtonChanged)
    }

    if (isSearchOnCurrentMapButtonClicked) {
        mainViewModel.getStoreDetail(
            newCoordinate.longitude + 0.5,
            newCoordinate.latitude + 0.5,
            newCoordinate.longitude - 0.5,
            newCoordinate.latitude - 0.5,
        )
        onCurrentMapChanged(false)
        onSearchOnCurrentMapButtonChanged(false)
        onOriginCoordinateChanged(newCoordinate)
    }

}

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
fun InitLocationButton(
    isMarkerClicked: Boolean,
    selectedOption: MutableState<Pair<Int, LocationTrackingMode>>,
) {
    val isFollow = remember { mutableStateOf(true) }

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
                selectedOption.value = getTrackingModePair(isFollow)
            },
        ) {
            Image(
                painter = painterResource(id = selectedOption.value.first),
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

@ExperimentalNaverMapApi
@Composable
fun StoreMarker(
    onBottomSheetChanged: (Boolean) -> Unit,
    storeDetail: StoreDetail,
    onStoreInfoChanged: (StoreDetail) -> Unit
) {
    Marker(
        state = MarkerState(
            position = LatLng(
                storeDetail.location.latitude,
                storeDetail.location.longitude
            )
        ),
        icon = OverlayImage.fromResource(storeDetail.certificationName.first().initPinImg),
        onClick = {
            onBottomSheetChanged(true)
            onStoreInfoChanged(storeDetail)

            true
        }
    )
}

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun ClickedStoreMarker(
    storeDetail: StoreDetail
) {
    Marker(
        state = MarkerState(
            position = LatLng(
                storeDetail.location.latitude,
                storeDetail.location.longitude
            )
        ),
        icon = OverlayImage.fromResource(storeDetail.certificationName.first().clickedPinImg),
        onClick = {
            true
        }
    )
}