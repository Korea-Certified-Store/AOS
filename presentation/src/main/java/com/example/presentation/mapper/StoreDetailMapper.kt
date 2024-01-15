package com.example.presentation.mapper

import com.example.domain.model.Coordinate
import com.example.domain.model.OpeningHours
import com.example.domain.model.StoreDetail
import com.example.domain.model.TimeInfo
import com.example.presentation.model.CoordinateModel
import com.example.presentation.model.OpeningHoursModel
import com.example.presentation.model.StoreDetailModel
import com.example.presentation.model.TimeInfoModel

fun StoreDetail.toStoreDetailModel(): StoreDetailModel = StoreDetailModel(
    id,
    displayName,
    primaryTypeDisplayName,
    formattedAddress,
    phoneNumber,
    location.toLocationModel(),
    regularOpeningHours.map { it.toOpeningHoursModel() },
    localPhotos,
    certificationName
)

fun Coordinate.toLocationModel(): CoordinateModel = CoordinateModel(latitude, longitude)

fun OpeningHours.toOpeningHoursModel(): OpeningHoursModel {
    return OpeningHoursModel(
        open = this.open.toTimeInfoModel(),
        close = this.close.toTimeInfoModel()
    )
}

fun TimeInfo.toTimeInfoModel(): TimeInfoModel = TimeInfoModel(day, hour, minute)
