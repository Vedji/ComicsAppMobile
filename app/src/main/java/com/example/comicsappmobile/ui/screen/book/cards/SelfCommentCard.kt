package com.example.comicsappmobile.ui.screen.book.cards

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.comicsappmobile.ui.components.ThemedErrorCard
import com.example.comicsappmobile.ui.presentation.viewmodel.BookViewModel
import com.example.comicsappmobile.ui.presentation.viewmodel.UiState


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

                    AnimatedVisibility(
                        modifier = Modifier,
                        visible = cardPageIdentify.intValue == 0,
                        enter = expandVertically(
                            animationSpec = tween(250),
                            expandFrom = Alignment.Bottom,
                            clip = false
                        ) + fadeIn(animationSpec = tween(250)), // Плавное появление
                        exit = shrinkVertically(
                            animationSpec = tween(250),
                            shrinkTowards = Alignment.Bottom,
                            clip = false
                        ) + fadeOut(animationSpec = tween(250)) // Исчезновение
                    ) {
                        SelfCommentViewCardPage(
                            commentUiModel.data!!,
                            moveToAddReview = { cardPageIdentify.intValue = it },
                            bookViewModel = bookViewModel
                        )
                    }


                    AnimatedVisibility(
                        modifier = Modifier,
                        visible = cardPageIdentify.intValue == 1,
                        enter = expandVertically(
                            animationSpec = tween(250),
                            expandFrom = Alignment.Bottom,
                            clip = false
                        ) + fadeIn(animationSpec = tween(250)), // Плавное появление
                        exit = shrinkVertically(
                            animationSpec = tween(250),
                            shrinkTowards = Alignment.Bottom,
                            clip = false
                        ) + fadeOut(animationSpec = tween(250)) // Исчезновение
                    ) {
                        SelfCommentAddCardPage(
                            commentUiModel.data!!,
                            onCancel = { cardPageIdentify.intValue = it },
                            bookViewModel = bookViewModel
                        )
                    }
                    AnimatedVisibility(
                        visible = cardPageIdentify.intValue == 2,
                        enter = expandVertically(
                            animationSpec = tween(250),
                            expandFrom = Alignment.Top,
                            clip = false
                        ) + fadeIn(animationSpec = tween(250)),
                        exit = shrinkVertically(
                            animationSpec = tween(250),
                            shrinkTowards = Alignment.Top,
                            clip = false
                        ) + fadeOut(animationSpec = tween(250))
                    ) {
                        SelfCommentEditCardPage(
                            commentUiModel.data!!,
                            onCancel = { cardPageIdentify.intValue = it },
                            bookViewModel = bookViewModel
                        )
                    }
                if (cardPageIdentify.intValue < 0 || cardPageIdentify.intValue > 2) {
                    ThemedErrorCard(message = "Часть не реализована")
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
