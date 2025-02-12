package com.example.comicsappmobile.ui.screen.profiles.me.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
            is UiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }
            }
            is UiState.Success -> {
                val stars: List<Pair<BookUiModel, UserFavoriteUiModel>> = if (userStarsBooks.data != null) userStarsBooks.data!! else emptyList()
                if (stars.isEmpty()){
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ){
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "В избранном пусто",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                textAlign = TextAlign.Center
                            ),
                        )
                    }
                }else{
                    for((book, favorite) in stars){
                        ProfileFavoriteBookCard(book, favorite, navController)
                    }
                }
            }
        }
    }
}