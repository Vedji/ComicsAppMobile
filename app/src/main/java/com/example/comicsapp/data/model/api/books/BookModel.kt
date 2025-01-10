package com.example.comicsapp.data.model.api.books

import com.google.gson.annotations.SerializedName

data class BookModel (
    @SerializedName("bookID") val bookID: Int,
    @SerializedName("bookAddedBy") val bookAddedBy: Int,
    @SerializedName("bookTitle") val bookTitle: String,
    @SerializedName("bookTitleImage") val bookTitleImage: Int,
    @SerializedName("bookRating") val bookRating: Float,
    @SerializedName("bookDatePublication") val bookDatePublication: String,
    @SerializedName("bookDescription") val bookDescription: String,
    @SerializedName("bookISBN") val bookISBN: String,
    @SerializedName("uploadDate") val uploadDate: String
)