package com.example.comicsapp.ui.view.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.comicsapp.BuildConfig
import com.example.comicsapp.R
import com.example.comicsapp.ui.viewmodel.AppViewModel


@Composable
fun ImageByID (
    imageID: Int,
    appViewModel: AppViewModel,
    contentDescription: String = "ImageByID",
    modifier: Modifier = Modifier
        .fillMaxSize()
        .alpha(0.3f)
        .padding(0.dp)
        .offset(y = (-64).dp),
    contentScale: ContentScale = ContentScale.None
)
{
    val imageURL = remember { "${BuildConfig.API_BASE_URL}/api/v1/file/$imageID/get" }

    AsyncImage(
        model = ImageRequest.Builder(appViewModel.context)
            .data(imageURL) // URL изображения
            .crossfade(true) // Плавный переход при загрузке
            .build(),
        placeholder = painterResource(id = R.drawable.background_white_theme),
        error = painterResource(id = R.drawable.background_white_theme),
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = contentScale
    )
}

