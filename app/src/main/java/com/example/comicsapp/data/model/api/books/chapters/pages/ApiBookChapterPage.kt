package com.example.comicsapp.data.model.api.books.chapters.pages

import com.google.gson.annotations.SerializedName


data class ApiBookChapterPage (
    @SerializedName("pageID") val pageID: Int,
    @SerializedName("chapterID") val chapterID: Int,
    @SerializedName("pageNumber") val pageNumber: Int,
    @SerializedName("pageImageID") val pageImageID: Int,
    @SerializedName("addedBy") val addedBy: Int,
    @SerializedName("uploadDate") val uploadDate: String,
)