package com.example.comicsappmobile.ui.presentation.viewmodel

import com.example.comicsappmobile.data.dto.entities.metadata.Pagination

sealed class UiState<T>(
    var data: T? = null,
    val message: String? = null,
    val typeError: String? = null,
    val statusCode: Int = 200,
    val metadata: Pagination? = null
) {
    class Success<T>(data: T, metadata: Pagination? = null) : UiState<T>(data = data, metadata = metadata)
    class Loading<T>(data: T? = null) : UiState<T>(data)
    class Error<T>(
        data: T? = null,
        message: String? = null,
        typeError: String? = null,
        statusCode: Int = 200
    ) : UiState<T>(data, message, typeError, statusCode)
}