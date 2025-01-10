package com.example.comicsapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.comicsapp.utils.network.RetrofitInstance
import com.example.comicsapp.data.model.api.books.BookModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class BookCatalogViewModel : ViewModel() {
    // StateFlow для хранения списка пользователей
    private val _bookList = MutableStateFlow<List<BookModel>>(emptyList())
    val bookList: StateFlow<List<BookModel>> = _bookList

    private var totalCount = 0
    private var booksListSize = 0
    private var page = 0
    private val limit = 10


    private var sortBy: String = "addedASC"
    private var searchInCatalog: String = ""
    private var genreFilter: List<Int> = emptyList()

    init { loadBooks() }

    fun setFilterParameters(searchInCatalog: String = "", genreFilter: List<Int> = emptyList(), sortBy: String = "addedASC"){
        this.searchInCatalog = searchInCatalog
        this.genreFilter = genreFilter
        this.sortBy = sortBy

        // clear list
        _bookList.value = emptyList()
        totalCount = 0
        page = 0
        booksListSize = 0

    }


    // Загрузка списка книг
    fun loadBooks() {
        Log.d("loadBooks page", page.toString())
        viewModelScope.launch {
            try {
                val addedBookCatalogList = RetrofitInstance.api.getCatalogBooks(
                    limit,
                    limit * page,
                    searchInCatalog,
                    sortBy,
                    genreFilter
                )
                addBooks(addedBookCatalogList.pageItems)
                page += 1
                totalCount = addedBookCatalogList.totalCount
                booksListSize += addedBookCatalogList.perPage
            } catch (e: Exception) {
                // Обработка ошибок
                e.printStackTrace()
            }
        }
    }

    // Добавление книг к списку существующих книг
    private fun addBooks(booksList: List<BookModel>) {
        for (book in booksList)
            _bookList.value += book
    }

}