package com.example.comicsapp.data.model.api.books.users

import com.example.comicsapp.data.model.api.books.BookModel
import com.google.gson.annotations.SerializedName

data class ApiBookList (
    @SerializedName("pageItems") val pageItems: List<BookModel>,
    @SerializedName("perPage") val perPage: Int,
    @SerializedName("page") val page: Int,
    @SerializedName("totalCount") val totalCount: Int
)