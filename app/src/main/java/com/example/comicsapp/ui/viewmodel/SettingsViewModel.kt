package com.example.comicsapp.ui.viewmodel

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.comicsapp.dataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope


class SettingsViewModel(private val state: SavedStateHandle, private val context: Context) : ViewModel()  {
    private val _isDarkTheme = MutableStateFlow( 0 )
    val isDarkTheme: StateFlow<Int> = _isDarkTheme

    companion object {
        private const val KEY_DATA = "app_theme"

        val appThemes = mapOf(
            -1 to "System Theme",
            0 to "LightColorScheme",
            1 to "DarkColorScheme",
            2 to "TestLightColorScheme"
        )

    }
    private val dataStoreKey = intPreferencesKey(KEY_DATA)

    fun getAppThemeName(): String{
        return appThemes[_isDarkTheme.value] ?: "Theme not set"
    }


    init {
        viewModelScope.launch {
            context.dataStore.data
                .map { preferences -> preferences[dataStoreKey] ?: 0 } // Используем значение по умолчанию, если ключ отсутствует
                .collect { isDarkThemeFromStore ->
                    _isDarkTheme.value = isDarkThemeFromStore
                }
        }
    }

    fun setAppTheme(isDarkTheme: Int) {
        syncWithSavedState(isDarkTheme)
        _isDarkTheme.value = isDarkTheme // Простая синхронная установка значения

    }

    fun getAppTheme(): Int {
        return _isDarkTheme.value
    }

    private suspend fun saveIsDarkThemeToDataStore(data: Int) {
        context.dataStore.edit { preferences ->
            preferences[dataStoreKey] = data
        }
    }

    // Интеграция SavedStateHandle с DataStore
    private fun syncWithSavedState(data: Int) {
        viewModelScope.launch {
            saveIsDarkThemeToDataStore(data) // сохраняем в DataStore
        }
    }
}