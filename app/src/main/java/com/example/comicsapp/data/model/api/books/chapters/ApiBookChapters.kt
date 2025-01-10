package com.example.comicsapp.data.model.api.books.chapters

import com.google.gson.annotations.SerializedName

data class ApiBookChapters (
    @SerializedName("chaptersList") val chaptersList: List<ApiChapter>,
    @SerializedName("count") val count: Int,
    @SerializedName("page") val page: Int
)