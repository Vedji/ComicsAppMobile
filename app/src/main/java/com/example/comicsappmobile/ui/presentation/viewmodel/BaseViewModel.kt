package com.example.comicsappmobile.ui.presentation.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay

open class BaseViewModel: ViewModel() {

    suspend fun <T> loadWithState(
        callResponse: suspend () -> UiState<out T>
    ): UiState<T> {
        return try {
            // Выполняем вызов функции
            callResponse() as UiState<T>
        } catch (e: IllegalArgumentException) {
            // Возвращаем ошибочное состояние
            UiState.Error(
                message = e.localizedMessage,
                typeError = "Network",
                statusCode = 500
            )
        }
    }
}