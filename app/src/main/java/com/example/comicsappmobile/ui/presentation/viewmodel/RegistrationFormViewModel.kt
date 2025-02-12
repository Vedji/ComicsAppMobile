package com.example.comicsappmobile.ui.presentation.viewmodel

import com.example.comicsappmobile.data.repository.UserRepository
import com.example.comicsappmobile.di.GlobalState
import com.example.comicsappmobile.ui.presentation.model.UserUiModel
import com.example.comicsappmobile.utils.Logger
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

    suspend fun registration(username: String, mail: String, password: String): Boolean {
        try {
            _userLogin.value = UiState.Loading()
            val response = userRepository.responseUserRegistration(
                username = username, email = mail, password = password
            )
            _userLogin.value =
                if (response is UiState.Success && globalState.authUser.value.userId > 0)
                    response
                else
                    UiState.Error(
                        message = response.message,
                        statusCode = response.statusCode,
                        typeError = response.typeError
                    )
            Logger.debug(
                "RegistrationFormViewModel -> registration",
                "User info = '${response.data.toString()}'"
            )
            return if (_userLogin.value is UiState.Success) (_userLogin.value.data?.userId ?: 0) > 0 else false
        } catch (e: IllegalArgumentException) {
            _userLogin.value = UiState.Error(
                message = e.localizedMessage,
                typeError = "Network",
                statusCode = 500
            )
            return false
        }
    }
}