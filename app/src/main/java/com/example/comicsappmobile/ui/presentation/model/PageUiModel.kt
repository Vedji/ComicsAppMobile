package com.example.comicsappmobile.ui.presentation.model

import com.google.gson.annotations.SerializedName

class PageUiModel(
    val chapterId: Int = -1,
    val pageId: Int = -1,
    val pageNumber: Int = -1,
    val pageImageId: Int = -1,
    val addedBy: Int = -1,
    val uploadDate: String = "00:00:00 00-00-0000",
) {
    override fun toString(): String {
        return "<PageUiModel(pageId = '$pageId', pageNumber = $pageNumber)>"
    }

    fun copy(
        chapterId: Int? = null,
        pageId: Int? = null,
        pageNumber: Int? = null,
        pageImageId: Int? = null,
        addedBy: Int? = null,
        uploadDate: String? = null,
        ): PageUiModel{
        return PageUiModel(
            chapterId = chapterId ?: this.chapterId,
            pageId = pageId ?: this.pageId,
            pageNumber = pageNumber ?: this.pageNumber,
            pageImageId = pageImageId ?: this.pageImageId,
            addedBy = addedBy ?: this.addedBy,
            uploadDate = uploadDate ?: this.uploadDate
        )
    }
}