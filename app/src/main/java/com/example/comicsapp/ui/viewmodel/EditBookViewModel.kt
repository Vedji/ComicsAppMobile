package com.example.comicsapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.comicsapp.domain.model.DomainError
import com.example.comicsapp.domain.model.books.book.Genre
import com.example.comicsapp.data.model.api.books.BookModel
import com.example.comicsapp.data.model.api.response.ApiResponse
import com.example.comicsapp.data.repository.GenresRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class EditBookViewModel(val appViewModel: AppViewModel) : ViewModel() {

    // Репозитории
    private val genresRepository = GenresRepository()

    // Свойства
    private val _book = MutableStateFlow<BookModel?>(null)
    val book: StateFlow<BookModel?> get() = _book

    private val _allGenres = MutableStateFlow<List<Genre>>(emptyList())
    val allGenres: StateFlow<List<Genre>> get() = _allGenres

    private val _bookGenres = MutableStateFlow<List<Genre>>(emptyList())
    val bookGenres: StateFlow<List<Genre>> get() = _bookGenres

    private val _editableBookGenres = MutableStateFlow<List<Genre>>( emptyList() )
    val editableBookGenres: StateFlow<List<Genre>> get() = _editableBookGenres


    private var _error = MutableStateFlow<DomainError?>( null )
    val error: StateFlow<DomainError?> get() = _error

    fun setBook(newBook: BookModel?) {
        Log.d("fetchGenres", "Testing...")
        clear()
        _book.value = newBook

        if (_book.value != null)
            loadBookGenres(_book.value!!.bookID ?: 0)
        loadAllGenres()
    }

    fun clear(){
        _error.value = null
        _book.value = null
        _allGenres.value = emptyList()
        _editableBookGenres.value = emptyList()
        _bookGenres.value = emptyList()
    }

    fun setError(err: DomainError?){
        _error.value = err
    }

    fun setGenre(newGenre: Genre){
        if (_editableBookGenres.value.none { it.genreID == newGenre.genreID }) {
            val updatedList = _editableBookGenres.value.toMutableList().apply { add(newGenre) }
            _editableBookGenres.value = updatedList
        }
        // _editableBookGenres.value.add(newGenre)
        Log.d("moveGenre", "setGenre ${_editableBookGenres.value.map { it.genreName }}")
        Log.d("moveGenre", "setGenre ${_editableBookGenres.value.size}")
    }


    fun moveGenre(newGenre: Genre){
        if (_editableBookGenres.value.none { it.genreID == newGenre.genreID })
            _editableBookGenres.value += newGenre
        else{
            val updatedList = _editableBookGenres.value.toMutableList().apply { remove(newGenre) }
            _editableBookGenres.value = updatedList
        }
        // _editableBookGenres.value.add(newGenre)
        Log.d("moveGenre", "moveGenre ${_editableBookGenres.value.map { it.genreName }}")
        Log.d("moveGenre", "moveGenre ${_editableBookGenres.value.size}")
    }

    fun setBookGenres(newGenres: List<Genre>) {
        _bookGenres.value = newGenres
        _editableBookGenres.value = newGenres
    }

    fun setAllGenres(newGenres: List<Genre>) {
        _allGenres.value = newGenres
    }

    fun rollback(){
        _allGenres.value = _allGenres.value
        _editableBookGenres.value = _bookGenres.value
        Log.d("moveGenre", "moveGenre ${_editableBookGenres.value.map { it.genreName }}")
        Log.d("moveGenre", "moveGenre ${_editableBookGenres.value.size}")
    }


    private fun loadBookGenres(bookID: Int) {
        viewModelScope.launch {
            Log.d("fetchGenres", "Testing...")
            when (val response = genresRepository.fetchBookGenres(bookID)) {
                is ApiResponse.Success -> {
                    // Успешный ответ
                    val data = response.data

                    val genres = response.data.data.map { it.toDomain() }
                    setBookGenres( genres )
                }
                is ApiResponse.Error -> {
                    appViewModel.setApiErrorFromApi(response.data.data)
                }
            }
        }
    }

    private fun loadAllGenres() {
        viewModelScope.launch {
            Log.d("fetchGenres", "Testing...")
            when (val response = genresRepository.fetchAllGenres()) {
                is ApiResponse.Success -> {
                    // Успешный ответ
                    val genres = response.data.data.map { it.toDomain() }
                    setAllGenres( genres )
                }
                is ApiResponse.Error -> {
                    appViewModel.setApiErrorFromApi(response.data.data)
                    Log.d("fetchGenres", "Testing... ${response.data.data.typeError}")

                }
            }
        }
    }

}