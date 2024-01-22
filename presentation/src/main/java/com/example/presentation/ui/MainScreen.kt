package com.example.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
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
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import kotlin.math.pow
import kotlin.math.sqrt

@ExperimentalNaverMapApi
@Composable
fun MainScreen(
    mainViewModel: MainViewModel,
    onCallStoreChanged: (String) -> Unit
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

    val (selectedLocationButton, onLocationButtonChanged) =
        remember {
            mutableStateOf(
                mainViewModel.getInitialLocationTrackingMode()
            )
        }

    val (bottomSheetHeight, onBottomSheetHeightChanged) = remember { mutableStateOf(MainConstants.BOTTOM_SHEET_HEIGHT_OFF.dp) }

    val (clickedMarkerId, onMarkerChanged) = remember { mutableLongStateOf(-1) }

    val (initLocationSize, onInitLocationChanged) = remember { mutableIntStateOf(0) }

    val (isFilterStateChanged, onFilterStateChanged) = remember { mutableStateOf(false) }

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
        onMarkerChanged,
        selectedLocationButton,
        onLocationButtonChanged,
        onSearchOnCurrentMapButtonChanged,
        initLocationSize,
        onInitLocationChanged,
        screenCoordinate
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
        onSafeFilterChanged,
        mainViewModel,
        onFilterStateChanged
    )

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
        val limitScreenCoordinate = parallelTranslate(screenCoordinate)
        mainViewModel.getStoreDetail(
            nwLong = limitScreenCoordinate.northWest.longitude,
            nwLat = limitScreenCoordinate.northWest.latitude,

            swLong = limitScreenCoordinate.southWest.longitude,
            swLat = limitScreenCoordinate.southWest.latitude,

            seLong = limitScreenCoordinate.southEast.longitude,
            seLat = limitScreenCoordinate.southEast.latitude,

            neLong = limitScreenCoordinate.northEast.longitude,
            neLat = limitScreenCoordinate.northEast.latitude
        )
        onCurrentMapChanged(false)
        onSearchOnCurrentMapButtonChanged(false)
        onOriginCoordinateChanged(newCoordinate)
    }

    if (isFilterStateChanged) {
        onMarkerChanged(-1)
        onFilterStateChanged(false)
        onBottomSheetChanged(false)
        onBottomSheetHeightChanged(MainConstants.BOTTOM_SHEET_HEIGHT_OFF.dp)
    }
}

fun parallelTranslate(requestLocation: ScreenCoordinate): ScreenCoordinate {
    val distance1 = sqrt(
        (requestLocation.northWest.longitude - requestLocation.southWest.longitude).pow(2)
                + (requestLocation.northWest.latitude - requestLocation.southWest.latitude).pow(2)
    )
    val distance2 = sqrt(
        (requestLocation.northWest.longitude - requestLocation.northEast.longitude).pow(2)
                + (requestLocation.northWest.latitude - requestLocation.northEast.latitude).pow(2)
    )

    val center = Coordinate(
        longitude = (requestLocation.northWest.longitude + requestLocation.southEast.longitude) / 2.0,
        latitude = (requestLocation.northWest.latitude + requestLocation.southEast.latitude) / 2.0
    )

    if (distance1 > 0.07) {
        var newLocation = translateHeightLocations(
            loc1 = requestLocation.northWest,
            loc2 = requestLocation.northEast,
            center = center
        )
        if (distance2 > 0.07) {
            newLocation = translateHeightLocations(
                loc1 = newLocation.northEast,
                loc2 = newLocation.southEast,
                center = center
            )
        }
        return newLocation
    }

    return requestLocation
}

fun translateHeightLocations(
    loc1: Coordinate,
    loc2: Coordinate,
    center: Coordinate
): ScreenCoordinate {
    return if (loc1.latitude == loc2.latitude) {
        return ScreenCoordinate(
            northWest = Coordinate(longitude = loc1.longitude, latitude = center.latitude + 0.035),
            southWest = Coordinate(longitude = loc1.longitude, latitude = center.latitude - 0.035),
            southEast = Coordinate(longitude = loc2.longitude, latitude = center.latitude - 0.035),
            northEast = Coordinate(longitude = loc2.longitude, latitude = center.latitude + 0.035)
        )
    } else if (loc1.longitude == loc2.longitude) {
        return ScreenCoordinate(
            northWest = Coordinate(longitude = center.longitude + 0.035, latitude = loc1.latitude),
            southWest = Coordinate(longitude = center.longitude - 0.035, latitude = loc1.latitude),
            southEast = Coordinate(longitude = center.longitude - 0.035, latitude = loc2.latitude),
            northEast = Coordinate(longitude = center.longitude + 0.035, latitude = loc2.latitude)
        )
    } else {
        val slope = (loc2.latitude - loc1.latitude) / (loc2.longitude - loc1.longitude)
        val constant1 = 0.035 * sqrt(slope.pow(2) + 1) - slope * center.longitude + center.latitude
        val constant2 =
            (-0.035) * sqrt(slope.pow(2) + 1) - slope * center.longitude + center.latitude

        ScreenCoordinate(
            northWest = getNewLocation(location = loc1, slope = slope, constant = constant1),
            southWest = getNewLocation(location = loc1, slope = slope, constant = constant2),
            southEast = getNewLocation(location = loc2, slope = slope, constant = constant2),
            northEast = getNewLocation(location = loc2, slope = slope, constant = constant1)
        )
    }
}

fun getNewLocation(location: Coordinate, slope: Double, constant: Double): Coordinate {
    return Coordinate(
        longitude = (location.latitude + (location.longitude / slope) - constant) / (slope + 1 / slope),
        latitude = (slope * location.latitude + location.longitude + constant / slope) / (slope + 1 / slope)
    )
}