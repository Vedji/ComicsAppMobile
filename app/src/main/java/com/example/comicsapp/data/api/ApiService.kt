package com.example.comicsapp.data.api

import com.example.comicsapp.data.model.api.ApiAuthorizationUser
import com.example.comicsapp.data.model.api.books.BookModel
import com.example.comicsapp.data.model.api.books.chapters.pages.ApiBookChapterPage
import com.example.comicsapp.data.model.api.books.chapters.pages.ApiBookChapterPages
import com.example.comicsapp.data.model.api.books.chapters.ApiBookChapters
import com.example.comicsapp.data.model.api.books.comments.ApiBookCommentsList
import com.example.comicsapp.data.model.api.books.users.ApiBookList
import com.example.comicsapp.data.model.api.books.genres.ApiCatalogBookGenres
import com.example.comicsapp.data.model.api.books.chapters.ApiChapter
import com.example.comicsapp.data.model.api.books.users.ApiGetIsUserFavoriteBook
import com.example.comicsapp.data.model.api.ApiUser
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("/api/v1/user/{userID}/info")
    suspend fun getInfoAboutUser(
        @Path("userID") userID: Int,
    ): ApiUser

    @GET("/api/v1/books/{bookID}/info")
    suspend fun getInfoAboutBookByID(
        @Path("bookID") bookID: Int,
    ): BookModel

    @GET("/api/v1/books/catalog")
    suspend fun getCatalogBooks(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("search") search: String,
        @Query("sortBy") sortBy: String,
        @Query("genreID") genreIDs: List<Int>
    ): ApiBookList

    @GET("/api/v1/books/{bookID}/getGenres")
    suspend fun getBookGenres(
        @Path("bookID") bookID: Int,
    ): ApiCatalogBookGenres

    @GET("/api/v1/books/{bookID}/chapters/{chapterID}")
    suspend fun getChapterInfo(
        @Path("bookID") bookID: Int,
        @Path("chapterID") chapterID: Int
    ): ApiChapter

    @GET("/api/v1/books/{bookID}/chapters")
    suspend fun getBookChaptersList(
        @Path("bookID") bookID: Int
    ): ApiBookChapters

    @GET("/api/v1/books/{bookID}/chapters/{chapterID}/pages/{pageID}")
    suspend fun getBookChapterPage(
        @Path("bookID") bookID: Int,
        @Path("chapterID") chapterID: Int,
        @Path("pageID") pageID: Int
    ): ApiBookChapterPage

    @GET("/api/v1/books/{bookID}/chapters/{chapterID}/pages")
    suspend fun getBookChapterPages(
        @Path("bookID") bookID: Int,
        @Path("chapterID") chapterID: Int
    ): ApiBookChapterPages

    @GET("/api/v1/book_comments/{bookID}")
    suspend fun getBookCommentsList(
        @Path("bookID") bookID: Int,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): ApiBookCommentsList

    @FormUrlEncoded
    @POST("/api/v1/user/login")
    suspend fun getToken(
        @Field("email") email: String? = null,
        @Field("username") username: String? = null,
        @Field("password") password: String
    ): ApiAuthorizationUser

    @POST("/api/v1/book/{bookID}/favorites") // Замените "endpoint" на ваш API URL

    suspend fun getIsBookInUserFavoriteList(
        @Header("Authorization") token: String, // Токен для авторизации
        @Path("bookID") bookID: Int,

    ): ApiGetIsUserFavoriteBook // или другой тип ответа

}