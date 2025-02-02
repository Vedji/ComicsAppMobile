package com.example.comicsappmobile.ui.screen.profiles.me.tabs

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.comicsappmobile.ui.components.ThemedErrorCard
import com.example.comicsappmobile.ui.presentation.viewmodel.ProfileViewModel
import com.example.comicsappmobile.ui.presentation.viewmodel.UiState
import com.example.comicsappmobile.ui.screen.profiles.me.cards.ProfileCommentsCard


@Composable
fun ProfileCommentsTab(
    navController: NavHostController,
    profileViewModel: ProfileViewModel
) {
    val userCommentsList by profileViewModel.userCommentsList.collectAsState()
    when(userCommentsList){
        is UiState.Error -> {
            ThemedErrorCard(
                message = userCommentsList.message ?: "No message in ProfileCommentsTab (is UiState.Error)",
                errorType = userCommentsList.typeError ?: "No error type in ProfileCommentsTab (is UiState.Error)",
                statusCode = userCommentsList.statusCode
            )
        }
        is UiState.Loading -> { CircularProgressIndicator() }
        is UiState.Success -> {
            Column (
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(12.dp)
                    .padding(bottom = 24.dp)
                ,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                if (userCommentsList.data != null){
                    if (userCommentsList.data.isNullOrEmpty()){
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ){
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = "Вы не оставили ни одного комментария",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    textAlign = TextAlign.Center
                                ),
                            )
                        }
                    }else{
                        for(comment in userCommentsList.data!!){
                            ProfileCommentsCard(comment, navController, profileViewModel)
                        }
                    }
                }else{
                    ThemedErrorCard(message = "Комментарии = null в ProfileCommentsTab")
                }

            }
        }
    }
}