package com.example.comicsapp.ui.components.text

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.comicsapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldAppBar(
    query: TextFieldValue,
    onQueryChange: (TextFieldValue) -> Unit,
    onClose: () -> Unit
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = {
            Text(
                text = "Поиск...",
                textAlign = TextAlign.Start,
                style = TextStyle(
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSecondary
                )
            )
        },
        singleLine = true,
        modifier = Modifier
            .width(256.dp)
            .clip(RoundedCornerShape(48.dp)) // Обрезаем углы для закругления
            .background(MaterialTheme.colorScheme.secondary) // Устанавливаем фон
            .height(48.dp) // Рекомендуемая высота для TextField
            .padding(horizontal = 0.dp), // Внутренние отступы
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent, // Убираем линию фокуса
            unfocusedIndicatorColor = Color.Transparent, // Убираем линию без фокуса
            disabledIndicatorColor = MaterialTheme.colorScheme.secondary
        ),
        textStyle = TextStyle(
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal, // Ставим Normal, чтобы уменьшить визуальный вес текста
            color = MaterialTheme.colorScheme.onPrimary,
            lineHeight = 16.sp // Увеличиваем lineHeight для лучшей читаемости
        ),
        trailingIcon = {
            IconButton(
                onClick = onClose,
                modifier = Modifier.size(24.dp) // Увеличиваем иконку для соразмерности с высотой
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_search),
                    contentDescription = "Очистить",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    )
}