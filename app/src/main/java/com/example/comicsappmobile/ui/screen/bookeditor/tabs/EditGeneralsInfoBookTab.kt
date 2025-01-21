package com.example.comicsappmobile.ui.screen.bookeditor.tabs

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.KeyboardArrowRight
import androidx.compose.material.icons.twotone.Check
import androidx.compose.material.icons.twotone.Clear
import androidx.compose.material.icons.twotone.Refresh
import androidx.compose.material3.AssistChip
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.comicsappmobile.BuildConfig
import com.example.comicsappmobile.ui.components.ThemedInputField
import com.example.comicsappmobile.ui.presentation.model.GenreUiModel
import com.example.comicsappmobile.utils.Logger
import com.example.comicsappmobile.utils.vibrate
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditGeneralsInfoBookTab(
    paddingValues: PaddingValues,
    bookId: Int = -1,

    fetchedBookTitleName: String,
    inputTitleName: MutableState<String>,

    fetchedAllGenres: List<GenreUiModel>,
    fetchedBookGenres: List<GenreUiModel>,
    inputGenres: MutableState<List<GenreUiModel>>,

    fetchedDescription: String,
    inputDescription: MutableState<String>,

    fetchedImageId: Int = -1,
    selectedImage: MutableState<Uri?>,

    fetchedDateOfPublication: String,
    inputDateOfPublication: MutableState<String>,


    setPage: (Int) -> Unit = {}
) {
    // TODO: move to upper


    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        selectedImage.value = uri
        Logger.debug("Select Uri", "uri = '${selectedImage.value}'")
    }

    // For tab scroll
    val localContext = LocalContext.current
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(12.dp)
            .fillMaxWidth()
            .verticalScroll(scrollState)
    ) {
        // Edit book title name
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Названия",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
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
        }
        Spacer(modifier = Modifier.height(6.dp))
        ThemedInputField(
            textFieldValue = inputTitleName,
            placeholder = "Введите название книги",
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

        // Edit book genres
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Жанры",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            IconButton(
                onClick = { inputGenres.value = fetchedBookGenres }
            ) {
                Icon(
                    if (
                        fetchedBookGenres.all { it.genreId in inputGenres.value.map { y -> y.genreId } } &&
                        fetchedBookGenres.size == inputGenres.value.size &&
                        bookId > 0) Icons.TwoTone.Check else Icons.TwoTone.Refresh,
                    "Has been resource updated?"
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        // TODO: Replace to ???
        var expanded by remember { mutableStateOf(false) }
        val textFieldState = remember { mutableStateOf("") }
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            ThemedInputField(
                textFieldValue = textFieldState,
                onValueChange = { expanded = it.isNotEmpty() },
                placeholder = "Введите название жанра",
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign = TextAlign.Start
                ),
                placeholderStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                    textAlign = TextAlign.Start
                ),
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                cursorColor = MaterialTheme.colorScheme.onSecondaryContainer,
                rightIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryEditable, true)
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                var flagExposedDropdownMenu: Boolean = false
                for (selectionOption in fetchedAllGenres) {
                    if (textFieldState.value.lowercase() !in selectionOption.genreName.lowercase() && textFieldState.value.isNotEmpty())
                        continue
                    if (selectionOption.genreId in inputGenres.value.map { it.genreId })
                        continue
                    flagExposedDropdownMenu = true
                    DropdownMenuItem(
                        text = { Text(selectionOption.genreName) },
                        onClick = {
                            textFieldState.value = ""
                            expanded = false
                            if (inputGenres.value.lastIndexOf(selectionOption) < 0)
                                inputGenres.value += selectionOption
                        }
                    )
                }
                if (!flagExposedDropdownMenu) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                text =
                                if (textFieldState.value.length <= 0) "Жанров больше нет((("
                                else "Жанра не найдено"
                            )
                        },
                        onClick = {}
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        inputGenres.value.forEach {
            AssistChip(
                onClick = {
                    coroutineScope.launch {
                        val a = inputGenres.value.lastIndexOf(it)
                        val arr = inputGenres.value.toMutableList()
                        vibrate(localContext.applicationContext, 100)
                        delay(400)
                        if (a >= 0) arr.remove(it)
                        inputGenres.value = arr.toList()
                        Logger.debug(
                            "inputGenres.value",
                            "inputGenres.value = ${inputGenres.value}"
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = it.genreName) },
                trailingIcon = { Icon(Icons.TwoTone.Clear, "Remove genre from list") }
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        // Text(
        //     text = "Fish text for edit book genres",
        //     style = MaterialTheme.typography.bodyLarge.copy(
        //         color = MaterialTheme.colorScheme.secondary,
        //         textAlign = TextAlign.Justify ),)
        // Edit book description
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Описание",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            IconButton(
                onClick = { inputDescription.value = fetchedDescription }
            ) {
                Icon(
                    if (inputDescription.value == fetchedDescription && bookId > 0) Icons.TwoTone.Check else Icons.TwoTone.Refresh,
                    "Has been resource updated?"
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        ThemedInputField(
            textFieldValue = inputDescription,
            placeholder = "Введите описание книги",
            modifier = Modifier.heightIn(min = 128.dp),
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Start
            ),
            placeholderStyle = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                textAlign = TextAlign.Start
            ),
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            cursorColor = MaterialTheme.colorScheme.onSecondaryContainer,
            oneLine = false
        )
        Spacer(modifier = Modifier.height(24.dp))
        // Edit book date of publication
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Дата публикации",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            IconButton(
                onClick = { inputDateOfPublication.value = fetchedDateOfPublication }
            ) {
                Icon(
                    if (inputDateOfPublication.value == fetchedDateOfPublication && bookId > 0) Icons.TwoTone.Check else Icons.TwoTone.Refresh,
                    "Has been resource updated?"
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        ThemedInputField(
            textFieldValue = inputDateOfPublication,
            placeholder = "Введите дату публикацию",
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
        // Edit book title image
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Превью изображение",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            IconButton(
                onClick = { selectedImage.value = null }
            ) {
                Icon(
                    if (selectedImage.value == null && bookId > 0) Icons.TwoTone.Check else Icons.TwoTone.Refresh,
                    "Has been resource updated?"
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .width(400.dp)
                .aspectRatio(0.6f)
                .clip(MaterialTheme.shapes.large)
                .clickable { imagePickerLauncher.launch("image/*") }
                .background(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = MaterialTheme.shapes.large
                )
                .border(
                    1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = MaterialTheme.shapes.large
                )
                .padding(6.dp)


        ) {
            Text(
                text = "Выберите изображение",
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign = TextAlign.Start
                ),
            )
            AsyncImage(
                model =
                if (selectedImage.value != null)
                    selectedImage.value
                else "${BuildConfig.API_BASE_URL}/api/v1/file/${fetchedImageId}/get",
                contentDescription = "Выбранное изображение",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(MaterialTheme.shapes.large),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(24.dp))

        AssistChip(
            modifier = Modifier.fillMaxWidth(),
            onClick = { setPage(1) },
            label = { Text(text = "Изменить порядок глав в книге") },
            trailingIcon = {
                Icon(
                    imageVector = Icons.AutoMirrored.TwoTone.KeyboardArrowRight,
                    contentDescription = "Перейти на страницу изменения порядка глав"
                )
            })
        Spacer(modifier = Modifier.height(24.dp))
    }
}