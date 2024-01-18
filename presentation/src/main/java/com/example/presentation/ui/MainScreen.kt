package com.example.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.presentation.R
import com.example.presentation.model.Contact
import com.example.presentation.model.Coordinate
import com.example.presentation.model.StoreDetail
import com.example.presentation.ui.map.FilterButtons
import com.example.presentation.ui.map.InitMap
import com.example.presentation.ui.map.SearchOnCurrentMapButton
import com.example.presentation.ui.map.StoreCallDialog
import com.example.presentation.ui.map.StoreSummaryBottomSheet
import com.example.presentation.util.MainConstants.BOTTOM_SHEET_HEIGHT_OFF
import com.example.presentation.util.MainConstants.BOTTOM_SHEET_HEIGHT_ON
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.CameraUpdateReason
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.LocationTrackingMode

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

fun getTrackingModePair(isFollow: MutableState<Boolean>): Pair<Int, LocationTrackingMode> {
    isFollow.value = !isFollow.value
    return when (isFollow.value) {
        true -> Pair(R.drawable.icon_follow, LocationTrackingMode.Follow)
        false -> Pair(R.drawable.icon_face, LocationTrackingMode.Face)
    }
}