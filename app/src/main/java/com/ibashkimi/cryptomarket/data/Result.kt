package com.ibashkimi.cryptomarket.data

sealed class ApiResponse<T> {
    class Success<T>(val result: T) : ApiResponse<T>()
    class Failure<T>(val error: Any? = null) : ApiResponse<T>()
}