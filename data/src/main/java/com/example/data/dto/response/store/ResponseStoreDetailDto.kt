package com.example.data.dto.response.store

import com.example.domain.model.map.StoreDetailModel
import kotlinx.serialization.Serializable

data class ResponseStoreDetailDto(
    val id: Long,
    val displayName: String,
    val primaryTypeDisplayName: String?,
    val formattedAddress: String,
    val phoneNumber: String?,
    val location: CoordinateDto,
    val regularOpeningHours: List<OpeningHoursDto>,
    val localPhotos: List<String>,
    val certificationName: List<String>
)

internal fun ResponseStoreDetailDto.toDomainModel(): StoreDetailModel = StoreDetailModel(
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