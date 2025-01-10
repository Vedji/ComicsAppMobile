package com.example.comicsapp.ui.view.screens.aboutbook.components

import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.comicsapp.BuildConfig
import com.example.comicsapp.ui.view.components.LoadImageWithCacheCheck
import com.example.comicsapp.ui.view.components.text.ThemedText
import com.example.comicsapp.ui.view.components.text.ThemedTextStyle
import com.example.comicsapp.ui.viewmodel.AppViewModel


@Composable
fun TableBookComments(navController: NavHostController, viewModel: AppViewModel, parentScrollState: ScrollState){
    val comments by viewModel.selectedBook.bookCommentsList.collectAsState()
    val usersWhoAddComments by viewModel.selectedBook.bookCommentsAboutUsersList.collectAsState()
    val currentUser by viewModel.currentUser.authorizedUser.collectAsState()
    val isVisibleLastElement = remember { mutableStateOf(false) }
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    LaunchedEffect(isVisibleLastElement.value) {
        if (!isVisibleLastElement.value)
            viewModel.selectedBook.loadNewComments()
        Log.d("AboutBookComments", "isVisibleLastElement = ${isVisibleLastElement.value}")
    }


    val textState = remember { mutableStateOf("") }

    TextField(
        value = textState.value,
        onValueChange = { textState.value = it },
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        placeholder = { Text("Введите текст...") },
        maxLines = 10, // Максимум строк (можно убрать, чтобы не ограничивать)
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Default
        ),
        label = { Text("Многострочный редактор") }
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ){
        Text(text = "AboutBookComments", modifier = Modifier)
        // Comments Column

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            for (item in comments){
                if (item.userID != (currentUser?.userID ?: -1))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .onGloballyPositioned { layoutCoordinates ->
                                if (item.commentID == comments.last().commentID) {
                                    val positionInParent = layoutCoordinates.positionInParent()
                                    val parentBounds =
                                        layoutCoordinates.parentLayoutCoordinates?.boundsInParent()

                                    val isVisible = positionInParent.y.toInt() - parentScrollState.value.toInt() < screenHeight.value.toInt()
                                    isVisibleLastElement.value = isVisible
                                }
                            }
                    ) {
                        Row (
                            modifier = Modifier
                                .padding(PaddingValues(start = 12.dp, bottom = 8.dp, end = 24.dp))
                        ){
                            if (!usersWhoAddComments.containsKey(item.commentID))
                                Text(item.commentID.toString())
                            else{
                                val imageURL = remember { "${BuildConfig.API_BASE_URL}/api/v1/file/${usersWhoAddComments[item.commentID]?.userTitleImage ?: -1}/get" }
                                LoadImageWithCacheCheck(
                                    imageURL,
                                    viewModel,
                                    modifier = Modifier
                                        .size(48.dp) // Укажите размер круга
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                                Column(modifier = Modifier.padding(start = 8.dp)) {
                                    ThemedText(
                                        text = usersWhoAddComments[item.commentID]?.username ?: " -- Error --",
                                        modifier = Modifier
                                            .wrapContentSize(),
                                        style = ThemedTextStyle.primary().copy(
                                            padding = PaddingValues(start = 0.dp, bottom = 0.dp, end = 0.dp)
                                        )
                                    )
                                    Row (modifier = Modifier.fillMaxWidth()) {
                                        Text(
                                            modifier = Modifier
                                                .wrapContentSize(),
                                            textAlign = TextAlign.Justify,
                                            text = "${item.rating}",
                                            style = MaterialTheme.typography.bodyLarge.copy(
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        )
                                        Icon(
                                            imageVector =  Icons.Filled.Star,
                                            contentDescription = "Star",
                                            tint = Color(0xFFFFD700), // Цвет звезды
                                            modifier = Modifier
                                                .size(18.dp) // Размер звезды
                                        )
                                    }
                                }

                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp).padding(start = 24.dp, end = 24.dp))

                        if (item.comment.isNotEmpty()){
                            ThemedText(
                                text = item.comment,
                                modifier = Modifier
                                    .wrapContentSize(),
                                style = ThemedTextStyle.secondary().copy(
                                    padding = PaddingValues(start = 24.dp, bottom = 24.dp, end = 24.dp)
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp).padding(start = 24.dp, end = 24.dp))
                        HorizontalDivider(
                            modifier = Modifier.fillMaxWidth(),
                            thickness = 1.dp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(modifier = Modifier.height(24.dp).padding(start = 24.dp, end = 24.dp))
                    }

            }
            Spacer(modifier = Modifier
                .height(8.dp)
            )
        }
    }
}