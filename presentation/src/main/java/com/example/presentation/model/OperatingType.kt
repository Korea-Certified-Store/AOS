package com.example.presentation.model

import androidx.annotation.StringRes
import com.example.presentation.R

enum class OperatingType(@StringRes val description: Int) {
    OPERATING(R.string.operating),
    CLOSED(R.string.closed),
    DAY_OFF(R.string.day_off),
    BREAK_TIME(R.string.break_time)
}