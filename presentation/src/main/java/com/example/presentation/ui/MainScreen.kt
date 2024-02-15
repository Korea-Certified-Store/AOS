package com.example.presentation.ui

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
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
import com.example.presentation.ui.search.ReSearchComponent
import com.example.presentation.ui.search.StoreSearchComponent
import com.example.presentation.util.MainConstants
import com.example.presentation.util.MainConstants.SEARCH_KEY
import com.example.presentation.util.MainConstants.UN_MARKER
import com.example.presentation.util.MapScreenType
import com.naver.maps.map.compose.ExperimentalNaverMapApi

@ExperimentalNaverMapApi
@Composable
fun MainScreen(
    onCallStoreChanged: (String) -> Unit,
    onSplashScreenShowAble: (Boolean) -> Unit,
    navController: NavHostController,
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

    val (errorToastMsg, onErrorToastChanged) = remember { mutableStateOf("") }

    val (isListItemClicked, onListItemChanged) = remember { mutableStateOf(false) }

    val (showMoreCount, onShowMoreCountChanged) = remember { mutableStateOf(ShowMoreCount(-1, 5)) }

    val (isReloadOrShowMoreShowAble, onReloadOrShowMoreChanged) = remember { mutableStateOf(false) }

    val (isReSearchButtonClicked, onReSearchButtonChanged) = remember { mutableStateOf(false) }

    val (isScreenCoordinateChanged, onGetNewScreenCoordinateChanged) = remember {
        mutableStateOf(
            false
        )
    }

    val (isSearchComponentClicked, onSearchComponentChanged) = remember { mutableStateOf(false) }

    val (isSearchTerminationButtonClicked, onSearchTerminationButtonChanged) = remember {
        mutableStateOf(
            false
        )
    }
    val (isBackPressed, onBackPressedChanged) = remember { mutableStateOf(false) }

    val isSearchTextExist = navController.previousBackStackEntry?.savedStateHandle?.contains(
        SEARCH_KEY
    ) ?: false

    val searchText = navController.previousBackStackEntry?.savedStateHandle?.get<String>(
        SEARCH_KEY
    )

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
        onErrorToastChanged,
        isListItemClicked,
        onListItemChanged,
        clickedStoreInfo.location,
        onShowMoreCountChanged,
        onReloadOrShowMoreChanged,
        isReloadButtonClicked,
        onGetNewScreenCoordinateChanged,
        isSearchComponentClicked,
        onSearchComponentChanged,
        isSearchTerminationButtonClicked,
        onSearchTerminationButtonChanged,
        isBackPressed,
        onBackPressedChanged,
        mapViewModel,
        navController,
        isFilteredMarker,
        onFilteredMarkerChanged,
        isReSearchButtonClicked
    )

    if (isReloadOrShowMoreShowAble) {
        if (isSearchTextExist) {
            ReSearchComponent(
                isMarkerClicked,
                currentSummaryInfoHeight,
                isMapGestured,
                onReSearchButtonChanged,
                onMarkerChanged,
                onBottomSheetChanged,
                isLoading,
            )
        } else {
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
    }

    StoreSearchComponent(
        searchText,
        onSearchComponentChanged,
        onSearchTerminationButtonChanged
    )

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

    val mapCenterCoordinate by mapViewModel.mapCenterCoordinate.collectAsStateWithLifecycle()
    if (isReSearchButtonClicked && isScreenCoordinateChanged) {
        Log.d("테스트", "키워드 재검색 눌리고있음?")
        mapViewModel.updateIsFilteredMarker(false)
        mapViewModel.searchStore(
            mapCenterCoordinate.longitude,
            mapCenterCoordinate.latitude,
            searchText ?: ""
        )
        onReSearchButtonChanged(false)
        onGetNewScreenCoordinateChanged(false)
    }

    if ((isReloadButtonClicked && isScreenCoordinateChanged)) {
        mapViewModel.updateIsFilteredMarker(false)
        onErrorToastChanged("")
        onFilteredMarkerChanged(false)
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

    if (errorToastMsg.isNotEmpty()) {
        val context = LocalContext.current as Activity
        Toast.makeText(context, errorToastMsg, Toast.LENGTH_SHORT).show()
        onErrorToastChanged("")
    }

    PressBack(mapViewModel, onBackPressedChanged)
}

@Composable
fun PressBack(
    mapViewModel: MapViewModel,
    onBackPressedChanged: (Boolean) -> Unit
) {
    val mapScreenType by mapViewModel.mapScreenType.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var backPressedTime = 0L

    BackHandler {
        if (mapScreenType == MapScreenType.SEARCH) {
            onBackPressedChanged(true)
            mapViewModel.updateMapScreenType(MapScreenType.MAIN)
        } else {
            if (System.currentTimeMillis() - backPressedTime <= 2000L) {
                (context as Activity).finish()
            } else {
                Toast.makeText(context, "한 번 더 누르면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show()
            }
            backPressedTime = System.currentTimeMillis()
        }
    }
}