package com.example.presentation.mapper

import com.example.domain.model.map.CoordinateModel
import com.example.presentation.model.Coordinate
import com.example.presentation.model.StoreDetail
import com.example.presentation.model.StoreType

fun com.example.domain.model.map.StoreDetail.toUiModel(): StoreDetail =
    StoreDetail(
        id,
        displayName,
        primaryTypeDisplayName,
        formattedAddress,
        phoneNumber,
        location.toUiModel(),
        operatingType,
        timeDescription,
        localPhotos,
        certificationName.toUiModel()
    )

fun CoordinateModel.toUiModel(): Coordinate =
    Coordinate(latitude, longitude)

fun List<String>.toUiModel(): List<StoreType> {
    return this.map {
        when (it) {
            "모범음식점" -> StoreType.GREAT
            "착한가격업소" -> StoreType.KIND
            else -> StoreType.SAFE
        }
    }.sortedBy { it.ordinal }
}