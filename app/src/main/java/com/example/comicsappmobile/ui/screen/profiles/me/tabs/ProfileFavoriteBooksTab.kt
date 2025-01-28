package com.example.comicsappmobile.ui.screen.profiles.me.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.comicsappmobile.ui.components.ThemedErrorCard
import com.example.comicsappmobile.ui.presentation.model.BookUiModel
import com.example.comicsappmobile.ui.presentation.model.UserFavoriteUiModel
import com.example.comicsappmobile.ui.presentation.viewmodel.ProfileViewModel
import com.example.comicsappmobile.ui.presentation.viewmodel.UiState
import com.example.comicsappmobile.ui.screen.profiles.me.cards.ProfileFavoriteBookCard

@Composable
fun ProfileFavoriteBooksTab(
    navController: NavHostController,
    profileViewModel: ProfileViewModel
) {

    val userStarsBooks by profileViewModel.userStarsBooks.collectAsState()

    Column (
        modifier = Modifier
            .wrapContentHeight()
            .padding(12.dp)
            .padding(bottom = 24.dp)
        ,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        when(userStarsBooks){
            is UiState.Error -> {
                ThemedErrorCard(
                    message = userStarsBooks.message ?: "No message in ProfileFavoriteBooksTab (is UiState.Error)",
                    errorType = userStarsBooks.typeError ?: "No error type in ProfileFavoriteBooksTab (is UiState.Error)",
                    statusCode = userStarsBooks.statusCode
                )
            }
            is UiState.Loading -> { CircularProgressIndicator() }
            is UiState.Success -> {
                val stars: List<Pair<BookUiModel, UserFavoriteUiModel>> = if (userStarsBooks.data != null) userStarsBooks.data!! else emptyList()
                    for((book, favorite) in stars){
                        ProfileFavoriteBookCard(book, favorite, navController)
                    }
            }
        }
    }
}