package com.example.comicsappmobile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

class ThemedInputField {
}

// TODO: Move to components
@Composable
fun ThemedInputField(
    textFieldValue: MutableState<String> = rememberSaveable { mutableStateOf("") },
    onValueChange: (String) -> Unit = {},
    placeholder: String = "Simple ThemedInputField",
    modifier: Modifier = Modifier.height(36.dp),
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge.copy(
        color = MaterialTheme.colorScheme.onSecondaryContainer
    ),
    placeholderStyle: TextStyle = MaterialTheme.typography.bodyLarge.copy(
        color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.5f)
    ),
    containerColor: Color = MaterialTheme.colorScheme.tertiaryContainer,
    cursorColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
    rightIcon: @Composable (() -> Unit)? = null,
    oneLine: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    enabled: Boolean = true
) {

    BasicTextField(
        value = textFieldValue.value,
        onValueChange = {
            textFieldValue.value = it
            onValueChange(it) },
        textStyle = textStyle,
        cursorBrush = SolidColor(cursorColor),
        singleLine = oneLine,
        enabled = enabled,
        visualTransformation = visualTransformation,
        modifier = modifier
            .border(1.dp, color = MaterialTheme.colorScheme.outline, shape = MaterialTheme.shapes.large)
            .clip(MaterialTheme.shapes.large)
            .background(containerColor),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .padding(horizontal = 16.dp),
                verticalAlignment = if (oneLine) Alignment.CenterVertically else Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = if (rightIcon != null) 8.dp else 0.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (textFieldValue.value.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = placeholderStyle,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    innerTextField()
                }

                rightIcon?.let {
                    it()
                }
            }
        }
    )
}
