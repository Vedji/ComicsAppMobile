package com.example.comicsapp.data.model.api.response

import com.google.gson.annotations.SerializedName



sealed class ApiResponse<out T> {
    data class Success<out T>(val data: T) : ApiResponse<T>()
    data class Error(val data: ApiBody<ApiError>) : ApiResponse<Nothing>()
}