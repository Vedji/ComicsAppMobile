package com.example.comicsappmobile.data.api

import com.example.comicsappmobile.data.dto.entities.BookDto
import com.example.comicsappmobile.data.dto.entities.user.LoginUserDto
import com.example.comicsappmobile.data.dto.entities.user.UserComment
import com.example.comicsappmobile.data.dto.entities.joined.UserFavoriteListDto
import com.example.comicsappmobile.data.dto.entities.user.UserDto
import com.example.comicsappmobile.data.dto.response.ResponseDto
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface UsersApi {

    @GET("/api/v2/user/comments")
    suspend fun getUserComments(
        @Header("Authorization") token: String
    ): Response<ResponseDto<List<UserComment>>>

    @FormUrlEncoded
    @POST("/api/v2/user/login")
    suspend fun userAuthorization(
        @Field("email") email: String? = null,
        @Field("username") username: String? = null,
        @Field("password") password: String
    ): Response<ResponseDto<LoginUserDto>>

    @POST("/api/v2/user/refreshToken")
    suspend fun refreshUserAuthorization(
        @Header("Authorization") token: String
    ): Response<ResponseDto<LoginUserDto>>

    @GET("/api/v2/user/favorites")
    suspend fun getAuthUserFavoriteList(
        @Header("Authorization") token: String,
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int? = null
    ): Response<ResponseDto<List<UserFavoriteListDto>>>

    @GET("/api/v2/user/{userId}/addedBooks")
    suspend fun getBooksWhichUserAdded(
        // @Path("bookId") bookId: Int
        @Path("userId") userId: Int,
    ): Response<ResponseDto<List<BookDto>>>

    @FormUrlEncoded
    @PUT("/api/v2/user/editAboutInfo")
    suspend fun uploadInfoAboutUser(
        @Header("Authorization") token: String,
        @Field("newUserTitleImageId") newUserTitleImageId: Int,
        @Field("newUserDescription") newUserDescription: String
    ): Response<ResponseDto<UserDto>>

}