package com.example.comicsappmobile.ui.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.comicsappmobile.data.repository.BooksRepository
import com.example.comicsappmobile.data.repository.FavoriteRepository
import com.example.comicsappmobile.data.repository.GenresRepository
import com.example.comicsappmobile.ui.presentation.model.BookUiModel
import com.example.comicsappmobile.ui.presentation.model.GenreUiModel
import com.example.comicsappmobile.utils.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CatalogViewModel(
    private val booksRepository: BooksRepository,
    private val genresRepository: GenresRepository,
    private val favoriteRepository: FavoriteRepository
): BaseViewModel() {

    private val sortedAccess: List<String> = listOf(
        "addedASC",
        "ratingDESC",
        "ratingASC",
        "titleASC",
        "titleDESC",
        "publishedASC",
        "publishedDESC",
        "addedDESC"
    )

    private var limit: Int = 10
    private var offset: Int = 0
    private var search: String = ""
    private var hasNext: Boolean = true


    private val _selectedSorted = MutableStateFlow( sortedAccess[0] )
    val selectedSorted: StateFlow<String> = _selectedSorted

    private val _selectedGenresIds = MutableStateFlow(emptyList<Int>())
    val selectedGenresIds: StateFlow<List<Int>> = _selectedGenresIds

    private val _booksUiState = MutableStateFlow<UiState<List<BookUiModel>>>(UiState.Loading())
    val booksUiState: StateFlow<UiState<List<BookUiModel>>> = _booksUiState

    private val _allGenresUi = MutableStateFlow<UiState<List<GenreUiModel>>>(UiState.Loading())
    val allGenresUi: StateFlow<UiState<List<GenreUiModel>>> = _allGenresUi

    init {
        viewModelScope.launch {
            loadCatalogBooks(true)
            val value = favoriteRepository.fetchBookInFavorite(bookId = 3)
            Logger.debug("test Favorite", "viewModel = $value, message = ${value.message}")
        }
        loadAllGenres()
    }

    fun getSortedAccessValues(): List<String>{
        return sortedAccess
    }

    fun getCurrentSelectedGenres(): List<Int>{
        return _selectedGenresIds.value
    }

    fun removeParameters(){
        viewModelScope.launch {
            removeFilters()
        }
    }

    suspend fun removeFilters(){
        search = ""
        _selectedGenresIds.value = emptyList()
        _selectedSorted.value = sortedAccess[0]
        limit = 10
        offset = 0
        hasNext = true
        _booksUiState.value = UiState.Loading()
        loadCatalogBooks(true)
    }

    fun setParameters(newSortBy: String, newGenres: List<Int>): Boolean{
        _selectedGenresIds.value = newGenres
        _selectedSorted.value = newSortBy
        limit = 10
        offset = 0
        hasNext = true
        _booksUiState.value = UiState.Loading()
        viewModelScope.launch {
            loadCatalogBooks(true)
        }
        return true
    }

    fun setSortedBy(newSortBy: String): Boolean{
        if (newSortBy !in sortedAccess){
            return false
        }
        _selectedSorted.value = newSortBy
        limit = 10
        offset = 0
        hasNext = true
        _booksUiState.value = UiState.Loading()
        viewModelScope.launch {
            loadCatalogBooks(true)
        }
        return true
    }

    fun setFilters(genres: List<Int>): Boolean{
        // val GenresUid = _allGenresUi.value.data?.map { it.genreId } ?: emptyList()
        // if (genres.any {it !in GenresUid})
        //     return false
        // val frequencyMap = genres.groupingBy { it }.eachCount()
        // val duplicates = frequencyMap.filter { it.value > 1 }
        // if (duplicates.isNotEmpty())
        //     return false
        _selectedGenresIds.value = genres
        limit = 10
        offset = 0
        hasNext = true
        _booksUiState.value = UiState.Loading()
        viewModelScope.launch {
            loadCatalogBooks(true)
        }
        return true
    }

    fun setSearch(newSearchArg: String){
        if (newSearchArg == search)
            return
        search = newSearchArg
        limit = 10
        offset = 0
        hasNext = true
        _booksUiState.value = UiState.Loading()
        viewModelScope.launch {
            loadCatalogBooks(true)
        }
    }

    private fun loadAllGenres(){
        viewModelScope.launch {
            _allGenresUi.value = UiState.Loading()
            _allGenresUi.value = loadWithState { genresRepository.getAllGenres() }
            Logger.debug("CatalogScreen -> loadAllGenres", _allGenresUi.value.data.toString())
        }
    }

    suspend fun loadCatalogBooks(first: Boolean = false) {
        if (hasNext && _booksUiState.value is UiState.Success || first) {

            val books = _booksUiState.value.data as? List<BookUiModel> ?: emptyList()
            Logger.debug("loadCatalogBooks", "books -> ${books}")
            if (first)
                _booksUiState.value = UiState.Loading()
            else
                _booksUiState.value = UiState.Loading(data = books)
            Logger.debug("CatalogScreen -> ", "${_booksUiState.value}")
            val response = loadWithState({
                booksRepository.getBooks(
                    limit = limit,
                    offset = offset,
                    search = search,
                    sortBy = _selectedSorted.value,
                    genresId = _selectedGenresIds.value
                )
            })
            val resp_books: List<BookUiModel> = when (response) {
                is UiState.Success -> (
                        if (response.data != null && response.data is List<BookUiModel>)
                            response.data
                        else
                            emptyList<BookUiModel>())!!

                else -> emptyList<BookUiModel>()
            }
            if (response is UiState.Success) {
                _booksUiState.value =
                    UiState.Success(data = books + resp_books, metadata = response.metadata)
                if (response.metadata != null) {
                    limit = response.metadata.limit
                    offset = response.metadata.offset + limit
                    hasNext = response.metadata.hasNext
                }
            }

            Logger.debug(
                "CatalogScreen -> loadCatalogBooks",
                _booksUiState.value.data?.size.toString()
            )
            Logger.debug(
                "CatalogScreen -> loadCatalogBooks",
                _booksUiState.value.metadata.toString()
            )
        }
    }
}