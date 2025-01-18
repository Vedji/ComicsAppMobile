package com.example.comicsappmobile.ui.screen.book.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.comicsappmobile.ui.components.ImageByID
import com.example.comicsappmobile.utils.Logger
import com.example.comicsappmobile.ui.presentation.viewmodel.BookViewModel
import com.example.comicsappmobile.ui.presentation.viewmodel.UiState
import com.example.comicsappmobile.ui.presentation.model.CommentUiModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentsBookTab(bookViewModel: BookViewModel) {
    val commentsUiState by bookViewModel.commentsUiState.collectAsState()
    val authUser by bookViewModel.globalState.authUser.collectAsState()

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Отслеживание прокрутки
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo }
            .collect { visibleItems ->
                if (visibleItems.isNotEmpty()) {
                    // Проверяем, достигнут ли конец списка
                    val lastVisibleItem = visibleItems.last()
                    val totalItems = listState.layoutInfo.totalItemsCount
                    if (lastVisibleItem.index == totalItems - 1) {
                        coroutineScope.launch {
                            Logger.debug("LaunchedEffect", "lastVisibleItem.index = ${lastVisibleItem.index}")
                            bookViewModel.loadComments()
                        }
                    }
                }
            }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
            state = listState,
            verticalArrangement = Arrangement.spacedBy(36.dp)
        ) {
            if (commentsUiState.data != null && commentsUiState.data is List<CommentUiModel>)
                items(commentsUiState.data!!.count()) {
                    val comment = commentsUiState.data!![it]
                    if (authUser.userId != comment.aboutUser.userId)
                    CommentCard(comment, bookViewModel)
                }
            if (commentsUiState is UiState.Loading)
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterVertically))
                    }
                }
            item {
                Spacer(modifier = Modifier.height(36.dp))
            }
        }
    }
}


@Composable
fun CommentCard(commentUiModel: CommentUiModel, bookViewModel: BookViewModel){
    val currentUser = bookViewModel.sharedViewModel.currentAuthorizingUser.value
    Box{

        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth(),
            colors = CardDefaults.outlinedCardColors().copy(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.tertiary,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContentColor = MaterialTheme.colorScheme.secondary
            )
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ImageByID(
                        commentUiModel.aboutUser.userTitleImage,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(start = 8.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = commentUiModel.aboutUser.username,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            textAlign = TextAlign.Justify,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = Modifier
                                    .wrapContentSize(),
                                textAlign = TextAlign.Justify,
                                text = "${commentUiModel.rating}",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = "Star",
                                tint = Color(0xFFFFD700), // Цвет звезды
                                modifier = Modifier
                                    .size(18.dp) // Размер звезды
                            )
                        }
                    }
                }
                if (commentUiModel.comment.isNotEmpty()) {
                    Text(
                        text = commentUiModel.comment,
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Justify
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
        if (
            bookViewModel.sharedViewModel.isUserHasPermission(
                addedUserId = commentUiModel.aboutUser.userId, hasHelper = true, hasAdmin = true
            )) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .offset(y = (14).dp)
                    .padding(end = 8.dp)
                    .align(Alignment.BottomEnd),
                horizontalArrangement = Arrangement.End
            ) {
                SmallFloatingActionButton(
                    onClick = {
                        // TODO: Delete comment for admin and helper
                    },
                    modifier = Modifier
                        .size(32.dp),
                    containerColor = MaterialTheme.colorScheme.errorContainer
                ) {
                    Icon(
                        Icons.Filled.Delete,
                        contentDescription = "Delete",
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}


