package com.example.comicsappmobile.ui.screen.book.tabs

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.comicsappmobile.ui.components.ThemedErrorCard
import com.example.comicsappmobile.utils.Logger
import com.example.comicsappmobile.ui.presentation.viewmodel.BookViewModel
import com.example.comicsappmobile.ui.presentation.viewmodel.UiState
import com.example.comicsappmobile.ui.presentation.model.CommentUiModel
import com.example.comicsappmobile.ui.screen.book.cards.OthersCommentCard
import com.example.comicsappmobile.ui.screen.book.cards.SelfCommentCard
import kotlinx.coroutines.launch


@Composable
fun CommentsBookTab(bookViewModel: BookViewModel) {
    val commentsUiState by bookViewModel.commentsUiState.collectAsState()
    val authUser by bookViewModel.globalState.authUser.collectAsState()
    val userComment by bookViewModel.userComment.collectAsState()

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 24.dp, end = 24.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            state = listState,
            verticalArrangement = Arrangement.spacedBy(36.dp)
        ) {
            item {
                SelfCommentCard(bookViewModel)

            }
            var userComments: Boolean = false
            if (commentsUiState.data != null && commentsUiState.data is List<CommentUiModel>) {
                if (commentsUiState.data!!.isNotEmpty()) {
                    items(commentsUiState.data!!.count()) {
                        val comment = commentsUiState.data!![it]
                        if (authUser.userId != comment.aboutUser.userId)
                            OthersCommentCard(comment, bookViewModel)
                        else userComments = true
                    }
                } else {
                    if (userComment is UiState.Success && (userComment.data?.commentId ?: 0) > 0){
                        item {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = "Других комментариев нет",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    else{
                        item {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = "Комментариев нет",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
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




