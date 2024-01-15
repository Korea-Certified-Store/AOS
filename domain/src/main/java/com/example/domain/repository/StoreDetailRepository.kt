package com.example.domain.repository

import com.example.domain.model.StoreDetail
import kotlinx.coroutines.flow.Flow

interface StoreDetailRepository {
    suspend fun getStoreDetail(
        nwLong: Double,
        nwLat: Double,
        seLong: Double,
        seLat: Double): Flow<List<StoreDetail>>
}