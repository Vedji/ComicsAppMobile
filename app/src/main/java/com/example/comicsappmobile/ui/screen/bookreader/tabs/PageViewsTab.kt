package com.example.comicsappmobile.ui.screen.bookreader.tabs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.twotone.Favorite
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.example.comicsappmobile.navigation.Screen
import com.example.comicsappmobile.ui.components.ImageByID
import com.example.comicsappmobile.ui.components.ThemedErrorCard
import com.example.comicsappmobile.utils.Logger
import com.example.comicsappmobile.utils.vibrate
import com.example.comicsappmobile.ui.presentation.viewmodel.PagesViewModel
import com.example.comicsappmobile.ui.presentation.viewmodel.UiState
import com.example.comicsappmobile.ui.presentation.model.PageUiModel
import kotlinx.coroutines.launch
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable



@Composable
fun PageViewsTab(
    navController: NavHostController,
    pagesViewModel: PagesViewModel,
    paddingValues: PaddingValues,
    onImageOneTap: (offset: Offset) -> Unit = {},   // visiblyChapterSettings.value = !visiblyChapterSettings.value
    onSetTap: () -> Unit = {},
    getVisible: () -> Boolean = { true },
    getPageCount: () -> Int = { 0 }
) {
    val pagesUiState by pagesViewModel.pagesUiState.collectAsState()
    val pagerState = rememberPagerState(
        pageCount = {
            if (pagesUiState is UiState.Success)
                pagesUiState.data?.size ?: 0
            else 0
        })
    val previousChapterIdState by pagesViewModel.previousChapterIdState.collectAsState()
    val followingChapterIdState by pagesViewModel.followingChapterIdState.collectAsState()

    val userAuth by pagesViewModel.globalState.authUser.collectAsState()
    val bookInFavorite by pagesViewModel.bookInFavorite.collectAsState()

    val pages: List<PageUiModel> =
        if (pagesUiState is UiState.Success && pagesUiState.data is List<PageUiModel>)
            pagesUiState.data ?: emptyList()
        else
            emptyList()
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val screenWidthDp = configuration.screenWidthDp
    val screenWidthPx = with(density) { screenWidthDp.dp.toPx() }
    val context = LocalContext.current.applicationContext
    val coroutineScope = rememberCoroutineScope()

    val startPage by pagesViewModel.startPage.collectAsState()
    LaunchedEffect(startPage) {
        pagerState.scrollToPage(startPage)
    }

    Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
        when (pagesUiState) {
            is UiState.Success -> {
                if (pages.size == 0) {
                    Text(text = "У главы отсутствуют страницы")
                    Row(
                        modifier = Modifier.fillMaxWidth().align(Alignment.Center),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        AssistChip(
                            onClick = {
                                if (pagerState.currentPage > 0)
                                    coroutineScope.launch {
                                        pagerState.scrollToPage(page = pagerState.currentPage - 1)
                                    }
                                else {
                                    if (previousChapterIdState is UiState.Success) {
                                        pagesViewModel.goLast()
                                    } else {
                                        vibrate(context)
                                        navController.navigate(
                                            Screen.AboutBook.createRoute(
                                                pagesViewModel.getBookId().toString()
                                            )
                                        )
                                    }

                                }
                            },
                            label = { Text("Предыдущая глава") },
                            leadingIcon = {
                                Icon(
                                    Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                    contentDescription = "Localized description",
                                    Modifier.size(AssistChipDefaults.IconSize)
                                )
                            }
                        )
                        AssistChip(
                            onClick = {
                                if (pagerState.currentPage + 1 < pages.size)
                                    coroutineScope.launch {
                                        pagerState.scrollToPage(page = pagerState.currentPage + 1)
                                    }
                                else {
                                    if (followingChapterIdState is UiState.Success) {
                                        pagesViewModel.goNext()
                                    } else {
                                        vibrate(context)
                                        navController.navigate(
                                            Screen.AboutBook.createRoute(
                                                pagesViewModel.getBookId().toString()
                                            )
                                        )
                                    }
                                }
                            },
                            label = { Text("Следующая глава") },
                            leadingIcon = {
                                Icon(
                                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = "Localized description",
                                    modifier = Modifier.size(AssistChipDefaults.IconSize)
                                )
                            }
                        )
                    }
                }
                HorizontalPager(
                    state = pagerState,
                    beyondViewportPageCount = 50,
                    pageSpacing = 4.dp, // Отступ между страницами
                    modifier = Modifier
                        .fillMaxWidth()
                        .zIndex(0f),
                ) { page ->
                    // Содержимое каждой страницы
                    val zoomState = rememberZoomState()
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp)
                            .zIndex(if (pagerState.currentPage == page) 2f else 1f)
                            .clipToBounds()
                    ) {
                        ImageByID(
                            imageId = pages[page].pageImageId,
                            modifier = Modifier
                                .fillMaxSize()
                                .zoomable(
                                    zoomState,
                                    enableOneFingerZoom = false,
                                    onTap = {

                                        // Left click
                                        if (it.x.dp < (screenWidthPx * 0.33).dp) {
                                            if (pagerState.currentPage > 0)
                                                coroutineScope.launch {
                                                    pagerState.animateScrollToPage(page = pagerState.currentPage - 1)
                                                }
                                            else {
                                                if (previousChapterIdState is UiState.Success) {
                                                    pagesViewModel.goLast()
                                                } else {
                                                    vibrate(context)
                                                    navController.navigate(
                                                        Screen.AboutBook.createRoute(
                                                            pagesViewModel.getBookId().toString()
                                                        )
                                                    )
                                                }

                                            }
                                            // Right click
                                        } else if (it.x.dp > (screenWidthPx * 0.66).dp) {
                                            if (pagerState.currentPage + 1 < pages.size)
                                                coroutineScope.launch {
                                                    pagerState.animateScrollToPage(page = pagerState.currentPage + 1)
                                                }
                                            else {
                                                if (followingChapterIdState is UiState.Success) {
                                                    pagesViewModel.goNext()
                                                } else {
                                                    vibrate(context)
                                                    navController.navigate(
                                                        Screen.AboutBook.createRoute(
                                                            pagesViewModel.getBookId().toString()
                                                        )
                                                    )
                                                }
                                            }
                                        } else {
                                            Logger.debug("PageViews", "Middle tap")
                                            onImageOneTap(it)
                                        }
                                    }),
                            contentScale = ContentScale.Fit
                        )
                    }
                }
            }

            is UiState.Error -> {
                ThemedErrorCard(pagesUiState as UiState.Error<*>)
            }

            else -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }

        // Pages Navigator
        AnimatedVisibility(
            visible = (getVisible()),
            enter = fadeIn(animationSpec = tween(durationMillis = 500)) + slideInVertically(
                animationSpec = tween(durationMillis = 500),
                initialOffsetY = { it }
            ),
            exit = fadeOut(animationSpec = tween(durationMillis = 500)) + slideOutVertically(
                animationSpec = tween(durationMillis = 500),
                targetOffsetY = { it },
            ),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 36.dp)
                .padding(horizontal = 84.dp)
                .heightIn(36.dp, 42.dp)
        ) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .clip(MaterialTheme.shapes.extraLarge)
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.secondary,
                        MaterialTheme.shapes.extraLarge
                    )
                    .background(MaterialTheme.colorScheme.secondaryContainer),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Box(
                    modifier = Modifier.fillMaxSize().weight(1f)
                ) {

                    Text(
                        text = "${pagerState.currentPage + 1} / ${pagerState.pageCount}",//pages.size
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    )
                }
                if (userAuth.userId > 0) {
                    VerticalDivider(
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Box(
                        modifier = Modifier.fillMaxSize().weight(1f)
                    ) {
                        IconButton(
                            onClick = { pagesViewModel.switchFavoriteBook() },
                            modifier = Modifier.align(Alignment.Center)
                        ) {
                            Icon(
                                if ((bookInFavorite.data?.favoriteId ?: 0) > 0 &&
                                    (bookInFavorite.data?.chapterId ?: -2) == pagesViewModel.getChapterId()
                                )
                                    Icons.Filled.Favorite
                                else
                                    Icons.TwoTone.Favorite,
                                "Go to favorite list user"
                            )
                        }
                    }
                }

                VerticalDivider(color = MaterialTheme.colorScheme.secondary)
                Box(
                    modifier = Modifier.fillMaxSize().weight(1f)) {
                    IconButton(
                        onClick = onSetTap,
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.List,
                            "Open chapter list"
                        )
                    }
                }
            }
        }
    }
}