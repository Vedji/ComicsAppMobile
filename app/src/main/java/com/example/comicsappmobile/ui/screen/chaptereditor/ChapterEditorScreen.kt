package com.example.comicsappmobile.ui.screen.chaptereditor

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.AlertDialog
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.comicsappmobile.navigation.Screen
import com.example.comicsappmobile.ui.components.ThemedAlertDialog
import com.example.comicsappmobile.ui.presentation.model.PageUiModel
import com.example.comicsappmobile.ui.presentation.viewmodel.ChapterEditorViewModel
import com.example.comicsappmobile.ui.presentation.viewmodel.UiState
import com.example.comicsappmobile.ui.screen.chaptereditor.tabs.EditChapterGeneral
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ChapterEditorScreen(
    bookId: Int = -1,
    chapterId: Int = -1,
    navController: NavHostController,
    chapterEditorViewModel: ChapterEditorViewModel = koinViewModel { parametersOf( bookId, chapterId )}
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val selectedTab = remember { mutableIntStateOf(0) }
    val errorDialog = remember { mutableStateOf(false) }
    val confirmDialog = remember { mutableStateOf(false) }
    var isLoadingDialog: Boolean by remember { mutableStateOf(false) }

    val chapterUiModel by chapterEditorViewModel.chapterUiState.collectAsState()
    val pagesUiModel by chapterEditorViewModel.pagesUiState.collectAsState()

    var fetchedName: String =
        if (chapterUiModel is UiState.Success)
            chapterUiModel.data?.chapterTitle ?: ""
        else
            ""
    var fetchedPages: List<PageUiModel> =
        if (pagesUiModel is UiState.Success)
            pagesUiModel.data ?: emptyList()
        else
            emptyList()

    val inputTitleName = remember { mutableStateOf(fetchedName) }
    val inputPagesSize = remember { mutableIntStateOf(fetchedPages.size) }
    val inputPages = remember { mutableStateOf(fetchedPages) }
    val inputPagesImagesUri = remember { mutableMapOf<Int, Uri>() }

    LaunchedEffect(chapterUiModel) {
        fetchedName =
            if (chapterUiModel is UiState.Success)
                chapterUiModel.data?.chapterTitle ?: ""
            else
                ""
        inputTitleName.value = fetchedName
    }
    LaunchedEffect(pagesUiModel) {
        fetchedPages =
            if (pagesUiModel is UiState.Success)
                pagesUiModel.data ?: emptyList()
            else
                emptyList()
        inputPagesSize.intValue = fetchedPages.size
        inputPages.value = fetchedPages
        inputPagesImagesUri.values.clear()
    }

    if (isLoadingDialog) {
        AlertDialog(
            onDismissRequest = { isLoadingDialog = false },
            title = { Text(text = "Идет загрузка подождите") },
            text = {
                Box(modifier = Modifier.fillMaxWidth())
                { CircularProgressIndicator(modifier = Modifier.align(Alignment.Center)) }
            },
            backgroundColor = MaterialTheme.colorScheme.primaryContainer,
            confirmButton = { },
            dismissButton = {
                TextButton(
                    onClick = {
                        coroutineScope.cancel()
                        isLoadingDialog = false
                        errorDialog.value = false
                        navController.navigate(
                            Screen.AboutBook.createRoute(
                                itemId = chapterEditorViewModel.bookId.toString(),
                                selectionTab = 1
                            )
                        )
                    }) { Text("Отменить") }
            }
        )
    }

    if (errorDialog.value) {
        ThemedAlertDialog(
            titleText = "Ошибка при загрузке глав",
            messageText = "",
            onDismiss = { errorDialog.value = false },
            onConfirm = { errorDialog.value = false }
        )
    }

    if (confirmDialog.value) {
        ThemedAlertDialog(
            titleText = "Вы уверены, что хотите применить изменения?",
            messageText = "",
            onDismiss = { confirmDialog.value = false },
            onConfirm = {
                isLoadingDialog = true
                coroutineScope.launch {
                    val response = chapterEditorViewModel.uploadChapter(
                        chapterTitle = inputTitleName.value,
                        inputPages = inputPages.value,
                        inputImages = inputPagesImagesUri,
                        context = context
                    )
                    if (response) {
                        navController.navigate(
                            Screen.BookReader.createRoute(
                                bookId = chapterEditorViewModel.bookId,
                                chapterId = chapterEditorViewModel.chapterId
                            )
                        )
                    } else
                        errorDialog.value = true && isLoadingDialog
                    confirmDialog.value = false
                    isLoadingDialog = false
                }
            }
        )
    }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .height(64.dp)
                    .fillMaxWidth(),
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        when (selectedTab.intValue) {
                            0 -> {
                                navController.navigate(
                                    Screen.AboutBook.createRoute(
                                        chapterEditorViewModel.bookId.toString()
                                    )
                                )
                            }

                            1 -> {
                                selectedTab.intValue = 0
                            }   // Navigate to general book editable
                            else -> {}
                        }
                    }) {
                        Icon(
                            imageVector = when (selectedTab.intValue) {
                                0 -> Icons.AutoMirrored.Filled.KeyboardArrowLeft
                                1 -> Icons.AutoMirrored.Filled.KeyboardArrowLeft
                                else -> Icons.Default.Home
                            },
                            contentDescription = "Moved"
                        )
                    }
                    Text(
                        text = when (selectedTab.intValue) {
                            0 -> {
                                "Основная информация"
                            }

                            1 -> {
                                "Добавление страниц"
                            }

                            else -> {
                                "Неизвестная страница"
                            }
                        },
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
                if (selectedTab.intValue == 0) {
                    IconButton(
                        modifier = Modifier.align(Alignment.CenterEnd),
                        onClick = { confirmDialog.value = true }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Approve edit"
                        )
                    }
                }
            }
        }) { paddingValues ->
        when (selectedTab.intValue) {
            0 -> {
                EditChapterGeneral(
                    fetchedName = fetchedName,
                    fetchedPages = fetchedPages,
                    inputTitleName = inputTitleName,
                    inputPagesSize = inputPagesSize,
                    inputPages = inputPages,
                    inputPagesImagesUri = inputPagesImagesUri,
                    paddingValues = paddingValues,
                    setPage = { selectedTab.intValue = it })
            }
        }
    }
}