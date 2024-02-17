package com.example.presentation.ui

import android.app.Activity
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.domain.model.map.ShowMoreCount
import com.example.presentation.model.Contact
import com.example.presentation.model.Coordinate
import com.example.presentation.model.ExpandedType
import com.example.presentation.model.ScreenCoordinate
import com.example.presentation.model.StoreDetail
import com.example.presentation.ui.map.MapViewModel
import com.example.presentation.ui.map.NaverMapScreen
import com.example.presentation.ui.map.call.StoreCallDialog
import com.example.presentation.ui.map.filter.FilterComponent
import com.example.presentation.ui.map.list.StoreListBottomSheet
import com.example.presentation.ui.map.reload.ReloadOrShowMoreButton
import com.example.presentation.ui.map.summary.DimScreen
import com.example.presentation.ui.map.summary.StoreSummaryBottomSheet
import com.example.presentation.ui.navigation.Screen
import com.example.presentation.ui.search.StoreSearchComponent
import com.example.presentation.util.MainConstants
import com.example.presentation.util.MainConstants.UN_MARKER
import com.example.presentation.util.MapScreenType
import com.naver.maps.map.compose.ExperimentalNaverMapApi

@ExperimentalNaverMapApi
@Composable
fun MainScreen(
    onCallStoreChanged: (String) -> Unit,
    onSplashScreenShowAble: (Boolean) -> Unit,
    navController: NavController,
    searchText: String?,
    mapViewModel: MapViewModel
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
                localPhotos = listOf(""),
                operationTimeOfWeek = emptyMap(),
            )
        )
    }

    val (isMarkerClicked, onBottomSheetChanged) = remember { mutableStateOf(false) }

    val (isCallClicked, onCallDialogChanged) = remember { mutableStateOf(false) }
    val (isCallDialogCancelClicked, onCallDialogCanceled) = remember { mutableStateOf(false) }

    val (isMapGestured, onCurrentMapChanged) = remember { mutableStateOf(false) }
    val (isReloadButtonClicked, onReloadButtonChanged) = remember {
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

    val (selectedLocationButton, onLocationButtonChanged) =
        remember {
            mutableStateOf(
                mapViewModel.setLocationTrackingMode()
            )
        }

    val (currentSummaryInfoHeight, onCurrentSummaryInfoHeightChanged) = remember {
        mutableStateOf(
            MainConstants.BOTTOM_SHEET_HEIGHT_OFF.dp
        )
    }

    val (clickedMarkerId, onMarkerChanged) = remember { mutableLongStateOf(UN_MARKER) }

    val (isFilterStateChanged, onFilterStateChanged) = remember { mutableStateOf(false) }

    val (bottomSheetExpandedType, onBottomSheetExpandedChanged) = remember {
        mutableStateOf(
            ExpandedType.COLLAPSED
        )
    }

    val (isLoading, onLoadingChanged) = remember { mutableStateOf(false) }

    val (isFilteredMarker, onFilteredMarkerChanged) = remember { mutableStateOf(false) }

    val (errorSnackBarMsg, onErrorSnackBarChanged) = remember { mutableStateOf("") }

    val (isListItemClicked, onListItemChanged) = remember { mutableStateOf(false) }

    val (showMoreCount, onShowMoreCountChanged) = remember { mutableStateOf(ShowMoreCount(-1, 5)) }

    val (isReloadOrShowMoreShowAble, onReloadOrShowMoreChanged) = remember { mutableStateOf(false) }

    val (isScreenCoordinateChanged, onGetNewScreenCoordinateChanged) = remember {
        mutableStateOf(
            false
        )
    }

    val (isSearchComponentClicked, onSearchComponentChanged) = remember { mutableStateOf(false) }

    val (isSearchCoordinateGotten, onSearchCoordinatedChanged) = remember { mutableStateOf(false) }

    val (mapCenterCoordinate, onMapCenterCoordinateChanged) = remember {
        mutableStateOf(
            Coordinate(
                0.0,
                0.0
            )
        )
    }

    val (mapScreenType, onMapScreenTypeChanged) = remember { mutableStateOf(MapScreenType.MAIN) }

    if (searchText == null) {
        onMapScreenTypeChanged(MapScreenType.MAIN)
    } else {
        onMapScreenTypeChanged(MapScreenType.SEARCH)
    }

    NaverMapScreen(
        isMarkerClicked,
        onBottomSheetChanged,
        onStoreInfoChanged,
        onScreenChanged,
        currentSummaryInfoHeight,
        clickedMarkerId,
        onMarkerChanged,
        selectedLocationButton,
        onLocationButtonChanged,
        onReloadButtonChanged,
        onSplashScreenShowAble,
        onLoadingChanged,
        onCurrentMapChanged,
        isFilteredMarker,
        onFilteredMarkerChanged,
        onErrorSnackBarChanged,
        isListItemClicked,
        onListItemChanged,
        clickedStoreInfo.location,
        onShowMoreCountChanged,
        onReloadOrShowMoreChanged,
        isReloadButtonClicked,
        onGetNewScreenCoordinateChanged,
        isSearchComponentClicked,
        onMapCenterCoordinateChanged,
        onSearchCoordinatedChanged,
        mapViewModel,
        mapScreenType
    )

    if (isReloadOrShowMoreShowAble) {
        ReloadOrShowMoreButton(
            isMarkerClicked,
            currentSummaryInfoHeight,
            isMapGestured,
            onShowMoreCountChanged,
            onReloadButtonChanged,
            onMarkerChanged,
            onBottomSheetChanged,
            isLoading,
            showMoreCount,
            mapViewModel
        )
    }

    StoreSearchComponent(navController, searchText, onSearchComponentChanged)

    FilterComponent(
        isKindFilterClicked,
        onKindFilterChanged,
        isGreatFilterClicked,
        onGreatFilterChanged,
        isSafeFilterClicked,
        onSafeFilterChanged,
        mapViewModel,
        onFilterStateChanged
    )

    if (bottomSheetExpandedType == ExpandedType.FULL || bottomSheetExpandedType == ExpandedType.HALF || bottomSheetExpandedType == ExpandedType.DIM_CLICK) {
        DimScreen(bottomSheetExpandedType, onBottomSheetExpandedChanged)
    }

    if (isMarkerClicked) {
        StoreSummaryBottomSheet(
            clickedStoreInfo,
            onCallDialogChanged,
            currentSummaryInfoHeight,
            onCurrentSummaryInfoHeightChanged,
            bottomSheetExpandedType,
            onBottomSheetExpandedChanged
        )
    } else {
        StoreListBottomSheet(
            bottomSheetExpandedType,
            onBottomSheetExpandedChanged,
            onBottomSheetChanged,
            onStoreInfoChanged,
            onMarkerChanged,
            onListItemChanged,
            mapViewModel
        )
    }

    if (isCallClicked && isCallDialogCancelClicked.not() && clickedStoreInfo.phoneNumber != null) {
        StoreCallDialog(
            Contact(
                clickedStoreInfo.displayName,
                clickedStoreInfo.phoneNumber,
                clickedStoreInfo.formattedAddress
            ),
            onCallDialogCanceled,
            onCallStoreChanged
        )
    }

    if (isCallDialogCancelClicked) {
        onCallDialogCanceled(false)
        onCallDialogChanged(false)
    }

    if (isSearchCoordinateGotten) {
        onSearchComponentChanged(false)
        navController.currentBackStackEntry?.savedStateHandle?.set(
            key = MainConstants.SEARCH_COORDINATE_KEY,
            value = mapCenterCoordinate
        )
        navController.navigate(Screen.Search.route)
        onSearchCoordinatedChanged(false)
    }

    if (isReloadButtonClicked && isScreenCoordinateChanged) {
        onFilteredMarkerChanged(false)
        onErrorSnackBarChanged("")
        mapViewModel.getStoreDetail(
            nwLong = screenCoordinate.northWest.longitude,
            nwLat = screenCoordinate.northWest.latitude,

            swLong = screenCoordinate.southWest.longitude,
            swLat = screenCoordinate.southWest.latitude,

            seLong = screenCoordinate.southEast.longitude,
            seLat = screenCoordinate.southEast.latitude,

            neLong = screenCoordinate.northEast.longitude,
            neLat = screenCoordinate.northEast.latitude
        )
        onReloadButtonChanged(false)
        onGetNewScreenCoordinateChanged(false)
    }

    if (isFilterStateChanged) {
        onMarkerChanged(UN_MARKER)
        onFilterStateChanged(false)
        onBottomSheetChanged(false)
        onCurrentSummaryInfoHeightChanged(MainConstants.BOTTOM_SHEET_HEIGHT_OFF.dp)
    }

    if (errorSnackBarMsg.isNotEmpty()) {
        val context = LocalContext.current as Activity
        Toast.makeText(context, errorSnackBarMsg, Toast.LENGTH_SHORT).show()
        onErrorSnackBarChanged("")
    }
}