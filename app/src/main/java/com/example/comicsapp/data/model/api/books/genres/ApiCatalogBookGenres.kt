package com.example.comicsapp.data.model.api.books.genres

import com.google.gson.annotations.SerializedName

data class ApiCatalogBookGenres (
    @SerializedName("bookID") val bookID: Int,
    @SerializedName("genreList") val genreList: List<String>
)