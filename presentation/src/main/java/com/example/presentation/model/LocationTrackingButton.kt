package com.example.presentation.model

import androidx.annotation.DrawableRes
import com.example.presentation.R
import com.naver.maps.map.compose.LocationTrackingMode

enum class LocationTrackingButton(
    val mode: LocationTrackingMode,
    @DrawableRes val img: Int,
) {
    NONE(
        LocationTrackingMode.None,
        R.drawable.icon_none,
    ),
    NO_FOLLOW(
        LocationTrackingMode.NoFollow,
        R.drawable.icon_none,
    ),
    FOLLOW(
        LocationTrackingMode.Follow,
        R.drawable.icon_follow,
    ),
    FACE(
        LocationTrackingMode.Face,
        R.drawable.icon_face,
    ),
}