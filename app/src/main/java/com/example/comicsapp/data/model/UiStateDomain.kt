package com.example.comicsapp.data.model

sealed class UiStateDomain<out T> {
    object Loading : ResponseModel<Nothing>()

    data class Success<T>(
        val data: T,
        val metadata: Any? = null,
        val status: String = "success"
    ) : ResponseModel<T>()
    data class Error(
        val type: String,
        val message: ErrorDataModel,
        val status: String = "error"
    ) : ResponseModel<Nothing>()
}