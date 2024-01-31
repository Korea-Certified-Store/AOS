package com.example.presentation.ui

import android.app.Activity
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.presentation.model.Contact
import com.example.presentation.model.Coordinate
import com.example.presentation.model.ExpandedType
import com.example.presentation.model.ScreenCoordinate
import com.example.presentation.model.StoreDetail
import com.example.presentation.ui.map.MapViewModel
import com.example.presentation.ui.map.NaverMapScreen
import com.example.presentation.ui.map.call.StoreCallDialog
import com.example.presentation.ui.map.filter.FilterComponent
import com.example.presentation.ui.map.reload.ReloadButton
import com.example.presentation.ui.map.summary.DimScreen
import com.example.presentation.ui.map.summary.StoreSummaryBottomSheet
import com.example.presentation.util.MainConstants
import com.example.presentation.util.MainConstants.UNMARKER
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import kotlin.math.pow
import kotlin.math.sqrt

@ExperimentalNaverMapApi
@Composable
fun MainScreen(
    mapViewModel: MapViewModel,
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
                localPhotos = listOf(""),
                operationTimeOfWeek = emptyMap(),
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

    val (clickedMarkerId, onMarkerChanged) = remember { mutableLongStateOf(UNMARKER) }

    val (initLocationSize, onInitLocationChanged) = remember { mutableIntStateOf(0) }

    val (isFilterStateChanged, onFilterStateChanged) = remember { mutableStateOf(false) }

    val (bottomSheetExpandedType, onBottomSheetExpandedChanged) = remember {
        mutableStateOf(
            ExpandedType.COLLAPSED
        )
    }

    val (isSplashScreenShowAble, onSplashScreenShowAble) = remember { mutableStateOf(true) }

    val (isLoading, onLoadingChanged) = remember { mutableStateOf(false) }

    val (isFilteredMarker, onFilteredMarkerChanged) = remember { mutableStateOf(false) }

    val (errorSnackBarMsg, onErrorSnackBarChanged) = remember { mutableStateOf("") }

    NaverMapScreen(
        mapViewModel,
        isMarkerClicked,
        onBottomSheetChanged,
        onStoreInfoChanged,
        onOriginCoordinateChanged,
        onNewCoordinateChanged,
        onScreenChanged,
        currentSummaryInfoHeight,
        clickedMarkerId,
        onMarkerChanged,
        selectedLocationButton,
        onLocationButtonChanged,
        onReloadButtonChanged,
        initLocationSize,
        onInitLocationChanged,
        screenCoordinate,
        onSplashScreenShowAble,
        onLoadingChanged,
        onCurrentMapChanged,
        isFilteredMarker,
        onFilteredMarkerChanged,
        onErrorSnackBarChanged
    )

    if (isMapGestured) {
        ReloadButton(
            isMarkerClicked,
            onReloadButtonChanged,
            currentSummaryInfoHeight,
            onMarkerChanged,
            onBottomSheetChanged,
            isLoading
        )
    }

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

    if (bottomSheetExpandedType == ExpandedType.FULL || bottomSheetExpandedType == ExpandedType.DIM || bottomSheetExpandedType == ExpandedType.DIM_CLICK) {
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
    }

    if (isSplashScreenShowAble) {
        SplashScreen()
    } else {
        mapViewModel.updateSplashState()
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

    if (originCoordinate != newCoordinate) {
        onCurrentMapChanged(true)
    }

    if (isReloadButtonClicked) {
        val limitScreenCoordinate = parallelTranslate(screenCoordinate)
        onFilteredMarkerChanged(false)
        onErrorSnackBarChanged("")
        mapViewModel.getStoreDetail(
            nwLong = limitScreenCoordinate.northWest.longitude,
            nwLat = limitScreenCoordinate.northWest.latitude,

            swLong = limitScreenCoordinate.southWest.longitude,
            swLat = limitScreenCoordinate.southWest.latitude,

            seLong = limitScreenCoordinate.southEast.longitude,
            seLat = limitScreenCoordinate.southEast.latitude,

            neLong = limitScreenCoordinate.northEast.longitude,
            neLat = limitScreenCoordinate.northEast.latitude
        )
        onReloadButtonChanged(false)
        onOriginCoordinateChanged(newCoordinate)
    }

    if (isFilterStateChanged) {
        onMarkerChanged(UNMARKER)
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