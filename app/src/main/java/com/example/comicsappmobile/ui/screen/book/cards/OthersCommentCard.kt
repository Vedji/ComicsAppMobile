package com.example.comicsappmobile.ui.screen.book.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.comicsappmobile.ui.components.ImageByID
import com.example.comicsappmobile.ui.presentation.model.CommentUiModel
import com.example.comicsappmobile.ui.presentation.viewmodel.BookViewModel


@Composable
fun OthersCommentCard(commentUiModel: CommentUiModel, bookViewModel: BookViewModel){
    val currentUser = bookViewModel.sharedViewModel.currentAuthorizingUser.value
    Box{

        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth(),
            colors = CardDefaults.outlinedCardColors().copy(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ImageByID(
                        commentUiModel.aboutUser.userTitleImage,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(start = 8.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = commentUiModel.aboutUser.username,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            ),
                            textAlign = TextAlign.Justify,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = Modifier
                                    .wrapContentSize(),
                                textAlign = TextAlign.Justify,
                                text = "${commentUiModel.rating}",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            )
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = "Star",
                                tint = Color(0xFFFFD700), // Цвет звезды
                                modifier = Modifier
                                    .size(18.dp) // Размер звезды
                            )
                        }
                    }
                }
                if (commentUiModel.comment.isNotEmpty()) {
                    Text(
                        text = commentUiModel.comment,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Justify
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
        if (false) {
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
                        // TODO: Delete comment for admin and helper
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
}
