package com.example.comicsappmobile.data.api

import com.example.comicsappmobile.data.dto.entities.BookDto
import com.example.comicsappmobile.data.dto.response.ResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BooksApi {

    @GET("/api/v2/books/{bookId}")
    suspend fun getBook(
        @Path("bookId") bookId: Int
    ): Response<ResponseDto<BookDto>>

    @GET("/api/v2/books")
    suspend fun getBooks(
        // @Path("bookId") bookId: Int
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 0,
        @Query("search") search: String = "",
        @Query("sortBy") sortBy: String = "ratingDESC",
        @Query("genreId") genresId: List<Int> = emptyList()
    ): Response<ResponseDto<List<BookDto>>>

}