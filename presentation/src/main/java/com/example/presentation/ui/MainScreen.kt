package com.example.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import com.example.presentation.model.Contact
import com.example.presentation.model.Coordinate
import com.example.presentation.model.ScreenCoordinate
import com.example.presentation.model.StoreDetail
import com.example.presentation.ui.map.FilterButtons
import com.example.presentation.ui.map.InitMap
import com.example.presentation.ui.map.SearchOnCurrentMapButton
import com.example.presentation.ui.map.StoreCallDialog
import com.example.presentation.ui.map.StoreSummaryBottomSheet
import com.example.presentation.util.MainConstants
import com.example.presentation.util.MainConstants.LAT_LIMIT
import com.example.presentation.util.MainConstants.LONG_LIMIT
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import kotlin.math.max
import kotlin.math.min

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
                operatingType = "",
                timeDescription = "",
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

    val (screenCoordinate, onScreenChanged) = remember {
        mutableStateOf(
            ScreenCoordinate(
                Coordinate(0.0, 0.0),
                Coordinate(0.0, 0.0),
                Coordinate(0.0, 0.0),
                Coordinate(0.0, 0.0)
            )
        )
    }

    val (bottomSheetHeight, onBottomSheetHeightChanged) = remember { mutableStateOf(MainConstants.BOTTOM_SHEET_HEIGHT_OFF.dp) }

    val (clickedMarkerId, onMarkerChanged) = remember { mutableLongStateOf(-1) }

    InitMap(
        mainViewModel,
        isMarkerClicked,
        onBottomSheetChanged,
        onStoreInfoChanged,
        onOriginCoordinateChanged,
        onNewCoordinateChanged,
        onScreenChanged,
        bottomSheetHeight,
        clickedMarkerId,
        onMarkerChanged
    )

    StoreSummaryBottomSheet(
        isMarkerClicked,
        clickedStoreInfo,
        onCallDialogChanged,
        bottomSheetHeight,
        onBottomSheetHeightChanged
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
        SearchOnCurrentMapButton(
            isMarkerClicked,
            onSearchOnCurrentMapButtonChanged,
            bottomSheetHeight,
            onMarkerChanged,
            onBottomSheetChanged
        )
    }

    if (isSearchOnCurrentMapButtonClicked) {
        mainViewModel.getStoreDetail(
            max(screenCoordinate.northWest.longitude, (newCoordinate.longitude - LONG_LIMIT)),
            min(screenCoordinate.northWest.latitude, (newCoordinate.latitude + LAT_LIMIT)),

            max(screenCoordinate.southWest.longitude, (newCoordinate.longitude - LONG_LIMIT)),
            max(screenCoordinate.southWest.latitude, (newCoordinate.latitude - LAT_LIMIT)),

            min(screenCoordinate.southEast.longitude, (newCoordinate.longitude + LONG_LIMIT)),
            max(screenCoordinate.southEast.latitude, (newCoordinate.latitude - LAT_LIMIT)),

            min(screenCoordinate.northEast.longitude, (newCoordinate.longitude + LONG_LIMIT)),
            min(screenCoordinate.northEast.latitude, (newCoordinate.latitude + LAT_LIMIT)),
        )
        onCurrentMapChanged(false)
        onSearchOnCurrentMapButtonChanged(false)
        onOriginCoordinateChanged(newCoordinate)
    }
}