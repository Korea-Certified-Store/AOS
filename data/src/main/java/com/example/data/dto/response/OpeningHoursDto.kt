package com.example.data.dto.response

import com.example.domain.model.OpeningHours
import kotlinx.serialization.Serializable

@Serializable
data class OpeningHoursDto(
    val open: TimeInfoData,
    val close: TimeInfoData,
)

internal fun OpeningHoursDto.toDomainModel(): OpeningHours =
    OpeningHours(open.toDomainModel(), close.toDomainModel())
