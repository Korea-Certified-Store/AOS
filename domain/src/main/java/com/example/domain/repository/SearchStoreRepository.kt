package com.example.domain.repository

import com.example.domain.model.map.StoreDetailModel

interface SearchStoreRepository {
    suspend fun searchStore(
        currLong: Double,
        currLat: Double,
        searchKeyword: String
    ): Result<List<StoreDetailModel>>
}