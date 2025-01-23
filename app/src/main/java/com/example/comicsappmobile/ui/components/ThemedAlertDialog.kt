package com.example.comicsappmobile.ui.components

import androidx.compose.material.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
fun ThemedAlertDialog(
    titleText: String = "",
    messageText: String = "",
    onConfirm: () -> Unit = {},
    onDismiss: () -> Unit = {},
    onDismissRequest: () -> Unit = { onDismiss() }
) {
    val openDialog = remember { mutableStateOf(true) }

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
                onDismissRequest()
            },
            title = { Text(text = titleText) },
            text = { Text(text = messageText) },
            backgroundColor = MaterialTheme.colorScheme.primaryContainer,
            confirmButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                        onConfirm()
                    }) { Text("Ok") }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                        onDismiss()
                    }) { Text("Отменить") }
            }
        )
    }
}