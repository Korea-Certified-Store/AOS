package com.example.data.repository

import com.example.data.source.SearchStoreDataSource
import com.example.domain.model.map.StoreDetailModel
import com.example.domain.repository.SearchStoreRepository
import javax.inject.Inject

class SearchStoreRepositoryImpl @Inject constructor(
    private val dataSource: SearchStoreDataSource
) : SearchStoreRepository {
    override suspend fun searchStore(
        currLong: Double,
        currLat: Double,
        searchKeyword: String
    ): Result<List<StoreDetailModel>> {
        return dataSource.searchStore(currLong, currLat, searchKeyword)
    }
}