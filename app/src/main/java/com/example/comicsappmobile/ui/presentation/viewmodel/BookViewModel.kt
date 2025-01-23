package com.example.comicsappmobile.ui.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.comicsappmobile.data.repository.BooksRepository
import com.example.comicsappmobile.data.repository.ChaptersRepository
import com.example.comicsappmobile.data.repository.CommentsRepository
import com.example.comicsappmobile.data.repository.FavoriteRepository
import com.example.comicsappmobile.data.repository.GenresRepository
import com.example.comicsappmobile.data.repository.UserRepository
import com.example.comicsappmobile.di.GlobalState
import com.example.comicsappmobile.di.SharedViewModel
import com.example.comicsappmobile.utils.Logger
import com.example.comicsappmobile.ui.presentation.model.BookUiModel
import com.example.comicsappmobile.ui.presentation.model.ChapterUiModel
import com.example.comicsappmobile.ui.presentation.model.CommentUiModel
import com.example.comicsappmobile.ui.presentation.model.FavoriteUiModel
import com.example.comicsappmobile.ui.presentation.model.GenreUiModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BookViewModel(
    val bookId: Int = -1,
    private val booksRepository: BooksRepository,
    private val genresRepository: GenresRepository,
    private val chaptersRepository: ChaptersRepository,
    private val commentsRepository: CommentsRepository,
    val sharedViewModel: SharedViewModel,
    private val userRepository: UserRepository,
    private val favoriteRepository: FavoriteRepository,
    val globalState: GlobalState
    ): BaseViewModel() {

    private val _bookUiState = MutableStateFlow<UiState<BookUiModel>>(UiState.Loading())
    val bookUiState: StateFlow<UiState<BookUiModel>> = _bookUiState

    private val _genres = MutableStateFlow<UiState<List<GenreUiModel>>>(UiState.Loading())
    val genreUiState: StateFlow<UiState<List<GenreUiModel>>> = _genres

    private val _chapters = MutableStateFlow<UiState<List<ChapterUiModel>>>(UiState.Loading())
    val chaptersUiState: StateFlow<UiState<List<ChapterUiModel>>> = _chapters

    private val _bookInFavorite = MutableStateFlow<UiState<FavoriteUiModel>>(UiState.Loading())
    val bookInFavorite: StateFlow<UiState<FavoriteUiModel>> = _bookInFavorite

    private val _userComment = MutableStateFlow<UiState<CommentUiModel>>(UiState.Loading())
    val userComment: StateFlow<UiState<CommentUiModel>> = _userComment

    private val commentsLimit = 2
    private var commentsOffset = 0
    private var commentsHasNext = true
    private val _comments = MutableStateFlow<UiState<List<CommentUiModel>>>(UiState.Loading())
    val commentsUiState: StateFlow<UiState<List<CommentUiModel>>> = _comments

    init { refresh() }

    fun refresh(){
        Logger.debug("return when(test)", bookId.toString())
        loadBook()
        loadGenres()
        loadChapters()
        loadComments(true)
        viewModelScope.launch {
            fetchHasBookInFavorite()
            loadUserComment()
            delay(200)
            // setUserComment(4, "Spnsor about this app is Flash energy and monser energy))))")
            // deleteUserComment()
        }
    }

    fun commentHasNext(): Boolean{
        return commentsHasNext
    }

    private fun loadBook() {
        viewModelScope.launch {

            try {
                val response = booksRepository.getBook(bookId = bookId)
                _bookUiState.value = UiState.Loading()
                _bookUiState.value = booksRepository.getBook(bookId = bookId)
                if (response is UiState.Success && response.data != null){
                    sharedViewModel.updateSelectedBookInfo(response.data!!)
                }

            } catch (e: IllegalArgumentException) {
                _bookUiState.value = UiState.Error(
                    message = e.localizedMessage,
                    typeError = "Network",
                    statusCode = 500
                ) // Устанавливаем ошибочное состояние
            }
        }
    }

    private fun loadGenres(){
        viewModelScope.launch {
            try {
                _genres.value = UiState.Loading()
                _genres.value = genresRepository.getBookGenres(bookId = bookId)
                if (_genres.value is UiState.Success)
                    Logger.debug("BookViewModel -> loadGenres", "genres = ${_genres.value.data}")
                if (_genres.value is UiState.Error)
                    Logger.debug("BookViewModel -> loadGenres", _genres.value.message.toString())
            } catch (e: IllegalArgumentException) {
                _genres.value = UiState.Error(
                    message = e.localizedMessage,
                    typeError = "Network",
                    statusCode = 500
                ) // Устанавливаем ошибочное состояние
            }
        }
    }

    private fun loadChapters(){
        viewModelScope.launch {
            _chapters.value = UiState.Loading()
            _chapters.value = loadWithState({ chaptersRepository.getBookChapters(bookId = bookId) })
            if (_chapters.value is UiState.Success && _chapters.value.data != null){
                sharedViewModel.updateSelectedBookChapters(_chapters.value.data!!)
            }
        }
    }

    private suspend fun fetchHasBookInFavorite(bookId: Int = this.bookId) {
        try {
            _bookInFavorite.value = UiState.Loading()
            _bookInFavorite.value = favoriteRepository.fetchBookInFavorite(bookId = bookId)
            if (_bookInFavorite.value is UiState.Success)
                Logger.debug(
                    "BookViewModel -> fetchHasBookInFavorite",
                    "value = ${_bookInFavorite.value.data}"
                )
            if (_bookInFavorite.value is UiState.Error)
                Logger.debug(
                    "BookViewModel -> loadGenres",
                    _bookInFavorite.value.message.toString()
                )
        } catch (e: IllegalArgumentException) {
            _bookInFavorite.value = UiState.Error(
                message = e.localizedMessage,
                typeError = "Network",
                statusCode = 500
            ) // Устанавливаем ошибочное состояние
        }

    }

    fun loadComments(first: Boolean = false) {
        viewModelScope.launch {
            if ((commentsHasNext && _comments.value is UiState.Success) || first) {
                val buffer = _comments.value.data as? List<CommentUiModel> ?: emptyList()
                Logger.debug("loadComments", "buffer = ${buffer.toString()}")
                _comments.value = UiState.Loading(data = buffer)
                delay(600)
                // if (BuildConfig.DEBUG)
                //     delay(1_500)
                val response = loadWithState({
                    commentsRepository.loadComments(
                        bookId = bookId,
                        limit = commentsLimit,
                        offset = commentsOffset
                    )
                })
                if (response is UiState.Success) {
                    val res = response.data ?: emptyList()

                    Logger.debug("loadComments", "res = ${res.toString()}")
                    val data = buffer + res
                    Logger.debug("loadComments", "res + buffer = ${data}")

                    _comments.value = UiState.Success<List<CommentUiModel>>(
                        data = data,
                        metadata = response.metadata
                    )
                    Logger.debug("loadComments value", "value = ${_comments.value.data}")
                }
                if (response is UiState.Success)
                    commentsOffset = (_comments.value.metadata?.offset ?: 0) + commentsLimit
                if (response is UiState.Success)
                    commentsHasNext = response.metadata?.hasNext ?: false
                Logger.debug(
                    "loadComments",
                    "commentsOffset = '$commentsOffset', commentsHasNext = '$commentsHasNext'"
                )

            }
        }
    }

    private suspend fun setBookToFavorite(bookId: Int = this.bookId) {
        if (_bookInFavorite.value !is UiState.Success)
            return
        try {
            _bookInFavorite.value = UiState.Loading()
            _bookInFavorite.value = favoriteRepository.setBookFavorite(bookId = bookId)
            if (_bookInFavorite.value is UiState.Success)
                Logger.debug(
                    "BookViewModel -> fetchHasBookInFavorite",
                    "value = ${_bookInFavorite.value.data}"
                )
            if (_bookInFavorite.value is UiState.Error)
                Logger.debug(
                    "BookViewModel -> loadGenres",
                    _bookInFavorite.value.message.toString()
                )
        } catch (e: IllegalArgumentException) {
            _bookInFavorite.value = UiState.Error(
                message = e.localizedMessage,
                typeError = "Network",
                statusCode = 500
            ) // Устанавливаем ошибочное состояние
        }

    }

    private suspend fun deleteBookFromFavorite() {  // FIXME Not return a FavoriteUiModel
        if (_bookInFavorite.value !is UiState.Success)
            return
        if ((_bookInFavorite.value.data?.favoriteId ?: 0 ) < 1)
            return
        try {
            val favoriteId = _bookInFavorite.value.data?.favoriteId ?: -1
            _bookInFavorite.value = UiState.Loading()
            _bookInFavorite.value = favoriteRepository.deleteBookFromFavorite(
                favoriteId = favoriteId
            )
            if (_bookInFavorite.value is UiState.Success)
                Logger.debug(
                    "BookViewModel -> deleteBookFromFavorite",
                    "value = ${_bookInFavorite.value.data}"
                )
            if (_bookInFavorite.value is UiState.Error)
                Logger.debug(
                    "BookViewModel -> deleteBookFromFavorite",
                    _bookInFavorite.value.message.toString()
                )
        } catch (e: IllegalArgumentException) {
            _bookInFavorite.value = UiState.Error(
                message = e.localizedMessage,
                typeError = "Network",
                statusCode = 500
            ) // Устанавливаем ошибочное состояние
        }

    }

    fun switchFavoriteBook(){
        if (_bookInFavorite.value !is UiState.Success)
            return
        viewModelScope.launch {
            val favoriteId = _bookInFavorite.value.data?.favoriteId ?: -1
            if (favoriteId > 0){
                deleteBookFromFavorite()
                fetchHasBookInFavorite()
            }
            else setBookToFavorite()
            Logger.debug("StarSwitch", "uiModel = ${_bookInFavorite.value.data}")
        }
    }

    suspend fun loadUserComment(bookId: Int = this.bookId) {
        _userComment.value = UiState.Loading()
        try {
            val response = loadWithState({ commentsRepository.loadUserCommentForBook(bookId = bookId) })
            _userComment.value = response
            Logger.debug("BookViewModel -> loadUserComment", "response = $response")
        } catch (e: IllegalArgumentException) {
            _userComment.value = UiState.Error(
                message = e.localizedMessage,
                typeError = "Network",
                statusCode = 500
            )
        } catch (e: Exception){
            _userComment.value = UiState.Error(
                message = e.localizedMessage,
                typeError = e.message,
                statusCode = -1
            )
        }
    }

    suspend fun setUserComment(rating: Int, comment: String, bookId: Int = this.bookId) {
        if (_userComment.value is UiState.Success){
            try {
                _userComment.value = UiState.Loading()
                val response = loadWithState {
                    commentsRepository.setUserCommentForBook(
                        bookId = bookId,
                        rating = rating,
                        comment = comment) }
                _userComment.value = response
                Logger.debug("BookViewModel -> setUserComment", "response = $response")
            } catch (e: IllegalArgumentException) {
                _userComment.value = UiState.Error(
                    message = e.localizedMessage,
                    typeError = "Network",
                    statusCode = 500
                )
            } catch (e: Exception){
                _userComment.value = UiState.Error(
                    message = e.localizedMessage,
                    typeError = e.message,
                    statusCode = -1
                )
            }
        }
    }

    suspend fun deleteUserComment() {
        if (_userComment.value is UiState.Success){
            try {
                val commentId: Int = _userComment.value.data?.commentId ?: -1
                if (commentId < 0){
                    _userComment.value = UiState.Error(message = "Comment id in deleteUserComment is null", typeError = "InvalidValue")
                    return
                }
                _userComment.value = UiState.Loading()
                val response = loadWithState{ commentsRepository.deleteUserCommentForBook(commentId = commentId) }
                _userComment.value = response
                Logger.debug("BookViewModel -> deleteUserComment", "response = $response")
            } catch (e: IllegalArgumentException) {
                _userComment.value = UiState.Error(
                    message = e.localizedMessage,
                    typeError = "Network",
                    statusCode = 500
                )
            } catch (e: Exception){
                _userComment.value = UiState.Error(
                    message = e.localizedMessage,
                    typeError = e.message,
                    statusCode = -1
                )
            }
        }
    }

    suspend fun deleteChapter(chapterId: Int): Boolean{
        if (_chapters.value !is UiState.Success)
            return false
        val response = loadWithState { chaptersRepository.deleteChapter(bookId = bookId, chapterId = chapterId) }
        if (response is UiState.Success){
            loadChapters()
            return true
        }
        return false
    }

    fun launchSetUserComment(rating: Int, comment: String, bookId: Int = this.bookId){
        viewModelScope.launch {
            setUserComment(rating, comment, bookId)
        }
    }

    fun launchDeleteUserComment(){
        viewModelScope.launch {
            deleteUserComment()
            loadUserComment()
        }
    }

    fun launchDeleteChapter(chapterId: Int): Boolean{
        var buffer = false
        viewModelScope.launch {
            buffer = deleteChapter(chapterId)
        }
        return buffer
    }
}