package com.example.comicsapp.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.comicsapp.ui.components.button.CustomButtonStyle
import com.example.comicsapp.ui.components.checkbox.PreviewTestCustomCheckBoxes
import com.example.comicsapp.ui.components.expandedlist.ExpandedList
import com.example.comicsapp.ui.components.expandedlist.ExpandedListStyle
import com.example.comicsapp.ui.components.text.ThemedText
import com.example.comicsapp.ui.components.text.ThemedTextStyle
import com.example.comicsapp.ui.components.textfield.ThemedTextField
import com.example.comicsapp.ui.components.textfield.ThemedTextFieldStyle


@Composable
fun CustomButtonExample(type: String) {
    val style = when (type) {
        "primary" -> CustomButtonStyle.primary()
        "secondary" -> CustomButtonStyle.secondary()
        "success" -> CustomButtonStyle.success()
        "error" -> CustomButtonStyle.error()
        "warning" -> CustomButtonStyle.warning()
        "info" -> CustomButtonStyle.info()
        else -> throw IllegalArgumentException("Invalid button type. Valid types are: primary, secondary, success, error, warning, info.")
    }
    Button(
        onClick = { /*TODO: Handle click*/ },
        modifier = Modifier.padding(8.dp),
        shape = style.shape,
        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
            containerColor = style.background,
            contentColor = style.color
        )
    ) {
        Text(text = "${type.capitalize()} Button", style = style.style)
    }
}

@Composable
fun CustomTextFieldExample(type: String, value: String, onValueChange: (TextFieldValue) -> Unit) {
    val style = when (type) {
        "default" -> ThemedTextFieldStyle.default()
        "primary" -> ThemedTextFieldStyle.primary()
        "secondary" -> ThemedTextFieldStyle.secondary()
        "success" -> ThemedTextFieldStyle.success()
        "error" -> ThemedTextFieldStyle.error()
        else -> throw IllegalArgumentException("Invalid text field type. Valid types are: default, primary, secondary, success, error.")
    }

    ThemedTextField(
        value = value,
        onValueChange = onValueChange,
        style = style,
        placeholder = "${type.capitalize()} TextField"
    )
}

@Composable
fun CustomTextExample(type: String) {
    val style = when (type) {
        "default" -> ThemedTextStyle.default()
        "primary" -> ThemedTextStyle.primary()
        "secondary" -> ThemedTextStyle.secondary()
        "warning" -> ThemedTextStyle.warning()
        "info" -> ThemedTextStyle.info()
        else -> throw IllegalArgumentException("Invalid text field type. Valid types are: default, primary, secondary, success, error.")
    }

    ThemedText(
        text = type,
        style = style
    )
}


@Preview(showBackground = true)
@Composable
fun PreviewTestCustomComponents() {
    val scrollButtons = rememberScrollState()
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()

            .padding(16.dp)
            .border(1.dp, Color.Red)
    ) {
        item {
            ExpandedList(
                text = "Пример CustomButton",
                style = ExpandedListStyle.default(),
            ){
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .heightIn(200.dp, 500.dp)
                        .wrapContentHeight()
                        .padding(16.dp)
                ) {
                    item {
                        // Кнопки
                        Text("Buttons:", style = MaterialTheme.typography.bodyLarge)
                        CustomButtonExample("primary")
                    }
                    item {
                        // Кнопки
                        Text("Buttons:", style = MaterialTheme.typography.bodyLarge)
                        CustomButtonExample("secondary")
                    }
                    item {
                        // Кнопки
                        Text("Buttons:", style = MaterialTheme.typography.bodyLarge)
                        CustomButtonExample("success")
                    }
                    item {
                        // Кнопки
                        Text("Buttons:", style = MaterialTheme.typography.bodyLarge)
                        CustomButtonExample("error")
                    }
                    item {
                        // Кнопки
                        Text("Buttons:", style = MaterialTheme.typography.bodyLarge)
                        CustomButtonExample("warning")
                    }
                    item {
                        // Кнопки
                        Text("Buttons:", style = MaterialTheme.typography.bodyLarge)
                        CustomButtonExample("info")
                    }
                }
            }
        }

        item {
            ExpandedList(
                text = "Пример CustomTextField",
                style = ExpandedListStyle.primary(),
            ){
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .heightIn(200.dp, 500.dp)
                        .wrapContentHeight()
                        .padding(16.dp)
                ) {
                    item {
                        val textFieldPrimary = remember { mutableStateOf(TextFieldValue("Primary")) }

                        Text(text = textFieldPrimary.value.text)

                        CustomTextFieldExample(
                            "primary",
                            textFieldPrimary.value.text
                        ) { textFieldPrimary.value = it }
                    }
                    item {
                        val textFieldSecondary = remember { mutableStateOf(TextFieldValue("Secondary")) }
                        Text(text = textFieldSecondary.value.text)
                        CustomTextFieldExample(
                            "secondary",
                            textFieldSecondary.value.text
                        ) { textFieldSecondary.value = it }
                    }
                    item {
                        val textFieldSuccess = remember { mutableStateOf(TextFieldValue("Success")) }
                        Text(text = textFieldSuccess.value.text)
                        CustomTextFieldExample(
                            "success",
                            textFieldSuccess.value.text
                        ) { textFieldSuccess.value = it }
                    }
                    item {
                        val textFieldError = remember { mutableStateOf(TextFieldValue("Error")) }
                        Text(text = textFieldError.value.text)
                        CustomTextFieldExample("error", textFieldError.value.text) { textFieldError.value = it }
                    }
                }
            }
        }

        item {
            ExpandedList(
                text = "Текст: CustomText",
                style = ExpandedListStyle.secondary(),
            ){
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .heightIn(200.dp, 500.dp)
                        .wrapContentHeight()
                        .padding(16.dp)
                ) {
                    item {
                        // Кнопки
                        CustomTextExample("default")
                    }
                    item {
                        // Кнопки
                        CustomTextExample("primary")
                    }
                    item {
                        // Кнопки
                        CustomTextExample("secondary")
                    }
                    item {
                        // Кнопки
                        CustomTextExample("warning")
                    }
                    item {
                        // Кнопки
                        CustomTextExample("info")
                    }
                }
            }
        }

        item {
            ExpandedList(
                text = "Чекбоксы, CustomCheckBox",
                style = ExpandedListStyle.info(),
            ){
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .heightIn(200.dp, 500.dp)
                        .wrapContentHeight()
                        .padding(16.dp)
                ) {
                    item {
                        // Кнопки
                        PreviewTestCustomCheckBoxes()
                    }
                }
            }
        }


    }
}

