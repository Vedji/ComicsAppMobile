package com.example.comicsappmobile.data.mapper

import com.example.comicsappmobile.data.dto.entities.GenreDto
import com.example.comicsappmobile.utils.Logger
import com.example.comicsappmobile.ui.presentation.model.GenreUiModel


object GenreMapper{

    // fun map(uiModel: GenreUiModel): GenreDto {
    //     return GenreDto(
    //         genreId = uiModel.genreId,
    //         genreName = uiModel.genreName
    //     )
    // }

    fun map(apiModel: GenreDto): GenreUiModel {
        return GenreUiModel(
            genreId = apiModel.genreId,
            genreName = apiModel.genreName
        )
    }

    fun mapList(apiModels: List<GenreDto>): List<GenreUiModel> {
        Logger.debug("TmapListe", apiModels.toString())
        return apiModels.map { map(it) }
    }

}