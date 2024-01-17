package com.example.data.source

import com.example.domain.model.StoreDetail

interface StoreDetailDataSource {
    suspend fun getStoreDetail(
        nwLong: Double,
        nwLat: Double,
        seLong: Double,
        seLat: Double
    ): List<StoreDetail>
}