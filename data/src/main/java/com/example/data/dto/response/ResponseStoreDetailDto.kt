package com.example.data.dto.response

import com.example.domain.model.Coordinate
import com.example.domain.model.OpeningHours
import com.example.domain.model.StoreDetail
import com.example.domain.model.TimeInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseStoreDetailDto(
    @SerialName("id")
    val id: Long,
    @SerialName("displayName")
    val displayName: String,
    @SerialName("primaryTypeDisplayName")
    val primaryTypeDisplayName: String?,
    @SerialName("formattedAddress")
    val formattedAddress: String,
    @SerialName("phoneNumber")
    val phoneNumber: String?,
    @SerialName("location")
    val location: CoordinateData,
    @SerialName("regularOpeningHours")
    val regularOpeningHours: List<OpeningHoursData>,
    @SerialName("localPhotos")
    val localPhotos: List<String?>,
    @SerialName("certificationName")
    val certificationName: List<String>
) {
    @Serializable
    data class CoordinateData(
        @SerialName("longitude")
        val longitude: Double,
        @SerialName("latitude")
        val latitude: Double,
    ) {
        fun toCoordinate() = run {
            Coordinate(longitude, latitude)
        }
    }

    @Serializable
    data class OpeningHoursData(
        @SerialName("open")
        val open: TimeInfoData,
        @SerialName("close")
        val close: TimeInfoData,
    ) {
        fun toOpeningHours() = run {
            OpeningHours(open.toTimeInfo(), close.toTimeInfo())
        }

        @Serializable
        data class TimeInfoData(
            @SerialName("day")
            val day: String,
            @SerialName("hour")
            val hour: Int,
            @SerialName("minute")
            val minute: Int,
        ) {

            fun toTimeInfo() = run {
                TimeInfo(day, hour, minute)
            }
        }
    }

    fun toStoreDetail() = run {
        StoreDetail(
            id,
            displayName,
            primaryTypeDisplayName,
            formattedAddress,
            phoneNumber,
            location.toCoordinate(),
            regularOpeningHours.map { it.toOpeningHours() },
            localPhotos,
            certificationName
        )
    }
}