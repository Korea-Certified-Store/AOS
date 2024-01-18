package com.example.presentation.mapper

import com.example.domain.model.Coordinate
import com.example.domain.model.OpeningHours
import com.example.domain.model.StoreDetail
import com.example.domain.model.TimeInfo
import com.example.presentation.model.CoordinateModel
import com.example.presentation.model.OpeningHoursModel
import com.example.presentation.model.StoreDetailModel
import com.example.presentation.model.StoreType
import com.example.presentation.model.TimeInfoModel

fun StoreDetail.toUiModel(): StoreDetailModel = StoreDetailModel(
    id,
    displayName,
    primaryTypeDisplayName,
    formattedAddress,
    phoneNumber,
    location.toLocationModel(),
    regularOpeningHours.map { it.toOpeningHoursModel() },
    localPhotos,
    certificationName.toStoreTypeModel()
)

fun Coordinate.toLocationModel(): CoordinateModel = CoordinateModel(latitude, longitude)

fun OpeningHours.toOpeningHoursModel(): OpeningHoursModel {
    return OpeningHoursModel(
        open = this.open.toTimeInfoModel(),
        close = this.close.toTimeInfoModel()
    )
}

fun TimeInfo.toTimeInfoModel(): TimeInfoModel = TimeInfoModel(day, hour, minute)

fun List<String>.toStoreTypeModel(): List<StoreType> {
    return this.map {
        when (it) {
            "모범음식점" -> StoreType.GREAT
            "착한가격업소" -> StoreType.KIND
            else -> StoreType.SAFE
        }
    }
}