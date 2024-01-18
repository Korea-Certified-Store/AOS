package com.example.domain.usecase

import com.example.domain.model.StoreDetailModel
import com.example.domain.repository.StoreDetailRepository

class GetStoreDetailUseCase(
    private val repository: StoreDetailRepository
) {
    suspend operator fun invoke(
        nwLong: Double,
        nwLat: Double,
        seLong: Double,
        seLat: Double
    ): Result<List<StoreDetailModel>> =
        repository.getStoreDetail(
            nwLong,
            nwLat,
            seLong,
            seLat
        )
}