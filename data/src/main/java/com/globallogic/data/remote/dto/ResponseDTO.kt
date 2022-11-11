package com.globallogic.data.remote.dto

data class ResponseDTO<T>(
    val resultCount: Int,
    val results: List<T>
)
