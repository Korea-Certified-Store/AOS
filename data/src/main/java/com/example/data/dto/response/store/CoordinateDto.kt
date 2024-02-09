package com.example.data.dto.response.store

import com.example.domain.model.map.CoordinateModel
import kotlinx.serialization.Serializable

data class CoordinateDto(
    val longitude: Double,
    val latitude: Double,
)

internal fun CoordinateDto.toDomainModel(): CoordinateModel =
    CoordinateModel(longitude = longitude, latitude = latitude)