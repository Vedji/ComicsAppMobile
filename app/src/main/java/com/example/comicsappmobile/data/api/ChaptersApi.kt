package com.example.comicsappmobile.data.api

import com.example.comicsappmobile.data.dto.entities.BookDto
import com.example.comicsappmobile.data.dto.entities.ChapterDto
import com.example.comicsappmobile.data.dto.response.ResponseDto
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT
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

    @FormUrlEncoded
    @PUT("/api/v2/books/{bookId}/chapters/{chapterId}")
    suspend fun uploadChapters(
        @Header("Authorization") token: String,

        @Path("bookId") bookId: Int,
        @Path("chapterId") chapterId: Int,

        @Field("chapterTitle") chapterTitle: String,
        @Field("chapterPagesIds") chapterPagesIds: List<Int>,
        @Field("chapterPagesImageIds") chapterPagesImageIds: List<Int>,
    ): Response<ResponseDto<ChapterDto>>

    @DELETE("/api/v2/books/{bookId}/chapters/{chapterId}")
    suspend fun deleteChapter(
        @Header("Authorization") token: String,

        @Path("bookId") bookId: Int,
        @Path("chapterId") chapterId: Int,
    ): Response<ResponseDto<ChapterDto>>

}