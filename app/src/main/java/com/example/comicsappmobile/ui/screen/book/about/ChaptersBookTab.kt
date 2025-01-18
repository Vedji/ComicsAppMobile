package com.example.comicsappmobile.ui.screen.book.about

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.comicsappmobile.navigation.Screen
import com.example.comicsappmobile.ui.components.ThemedStateView
import com.example.comicsappmobile.utils.Logger
import com.example.comicsappmobile.ui.presentation.viewmodel.BookViewModel
import com.example.comicsappmobile.ui.presentation.viewmodel.UiState
import com.example.comicsappmobile.ui.presentation.model.ChapterUiModel


@Composable
fun ChaptersBookTab(bookViewModel: BookViewModel, navController: NavController) {
    val chaptersUiState by bookViewModel.chaptersUiState.collectAsState()
    var sortByAsc by remember { mutableStateOf(true) }
    val book by bookViewModel.bookUiState.collectAsState()

    Box(
        modifier = Modifier
            .padding(start = 24.dp, end = 24.dp, )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            LazyColumn {
                item {
                    Row(
                        modifier = Modifier
                            .padding(bottom = 8.dp, end = 8.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AssistChip(
                            onClick = { sortByAsc = !sortByAsc },
                            label = { Text(
                                text = "Assist Chip",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                                ) },
                            leadingIcon = {
                                Icon(
                                    if (!sortByAsc) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                                    contentDescription = "Localized description",
                                    Modifier.size(AssistChipDefaults.IconSize),
                                    tint = MaterialTheme.colorScheme.onSecondary
                                )
                            },
                            modifier = Modifier.height(40.dp),
                            colors = AssistChipDefaults.assistChipColors().copy(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        )
                        if (( book is UiState.Success &&
                                    bookViewModel.sharedViewModel.isUserHasPermission(
                                    addedUserId = book.data?.bookAddedBy ?: -1,
                                    hasEditor = true,
                                    hasPublisher = true,
                                    hasAdmin = true
                                    ))) {
                            SmallFloatingActionButton(
                                onClick = {},   // TODO: Add onClick to add newChapter
                                modifier = Modifier
                                    .size(40.dp)
                                    .offset(x = (4).dp),
                                containerColor = MaterialTheme.colorScheme.secondary

                                ) {
                                Icon(
                                    Icons.Filled.Add,
                                    contentDescription = "Add chapter",
                                    modifier = Modifier
                                        .size(20.dp),
                                )
                            }
                        }
                    }
                }
                item{
                    ThemedStateView(chaptersUiState, {
                        if (chaptersUiState is UiState.Success){
                            var genres = chaptersUiState.data!! ?: emptyList()

                            if (sortByAsc)
                                genres = genres.sortedBy { it.chapterNumber }
                            else
                                genres = genres.sortedByDescending { it.chapterNumber }

                            for (item in genres){
                                ChapterCard(item, navController = navController, bookViewModel)
                            }
                            if ( chaptersUiState.data!!.isEmpty() ?: false){
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = "Эта работа не содержит глав",
                                    color = MaterialTheme.colorScheme.primary,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                    },
                        {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = "Загрузка глав ...",
                                    color = MaterialTheme.colorScheme.primary,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.padding(24.dp))
                                CircularProgressIndicator()
                            }

                        }
                    )
                }
                item{
                    Spacer(modifier = Modifier.height(64.dp))
                }
            }

        }
    }

}

@Composable
fun ChapterCard(chapter: ChapterUiModel, navController: NavController, bookViewModel: BookViewModel){
    val hisCardEnabled = chapter.chapterLength > 0
    Box(
        modifier = Modifier.fillMaxWidth().padding(top = 12.dp)
    ){
        OutlinedCard(
            onClick = {
                navController.navigate(Screen.BookReader.createRoute(bookId = chapter.bookId, chapterId = chapter.chapterId))
                    },
            modifier = Modifier
                .fillMaxWidth(),
            colors = CardDefaults.outlinedCardColors().copy(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContentColor = MaterialTheme.colorScheme.onSecondaryContainer
            ),
            enabled = hisCardEnabled
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Row {
                    Text(
                        text = "Глава ${chapter.chapterTitle}",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = if(hisCardEnabled) MaterialTheme.colorScheme.onPrimaryContainer
                            else MaterialTheme.colorScheme.onSecondaryContainer,
                            textAlign = TextAlign.Justify
                        )
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Страниц: ${chapter.chapterLength.toString()}",
                        textAlign = TextAlign.Start,
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = if(hisCardEnabled) MaterialTheme.colorScheme.onPrimaryContainer
                            else MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    )
                    Text(
                        text = "Глава: ${chapter.chapterNumber}",
                        textAlign = TextAlign.Start,
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = if(hisCardEnabled) MaterialTheme.colorScheme.onPrimaryContainer
                            else MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    )

                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        if ((
            bookViewModel.sharedViewModel.isUserHasPermission(
                addedUserId = chapter.addedBy,
                hasEditor = true,
                hasPublisher = true,
                hasAdmin = true
            ))) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomEnd)
                    .offset(y = (14).dp)
                    .padding(end = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Logger.debug(
                    "UserRepository",
                    "userPermission = ${bookViewModel.sharedViewModel.currentAuthorizingUser.value}"
                )
                SmallFloatingActionButton(
                    onClick = {
                        // TODO: Add navigate to edit chapter page
                    },
                    modifier = Modifier
                        .size(32.dp),
                    containerColor = MaterialTheme.colorScheme.secondaryContainer

                ) {
                    Icon(
                        Icons.Filled.Edit,
                        contentDescription = "Edit",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                SmallFloatingActionButton(
                    onClick = {
                        // TODO: Delete chapter
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
    Spacer(modifier = Modifier.height(24.dp))
}