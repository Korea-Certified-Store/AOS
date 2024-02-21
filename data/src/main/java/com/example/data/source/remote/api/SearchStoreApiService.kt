package com.example.data.source.remote.api

import com.example.data.dto.response.BaseResponse
import com.example.data.dto.response.store.ResponseStoreDetailDto
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchStoreApiService {
    @GET("/api/storecertification/byLocationAndKeyword/v1")
    suspend fun searchStoreByLocationAndKeyword(
        @Query(CURR_LONG) currLong: Double,
        @Query(CURR_LAT) currLat: Double,
        @Query(SEARCH_KEYWORD) searchKeyword: String,
    ): BaseResponse<List<ResponseStoreDetailDto>>

    companion object {
        const val CURR_LONG = "currLong"
        const val CURR_LAT = "currLat"
        const val SEARCH_KEYWORD = "searchKeyword"
    }
}