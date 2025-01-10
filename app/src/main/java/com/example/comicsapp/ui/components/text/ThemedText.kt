package com.example.comicsapp.ui.components.text

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.comicsapp.ui.components.text.ThemedTextStyle.Companion.default

@Composable
fun ThemedText(
    text: String,
    modifier: Modifier = Modifier,
    style: ThemedTextStyle = ThemedTextStyle.default()
) {
    Box(
        modifier = modifier
            .background(style.backgroundColor, shape = style.cornerBasedShape)
            .padding(style.padding)
    ) {
        Text(
            text = text,
            color = style.textColor,
            textAlign = style.textAlign,
            style = style.textStyle.copy(
                color = style.textColor
            )
        )
    }
}

@Preview
@Composable
fun PreviewThemedText() {
    ThemedText(
        text = "Пример текста",
        style = ThemedTextStyle.primary()
    )
}