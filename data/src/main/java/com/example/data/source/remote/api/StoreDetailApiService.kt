package com.example.data.source.remote.api

import com.example.data.dto.response.BaseResponse
import com.example.data.dto.response.store.ResponseStoreDetailDto
import retrofit2.http.GET
import retrofit2.http.Query

interface  StoreDetailApiService {
    @GET("/api/v2/storecertification/byLocation")
    suspend fun getStoreDetailsByLocation(
        @Query(NORTH_WEST_LONG) nwLong: Double,
        @Query(NORTH_WEST_LAT) nwLat: Double,
        @Query(SOUTH_WEST_LONG) swLong: Double,
        @Query(SOUTH_WEST_LAT) swLat: Double,
        @Query(SOUTH_EAST_LONG) seLong: Double,
        @Query(SOUTH_EAST_LAT) seLat: Double,
        @Query(NORTH_EAST_LONG) neLong: Double,
        @Query(NORTH_EAST_LAT) neLat: Double
    ): BaseResponse<List<List<ResponseStoreDetailDto>>>

    companion object {
        const val NORTH_WEST_LONG = "nwLong"
        const val NORTH_WEST_LAT = "nwLat"
        const val SOUTH_WEST_LONG = "swLong"
        const val SOUTH_WEST_LAT = "swLat"
        const val SOUTH_EAST_LONG = "seLong"
        const val SOUTH_EAST_LAT = "seLat"
        const val NORTH_EAST_LONG = "neLong"
        const val NORTH_EAST_LAT = "neLat"
    }
}