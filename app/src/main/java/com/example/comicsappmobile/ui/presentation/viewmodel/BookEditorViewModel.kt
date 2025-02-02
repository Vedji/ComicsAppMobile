package com.example.comicsappmobile.ui.presentation.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.example.comicsappmobile.data.repository.BooksRepository
import com.example.comicsappmobile.data.repository.ChaptersRepository
import com.example.comicsappmobile.data.repository.FilesRepository
import com.example.comicsappmobile.data.repository.GenresRepository
import com.example.comicsappmobile.di.GlobalState
import com.example.comicsappmobile.ui.presentation.model.BookUiModel
import com.example.comicsappmobile.ui.presentation.model.ChapterUiModel
import com.example.comicsappmobile.ui.presentation.model.FileUiModel
import com.example.comicsappmobile.ui.presentation.model.GenreUiModel
import com.example.comicsappmobile.utils.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BookEditorViewModel(
    private var bookId: Int = -1,
    private val booksRepository: BooksRepository,
    private val genresRepository: GenresRepository,
    private val chaptersRepository: ChaptersRepository,
    private val filesRepository: FilesRepository,
    private val globalState: GlobalState
): BaseViewModel() {

    private val _bookUiState = MutableStateFlow<UiState<BookUiModel>>(UiState.Loading())
    val bookUiState: StateFlow<UiState<BookUiModel>> = _bookUiState

    private val _allGenresUiState = MutableStateFlow<UiState<List<GenreUiModel>>>(UiState.Loading())
    val allGenresUiState: StateFlow<UiState<List<GenreUiModel>>> = _allGenresUiState

    private val _bookGenresUiState =
        MutableStateFlow<UiState<List<GenreUiModel>>>(UiState.Loading())
    val bookGenresUiState: StateFlow<UiState<List<GenreUiModel>>> = _bookGenresUiState

    private val _bookChaptersUiState =
        MutableStateFlow<UiState<List<ChapterUiModel>>>(UiState.Loading())
    val bookChaptersUiState: StateFlow<UiState<List<ChapterUiModel>>> = _bookChaptersUiState

    private val _uploading = MutableStateFlow<UiState<Boolean>>(UiState.Loading())
    val uploading: StateFlow<UiState<Boolean>> = _uploading

    private val _uploadingError = MutableStateFlow<UiState<Boolean>>(UiState.Loading())
    val uploadingError: StateFlow<UiState<Boolean>> = _uploadingError

    init {
        viewModelScope.launch {
            refreshBook()
        }
    }

    suspend fun uploadFile(context: Context, fileUri: Uri): UiState<FileUiModel> {
        val data = filesRepository.uploadFile(context, fileUri)
        Logger.debug("uploadBook", "in view model = ${data.data.toString()}")
        return data
    }

    suspend fun uploadBook(
        bookId: Int = this.bookId,
        bookName: String?,
        bookGenres: List<Int>?,
        bookDescription: String?,
        bookDateOfPublication: String?,
        bookTitleImageId: Int?,
        bookChaptersSequence: List<Int>?
    ): UiState<BookUiModel> {
        val response = booksRepository.updateBook(
            bookId = bookId,
            bookName = bookName,
            bookGenres = bookGenres,
            bookDescription = bookDescription,
            bookDateOfPublication = bookDateOfPublication,
            bookTitleImageId = bookTitleImageId,
            bookChaptersSequence = bookChaptersSequence
        )
        if (response is UiState.Error) {
            _uploadingError.value = UiState.Error(
                statusCode = response.statusCode,
                typeError = response.typeError,
                message = response.message
            )
        }
        if (response is UiState.Success && response.data != null) {
            this.bookId = response.data?.bookId ?: this.bookId
            Logger.debug("newBookId", "bookId = ${this.bookId}")
        }
        return response
    }

    suspend fun loadBookAndImage(
        context: Context,
        fileUri: Uri?,
        bookId: Int = this.bookId,
        bookName: String?,
        bookGenres: List<Int>?,
        bookDescription: String?,
        bookDateOfPublication: String?,
        bookChaptersSequence: List<Int>?
    ): UiState<BookUiModel> {
        _uploading.value = UiState.Loading()
        var image: Int = -1
        if (fileUri == null && bookId <= 0) {
            _uploading.value = UiState.Error()
            return UiState.Error()
        }
        if (fileUri != null) {
            val fileImage = uploadFile(context, fileUri)
            if (fileImage is UiState.Success && fileImage.data?.fileID != null) {
                image = fileImage.data?.fileID ?: -1
            } else {
                _uploading.value = UiState.Error()
                return UiState.Error(
                    statusCode = fileImage.statusCode,
                    typeError = fileImage.typeError,
                    message = fileImage.message
                )
            }

        }
        val updatingBook = uploadBook(
            bookId = bookId,
            bookName = bookName,
            bookGenres = bookGenres,
            bookDescription = bookDescription,
            bookDateOfPublication = bookDateOfPublication,
            bookTitleImageId = image,
            bookChaptersSequence = bookChaptersSequence
        )
        _uploading.value =
            UiState.Success(data = if (updatingBook is UiState.Success) true else false)
        return if (updatingBook is UiState.Success) updatingBook else UiState.Error()
    }

    suspend fun refreshBook() {
        loadBook()
        loadAllGenres()
        loadGenres()
        loadChapters()
        _uploading.value = UiState.Success(data = true)
    }

    fun getBookId(): Int {
        return bookId
    }

    private suspend fun loadBook() {
        try {
            _bookUiState.value = UiState.Loading()
            val response = booksRepository.getBook(bookId = bookId)
            _bookUiState.value = response
        } catch (e: IllegalArgumentException) {
            _bookUiState.value = UiState.Error(
                message = e.localizedMessage,
                typeError = "Network",
                statusCode = 500
            ) // Устанавливаем ошибочное состояние
        }
    }

    private suspend fun loadGenres() {
        try {
            _bookGenresUiState.value = UiState.Loading()
            val response = genresRepository.getBookGenres(bookId = bookId)
            _bookGenresUiState.value = response
        } catch (e: IllegalArgumentException) {
            _bookGenresUiState.value = UiState.Error(
                message = e.localizedMessage,
                typeError = "Network",
                statusCode = 500
            ) // Устанавливаем ошибочное состояние
        }
    }

    private suspend fun loadChapters() {
        _bookChaptersUiState.value = UiState.Loading()
        val response = loadWithState { chaptersRepository.getBookChapters(bookId = bookId) }
        _bookChaptersUiState.value = response
    }

    private suspend fun loadAllGenres() {
        _allGenresUiState.value = UiState.Loading()
        val response = loadWithState { genresRepository.getAllGenres() }
        _allGenresUiState.value = response
    }
}