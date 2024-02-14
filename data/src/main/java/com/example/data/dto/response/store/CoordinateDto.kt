package com.example.data.dto.response.store

import com.example.domain.model.map.CoordinateModel

data class CoordinateDto(
    val longitude: Double,
    val latitude: Double,
)

internal fun CoordinateDto.toDomainModel(): CoordinateModel =
    CoordinateModel(longitude = longitude, latitude = latitude)