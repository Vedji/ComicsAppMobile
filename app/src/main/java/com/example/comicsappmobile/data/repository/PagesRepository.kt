package com.example.comicsappmobile.data.repository

import com.example.comicsappmobile.data.api.PagesApi
import com.example.comicsappmobile.data.mapper.PageMapper
import com.example.comicsappmobile.data.dto.StateResponseDto
import com.example.comicsappmobile.data.dto.entities.PageDto
import com.example.comicsappmobile.data.dto.entities.metadata.Pagination
import com.example.comicsappmobile.data.dto.response.data.ErrorDataDto
import com.example.comicsappmobile.utils.Logger
import com.example.comicsappmobile.ui.presentation.viewmodel.UiState
import com.example.comicsappmobile.ui.presentation.model.PageUiModel

class PagesRepository (
    private val pagesApi: PagesApi
): BaseRepository() {

    suspend fun getChapterPages(bookId: Int, chapterId: Int): UiState<List<PageUiModel>> {
        val test = safeApiCall { pagesApi.getChapterPages(bookId, chapterId) }
        return when (test) {
            is StateResponseDto.Success -> {
                // val t: List<PageDto> = test.getData()
                val list: List<PageDto> = (test.data as? List<PageDto>) ?: emptyList()
                val metadata: Pagination? =
                    if (test.metadata is Pagination)
                        test.metadata
                else
                    null
                Logger.debug("getChapterPages", "list = ${list.toString()}")
                Logger.debug("getChapterPages", "metadata = ${metadata.toString()}")
                UiState.Success(data = list.map { PageMapper.toUiModel(it) }, metadata = metadata)
                // UiState.Loading()
            }

            is StateResponseDto.Error -> {
                val data = (test.data as? ErrorDataDto)
                Logger.debug("return when(test)", "data = '${test}'")
                UiState.Error(
                    data = emptyList(),
                    message = data?.message ?: "GenresRepository -> getBookGenres",
                    typeError = data?.typeError ?: "GenresRepository -> getBookGenres",
                    statusCode = -1
                )
            }
        }
    }
}