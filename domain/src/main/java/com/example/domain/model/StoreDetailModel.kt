package com.example.domain.model

data class StoreDetailModel(
    val id: Long,
    val displayName: String,
    val primaryTypeDisplayName: String?,
    val formattedAddress: String,
    val phoneNumber: String?,
    val location: CoordinateModel,
    val regularOpeningHours: List<OpeningHoursModel>,
    val localPhotos: List<String?>,
    val certificationName: List<String>
)