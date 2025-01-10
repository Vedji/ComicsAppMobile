package com.example.comicsapp.ui.view.screens.aboutbook.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.comicsapp.ui.view.components.text.ThemedText
import com.example.comicsapp.ui.view.components.text.ThemedTextStyle
import com.example.comicsapp.ui.viewmodel.AppViewModel

@Composable
fun AboutBookTable(navController: NavHostController, viewModel: AppViewModel){
    val book by viewModel.selectedBook.book.collectAsState()
    val genreNamesList by viewModel.selectedBook.bookGenresList.collectAsState()
    // book?.bookRating?.toInt() ?: 0
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ){
        val aboutBookTableItems = mapOf(
            "Жанры" to ( if (genreNamesList.isNotEmpty()) { genreNamesList.joinToString(", ") } else { "Жанров нет" } ),
            "О произведении" to (book?.bookDescription ?: "No upload"),
            "Дата публикации" to (book?.bookDatePublication ?: "No upload"),
            "ISBN" to (book?.bookISBN ?: "No upload")
        )
        for ((key, value) in aboutBookTableItems) {

            ThemedText(
                text = key,
                modifier = Modifier
                    .wrapContentSize(),
                style = ThemedTextStyle.primary().copy(
                    padding = PaddingValues(start = 24.dp, bottom = 8.dp, end = 24.dp)
                )
            )

            ThemedText(
                text = value,
                modifier = Modifier
                    .wrapContentSize(),
                style = ThemedTextStyle.secondary().copy(
                    padding = PaddingValues(start = 24.dp, bottom = 24.dp, end = 24.dp)
                )
            )
        }
    }
}