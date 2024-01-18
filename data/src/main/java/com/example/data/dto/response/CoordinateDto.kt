package com.example.data.dto.response

import com.example.domain.model.Coordinate
import kotlinx.serialization.Serializable

@Serializable
data class CoordinateDto(
    val longitude: Double,
    val latitude: Double,
)

internal fun CoordinateDto.toDomainModel(): Coordinate =
    Coordinate(longitude = longitude, latitude = latitude)