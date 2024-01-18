package com.example.data.source.remote

import com.example.data.dto.response.toDomainModel
import com.example.data.source.StoreDetailDataSource
import com.example.data.source.remote.api.StoreDetailApiService
import com.example.domain.model.StoreDetail
import javax.inject.Inject

class StoreDetailSourceImpl @Inject constructor(
    private val apiService: StoreDetailApiService
) : StoreDetailDataSource {
    override suspend fun getStoreDetail(
        nwLong: Double,
        nwLat: Double,
        seLong: Double,
        seLat: Double
    ): Result<List<StoreDetail>> {
        return runCatching {
            apiService.getStoreDetailsByLocation(
                nwLong,
                nwLat,
                seLong,
                seLat
            ).data.map { it.toDomainModel() }
        }.fold(onSuccess = {
            Result.success(it)
        }, onFailure = { e ->
            Result.failure(e)
        })
    }
}