package com.example.comicsapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.comicsapp.utils.network.RetrofitInstance
import com.example.comicsapp.data.model.api.books.BookModel
import com.example.comicsapp.data.model.api.books.chapters.pages.ApiBookChapterPage
import com.example.comicsapp.data.model.api.books.chapters.ApiChapter
import com.example.comicsapp.data.model.api.books.comments.ApiComment
import com.example.comicsapp.data.model.api.ApiUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SelectedBookViewModel(val appViewModel: AppViewModel) : ViewModel()  {

    private val _book = MutableStateFlow<BookModel?>(null)
    val book: StateFlow<BookModel?> get() = _book

    private val _bookChaptersList = MutableStateFlow<List<ApiChapter>>(emptyList())
    val chaptersList: StateFlow<List<ApiChapter>> get() = _bookChaptersList

    private val _chapter = MutableStateFlow<ApiChapter?>(null)
    val chapter: StateFlow<ApiChapter?> get() = _chapter

    private val _bookPagesList = MutableStateFlow<List<ApiBookChapterPage>>(emptyList())
    val bookPagesList: StateFlow<List<ApiBookChapterPage>> get() = _bookPagesList

    private val _bookPage = MutableStateFlow<ApiBookChapterPage?>(null)
    val bookPage: StateFlow<ApiBookChapterPage?> get() = _bookPage

    private val _bookGenresList = MutableStateFlow<List<String>>(emptyList())
    val bookGenresList: StateFlow<List<String>> get() = _bookGenresList

    private val _bookCommentsList = MutableStateFlow<List<ApiComment>>(emptyList())
    val bookCommentsList: StateFlow<List<ApiComment>> get() = _bookCommentsList

    private val _bookCommentsAboutUsersList = MutableStateFlow<MutableMap<Int, ApiUser>>( mutableMapOf() )
    val bookCommentsAboutUsersList: StateFlow<MutableMap<Int, ApiUser>> get() = _bookCommentsAboutUsersList

    private var bookCommentsListTotalCount = -1
    private var bookCommentsListCurrentCount = 0
    private var bookCommentsListPage = 0
    private val bookCommentsListLimit = 10




    init { setBookById(3) }


    fun getBook(): BookModel? = book.value
    fun getBookChaptersList(): List<ApiChapter> = chaptersList.value
    fun getChapter(): ApiChapter? = chapter.value
    fun getBookPagesList(): List<ApiBookChapterPage> = bookPagesList.value
    fun getBookPage(): ApiBookChapterPage? = bookPage.value
    fun getBookGenresList(): List<String> = bookGenresList.value

    fun setBookById(bookId: Int){
        if (bookId < 0) return
        clearAllExceptBook()
        try {
            viewModelScope.launch {
                val newBook = RetrofitInstance.api.getInfoAboutBookByID(bookId)
                setBook(newBook)
            }
        } catch (e: Exception) {
            // Обработка ошибок
            e.printStackTrace()
        }
    }

    // Сеттеры
    fun setBook(newBook: BookModel) {
        Log.d("fetchGenres", "Testing...")
        _book.value = newBook
        appViewModel.editBook.setBook(newBook)
        clearAllExceptBook()

        viewModelScope.launch {
            loadChaptersFromApi(newBook.bookID)
            loadGenresFromApi(newBook.bookID)
            loadCommentsFromApi(newBook.bookID)
        }
    }

    private fun setBookChaptersList(newChaptersList: List<ApiChapter>) {
        _bookChaptersList.value = newChaptersList
    }

    fun setChapter(newChapter: ApiChapter?, last: Boolean = false) {
        _chapter.value = newChapter
        if (_book.value == null || newChapter == null) return
        viewModelScope.launch {
            loadPagesFromApi(_book.value!!.bookID, newChapter.chapterID, last)
        }
    }

    private fun setBookPagesList(newPagesList: List<ApiBookChapterPage>) {
        _bookPagesList.value = newPagesList
    }

    private fun setBookPage(newPage: ApiBookChapterPage?) {
        _bookPage.value = newPage
    }

    private fun setBookGenresList(newGenresList: List<String>) {
        _bookGenresList.value = newGenresList
    }

    private fun setBookCommentsList(commentsList: List<ApiComment>) {
        _bookCommentsList.value = commentsList
        _bookCommentsAboutUsersList.value.clear()
        if (commentsList.isEmpty()){
            bookCommentsListTotalCount = -1
            bookCommentsListPage = 0
            bookCommentsListCurrentCount = 0
        }
    }

    private fun addBookCommentsList(commentsList: List<ApiComment>){
        _bookCommentsList.value += commentsList
        viewModelScope.launch {
            for (item in commentsList){
                loadAboutUserFromComment(item.commentID, item.userID)
            }
        }
    }

    private fun clearAllExceptBook() {
        setBookChaptersList(emptyList())
        setChapter(null)
        setBookPagesList(emptyList())
        setBookPage(null)
        setBookGenresList(emptyList())
        setBookCommentsList(emptyList())
    }

    private suspend fun loadChaptersFromApi(bookId: Int) {

        setBookChaptersList(emptyList())
        setChapter(null)
        setBookPagesList(emptyList())
        setBookPage(null)

        try {
            val chaptersList = RetrofitInstance.api.getBookChaptersList(bookId)
            if (chaptersList.chaptersList.isNotEmpty()) {
                setBookChaptersList(chaptersList.chaptersList)
                setChapter(chaptersList.chaptersList.first())
            }
        } catch (e: Exception) {
            // Обработка ошибок
            e.printStackTrace()
        }
    }

    private suspend fun loadPagesFromApi(bookId: Int, chapterId: Int, last: Boolean = false) {
        setBookPagesList(emptyList())
        setBookPage(null)


        try {
            val pagesList = RetrofitInstance.api.getBookChapterPages(bookId, chapterId)
            if (pagesList.pagesList.isNotEmpty()) {
                setBookPagesList(pagesList.pagesList)
                if (last)
                    setBookPage(pagesList.pagesList.last())
                else
                    setBookPage(pagesList.pagesList.first())
            }
        } catch (e: Exception) {
            // Обработка ошибок
            e.printStackTrace()
        }
    }

    private suspend fun loadGenresFromApi(bookId: Int) {
        setBookGenresList(emptyList())
        try {
            val genresList = RetrofitInstance.api.getBookGenres(bookId)
            if (genresList.genreList.isNotEmpty()) {
                setBookGenresList(genresList.genreList)
            }
        } catch (e: Exception) {
            // Обработка ошибок
            e.printStackTrace()
        }
    }

    fun setBackPage(): Boolean  {

        val pageNumber = _bookPage.value?.pageNumber
        val chapterLength = _chapter.value?.chapterLength
        val chapterNumber = _chapter.value?.chapterNumber

        if (pageNumber == null || chapterLength == null || chapterNumber == null)
            return false

        if (pageNumber - 1 >= 0){
            val page = _bookPagesList.value.find { it.pageNumber == pageNumber -1 }
            setBookPage(page)
            return page != null
        }
        if (chapterNumber - 1 >= 0){
            val chapter = _bookChaptersList.value.find { it.chapterNumber == chapterNumber - 1 }
            Log.d("selectedBookChapter", chapter.toString())
            if (chapter != null)
                setChapter(chapter, true)
            return chapter != null
        }
        return false
    }

    fun setNextPage(): Boolean  {

        val pageNumber = _bookPage.value?.pageNumber
        val chapterLength = _chapter.value?.chapterLength
        val chapterNumber = _chapter.value?.chapterNumber
        val chaptersCount = _bookChaptersList.value.size

        if (pageNumber == null || chapterLength == null || chapterNumber == null)
            return false

        if (pageNumber + 1 < chapterLength){
            val page = _bookPagesList.value.find { it.pageNumber == pageNumber + 1 }
            setBookPage(page)
            return page != null
        }
        if (chapterNumber + 1 < chaptersCount){
            val chapter = _bookChaptersList.value.find { it.chapterNumber == chapterNumber + 1 }
            Log.d("selectedBookChapter", chapter.toString())
            if (chapter != null)
                setChapter(chapter)
            return chapter != null
        }
        return false
    }

    private suspend fun loadCommentsFromApi(bookId: Int) {

        if (bookCommentsListCurrentCount >= bookCommentsListTotalCount && bookCommentsListTotalCount != -1)
            return
        try {
            val commentsFromApi = RetrofitInstance.api.getBookCommentsList(
                bookId,
                bookCommentsListLimit,
                bookCommentsListLimit * bookCommentsListPage
            )
            if (commentsFromApi.pageItems.isNotEmpty()) {
                addBookCommentsList(commentsFromApi.pageItems)
                bookCommentsListPage += 1
                bookCommentsListCurrentCount += commentsFromApi.perPage
                bookCommentsListTotalCount = if (commentsFromApi.totalCount == 0) { -1 } else { commentsFromApi.totalCount }
            }
        } catch (e: Exception) {
            // Обработка ошибок
            e.printStackTrace()
        }
    }

    private suspend fun loadAboutUserFromComment(commentID: Int, userID: Int){
        if (_bookCommentsAboutUsersList.value.containsKey(commentID))
            return
        try {
            val userFromApi = RetrofitInstance.api.getInfoAboutUser(userID)
            _bookCommentsAboutUsersList.value[commentID] = userFromApi
        } catch (e: Exception) {
            // Обработка ошибок
            e.printStackTrace()
        }
    }

    fun loadNewComments(){
        viewModelScope.launch {
            _book.value?.let { loadCommentsFromApi(it.bookID) }
        }
    }

}