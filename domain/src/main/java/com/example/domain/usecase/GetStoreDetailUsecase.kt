package com.example.domain.usecase

import com.example.domain.model.StoreDetail
import com.example.domain.repository.StoreDetailRepository
import kotlinx.coroutines.flow.Flow

class GetStoreDetailUsecase(
    private val repository: StoreDetailRepository
) {
    suspend operator fun invoke(
        nwLong: Double,
        nwLat: Double,
        seLong: Double,
        seLat: Double
    ): Flow<List<StoreDetail>> =
        repository.getStoreDetail(
            nwLong,
            nwLat,
            seLong,
            seLat
        )
}