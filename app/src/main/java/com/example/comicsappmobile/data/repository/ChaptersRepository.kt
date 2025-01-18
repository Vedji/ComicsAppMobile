package com.example.comicsappmobile.data.repository

import com.example.comicsappmobile.data.api.ChaptersApi
import com.example.comicsappmobile.data.mapper.ChapterMapper
import com.example.comicsappmobile.data.dto.StateResponseDto
import com.example.comicsappmobile.data.dto.entities.ChapterDto
import com.example.comicsappmobile.data.dto.response.data.ErrorDataDto
import com.example.comicsappmobile.ui.presentation.viewmodel.UiState
import com.example.comicsappmobile.ui.presentation.model.ChapterUiModel

class ChaptersRepository (
    private val chaptersApi: ChaptersApi
): BaseRepository() {

    // init {
    //     runBlocking{
    //         val data = getBookChapters(3)
    //         if (data is UiState.Success)
    //             Logger.debug("runBlocking", data.data.toString())
    //     }
//
    // }

    suspend fun getBookChapters(bookId: Int): UiState<List<ChapterUiModel>> {
        val response = safeApiCall { chaptersApi.getBookChapters(bookId) }


        return when (response) {
            is StateResponseDto.Success -> {
                val data = (response.data as? List<ChapterDto>) ?: emptyList()

                UiState.Success(data = ChapterMapper.mapList(data))
            }

            is StateResponseDto.Error -> {
                val data = (response.data as? ErrorDataDto)
                UiState.Error(
                    data = null,
                    message = data?.message ?: "ChaptersRepository -> getBookChapters",
                    typeError = data?.typeError ?: "ChaptersRepository -> getBookChapters",
                    statusCode = data?.statusCode ?: -1,
                )
            }
        }
    }
}