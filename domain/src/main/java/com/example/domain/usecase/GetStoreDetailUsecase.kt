package com.example.domain.usecase

import com.example.domain.model.StoreDetail
import com.example.domain.repository.StoreDetailRepository

class GetStoreDetailUsecase(
    private val repository: StoreDetailRepository
) {
    suspend operator fun invoke(
        nwLong: Double,
        nwLat: Double,
        seLong: Double,
        seLat: Double
    ): Result<List<StoreDetail>> =
        repository.getStoreDetail(
            nwLong,
            nwLat,
            seLong,
            seLat
        )
}