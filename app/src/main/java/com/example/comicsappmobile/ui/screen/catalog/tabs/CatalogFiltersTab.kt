package com.example.comicsappmobile.ui.screen.catalog.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.comicsappmobile.ui.components.ThemedInputField
import com.example.comicsappmobile.utils.Logger
import com.example.comicsappmobile.ui.presentation.viewmodel.CatalogViewModel
import com.example.comicsappmobile.ui.presentation.viewmodel.UiState
import com.example.comicsappmobile.ui.presentation.model.GenreUiModel
import kotlinx.coroutines.flow.MutableStateFlow


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogFiltersTab(
    navController: NavHostController,
    catalogViewModel: CatalogViewModel,
    innerPadding: PaddingValues = PaddingValues(0.dp),
    comeBack: () -> Unit = { }
) {
    val sel = catalogViewModel.selectedSorted.collectAsState()
    val inputSorted = remember { MutableStateFlow(sel.value) }
    val inputGenres = remember { MutableStateFlow(emptyList<Int>()) }
    val inputGenresFromFilter = remember { MutableStateFlow(mutableListOf<Int>()) }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(bottom = 64.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .padding(start = 24.dp, end = 24.dp, top = 12.dp, bottom = 12.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { comeBack() }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Localized Description",
                        modifier = Modifier
                            .size(24.dp),
                        tint = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
                Text(
                    text = "Меню поиска",
                    modifier = Modifier,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState())

            ) {
                Text(
                    text = "Сортировать по",
                    modifier = Modifier
                        .padding(vertical = 24.dp),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Medium
                    )
                )
                val sortedVariablesList = remember { catalogViewModel.getSortedAccessValues() }
                var expandedSortInput by remember { mutableStateOf(false) }
                val textFieldStateSortInput = remember { mutableStateOf(gettingSortNameForView(inputSorted.value)) }
                ExposedDropdownMenuBox(
                    expanded = expandedSortInput,
                    onExpandedChange = { expandedSortInput = it }
                ) {
                    ThemedInputField(
                        textFieldValue = textFieldStateSortInput,
                        onValueChange = {  },
                        placeholder = "Введите тип сортировки",
                        textStyle = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.secondary,
                            textAlign = TextAlign.Start
                        ),
                        placeholderStyle = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                            textAlign = TextAlign.Start
                        ),
                        enabled = false,
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        cursorColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        rightIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSortInput)
                        },
                        modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryEditable, true)
                    )
                    ExposedDropdownMenu(
                        expanded = expandedSortInput,
                        onDismissRequest = { expandedSortInput = false }
                    ) {
                        for (item in sortedVariablesList) {
                            DropdownMenuItem(
                                text = { Text(gettingSortNameForView(item)) },
                                onClick = {
                                    textFieldStateSortInput.value = gettingSortNameForView(item)
                                    inputSorted.value = item
                                    expandedSortInput = false
                                }
                            )
                        }
                    }
                }
                // RadioGroupSample(catalogViewModel, onItemSelect = { inputSorted.value = it })
                Text(
                    text = "Жанры",
                    modifier = Modifier.padding(vertical = 24.dp),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Medium
                    )
                )
                FilterByGenres(catalogViewModel, inputGenres = inputGenresFromFilter)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .align(Alignment.BottomStart),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    catalogViewModel.removeParameters()
                    comeBack()
                          },
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                ),
                modifier = Modifier.size(136.dp, 36.dp)
            ) {
                Text(
                    text = "Сбросить",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Button(
                onClick = {
                    inputGenres.value = inputGenresFromFilter.value
                    Logger.debug("RadioGroupSample", "test = ${inputGenres.value}")
                    catalogViewModel.setParameters(inputSorted.value, inputGenres.value)
                    comeBack()
                },
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                modifier = Modifier.size(136.dp, 36.dp)
            ) {
                Text(
                    text = "Применить",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

        }
    }
}

fun gettingSortNameForView(item: String): String{
    return when(item){
        "addedASC" -> { "возрастанию даты добавления" }
        "addedDESC" -> { "убыванию даты добавления" }
        "ratingASC" -> { "возрастанию рейтинга" }
        "ratingDESC" -> { "убыванию рейтинга" }
        "titleASC" -> { "возрастанию названия" }
        "titleDESC" -> { "убыванию названия" }
        "publishedASC" -> { "возрастанию даты публикации" }
        "publishedDESC" -> { "убыванию даты публикации" }
        else -> { item }
    }
}


@Composable
fun RadioGroupSample(catalogViewModel: CatalogViewModel, onItemSelect: (text: String) -> Unit = {}) {
    val radioOptions = remember { catalogViewModel.getSortedAccessValues() }
    val sel = catalogViewModel.selectedSorted.collectAsState()
    Logger.debug("RadioGroupSample", "test = ${sel.value}")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(sel.value) }
    // Note that Modifier.selectableGroup() is essential to ensure correct accessibility behavior
    Column(Modifier.selectableGroup()) {
        radioOptions.forEach { text ->
            Row(
                Modifier.fillMaxWidth()
                    .height(56.dp)
                    .selectable(
                        selected = (text == selectedOption),
                        onClick = {
                            onItemSelect(text)
                            onOptionSelected(text)
                                  },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (text == selectedOption),
                    onClick = null // null recommended for accessibility with screenreaders
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}



@Composable
fun FilterByGenres(
    catalogViewModel: CatalogViewModel,
    inputGenres: MutableStateFlow<MutableList<Int>> = remember { MutableStateFlow(mutableListOf<Int>()) }
){
    val allGenresUi by catalogViewModel.allGenresUi.collectAsState()
    val selectedGenres by catalogViewModel.selectedGenresIds.collectAsState()
    if (allGenresUi is UiState.Success) {
        val genres = allGenresUi.data as List<GenreUiModel> ?: emptyList()
        Column (
            modifier = Modifier
                .padding()
                .fillMaxWidth()
        ) {
            repeat(genres.size){ it ->
                if (genres[it].genreId !in inputGenres.value && genres[it].genreId in selectedGenres){
                    inputGenres.value.add(genres[it].genreId)
                }
                FilterChipSample(
                    text = genres[it].genreName,
                    initSelected = genres[it].genreId in selectedGenres,
                    value = genres[it].genreId,
                    modifier = Modifier
                        .fillMaxWidth(),
                    onItemChange = {
                        gId, check ->
                        if (check && gId !in inputGenres.value)
                            inputGenres.value.add(gId)
                        else if (!check && gId in inputGenres.value)
                            inputGenres.value.remove(gId)
                    }
                )
            }
            Spacer(modifier = Modifier.padding(end = 48.dp))
        }
    }
}


@Composable
fun FilterChipSample(
    text: String = "Filter chip",
    initSelected: Boolean = false,
    value: Int = -1,
    modifier: Modifier = Modifier.width(156.dp),
    onItemChange: (text: Int, selected: Boolean) -> Unit
) {
    var selected by rememberSaveable(initSelected) { mutableStateOf(initSelected) }
    FilterChip(
        modifier = modifier,
        selected = selected,
        onClick = {
            selected = !selected
            onItemChange(value, selected)
            // Logger.debug("FilterChipSample", "modifier = ${modifier. .toString()}")
                  },
        label = {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color =
                    if (selected) MaterialTheme.colorScheme.onTertiaryContainer
                    else MaterialTheme.colorScheme.onSurface
                ),
                textAlign = TextAlign.Start,
                overflow = TextOverflow.Ellipsis
            )
                },
        leadingIcon =
        if (selected) {
            {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = "Localized Description",
                    modifier = Modifier.size(FilterChipDefaults.IconSize),
                    tint = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        } else {
            null
        }
    )
}