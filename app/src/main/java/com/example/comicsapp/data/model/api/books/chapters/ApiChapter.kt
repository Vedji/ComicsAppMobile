package com.example.comicsapp.data.model.api.books.chapters

import com.google.gson.annotations.SerializedName

data class ApiChapter (
    @SerializedName("chapterID") val chapterID: Int,
    @SerializedName("bookID") val bookID: Int,
    @SerializedName("chapterTitle") val chapterTitle: String,
    @SerializedName("chapterNumber") val chapterNumber: Int,
    @SerializedName("chapterLength") val chapterLength: Int,
    @SerializedName("addedBy") val addedBy: Int,
    @SerializedName("uploadDate") val uploadDate: String
    )