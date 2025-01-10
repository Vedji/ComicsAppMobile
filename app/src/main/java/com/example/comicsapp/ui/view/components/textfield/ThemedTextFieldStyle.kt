package com.example.comicsapp.ui.view.components.textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.comicsapp.ui.view.theme.ComicsAppTheme
import androidx.compose.foundation.layout.PaddingValues

data class ThemedTextFieldStyle(
    val textStyle: TextStyle,
    val placeholderStyle: TextStyle,
    val isFocusedBorderColor: Color,
    val unfocusedBorderColor: Color,
    val backgroundColor: Color,
    val cornerRadius: Dp,
    val padding: PaddingValues,
    val focusedBorderWidth: Dp,
    val unfocusedBorderWidth: Dp,
    val textColor: Color,
    val cursorColor: Color,
    val errorBorderColor: Color,
    val disabledBorderColor: Color,
    val disabledBackgroundColor: Color,
    val disabledTextColor: Color,
    val errorTextColor: Color,
    val shadowElevation: Dp,
    val shadowColor: Color
) {
    companion object {
        @Composable
        fun default(): ThemedTextFieldStyle {
            return ThemedTextFieldStyle(
                textStyle = LocalTextStyle.current,
                placeholderStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)),
                isFocusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                backgroundColor = MaterialTheme.colorScheme.surface,
                cornerRadius = 8.dp,
                padding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                focusedBorderWidth = 2.dp,
                unfocusedBorderWidth = 1.dp,
                textColor = MaterialTheme.colorScheme.onSurface,
                cursorColor = MaterialTheme.colorScheme.primary,
                errorBorderColor = MaterialTheme.colorScheme.error,
                disabledBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                disabledBackgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                errorTextColor = MaterialTheme.colorScheme.error,
                shadowElevation = 0.dp,
                shadowColor = Color.Black.copy(alpha = 0.1f)
            )
        }

        @Composable
        fun primary(): ThemedTextFieldStyle {
            return ThemedTextFieldStyle(
                textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onPrimary),
                placeholderStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f)),
                isFocusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                cornerRadius = 8.dp,
                padding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                focusedBorderWidth = 2.dp,
                unfocusedBorderWidth = 1.dp,
                textColor = MaterialTheme.colorScheme.onPrimary,
                cursorColor = MaterialTheme.colorScheme.primary,
                errorBorderColor = MaterialTheme.colorScheme.error,
                disabledBorderColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f),
                disabledBackgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                disabledTextColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f),
                errorTextColor = MaterialTheme.colorScheme.error,
                shadowElevation = 0.dp,
                shadowColor = Color.Black.copy(alpha = 0.1f)
            )
        }

        @Composable
        fun secondary(): ThemedTextFieldStyle {
            return ThemedTextFieldStyle(
                textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onSecondary),
                placeholderStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.5f)),
                isFocusedBorderColor = MaterialTheme.colorScheme.secondary,
                unfocusedBorderColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                cornerRadius = 8.dp,
                padding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                focusedBorderWidth = 2.dp,
                unfocusedBorderWidth = 1.dp,
                textColor = MaterialTheme.colorScheme.onSecondary,
                cursorColor = MaterialTheme.colorScheme.secondary,
                errorBorderColor = MaterialTheme.colorScheme.error,
                disabledBorderColor = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.2f),
                disabledBackgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                disabledTextColor = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.5f),
                errorTextColor = MaterialTheme.colorScheme.error,
                shadowElevation = 0.dp,
                shadowColor = Color.Black.copy(alpha = 0.1f)
            )
        }

        @Composable
        fun success(): ThemedTextFieldStyle {
            return ThemedTextFieldStyle(
                textStyle = LocalTextStyle.current.copy(color = Color.White),
                placeholderStyle = LocalTextStyle.current.copy(color = Color.White.copy(alpha = 0.5f)),
                isFocusedBorderColor = Color(0xFF4CAF50),
                unfocusedBorderColor = Color(0xFF4CAF50).copy(alpha = 0.5f),
                backgroundColor = Color(0xFF4CAF50),
                cornerRadius = 8.dp,
                padding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                focusedBorderWidth = 2.dp,
                unfocusedBorderWidth = 1.dp,
                textColor = Color.White,
                cursorColor = Color.White,
                errorBorderColor = MaterialTheme.colorScheme.error,
                disabledBorderColor = Color.White.copy(alpha = 0.2f),
                disabledBackgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                disabledTextColor = Color.White.copy(alpha = 0.5f),
                errorTextColor = MaterialTheme.colorScheme.error,
                shadowElevation = 0.dp,
                shadowColor = Color.Black.copy(alpha = 0.1f)
            )
        }

        @Composable
        fun error(): ThemedTextFieldStyle {
            return ThemedTextFieldStyle(
                textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onError),
                placeholderStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onError.copy(alpha = 0.5f)),
                isFocusedBorderColor = MaterialTheme.colorScheme.error,
                unfocusedBorderColor = MaterialTheme.colorScheme.error.copy(alpha = 0.5f),
                backgroundColor = MaterialTheme.colorScheme.errorContainer,
                cornerRadius = 8.dp,
                padding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                focusedBorderWidth = 2.dp,
                unfocusedBorderWidth = 1.dp,
                textColor = MaterialTheme.colorScheme.onError,
                cursorColor = MaterialTheme.colorScheme.error,
                errorBorderColor = MaterialTheme.colorScheme.error,
                disabledBorderColor = MaterialTheme.colorScheme.onError.copy(alpha = 0.2f),
                disabledBackgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                disabledTextColor = MaterialTheme.colorScheme.onError.copy(alpha = 0.5f),
                errorTextColor = MaterialTheme.colorScheme.error,
                shadowElevation = 0.dp,
                shadowColor = Color.Black.copy(alpha = 0.1f)
            )
        }
    }
}