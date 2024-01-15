package com.example.data.source.remote

import com.example.data.dto.response.ResponseStoreDetailDto
import com.example.data.source.StoreDetailDataSource
import com.example.data.source.remote.api.StoreDetailApiService
import javax.inject.Inject

class StoreDetailSourceImpl @Inject constructor(
    private val apiService: StoreDetailApiService
) : StoreDetailDataSource {
    override suspend fun getStoreDetail(
        nwLong: Double,
        nwLat: Double,
        seLong: Double,
        seLat: Double
    ): List<ResponseStoreDetailDto> {
        return apiService.getStoreDetailsByLocation(nwLong, nwLat, seLong, seLat)
    }
}