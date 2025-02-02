package com.example.comicsappmobile.ui.screen.profiles.me.cards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.comicsappmobile.navigation.Screen
import com.example.comicsappmobile.ui.presentation.model.CommentUiModel
import com.example.comicsappmobile.ui.presentation.viewmodel.ProfileViewModel
import com.example.comicsappmobile.ui.presentation.viewmodel.UiState

@Composable
fun ProfileCommentsCard(commentUiModel: CommentUiModel, navController: NavHostController, profileViewModel: ProfileViewModel) {
    val bookTitleName = rememberSaveable() { mutableStateOf("") }

    LaunchedEffect(commentUiModel){
        val bookId: Int = commentUiModel.bookId
        val bookToComment =
            if (bookId > 0)
                profileViewModel.fetchBookInfoById(bookId)
            else
                UiState.Error(message = "Книга не найдена")
        bookTitleName.value =
            if (bookToComment is UiState.Success) bookToComment.data?.rusTitle ?: "Пустое имя книги"
            else "Книга не найдена"
    }
    Box {
        OutlinedCard(
            onClick = {
                if (commentUiModel.bookId != null && commentUiModel.bookId > 0)
                    navController.navigate(Screen.AboutBook.createRoute(commentUiModel.bookId.toString(), selectionTab = 2))
                      },
            modifier = Modifier
                .fillMaxWidth(),
            colors = CardDefaults.outlinedCardColors().copy(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.secondary,
                disabledContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                disabledContentColor = MaterialTheme.colorScheme.tertiary
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
        ) {
            Text(
                text = bookTitleName.value,
                modifier = Modifier.padding(12.dp),
                style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Start
                )
            )
            HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outline)
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                if (commentUiModel.comment.isNotEmpty()) {
                    Text(
                        text = commentUiModel.comment,
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Justify
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            modifier = Modifier
                                .wrapContentSize()
                                .weight(1f, false),
                            textAlign = TextAlign.Justify,
                            text = "${commentUiModel.rating}",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Star",
                            tint = Color(0xFFFFD700), // Цвет звезды
                            modifier = Modifier
                                .size(18.dp) // Размер звезды
                                .weight(1f, false)
                        )
                    }

                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            modifier = Modifier
                                .wrapContentSize(),
                            textAlign = TextAlign.Justify,
                            text = commentUiModel.uploadDate,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }

                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
        /*
        Row(
            Modifier
                .fillMaxWidth()
                .offset(y = (14).dp)
                .padding(end = 8.dp)
                .align(Alignment.BottomEnd),
            horizontalArrangement = Arrangement.End
        ) {
            SmallFloatingActionButton(
                onClick = {
                    // TODO: navigate to edit comment
                },
                modifier = Modifier
                    .size(32.dp),
                containerColor = MaterialTheme.colorScheme.errorContainer
            ) {
                Icon(
                    Icons.Filled.Edit,
                    contentDescription = "Edit",
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            SmallFloatingActionButton(
                onClick = {
                    // TODO: Delete comment and refresh page
                },
                modifier = Modifier
                    .size(32.dp),
                containerColor = MaterialTheme.colorScheme.errorContainer
            ) {
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = "Delete",
                    modifier = Modifier.size(16.dp)
                )
            }
        }
        */

    }
}
