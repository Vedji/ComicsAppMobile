package com.example.comicsapp.ui.view.screens.aboutbook.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.comicsapp.ui.view.components.button.CustomButton
import com.example.comicsapp.ui.view.components.button.CustomButtonStyle
import com.example.comicsapp.ui.viewmodel.AppViewModel

@SuppressLint("MutableCollectionMutableState")
@Composable
fun TableBookChapters(navController: NavHostController, viewModel: AppViewModel){

    // val chapters by remember { mutableStateOf(viewModel.bookCatalog.getBookChaptersList()) }
    val chapters by viewModel.selectedBook.chaptersList.collectAsState()

    if (chapters.isNotEmpty())
        for (item in chapters) {
                CustomButton(
                    text = "Глава ${item.chapterNumber} - ${item.chapterTitle}",
                    onClick = {
                        viewModel.selectedBook.setChapter(item)
                        navController.navigate("book_read_screen")
                    },
                    buttonStyle = CustomButtonStyle.primary().copy(
                        shape = MaterialTheme.shapes.small
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, end = 24.dp)
                )
            Spacer(modifier = Modifier.height(8.dp))
        }
    else
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp)
        ) {
            Text(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = "Эта работа не содержит глав",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        }

}