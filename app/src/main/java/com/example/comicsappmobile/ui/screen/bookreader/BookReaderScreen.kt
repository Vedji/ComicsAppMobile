package com.example.comicsappmobile.ui.screen.bookreader

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.comicsappmobile.navigation.Screen
import com.example.comicsappmobile.ui.screen.bookreader.tabs.ChapterListTab
import com.example.comicsappmobile.ui.screen.bookreader.tabs.PageViewsTab
import com.example.comicsappmobile.utils.Logger
import com.example.comicsappmobile.ui.presentation.viewmodel.PagesViewModel
import com.example.comicsappmobile.ui.presentation.model.ChapterUiModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class,
    ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class
)
@Composable
fun BookReaderScreen(
    bookId: Int = -2,
    chapterId: Int = -2,
    navController: NavHostController,
    pagesViewModel: PagesViewModel = koinViewModel { parametersOf(bookId, chapterId) }
) {
     Logger.debug("BookReaderScreen", "Created at (bookId = '$bookId', chapterId = '$chapterId')")
    val currentChapterUiState by pagesViewModel.currentChapter.collectAsState()

    // is App topBar and Buttons chapters is visible
    val visiblyChapterSettings = rememberSaveable() { mutableStateOf(true) }
    val currentSelectedTab = remember { mutableIntStateOf(0) }

    val currentPageNumber = remember { mutableIntStateOf(0) }
    // Pager State
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AnimatedVisibility(
                visible = (visiblyChapterSettings.value && currentSelectedTab.intValue == 0) || currentSelectedTab.intValue == 1,
                enter = fadeIn(animationSpec = tween(durationMillis = 500)) + slideInVertically(
                    animationSpec = tween(durationMillis = 500),
                    initialOffsetY = { -it }
                ),
                exit = fadeOut(animationSpec = tween(durationMillis = 500)) + slideOutVertically(
                    animationSpec = tween(durationMillis = 500),
                    targetOffsetY = { -it }
                )
            ){
                TopAppBar(
                    backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.background,
                ) {
                    when (currentSelectedTab.intValue) {
                        0 -> {
                            val chapter = currentChapterUiState.data ?: ChapterUiModel()
                            IconButton(
                                onClick = {
                                    navController.navigate(
                                        Screen.AboutBook.createRoute(
                                            pagesViewModel.getBookId().toString()
                                        )) },
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                    "Go to favorite list user"
                                )
                            }
                            Text(
                                modifier = Modifier.padding(start = 0.dp, end = 12.dp),
                                text = "Глава ${chapter.chapterNumber} ${if (chapter.chapterTitle.isNotEmpty()) ": " + chapter.chapterTitle else ""}",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                textAlign = TextAlign.Justify
                            )
                        }
                        1 -> {
                            IconButton(
                                onClick = { currentSelectedTab.intValue = 0},
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                    "Go to favorite list user"
                                )
                            }
                        }

                        else -> {
                            Text(text = "No selected tab")
                        }
                    }

                }
            }

        },
        content = {
            when(currentSelectedTab.intValue){
                0 -> {

                }
                1 -> {

                }
                else -> {
                    Text(text = "Сстраницы с номером ${currentSelectedTab.intValue} не определенно")
                }

            }

            AnimatedVisibility(
                visible = currentSelectedTab.intValue == 0,
                enter = fadeIn(animationSpec = tween(durationMillis = 500)) + slideInVertically(
                    animationSpec = tween(durationMillis = 500),
                    initialOffsetY = { it }
                ),
                exit = fadeOut(animationSpec = tween(durationMillis = 500)) + slideOutVertically(
                    animationSpec = tween(durationMillis = 500),
                    targetOffsetY = { it }
                )
            ) {
                PageViewsTab(
                    navController = navController,
                    pagesViewModel = pagesViewModel,
                    paddingValues = it,
                    onImageOneTap = {
                        visiblyChapterSettings.value = !visiblyChapterSettings.value
                        Logger.debug("onImageOneTap", "offset = '$it'")
                    },
                    onSetTap = { currentSelectedTab.intValue = 1 },
                    getVisible = { (visiblyChapterSettings.value && currentSelectedTab.intValue == 0) || currentSelectedTab.intValue == 1 }
                )
            }


            AnimatedVisibility(
                visible = currentSelectedTab.intValue == 1,
                enter = fadeIn(animationSpec = tween(durationMillis = 500)) + slideInVertically(
                    animationSpec = tween(durationMillis = 500),
                    initialOffsetY = { it }
                ),
                exit = fadeOut(animationSpec = tween(durationMillis = 500)) + slideOutVertically(
                    animationSpec = tween(durationMillis = 500),
                    targetOffsetY = { it }
                )
            ) {
                ChapterListTab(
                    navController = navController,
                    pagesViewModel = pagesViewModel,
                    paddingValues = it,
                    onChapterSelected = { currentSelectedTab.intValue = 0 }
                )
            }

        }
    )
}


