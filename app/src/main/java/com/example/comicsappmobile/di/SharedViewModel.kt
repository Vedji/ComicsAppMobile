package com.example.comicsappmobile.di

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.comicsappmobile.BuildConfig
import com.example.comicsappmobile.data.dto.entities.user.UserDto
import com.example.comicsappmobile.utils.Logger
import com.example.comicsappmobile.ui.presentation.model.BookUiModel
import com.example.comicsappmobile.ui.presentation.model.ChapterUiModel

class SharedViewModel : ViewModel() {

    init {
        Logger.debug("SharedViewModel", "Test _inputText")
    }

    private val _currentAuthorizingUser = mutableStateOf<UserDto>(UserDto.createDefaultUser(-1, "Не авторизированный пользователь"))
    val currentAuthorizingUser: State<UserDto> get() = _currentAuthorizingUser

    private val _selectedBookInfo = mutableStateOf<BookUiModel?>( null )
    val selectedBookInfo: State<BookUiModel?> get() = _selectedBookInfo

    private val _selectedBookChapters = mutableStateOf<List<ChapterUiModel>?>(null)
    val selectedBookChapters: State<List<ChapterUiModel>?> get() = _selectedBookChapters

    private val _inputText = mutableStateOf("")
    val inputText: State<String> get() = _inputText

    fun updateCurrentAuthorizingUser(newUser: UserDto){
        _currentAuthorizingUser.value = newUser
    }

    fun updateText(newText: String) {
        _inputText.value = newText
    }

    fun updateSelectedBookInfo(newSelectedBookInfo: BookUiModel){
        _selectedBookInfo.value = newSelectedBookInfo
    }

    fun updateSelectedBookChapters(newSelectedBookChapters: List<ChapterUiModel>){
        _selectedBookChapters.value = newSelectedBookChapters
    }
    fun isUserHasPermission(
        addedUserId: Int = -100000,
        hasAdmin: Boolean = false,
        hasHelper:Boolean = false,
        hasEditor: Boolean = false,
        hasPublisher: Boolean = false,
        hasUser: Boolean = false
        ): Boolean{
        // User has been authorizated with any actions
        if (BuildConfig.DEBUG)
            return true
        if ((_currentAuthorizingUser.value.userId ?: 0) > 0) return true
        if (_currentAuthorizingUser.value.userId == addedUserId) return true
        if (hasAdmin && (_currentAuthorizingUser.value.permission ?: 0) == 4) return true
        if (hasPublisher && (_currentAuthorizingUser.value.permission ?: 0) == 3) return true
        if (hasEditor && (_currentAuthorizingUser.value.permission ?: 0) == 2) return true
        if (hasHelper && (_currentAuthorizingUser.value.permission ?: 0) == 1) return true
        return false
    }
}

