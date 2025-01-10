package com.example.comicsapp.data.model.api.books.comments

import com.google.gson.annotations.SerializedName


data class ApiBookCommentsList (
    @SerializedName("pageItems") val pageItems: List<ApiComment>,
    @SerializedName("page") val page: Int,
    @SerializedName("perPage") val perPage: Int,
    @SerializedName("totalCount") val totalCount: Int
)