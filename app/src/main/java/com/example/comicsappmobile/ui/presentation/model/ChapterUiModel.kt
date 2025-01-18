package com.example.comicsappmobile.ui.presentation.model

class ChapterUiModel(
    val bookId: Int = -1,

    val chapterId: Int = -1,

    val chapterTitle: String = "Нет названия",

    val chapterNumber: Int = -1,

    val chapterLength: Int = 0,

    val addedBy: Int = -1,

    val uploadDate: String = " - "
) {
    override fun toString(): String {
        return "<ChaptersUiModel(chapterId = '$chapterId', chapterTitle = $chapterTitle)>"
    }
}