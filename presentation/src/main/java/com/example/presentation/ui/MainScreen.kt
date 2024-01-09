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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.R
import com.example.presentation.model.Coordinate
import com.example.presentation.model.StoreInfo
import com.example.presentation.model.StoreType
import com.example.presentation.ui.MainUtils.BOTTOM_SHEET_HEIGHT_OFF
import com.example.presentation.ui.MainUtils.BOTTOM_SHEET_HEIGHT_ON
import com.example.presentation.ui.theme.MediumBlue
import com.example.presentation.ui.theme.MediumGray
import com.example.presentation.ui.theme.Red
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.overlay.OverlayImage

@ExperimentalNaverMapApi
@Composable
fun MainScreen(isMarkerClicked: MutableState<Boolean>) {

    val testData = listOf(
        StoreInfo(
            storeId = 1,
            displayName = "미진일식 1호점",
            googlePlaceId = "",
            primaryType = "일식",
            formattedAddress = "주소",
            regularOpeningHours = "11:00 ~ 23:00",
            location = Coordinate(37.5657, 126.9775),
            internationalPhoneNumber = "연락처",
            storeCertificationId = StoreType.GREAT
        ),
        StoreInfo(
            storeId = 2,
            displayName = "미진일식 2호점",
            googlePlaceId = "",
            primaryType = "일식",
            formattedAddress = "주소",
            regularOpeningHours = "11:00 ~ 23:00",
            location = Coordinate(37.5667, 126.9785),
            internationalPhoneNumber = "연락처",
            storeCertificationId = StoreType.KIND
        ),
        StoreInfo(
            storeId = 3,
            displayName = "미진일식 3호점",
            googlePlaceId = "",
            primaryType = "일식",
            formattedAddress = "주소",
            regularOpeningHours = "11:00 ~ 23:00",
            location = Coordinate(37.5647, 126.9770),
            internationalPhoneNumber = "연락처",
            storeCertificationId = StoreType.SAFE
        )
    )

    val clickedStoreInfo = remember {
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
                storeCertificationId = StoreType.GREAT
            )
        )
    }

    InitMap(isMarkerClicked, testData, clickedStoreInfo)
    StoreSummaryBottomSheet(
        if (isMarkerClicked.value) BOTTOM_SHEET_HEIGHT_ON else BOTTOM_SHEET_HEIGHT_OFF,
        clickedStoreInfo
    )
}

@ExperimentalNaverMapApi
@Composable
fun InitMap(
    isMarkerClicked: MutableState<Boolean>,
    testData: List<StoreInfo>,
    clickedStoreInfo: MutableState<StoreInfo>
) {
    NaverMap(
        modifier = Modifier.fillMaxSize(),
        onMapClick = { _, _ ->
            isMarkerClicked.value = false
        }
    ) {
        testData.forEach { storeInfo ->
            StoreMarker(isMarkerClicked, storeInfo, clickedStoreInfo)
        }
    }
}

@ExperimentalNaverMapApi
@Composable
fun StoreMarker(
    isMarkerClicked: MutableState<Boolean>,
    storeInfo: StoreInfo,
    clickedStoreInfo: MutableState<StoreInfo>
) {
    Marker(
        state = MarkerState(
            position = LatLng(
                storeInfo.location.latitude,
                storeInfo.location.longitude
            )
        ),
        icon = when (storeInfo.storeCertificationId) {
            StoreType.KIND -> OverlayImage.fromResource(R.drawable.kind_store_pin)
            StoreType.GREAT -> OverlayImage.fromResource(R.drawable.great_store_pin)
            StoreType.SAFE -> OverlayImage.fromResource(R.drawable.safe_store_pin)
        },
        onClick = {
            isMarkerClicked.value = true
            clickedStoreInfo.value = storeInfo


            true
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreSummaryBottomSheet(heightType: Int, clickedStoreInfo: MutableState<StoreInfo>) {
    BottomSheetScaffold(
        sheetContent = {
            Column {
                StoreSummaryInfo(clickedStoreInfo.value)
            }
        },
        sheetPeekHeight = heightType.dp,
        sheetContainerColor = Color.White,
        sheetShape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp),
        sheetShadowElevation = 5.dp,
        sheetDragHandle = {
            Column {
                Spacer(modifier = Modifier.height(10.dp))
                Spacer(
                    modifier = Modifier
                        .width(32.dp)
                        .height(3.dp)
                        .background(Color.LightGray)
                )
            }
        }
    ) {

    }
}

@Composable
fun StoreSummaryInfo(
    storeInfo: StoreInfo
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
            Text(
                text = storeInfo.displayName,
                color = MediumBlue,
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = storeInfo.primaryType,
                color = MediumGray,
                fontSize = 10.sp,
                fontWeight = FontWeight.Normal
            )
            Spacer(modifier = Modifier.height(6.dp))
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
            StoreCallButton()
            Spacer(modifier = Modifier.height(14.dp))
        }
        Column {
            Spacer(modifier = Modifier.height(13.dp))
            StoreImage()
        }
    }
}

@Preview
@Composable
fun StoreCallButton() {
    Button(
        onClick = {},
        modifier = Modifier
            .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
        contentPadding = PaddingValues(horizontal = 27.dp, vertical = 6.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(3.dp),
        border = BorderStroke(1.dp, Color.LightGray)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.call),
            tint = Color.DarkGray,
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

object MainUtils {
    const val BOTTOM_SHEET_HEIGHT_ON = 170
    const val BOTTOM_SHEET_HEIGHT_OFF = 0
}
