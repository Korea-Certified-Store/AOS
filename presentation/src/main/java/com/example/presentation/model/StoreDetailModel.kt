package com.example.presentation.model

data class StoreDetailModel(
    val id: Long,
    val displayName: String,
    val primaryTypeDisplayName: String?,
    val formattedAddress: String,
    val phoneNumber: String?,
    val location: CoordinateModel,
    val regularOpeningHours: List<OpeningHoursModel?>,
    val localPhotos: List<String?>,
    val certificationName: List<StoreType>
)

data class OpeningHoursModel(
    val open: TimeInfoModel,
    val close: TimeInfoModel,
)

data class TimeInfoModel(
    val day: String,
    val hour: Int,
    val minute: Int,
)