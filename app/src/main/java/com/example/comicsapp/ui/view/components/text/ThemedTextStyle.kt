package com.example.comicsapp.ui.view.components.text

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign

data class ThemedTextStyle(
    val textStyle: TextStyle,
    val textColor: Color,
    val backgroundColor: Color,
    val textAlign: TextAlign,
    val padding: PaddingValues,
    val cornerBasedShape: CornerBasedShape
) {
    companion object {
        @Composable
        fun default(): ThemedTextStyle {
            return ThemedTextStyle(
                textStyle = MaterialTheme.typography.headlineMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface),
                textColor = MaterialTheme.colorScheme.onSurface,
                backgroundColor = MaterialTheme.colorScheme.surface,
                textAlign = TextAlign.Start,
                padding = PaddingValues(0.dp),
                cornerBasedShape = MaterialTheme.shapes.medium
            )
        }

        @Composable
        fun primary(): ThemedTextStyle {
            return ThemedTextStyle(
                textStyle = MaterialTheme.typography.displaySmall,
                textColor = MaterialTheme.colorScheme.onSurface,
                backgroundColor = Color.Transparent,
                textAlign = TextAlign.Start,
                padding = PaddingValues(0.dp),
                cornerBasedShape = MaterialTheme.shapes.medium
            )
        }

        @Composable
        fun secondary(): ThemedTextStyle {
            return ThemedTextStyle(
                textStyle = MaterialTheme.typography.bodyMedium,
                textColor = MaterialTheme.colorScheme.onSurface,
                backgroundColor = Color.Transparent,
                textAlign = TextAlign.Justify,
                padding = PaddingValues(0.dp),
                cornerBasedShape = MaterialTheme.shapes.medium
            )
        }

        @Composable
        fun warning(): ThemedTextStyle {
            return ThemedTextStyle(
                textStyle = LocalTextStyle.current.copy(color = Color(0xFFFFA726)),
                textColor = Color(0xFFFFA726),
                backgroundColor = Color(0xFFFFF3E0),
                textAlign = TextAlign.Start,
                padding = PaddingValues(0.dp),
                cornerBasedShape = MaterialTheme.shapes.medium
            )
        }

        @Composable
        fun info(): ThemedTextStyle {
            return ThemedTextStyle(
                textStyle = LocalTextStyle.current.copy(color = Color(0xFF29B6F6)),
                textColor = Color(0xFF29B6F6),
                backgroundColor = Color(0xFFE1F5FE),
                textAlign = TextAlign.Start,
                padding = PaddingValues(0.dp),
                cornerBasedShape = MaterialTheme.shapes.medium
            )
        }
    }
}

