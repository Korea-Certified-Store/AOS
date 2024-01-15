package com.example.presentation.model

data class StoreInfo(
    val storeId: Int,
    val displayName: String,
    val formattedAddress: String,
    val googlePlaceId: String,
    val internationalPhoneNumber: String,
    val primaryType: String,
    val location: CoordinateModel,
    val regularOpeningHours: String,
    val storeCertificationId: List<StoreType>
)