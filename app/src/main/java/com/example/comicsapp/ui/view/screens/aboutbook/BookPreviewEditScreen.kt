package com.example.comicsapp.ui.view.screens.aboutbook

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.comicsapp.ui.viewmodel.AppViewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.comicsapp.ui.components.PreviewTestCustomComponents
import com.example.comicsapp.ui.components.button.CustomButton
import com.example.comicsapp.ui.components.button.TestSmallButton
import com.example.comicsapp.ui.components.button.TestSmallButtonFactory

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun BookPreviewEditScreen(navController: NavHostController, viewModel: AppViewModel) {
    val bookPreviewEditScreenState = rememberLazyListState()
    val scrollState = rememberScrollState()
    Box (
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopStart
    ){
        PreviewTestCustomComponents()
    }

}

