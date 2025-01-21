package com.example.comicsappmobile.di

import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import android.content.Context
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.comicsappmobile.data.dto.entities.user.UserDto
import com.example.comicsappmobile.ui.theme.DarkMediumContrastColorScheme
import com.example.comicsappmobile.ui.theme.darkGreenColorScheme
import com.example.comicsappmobile.ui.theme.darkScheme
import com.example.comicsappmobile.ui.theme.lightScheme
import com.example.comicsappmobile.utils.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

data class GlobalState(
    var isOpenBottomSheet: Boolean,
    var bottomSheetSkipPartiallyExpanded: Boolean,
    var appContext: Context
){

    val appThemes: Map<Int, Pair<String, ColorScheme>> = mapOf(
        0 to ("Светлая тема" to lightScheme),
        1 to ("Темная тема" to darkScheme),
        2 to ("Динамичная светлая тема" to dynamicLightColorScheme(appContext)),
        3 to ("Динамичная темная тема" to dynamicDarkColorScheme(appContext)),
        4 to ("Токсичная тема" to darkGreenColorScheme),
        5 to ("Material3 dark" to DarkMediumContrastColorScheme)
    )
    var currentThemeId = 0

    var firstUserAuth = false

    private val _authUser = MutableStateFlow<UserDto>(
        UserDto.createDefaultUser(-1, "Не авторизованный пользователь"))
    val authUser: MutableStateFlow<UserDto> = _authUser

    fun setAuthUser(newAuthUser: UserDto?){
        _authUser.value = newAuthUser ?: UserDto.createDefaultUser(-1, "Не авторизованный пользователь")
    }

    private val Context.dataStore by preferencesDataStore(name = "appParameters")

    private val USER_ACCESS_TOKEN = stringPreferencesKey("user_access_token")
    private val SETTINGS_APP_THEME = intPreferencesKey("user_age")

    suspend fun saveUserAccessToken(token: String) {
        Logger.debug("saveUserAccessToken", token)
        appContext.dataStore.edit { preferences ->
            preferences[USER_ACCESS_TOKEN] = token
        }
    }

    suspend fun saveSettingsAppTheme(appThemeId: Int) {
        appContext.dataStore.edit { preferences ->
            preferences[SETTINGS_APP_THEME] = appThemeId
        }
    }

    fun getUserAccessToken(): Flow<String?> {
        return appContext.dataStore.data.map { preferences ->
            if (preferences[USER_ACCESS_TOKEN] != "None"){
                RetrofitInstance.accessToken = preferences[USER_ACCESS_TOKEN]
                preferences[USER_ACCESS_TOKEN]
            }else null
        }
    }

    fun getAppThemeId(): Flow<Int> {
        return appContext.dataStore.data.map { preferences ->
            currentThemeId = preferences[SETTINGS_APP_THEME] ?: 0
            preferences[SETTINGS_APP_THEME] ?: 0  // Pair(username, age)
        }
    }


}