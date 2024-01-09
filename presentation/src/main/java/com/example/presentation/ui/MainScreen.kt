package com.example.presentation.ui

import androidx.compose.foundation.Image
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
import com.example.presentation.ui.theme.LightGray
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
    InitMap(isMarkerClicked)
    StoreSummaryBottomSheet(if (isMarkerClicked.value) BOTTOM_SHEET_HEIGHT_ON else BOTTOM_SHEET_HEIGHT_OFF)
}

@ExperimentalNaverMapApi
@Composable
fun InitMap(isMarkerClicked: MutableState<Boolean>) {
    NaverMap(
        modifier = Modifier.fillMaxSize(),
        onMapClick = { _, _ ->
            isMarkerClicked.value = false
        }
    ) {
        StoreMarker(isMarkerClicked, Coordinate(37.5657, 126.9775), StoreType.GREAT)
    }
}

@ExperimentalNaverMapApi
@Composable
fun StoreMarker(
    isMarkerClicked: MutableState<Boolean>,
    coordinate: Coordinate,
    storeType: StoreType
) {
    Marker(
        state = MarkerState(position = LatLng(coordinate.latitude, coordinate.longitude)),
        icon = when (storeType) {
            StoreType.KIND -> OverlayImage.fromResource(R.drawable.kind_store_pin)
            StoreType.GREAT -> OverlayImage.fromResource(R.drawable.great_store_pin)
            StoreType.SAFE -> OverlayImage.fromResource(R.drawable.safe_store_pin)
        },
        onClick = {
            isMarkerClicked.value = true
            true
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreSummaryBottomSheet(heightType: Int) {
    BottomSheetScaffold(
        sheetContent = {
            Column {
                StoreSummaryInfo(
                    StoreInfo(
                        storeName = "미진일식",
                        type = "일식",
                        address = "주소",
                        operatingTime = "영업시간",
                        coordinate = Coordinate(0.0, 0.0),
                        contact = "연락처",
                        picture = "사진"
                    )
                )
            }
        },
        sheetPeekHeight = heightType.dp,
        sheetContainerColor = Color.White,
        sheetShape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp),
        sheetShadowElevation = 5.dp
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
            Text(
                text = storeInfo.storeName,
                color = MediumBlue,
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.padding(bottom = 2.dp))
            Text(
                text = storeInfo.type,
                color = MediumGray,
                fontSize = 10.sp,
                fontWeight = FontWeight.Normal
            )
            Spacer(modifier = Modifier.padding(bottom = 6.dp))
            Text(text = "영업 중", color = Red, fontSize = 11.sp, fontWeight = FontWeight.Normal)
            Spacer(modifier = Modifier.height(17.dp))
            StoreCallButton()
            Spacer(modifier = Modifier.height(16.dp))
        }
        Column {
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
        contentPadding = PaddingValues(horizontal = 25.dp, vertical = 10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = LightGray
        )
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.call),
            contentDescription = "Call",
            modifier = Modifier.size(21.dp)
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
    const val BOTTOM_SHEET_HEIGHT_ON = 200
    const val BOTTOM_SHEET_HEIGHT_OFF = 0
}
