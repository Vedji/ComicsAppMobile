package com.example.comicsappmobile.data.api

import com.example.comicsappmobile.data.dto.entities.FavoriteDto
import com.example.comicsappmobile.data.dto.entities.joined.UserFavoriteListDto
import com.example.comicsappmobile.data.dto.response.ResponseDto
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface FavoriteApi {



    @GET("/api/v2/user/book/{bookId}/favorites")
    suspend fun getBookInFavorite(
        @Header("Authorization") token: String,
        @Path("bookId") bookId: Int
    ): Response<ResponseDto<FavoriteDto>>

    @PUT("/api/v2/user/book/{bookId}/favorites")
    suspend fun setBookInFavorite(
        @Header("Authorization") token: String,
        @Path("bookId") bookId: Int,
        @Query("chapterId") chapterId: Int? = null
    ): Response<ResponseDto<FavoriteDto>>

    @DELETE("/api/v2/user/book/favorites/{favoriteId}")
    suspend fun removeBookFromFavorite(
        @Header("Authorization") token: String,
        @Path("favoriteId") favoriteId: Int
    ): Response<ResponseDto<FavoriteDto>>

}