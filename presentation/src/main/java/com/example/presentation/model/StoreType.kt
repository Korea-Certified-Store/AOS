package com.example.presentation.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.example.presentation.R
import com.example.presentation.ui.theme.Red
import com.example.presentation.ui.theme.SkyBlue
import com.example.presentation.ui.theme.Yellow

enum class StoreType(
    @StringRes val storeTypeName: Int,
    @DrawableRes val initPinImg: Int,
    @DrawableRes val clickedPinImg: Int,
    val color: Color
) {
    KIND(
        R.string.kind_price_store,
        R.drawable.kind_store_pin,
        R.drawable.clicked_kind_store_pin,
        Red
    ),
    GREAT(
        R.string.great_store,
        R.drawable.great_store_pin,
        R.drawable.clicked_great_store_pin,
        Yellow
    ),
    SAFE(
        R.string.safe_store,
        R.drawable.safe_store_pin,
        R.drawable.clicked_safe_store_pin,
        SkyBlue
    )
}