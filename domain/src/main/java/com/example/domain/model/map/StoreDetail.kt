package com.example.domain.model.map

data class StoreDetail(
    val id: Long,
    val displayName: String,
    val primaryTypeDisplayName: String?,
    val formattedAddress: String,
    val phoneNumber: String?,
    val location: CoordinateModel,
    val operatingType: String,
    val timeDescription: String,
    val localPhotos: List<String?>,
    val certificationName: List<String>
)