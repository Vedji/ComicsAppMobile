package com.example.comicsapp.ui.components

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import okhttp3.OkHttpClient
import okhttp3.Request
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.core.graphics.drawable.toDrawable
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import coil.disk.DiskCache
import coil.imageLoader
import coil.memory.MemoryCache
import coil.size.Scale
import com.example.comicsapp.R
import com.example.comicsapp.ui.viewmodel.AppViewModel
import okhttp3.CacheControl


@Composable
fun LoadImageWithCacheCheck(url: String, appViewModel: AppViewModel, modifier: Modifier = Modifier, contentScale: ContentScale = ContentScale.None) {
    val context = LocalContext.current

    val imageLoader = (context.applicationContext).imageLoader


    val key = MemoryCache.Key(url)
    // val cachedImage = imageLoader.memoryCache?.get(key)
    val request = ImageRequest.Builder(context)
        .data(url) // URL изображения
        .memoryCachePolicy(CachePolicy.ENABLED) // Проверка в памяти
        .diskCachePolicy(CachePolicy.ENABLED)   // Проверка на диске
        .networkCachePolicy(CachePolicy.READ_ONLY) // Загрузка из сети при необходимости
        .listener { request, result ->
            Log.d("LoadImageWithCacheCheck", "AsyncsImageLoad from ${result.dataSource.name}")
        }
        .build()
    // imageLoader.enqueue(request)

    // if (cachedImage != null) {
    //     // Используем изображение из кэша
    //     Image(
    //         painter = BitmapPainter(cachedImage.bitmap.asImageBitmap()),
    //         contentDescription = null,
    //         modifier = modifier,
    //         contentScale = contentScale
    //     )
    // } else {
    //     // Загружаем изображение через Coil
    //
    //     AsyncImage(
    //         model = request,
    //         contentDescription = null,
    //         imageLoader = imageLoader,
    //         modifier = modifier,
    //         contentScale = contentScale
    //     )
    // }

    // Проверка наличия изображения в кэше
    AsyncImage(
        model = request,
        imageLoader = imageLoader,
        contentDescription = "Tested",
        modifier = modifier,
        contentScale = contentScale
    )


}

