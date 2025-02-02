package com.example.comicsappmobile.ui.screen.bookreader.tabs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.comicsappmobile.ui.components.ThemedErrorCard
import com.example.comicsappmobile.ui.presentation.model.ChapterUiModel
import com.example.comicsappmobile.ui.presentation.viewmodel.PagesViewModel
import com.example.comicsappmobile.ui.presentation.viewmodel.UiState
import com.example.comicsappmobile.ui.theme.ComicsAppMobileTheme


@Composable
fun ChapterListTab(
    navController: NavHostController,
    pagesViewModel: PagesViewModel,
    paddingValues: PaddingValues,
    onChapterSelected: () -> Unit = {}
) {
    val chaptersUiState by pagesViewModel.chaptersUiState.collectAsState()


    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)){

        when (chaptersUiState){
            is UiState.Success -> {
                if (chaptersUiState.data != null)
                    for (item in chaptersUiState.data!!){
                        item {
                            Box(
                                modifier = Modifier.padding(
                                    top = 6.dp,
                                    bottom = 6.dp,
                                    start = 24.dp,
                                    end = 24.dp
                                )
                            ) {
                                ChapterInListCard(
                                    chapter = item,
                                    onClick = {
                                        pagesViewModel.setChapter(item.chapterId)
                                        onChapterSelected()
                                    }
                                )
                            }
                        }
                    }
                else
                    item {
                        Text(text = "Список chaptersUiState.data в ChapterListTab равен 'null' ")
                    }
            }
            is UiState.Loading -> {
                item {
                    CircularProgressIndicator(
                        modifier = Modifier
                    )
                }
            }
            is UiState.Error -> {
                item {
                    ThemedErrorCard(chaptersUiState as UiState.Error)
                }
            }
        }

    }
}


@Composable
fun ChapterInListCard(
    chapter: ChapterUiModel,
    modifier: Modifier = Modifier.fillMaxWidth(),
    onClick: () -> Unit ={}
): Unit {
    val hisCardEnabled = chapter.chapterLength > 0
    OutlinedCard(
        modifier = modifier,
        onClick = onClick,
        colors = CardDefaults.outlinedCardColors().copy(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            disabledContentColor = MaterialTheme.colorScheme.onSecondaryContainer
        ),
        enabled = chapter.chapterLength > 0
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
            Text(
                text = "Глава ${chapter.chapterNumber} ${if (chapter.chapterTitle.isNotEmpty()) ": " + chapter.chapterTitle else ""}",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                        color = if(hisCardEnabled) MaterialTheme.colorScheme.onPrimaryContainer
                    else MaterialTheme.colorScheme.onSecondaryContainer,
                    textAlign = TextAlign.Justify
                )
            )
            Text(
                text = "Страниц: ${chapter.chapterLength}",
                style = MaterialTheme.typography.labelLarge.copy(
                    color = if(hisCardEnabled) MaterialTheme.colorScheme.onPrimaryContainer
                    else MaterialTheme.colorScheme.onSecondaryContainer,
                    textAlign = TextAlign.Start
                )
            )
        }
    }

}

@Preview
@Composable
fun PreviewChapterInListCard(){
    val chapter = ChapterUiModel()
    ComicsAppMobileTheme{
        Box(
            modifier =  Modifier.fillMaxSize(),
        ){
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                ChapterInListCard(chapter, Modifier.fillMaxWidth())
            }

        }
    }

}