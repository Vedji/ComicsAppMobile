package com.example.comicsappmobile.ui.screen.book

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.twotone.Favorite
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabIndicatorScope
import androidx.compose.material3.TabPosition
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.comicsappmobile.R
import com.example.comicsappmobile.navigation.Screen
import com.example.comicsappmobile.ui.components.ImageByID
import com.example.comicsappmobile.ui.screen.book.tabs.ChaptersBookTab
import com.example.comicsappmobile.ui.screen.book.tabs.CommentsBookTab
import com.example.comicsappmobile.ui.screen.book.tabs.DescriptionBookTab
import com.example.comicsappmobile.utils.Logger
import com.example.comicsappmobile.ui.presentation.viewmodel.BookViewModel
import com.example.comicsappmobile.ui.presentation.viewmodel.UiState
import com.example.comicsappmobile.utils.vibrate
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookScreen(
    navController: NavHostController,
    bookId: Int,
    selectionTab: Int = 0,
    bookViewModel: BookViewModel = koinViewModel { parametersOf(bookId) }
) {

    val bookInUserFavorite = bookViewModel.bookInFavorite.collectAsState()
    val bookChapters = bookViewModel.chaptersUiState.collectAsState()
    val bookAboutUi by bookViewModel.bookUiState.collectAsState()
    val authUser by bookViewModel.globalState.authUser.collectAsState()

    val configuration = LocalConfiguration.current
    val context = LocalContext.current.applicationContext

    val screenHeightDp = configuration.screenHeightDp.dp
    val screenHeightFloat = configuration.screenHeightDp.toFloat()
    val coroutineScope = rememberCoroutineScope()

    val scaffoldState = rememberBottomSheetScaffoldState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    var state by remember { mutableStateOf(selectionTab) }
    val titles =
        listOf(
            "Описание",
            "Главы",
            "Отзывы"
        )
    Logger.debug(
        "BottomSheetScaffold",
        (scaffoldState.bottomSheetState.currentValue.name == "Expanded").toString()
    )
    BottomSheetScaffold(
        /*
        sheetSwipeEnabled = false,
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(start = 0.dp),
                navigationIcon = {
                    TooltipBox(
                        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider() ,
                        tooltip = { PlainTooltip { Text("В каталог") } },
                        state = rememberTooltipState()
                    ) {
                        IconButton(onClick = { navController.navigate(Screen.Catalog.route) }) { // TODO: open nav
                            Icon(
                                painter = painterResource(R.drawable.baseline_tune_24),
                                "",
                                tint = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                },
                title = {},
                windowInsets = WindowInsets(0.dp),
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent
                )
            )
        },
         */
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        scaffoldState = scaffoldState,
        sheetDragHandle = {
            SecondaryScrollableTabRow(
                containerColor = MaterialTheme.colorScheme.surface,
                selectedTabIndex = state,
                indicator = { FancyAnimatedIndicatorWithModifier(state) }
            ) {
                titles.forEachIndexed { index, title ->
                    Tab(
                        selected = state == index,
                        onClick = { state = index },
                        text = {
                            Text(
                                text = title,
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    )
                }
            }
        },
        sheetContainerColor = MaterialTheme.colorScheme.surface,
        sheetShape =
        if (scaffoldState.bottomSheetState.currentValue.name == "Expanded") RoundedCornerShape(0.dp)
        else MaterialTheme.shapes.extraLarge,
        sheetContent = {
            Spacer(modifier = Modifier.height(24.dp))
            when (state) {
                0 -> { DescriptionBookTab(bookViewModel) }
                1 -> { ChaptersBookTab(bookViewModel, navController) }
                2 -> { CommentsBookTab(bookViewModel) }
                else -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                    ) {
                        Text(
                            modifier = Modifier,
                            text = "No Tab",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(screenHeightDp))
        },
        sheetPeekHeight = (screenHeightFloat * 0.46).dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.background,      // Цвет в верхней части
                                MaterialTheme.colorScheme.background.copy(alpha = 0.7f), // Полупрозрачный
                                Color.Transparent  // Полностью прозрачный
                            )
                        )
                    ),
                horizontalArrangement =
                if (authUser.userId > 0) Arrangement.SpaceBetween else Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    colors = IconButtonDefaults.iconButtonColors().copy(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    onClick = {
                        navController.navigate(Screen.Catalog.route)
                    }) {
                    Icon(
                        imageVector = Icons.Filled.Home,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                if (authUser.userId > 0) {
                    IconButton(
                        colors = IconButtonDefaults.iconButtonColors().copy(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        onClick = {
                            navController.navigate(Screen.EditedBookScreen.createRoute(bookId = bookViewModel.bookId))
                        }) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
            HorizontalDivider()
        }
        ImageByID(
            bookAboutUi.data?.bookTitleImageId ?: -1,
            modifier = Modifier
                .alpha(0.3f)
                .offset(y = (-64).dp)
                .fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0f),
                            MaterialTheme.colorScheme.surface
                        ),
                        startY = (screenHeightFloat * 0.4).toFloat(),
                        endY = (screenHeightFloat * 1.4).toFloat()
                    )
                )
                .alpha(0.75f)
        )
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .padding(top = 96.dp)
                        .height(245.dp)
                        .aspectRatio(0.65f)
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.outline,
                            MaterialTheme.shapes.large
                        )
                ) {
                    ImageByID(
                        imageId = bookAboutUi.data?.bookTitleImageId ?: -1,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Row(
                        modifier = Modifier
                            .offset(y = (-4).dp)
                            .background(
                                MaterialTheme.colorScheme.background,
                                MaterialTheme.shapes.medium
                            )
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.outline,
                                MaterialTheme.shapes.medium
                            )
                            .padding(6.dp)
                            .height(18.dp)
                            .align(Alignment.BottomCenter),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier
                                .wrapContentSize(),
                            textAlign = TextAlign.Justify,
                            text = "${bookAboutUi.data?.bookRating ?: 0f}",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Star",
                            tint = Color(0xFFFFD700),
                            modifier = Modifier
                                .size(18.dp)
                        )
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                        .padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (bookChapters.value is UiState.Success) {
                        val chapters = bookChapters.value.data ?: emptyList()
                        if (bookChapters.value.data?.any { it.chapterLength > 0 } == true) {
                            val inUserFavorite: Boolean =
                                bookChapters.value is UiState.Success && bookChapters.value.data?.any { it.chapterLength > 0 } == true && bookInUserFavorite.value is UiState.Success &&
                                        bookInUserFavorite.value.data != null &&
                                        bookInUserFavorite.value.data!!.favoriteId > 0 &&
                                        bookInUserFavorite.value.data!!.chapterId > 0
                            IconButton(
                                onClick = {
                                    if (bookChapters.value is UiState.Success && !bookChapters.value.data.isNullOrEmpty()) {
                                        if (!inUserFavorite) {
                                            val chapter =
                                                bookChapters.value.data!!.filter { it.chapterLength > 0 }
                                                    .first()
                                            navController.navigate(
                                                Screen.BookReader.createRoute(
                                                    chapter.bookId,
                                                    chapter.chapterId
                                                )
                                            )
                                        } else if (bookInUserFavorite.value is UiState.Success &&
                                            bookInUserFavorite.value.data != null &&
                                            bookInUserFavorite.value.data!!.bookId > 0 &&
                                            bookInUserFavorite.value.data!!.chapterId > 0
                                        ) {
                                            navController.navigate(
                                                Screen.BookReader.createRoute(
                                                    bookInUserFavorite.value.data!!.bookId,
                                                    bookInUserFavorite.value.data!!.chapterId
                                                )
                                            )

                                        } else {
                                            vibrate(context)
                                        }
                                    }
                                },
                                modifier = Modifier.size(48.dp)
                            ) {
                                Icon(
                                    painter = painterResource(
                                        if (inUserFavorite) R.drawable.round_play_arrow_24 else R.drawable.sharp_resume_24
                                    ),
                                    contentDescription = "Go to read",
                                    modifier = Modifier.size(42.dp)
                                )
                            }
                        }
                    }
                    if (authUser.userId > 0) {
                        val inUserFavorite: Boolean = bookInUserFavorite.value is UiState.Success &&
                                bookInUserFavorite.value.data != null &&
                                bookInUserFavorite.value.data!!.favoriteId > 0
                        IconButton(
                            onClick = { bookViewModel.switchFavoriteBook() },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = if (inUserFavorite) Icons.Filled.Favorite else Icons.TwoTone.Favorite,
                                "Book in favorite",
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabIndicatorScope.FancyAnimatedIndicatorWithModifier(index: Int) {
    val colors =
        listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.primary,
        )
    var startAnimatable by remember { mutableStateOf<Animatable<Dp, AnimationVector1D>?>(null) }
    var endAnimatable by remember { mutableStateOf<Animatable<Dp, AnimationVector1D>?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val indicatorColor: Color by animateColorAsState(colors[index % colors.size], label = "")
    Box(
        Modifier.tabIndicatorLayout {
                measurable: Measurable,
                constraints: Constraints,
                tabPositions: List<TabPosition> ->
            val newStart = tabPositions[index].left
            val newEnd = tabPositions[index].right
            val startAnim =
                startAnimatable
                    ?: Animatable(newStart, Dp.VectorConverter).also { startAnimatable = it }

            val endAnim =
                endAnimatable
                    ?: Animatable(newEnd, Dp.VectorConverter).also { endAnimatable = it }

            if (endAnim.targetValue != newEnd) {
                coroutineScope.launch {
                    endAnim.animateTo(
                        newEnd,
                        animationSpec =
                        if (endAnim.targetValue < newEnd) {
                            spring(dampingRatio = 1f, stiffness = 1000f)
                        } else {
                            spring(dampingRatio = 1f, stiffness = 50f)
                        }
                    )
                }
            }

            if (startAnim.targetValue != newStart) {
                coroutineScope.launch {
                    startAnim.animateTo(
                        newStart,
                        animationSpec =
                        // Handle directionality here, if we are moving to the right, we
                        // want the right side of the indicator to move faster, if we are
                        // moving to the left, we want the left side to move faster.
                        if (startAnim.targetValue < newStart) {
                            spring(dampingRatio = 1f, stiffness = 50f)
                        } else {
                            spring(dampingRatio = 1f, stiffness = 1000f)
                        }
                    )
                }
            }

            val indicatorEnd = endAnim.value.roundToPx()
            val indicatorStart = startAnim.value.roundToPx()

            // Apply an offset from the start to correctly position the indicator around the tab
            val placeable =
                measurable.measure(
                    constraints.copy(
                        maxWidth = indicatorEnd - indicatorStart,
                        minWidth = indicatorEnd - indicatorStart,
                    )
                )
            layout(constraints.maxWidth, constraints.maxHeight) {
                placeable.place(indicatorStart, 0)
            }
        }
            .padding(5.dp)
            .fillMaxSize()
            .drawWithContent {
                drawRoundRect(
                    color = indicatorColor,
                    cornerRadius = CornerRadius(5.dp.toPx()),
                    style = Stroke(width = 2.dp.toPx())
                )
            }
    )
}