package com.example.data.dto.response

import com.example.domain.model.TimeInfo
import kotlinx.serialization.Serializable

@Serializable
data class TimeInfoData(
    val day: String,
    val hour: Int,
    val minute: Int,
)

internal fun TimeInfoData.toDomainModel(): TimeInfo =
    TimeInfo(day = day, hour = hour, minute = minute)
