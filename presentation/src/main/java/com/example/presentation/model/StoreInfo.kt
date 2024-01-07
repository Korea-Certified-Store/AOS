package com.example.presentation.model

data class StoreInfo(
    val storeName: String,
    val type: String,
    val address: String,
    val operatingTime: String,
    val coordinate: Coordinate,
    val contact: String,
    val picture: String
)
