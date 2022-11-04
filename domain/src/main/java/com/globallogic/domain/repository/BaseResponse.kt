package com.globallogic.domain.repository

sealed class BaseResponse<T> {
    data class Success<T>(val data: T) : BaseResponse<T>()
    data class Failure<T>(val exception: Exception) : BaseResponse<T>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success data: $data]"
            is Failure -> "Error exception: ${exception.message}]"
        }
    }
}
