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
    val location: CoordinateDto,
    val regularOpeningHours: List<OpeningHoursDto>,
    val localPhotos: List<String?>,
    val certificationName: List<String>
) {
    @Serializable
    data class CoordinateDto(
        val longitude: Double,
        val latitude: Double,
    )

    @Serializable
    data class OpeningHoursDto(
        val open: TimeInfoData,
        val close: TimeInfoData,
    )

    @Serializable
    data class TimeInfoData(
        val day: String,
        val hour: Int,
        val minute: Int,
    )
}

internal fun ResponseStoreDetailDto.toDomainModel(): StoreDetail = StoreDetail(
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

internal fun ResponseStoreDetailDto.CoordinateDto.toDomainModel(): Coordinate =
    Coordinate(longitude = longitude, latitude = latitude)

internal fun ResponseStoreDetailDto.OpeningHoursDto.toDomainModel(): OpeningHours =
    OpeningHours(open.toDomainModel(), close.toDomainModel())

internal fun ResponseStoreDetailDto.TimeInfoData.toDomainModel(): TimeInfo =
    TimeInfo(day = day, hour = hour, minute = minute)
