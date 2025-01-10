package com.example.comicsapp.utils

sealed class UiState<T>(
    val data: T? = null,
    val message: String? = null,
    val statusCode: Int? = null
) {
    class Success<T>(data: T) : UiState<T>(data)
    class Loading<T>(data: T? = null) : UiState<T>(data)
    class Error<T>(message: String, statusCode: Int, data: T? = null) : UiState<T>(data, message, statusCode)
}