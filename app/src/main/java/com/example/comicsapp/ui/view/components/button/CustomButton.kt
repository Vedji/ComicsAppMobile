package com.example.comicsapp.ui.view.components.button

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.comicsapp.ui.view.theme.ComicsAppTheme

@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit,
    buttonStyle: CustomButtonStyle = CustomButtonStyle.primary(),
    modifier: Modifier = Modifier
    ){
    Box (
        modifier = modifier
            .clip(buttonStyle.shape)
            .background(buttonStyle.background)
            .clickable { onClick() }
        // colors = ButtonDefaults.buttonColors().copy(
        //     contentColor = color,
        //     containerColor = background
        // )
    ) {
        Text(
            text,
            style = buttonStyle.style.copy(color = buttonStyle.color),
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Preview()
@Composable
fun TestSmallButton(appThemeID: Int = 2){
    var themeID by remember { mutableIntStateOf(appThemeID ) }
    val editValue by remember { mutableStateOf( "$appThemeID" ) }

    val color = MaterialTheme.colorScheme.onSecondaryContainer
    val background = MaterialTheme.colorScheme.secondaryContainer


    ComicsAppTheme(themeID) {
        Box (
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

                Text("Theme ID: $themeID")



                TextField(
                    value = editValue,
                    onValueChange = { newValue ->
                        // Фильтрация ввода только для чисел
                        if (newValue.isEmpty()){
                            themeID = 0
                        }else if (newValue.all { it.isDigit() }) {
                            themeID = newValue.toInt()
                        } else{
                            themeID = 0
                        }
                    },
                    label = { Text("Число") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier.fillMaxWidth().background(Color.Blue)
                )


                // Display buttons
                CustomButton(
                    text = "Display Large -> extraLarge",
                    onClick = {},
                    buttonStyle = CustomButtonStyle(
                        color = color,
                        background = background,
                        style = MaterialTheme.typography.displayLarge,
                        shape = MaterialTheme.shapes.extraLarge
                    ),
                )
                CustomButton(
                    text = "Display Medium",
                    onClick = {},
                    buttonStyle = CustomButtonStyle(
                        color = color,
                        background = background,
                        style = MaterialTheme.typography.displayMedium,
                        shape = MaterialTheme.shapes.large
                    ),
                )
                CustomButton(
                    text = "Display Small",
                    onClick = {},
                    buttonStyle = CustomButtonStyle(
                        color = color,
                        background = background,
                        style = MaterialTheme.typography.displaySmall,
                        shape = MaterialTheme.shapes.medium
                    ),
                )

// Headline buttons
                CustomButton(
                    text = "Headline Large",
                    onClick = {},
                    buttonStyle = CustomButtonStyle(
                        color = color,
                        background = background,
                        style = MaterialTheme.typography.headlineLarge,
                        shape = MaterialTheme.shapes.large
                    ),
                )
                CustomButton(
                    text = "Headline Medium",
                    onClick = {},
                    buttonStyle = CustomButtonStyle(
                        color = color,
                        background = background,
                        style = MaterialTheme.typography.headlineMedium,
                        shape = MaterialTheme.shapes.medium
                    ),
                )
                CustomButton(
                    text = "Headline Small",
                    onClick = {},
                    buttonStyle = CustomButtonStyle(
                        color = color,
                        background = background,
                        style = MaterialTheme.typography.headlineSmall,
                        shape = MaterialTheme.shapes.small
                    ),
                )

// Title buttons
                CustomButton(
                    text = "Title Large",
                    onClick = {},
                    buttonStyle = CustomButtonStyle(
                        color = color,
                        background = background,
                        style = MaterialTheme.typography.titleLarge,
                        shape = MaterialTheme.shapes.medium
                    ),
                )
                CustomButton(
                    text = "Title Medium",
                    onClick = {},
                    buttonStyle = CustomButtonStyle(
                        color = color,
                        background = background,
                        style = MaterialTheme.typography.titleMedium,
                        shape = MaterialTheme.shapes.small
                    ),
                )
                CustomButton(
                    text = "Title Small",
                    onClick = {},
                    buttonStyle = CustomButtonStyle(
                        color = color,
                        background = background,
                        style = MaterialTheme.typography.titleSmall,
                        shape = MaterialTheme.shapes.extraSmall
                    ),
                )

// Body buttons
                CustomButton(
                    text = "Body Large",
                    onClick = {},
                    buttonStyle = CustomButtonStyle(
                        color = color,
                        background = background,
                        style = MaterialTheme.typography.bodyLarge,
                        shape = MaterialTheme.shapes.medium
                    ),
                )
                CustomButton(
                    text = "Body Medium",
                    onClick = {},
                    buttonStyle = CustomButtonStyle(
                        color = color,
                        background = background,
                        style = MaterialTheme.typography.bodyMedium,
                        shape = MaterialTheme.shapes.small
                    ),
                )
                CustomButton(
                    text = "Body Small",
                    onClick = {},
                    buttonStyle = CustomButtonStyle(
                        color = color,
                        background = background,
                        style = MaterialTheme.typography.bodySmall,
                        shape = MaterialTheme.shapes.extraSmall
                    ),
                )

// Label buttons
                CustomButton(
                    text = "Label Large",
                    onClick = {},
                    buttonStyle = CustomButtonStyle(
                        color = color,
                        background = background,
                        style = MaterialTheme.typography.labelLarge,
                        shape = MaterialTheme.shapes.large
                    ),
                )
                CustomButton(
                    text = "Label Medium",
                    onClick = {},
                    buttonStyle = CustomButtonStyle(
                        color = color,
                        background = background,
                        style = MaterialTheme.typography.labelMedium,
                        shape = MaterialTheme.shapes.medium
                    ),
                )
                CustomButton(
                    text = "Label Small",
                    onClick = {},
                    buttonStyle = CustomButtonStyle(
                        color = color,
                        background = background,
                        style = MaterialTheme.typography.labelSmall,
                        shape = MaterialTheme.shapes.extraSmall
                    ),
                )


            }

        }
    }
}


@Preview()
@Composable
fun TestSmallButtonFactory(_appThemeID: Int = 2) {

    val appThemeID: MutableState<Int?> =  remember { mutableStateOf( _appThemeID  ) }
    var editValue by remember { mutableStateOf( "$_appThemeID" ) }

    ComicsAppTheme(appThemeID.value ?: 0) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )
        {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Text("Theme ID: ${ appThemeID.value ?: 0}")

                TextField(
                    value = editValue,
                    onValueChange = { newValue ->
                        // Фильтрация ввода только для чисел
                        editValue = newValue
                        appThemeID.value = newValue.toIntOrNull() // Безопасное преобразование строки в число
                    },
                    label = { Text("Число") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Blue)
                )

                PreviewCustomButtons()

            }
        }
    }
}

@Composable
fun CustomButtonExample(type: String) {
    val style = when (type) {
        "primary" -> CustomButtonStyle.primary()
        "secondary" -> CustomButtonStyle.secondary()
        "success" -> CustomButtonStyle.success()
        "error" -> CustomButtonStyle.error()
        "warning" -> CustomButtonStyle.warning()
        "info" -> CustomButtonStyle.info()
        else -> throw IllegalArgumentException("Invalid button type")
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

@Preview(showBackground = true)
@Composable
fun PreviewCustomButtons(appThemeID:Int = 1) {
    ComicsAppTheme(appThemeID = appThemeID) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            CustomButtonExample("primary")
            CustomButtonExample("secondary")
            CustomButtonExample("success")
            CustomButtonExample("error")
            CustomButtonExample("warning")
            CustomButtonExample("info")
        }
    }
}