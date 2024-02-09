package com.example.data.source.remote

import com.example.data.dto.response.store.toDomainModel
import com.example.data.source.StoreDetailDataSource
import com.example.data.source.remote.api.StoreDetailApiService
import com.example.domain.model.map.StoreDetailModel
import javax.inject.Inject

class StoreDetailSourceImpl @Inject constructor(
    private val apiService: StoreDetailApiService
) : StoreDetailDataSource {
    override suspend fun getStoreDetail(
        nwLong: Double,
        nwLat: Double,
        swLong: Double,
        swLat: Double,
        seLong: Double,
        seLat: Double,
        neLong: Double,
        neLat: Double,
    ): Result<List<List<StoreDetailModel>>> {
        return runCatching {
            apiService.getStoreDetailsByLocation(
                nwLong,
                nwLat,
                swLong,
                swLat,
                seLong,
                seLat,
                neLong,
                neLat
            ).data.map { items -> items.map { it.toDomainModel() } }
        }.fold(onSuccess = {
            Result.success(it)
        }, onFailure = { e ->
            Result.failure(e)
        })
    }
}