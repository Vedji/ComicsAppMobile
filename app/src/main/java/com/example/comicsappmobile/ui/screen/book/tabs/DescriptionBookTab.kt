package com.example.comicsappmobile.ui.screen.book.tabs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.comicsappmobile.ui.components.ThemedStateView
import com.example.comicsappmobile.ui.presentation.viewmodel.BookViewModel
import com.example.comicsappmobile.ui.presentation.viewmodel.UiState
import com.example.comicsappmobile.utils.Logger

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DescriptionBookTab(bookViewModel: BookViewModel) {
    val bookAboutUi by bookViewModel.bookUiState.collectAsState()
    val genreUiState by bookViewModel.genreUiState.collectAsState()

    Column(
        modifier = Modifier.padding(start = 24.dp, end = 24.dp, )
    ) {

        Text(
            text = "Название",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            )
        )
        ThemedStateView(
            uiState = bookAboutUi,
            {
                if (bookAboutUi is UiState.Success){
                    Text(
                        text = bookAboutUi.data?.rusTitle ?: "Названия нет",
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Justify
                    )
                }
            }
        )
        Spacer(modifier = Modifier.padding(top = 24.dp))
        Text(
            text = "Жанры",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            )
        )
        ThemedStateView(
            uiState = genreUiState,
            onSuccess = {
                if (genreUiState is UiState.Success){
                    val genres = genreUiState.data ?: emptyList()
                    Logger.debug("ThemedStateView", genres.toString())
                    val genresText =
                        if (genres.isNotEmpty())
                        genres.joinToString(separator = ", ") { it.genreName }
                    else
                        "Жанров нет"
                    Text(
                        text = genresText,
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Justify
                    )
                }
            },
            /*
            onError = {
                val openDialog = remember { mutableStateOf(true) }
                BasicAlertDialog(
                    onDismissRequest = {
                        // Dismiss the dialog when the user clicks outside the dialog or on the back
                        // button. If you want to disable that functionality, simply use an empty
                        // onDismissRequest.
                        openDialog.value = false
                    }
                ) {
                    Surface(
                        modifier = Modifier.wrapContentWidth().wrapContentHeight(),
                        shape = MaterialTheme.shapes.large,
                        tonalElevation = AlertDialogDefaults.TonalElevation
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = genreUiState.statusCode.toString(),
                            )
                            Text(
                                text = genreUiState.typeError.toString(),
                            )
                            Text(
                                text = genreUiState.message.toString(),
                                modifier = Modifier.wrapContentSize()
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            TextButton(
                                onClick = { openDialog.value = false }
                            ) {
                                Text("Confirm")
                            }
                        }
                    }
                }
            }

             */
        )
        // Description about book
        Spacer(modifier = Modifier.padding(top = 24.dp))
        Text(
            text = "О произведении",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            )
        )
        ThemedStateView(
            uiState = bookAboutUi,
            {
                if (bookAboutUi is UiState.Success){
                    Text(
                        text = bookAboutUi.data?.bookDescription ?: "Описания нет",
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Justify
                    )
                }
            }
        )
        // Date of publication a book
        Spacer(modifier = Modifier.padding(top = 24.dp))
        Text(
            text = "Дата публикации",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            )
        )
        ThemedStateView(
            uiState = bookAboutUi,
            {
                if (bookAboutUi is UiState.Success){
                    Text(
                        text = bookAboutUi.data?.bookDatePublication ?: "Дата публикации отсутствует",
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Start
                    )
                }
            }
        )
        // Date of publication a book
        Spacer(modifier = Modifier.padding(top = 24.dp))
        // Text(
        //     text = "ISBN",
        //     color = MaterialTheme.colorScheme.primary,
        //     style = MaterialTheme.typography.titleLarge.copy(
        //         fontWeight = FontWeight.Bold
        //     )
        // )
        // ThemedStateView(
        //     uiState = bookAboutUi,
        //     {
        //         if (bookAboutUi is UiState.Success){
        //             Text(
        //                 text = bookAboutUi.data?.bookISBN ?: "ISBN отсутствует",
        //                 color = MaterialTheme.colorScheme.secondary,
        //                 style = MaterialTheme.typography.bodyLarge,
        //                 textAlign = TextAlign.Start
        //             )
        //         }
        //     }
        // )

    }
}