package com.example.comicsappmobile.data.api

import com.example.comicsappmobile.data.dto.entities.PageDto
import com.example.comicsappmobile.data.dto.response.ResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface PagesApi {

    @GET("/api/v2/books/{bookId}/chapters/{chapterId}/pages")
    suspend fun getChapterPages(
        @Path("bookId") bookId: Int,
        @Path("chapterId") chapterId: Int
    ): Response<ResponseDto<List<PageDto>>>

}