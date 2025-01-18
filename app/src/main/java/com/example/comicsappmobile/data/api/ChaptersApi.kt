package com.example.comicsappmobile.data.api

import com.example.comicsappmobile.data.dto.entities.BookDto
import com.example.comicsappmobile.data.dto.entities.ChapterDto
import com.example.comicsappmobile.data.dto.response.ResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


interface ChaptersApi {

    @GET("/api/v2/books/{bookId}/chapters")
    suspend fun getBookChapters(
        @Path("bookId") bookId: Int
    ): Response<ResponseDto<List<ChapterDto>>>

    @GET("/api/v2/books/{bookId}/chapters/{chapterId}")
    suspend fun getBookChapter(
        @Path("bookId") bookId: Int,
        @Path("chapterId") chapterId: Int
    ): Response<ResponseDto<BookDto>>
}