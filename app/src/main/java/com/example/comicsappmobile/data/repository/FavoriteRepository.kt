package com.example.comicsappmobile.data.repository

import com.example.comicsappmobile.data.api.FavoriteApi
import com.example.comicsappmobile.data.dto.StateResponseDto
import com.example.comicsappmobile.data.dto.entities.FavoriteDto
import com.example.comicsappmobile.data.dto.response.data.ErrorDataDto
import com.example.comicsappmobile.data.mapper.FavoriteMapper
import com.example.comicsappmobile.di.GlobalState
import com.example.comicsappmobile.di.RetrofitInstance
import com.example.comicsappmobile.ui.presentation.model.FavoriteUiModel
import com.example.comicsappmobile.ui.presentation.viewmodel.UiState
import com.example.comicsappmobile.utils.Logger

class FavoriteRepository(
    private val favoriteApi: FavoriteApi,
    private val globalState: GlobalState
): BaseRepository() {

    suspend fun fetchBookInFavorite(bookId: Int): UiState<FavoriteUiModel> {

        val accessToken = RetrofitInstance.accessToken
        if (accessToken == null)
            return UiState.Error(data = null, message = "access tokes has null")
        val test = safeApiCall { favoriteApi.getBookInFavorite(
            token = accessToken,
            bookId = bookId
        ) }
        return when(test) {
            is StateResponseDto.Success -> {
                val responseBode: FavoriteDto? = test.data as? FavoriteDto
                Logger.debug("test Favorite", "responseBode = $responseBode")
                // UiState.Success(data = GenreMapper.mapList(data) )
                responseBode?.let {
                    UiState.Success(data = FavoriteMapper.toUiModel(it))

                } ?: UiState.Error(
                    data = null,
                    message = "FavoriteRepository -> fetchBookInFavorite",
                    typeError = "In Let"
                    )

            }
            is StateResponseDto.Error -> {
                val data = (test.data as? ErrorDataDto)
                Logger.debug("return when(test)", "data = '${test}'")
                UiState.Error(
                    data = null,
                    message = data?.message ?: "GenresRepository -> getBookGenres",
                    typeError = data?.typeError ?: "GenresRepository -> getBookGenres",
                    statusCode = -1
                )
            }
        }
    }



    suspend fun setBookFavorite(bookId: Int, chapterId: Int = 0): UiState<FavoriteUiModel> {

        val accessToken = RetrofitInstance.accessToken
        if (accessToken == null)
            return UiState.Error(data = null, message = "access tokes has null")
        val test = safeApiCall { favoriteApi.setBookInFavorite(
            token = accessToken,
            bookId = bookId,
            chapterId = chapterId
        ) }
        return when(test) {
            is StateResponseDto.Success -> {
                val responseBode: FavoriteDto? = test.data as? FavoriteDto
                Logger.debug("test Favorite", "responseBode = $responseBode")
                // UiState.Success(data = GenreMapper.mapList(data) )
                responseBode?.let {
                    UiState.Success(data = FavoriteMapper.toUiModel(it))

                } ?: UiState.Error(
                    data = null,
                    message = "FavoriteRepository -> setBookFavorite",
                    typeError = "In Let"
                )

            }
            is StateResponseDto.Error -> {
                val data = (test.data as? ErrorDataDto)
                Logger.debug("return when(test)", "data = '${test}'")
                UiState.Error(
                    data = null,
                    message = data?.message ?: "FavoriteRepository -> setBookFavorite",
                    typeError = data?.typeError ?: "FavoriteRepository -> setBookFavorite",
                    statusCode = -1
                )
            }
        }
    }

    suspend fun deleteBookFromFavorite(favoriteId: Int = -1): UiState<FavoriteUiModel> {
        if (favoriteId < 0)
            return UiState.Error(message = "favoriteId is not ${favoriteId} < 0")
        val accessToken = RetrofitInstance.accessToken
        if (accessToken == null)
            return UiState.Error(data = null, message = "access tokes has null")
        val test = safeApiCall { favoriteApi.removeBookFromFavorite(
            token = accessToken,
            favoriteId = favoriteId
        ) }
        return when(test) {
            is StateResponseDto.Success -> {
                val responseBode: FavoriteDto? = test.data as? FavoriteDto
                Logger.debug("test Favorite", "responseBode = $responseBode")
                // UiState.Success(data = GenreMapper.mapList(data) )
                responseBode?.let {
                    UiState.Success(data = FavoriteMapper.toUiModel(it))

                } ?: UiState.Error(
                    data = null,
                    message = "FavoriteRepository -> setBookFavorite",
                    typeError = "In Let"
                )

            }
            is StateResponseDto.Error -> {
                val data = (test.data as? ErrorDataDto)
                Logger.debug("return when(test)", "data = '${test}'")
                UiState.Error(
                    data = null,
                    message = data?.message ?: "FavoriteRepository -> setBookFavorite",
                    typeError = data?.typeError ?: "FavoriteRepository -> setBookFavorite",
                    statusCode = -1
                )
            }
        }
    }


}