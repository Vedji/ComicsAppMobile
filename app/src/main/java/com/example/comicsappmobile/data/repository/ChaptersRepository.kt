package com.example.comicsappmobile.data.repository

import com.example.comicsappmobile.data.api.ChaptersApi
import com.example.comicsappmobile.data.dto.StateResponseDto
import com.example.comicsappmobile.data.dto.entities.ChapterDto
import com.example.comicsappmobile.data.dto.response.data.ErrorDataDto
import com.example.comicsappmobile.data.mapper.ChapterMapper
import com.example.comicsappmobile.di.RetrofitInstance
import com.example.comicsappmobile.ui.presentation.model.ChapterUiModel
import com.example.comicsappmobile.ui.presentation.viewmodel.UiState
import com.example.comicsappmobile.utils.Logger

class ChaptersRepository (
    private val chaptersApi: ChaptersApi
): BaseRepository() {

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

    suspend fun updateChapter(
        bookId: Int,
        chapterId: Int?,
        chapterTitle: String,
        chapterPagesIds: List<Int>,
        chapterPagesImageIds: List<Int>,
    ): UiState<ChapterUiModel> {
        val token: String = RetrofitInstance.accessToken ?: ""
        if (token.isEmpty()) return UiState.Error(message = "No access token")

        val test = safeApiCall { chaptersApi.uploadChapters(
            token = token,
            bookId = bookId,
            chapterId = chapterId ?: 0,
            chapterTitle = chapterTitle,
            chapterPagesIds = chapterPagesIds,
            chapterPagesImageIds = chapterPagesImageIds
        ) }
        return when(test) {
            is StateResponseDto.Success -> {
                val data = (test.data as? ChapterDto) ?: ChapterDto()
                Logger.debug("ChaptersRepository -> updateChapter", "BookId = $bookId")
                Logger.debug("ChaptersRepository -> updateChapter", data.chapterTitle.toString() ?: "NoContent")
                UiState.Success(data = ChapterMapper.map(data))
            }
            is StateResponseDto.Error -> {
                val data = (test.data as? ErrorDataDto)
                Logger.debug("ChaptersRepository -> updateChapter", "errorData = '${test}'")
                UiState.Error(
                    data = null,
                    message = data?.message ?: "ChaptersRepository -> updateChapter",
                    typeError = data?.typeError ?: "ChaptersRepository -> updateChapter",
                    statusCode = -1
                )
            }
        }
    }

    suspend fun deleteChapter(
        bookId: Int,
        chapterId: Int,
    ): UiState<ChapterUiModel> {
        val token: String = RetrofitInstance.accessToken ?: ""
        if (token.isEmpty()) return UiState.Error(message = "No access token")

        val test = safeApiCall { chaptersApi.deleteChapter(
            token = token,
            bookId = bookId,
            chapterId = chapterId,
        ) }
        return when(test) {
            is StateResponseDto.Success -> {
                val data = (test.data as? ChapterDto) ?: ChapterDto()
                Logger.debug("ChaptersRepository -> deleteChapter", "BookId = $bookId")
                Logger.debug("ChaptersRepository -> deleteChapter", data.toString() ?: "NoContent")
                UiState.Success(data = ChapterMapper.map(data))
            }
            is StateResponseDto.Error -> {
                val data = (test.data as? ErrorDataDto)
                Logger.debug("ChaptersRepository -> deleteChapter", "errorData = '${test}'")
                UiState.Error(
                    data = null,
                    message = data?.message ?: "ChaptersRepository -> deleteChapter",
                    typeError = data?.typeError ?: "ChaptersRepository -> deleteChapter",
                    statusCode = -1
                )
            }
        }
    }


}