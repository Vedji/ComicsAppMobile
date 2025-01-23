package com.example.comicsappmobile.ui.screen.bookeditor

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.comicsappmobile.navigation.Screen
import com.example.comicsappmobile.ui.components.ThemedAlertDialog
import com.example.comicsappmobile.ui.presentation.model.ChapterUiModel
import com.example.comicsappmobile.ui.presentation.model.GenreUiModel
import com.example.comicsappmobile.ui.presentation.viewmodel.BookEditorViewModel
import com.example.comicsappmobile.ui.presentation.viewmodel.UiState
import com.example.comicsappmobile.ui.screen.bookeditor.tabs.EditChaptersSequenceTab
import com.example.comicsappmobile.ui.screen.bookeditor.tabs.EditGeneralsInfoBookTab
import com.example.comicsappmobile.utils.Logger
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun BookEditorScreen(
    bookId: Int = -1,
    navController: NavHostController,
    bookEditorViewModel: BookEditorViewModel = koinViewModel { parametersOf(bookId) }
) {
    // TODO: Editable ->
    //  + title name
    //  + Genres
    //  + titleImage
    //  + Description
    //  + Date of publication
    //  + Go to edit chapter sequence tab
    //      + Edit sequence of chapters

    //  - On other screen
    //    - Edit chapter name
    //    - Add or delete chapters
    //    - Go to edit chapter context
    //       - Edit pages sequence
    //       - Add or delete pages
    val selectedTab = remember { mutableIntStateOf(0) }
    val context = LocalContext.current

    // Fetched values
    val bookUiState by bookEditorViewModel.bookUiState.collectAsState()
    val allGenresUiState by bookEditorViewModel.allGenresUiState.collectAsState()
    val bookGenresUiState by bookEditorViewModel.bookGenresUiState.collectAsState()
    val bookChaptersUiState by bookEditorViewModel.bookChaptersUiState.collectAsState()
    val update by bookEditorViewModel.uploading.collectAsState()

    // From view model
    val bookTitleName: String = if (bookUiState is UiState.Success) bookUiState.data?.rusTitle
        ?: "Название отсутствует" else ""
    val fetchedAllGenres: List<GenreUiModel> =
        if (allGenresUiState is UiState.Success) allGenresUiState.data
            ?: emptyList() else emptyList()
    val fetchedBookGenres: List<GenreUiModel> =
        if (bookGenresUiState is UiState.Success) bookGenresUiState.data
            ?: emptyList() else emptyList()
    val baseDescription: String =
        if (bookUiState is UiState.Success) bookUiState.data?.bookDescription
            ?: "Отсутствует описание" else ""
    val baseImageId: Int =
        if (bookUiState is UiState.Success) bookUiState.data?.bookTitleImageId ?: -1 else -1
    val baseDateOfPublication: String =
        if (bookUiState is UiState.Success) bookUiState.data?.bookDatePublication ?: "" else ""
    val fetchedChapters: List<ChapterUiModel> =
        if (bookChaptersUiState is UiState.Success) bookChaptersUiState.data
            ?: emptyList() else emptyList()

    // User input data
    // Input generals
    val inputTitleName: MutableState<String> = remember { mutableStateOf(bookTitleName) }
    val inputGenres: MutableState<List<GenreUiModel>> =
        remember { mutableStateOf<List<GenreUiModel>>(fetchedBookGenres) }
    val inputDescription: MutableState<String> = remember { mutableStateOf(baseDescription) }
    val selectedImage: MutableState<Uri?> = remember { mutableStateOf<Uri?>(null) }
    val inputDateOfPublication: MutableState<String> =
        remember { mutableStateOf(baseDateOfPublication) }
    // Input chapters sequence tab:
    val chapters = remember { mutableStateOf(fetchedChapters) }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(bookUiState) {
        inputTitleName.value = bookTitleName
        inputDescription.value = baseDescription
        inputDateOfPublication.value = baseDateOfPublication
        // inputGenres
        // chapters
    }
    LaunchedEffect(bookGenresUiState) {
        inputGenres.value = fetchedBookGenres
    }
    LaunchedEffect(bookChaptersUiState) {
        chapters.value = fetchedChapters
    }
    val resetButtonClicked = remember { mutableStateOf(false) }
    val approveButtonClicked = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Row (
                modifier = Modifier.height(64.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                IconButton(onClick = {
                    when (selectedTab.intValue) {
                        1 -> {
                            selectedTab.intValue = 0
                        }   // Navigate to general book editable
                        else -> {
                            if (bookEditorViewModel.getBookId() > 0){
                                navController.navigate(Screen.AboutBook.createRoute(bookEditorViewModel.getBookId().toString()))
                            }else{
                                navController.navigate(Screen.Catalog.route)
                            }
                        }
                    }
                }) {
                    Icon(
                        imageVector = when (selectedTab.intValue) {
                            0 -> Icons.AutoMirrored.Filled.KeyboardArrowLeft
                            1 -> Icons.AutoMirrored.Filled.KeyboardArrowLeft
                            else -> Icons.Default.Home
                        },
                        contentDescription = "Moved"
                    )
                }
                    Text(
                        text = when(selectedTab.intValue){
                            0 -> { "Основная информация" }
                            1 -> { "Изменение порядка глав" }
                            else -> { "Неизвестная страница" }
                        },
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
            }
        },
        bottomBar = {
            if (selectedTab.intValue == 0){
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AssistChip(
                        onClick = {
                            resetButtonClicked.value = true
                        },
                        label = {
                            Text(
                                text = "Сбросить",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = MaterialTheme.colorScheme.onErrorContainer,
                                    textAlign = TextAlign.Justify
                                )
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors().copy(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    )
                    AssistChip(
                        onClick = {
                            // if (selectedImage.value != null)
                            // bookEditorViewModel.uploadFile(context, selectedImage.value!!)
                            approveButtonClicked.value = true
                        },
                        label = {
                            Text(
                                text = "Применить",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    textAlign = TextAlign.Justify
                                )
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors().copy(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        if (resetButtonClicked.value){
            ThemedAlertDialog(
                titleText = "Сбросить настроенные параметры?",
                messageText = "",
                onConfirm = {
                    resetButtonClicked.value = false
                    coroutineScope.launch { bookEditorViewModel.refreshBook() }
                            },
                onDismiss = { resetButtonClicked.value = false }
            )
        }
        if (approveButtonClicked.value){
            ThemedAlertDialog(
                titleText = "Применить изменения?",
                messageText = "",
                onConfirm = {
                    approveButtonClicked.value = false
                    coroutineScope.launch {
                        val response = bookEditorViewModel.loadBookAndImage(
                            context = context,
                            fileUri = selectedImage.value,
                            bookName = inputTitleName.value,
                            bookGenres = inputGenres.value.map { it.genreId },
                            bookDescription = inputDescription.value,
                            bookDateOfPublication = inputDateOfPublication.value,
                            bookChaptersSequence = chapters.value.map { it.chapterId }
                        )
                        Logger.debug("In screen edit", "book id = ${response.data?.bookId}")
                        bookEditorViewModel.refreshBook()
                        navController.navigate(Screen.AboutBook.createRoute(bookEditorViewModel.getBookId().toString()))
                    }

                },
                onDismiss = { approveButtonClicked.value = false }
            )
        }



        if (update is UiState.Success){
            when (selectedTab.intValue) {
                0 -> {
                    EditGeneralsInfoBookTab(
                        paddingValues = paddingValues,
                        bookId = bookId,
                        fetchedBookTitleName = bookTitleName,
                        inputTitleName = inputTitleName,
                        fetchedAllGenres = fetchedAllGenres,
                        fetchedBookGenres = fetchedBookGenres,
                        inputGenres = inputGenres,
                        fetchedDescription = baseDescription,
                        inputDescription = inputDescription,
                        fetchedImageId = baseImageId,
                        selectedImage = selectedImage,
                        fetchedDateOfPublication = baseDateOfPublication,
                        inputDateOfPublication = inputDateOfPublication,
                        setPage = { selectedTab.intValue = it }
                    )
                }

                1 -> {
                    EditChaptersSequenceTab(
                        paddingValues = paddingValues,
                        fetchedChapters = fetchedChapters,
                        chapters = chapters,
                        setPage = { selectedTab.intValue = it }
                    )
                }

                else -> {
                    Text(text = "No tab selected")
                }
            }
        }else{
            Box(
                modifier = Modifier.padding(paddingValues).fillMaxSize()
            ){
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }

    }
}


// @Preview
// @Composable
// fun PreviewBookEditorScreen(){
//     ComicsAppMobileTheme(3) {
//         BookEditorScreen()
//     }
// }