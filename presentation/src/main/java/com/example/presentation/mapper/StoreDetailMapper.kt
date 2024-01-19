package com.example.presentation.mapper

import com.example.domain.model.CoordinateModel
import com.example.domain.model.OpeningHoursModel
import com.example.domain.model.StoreDetailModel
import com.example.domain.model.TimeInfoModel
import com.example.presentation.model.Coordinate
import com.example.presentation.model.Day
import com.example.presentation.model.OpeningHours
import com.example.presentation.model.StoreDetail
import com.example.presentation.model.StoreType
import com.example.presentation.model.TimeInfo

fun StoreDetailModel.toUiModel(): StoreDetail =
    StoreDetail(
        id,
        displayName,
        primaryTypeDisplayName,
        formattedAddress,
        phoneNumber,
        location.toUiModel(),
        regularOpeningHours.map { it.toUiModel() },
        localPhotos,
        certificationName.toUiModel()
    )

fun CoordinateModel.toUiModel(): Coordinate =
    Coordinate(latitude, longitude)

fun OpeningHoursModel.toUiModel(): OpeningHours {
    return OpeningHours(
        open = this.open.toUiModel(),
        close = this.close.toUiModel()
    )
}

fun TimeInfoModel.toUiModel(): TimeInfo =
    TimeInfo(getDay(day), hour, minute)

fun getDay(day: String): Day {
    for (value in Day.values()) {
        if (day == value.name) {
            return value
        }
    }
    return Day.MON
}

fun List<String>.toUiModel(): List<StoreType> {
    return this.map {
        when (it) {
            "모범음식점" -> StoreType.GREAT
            "착한가격업소" -> StoreType.KIND
            else -> StoreType.SAFE
        }
    }.sortedBy { it.ordinal }
}