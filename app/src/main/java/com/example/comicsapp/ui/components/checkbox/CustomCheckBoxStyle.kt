package com.example.comicsapp.ui.components.checkbox

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class CustomCheckBoxStyle(
    val onBackgroundColor: Color,
    val offBackgroundColor: Color,
    val onTextColor: Color,
    val offTextColor: Color,
    val shape: CornerBasedShape,
    val textStyle: TextStyle,
    val contentPadding: PaddingValues, // добавлено для настройки внутреннего отступа
    val borderWidth: Dp = 1.dp, // добавлено для настройки ширины границы
    val onBorderColor: Color = Color.Transparent, // добавлено для цвета границы в состоянии "выбрано"
    val offBorderColor: Color = Color.Gray // добавлено для цвета границы в состоянии "не выбрано"
) {
    companion object {
        @Composable
        fun default(): CustomCheckBoxStyle {
            return CustomCheckBoxStyle(
                onBackgroundColor = MaterialTheme.colorScheme.primary,
                offBackgroundColor = MaterialTheme.colorScheme.secondary,
                onTextColor = MaterialTheme.colorScheme.onPrimary,
                offTextColor = MaterialTheme.colorScheme.onSecondary,
                shape = MaterialTheme.shapes.extraSmall,
                textStyle = MaterialTheme.typography.bodyLarge,
                contentPadding = PaddingValues(10.dp),
                borderWidth = 1.dp,
                onBorderColor = Color.Blue,
                offBorderColor = Color.Red
            )
        }

        @Composable
        fun primary(): CustomCheckBoxStyle {
            return CustomCheckBoxStyle(
                onBackgroundColor = MaterialTheme.colorScheme.primary,
                offBackgroundColor = MaterialTheme.colorScheme.secondary,
                onTextColor = MaterialTheme.colorScheme.onPrimary,
                offTextColor = MaterialTheme.colorScheme.onPrimary,
                shape = MaterialTheme.shapes.extraSmall,
                textStyle = MaterialTheme.typography.bodyLarge,
                contentPadding = PaddingValues(8.dp),
                borderWidth = 2.dp,
                onBorderColor = MaterialTheme.colorScheme.onPrimary,
                offBorderColor = MaterialTheme.colorScheme.primary
            )
        }

        @Composable
        fun secondary(): CustomCheckBoxStyle {
            return CustomCheckBoxStyle(
                onBackgroundColor = MaterialTheme.colorScheme.secondary,
                offBackgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                onTextColor = MaterialTheme.colorScheme.onSecondary,
                offTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
                shape = MaterialTheme.shapes.medium,
                textStyle = MaterialTheme.typography.bodyMedium,
                contentPadding = PaddingValues(6.dp),
                borderWidth = 1.dp,
                onBorderColor = MaterialTheme.colorScheme.onSecondary,
                offBorderColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }

    }

}