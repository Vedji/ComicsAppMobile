package com.example.comicsappmobile.ui.screen.profiles.me.cards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.comicsappmobile.navigation.Screen
import com.example.comicsappmobile.ui.components.ImageByID
import com.example.comicsappmobile.ui.presentation.model.BookUiModel
import com.example.comicsappmobile.ui.presentation.model.UserFavoriteUiModel

@Composable
fun ProfileFavoriteBookCard(
    book: BookUiModel,
    userFavoriteUiModel: UserFavoriteUiModel,
    navController: NavController
) {
    Box {
    OutlinedCard(
        onClick = {
            if (userFavoriteUiModel.chapterId > 0)
                navController.navigate(Screen.BookReader.createRoute(
                    bookId = book.bookId, chapterId = userFavoriteUiModel.chapterId))
            else
                navController.navigate(Screen.AboutBook.createRoute(book.bookId.toString()))
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
            Row {
                Row(
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth().weight(0.6f, false)
                ) {
                    ImageByID(
                        book.bookTitleImageId,
                        modifier = Modifier
                            .width(100.dp)
                            .aspectRatio(0.7f)
                            .clip(MaterialTheme.shapes.medium)
                            .clipToBounds(),
                        contentScale = ContentScale.Crop
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 12.dp, end = 12.dp)
                        .fillMaxWidth().weight(1f, false)
                ) {
                    Column {
                        Text(text = book.rusTitle)
                        Row(
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Text(
                                modifier = Modifier
                                    .wrapContentSize()
                                    .weight(1f, false),
                                textAlign = TextAlign.Justify,
                                text = "${book.bookRating}",
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
                    }
                }
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
                    // TODO: Delete book from favorite list
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