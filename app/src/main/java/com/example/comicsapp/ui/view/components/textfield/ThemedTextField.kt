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
import androidx.compose.material3.Button
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
import androidx.compose.ui.draw.shadow
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
import com.example.comicsapp.ui.view.components.button.CustomButtonStyle
import com.example.comicsapp.ui.view.theme.ComicsAppTheme
import androidx.compose.foundation.layout.PaddingValues as PaddingValues1


@Composable
fun ThemedTextField(
    value: String,
    onValueChange: (TextFieldValue) -> Unit = {},
    placeholder: String = "Введите текст",
    modifier: Modifier = Modifier,
    style: ThemedTextFieldStyle = ThemedTextFieldStyle.default(),
    isError: Boolean = false,
    isEnabled: Boolean = true
) {
    val focusRequester = remember { FocusRequester() }
    val isFocused = remember { mutableStateOf(false) }
    var inputText by remember { mutableStateOf(TextFieldValue(value)) }

    val borderColor = when {
        !isEnabled -> style.disabledBorderColor
        isError -> style.errorBorderColor
        isFocused.value -> style.isFocusedBorderColor
        else -> style.unfocusedBorderColor
    }

    val backgroundColor = if (isEnabled) style.backgroundColor else style.disabledBackgroundColor
    val textColor = if (isEnabled) style.textColor else style.disabledTextColor

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor, shape = RoundedCornerShape(style.cornerRadius))
            .border(
                width = if (isFocused.value) style.focusedBorderWidth else style.unfocusedBorderWidth,
                color = borderColor,
                shape = RoundedCornerShape(style.cornerRadius)
            )
            .shadow(elevation = style.shadowElevation, shape = RoundedCornerShape(style.cornerRadius), clip = false)
            .padding(style.padding)
            .clickable(enabled = isEnabled) { focusRequester.requestFocus() }
    ) {
        BasicTextField(
            value = inputText,
            onValueChange = { newValue ->
                inputText = newValue
                onValueChange(inputText)
            },
            modifier = Modifier
                .focusRequester(focusRequester)
                .onFocusChanged { isFocused.value = it.isFocused },
            textStyle = style.textStyle.copy(color = textColor),
            cursorBrush = SolidColor(style.cursorColor),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (inputText.text.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = style.placeholderStyle,
                            color = style.placeholderStyle.color
                        )
                    }
                    innerTextField() // Само поле ввода
                }
            }
        )
    }

    if (isError) {
        // Пример отображения ошибки, если нужно
        Text(
            text = "Ошибка ввода",
            color = style.errorTextColor,
            style = style.textStyle,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}


@Preview()
@Composable
fun TestCustomTextField(appThemeID: Int = 2) {
    val themeApp: MutableState<Int?> =  remember { mutableStateOf( appThemeID  ) }


    val color = MaterialTheme.colorScheme.onSecondaryContainer
    val background = MaterialTheme.colorScheme.secondaryContainer


    ComicsAppTheme(themeApp.value ?: 0) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize(),
        )
        {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {


                var editValue by remember { mutableStateOf( "${themeApp.value ?: 0}") }
                Text("${themeApp.value ?: 0} and Text field value ${editValue}")

            }
        }
    }
}





