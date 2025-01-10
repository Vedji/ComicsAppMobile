package com.example.comicsapp.data.api

import com.example.comicsapp.data.model.api.books.genres.ApiGenre
import com.example.comicsapp.data.model.api.response.ApiBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface GenresApi {
    @GET("/api/v1/genre/list")
    suspend fun getAllGenres(): Response<ApiBody<List<ApiGenre>>>

    @GET("/api/v2/books/{bookID}/getGenres")
    suspend fun getBookGenres(
        @Path("bookID") bookID: Int
    ): Response<ApiBody<List<ApiGenre>>>

    @FormUrlEncoded
    @POST("your_endpoint") // Замените "your_endpoint" на реальный путь
    fun sendFormData(
        @Field("field1") field1: String,
        @Field("field2") field2: String
    ): Response<ApiBody<List<ApiGenre>>> // Замените ResponseBody на свою модель, если нужно

}