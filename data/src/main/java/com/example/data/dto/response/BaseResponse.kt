package com.example.data.dto.response

data class BaseResponse<T>(
    val code: Int,
    val message: String,
    val data: T,
)