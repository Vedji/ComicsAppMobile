package com.example.comicsappmobile.data.mapper

import com.example.comicsappmobile.data.dto.entities.FavoriteDto
import com.example.comicsappmobile.ui.presentation.model.FavoriteUiModel

object FavoriteMapper {
    fun toUiModel(favoriteDto: FavoriteDto): FavoriteUiModel{
        return FavoriteUiModel(
        bookId = favoriteDto.bookId,
        chapterId = favoriteDto.chapterId,
        favoriteId = favoriteDto.favoriteId,
        userId = favoriteDto.userId,
        uploadAt = favoriteDto.uploadAt
        )
    }

    fun toListUiModel(favoriteList: List<FavoriteDto>): List<FavoriteUiModel>{
        return favoriteList.map { toUiModel(it) }
    }

}