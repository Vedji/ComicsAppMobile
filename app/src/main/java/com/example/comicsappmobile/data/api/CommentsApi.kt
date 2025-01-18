package com.example.comicsappmobile.data.api

import com.example.comicsappmobile.data.dto.entities.CommentDto
import com.example.comicsappmobile.data.dto.response.ResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface CommentsApi {

    @GET("/api/v2/book_comments/{bookId}")
    suspend fun getBookComments(
        @Path("bookId") bookId: Int,
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 0
    ): Response<ResponseDto<List<CommentDto>>>

    // @GET("/api/v2/books/{bookId}/chapters/{chapterId}")
    // suspend fun getBookChapter(
    //     @Path("bookId") bookId: Int,
    //     @Path("chapterId") chapterId: Int
    // ): Response<ResponseDto<BookDto>>
}