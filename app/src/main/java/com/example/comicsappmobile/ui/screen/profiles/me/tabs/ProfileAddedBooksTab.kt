package com.example.comicsappmobile.ui.screen.profiles.me.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.comicsappmobile.ui.components.ThemedErrorCard
import com.example.comicsappmobile.ui.presentation.viewmodel.ProfileViewModel
import com.example.comicsappmobile.ui.presentation.viewmodel.UiState
import com.example.comicsappmobile.ui.screen.profiles.me.cards.ProfileAddedBooksCard

@Composable
fun ProfileAddedBooksTab(navController: NavController, profileViewModel: ProfileViewModel) {

    val addedBook = profileViewModel.userAddedBooks.collectAsState()

    Column (
        modifier = Modifier
            .wrapContentHeight()
            .padding(12.dp)
            .padding(bottom = 24.dp)
        ,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        when(addedBook.value){
            is UiState.Loading -> {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) { CircularProgressIndicator() }
            }
            is UiState.Success -> {
                for(book in (addedBook.value.data ?: emptyList())){
                    ProfileAddedBooksCard(book, navController = navController)
                }
            }
            is UiState.Error -> {
                ThemedErrorCard(uiState = addedBook.value as? UiState.Error ?: UiState.Error(
                    data = null,
                    message = "Unknow error",
                    statusCode = -1,
                    typeError = "Unknow error"
                ))
            }
        }

    }
}