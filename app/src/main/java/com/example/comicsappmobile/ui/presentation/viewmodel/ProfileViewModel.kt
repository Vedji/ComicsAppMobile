package com.example.comicsappmobile.ui.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.comicsappmobile.data.mapper.UserMapper
import com.example.comicsappmobile.data.repository.BooksRepository
import com.example.comicsappmobile.data.repository.UserRepository
import com.example.comicsappmobile.di.GlobalState
import com.example.comicsappmobile.ui.presentation.model.BookUiModel
import com.example.comicsappmobile.ui.presentation.model.CommentUiModel
import com.example.comicsappmobile.ui.presentation.model.UserFavoriteUiModel
import com.example.comicsappmobile.ui.presentation.model.UserUiModel
import com.example.comicsappmobile.utils.Logger
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class ProfileViewModel(
    private val userRepository: UserRepository,
    val booksRepository: BooksRepository,
    val globalState: GlobalState
) : BaseViewModel() {

    private val _userLogin = MutableStateFlow<UiState<UserUiModel>>(UiState.Loading())
    val userLogin: StateFlow<UiState<UserUiModel>> = _userLogin

    private val _userCommentsList = MutableStateFlow<UiState<List<CommentUiModel>>>(UiState.Loading())
    val userCommentsList: StateFlow<UiState<List<CommentUiModel>>> = _userCommentsList

    private val _userStarsBooks = MutableStateFlow<UiState<List<Pair<BookUiModel, UserFavoriteUiModel>>>>(UiState.Loading())
    val userStarsBooks: StateFlow<UiState<List<Pair<BookUiModel, UserFavoriteUiModel>>>> = _userStarsBooks

    private val _userAddedBooks = MutableStateFlow<UiState<List<BookUiModel>>>(UiState.Loading())
    val userAddedBooks: StateFlow<UiState<List<BookUiModel>>> = _userAddedBooks

    private val _bookInfoById = MutableStateFlow<MutableMap<Int, BookUiModel>>(mutableMapOf<Int, BookUiModel>())
    val bookInfoById: StateFlow<MutableMap<Int, BookUiModel>> = _bookInfoById

    init{
        viewModelScope.launch {
            delay(200)
            _userLogin.value = UiState.Success(data = UserMapper.map(globalState.authUser.value))
            loadStarsBooks()
            fetchAddedBooks(userId = _userLogin.value.data?.userId ?: -1)
            fetchUserComments()
        }
    }

    suspend fun fetchBookInfoById(bookId: Int): UiState<BookUiModel>{
        try {
            val response = booksRepository.getBook(bookId)
            Logger.debug(
                "LoginFormViewModel -> fetchBookInfoById",
                "Stars info = '${response.data.toString()}'"
            )
            return response
        } catch (e: IllegalArgumentException) {
            return UiState.Error(
                message = e.localizedMessage,
                typeError = "Network",
                statusCode = 500
            ) // Устанавливаем ошибочное состояние
        }
    }

    private suspend fun fetchUserComments() {
        try {
            _userCommentsList.value = UiState.Loading()
            val response = userRepository.responseFetchUserComments()
            if (response is UiState.Success && response.data != null)
                for (comment in response.data!!){
                    val bookState = fetchBookInfoById(comment.bookId)
                    if (bookState !is UiState.Success) continue
                    bookState.data?.let { book ->
                        _bookInfoById.value[book.bookId] = book
                    }
                }
            _userCommentsList.value = response
        } catch (e: IllegalArgumentException) {
            _userCommentsList.value = UiState.Error(
                message = e.localizedMessage,
                typeError = "Network",
                statusCode = 500
            )
        }
    }

    private suspend fun loadStarsBooks(){
        try {
            _userStarsBooks.value = UiState.Loading()
            val response = userRepository.responseFetchUserFavorites()
            _userStarsBooks.value = response
            Logger.debug(
                "LoginFormViewModel -> loadStarsBooks",
                "Stars info = '${response.data.toString()}'"
            )
        } catch (e: IllegalArgumentException) {
            _userStarsBooks.value = UiState.Error(
                message = e.localizedMessage,
                typeError = "Network",
                statusCode = 500
            ) // Устанавливаем ошибочное состояние
        }
    }

    private suspend fun fetchAddedBooks(userId: Int) {
        try {
            _userAddedBooks.value = UiState.Loading()
            val response = userRepository.fetchBooksWhichUserAdded(userId = userId)
            _userAddedBooks.value = response
        } catch (e: IllegalArgumentException) {
            _userAddedBooks.value = UiState.Error(
                message = e.localizedMessage,
                typeError = "Network",
                statusCode = 500
            )
        }
    }
}