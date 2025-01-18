package com.example.comicsappmobile.ui.screen.profiles.cards

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.comicsappmobile.ui.presentation.model.CommentUiModel
import com.example.comicsappmobile.navigation.Screen

@Composable
fun ProfileCommentsCard(commentUiModel: CommentUiModel, navController: NavHostController) {
    Box {

        OutlinedCard(
            onClick = {
                if (commentUiModel.bookId != null && commentUiModel.bookId > 0)
                    navController.navigate(Screen.AboutBook.createRoute(commentUiModel.bookId.toString()))
                      },
            modifier = Modifier
                .fillMaxWidth(),
            colors = CardDefaults.outlinedCardColors().copy(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.tertiary,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContentColor = MaterialTheme.colorScheme.secondary
            )
        ) {
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
                            .fillMaxWidth()
                            .border(1.dp, Color.Red),
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
                            .fillMaxWidth()
                            .border(1.dp, Color.Red),
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

    }
}
