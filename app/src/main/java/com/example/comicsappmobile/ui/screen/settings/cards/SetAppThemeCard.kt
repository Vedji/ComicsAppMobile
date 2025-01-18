package com.example.comicsappmobile.ui.screen.settings.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.comicsappmobile.ui.presentation.viewmodel.SettingsViewModel
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun SetAppThemeCard(
    settingsViewModel: SettingsViewModel
) {
    val themed = settingsViewModel.appThemes

    val coroutineScope = rememberCoroutineScope()
    OutlinedCard(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth(),
        colors = CardDefaults.outlinedCardColors().copy(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.primary,
            disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            disabledContentColor = MaterialTheme.colorScheme.secondary
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            ExposedDropdownMenuBoxExample(
                themed, { newThemeId ->
                    coroutineScope.launch {
                        settingsViewModel.setAppTheme(newThemeId)
                     }
                },
                settingsViewModel = settingsViewModel
            )
        }

    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExposedDropdownMenuBoxExample(
    themedAll: Map<Int, Pair<String, ColorScheme>>,
    onSelectItem: (themeId: Int) -> Unit = {},
    settingsViewModel: SettingsViewModel
) { // TODO: Example for replaced in genres setting in catalog.
    val appThemeID = settingsViewModel.globalState.currentThemeId
    // Список значений для выбора
    val items = listOf("Item 1", "Item 2", "Item 3")
    var selectedItem by remember { mutableIntStateOf(appThemeID) }
    var expanded by remember { mutableStateOf(false) } // Состояние для раскрытия меню

    // Поле с выпадающим меню
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it } // Обработчик изменения состояния меню
    ) {
        // Текстовое поле, связанное с меню
        TextField(
            value = themedAll[selectedItem]?.first ?: "No themed",
            onValueChange = {}, // В данном примере мы не обрабатываем ввод текста
            readOnly = true, // Поле только для чтения, чтобы использовать как выбор из меню
            label = { Text("Тема приложения") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier.menuAnchor(),
            shape = MaterialTheme.shapes.large,
            colors = TextFieldDefaults.colors().copy(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                focusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                unfocusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        )
        // Само раскрывающееся меню
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false } // Закрытие меню при клике вне его
        ) {
            for ((themeId, value) in themedAll){
                DropdownMenuItem(
                    text = { Text(text = value.first) },
                    onClick = {
                        selectedItem = themeId // Установка выбранного элемента
                        expanded = false // Закрытие меню после выбора
                        onSelectItem(themeId)
                    }
                )
            }
        }
    }
}