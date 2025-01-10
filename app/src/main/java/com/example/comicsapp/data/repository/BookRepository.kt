package com.example.comicsapp.data.repository

import com.example.comicsapp.data.api.BookEndpoints
import com.example.comicsapp.data.model.api.books.BookModel
import com.example.comicsapp.data.model.api.response.ApiBody
import com.example.comicsapp.data.model.api.response.ApiResponse
import javax.inject.Inject

class BookRepository @Inject constructor(
    private val bookEndpoints: BookEndpoints
) : BaseRepository() {

    suspend fun fetchBook(bookID: Int): ApiResponse<ApiBody<BookModel>> {
        return safeApiCall { bookEndpoints.getBook(bookID) }
    }

    suspend fun fetchGenres(bookID: Int): ApiResponse<ApiBody<BookModel>> {
        return safeApiCall { bookEndpoints.getBook(bookID) }
    }

    suspend fun fetchChapters(bookID: Int): ApiResponse<ApiBody<BookModel>> {
        return safeApiCall { bookEndpoints.getBook(bookID) }
    }

    suspend fun fetchChapter(bookID: Int): ApiResponse<ApiBody<BookModel>> {
        return safeApiCall { bookEndpoints.getBook(bookID) }
    }

    suspend fun fetchPages(bookID: Int): ApiResponse<ApiBody<BookModel>> {
        return safeApiCall { bookEndpoints.getBook(bookID) }
    }

    suspend fun fetchPage(bookID: Int): ApiResponse<ApiBody<BookModel>> {
        return safeApiCall { bookEndpoints.getBook(bookID) }
    }

    suspend fun fetchComments(bookID: Int): ApiResponse<ApiBody<BookModel>> {
        return safeApiCall { bookEndpoints.getBook(bookID) }
    }
}