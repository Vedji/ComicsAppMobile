package com.example.comicsapp.ui.view.screens.readbook

import android.os.Build
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.comicsapp.ui.viewmodel.AppViewModel
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput

import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.comicsapp.BuildConfig
import com.example.comicsapp.R

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun BookPreviewReadScreen(navController: NavHostController, viewModel: AppViewModel) {

    val page by viewModel.selectedBook.bookPage.collectAsState()

    val scope = rememberCoroutineScope()

    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var rotation by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(page) {
        scale = 1f
        offset = Offset.Zero
        rotation = 0f
    }


    BackHandler {
        Log.d("BackHandler test", "back")
        // Ваше поведение кнопки "Назад"
        val previousRoute = navController.previousBackStackEntry?.destination?.route
        val currentRoute = navController.currentBackStackEntry?.destination?.route
        if (currentRoute == "book_read_screen" && previousRoute == "book_read_screen")
            viewModel.selectedBook.setBackPage()
        navController.popBackStack()
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .clipToBounds()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { position ->
                        // Определяем, в какой части Box нажали
                        val boxWidth = size.width
                        if (position.x < boxWidth / 2) {
                            // Левая сторона: Уменьшение масштаба
                            // scale = (scale - 0.1f).coerceAtLeast(0.5f)
                            if (!viewModel.selectedBook.setBackPage())
                                navController.navigate("book_preview")
                        } else {
                            if (!viewModel.selectedBook.setNextPage())
                                navController.navigate("book_preview")
                        }
                    }
                )
            }
    ) {

        if (page != null && page!!.pageImageID >= 0){

            // Контроллер для жестов
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
                    .pointerInput(Unit) {
                        detectTransformGestures { _, pan, zoom, rotationChange ->
                            scale *= zoom
                            offset += pan
                            rotation += rotationChange
                        }
                    }
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = offset.x,
                        translationY = offset.y,
                        rotationZ = rotation
                    )
            ){
                val url = "${BuildConfig.API_BASE_URL}/api/v1/file/${page!!.pageImageID ?: -1}/get"
                val context = LocalContext.current
                val request = ImageRequest.Builder(context)
                    .data(url) // URL изображения
                    .memoryCachePolicy(CachePolicy.READ_ONLY) // Проверка в памяти
                    .diskCachePolicy(CachePolicy.READ_ONLY)   // Проверка на диске
                    .networkCachePolicy(CachePolicy.READ_ONLY) // Загрузка из сети при необходимости
                    .listener { request, result ->
                        Log.d("LoadImageWithCacheCheck", "AsyncsImageLoad from ${result.dataSource.name}")
                    }
                    .build()
                AsyncImage(

                    model = request,
                    placeholder = painterResource(id = R.drawable.background_white_theme),
                    error = painterResource(id = R.drawable.background_white_theme),
                    // Убедитесь, что масштабирование задано корректно
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxSize()
                        .border(1.dp, Color.Red), // Граница, если нужна
                    contentDescription = "Example image",

                    )
            }
        }

    }
}