package com.example.presentation.model

import androidx.annotation.StringRes
import com.example.presentation.R

enum class StoreType(@StringRes val storeTypeName: Int) {
    KIND(R.string.kind_price_store),
    GREAT(R.string.great_store),
    SAFE(R.string.safe_store)
}