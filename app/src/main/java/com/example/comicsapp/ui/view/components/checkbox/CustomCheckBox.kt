package com.example.comicsapp.ui.view.components.checkbox

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.comicsapp.domain.model.books.book.Genre
import com.example.comicsapp.ui.view.theme.ComicsAppTheme







@Composable
fun CustomCheckBox(
    text: String,
    onItemSelected: (Boolean) -> Unit = {},
    modifier: Modifier = Modifier,
    checkBoxValue: Boolean = false,
    style: CustomCheckBoxStyle = CustomCheckBoxStyle.default(),
) {
    Box(
        modifier = modifier
            .defaultMinSize(minWidth = 10.dp, minHeight = 20.dp)
            .background(
                color = if (checkBoxValue) style.onBackgroundColor else style.offBackgroundColor,
                shape = style.shape
            )
            .clip(style.shape)
            .border(
                width = style.borderWidth,
                color = if (checkBoxValue) style.onBorderColor else style.offBorderColor,
                shape = style.shape
            )
            .clickable {
                checkBoxValue != checkBoxValue
                onItemSelected(checkBoxValue)
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = style.textStyle.copy(
                color = if (checkBoxValue) style.onTextColor else style.offTextColor
            ),
            modifier = Modifier.padding(style.contentPadding)
        )
    }
}




@Preview(showBackground = true)
@Composable
fun PreviewTestCustomCheckBoxes() {
    val tests = mapOf(
        "default" to Genre(0, "default"),
        "primary" to Genre(1, "primary"),
        "secondary" to Genre(2, "secondary")
    )
    // val genreList: List<Genre> = tests.values.toList()

    ComicsAppTheme(0) {
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ){
            for ((styleType, genre) in tests){
                Log.d("CustomTextExample", "Item for: ${genre.genreName}, style = $styleType")
                CustomTextExample(styleType, genre)
            }
        }
    }

}

@Composable
fun CustomTextExample(type: String, item: Genre) {

    val style = when (type) {
        "default" -> CustomCheckBoxStyle.default()
        "primary" -> CustomCheckBoxStyle.primary()
        "secondary" -> CustomCheckBoxStyle.secondary()
        // "warning" -> ThemedTextStyle.warning()
        // "info" -> ThemedTextStyle.info()
        else -> throw IllegalArgumentException("Invalid text field type. Valid types are: default, primary, secondary, success, error.")
    }

    val checked = remember { mutableStateOf( true ) }
    CustomCheckBox(
        checkBoxValue = checked.value,
        text = "Item: ${item.genreName}, style = $type",
        style = style,
        onItemSelected = {
            checked.value = !checked.value
        }
    )
    Text("Значение кнопки = '${checked.value}'")
}