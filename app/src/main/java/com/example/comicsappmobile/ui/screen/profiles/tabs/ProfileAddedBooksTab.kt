package com.example.comicsappmobile.ui.screen.profiles.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.comicsappmobile.ui.presentation.model.BookUiModel
import com.example.comicsappmobile.ui.screen.profiles.cards.ProfileAddedBooksCard

@Composable
fun ProfileAddedBooksTab() {

    val testBooks = listOf(
        BookUiModel(bookTitleImageId = 21),
        BookUiModel(bookTitleImageId = 21),
        BookUiModel(bookTitleImageId = 21),
        BookUiModel(bookTitleImageId = 21),
        BookUiModel(bookTitleImageId = 21),
    )

    Column (
        modifier = Modifier
            .wrapContentHeight()
            .padding(12.dp)
            .padding(bottom = 24.dp)
        ,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        for(book in testBooks){
            ProfileAddedBooksCard(book)
        }
    }
}