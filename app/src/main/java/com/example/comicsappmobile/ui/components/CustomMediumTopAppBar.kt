package com.example.comicsappmobile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomMediumTopAppBar(
    modifier: Modifier = Modifier,
    collapsedHeight: Dp = 0.dp,
    expandedHeight: Dp = 128.dp,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    title: @Composable () -> Unit
) {
    Surface(
        modifier = modifier,
        tonalElevation = 3.dp,
        color = MaterialTheme.colorScheme.background
    ) {
        // Container for scrolling behavior and dynamic height
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(expandedHeight) // Adjust based on expandedHeight
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(collapsedHeight) // Collapsed height
            ) {
                // Optionally place content here for collapsed state
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                title() // Custom title content (LazyVerticalGrid in your case)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomAppBarUsage(
    basicTextField: TextFieldState = rememberTextFieldState(),
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
) {


    CustomMediumTopAppBar(
        modifier = Modifier,
        collapsedHeight = 0.dp,
        expandedHeight = 128.dp,
        scrollBehavior = scrollBehavior,
        title = {
            LazyVerticalGrid(
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .fillMaxWidth()
            ) {
                item(span = { GridItemSpan(currentLineSpan = 2) }) {
                    BasicTextField(
                        state = basicTextField,
                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        ),
                        cursorBrush = SolidColor(
                            MaterialTheme.colorScheme.onSecondaryContainer
                        ),
                        decorator = { innerTextField ->
                            Row(
                                modifier = Modifier
                                    .size(128.dp, 36.dp)
                                    .clip(MaterialTheme.shapes.large)
                                    .background(MaterialTheme.colorScheme.secondaryContainer),
                                verticalAlignment = Alignment.CenterVertically

                            ) {
                                if (basicTextField.text.isEmpty()) {
                                    Text(
                                        text = "Введите текст",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(
                                                alpha = 0.5f
                                            )
                                        )
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )
                }
                item {
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors().copy(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        ),
                        modifier = Modifier.size(128.dp, 36.dp)
                    ) {
                        Text(text = "Сортировка")
                    }
                }
                item {
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors().copy(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        ),
                        modifier = Modifier.size(128.dp, 36.dp)
                    ) {
                        Text(text = "Фильтр")
                    }
                }
            }
        }
    )
}
