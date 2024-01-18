package com.example.data.source

import com.example.domain.model.StoreDetailModel

interface StoreDetailDataSource {
    suspend fun getStoreDetail(
        nwLong: Double,
        nwLat: Double,
        seLong: Double,
        seLat: Double
    ): Result<List<StoreDetailModel>>
}