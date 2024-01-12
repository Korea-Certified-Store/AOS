package com.example.presentation.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import com.example.presentation.ui.theme.LightBlue
import com.example.presentation.ui.theme.LightGray
import com.example.presentation.ui.theme.MediumBlue
import com.example.presentation.ui.theme.MediumGray
import com.example.presentation.ui.theme.Red
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.LocationTrackingMode
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.compose.rememberFusedLocationSource
import com.naver.maps.map.overlay.OverlayImage
import java.util.LinkedList
import java.util.Queue

@ExperimentalNaverMapApi
@Composable
fun MainScreen(isMarkerClicked: MutableState<Boolean>) {
    InitMap(isMarkerClicked)
    StoreSummaryBottomSheet(if (isMarkerClicked.value) BOTTOM_SHEET_HEIGHT_ON else BOTTOM_SHEET_HEIGHT_OFF)
}

@ExperimentalNaverMapApi
@Composable
fun InitMap(isMarkerClicked: MutableState<Boolean>) {
    val cameraPositionState = rememberCameraPositionState()

    val optionQueue: Queue<Pair<Int, LocationTrackingMode>> = LinkedList()
    optionQueue.add(Pair(R.drawable.icon_none, LocationTrackingMode.None))
    optionQueue.add(Pair(R.drawable.icon_follow, LocationTrackingMode.Follow))
    optionQueue.add(Pair(R.drawable.icon_face, LocationTrackingMode.Face))

    var selectedOption =
        remember { mutableStateOf(optionQueue.element()) }

    NaverMap(
        cameraPositionState = cameraPositionState,
        modifier = Modifier.fillMaxSize(),
        locationSource = rememberFusedLocationSource(),
        properties = MapProperties(
            locationTrackingMode = selectedOption.value.second
        ),
        onMapClick = { _, _ ->
            isMarkerClicked.value = false
        },
        onOptionChange = {
            cameraPositionState.locationTrackingMode?.let {
                selectedOption.value.second
            }
        },
    )
    {
        StoreMarker(isMarkerClicked, Coordinate(37.5657, 126.9775), StoreType.GREAT)
    }
    InitLocationButton(selectedOption, optionQueue)
}

@Composable
fun InitLocationButton(
    selectedOption: MutableState<Pair<Int, LocationTrackingMode>>,
    optionQueue: Queue<Pair<Int, LocationTrackingMode>>
) {
    val context = LocalContext.current

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
                selectedOption.value = rotateAndPeek(optionQueue)
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

fun rotateAndPeek(optionQueue: Queue<Pair<Int, LocationTrackingMode>>): Pair<Int, LocationTrackingMode> {
    optionQueue.add(optionQueue.remove())
    return optionQueue.element()
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
            painter = painterResource(id = R.drawable.call),
            tint = LightBlue,
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