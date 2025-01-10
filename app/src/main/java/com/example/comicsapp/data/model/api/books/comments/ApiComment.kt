package com.example.comicsapp.data.model.api.books.comments

import com.google.gson.annotations.SerializedName


data class ApiComment (
    @SerializedName("bookID") val bookID: Int,
    @SerializedName("commentID") val commentID: Int,
    @SerializedName("userID") val userID: Int,
    @SerializedName("comment") val comment: String,
    @SerializedName("rating") val rating: Int,
    @SerializedName("uploadDate") val uploadDate: String,
)