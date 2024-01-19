package com.example.presentation.model

data class StoreDetail(
    val id: Long,
    val displayName: String,
    val primaryTypeDisplayName: String?,
    val formattedAddress: String,
    val phoneNumber: String?,
    val location: Coordinate,
    val regularOpeningHours: List<OpeningHours>,
    val localPhotos: List<String?>,
    val certificationName: List<StoreType>
)



