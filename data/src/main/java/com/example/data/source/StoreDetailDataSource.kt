package com.example.data.source

import com.example.domain.model.map.StoreDetailModel

interface StoreDetailDataSource {
    suspend fun getStoreDetail(
        nwLong: Double,
        nwLat: Double,
        swLong: Double,
        swLat: Double,
        seLong: Double,
        seLat: Double,
        neLong: Double,
        neLat: Double,
    ): Result<List<List<StoreDetailModel>>>
}