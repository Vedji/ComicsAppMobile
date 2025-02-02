package com.example.comicsappmobile.ui.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.comicsappmobile.data.repository.BooksRepository
import com.example.comicsappmobile.data.repository.ChaptersRepository
import com.example.comicsappmobile.data.repository.FavoriteRepository
import com.example.comicsappmobile.data.repository.PagesRepository
import com.example.comicsappmobile.di.GlobalState
import com.example.comicsappmobile.di.SharedViewModel
import com.example.comicsappmobile.ui.presentation.model.BookUiModel
import com.example.comicsappmobile.ui.presentation.model.ChapterUiModel
import com.example.comicsappmobile.ui.presentation.model.FavoriteUiModel
import com.example.comicsappmobile.ui.presentation.model.PageUiModel
import com.example.comicsappmobile.utils.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class PagesViewModel(
    private val bookId: Int = 3,
    private var chapterId: Int = 1,
    private val pagesRepository: PagesRepository,
    private val chaptersRepository: ChaptersRepository,
    private val booksRepository: BooksRepository,
    private val favoriteRepository: FavoriteRepository,
    val sharedViewModel: SharedViewModel,
    val globalState: GlobalState
): BaseViewModel() {


    // Id предыдущей главы
    var previousChapterId: Int = -1
    private val _previousChapterIdState = MutableStateFlow<UiState<Int>>(UiState.Loading())
    val previousChapterIdState: StateFlow<UiState<Int>> = _previousChapterIdState

    // Id Текущей главы
    var followingChapterId: Int = -1
    private val _followingChapterIdState = MutableStateFlow<UiState<Int>>(UiState.Loading())
    val followingChapterIdState: StateFlow<UiState<Int>> = _followingChapterIdState

    // Стартовая страница
    private val _startPage = MutableStateFlow<Int>(0)
    val startPage: StateFlow<Int> = _startPage

    // Информация о книге
    private val _bookUiState = MutableStateFlow<UiState<BookUiModel>>(UiState.Loading())
    val bookUiState: StateFlow<UiState<BookUiModel>> = _bookUiState

    // Список глав
    private val _chapters = MutableStateFlow<UiState<List<ChapterUiModel>>>(UiState.Loading())
    val chaptersUiState: StateFlow<UiState<List<ChapterUiModel>>> = _chapters

    // Текущая глава
    private val _currentChapter = MutableStateFlow<UiState<ChapterUiModel>>(UiState.Loading())
    val currentChapter: StateFlow<UiState<ChapterUiModel>> = _currentChapter

    // Список страниц текущей главы
    private val _pagesUiState = MutableStateFlow<UiState<List<PageUiModel>>>(UiState.Loading())
    val pagesUiState: StateFlow<UiState<List<PageUiModel>>> = _pagesUiState

    // В избранном ли книга
    private val _bookInFavorite = MutableStateFlow<UiState<FavoriteUiModel>>(UiState.Loading())
    val bookInFavorite: StateFlow<UiState<FavoriteUiModel>> = _bookInFavorite


    init {
        val book = sharedViewModel.selectedBookInfo.value
        // val chapters = sharedViewModel.selectedBookChapters.value
        // Logger.debug("init PagesViewModel", "sharedBook = ${book}")
        // Logger.debug("init PagesViewModel", "sharedBook = ${chapters}")
        viewModelScope.launch {
            fetchBook(fetchingBookId = bookId)
            fetchChapters(fetchBookId = bookId)
            setChapter(chapterId = chapterId)
            fetchHasBookInFavorite()
            // if (chapters != null) setChapters(chapters)
            // else fetchChapters(fetchBookId = bookId)
            // if (book != null) setBook(book) else fetchBook(bookId)
        }
    }

    fun getBookId(): Int {
        return bookId
    }

    fun setChapter(chapterId: Int) {
        if (_chapters.value is UiState.Success && _chapters.value.data?.any { it.chapterId == chapterId } == true) {
            viewModelScope.launch {
                fetchChapter(chapterId)
                fetchPages(fetchBookId = bookId, fetchChapterId = chapterId)
                if (_pagesUiState.value is UiState.Success)
                    _startPage.value = _pagesUiState.value.data?.size ?: 0
            }
        }
    }

    fun getChapterId(): Int {
        return chapterId
    }

    fun goLast() {
        if (previousChapterId >= 0) {
            viewModelScope.launch {
                fetchChapter(previousChapterId)
                fetchPages(fetchBookId = bookId, fetchChapterId = previousChapterId)
                if (_pagesUiState.value is UiState.Success)
                    _startPage.value = _pagesUiState.value.data?.size ?: 0
                fetchHasBookInFavorite()
            }
        }
    }

    fun goNext() {
        if (followingChapterId >= 0) {
            viewModelScope.launch {
                fetchChapter(followingChapterId)
                fetchPages(fetchBookId = bookId, fetchChapterId = followingChapterId)
                _startPage.value = 0
                fetchHasBookInFavorite()
            }
        }
    }

    private fun setBook(newBook: BookUiModel) {
        _bookUiState.value = UiState.Success(data = newBook)
    }

    private fun setChapters(newChapters: List<ChapterUiModel>, newChapterId: Int = chapterId) {
        _chapters.value = UiState.Success(data = newChapters)
        fetchChapter(newChapterId)
    }

    private suspend fun fetchChapters(fetchBookId: Int) {
        _chapters.value = UiState.Loading()
        _chapters.value =
            loadWithState({ chaptersRepository.getBookChapters(bookId = fetchBookId) })
        fetchChapter(chapterId)
    }

    private fun fetchChapter(fetchChapterId: Int) {
        if (_chapters.value is UiState.Success && _chapters.value.data != null && _chapters.value.data is List<ChapterUiModel>) {
            val buffer: List<ChapterUiModel> = _chapters.value.data ?: emptyList()
            val find = buffer.findLast { it.chapterId == fetchChapterId }
            if (find != null) {
                _currentChapter.value = UiState.Success(data = find)
            } else {
                _currentChapter.value = UiState.Loading()
            }
        }
    }

    private suspend fun fetchPages(fetchBookId: Int, fetchChapterId: Int) {
        try {
            _pagesUiState.value = UiState.Loading()
            val response = pagesRepository.getChapterPages(
                bookId = fetchBookId,
                chapterId = fetchChapterId
            )
            when (response) {
                is UiState.Success -> {
                    previousChapterId = response.metadata?.lastItemId ?: -1
                    followingChapterId = response.metadata?.nextItemId ?: -1
                    chapterId = fetchChapterId
                    _previousChapterIdState.value =
                        if (previousChapterId >= 0) UiState.Success(data = previousChapterId) else UiState.Error()
                    _followingChapterIdState.value =
                        if (followingChapterId >= 0) UiState.Success(data = followingChapterId) else UiState.Error()
                }

                else -> {}
            }
            _pagesUiState.value = response
        } catch (e: IllegalArgumentException) {
            _pagesUiState.value = UiState.Error(
                message = e.localizedMessage,
                typeError = "Network",
                statusCode = 500
            ) // Устанавливаем ошибочное состояние
        }
    }

    private suspend fun fetchBook(fetchingBookId: Int) {
        try {
            _bookUiState.value = UiState.Loading()
            val response = booksRepository.getBook(bookId = fetchingBookId)
            if (response is UiState.Success && response.data != null) {
                sharedViewModel.updateSelectedBookInfo(response.data!!)
            }
            _bookUiState.value = response

        } catch (e: IllegalArgumentException) {
            _bookUiState.value = UiState.Error(
                message = e.localizedMessage,
                typeError = "Network",
                statusCode = 500
            ) // Устанавливаем ошибочное состояние
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

    private suspend fun setBookToFavorite(bookId: Int = this.bookId, chapterId: Int = this.chapterId) {
        if (_bookInFavorite.value !is UiState.Success)
            return
        try {
            _bookInFavorite.value = UiState.Loading()
            _bookInFavorite.value = favoriteRepository.setBookFavorite(bookId = bookId, chapterId = chapterId)
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
            val favoriteChapterId = _bookInFavorite.value.data?.chapterId ?: -1

            if (favoriteId > 0){
                if (favoriteChapterId == chapterId) deleteBookFromFavorite()
                else setBookToFavorite()
            }else setBookToFavorite()
            fetchHasBookInFavorite()
            Logger.debug("StarSwitch", "uiModel = ${_bookInFavorite.value.data}")
        }
    }
}

