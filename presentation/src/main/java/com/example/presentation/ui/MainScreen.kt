package com.example.presentation.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.R
import com.example.presentation.model.Contact
import com.example.presentation.model.Coordinate
import com.example.presentation.model.StoreInfo
import com.example.presentation.model.StoreType
import com.example.presentation.ui.MainUtils.BOTTOM_SHEET_HEIGHT_OFF
import com.example.presentation.ui.MainUtils.BOTTOM_SHEET_HEIGHT_ON
import com.example.presentation.ui.theme.DarkGray
import com.example.presentation.ui.theme.LightBlue
import com.example.presentation.ui.theme.LightYellow
import com.example.presentation.ui.theme.MediumBlue
import com.example.presentation.ui.theme.MediumGray
import com.example.presentation.ui.theme.Pink
import com.example.presentation.ui.theme.Red
import com.example.presentation.ui.theme.White
import com.naver.maps.geometry.LatLng
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
    onCallStoreChanged: (String) -> Unit,
    onSaveStoreNumberChanged: (Contact) -> Unit,
    onClipboardChanged: (String) -> Unit,
) {
    val testMarkerData = listOf(
        StoreInfo(
            storeId = 1,
            displayName = "미진일식 1호점",
            googlePlaceId = "",
            primaryType = "일식",
            formattedAddress = "주소",
            regularOpeningHours = "11:00 ~ 23:00",
            location = Coordinate(37.5657, 126.9775),
            internationalPhoneNumber = "1234",
            storeCertificationId = listOf(StoreType.KIND)
        ),
        StoreInfo(
            storeId = 2,
            displayName = "미진일식 2호점",
            googlePlaceId = "",
            primaryType = "일식",
            formattedAddress = "주소",
            regularOpeningHours = "11:00 ~ 23:00",
            location = Coordinate(37.5667, 126.9785),
            internationalPhoneNumber = "+82 2-1234-5678",
            storeCertificationId = listOf(StoreType.GREAT, StoreType.KIND)
        ),
        StoreInfo(
            storeId = 3,
            displayName = "미진일식 3호점",
            googlePlaceId = "",
            primaryType = "일식",
            formattedAddress = "주소",
            regularOpeningHours = "11:00 ~ 23:00",
            location = Coordinate(37.5647, 126.9770),
            internationalPhoneNumber = "+82 2-1234-5678",
            storeCertificationId = listOf(StoreType.SAFE, StoreType.GREAT, StoreType.KIND)
        )
    )

    val (clickedStoreInfo, onStoreInfoChanged) = remember {
        mutableStateOf(
            StoreInfo(
                storeId = 0,
                displayName = "",
                googlePlaceId = "",
                primaryType = "",
                formattedAddress = "",
                regularOpeningHours = "",
                location = Coordinate(0.0, 0.0),
                internationalPhoneNumber = "",
                storeCertificationId = listOf()
            )
        )
    }

    val (isMarkerClicked, onBottomSheetChanged) = remember { mutableStateOf(false) }

    val (isCallClicked, onCallDialogChanged) = remember { mutableStateOf(false) }
    val (isCallDialogCancelClicked, onCallDialogCanceled) = remember { mutableStateOf(false) }

    InitMap(
        isMarkerClicked,
        onBottomSheetChanged,
        testMarkerData,
        clickedStoreInfo,
        onStoreInfoChanged
    )
    StoreSummaryBottomSheet(
        if (isMarkerClicked) BOTTOM_SHEET_HEIGHT_ON else BOTTOM_SHEET_HEIGHT_OFF,
        clickedStoreInfo,
        onCallDialogChanged
    )

    if (isCallClicked && isCallDialogCancelClicked.not()) {
        StoreCallDialog(
            Contact(
                clickedStoreInfo.displayName,
                clickedStoreInfo.internationalPhoneNumber,
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

}

@ExperimentalNaverMapApi
@Composable
fun InitMap(
    isMarkerClicked: Boolean,
    onBottomSheetChanged: (Boolean) -> Unit,
    testMarkerData: List<StoreInfo>,
    clickedStoreInfo: StoreInfo,
    onStoreInfoChanged: (StoreInfo) -> Unit
) {
    val cameraPositionState = rememberCameraPositionState()
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

        cameraPositionState = cameraPositionState,
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
        testMarkerData.forEach { storeInfo ->
            StoreMarker(onBottomSheetChanged, storeInfo, onStoreInfoChanged)
        }

        if (isMarkerClicked) {
            ClickedStoreMarker(clickedStoreInfo)
        }
    }
    InitLocationButton(selectedOption)
}

@Composable
fun InitLocationButton(
    selectedOption: MutableState<Pair<Int, LocationTrackingMode>>,
) {
    val isFollow = remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(start = 12.dp),
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
        Spacer(modifier = Modifier.height(43.dp))
    }
}

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
    storeInfo: StoreInfo,
    onStoreInfoChanged: (StoreInfo) -> Unit
) {
    Marker(
        state = MarkerState(
            position = LatLng(
                storeInfo.location.latitude,
                storeInfo.location.longitude
            )
        ),
        icon = OverlayImage.fromResource(storeInfo.storeCertificationId.first().initPinImg),
        onClick = {
            onBottomSheetChanged(true)
            onStoreInfoChanged(storeInfo)

            true
        }
    )
}

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun ClickedStoreMarker(
    storeInfo: StoreInfo
) {
    Marker(
        state = MarkerState(
            position = LatLng(
                storeInfo.location.latitude,
                storeInfo.location.longitude
            )
        ),
        icon = OverlayImage.fromResource(storeInfo.storeCertificationId.first().clickedPinImg),
        onClick = {
            true
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreSummaryBottomSheet(
    heightType: Int,
    clickedStoreInfo: StoreInfo,
    onCallDialogChanged: (Boolean) -> Unit
) {
    BottomSheetScaffold(
        sheetContent = {
            Column {
                StoreSummaryInfo(clickedStoreInfo, onCallDialogChanged)
            }
        },
        sheetPeekHeight = heightType.dp,
        sheetContainerColor = White,
        sheetShape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp),
        sheetShadowElevation = 5.dp,
        sheetDragHandle = {
            Column {
                Spacer(modifier = Modifier.height(10.dp))
                Spacer(
                    modifier = Modifier
                        .width(32.dp)
                        .height(3.dp)
                        .background(LightGray)
                )
            }
        }
    ) {

    }
}

@Composable
fun StoreSummaryInfo(
    storeInfo: StoreInfo,
    onCallDialogChanged: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(1f),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(13.dp))
            Row {
                Text(
                    text = storeInfo.displayName,
                    Modifier.alignByBaseline(),
                    color = MediumBlue,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = storeInfo.primaryType,
                    Modifier.alignByBaseline(),
                    color = MediumGray,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Normal
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Chips(storeInfo.storeCertificationId)
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Text(
                    text = "영업 중",
                    color = Red,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = storeInfo.regularOpeningHours,
                    color = MediumGray,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Normal
                )
            }
            Spacer(modifier = Modifier.height(13.dp))
            StoreCallButton(onCallDialogChanged)
            Spacer(modifier = Modifier.height(14.dp))
        }
        Column {
            Spacer(modifier = Modifier.height(13.dp))
            StoreImage()
        }
    }
}

@Composable
fun StoreCallButton(onCallDialogChanged: (Boolean) -> Unit) {
    Button(
        onClick = {
            onCallDialogChanged(true)
        },
        modifier = Modifier
            .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
        contentPadding = PaddingValues(horizontal = 27.dp, vertical = 6.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = White
        ),
        shape = RoundedCornerShape(3.dp),
        border = BorderStroke(1.dp, LightGray)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.call),
            tint = DarkGray,
            contentDescription = "Call",
            modifier = Modifier.size(20.dp)
        )
    }
}

@Preview
@Composable
fun StoreImage() {
    Card(
        modifier = Modifier.size(116.dp),
        shape = RoundedCornerShape(6.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.store_example),
            contentDescription = "Store Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(1f)
        )
    }
}

@Composable
private fun Chip(
    storeType: StoreType
) {
    Surface(
        color = when (storeType) {
            StoreType.KIND -> Pink
            StoreType.GREAT -> LightYellow
            StoreType.SAFE -> LightBlue
        },
        shape = RoundedCornerShape(20.dp)
    ) {
        Text(
            text = stringResource(storeType.storeTypeName),
            color = MediumGray,
            fontSize = 9.sp,
            modifier = Modifier.padding(horizontal = 7.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun Chips(
    elements: List<StoreType>
) {
    LazyRow(modifier = Modifier) {
        items(elements.size) { idx ->
            Chip(storeType = elements[idx])
            Spacer(modifier = Modifier.padding(4.dp))
        }
    }
}

object MainUtils {
    const val BOTTOM_SHEET_HEIGHT_ON = 170
    const val BOTTOM_SHEET_HEIGHT_OFF = 0
}
