package com.example.comicsappmobile.data.dto.entities.user

import com.google.gson.annotations.SerializedName

class UserComment (
    @SerializedName("bookId") val bookId: Int,
    @SerializedName("commentId") val commentId: Int,
    @SerializedName("rating") val rating: Float,
    @SerializedName("comment") val comment: String,
    @SerializedName("uploadDate") val uploadDate: String,
)