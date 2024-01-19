package com.example.domain.usecase

import com.example.domain.model.StoreDetailModel
import com.example.domain.repository.StoreDetailRepository

class GetStoreDetailUseCase(
    private val repository: StoreDetailRepository
) {
    suspend operator fun invoke(
        nwLong: Double,
        nwLat: Double,
        swLong: Double,
        swLat: Double,
        seLong: Double,
        seLat: Double,
        neLong: Double,
        neLat: Double,
    ): Result<List<StoreDetailModel>> =
        repository.getStoreDetail(
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