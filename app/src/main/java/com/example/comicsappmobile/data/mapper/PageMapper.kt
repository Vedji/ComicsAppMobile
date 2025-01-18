package com.example.comicsappmobile.data.mapper

import com.example.comicsappmobile.data.dto.entities.PageDto
import com.example.comicsappmobile.ui.presentation.model.PageUiModel

object PageMapper{

    fun toUiModel(apiModel: PageDto): PageUiModel {
        return PageUiModel(
            chapterId = apiModel.chapterId,
            pageId = apiModel.pageId,
            pageNumber = apiModel.pageNumber,
            pageImageId = apiModel.pageImageId,
            addedBy = apiModel.addedBy,
            uploadDate = apiModel.uploadDate,
        )
    }

    fun toDtoModel(apiModel: PageUiModel): PageDto {
        return PageDto(
            chapterId = apiModel.chapterId,
            pageId = apiModel.pageId,
            pageNumber = apiModel.pageNumber,
            pageImageId = apiModel.pageImageId,
            addedBy = apiModel.addedBy,
            uploadDate = apiModel.uploadDate,
        )
    }

}