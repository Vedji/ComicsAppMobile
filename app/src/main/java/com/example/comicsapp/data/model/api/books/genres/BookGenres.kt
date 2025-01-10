package com.example.comicsapp.data.model.api.books.genres

import com.google.gson.annotations.SerializedName

data class BookGenres (
    @SerializedName("bookID") val genreName: Int,
    @SerializedName("genreID") val genreID: Int
)