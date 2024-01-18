package com.example.data.dto.response

import com.example.domain.model.OpeningHoursModel
import kotlinx.serialization.Serializable

@Serializable
data class OpeningHoursDto(
    val open: TimeInfoData,
    val close: TimeInfoData,
)

internal fun OpeningHoursDto.toDomainModel(): OpeningHoursModel =
    OpeningHoursModel(open.toDomainModel(), close.toDomainModel())
