package com.example.data.dto.response.store

import com.example.domain.model.map.OpeningHoursModel

data class OpeningHoursDto(
    val open: TimeInfoData,
    val close: TimeInfoData,
)

internal fun OpeningHoursDto.toDomainModel(): OpeningHoursModel =
    OpeningHoursModel(open.toDomainModel(), close.toDomainModel())
