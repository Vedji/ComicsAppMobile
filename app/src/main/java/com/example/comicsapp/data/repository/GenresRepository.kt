package com.example.comicsapp.data.repository

import com.example.comicsapp.utils.network.RetrofitInstance
import com.example.comicsapp.data.model.api.books.genres.ApiGenre
import com.example.comicsapp.data.model.api.response.ApiBody
import com.example.comicsapp.data.model.api.response.ApiResponse

class GenresRepository: BaseRepository() {

    suspend fun fetchBookGenres(bookID: Int): ApiResponse<ApiBody<List<ApiGenre>>>  {
        return safeApiCall { RetrofitInstance.genresApi.getBookGenres(bookID) }
    }

    suspend fun fetchAllGenres(): ApiResponse<ApiBody<List<ApiGenre>>>  {
        return safeApiCall { RetrofitInstance.genresApi.getAllGenres() }
    }

}