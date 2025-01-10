package com.example.comicsapp.ui.view.screens.bookcatalog
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.comicsapp.BuildConfig
import com.example.comicsapp.R
import com.example.comicsapp.data.model.api.books.BookModel
import com.example.comicsapp.ui.view.components.LoadImageWithCacheCheck
import com.example.comicsapp.ui.view.components.RatingBar
import com.example.comicsapp.ui.viewmodel.AppViewModel
import kotlinx.coroutines.flow.distinctUntilChanged



@Composable
fun CatalogScreen(navController: NavHostController, appViewModel: AppViewModel) {

    Log.d("CatalogScreen", "Screen run")

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Sorting button
            Row(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.secondary, shape = RoundedCornerShape(64.dp))
                    .padding(6.dp)
                    .width(128.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.sort_from_top_to_bottom_svgrepo_com), // Ссылка на ресурс
                    contentDescription = "Описание иконки",
                    modifier = Modifier
                        .size(28.dp) // Размер иконки
                        .padding(horizontal = 4.dp),
                    tint = MaterialTheme.colorScheme.onSecondary

                )
                Text(
                    text = "Сортировка",
                    style = MaterialTheme.typography.titleLarge.copy( // Используем существующий стиль и модифицируем его
                        fontSize = 16.sp, // Изменяем размер шрифта
                        fontWeight = FontWeight.Bold // Устанавливаем жирное начертание
                    ),
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }
            // Filter button
            Row(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.secondary, shape = RoundedCornerShape(64.dp))
                    .padding(6.dp)
                    .width(128.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    painter = painterResource(R.drawable.filter_svgrepo_com), // Ссылка на ресурс
                    contentDescription = "Описание иконки",
                    modifier = Modifier
                        .size(28.dp) // Размер иконки
                        .padding(horizontal = 4.dp),
                    tint = MaterialTheme.colorScheme.onSecondary


                )
                Text(
                    text = "Фильтр",
                    style = MaterialTheme.typography.titleLarge.copy( // Используем существующий стиль и модифицируем его
                        fontSize = 16.sp, // Изменяем размер шрифта
                        fontWeight = FontWeight.Bold // Устанавливаем жирное начертание
                    ),
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }

        }
        Spacer(modifier = Modifier.height(16.dp))
        ResponsiveGrid(navController, appViewModel)
    }
}

@Composable
fun ResponsiveGrid(navController: NavHostController, viewModel: AppViewModel, columnCount: Int = 3) {
    val bookCatalogListState = rememberLazyGridState()
    val bookList by viewModel.bookCatalog.bookList.collectAsState()



    LazyVerticalGrid(
        state = bookCatalogListState,
        columns = GridCells.Fixed(columnCount),
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        items(bookList) { item ->
            Log.d("CatalogScreen", "ResponsiveGrid " + item.toString())
            GridItem(navController, viewModel, book = item, 100, 150)
        }
    }

    LaunchedEffect(bookCatalogListState) {
        snapshotFlow { bookCatalogListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .distinctUntilChanged()
            .collect { lastVisibleItem ->

                if (lastVisibleItem == viewModel.bookCatalog.bookList.value.size - 1) {
                    viewModel.bookCatalog.loadBooks()
                }
            }
    }

}

@Composable
fun GridItem(navController: NavHostController, viewModel: AppViewModel, book: BookModel, imgW: Int = 100, imgH: Int = 150, tsp: Int = 14) {
    /*
    imgW - длина изображения
    imgH - высота изображения
    tsp - высота текста произведения
     */
    Column(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxSize()
            .clickable {
                viewModel.selectedBook.setBook(book)
                Log.d("setSelectedBook", book.toString())
                navController.navigate("book_preview")
            },
        horizontalAlignment = Alignment.Start
    ) {
        val imageURL = remember { "${BuildConfig.API_BASE_URL}/api/v1/file/${book.bookTitleImage}/get" }
        LoadImageWithCacheCheck(
            imageURL,
            viewModel,
            Modifier
                .size((imgW).dp, (imgH).dp) // Настройте размеры
                .clip(RoundedCornerShape(16.dp)),
            ContentScale.Crop)
        // ImageByID(
        //     book.bookTitleImage,
        //     viewModel,
        //     "Изображение из сети",
        //     Modifier
        //         .size((imgW).dp, (imgH).dp) // Настройте размеры
        //         .clip(RoundedCornerShape(16.dp)),
        //     ContentScale.Crop
        //     )

        Text(
            text = book.bookTitle,
            style = TextStyle(
                fontSize = (tsp).sp, // Размер текста
                fontWeight = FontWeight.Bold, // Жирность
                color = Color.Black // Цвет текста (по теме)
            ),
            maxLines = 2, // Максимальное количество строк

            modifier = Modifier
                .padding(top = 8.dp, start = 2.dp) // Отступы, если нужны
                .size((imgW * 0.8).dp, (imgH * 0.22).dp)
        )
        /*
        Text(
            text = book.bookAuthor,
            style = TextStyle(
                fontSize = (tsp * 0.8).sp, // Размер текста
                fontWeight = FontWeight.Bold, // Жирность
                color = Color.Gray // Цвет текста (по теме)
            ),
            maxLines = 2, // Максимальное количество строк
            modifier = Modifier
                .padding(start = 2.dp, top = 2.dp, bottom = 2.dp) // Отступы, если нужны
                .size((imgW * 0.8).dp, (imgH * 0.18).dp)
        )
        */

        var selectedRating by remember { mutableIntStateOf(book.bookRating.toInt()) }
        RatingBar(
            currentRating = selectedRating,
            onRatingChanged = { newRating ->
                selectedRating = newRating
            },
            modifier = Modifier.padding(bottom = 8.dp)
        )
    }
}



