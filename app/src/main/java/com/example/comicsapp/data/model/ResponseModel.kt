package com.example.comicsapp.data.model

sealed class ResponseModel<out T> {
    data class Success<T>(
        val data: T,
        val metadata: Any? = null,
        val status: String = "success"
    ) : ResponseModel<T>()

    data class Error(
        val data: ErrorDataModel,
        val status: String = "error"
    ) : ResponseModel<Nothing>()
}

