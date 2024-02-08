package com.example.data.repository

import com.example.data.source.StoreDetailDataSource
import com.example.domain.model.map.StoreDetailModel
import com.example.domain.repository.StoreDetailRepository
import javax.inject.Inject

class StoreDetailRepositoryImpl @Inject constructor(
    private val dataSource: StoreDetailDataSource
) : StoreDetailRepository {

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
        return dataSource.getStoreDetail(
            nwLong,
            nwLat,
            swLong,
            swLat,
            seLong,
            seLat,
            neLong,
            neLat
        )
    }
}