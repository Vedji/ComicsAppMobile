package com.example.comicsappmobile.data.dto.entities

import com.google.gson.annotations.SerializedName


class ChapterDto(
    @SerializedName("bookID")
    val bookId: Int = -1,

    @SerializedName("chapterID")
    val chapterId: Int = -1,

    @SerializedName("chapterTitle")
    val chapterTitle: String = "Нет названия",

    @SerializedName("chapterNumber")
    val chapterNumber: Int = -1,

    @SerializedName("chapterLength")
    val chapterLength: Int = 0,

    @SerializedName("addedBy")
    val addedBy: Int = -1,

    @SerializedName("uploadDate")
    val uploadDate: String = " - "
) {
    override fun toString(): String {
        return "<ChapterDto(chapterId = '$chapterId', chapterTitle = $chapterTitle)>"
    }
}