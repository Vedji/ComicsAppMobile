package com.example.comicsappmobile.data.repository

import com.example.comicsappmobile.data.api.GenresApi
import com.example.comicsappmobile.data.dto.StateResponseDto
import com.example.comicsappmobile.data.dto.entities.GenreDto
import com.example.comicsappmobile.data.dto.response.data.ErrorDataDto
import com.example.comicsappmobile.data.mapper.GenreMapper
import com.example.comicsappmobile.di.RetrofitInstance
import com.example.comicsappmobile.ui.presentation.model.GenreUiModel
import com.example.comicsappmobile.ui.presentation.viewmodel.UiState
import com.example.comicsappmobile.utils.Logger

class GenresRepository (
    private val genreApi: GenresApi
): BaseRepository() {

    suspend fun getBookGenres(bookId: Int): UiState<List<GenreUiModel>> {
        val repo = PagesRepository(RetrofitInstance.pagesApi)
        val test = safeApiCall { RetrofitInstance.genresApi.getBookGenres(bookId) }
        return when(test) {
            is StateResponseDto.Success -> {
                val data = ((test.data) as? List<GenreDto>) ?: emptyList()
                UiState.Success(data = GenreMapper.mapList(data) )
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

    suspend fun getAllGenres(): UiState<List<GenreUiModel>> {
        val test = safeApiCall { RetrofitInstance.genresApi.getAllGenres() }
        return when(test) {
            is StateResponseDto.Success -> {
                val data = (test.data as? List<GenreDto>)
                Logger.debug("GenresRepository -> getAllGenres", data.toString())
                UiState.Success(data = GenreMapper.mapList(data ?: emptyList()) )
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
