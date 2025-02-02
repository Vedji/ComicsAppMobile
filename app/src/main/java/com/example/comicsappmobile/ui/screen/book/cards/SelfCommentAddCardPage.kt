package com.example.comicsappmobile.ui.screen.book.cards

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.comicsappmobile.ui.components.EditableRatingBar
import com.example.comicsappmobile.ui.components.ThemedInputField
import com.example.comicsappmobile.ui.presentation.model.CommentUiModel
import com.example.comicsappmobile.ui.presentation.viewmodel.BookViewModel

@Composable
fun SelfCommentAddCardPage(commentUiModel: CommentUiModel, onCancel: (Int) -> Unit, bookViewModel: BookViewModel) { // TODO: ADD view model in args

    val textFieldValue = remember { mutableStateOf(commentUiModel.comment) }
    val rating = remember { mutableIntStateOf(commentUiModel.rating.toInt()) }
    val infoField = remember { mutableStateOf("") }

    Box {
        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth(),
            colors = CardDefaults.outlinedCardColors().copy(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContentColor = MaterialTheme.colorScheme.onSecondaryContainer
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
        ) {
            Row(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Добавте свой отзыв",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            }
            HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outline)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .padding(top = 8.dp, bottom = 24.dp)
            ) {
                if (infoField.value.isNotEmpty()) {
                    Text(text = infoField.value)
                }
                ThemedInputField(
                    textFieldValue = textFieldValue,
                    modifier = Modifier.height(120.dp),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onPrimary
                    ),
                    placeholderStyle = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f)
                    ),
                    cursorColor = MaterialTheme.colorScheme.onPrimary,
                    containerColor = MaterialTheme.colorScheme.primary,
                    oneLine = false
                )
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    EditableRatingBar(
                        currentRating = rating.intValue,
                        onRatingChanged = { rating.intValue = it })
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomEnd)
                .offset(y = (14).dp)
                .padding(end = 8.dp),
            horizontalArrangement = Arrangement.End
        ) {
            SmallFloatingActionButton(
                onClick = { onCancel(0) }, // TODO: Add navigate to card view self comment
                modifier = Modifier
                    .size(32.dp)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = MaterialTheme.shapes.small
                    ),
                containerColor = MaterialTheme.colorScheme.errorContainer
            ) {
                Icon(
                    Icons.Filled.Close,
                    contentDescription = "Cancel",
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            SmallFloatingActionButton(
                onClick = {
                    if (rating.value > 5 || rating.intValue <= 0) {
                        infoField.value = "Оценка должна быть от 0 до 5"
                        return@SmallFloatingActionButton
                    }
                    if (textFieldValue.value.length < 10) {
                        infoField.value = "Напишите пару слов о книге"
                        return@SmallFloatingActionButton
                    }
                    bookViewModel.launchSetUserComment(rating.intValue, textFieldValue.value)
                    textFieldValue.value = ""
                    onCancel(0)
                }, // TODO: Approve comment
                modifier = Modifier
                    .size(32.dp)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = MaterialTheme.shapes.small
                    ),
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ) {
                Icon(
                    Icons.Filled.Done,
                    contentDescription = "Approve",
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
