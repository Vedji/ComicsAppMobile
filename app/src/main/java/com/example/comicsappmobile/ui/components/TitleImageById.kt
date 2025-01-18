package com.example.comicsappmobile.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.comicsappmobile.BuildConfig
import com.example.comicsappmobile.utils.Logger

@Composable
fun ImageByID (
    imageId: Int,
    modifier: Modifier = Modifier.size(100.dp),
    contentScale: ContentScale = ContentScale.None
){
    val imageUrl = "${BuildConfig.API_BASE_URL}/api/v1/file/${imageId}/get"
    Logger.debug("ImageByID", imageUrl)

    AsyncImage(
        model = imageUrl, // URL изображения
        contentDescription = "Описание изображения", // Для доступности
        modifier = modifier, // Размер изображения
        contentScale = contentScale
    )
}