package com.example.data.dto.response

import com.example.domain.model.Coordinate
import com.example.domain.model.OpeningHours
import com.example.domain.model.StoreDetail
import com.example.domain.model.TimeInfo
import kotlinx.serialization.Serializable

@Serializable
data class ResponseStoreDetailDto(
    val id: Long,
    val displayName: String,
    val primaryTypeDisplayName: String?,
    val formattedAddress: String,
    val phoneNumber: String?,
    val location: CoordinateData,
    val regularOpeningHours: List<OpeningHoursData>,
    val localPhotos: List<String?>,
    val certificationName: List<String>
) {
    @Serializable
    data class CoordinateData(
        val longitude: Double,
        val latitude: Double,
    ) {
        fun toDomainModel(): Coordinate = Coordinate(longitude = longitude, latitude = latitude)
    }

    @Serializable
    data class OpeningHoursData(
        val open: TimeInfoData,
        val close: TimeInfoData,
    ) {
        fun toDomainModel(): OpeningHours =
            OpeningHours(open.toDomainTimeInfoModel(), close.toDomainTimeInfoModel())
    }

    @Serializable
    data class TimeInfoData(
        val day: String,
        val hour: Int,
        val minute: Int,
    ) {
        fun toDomainTimeInfoModel(): TimeInfo = TimeInfo(day = day, hour = hour, minute = minute)
    }

    fun toStoreDetail(): StoreDetail = StoreDetail(
        id = id,
        displayName = displayName,
        primaryTypeDisplayName = primaryTypeDisplayName,
        formattedAddress = formattedAddress,
        phoneNumber = phoneNumber,
        location = location.toDomainModel(),
        regularOpeningHours = regularOpeningHours.map { openingHoursData -> openingHoursData.toDomainModel() },
        localPhotos = localPhotos,
        certificationName = certificationName
    )
}