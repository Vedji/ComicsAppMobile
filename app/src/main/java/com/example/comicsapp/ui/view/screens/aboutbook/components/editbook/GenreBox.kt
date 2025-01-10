package com.example.comicsapp.ui.view.screens.aboutbook.components.editbook

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.example.comicsapp.domain.model.books.book.Genre


@Composable
fun GenreBox(genre: Genre, selectedItems: List<Genre>, onItemSelected: (Genre) -> Unit){
    Log.d("GenreBox", genre.genreName)

    val isChecked = rememberSaveable { mutableStateOf(true) }

    // Box с текстом, изменяющим цвет при нажатии
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (isChecked.value && selectedItems.any() { it.genreID == genre.genreID })
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.secondary
            )
            .clickable {
                // Изменяем состояние при нажатии
                onItemSelected(genre)
                isChecked.value = !isChecked.value
            }
            .padding(8.dp)
            .wrapContentWidth()
    ) {
        Text(
            text = genre.genreName,
            style = MaterialTheme.typography.labelSmall.copy(
                color = if (isChecked.value)
                    MaterialTheme.colorScheme.onPrimary
                else
                    MaterialTheme.colorScheme.onSecondary
            )
        )
    }
}


@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun TestGenreBox() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        GenreDropdownExample()
    }

}

@Composable
fun FilterableDropdownMenu(
    items: List<Genre>,
    onItemSelected: (Genre) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var inputText by remember { mutableStateOf(TextFieldValue("")) }
    val focusRequester = remember { FocusRequester() }
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    if (isFocused && inputText.text.isEmpty())
        expanded = isFocused

    LaunchedEffect(expanded) {
        if (!expanded) {
            focusManager.clearFocus()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                focusManager.clearFocus()
            }
            .padding(8.dp)
    ) {
        Column {
            Row {
                BasicTextField(
                    interactionSource = interactionSource,
                    value = inputText,
                    onValueChange = {
                        inputText = it
                        if (!expanded) expanded = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                        .padding(bottom = 8.dp),
                    decorationBox = { innerTextField ->
                        Surface(
                            shape = MaterialTheme.shapes.small,
                            tonalElevation = 1.dp,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row (
                                modifier = Modifier.padding(8.dp).fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                innerTextField()
                                Icon(
                                    imageVector =  Icons.Outlined.Close,
                                    contentDescription = "Star",
                                    tint = Color(0xFFFFD700), // Цвет звезды
                                    modifier = Modifier
                                        .size(18.dp) // Размер звезды
                                        .clickable {
                                            inputText = TextFieldValue("")
                                            expanded = false
                                            keyboardController?.hide()
                                            focusManager.clearFocus()
                                        }
                                )
                            }
                        }
                    },
                    singleLine = true
                )
            }


            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                properties = PopupProperties(focusable = false),
                modifier = Modifier.heightIn(max = 200.dp)
            ) {
                val filteredItems = items.filter {
                    it.genreName.contains(inputText.text, ignoreCase = true)
                }

                if (filteredItems.isEmpty()) {
                    DropdownMenuItem(
                        text = { Text("Элементы не найдены") },
                        onClick = { },
                        modifier = Modifier
                    )
                } else {
                    filteredItems.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item.genreName) },
                            modifier = Modifier,
                            onClick = {
                                onItemSelected(item)
                                inputText = TextFieldValue("")
                                expanded = false
                                keyboardController?.hide()
                                focusManager.clearFocus()
                            },
                            contentPadding = PaddingValues(
                                start = 16.dp,
                                top = 8.dp,
                                end = 16.dp,
                                bottom = 8.dp
                            )
                        )
                    }
                }
            }

        }
    }
}


@Composable
fun GenreDropdownExample() {
    // Пример данных
    val genres = listOf(
        Genre(1, "Action"),
        Genre(2, "Adventure"),
        Genre(3, "Comedy"),
        Genre(4, "Drama"),
        Genre(5, "Horror")
    )

    var selectedGenre by remember { mutableStateOf<Genre?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = selectedGenre?.genreName ?: "Выберите жанр",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // FilterableDropdownMenu(
        //     items = genres,
        //     onItemSelected = { genre ->
        //         selectedGenre = genre // Сохраняем выбранный жанр
        //     },
        //     modifier = Modifier.fillMaxWidth()
        // )
    }
}