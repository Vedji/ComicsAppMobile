package com.example.comicsappmobile.data.mapper

import com.example.comicsappmobile.data.dto.entities.ChapterDto
import com.example.comicsappmobile.ui.presentation.model.ChapterUiModel


object ChapterMapper{

    fun map(apiModel: ChapterDto): ChapterUiModel {
        return ChapterUiModel(
            bookId = apiModel.bookId,
            chapterId = apiModel.chapterId,
            chapterTitle = apiModel.chapterTitle,
            chapterNumber = apiModel.chapterNumber,
            chapterLength = apiModel.chapterLength,
            addedBy = apiModel.addedBy,
            uploadDate = apiModel.uploadDate
        )
    }

    fun mapList(apiModels: List<ChapterDto>): List<ChapterUiModel> {
        return apiModels.map { map(it) }
    }

}