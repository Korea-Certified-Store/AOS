package com.example.data.source.remote.api

import com.example.data.dto.response.ResponseStoreDetailDto
import retrofit2.http.GET
import retrofit2.http.Query

interface StoreDetailApiService {

    @GET("/api/v1/storecertification/byLocation")
    suspend fun getStoreDetailsByLocation(
        @Query(NORTH_WEST_LONG) nwLong: Double,
        @Query(NORTH_WEST_LAT) nwLat: Double,
        @Query(SOUTH_EAST_LONG) seLong: Double,
        @Query(SOUTH_EAST_LAT) seLat: Double
    ): List<ResponseStoreDetailDto>

    companion object {
        const val NORTH_WEST_LONG = "nwLong"
        const val NORTH_WEST_LAT = "nwLat"
        const val SOUTH_EAST_LONG = "seLong"
        const val SOUTH_EAST_LAT = "seLat"
    }
}