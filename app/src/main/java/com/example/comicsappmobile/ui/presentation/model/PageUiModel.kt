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
}