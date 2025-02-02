package com.example.comicsappmobile.data.api

import com.example.comicsappmobile.data.dto.entities.GenreDto
import com.example.comicsappmobile.data.dto.response.ResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface GenresApi {

    @GET("/api/v2/genres")
    suspend fun getAllGenres(
    ): Response<ResponseDto<List<GenreDto>>>

    @GET("/api/v2/books/{bookId}/genres")
    suspend fun getBookGenres(
        @Path("bookId") bookId: Int
    ): Response<ResponseDto<List<GenreDto>>>

}