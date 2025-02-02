package com.example.comicsappmobile.ui.screen.catalog.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.comicsappmobile.navigation.Screen
import com.example.comicsappmobile.ui.components.ImageByID
import com.example.comicsappmobile.ui.presentation.model.BookUiModel
import com.example.comicsappmobile.ui.presentation.viewmodel.CatalogViewModel
import com.example.comicsappmobile.ui.presentation.viewmodel.UiState
import com.example.comicsappmobile.utils.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun CatalogBooksTab(
    navController: NavHostController,
    catalogViewModel: CatalogViewModel,
    innerPadding: PaddingValues = PaddingValues(0.dp)
){
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val bookUiState by catalogViewModel.booksUiState.collectAsState()
    val books = if (bookUiState.data is List<BookUiModel>) bookUiState.data else emptyList()
    CatalogBookResponsiveGrid(
        modifier = Modifier
            .padding(innerPadding),
        navController = navController,
        books = books ?: emptyList<BookUiModel>(),
        loadingMoreItems = {
            coroutineScope.launch {
                catalogViewModel.loadCatalogBooks()
            }
        },
        footer = {
            Box(
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.TopCenter
            ) {
                if (bookUiState is UiState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.TopCenter))
                }
                if (bookUiState is UiState.Success && books?.size == 0 ?: false) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "По вашему запросу ничего не найдено",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    )
}

@Composable
fun CatalogBookResponsiveGrid(
    navController: NavHostController,
    books: List<BookUiModel>,
    loadingMoreItems: () -> Unit = {},
    footer: @Composable () -> Unit = {},
    modifier: Modifier
){
    val bookCatalogListState = rememberLazyGridState()

    // Calculate Grid Values
    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val colCount = (screenWidthDp * 1 / 162).toInt()
    val imgWidth = ((screenWidthDp / colCount) * 0.9).toInt()
    val imgHeight = (imgWidth * 1.4f).toInt()
    val imgPadding = ((imgWidth * 0.08f)).toInt()
    Logger.debug("CatalogBookResponsiveGrid", "screenWidthDp = '${screenWidthDp}'")
    Logger.debug("CatalogBookResponsiveGrid", "colCount = '${colCount}'")

    LaunchedEffect(bookCatalogListState) {
        snapshotFlow { bookCatalogListState.layoutInfo.visibleItemsInfo }
            .collect { visibleItems ->
                val lastVisibleItemIndex = visibleItems.lastOrNull()?.index
                val totalItemsCount = bookCatalogListState.layoutInfo.totalItemsCount
                if (lastVisibleItemIndex == totalItemsCount - 1) {
                    loadingMoreItems() // Вызываем подгрузку данных
                }
            }
    }


    LazyVerticalGrid(
        state = bookCatalogListState,
        columns = GridCells.Fixed(colCount),
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        items(books.count()) { item ->
            CatalogBookGridItem(
                navController = navController,
                book = books[item],
                columnCount = colCount,
                imgWidth = imgWidth,
                imgHeight = imgHeight,
                imgPadding = imgPadding
            )
        }
        item(span = { GridItemSpan(currentLineSpan = colCount) }) {
            footer()
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogBookGridItem(
    navController: NavHostController,
    book: BookUiModel,
    columnCount: Int,
    imgHeight: Int,
    imgWidth: Int,
    imgPadding: Int
) {
    TooltipBox(
        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(
            spacingBetweenTooltipAndAnchor = (-20).dp
        ),
        tooltip = { PlainTooltip { Text(book.bookDescription) } },
        state = rememberTooltipState()
    ) {
        Box(
            modifier = Modifier
                .padding(imgPadding.dp)
                .wrapContentWidth()
                .clickable { navController.navigate(Screen.AboutBook.createRoute(book.bookId.toString())) }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
            ) {
                ImageByID(
                    imageId = book.bookTitleImageId,
                    modifier = Modifier
                        .size((imgWidth).dp, (imgHeight).dp)
                        .clip(MaterialTheme.shapes.large),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = book.rusTitle,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 3
                )
            }
            Row(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset(x = (-imgPadding).dp, y = imgPadding.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.onSurface)
                    .padding(4.dp)
            ) {
                Text(
                    text = "${book.bookRating}",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.inverseOnSurface
                    ),
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                )
                // Icon(
                //     imageVector =  Icons.Filled.Star,
                //     contentDescription = "Star",
                //     tint = Color(0xFFFFD700), // Цвет звезды
                //     modifier = Modifier
                //         .size(16.dp) // Размер звезды
                //         .align(Alignment.CenterVertically)
                // )
            }

        }
    }
}
