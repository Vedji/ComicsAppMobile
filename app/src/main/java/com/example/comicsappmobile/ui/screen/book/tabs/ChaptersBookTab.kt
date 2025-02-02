package com.example.comicsappmobile.ui.screen.book.tabs

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.comicsappmobile.navigation.Screen
import com.example.comicsappmobile.ui.components.ThemedAlertDialog
import com.example.comicsappmobile.ui.components.ThemedStateView
import com.example.comicsappmobile.ui.presentation.model.ChapterUiModel
import com.example.comicsappmobile.ui.presentation.viewmodel.BookViewModel
import com.example.comicsappmobile.ui.presentation.viewmodel.UiState
import com.example.comicsappmobile.utils.Logger
import kotlinx.coroutines.launch


@Composable
fun ChaptersBookTab(bookViewModel: BookViewModel, navController: NavController) {
    val chaptersUiState by bookViewModel.chaptersUiState.collectAsState()
    var sortByAsc by remember { mutableStateOf(true) }
    val book by bookViewModel.bookUiState.collectAsState()
    val authUser by bookViewModel.globalState.authUser.collectAsState()
    val isUserHasPermission = remember {  mutableStateOf(false) }
    LaunchedEffect(book, authUser) {
        isUserHasPermission.value =
            (book.data?.bookAddedBy ?: -2) == authUser.userId || (authUser.permission ?: -1) >= 4
    }

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
                                text = "Сортировка",
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
                        if (isUserHasPermission.value) {
                            SmallFloatingActionButton(
                                onClick = {
                                    navController.navigate(
                                        Screen.ChapterEditorScreen.createRoute(
                                            bookId = bookViewModel.bookId,
                                            chapterId = -1)
                                    )
                                },
                                modifier = Modifier
                                    .size(40.dp)
                                    .offset(x = (4).dp)
                                    .border(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.outline,
                                        shape = MaterialTheme.shapes.medium
                                    ),
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
                            genres = if (sortByAsc)
                                genres.sortedBy { it.chapterNumber }
                            else
                                genres.sortedByDescending { it.chapterNumber }

                            for (item in genres){
                                ChapterCard(
                                    chapter = item,
                                    navController = navController,
                                    bookViewModel = bookViewModel,
                                    isEditedToolsVisibility = isUserHasPermission.value
                                )
                            }

                            if ( chaptersUiState.data!!.isEmpty() ?: false){
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = "Эта работа не содержит глав",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary,
                                        textAlign = TextAlign.Center
                                    ),
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
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center
                                    )
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
fun ChapterCard(
    chapter: ChapterUiModel,
    navController: NavController,
    bookViewModel: BookViewModel,
    isEditedToolsVisibility: Boolean = false
) {
    val isOpenDeleted = remember { mutableStateOf(false) }
    val isOpenDeletedError = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    if (isOpenDeleted.value)
        ThemedAlertDialog(
            titleText = "Вы действительно хотите удалить главу?",
            messageText = "",
            onConfirm = {
                coroutineScope.launch {
                    val a = bookViewModel.deleteChapter(chapterId = chapter.chapterId)
                    if (!a) isOpenDeletedError.value = true
                    isOpenDeleted.value = false
                } },
            onDismiss = { isOpenDeleted.value = false },
        )

    if (isOpenDeletedError.value)
        ThemedAlertDialog(
            titleText = "Ошибка при удалении главы ${chapter.chapterId}",
            messageText = "Какая то неизвесная оошибка(((",
            onConfirm = { isOpenDeletedError.value = false },
            onDismiss = { isOpenDeletedError.value = false }
        )

    val hisCardEnabled = chapter.chapterLength > 0
    Box(
        modifier = Modifier.fillMaxWidth().padding(top = 12.dp)
    ) {
        OutlinedCard(
            onClick = {
                navController.navigate(
                    Screen.BookReader.createRoute(
                        bookId = chapter.bookId,
                        chapterId = chapter.chapterId
                    )
                )
            },
            modifier = Modifier
                .fillMaxWidth(),
            colors = CardDefaults.outlinedCardColors().copy(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContentColor = MaterialTheme.colorScheme.onSecondaryContainer
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
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
                            color = if (hisCardEnabled) MaterialTheme.colorScheme.onPrimaryContainer
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
                            color = if (hisCardEnabled) MaterialTheme.colorScheme.onPrimaryContainer
                            else MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    )
                    Text(
                        text = "Глава: ${chapter.chapterNumber}",
                        textAlign = TextAlign.Start,
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = if (hisCardEnabled) MaterialTheme.colorScheme.onPrimaryContainer
                            else MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    )

                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        if (isEditedToolsVisibility) {
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
                        navController.navigate(
                            Screen.ChapterEditorScreen.createRoute(
                                bookId = chapter.bookId,
                                chapterId = chapter.chapterId
                            )
                        )
                    },
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
                        Icons.Filled.Edit,
                        contentDescription = "Edit",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                SmallFloatingActionButton(
                    onClick = { isOpenDeleted.value = true },
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