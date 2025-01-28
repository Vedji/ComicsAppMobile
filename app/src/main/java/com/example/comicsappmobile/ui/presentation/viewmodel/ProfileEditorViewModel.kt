package com.example.comicsappmobile.ui.presentation.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.example.comicsappmobile.data.mapper.UserMapper
import com.example.comicsappmobile.data.repository.FilesRepository
import com.example.comicsappmobile.data.repository.UserRepository
import com.example.comicsappmobile.di.GlobalState
import com.example.comicsappmobile.ui.presentation.model.FileUiModel
import com.example.comicsappmobile.ui.presentation.model.UserUiModel
import com.example.comicsappmobile.utils.Logger
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileEditorViewModel(
    private val userRepository: UserRepository,
    private val filesRepository: FilesRepository,
    private val globalState: GlobalState
) : BaseViewModel() {
    private val _userLogin = MutableStateFlow<UiState<UserUiModel>>(UiState.Loading())
    val userLogin: StateFlow<UiState<UserUiModel>> = _userLogin

    init {
        viewModelScope.launch {
            delay(200)
            _userLogin.value = UiState.Success(data = UserMapper.map(globalState.authUser.value))
        }
    }

    suspend fun uploadFile(context: Context, fileUri: Uri): UiState<FileUiModel>{
        val data = filesRepository.uploadFile(context, fileUri)
        Logger.debug("uploadBook", "in view model = ${data.data.toString()}")
        return data
    }

    private suspend fun responseUserInfo(newUserTitleImageId: Int, newUserDescription: String): UiState<UserUiModel> {
        try {
            val response = userRepository.responseUpdateInfoAboutUser(
                newUserTitleImageId = newUserTitleImageId,
                newUserDescription = newUserDescription
            )
            return response
        } catch (e: IllegalArgumentException) {
            return UiState.Error(
                message = e.localizedMessage,
                typeError = "Network",
                statusCode = 500
            )
        }
    }

    suspend fun uploadUserInfo(context: Context, newUserTitleImageUri: Uri?, newUserDescription: String): Boolean{
        if (_userLogin.value !is UiState.Success)
            return false
        val description: String = _userLogin.value.data?.userDescription ?: ""
        val imageId: Int = _userLogin.value.data?.userTitleImage ?: 4
        var newImageId: Int = imageId
        _userLogin.value = UiState.Loading()
        if (newUserTitleImageUri != null){
            val responseUploadFile = uploadFile(context, newUserTitleImageUri)
            if (responseUploadFile !is UiState.Success) return false
            newImageId = if (responseUploadFile is UiState.Success) responseUploadFile.data?.fileID ?: imageId else imageId
        }
        val responseUploadInfo = responseUserInfo(
            newUserTitleImageId = newImageId,
            newUserDescription = newUserDescription
        )
        _userLogin.value = responseUploadInfo
        return true
    }

}