package com.example.domain.repository

import com.example.domain.model.StoreDetailModel

interface StoreDetailRepository {
    suspend fun getStoreDetail(
        nwLong: Double,
        nwLat: Double,
        seLong: Double,
        seLat: Double): Result<List<StoreDetailModel>>
}