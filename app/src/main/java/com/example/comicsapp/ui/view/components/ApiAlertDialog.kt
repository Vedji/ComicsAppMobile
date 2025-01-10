package com.example.comicsapp.ui.view.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.example.comicsapp.domain.model.DomainError

@Composable
fun ApiErrorDialog(err: DomainError?, showDialog: Boolean, onDismiss: () -> Unit) {
    if (showDialog && err != null) {
        AlertDialog(
            onDismissRequest = {
                // Действие при закрытии диалога
                onDismiss()
            },
            title = {
                Text(
                    text = "ERROR ${err.getTypeError()}",
                    style = MaterialTheme.typography.headlineMedium
                )
            },
            text = {
                Column {
                    Text(
                        text = "HTTP STATUS: ${err.getStatusCode()}",
                        style = MaterialTheme.typography.bodyLarge
                        )
                    Text(
                        text = "MESSAGE: ${err.getMessage()}",
                        style = MaterialTheme.typography.bodyLarge
                        )
                }
            },
            confirmButton = {
                TextButton(onClick = { onDismiss() }) {
                    Text("ОК")
                }
            },
            dismissButton = {
                TextButton(onClick = { onDismiss() }) {
                    Text("Отмена")
                }
            }
        )
    }
}