package com.example.comicsappmobile.ui.presentation.viewmodel

import com.example.comicsappmobile.data.repository.UserRepository
import com.example.comicsappmobile.di.GlobalState
import com.example.comicsappmobile.di.RetrofitInstance
import com.example.comicsappmobile.di.SharedViewModel
import com.example.comicsappmobile.ui.presentation.model.UserUiModel
import com.example.comicsappmobile.utils.Logger
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RegistrationFormViewModel(
    private val userRepository: UserRepository,
    val globalState: GlobalState
): BaseViewModel() {
    private val _userLogin = MutableStateFlow<UiState<UserUiModel>>(UiState.Loading())
    val userLogin: StateFlow<UiState<UserUiModel>> = _userLogin

    fun setUserState(userData: UiState<UserUiModel>) {
        _userLogin.value = userData
    }

    suspend fun registration(username: String, mail: String, password: String) {
        try {
            _userLogin.value = UiState.Loading()
            val response = userRepository.responseUserRegistration(
                username = username, email = mail, password = password
            )
            Logger.debug("registration Test", response.message.toString())
            _userLogin.value = if (response is UiState.Success && globalState.authUser.value.userId > 0) response else UiState.Error(message = response.message)
            Logger.debug(
                "LoginFormViewModel -> registration",
                "User info = '${response.data.toString()}'"
            )
        } catch (e: IllegalArgumentException) {
            _userLogin.value = UiState.Error(
                message = e.localizedMessage,
                typeError = "Network",
                statusCode = 500
            )
        }
    }

    suspend fun refreshUserLogin(): Boolean {
        try {
            _userLogin.value = UiState.Loading()
            Logger.debug("RegistrationFormViewModel -> refreshUserLogin", "Collector'")
            if (RetrofitInstance.accessToken.isNullOrEmpty())
                return false
            val response = userRepository.responseRefreshUserAuthorization(token = RetrofitInstance.accessToken ?: "")
            delay(100)
            _userLogin.value = if (response is UiState.Success && globalState.authUser.value.userId > 0) response else UiState.Error(message = "Введите логи и пароль")
            Logger.debug(
                "RegistrationFormViewModel -> refreshUserLogin",
                "User info = '${response.data.toString()}'"
            )
            return true
        } catch (e: IllegalArgumentException) {
            _userLogin.value = UiState.Error(
                message = e.localizedMessage,
                typeError = "Network",
                statusCode = 500
            )
        }
        return false
    }
}