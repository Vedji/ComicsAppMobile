package com.example.comicsappmobile.data.dto.entities

import com.google.gson.annotations.SerializedName


class PageDto(
    @SerializedName("chapterID")
    val chapterId: Int = -1,

    @SerializedName("pageID")
    val pageId: Int = -1,

    @SerializedName("pageNumber")
    val pageNumber: Int = -1,

    @SerializedName("pageImageID")
    val pageImageId: Int = -1,

    @SerializedName("addedBy")
    val addedBy: Int = -1,

    @SerializedName("uploadDate")
    val uploadDate: String = "00:00:00 00-00-0000",
) {
    override fun toString(): String {
        return "<PageDto(pageId = '$pageId', pageNumber = $pageNumber)>"
    }
}