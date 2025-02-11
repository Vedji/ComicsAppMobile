package com.example.comicsappmobile.ui.screen.chaptereditor.tabs

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.twotone.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.comicsappmobile.BuildConfig
import com.example.comicsappmobile.ui.components.ThemedInputField
import com.example.comicsappmobile.ui.presentation.model.PageUiModel
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditChapterGeneral(
    fetchedName: String = "No name",
    fetchedPages: List<PageUiModel> = emptyList(),

    inputTitleName: MutableState<String>,
    inputPagesSize: MutableIntState,
    inputPages: MutableState<List<PageUiModel>>,
    inputPagesImagesUri: MutableMap<Int, Uri>,

    paddingValues: PaddingValues,
    chapterId: Int = -1,
    setPage: (Int) -> Unit = {}
) {
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments()
    ) { uris ->
        for (uri in uris){
            val newPage = PageUiModel(
                chapterId = chapterId,
                pageId = -1,
                pageNumber = inputPagesSize.intValue++,
                pageImageId = -1
            )
            inputPages.value += newPage
            inputPagesImagesUri[newPage.pageNumber] = uri
        }
    }

    val lazyListState = rememberLazyListState()
    val reorderableLazyListState = rememberReorderableLazyListState(lazyListState) { from, to ->
        inputPages.value = inputPages.value.toMutableList().apply {
            add(to.index, removeAt(from.index))
        }
    }


    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(12.dp)
            .fillMaxSize()
            .nestedScroll(rememberNestedScrollInteropConnection())
    ) {
        Text(
            text = "Названия",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            )
        )
        /*
        IconButton(
            onClick = { inputTitleName.value = fetchedBookTitleName }
        ) {
            Icon(
                imageVector =
                if (inputTitleName.value == fetchedBookTitleName && bookId > 0) Icons.TwoTone.Check
                else Icons.TwoTone.Refresh,
                "Has been resource updated?"
            )
        }
         */
        Spacer(modifier = Modifier.height(6.dp))
        ThemedInputField(
            textFieldValue = inputTitleName,
            placeholder = "Введите название главы",
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Start
            ),
            placeholderStyle = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                textAlign = TextAlign.Start
            ),
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            cursorColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Страницы",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            TooltipBox(
                positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                tooltip = { PlainTooltip { Text("Добавить страницы") } },
                state = rememberTooltipState()
            ) {
                IconButton(onClick = { imagePickerLauncher.launch(arrayOf("image/*")) }) {
                    Icon(
                        imageVector = Icons.TwoTone.Add,
                        contentDescription = "Add pages"
                    )
                }
            }
        }
        

        LazyColumn(
            state = lazyListState,
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (inputPages.value.isNullOrEmpty()){
                item {
                    Box {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "У главы нет страниц",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            textAlign = TextAlign.Center
                        )
                    }
                }

            }
            items(inputPages.value, key = { it.pageNumber }) { item ->
                ReorderableItem(reorderableLazyListState, key = item.pageNumber) { isDragging ->
                    val elevation by animateDpAsState(if (isDragging) 4.dp else 0.dp)
                    Surface(
                        shadowElevation = elevation,
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, MaterialTheme.colorScheme.outline, MaterialTheme.shapes.medium)
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(horizontal = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            val imageUrl =
                                if (item.pageImageId > 0)
                                "${BuildConfig.API_BASE_URL}/api/v1/file/${item.pageImageId}/get"
                            else
                                "${inputPagesImagesUri[item.pageNumber]}"

                            AsyncImage(
                                model = imageUrl,
                                contentDescription = "Описание изображения", // Для доступности
                                modifier = Modifier
                                    .width(120.dp)
                                    .aspectRatio(0.65f), // Размер изображения
                                contentScale = ContentScale.Crop
                            )
                            Row{
                                IconButton(
                                    onClick = {
                                        val buffer = inputPages.value.toMutableList()
                                        buffer.remove(item)
                                        inputPages.value = buffer.toList()

                                    }
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                                }
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
    }
}