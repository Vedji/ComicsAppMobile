package com.example.comicsapp.ui.view.screens.aboutbook

import android.util.Log

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height

import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextLayoutResult

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import com.example.comicsapp.BuildConfig
import com.example.comicsapp.ui.view.components.LoadImageWithCacheCheck
import com.example.comicsapp.ui.view.components.expandedlist.ExpandedList
import com.example.comicsapp.ui.view.components.expandedlist.ExpandedListStyle
import com.example.comicsapp.ui.view.screens.aboutbook.components.AboutBookTable
import com.example.comicsapp.ui.view.screens.aboutbook.components.TableBookChapters
import com.example.comicsapp.ui.view.screens.aboutbook.components.TableBookComments
import com.example.comicsapp.ui.view.screens.aboutbook.components.editbook.editBookGenres
import com.example.comicsapp.ui.viewmodel.AppViewModel


@Composable
fun BookPreviewScreen(navController: NavHostController, viewModel: AppViewModel) {
    Log.d("BookPreviewScreen", "Run BookPreviewScreen")

    val book by viewModel.selectedBook.book.collectAsState()
    val chaptersList by viewModel.selectedBook.chaptersList.collectAsState()


    val user by viewModel.currentUser.authorizedUser.collectAsState()
    val bookAboutScrollState = rememberScrollState()
    val scrollStateBookActions = rememberScrollState()
    var selectedTable by remember { mutableIntStateOf(0) }
    val tablesItems = mutableMapOf(
        0 to "Описание",
        1 to "Главы",
        2 to "Отзывы",
    )
    if (user != null && user!!.permission > 0)
        tablesItems[3] = "Изменить"
    LaunchedEffect(bookAboutScrollState.value) {

        Log.d("bookAboutScrollState", bookAboutScrollState.value.toString())
    }
    val imageURL = remember { "${BuildConfig.API_BASE_URL}/api/v1/file/${book?.bookTitleImage ?: 0}/get" }
    val configuration = LocalConfiguration.current

    val screenHeightDp = configuration.screenHeightDp.dp
    val screenHeightFloat = configuration.screenHeightDp.toFloat()


    LoadImageWithCacheCheck(
        imageURL,
        viewModel,
        Modifier
            .alpha(0.3f)
            .fillMaxSize()
            .padding(0.dp),
        ContentScale.Crop
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0f), // Эквивалент rgba(255, 255, 255, 0)
                        MaterialTheme.colorScheme.surface // Эквивалент rgb(255, 255, 255)
                    ),
                    startY = (screenHeightFloat * 0.4).toFloat(),
                    endY = (screenHeightFloat * 1.4).toFloat() // Настройка в зависимости от нужной высоты градиента
                )
            )
            .alpha(0.25f) // Устанавливаем общую прозрачность
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(bookAboutScrollState)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            LoadImageWithCacheCheck(
                imageURL,
                viewModel,
                Modifier
                    .width(167.dp)
                    .height(245.dp)
                    .clip(RoundedCornerShape(16.dp)),
                ContentScale.Crop
            )
        }

        // Название, автор произведения и рейтинг
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                modifier = Modifier
                    .padding(start = 32.dp, end = 32.dp)
                    .fillMaxWidth(),
                text = book?.bookTitle ?: "No Title",
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 24.sp,
                    lineHeight = 29.sp,
                    fontWeight = FontWeight(400),
                    color = Color(0xFF555A3F)
                )
            )
            Row(modifier = Modifier.padding(6.dp)) {
                Text(
                    modifier = Modifier
                        .wrapContentSize(),
                    textAlign = TextAlign.Justify,
                    text = "${book?.bookRating ?: 0f}",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
                Icon(
                    imageVector =  Icons.Filled.Star,
                    contentDescription = "Star",
                    tint = Color(0xFFFFD700), // Цвет звезды
                    modifier = Modifier
                        .size(18.dp) // Размер звезды
                )
            }
        }
        // Кнопки читать и форум
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp)
        ) {

            if (chaptersList.isNotEmpty())
                Button(
                    onClick = { navController.navigate("book_read_screen") },
                    modifier = Modifier
                        .size(128.dp, 36.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)

                ) {
                    Text(
                        text = "Читать",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 18.sp,
                            lineHeight = 18.sp,
                        ),
                    )
                }

            Button(
                onClick = { /* действие */ },
                modifier = Modifier
                    .size(128.dp, 36.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)

            ) {
                Text(
                    text = "Форум",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 18.sp,
                        lineHeight = 18.sp,
                    ),
                )
            }
        }

        // Жанры и описание книги
        Spacer(modifier = Modifier.height(36.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .background(MaterialTheme.colorScheme.surface)
                .wrapContentHeight()
                .defaultMinSize(minHeight = screenHeightDp * 0.37f)

        ) {

            Spacer(modifier = Modifier.height(2.dp))
            Box (
                contentAlignment = Alignment.BottomCenter,
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
            ){
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = 12.dp)
                        .padding(bottom = 12.dp)
                    ,
                    color = MaterialTheme.colorScheme.onPrimary,
                    thickness = 2.dp // Толщина линии
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding()
                        .horizontalScroll(scrollStateBookActions),
                    horizontalArrangement = Arrangement.SpaceAround
                ){ for ((key, value ) in tablesItems){
                    Spacer(modifier = Modifier.padding(end = 12.dp))
                        Column(
                            modifier = Modifier
                                .clickable { selectedTable = key }
                                .padding(top = 6.dp),
                        ) {
                            var textLengthDp by remember { mutableStateOf(0.dp) }
                            val density = LocalDensity.current
                            Text(
                                text = value,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Light,
                                    color = MaterialTheme.colorScheme.onSurface
                                ),
                                onTextLayout = { textLayoutResult: TextLayoutResult ->
                                    val textLengthPx = textLayoutResult.size.width.toFloat()
                                    textLengthDp = with(density) { textLengthPx.toDp() }
                                }
                            )
                            if (selectedTable == key)
                                HorizontalDivider(
                                    modifier = Modifier.width(textLengthDp),
                                    color = MaterialTheme.colorScheme.primary,
                                    thickness = 2.dp // Толщина линии
                                )
                        }
                    }
                    Spacer(modifier = Modifier.padding(end = 12.dp))
                }
            }

            Spacer(modifier = Modifier.padding(top = 12.dp))
            when (selectedTable){
                0 -> AboutBookTable(navController, viewModel)
                1 -> TableBookChapters(navController, viewModel)
                2 -> TableBookComments(navController, viewModel, bookAboutScrollState)
                3 -> EditAboutBook(navController, viewModel)
            }
        }
    }
}








@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EditAboutBook(navController: NavHostController, viewModel: AppViewModel){

    val configuration = LocalConfiguration.current
    val screenHeightDp = configuration.screenHeightDp

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 24.dp, end = 24.dp)
            .defaultMinSize(minHeight = screenHeightDp.dp)
    ){

        ExpandedList(
            text = "Изменение жанров",
            style = ExpandedListStyle.secondary().copy(
                textStyle = MaterialTheme.typography.displaySmall,
                textColor = MaterialTheme.colorScheme.onSurface,
                borderColor = MaterialTheme.colorScheme.onPrimary
            ),
            ) {
            editBookGenres(navController, viewModel)
        }
    }
}










