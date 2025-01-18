package com.example.comicsappmobile.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.comicsappmobile.ui.presentation.viewmodel.UiState

@Composable
fun<T> ThemedStateView(
    uiState: UiState<out T>,
    onSuccess: @Composable () -> Unit,
    onLoading: @Composable () -> Unit = { Text("Loading...") },
    onError: @Composable () -> Unit = { Text(text = uiState.message ?: "No error message") },
){
    when (uiState){
        is UiState.Error -> onError()
        is UiState.Loading -> onLoading()
        is UiState.Success -> onSuccess()
    }
}