package com.example.domain.model

data class StoreDetail(
    val id: Long,
    val displayName: String,
    val primaryTypeDisplayName: String?,
    val formattedAddress: String,
    val phoneNumber: String?,
    val location: Coordinate,
    val regularOpeningHours: List<OpeningHours>,
    val localPhotos: List<String?>,
    val certificationName: List<String>
)

data class Coordinate(
    val longitude: Double,
    val latitude: Double,
)

data class OpeningHours(
    val open: TimeInfo,
    val close: TimeInfo,
)

data class TimeInfo(
    val day: String,
    val hour: Int,
    val minute: Int,
)