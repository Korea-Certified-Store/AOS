package com.example.presentation.ui.map

import androidx.compose.runtime.Composable
import com.example.presentation.model.StoreDetail
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.overlay.OverlayImage

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun ClickedStoreMarker(
    storeDetail: StoreDetail
) {
    Marker(
        state = MarkerState(
            position = LatLng(
                storeDetail.location.latitude,
                storeDetail.location.longitude
            )
        ),
        icon = OverlayImage.fromResource(storeDetail.certificationName.first().clickedPinImg),
        onClick = {
            true
        }
    )
}