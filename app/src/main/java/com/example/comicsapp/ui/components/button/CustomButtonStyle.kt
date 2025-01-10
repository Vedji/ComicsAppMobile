package com.example.comicsapp.ui.components.button

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

// Модель для передачи стилей
data class CustomButtonStyle(
    val color: Color,
    val background: Color,
    val style: TextStyle,
    val shape: CornerBasedShape
) {
    companion object {
        @Composable
        fun primary(): CustomButtonStyle {
            return CustomButtonStyle(
                color = MaterialTheme.colorScheme.onPrimary,
                background = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyLarge,
                shape = MaterialTheme.shapes.small
            )
        }

        @Composable
        fun secondary(): CustomButtonStyle {
            return CustomButtonStyle(
                color = MaterialTheme.colorScheme.onSecondary,
                background = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodyMedium,
                shape = MaterialTheme.shapes.medium
            )
        }

        @Composable
        fun success(): CustomButtonStyle {
            return CustomButtonStyle(
                color = MaterialTheme.colorScheme.onPrimary,
                background = Color(0xFF4CAF50), // Зеленый
                style = MaterialTheme.typography.bodyMedium,
                shape = MaterialTheme.shapes.medium
            )
        }

        @Composable
        fun error(): CustomButtonStyle {
            return CustomButtonStyle(
                color = MaterialTheme.colorScheme.onError,
                background = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                shape = MaterialTheme.shapes.medium
            )
        }

        @Composable
        fun warning(): CustomButtonStyle {
            return CustomButtonStyle(
                color = MaterialTheme.colorScheme.onSecondary,
                background = Color(0xFFFFC107), // Желтый
                style = MaterialTheme.typography.bodyMedium,
                shape = MaterialTheme.shapes.medium
            )
        }

        @Composable
        fun info(): CustomButtonStyle {
            return CustomButtonStyle(
                color = MaterialTheme.colorScheme.onPrimary,
                background = Color(0xFF2196F3), // Синий
                style = MaterialTheme.typography.bodyMedium,
                shape = MaterialTheme.shapes.medium
            )
        }
    }
}
