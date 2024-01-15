package com.example.data.repository

import com.example.data.source.StoreDetailDataSource
import com.example.domain.model.StoreDetail
import com.example.domain.repository.StoreDetailRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class StoreDetailRepositoryImpl @Inject constructor(
    private val dataSource: StoreDetailDataSource
) : StoreDetailRepository {

    override suspend fun getStoreDetail(
        nwLong: Double,
        nwLat: Double,
        seLong: Double,
        seLat: Double
    ): Flow<List<StoreDetail>> {
        return flow {
            val result = runCatching {
                dataSource.getStoreDetail(
                    nwLong,
                    nwLat,
                    seLong,
                    seLat
                ).map { storeDetail -> storeDetail.toStoreDetail() }
            }
            emit(result.getOrDefault(emptyList()))
        }
    }
}