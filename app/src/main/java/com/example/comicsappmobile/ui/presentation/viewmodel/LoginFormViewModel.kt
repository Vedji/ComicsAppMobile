package com.example.comicsappmobile.ui.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.comicsappmobile.data.repository.UserRepository
import com.example.comicsappmobile.di.GlobalState
import com.example.comicsappmobile.di.RetrofitInstance
import com.example.comicsappmobile.di.SharedViewModel
import com.example.comicsappmobile.navigation.Screen
import com.example.comicsappmobile.utils.Logger
import com.example.comicsappmobile.ui.presentation.model.UserUiModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginFormViewModel(
    private val userRepository: UserRepository,
    val sharedViewModel: SharedViewModel,
    val globalState: GlobalState
): BaseViewModel() {

    // TODO: Get user comments to books
    //  viewModelScope.launch {
    //      val tt = userRepository.responseFetchUserComments()
    //      Logger.debug("responseFetchUserComments", "repo = ${tt.data}")
    //  }

    private val _userLogin = MutableStateFlow<UiState<UserUiModel>>(UiState.Loading())
    val userLogin: StateFlow<UiState<UserUiModel>> = _userLogin

    init {
        viewModelScope.launch {
            delay(100)  // add delay for access toke has been upload
            refreshUserLogin()
        }
    }


    suspend fun loginFromUsername(username: String, password: String) {
        try {
            _userLogin.value = UiState.Loading()
            val response = userRepository.responseUserAuthorization(
                username = username, password = password
            )
            delay(100)
            Logger.debug("loginFromUsername Test", response.message.toString())
            _userLogin.value = if (response is UiState.Success && globalState.authUser.value.userId > 0) response else UiState.Error(message = response.message)
            Logger.debug(
                "LoginFormViewModel -> loginFromUsername",
                "User info = '${response.data.toString()}'"
            )
        } catch (e: IllegalArgumentException) {
            _userLogin.value = UiState.Error(
                message = e.localizedMessage,
                typeError = "Network",
                statusCode = 500
            ) // Устанавливаем ошибочное состояние
        }
    }

    fun setUserAuthError(message: String, typeError: String){
        _userLogin.value = UiState.Error(message = message, typeError = typeError, statusCode = -1)
    }

    suspend fun refreshUserLogin(): Boolean {
        var hasRefreshSuccess = false
        var newToken: String? = null
        try {
            _userLogin.value = UiState.Loading()
            Logger.debug("LoginFormViewModel -> refreshUserLogin", "Collector'")
            if (RetrofitInstance.accessToken.isNullOrEmpty())
                return false
            val response = userRepository.responseRefreshUserAuthorization(token = RetrofitInstance.accessToken ?: "")
            delay(100)
            _userLogin.value = if (response is UiState.Success && globalState.authUser.value.userId > 0) response else UiState.Error(message = "Введите логи и пароль")
            Logger.debug(
                "LoginFormViewModel -> refreshUserLogin",
                "User info = '${response.data.toString()}'"
            )
            Logger.debug("LoginFormViewModel -> refreshUserLogin", "collector 2 = '$newToken'")
        } catch (e: IllegalArgumentException) {
            _userLogin.value = UiState.Error(
                message = e.localizedMessage,
                typeError = "Network",
                statusCode = 500
            ) // Устанавливаем ошибочное состояние
        }
        return false
    }


}