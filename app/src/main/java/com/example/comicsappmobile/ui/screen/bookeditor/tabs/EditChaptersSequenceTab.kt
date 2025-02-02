package com.example.comicsappmobile.ui.screen.bookeditor.tabs

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.comicsappmobile.ui.presentation.model.ChapterUiModel
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

@Composable
fun EditChaptersSequenceTab(
    paddingValues: PaddingValues,
    fetchedChapters: List<ChapterUiModel>,
    chapters: MutableState<List<ChapterUiModel>>,
    setPage: (Int) -> Unit = {}
) {
    // val chapters = remember { mutableStateOf(List(10) { ChapterUiModel(chapterId = it, chapterTitle = "Глава - $it", chapterNumber = it) }) }
    val lazyListState = rememberLazyListState()
    val reorderableLazyListState = rememberReorderableLazyListState(lazyListState) { from, to ->
        chapters.value = chapters.value.toMutableList().apply {
            add(to.index, removeAt(from.index))
        }
    }

    LazyColumn(
        state = lazyListState,
        modifier = Modifier.padding(paddingValues).fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(chapters.value, key = { it.chapterId }) { item ->
            ReorderableItem(reorderableLazyListState, key = item.chapterId) { isDragging ->
                val elevation by animateDpAsState(if (isDragging) 4.dp else 0.dp)
                Surface(
                    shadowElevation = elevation,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = item.chapterTitle)
                        IconButton(
                            modifier = Modifier.draggableHandle(),
                            onClick = {}
                        ) {
                            Icon(Icons.Default.Menu, contentDescription = "Reorder")
                        }
                    }
                }
            }
        }
    }
}