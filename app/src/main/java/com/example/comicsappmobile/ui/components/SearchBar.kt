package com.example.comicsappmobile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp


@Composable
fun ThemedSearchBar(
    text: String,
    onTextChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge.copy(
        color = MaterialTheme.colorScheme.primary,
    ),
    placeholderStyle: TextStyle = MaterialTheme.typography.bodyLarge.copy(
        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.5f)
    ),
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    cursorColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    onClickSearch: (String) -> Unit = {}
) {
    BasicTextField(
        value = text,
        onValueChange = onTextChange,
        textStyle = textStyle,
        cursorBrush = SolidColor(cursorColor),
        singleLine = true,
        modifier = modifier
            .size(128.dp, 36.dp)
            .clip(MaterialTheme.shapes.large)
            .border(1.dp, MaterialTheme.colorScheme.outline, MaterialTheme.shapes.large)
            .background(containerColor),
        decorationBox = { innerTextField ->
            Box(
                contentAlignment = Alignment.CenterStart,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .fillMaxWidth()
            ) {
                Row (
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ){

                    Box(
                        contentAlignment = Alignment.CenterStart,
                        modifier = Modifier.widthIn(max = 256.dp).weight(0.8f, false)
                    ) {
                        if (text.isEmpty()) {
                            Text(
                                text = placeholder,
                                style = placeholderStyle,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        innerTextField()
                    }
                    IconButton(
                        onClick = { onClickSearch(text) },
                        modifier = Modifier.weight(0.2f, false)
                    ) {
                        Icon(
                            Icons.Outlined.Search,"Search button",
                            modifier = Modifier
                                .size(28.dp)
                        )
                    }

                }
            }
        }
    )
}
