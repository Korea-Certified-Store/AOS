package com.example.data.source.remote

import com.example.data.dto.response.store.toDomainModel
import com.example.data.source.SearchStoreDataSource
import com.example.data.source.remote.api.SearchStoreApiService
import com.example.domain.model.map.StoreDetailModel
import javax.inject.Inject

class SearchStoreSourceImpl @Inject constructor(
    private val apiService: SearchStoreApiService
) : SearchStoreDataSource {
    override suspend fun searchStore(
        currLong: Double,
        currLat: Double,
        searchKeyword: String
    ): Result<List<StoreDetailModel>> {
        return runCatching {
            apiService.searchStoreByLocationAndKeyword(
                currLong,
                currLat,
                searchKeyword
            ).data.map { it.toDomainModel() }
        }.fold(onSuccess = {
            Result.success(it)
        }, onFailure = { e ->
            Result.failure(e)
        })
    }

}