package com.example.presentation.ui.map

import androidx.compose.runtime.Composable
import com.example.presentation.model.StoreDetail
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.overlay.OverlayImage

@ExperimentalNaverMapApi
@Composable
fun StoreMarker(
    onBottomSheetChanged: (Boolean) -> Unit,
    storeDetail: StoreDetail,
    onStoreInfoChanged: (StoreDetail) -> Unit
) {
    Marker(
        state = MarkerState(
            position = LatLng(
                storeDetail.location.latitude,
                storeDetail.location.longitude
            )
        ),
        icon = OverlayImage.fromResource(storeDetail.certificationName.first().initPinImg),
        onClick = {
            onBottomSheetChanged(true)
            onStoreInfoChanged(storeDetail)

            true
        }
    )
}