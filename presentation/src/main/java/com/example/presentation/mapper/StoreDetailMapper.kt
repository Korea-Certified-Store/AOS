package com.example.presentation.mapper

import com.example.domain.model.map.CoordinateModel
import com.example.presentation.model.Coordinate
import com.example.presentation.model.StoreDetail
import com.example.presentation.model.StoreType
import com.example.presentation.util.MainConstants.GREAT_STORE
import com.example.presentation.util.MainConstants.KIND_STORE

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
            GREAT_STORE -> StoreType.GREAT
            KIND_STORE -> StoreType.KIND
            else -> StoreType.SAFE
        }
    }.sortedBy { it.ordinal }
}