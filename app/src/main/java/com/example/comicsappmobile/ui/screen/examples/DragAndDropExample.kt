package com.example.comicsappmobile.ui.screen.examples


import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import sh.calvin.reorderable.*


@Composable
fun DragAndDropExample() {
    var list by remember { mutableStateOf(List(100) { "Item $it" }) }
    val lazyListState = rememberLazyListState()
    val reorderableLazyListState = rememberReorderableLazyListState(lazyListState) { from, to ->
        list = list.toMutableList().apply {
            add(to.index, removeAt(from.index))
        }
    }
    Column {
        Text(text = "Test top")
        LazyColumn(
            state = lazyListState,
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(list, key = { it }) { item ->
                ReorderableItem(reorderableLazyListState, key = item) { isDragging ->
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
                            Text(text = item)
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
        Text(text = "Test bottom", modifier = Modifier.border(1.dp, Color.Red))
    }
}