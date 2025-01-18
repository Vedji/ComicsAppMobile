package com.example.comicsappmobile.ui.presentation.viewmodel

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewModelScope
import com.example.comicsappmobile.data.dto.entities.user.UserDto
import com.example.comicsappmobile.data.mapper.UserMapper
import com.example.comicsappmobile.data.repository.UserRepository
import com.example.comicsappmobile.di.GlobalState
import com.example.comicsappmobile.di.SharedViewModel
import com.example.comicsappmobile.ui.presentation.model.UserUiModel
import com.example.comicsappmobile.ui.theme.darkScheme
import com.example.comicsappmobile.ui.theme.lightScheme
import com.example.comicsappmobile.utils.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class SettingsViewModel(
    private val userRepository: UserRepository,
    val globalState: GlobalState
): BaseViewModel() {

    val appThemes: Map<Int, Pair<String, ColorScheme>> = globalState.appThemes

    private val _appThemeID = MutableStateFlow<Int>(0)
    val appThemeID: StateFlow<Int> = _appThemeID

    private val _userLogin = MutableStateFlow<UiState<UserUiModel>>(UiState.Loading())
    val userLogin: StateFlow<UiState<UserUiModel>> = _userLogin


    init {
        val user = globalState.authUser.value
        if (user.userId > 0)
            _userLogin.value = UiState.Success(data = UserMapper.map(user))
        viewModelScope.launch {

        }

    }

    suspend fun setAppTheme(themeId: Int){
        globalState.saveSettingsAppTheme(themeId)
    }

    fun outFromUserLogin(){
        viewModelScope.launch {
            globalState.getUserAccessToken().collect{
                Logger.debug("outFromUserLogin","token = $it")
                globalState.saveUserAccessToken("None")
                globalState.setAuthUser(null)
                _userLogin.value = UiState.Loading()
            }
        }

    }



}
