package com.example.comicsappmobile.ui.screen.profiles.cards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.comicsappmobile.ui.components.ImageByID
import com.example.comicsappmobile.ui.components.ThemedErrorCard
import com.example.comicsappmobile.ui.presentation.viewmodel.ProfileViewModel
import com.example.comicsappmobile.ui.presentation.viewmodel.UiState

@Composable
fun ProfileDescriptionCard(
    navController: NavHostController,
    profileViewModel: ProfileViewModel
) {

    val userLogin by profileViewModel.userLogin.collectAsState()
    val expandedDescription = remember { mutableStateOf(false) }
    var descriptionLineCount by remember { mutableIntStateOf(0) }

    when (userLogin) {
        is UiState.Error -> {
            ThemedErrorCard(
                message = userLogin.message ?: "No message in ProfileDescriptionCard (is UiState.Error)",
                errorType = userLogin.typeError ?: "No error type in ProfileDescriptionCard (is UiState.Error)",
                statusCode = userLogin.statusCode
                )
        }
        is UiState.Loading -> { CircularProgressIndicator() }
        is UiState.Success -> {
            val userName = userLogin.data?.username ?: "Error"
            val descriptionUserProfile = userLogin.data?.userDescription ?: ""
            val userTitleImage = userLogin.data?.userTitleImage ?: -1


            OutlinedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
                    .padding(horizontal = 12.dp),
                colors = CardDefaults.outlinedCardColors().copy(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(12.dp)
                ) {
                    ImageByID(
                        imageId = userTitleImage,
                        modifier = Modifier.size(64.dp).clip(CircleShape),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.width(24.dp))
                    Column {
                        Text(
                            text = userName,
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                textAlign = TextAlign.Justify
                            )
                        )
                    }
                }
                Column(
                    modifier = Modifier.fillMaxWidth().padding(12.dp)
                ) {

                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        maxLines =
                        if (expandedDescription.value) Int.MAX_VALUE
                        else 3,
                        overflow = TextOverflow.Ellipsis,
                        text = descriptionUserProfile,
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            textAlign =
                            if (expandedDescription.value) TextAlign.Justify
                            else TextAlign.Unspecified,
                            // lineHeight = MaterialTheme.typography.titleMedium.fontSize,
                            fontWeight = FontWeight.W200
                        ),
                        onTextLayout = { textLayoutResult: TextLayoutResult ->
                            descriptionLineCount =
                                textLayoutResult.lineCount // Получаем количество строк
                        }
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (descriptionLineCount >= 3) {
                            TextButton(
                                onClick = {
                                    expandedDescription.value = !expandedDescription.value
                                }) {
                                Text(
                                    text = if (expandedDescription.value) "Раскрыть" else "Скрыть"
                                )
                            }
                        }
                        SmallFloatingActionButton(
                            onClick = {
                                // TODO: Edit user profile information
                            },
                            modifier = Modifier
                                .size(32.dp),
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        ) {
                            Icon(
                                Icons.Filled.Edit,
                                contentDescription = "Edit user profile information",
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }


            }
        }
    }
    // val userName = "Искатель историй: отзывы на книги со смыслом"
    // val descriptionUserProfile =
    //     "Привет! Я страстный книгоман, который не представляет своей жизни без новых историй и запаха свежих страниц. Люблю читать, анализировать и делиться своими мыслями о книгах. В моем профиле вы найдете честные, увлекательные и подробные отзывы, которые помогут вам выбрать свою следующую литературную любовь.\n" +
    //             "\n" +
    //             "\uD83C\uDFA8 Жанры, которые читаю: от магического реализма до научной фантастики, от философских эссе до триллеров, заставляющих забыть про сон. Всегда открыт к экспериментам, поэтому здесь можно встретить самые неожиданные книги.\n" +
    //             "\n" +
    //             "✍\uFE0F Что ищу в книгах: глубокие сюжеты, запоминающихся персонажей и искры вдохновения, которые остаются в душе. Особенно ценю, когда книга учит смотреть на мир иначе.\n" +
    //             "\n" +
    //             "\uD83C\uDF0D Читаю для души и для роста. Моя цель — не только наслаждаться процессом, но и находить идеи, которые делают жизнь интереснее.\n" +
    //             "\n" +
    //             "Заглядывайте, оставляйте комментарии и делитесь своими впечатлениями! Вместе сделаем чтение еще увлекательнее! \uD83D\uDE0A"
    //
}