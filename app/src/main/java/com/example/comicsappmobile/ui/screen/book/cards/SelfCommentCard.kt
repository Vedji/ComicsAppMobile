package com.example.comicsappmobile.ui.screen.book.cards

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.comicsappmobile.ui.components.ThemedErrorCard
import com.example.comicsappmobile.ui.presentation.model.CommentUiModel
import com.example.comicsappmobile.ui.presentation.viewmodel.BookViewModel
import com.example.comicsappmobile.ui.presentation.viewmodel.UiState
import com.example.comicsappmobile.ui.theme.ComicsAppMobileTheme


@Composable
fun SelfCommentCard(bookViewModel: BookViewModel) { // TODO: ADD view model in args

    val commentUiModel by bookViewModel.userComment.collectAsState()

    val cardPageIdentify = rememberSaveable { mutableIntStateOf(0) }
    // cardPageIdentify = 0: View self comment
    // cardPageIdentify = 1: View add comment
    // cardPageIdentify = 2: View edit self comment


    when (commentUiModel) {
        is UiState.Success -> {
            if (commentUiModel.data == null)
                return
            when (cardPageIdentify.intValue) {     // TODO: Add view model arg
                0 -> {
                    SelfCommentViewCardPage(
                        commentUiModel.data!!,
                        moveToAddReview = { cardPageIdentify.intValue = it },
                        bookViewModel = bookViewModel)
                }

                1 -> {
                    SelfCommentAddCardPage(
                        commentUiModel.data!!,
                        onCancel = { cardPageIdentify.intValue = it },
                        bookViewModel = bookViewModel)
                }

                2 -> {
                    SelfCommentEditCardPage(
                        commentUiModel.data!!,
                        onCancel = { cardPageIdentify.intValue = it },
                        bookViewModel = bookViewModel
                        )
                }

                else -> {
                    ThemedErrorCard(message = "Часть не реализована")
                }
            }
        }
        is UiState.Error -> {
            if (commentUiModel.typeError == "AuthError"){
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Авторизуйтесь, чтобы оставить комментарий",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        textAlign = TextAlign.Center
                        )
                }
            }else{
                ThemedErrorCard(
                    errorType = commentUiModel.typeError ?: "No errorType in SelfCommentCard",
                    message = commentUiModel.message ?: "No message type in SelfCommentCard",
                    statusCode = commentUiModel.statusCode
                )
            }
        }
        is UiState.Loading -> {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center) {
                CircularProgressIndicator()
            }
        }
    }
}
