package com.example.data.dto.response

import com.example.domain.model.TimeInfoModel
import kotlinx.serialization.Serializable

@Serializable
data class TimeInfoData(
    val day: String,
    val hour: Int,
    val minute: Int,
)

internal fun TimeInfoData.toDomainModel(): TimeInfoModel =
    TimeInfoModel(day = day, hour = hour, minute = minute)
