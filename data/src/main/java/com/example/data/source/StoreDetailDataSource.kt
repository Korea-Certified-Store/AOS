package com.example.data.source

import com.example.data.dto.response.ResponseStoreDetailDto

interface StoreDetailDataSource {
    suspend fun getStoreDetail(
        nwLong: Double,
        nwLat: Double,
        seLong: Double,
        seLat: Double
    ): List<ResponseStoreDetailDto>
}