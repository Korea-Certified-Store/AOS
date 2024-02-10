package com.example.data.source

import com.example.domain.model.map.StoreDetailModel

interface SearchStoreDataSource {
    suspend fun searchStore(
        currLong: Double,
        currLat: Double,
        searchKeyword: String
    ): Result<List<StoreDetailModel>>
}