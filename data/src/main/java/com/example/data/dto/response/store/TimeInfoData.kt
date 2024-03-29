package com.example.data.dto.response.store

import com.example.domain.model.map.TimeInfoModel

data class TimeInfoData(
    val day: String,
    val hour: Int,
    val minute: Int,
)

internal fun TimeInfoData.toDomainModel(): TimeInfoModel =
    TimeInfoModel(day = day, hour = hour, minute = minute)
