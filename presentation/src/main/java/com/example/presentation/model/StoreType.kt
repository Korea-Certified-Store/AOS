package com.example.presentation.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.presentation.R

enum class StoreType(
    @StringRes val storeTypeName: Int,
    @DrawableRes val initPinImg: Int,
    @DrawableRes val clickedPinImg: Int
) {
    KIND(R.string.kind_price_store, R.drawable.kind_store_pin, R.drawable.clicked_kind_store_pin),
    GREAT(R.string.great_store, R.drawable.great_store_pin, R.drawable.clicked_great_store_pin),
    SAFE(R.string.safe_store, R.drawable.safe_store_pin, R.drawable.clicked_safe_store_pin)
}