package com.example.comicsappmobile.ui.screen.profiles.cards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.comicsappmobile.ui.presentation.model.BookUiModel
import com.example.comicsappmobile.ui.components.ImageByID


@Composable
fun ProfileAddedBooksCard(book: BookUiModel) {

    Box {
        OutlinedCard(
            onClick = {},   // TODO: Add navigate to book
            modifier = Modifier
                .fillMaxWidth(),
            colors = CardDefaults.outlinedCardColors().copy(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.tertiary,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContentColor = MaterialTheme.colorScheme.secondary
            )
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
                    // TODO: Edit self added book
                },
                modifier = Modifier
                    .size(32.dp)
                    .border(BorderStroke(1.dp, MaterialTheme.colorScheme.outline), MaterialTheme.shapes.small),
                containerColor = MaterialTheme.colorScheme.onSecondary,
                shape = MaterialTheme.shapes.small
            ) {
                Icon(
                    Icons.Filled.Edit,
                    contentDescription = "Edit book",
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}