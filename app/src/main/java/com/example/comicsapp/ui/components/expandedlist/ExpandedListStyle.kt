package com.example.comicsapp.ui.components.expandedlist

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

// Модель для передачи стилей
data class ExpandedListStyle(
    val textStyle: TextStyle,
    val textColor: Color,
    val backgroundColor: Color,
    val borderColor: Color,
    val borderWidth: Int,
    val headPadding: PaddingValues,
    val contentPadding: PaddingValues,
    val iconSize: Int,
    val cornerBasedShape: CornerBasedShape
) {
    companion object {
        @Composable
        fun default(): ExpandedListStyle {
            return ExpandedListStyle(
                textStyle = MaterialTheme.typography.headlineMedium,
                textColor = MaterialTheme.colorScheme.onSecondary,
                backgroundColor = MaterialTheme.colorScheme.onPrimary,
                borderColor = MaterialTheme.colorScheme.onSecondary,
                borderWidth = 1,
                headPadding = PaddingValues(8.dp),
                contentPadding = PaddingValues(8.dp),
                iconSize = 48,
                cornerBasedShape = MaterialTheme.shapes.medium
            )
        }

        @Composable
        fun primary(): ExpandedListStyle {
            return ExpandedListStyle(
                textStyle = MaterialTheme.typography.bodyLarge,
                textColor = MaterialTheme.colorScheme.onPrimary,
                backgroundColor = MaterialTheme.colorScheme.primary,
                borderColor = Color.Red,
                borderWidth = 2,
                headPadding = PaddingValues(8.dp),
                contentPadding = PaddingValues(8.dp),
                iconSize = 56,
                cornerBasedShape = MaterialTheme.shapes.medium
            )
        }

        @Composable
        fun secondary(): ExpandedListStyle {
            return ExpandedListStyle(
                textStyle = MaterialTheme.typography.bodyMedium,
                textColor = MaterialTheme.colorScheme.onSecondary,
                backgroundColor = MaterialTheme.colorScheme.secondary,
                borderColor = MaterialTheme.colorScheme.secondary,
                borderWidth = 1,
                headPadding = PaddingValues(8.dp),
                contentPadding = PaddingValues(8.dp),
                iconSize = 48,
                cornerBasedShape = MaterialTheme.shapes.medium
            )
        }

        @Composable
        fun success(): ExpandedListStyle {
            return ExpandedListStyle(
                textStyle = MaterialTheme.typography.bodyMedium,
                textColor = MaterialTheme.colorScheme.onPrimary,
                backgroundColor = Color(0xFF4CAF50), // Зеленый
                borderColor = Color(0xFF388E3C),
                borderWidth = 2,
                headPadding = PaddingValues(8.dp),
                contentPadding = PaddingValues(8.dp),
                iconSize = 50,
                cornerBasedShape = MaterialTheme.shapes.medium
            )
        }

        @Composable
        fun error(): ExpandedListStyle {
            return ExpandedListStyle(
                textStyle = MaterialTheme.typography.bodyMedium,
                textColor = MaterialTheme.colorScheme.onError,
                backgroundColor = MaterialTheme.colorScheme.error,
                borderColor = MaterialTheme.colorScheme.error,
                borderWidth = 2,
                headPadding = PaddingValues(8.dp),
                contentPadding = PaddingValues(8.dp),
                iconSize = 48,
                cornerBasedShape = MaterialTheme.shapes.medium
            )
        }

        @Composable
        fun warning(): ExpandedListStyle {
            return ExpandedListStyle(
                textStyle = MaterialTheme.typography.bodyMedium,
                textColor = MaterialTheme.colorScheme.onSecondary,
                backgroundColor = Color(0xFFFFC107), // Желтый
                borderColor = Color(0xFFFFA000),
                borderWidth = 2,
                headPadding = PaddingValues(8.dp),
                contentPadding = PaddingValues(8.dp),
                iconSize = 48,
                cornerBasedShape = MaterialTheme.shapes.medium
            )
        }

        @Composable
        fun info(): ExpandedListStyle {
            return ExpandedListStyle(
                textStyle = MaterialTheme.typography.bodyMedium,
                textColor = MaterialTheme.colorScheme.onPrimary,
                backgroundColor = Color(0xFF2196F3), // Синий
                borderColor = Color(0xFF1976D2),
                borderWidth = 1,
                headPadding = PaddingValues(8.dp),
                contentPadding = PaddingValues(8.dp),
                iconSize = 48,
                cornerBasedShape = MaterialTheme.shapes.medium
            )
        }
    }
}
