package com.example.data.repository

import com.example.data.source.StoreDetailDataSource
import com.example.domain.model.StoreDetail
import com.example.domain.repository.StoreDetailRepository
import javax.inject.Inject

class StoreDetailRepositoryImpl @Inject constructor(
    private val dataSource: StoreDetailDataSource
) : StoreDetailRepository {

    override suspend fun getStoreDetail(
        nwLong: Double,
        nwLat: Double,
        seLong: Double,
        seLat: Double
    ): Result<List<StoreDetail>> {
        return dataSource.getStoreDetail(
            nwLong,
            nwLat,
            seLong,
            seLat
        )
    }
}