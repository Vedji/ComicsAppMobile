package com.example.comicsapp.data.model.api.books.chapters.pages

import com.google.gson.annotations.SerializedName


data class ApiBookChapterPages (
    @SerializedName("pagesList") val pagesList: List<ApiBookChapterPage>,
    @SerializedName("chapterLength") val chapterLength: Int,
)