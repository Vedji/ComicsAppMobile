package com.example.comicsappmobile.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun EditableRatingBar(
    modifier: Modifier = Modifier,
    maxRating: Int = 5, // Количество звезд
    currentRating: Int = 0, // Текущий рейтинг
    onRatingChanged: (Int) -> Unit, // Обработчик изменения рейтинга
    starSize: Dp = 24.dp
) {
    var rating by remember { mutableIntStateOf(currentRating) }
    Row(modifier = modifier) {
        for (i in 1..maxRating) {
            Icon(
                imageVector = if (i <= rating) Icons.Filled.Star else Icons.Outlined.Star,
                contentDescription = "Star $i",
                tint = if (i <= rating) Color(0xFFFFD700) else Color.Gray, // Цвет звезды
                modifier = Modifier
                    .clickable {
                        rating = i
                        onRatingChanged(rating) // Вызываем обработчик
                    }
                    .size(starSize) // Размер звезды
            )
        }
    }
}