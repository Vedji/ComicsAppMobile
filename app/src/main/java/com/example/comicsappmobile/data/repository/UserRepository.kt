package com.example.comicsappmobile.data.repository

import com.example.comicsappmobile.data.api.UsersApi
import com.example.comicsappmobile.data.mapper.BookMapper
import com.example.comicsappmobile.data.mapper.CommentMapper
import com.example.comicsappmobile.data.mapper.UserMapper
import com.example.comicsappmobile.data.dto.StateResponseDto
import com.example.comicsappmobile.data.dto.entities.metadata.Pagination
import com.example.comicsappmobile.data.dto.entities.user.LoginUserDto
import com.example.comicsappmobile.data.dto.entities.user.UserComment
import com.example.comicsappmobile.data.dto.entities.user.UserDto
import com.example.comicsappmobile.data.dto.entities.joined.UserFavoriteListDto
import com.example.comicsappmobile.data.dto.entities.user.UserTokensDto
import com.example.comicsappmobile.data.dto.response.data.ErrorDataDto
import com.example.comicsappmobile.di.GlobalState
import com.example.comicsappmobile.di.RetrofitInstance
import com.example.comicsappmobile.di.SharedViewModel
import com.example.comicsappmobile.ui.presentation.model.BookUiModel
import com.example.comicsappmobile.utils.Logger
import com.example.comicsappmobile.ui.presentation.viewmodel.UiState
import com.example.comicsappmobile.ui.presentation.model.CommentUiModel
import com.example.comicsappmobile.ui.presentation.model.UserFavoriteUiModel
import com.example.comicsappmobile.ui.presentation.model.UserUiModel

//RetrofitInstance.accessToken = accessToken

class UserRepository (
    private val userApi: UsersApi,
    private val sharedViewModel: SharedViewModel,
    private val globalState: GlobalState
): BaseRepository() {

    // refreshUserAuthorization

    suspend fun responseRefreshUserAuthorization(token: String): UiState<UserUiModel> {
        Logger.debug("UserRepository", "Run")

        val test = safeApiCall { userApi.refreshUserAuthorization(token) }
        Logger.debug("UserRepository", "test = '$test'")

        return when(test) {
            is StateResponseDto.Success -> {
                Logger.debug("UserRepository", "data = ${test.data}")
                val data = (test.data as? LoginUserDto)
                val user = data?.aboutUser ?: UserDto.createDefaultUser(-4, "Not created")

                if (data?.tokens is UserTokensDto){
                    RetrofitInstance.accessToken = data.tokens.accessToken
                    RetrofitInstance.refreshToken = data.tokens.refreshToken
                }
                data?.let {
                    globalState.saveUserAccessToken(it.tokens.accessToken)
                    sharedViewModel.updateCurrentAuthorizingUser(it.aboutUser)
                    globalState.setAuthUser(it.aboutUser)

                }
                UiState.Success(data = UserMapper.map(user))
            }
            is StateResponseDto.Error -> {
                val data = (test.data as? ErrorDataDto)
                Logger.debug("return when(test)", "data = '${test}'")
                UiState.Error(
                    data = null,
                    message = data?.message ?: "BooksRepository -> getBook",
                    typeError = data?.typeError ?: "GenresRepository -> getBookGenres",
                    statusCode = -1
                )
            }
        }
    }

    suspend fun responseUserAuthorization(username: String, password: String, email: String = ""): UiState<UserUiModel> {
        Logger.debug("UserRepository", "Run")
        val test = safeApiCall { userApi.userAuthorization(
            username = username, password =  password, email = email) }
        Logger.debug("UserRepository", "test = '$test'")

        return when(test) {
            is StateResponseDto.Success -> {
                Logger.debug("UserRepository", "data = ${test.data}")
                val data = (test.data as? LoginUserDto)
                val user = data?.aboutUser ?: UserDto.createDefaultUser(-4, "Not created")
                if (data?.tokens is UserTokensDto){
                    RetrofitInstance.accessToken = data.tokens.accessToken
                    RetrofitInstance.refreshToken = data.tokens.refreshToken
                }
                data?.let {
                    sharedViewModel.updateCurrentAuthorizingUser(it.aboutUser)
                    globalState.setAuthUser(it.aboutUser)
                    globalState.saveUserAccessToken(it.tokens.accessToken ?: "")
                }
                Logger.debug("StateResponseDto.Success -> responseUserAuthorization", "globalStateUser = ${globalState.authUser.value}")
                UiState.Success(data = UserMapper.map(user))
            }
            is StateResponseDto.Error -> {
                val data = (test.data as? ErrorDataDto)
                Logger.debug("return when(test)", "data = '${test}'")
                UiState.Error(
                    data = null,
                    message = data?.message ?: "BooksRepository -> getBook",
                    typeError = data?.typeError ?: "GenresRepository -> getBookGenres",
                    statusCode = -1
                )
            }
        }
    }

    suspend fun responseFetchUserComments(limit: Int = 10, offset: Int = 0): UiState<List<CommentUiModel>> {
        Logger.debug("UserRepository", "Run")
        val accessToken = RetrofitInstance.accessToken
        val test = safeApiCall { userApi.getUserComments(accessToken ?: "") }
        Logger.debug("responseFetchUserComments", "test = '$test'")

        return when(test) {
            is StateResponseDto.Success -> {
                Logger.debug("responseFetchUserComments", "data = ${test.data}")
                val data = (test.data as? List<UserComment>)
                val user = sharedViewModel.currentAuthorizingUser.value
                Logger.debug("responseFetchUserComments", "data = ${test.data}")
                UiState.Success(data = CommentMapper.mapList(data ?: emptyList(), user))
            }
            is StateResponseDto.Error -> {
                val data = (test.data as? ErrorDataDto)
                Logger.debug("return when(test)", "data = '${test}'")
                UiState.Error(
                    data = null,
                    message = data?.message ?: "BooksRepository -> getBook",
                    typeError = data?.typeError ?: "GenresRepository -> getBookGenres",
                    statusCode = -1
                )
            }
        }
    }


    suspend fun responseFetchUserFavorites(limit: Int = 10, offset: Int = 0): UiState<List<Pair<BookUiModel, UserFavoriteUiModel>>> {
        Logger.debug("responseFetchUserFavorites", "Run")
        val test = safeApiCall { userApi.getAuthUserFavoriteList(RetrofitInstance.accessToken ?: "") }
        Logger.debug("responseFetchUserFavorites", "test = '$test'")

        return when(test) {
            is StateResponseDto.Success -> {
                val resp: MutableList<Pair<BookUiModel, UserFavoriteUiModel>> = mutableListOf()
                val responseData: List<UserFavoriteListDto>? = test.data as? List<UserFavoriteListDto> ?: null

                responseData?.let {
                    for (item in it){
                        resp += (Pair(
                            BookMapper.map(item.aboutBook),
                            UserFavoriteUiModel(
                                favoriteId = item.favoriteId,
                                chapterId = item.chapterId,
                                uploadAt = item.uploadAt
                            )))
                    }
                }
                Logger.debug("responseFetchUserFavorites", "debug = '$resp'")
                // val meta: Pagination? = if (test.data.metadata is Pagination) test.data.let { it.metadata } ?: null else null
                UiState.Success(data = resp, metadata = test.metadata as? Pagination)
            }
            is StateResponseDto.Error -> {
                val data = (test.data as? ErrorDataDto)
                Logger.debug("return when(test)", "data = '${test}'")
                UiState.Error(
                    data = null,
                    message = data?.message ?: "UserRepository -> responseFetchUserFavorites",
                    typeError = data?.typeError ?: "UserRepository -> responseFetchUserFavorites",
                    statusCode = -1
                )
            }
        }
    }





}