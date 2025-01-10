package com.example.comicsapp.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.comicsapp.utils.network.RetrofitInstance
import com.example.comicsapp.data.model.api.ApiAuthorizationUser
import com.example.comicsapp.data.model.api.ApiUser
import com.example.comicsapp.data.model.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class UserViewModel(private val state: SavedStateHandle, private val context: Context) : ViewModel() {


    private val defaultUser: ApiUser = ApiUser(
        -1,
        "No user",
        "",
        -1,
        "",
        0,
        ""
    )
    // StateFlow для хранения списка пользователей
    private val _authorizedUser = MutableStateFlow<ApiUser?>( null )
    val authorizedUser: StateFlow<ApiUser?> = _authorizedUser

    private val _accessToken = MutableStateFlow<String?>( null )
    val accessToken: StateFlow<String?> = _accessToken

    private val _uiStateAuthorization = MutableStateFlow<UiState<ApiAuthorizationUser>>(UiState.Loading)
    val uiStateAuthorization: StateFlow<UiState<ApiAuthorizationUser>> = _uiStateAuthorization

    // Для подписки использовать: val user by userState.collectAsState()

    init {
        authorizationByUserName("Veji", "hashed_password")
    }

    private fun setAuthorizedUser(newUser: ApiUser, accessToken: String, refreshToken: String) {
        _authorizedUser.value = newUser // Простая синхронная установка значения
        _accessToken.value = accessToken
        RetrofitInstance.accessToken = accessToken
    }
    private fun setAccessToken(newAccessToken: String) {
        _accessToken.value = newAccessToken // Простая синхронная установка значения
    }

    fun login(email: String, password: String){
        Log.d("User login", "email: $email, password: $password")
    }

    fun authorizationByUserName(username: String, password: String){
        viewModelScope.launch {
            Log.d("userAuthorization", "launch")
            userAuthorization(null, username, password)
            Log.d("userAuthorization", "user = ${_authorizedUser.value}, accessToken = $_accessToken")

            for (i in 0..10){
                try {
                    val response = RetrofitInstance.api.getIsBookInUserFavoriteList(
                        _accessToken.value ?: "",
                        3
                    )

                    Log.d("userAuthorization response", "response($i) = ${response.toString()}")
                }catch (e: Exception) {
                    Log.d("userAuthorization response", "Error: ${e.message}")
                    _uiStateAuthorization.value = UiState.Error(e.toString())
                    e.printStackTrace()
                }

        }


        }
    }

    private suspend fun userAuthorization(email: String?, username: String?, password: String){
        Log.d("userAuthorization", "launch")
        if (email == null && username == null)
            return
        if (email == null && username != null){
            Log.d("userAuthorization", "launch")

            try {
                val response = RetrofitInstance.api.getToken(username = username, password = password)
                Log.d("userAuthorization", "launch ${response}")
                Log.d("userAuthorization", response.toString())
                setAuthorizedUser(response.aboutUser, response.accessToken, response.refreshToken)
            } catch (e: Exception) {
                Log.d("userAuthorization", "Error: ${e.message}")
                _uiStateAuthorization.value = UiState.Error(e.toString())
                e.printStackTrace()
            }
            return
        }
        if (email != null && username == null){
            val response = RetrofitInstance.api.getToken(email, "", password)
            try {
                Log.d("userAuthorization", response.toString())
                setAuthorizedUser(response.aboutUser, response.accessToken, response.refreshToken)
            } catch (e: Exception) {
                _uiStateAuthorization.value = UiState.Error(e.toString())
                e.printStackTrace()
            }
            return
        }
        if (email != null && username != null){
            val response = RetrofitInstance.api.getToken(email, username, password)
            try {
                Log.d("userAuthorization", response.toString())
                setAuthorizedUser(response.aboutUser, response.accessToken, response.refreshToken)
            } catch (e: Exception) {
                _uiStateAuthorization.value = UiState.Error(e.toString())
                e.printStackTrace()
            }
            return
        }
    }

}