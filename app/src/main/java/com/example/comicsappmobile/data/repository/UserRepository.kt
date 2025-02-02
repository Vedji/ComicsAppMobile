package com.example.comicsappmobile.data.repository

import com.example.comicsappmobile.data.api.UsersApi
import com.example.comicsappmobile.data.dto.StateResponseDto
import com.example.comicsappmobile.data.dto.entities.BookDto
import com.example.comicsappmobile.data.dto.entities.joined.UserFavoriteListDto
import com.example.comicsappmobile.data.dto.entities.metadata.Pagination
import com.example.comicsappmobile.data.dto.entities.user.LoginUserDto
import com.example.comicsappmobile.data.dto.entities.user.UserComment
import com.example.comicsappmobile.data.dto.entities.user.UserDto
import com.example.comicsappmobile.data.dto.entities.user.UserTokensDto
import com.example.comicsappmobile.data.dto.response.data.ErrorDataDto
import com.example.comicsappmobile.data.mapper.BookMapper
import com.example.comicsappmobile.data.mapper.CommentMapper
import com.example.comicsappmobile.data.mapper.UserMapper
import com.example.comicsappmobile.di.GlobalState
import com.example.comicsappmobile.di.RetrofitInstance
import com.example.comicsappmobile.di.SharedViewModel
import com.example.comicsappmobile.ui.presentation.model.BookUiModel
import com.example.comicsappmobile.ui.presentation.model.CommentUiModel
import com.example.comicsappmobile.ui.presentation.model.UserFavoriteUiModel
import com.example.comicsappmobile.ui.presentation.model.UserUiModel
import com.example.comicsappmobile.ui.presentation.viewmodel.UiState
import com.example.comicsappmobile.utils.Logger

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

        return when (test) {
            is StateResponseDto.Success -> {
                Logger.debug("UserRepository", "data = ${test.data}")
                val data = (test.data as? LoginUserDto)
                val user = data?.aboutUser ?: UserDto.createDefaultUser(-4, "Not created")

                if (data?.tokens is UserTokensDto) {
                    RetrofitInstance.accessToken = data.tokens.accessToken
                    RetrofitInstance.refreshToken = data.tokens.refreshToken
                    globalState.saveUserAccessToken(data.tokens.accessToken)
                }
                sharedViewModel.updateCurrentAuthorizingUser(user)
                globalState.setAuthUser(user)
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

    suspend fun responseUserAuthorization(
        username: String,
        password: String,
        email: String = ""
    ): UiState<UserUiModel> {
        Logger.debug("UserRepository", "Run")
        val test = safeApiCall {
            userApi.userAuthorization(
                username = username, password = password, email = email
            )
        }
        Logger.debug("UserRepository", "test = '$test'")

        return when (test) {
            is StateResponseDto.Success -> {
                Logger.debug("UserRepository", "data = ${test.data}")
                val data = (test.data as? LoginUserDto)
                val user = data?.aboutUser ?: UserDto.createDefaultUser(-4, "Not created")
                if (data?.tokens is UserTokensDto) {
                    RetrofitInstance.accessToken = data.tokens.accessToken
                    RetrofitInstance.refreshToken = data.tokens.refreshToken
                    globalState.saveUserAccessToken(data.tokens.accessToken ?: "")
                }
                globalState.setAuthUser(user)
                data?.let {
                    sharedViewModel.updateCurrentAuthorizingUser(it.aboutUser)

                }
                Logger.debug(
                    "StateResponseDto.Success -> responseUserAuthorization",
                    "globalStateUser = ${globalState.authUser.value}"
                )
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

    suspend fun responseFetchUserComments(
        limit: Int = 10,
        offset: Int = 0
    ): UiState<List<CommentUiModel>> {
        Logger.debug("UserRepository", "Run")
        val accessToken = RetrofitInstance.accessToken
        val test = safeApiCall { userApi.getUserComments(accessToken ?: "") }
        Logger.debug("responseFetchUserComments", "test = '$test'")

        return when (test) {
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

    suspend fun responseFetchUserFavorites(
        limit: Int = 10,
        offset: Int = 0
    ): UiState<List<Pair<BookUiModel, UserFavoriteUiModel>>> {
        Logger.debug("responseFetchUserFavorites", "Run")
        val test =
            safeApiCall { userApi.getAuthUserFavoriteList(RetrofitInstance.accessToken ?: "") }
        Logger.debug("responseFetchUserFavorites", "test = '$test'")

        return when (test) {
            is StateResponseDto.Success -> {
                val resp: MutableList<Pair<BookUiModel, UserFavoriteUiModel>> = mutableListOf()
                val responseData: List<UserFavoriteListDto>? =
                    test.data as? List<UserFavoriteListDto> ?: null

                responseData?.let {
                    for (item in it) {
                        resp += (Pair(
                            BookMapper.map(item.aboutBook),
                            UserFavoriteUiModel(
                                favoriteId = item.favoriteId,
                                chapterId = item.chapterId,
                                uploadAt = item.uploadAt
                            )
                        ))
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

    suspend fun fetchBooksWhichUserAdded(userId: Int): UiState<List<BookUiModel>> {
        val response = safeApiCall { userApi.getBooksWhichUserAdded(userId = userId) }

        return when (response) {
            is StateResponseDto.Success -> {
                val data = (response.data as? List<BookDto>) ?: emptyList()
                val metadata = (response.metadata as? Pagination)
                UiState.Success(data = BookMapper.mapList(data), metadata = metadata)
            }

            is StateResponseDto.Error -> {
                val data = (response.data as? ErrorDataDto)
                Logger.debug("return when(test)", "data = '${response}'")
                UiState.Error(
                    data = null,
                    message = data?.message ?: "BooksRepository -> getBooks",
                    typeError = data?.typeError ?: "BooksRepository -> getBooks",
                    statusCode = -1
                )
            }
        }
    }

    suspend fun responseUpdateInfoAboutUser(
        newUserTitleImageId: Int,
        newUserDescription: String,
    ): UiState<UserUiModel> {
        Logger.debug("UserRepository -> responseUpdateInfoAboutUser", "Run")
        val accessToken = RetrofitInstance.accessToken
        val response = safeApiCall {
            userApi.uploadInfoAboutUser(
                token = accessToken ?: "",
                newUserTitleImageId = newUserTitleImageId,
                newUserDescription = newUserDescription
            )
        }
        Logger.debug("UserRepository -> responseUpdateInfoAboutUser", "response = '$response'")

        return when (response) {
            is StateResponseDto.Success -> {
                Logger.debug("UserRepository -> responseUpdateInfoAboutUser", "data = ${response.data}")
                val user = (response.data as? UserDto)?: UserDto.createDefaultUser(-4, "Not created")
                if (user.userId > 0){
                    globalState.setAuthUser(user)
                    UiState.Success(data = UserMapper.map(user))
                }else{ // TODO: Replace to error user
                    UiState.Success(data = UserMapper.map(globalState.authUser.value))
                }
            }
            is StateResponseDto.Error -> {
                val data = (response.data as? ErrorDataDto)
                Logger.debug("return when(responseUpdateInfoAboutUser)", "data = '${response}'")
                UiState.Error(
                    data = null,
                    message = data?.message ?: "UserRepository -> responseUpdateInfoAboutUser",
                    typeError = data?.typeError ?: "UserRepository -> responseUpdateInfoAboutUser",
                    statusCode = -1
                )
            }
        }
    }

    suspend fun responseUserRegistration(
        username: String,
        password: String,
        email: String
    ): UiState<UserUiModel> {
        Logger.debug("UserRepository -> responseUserRegistration", "Run")
        val test = safeApiCall {
            userApi.registrationUser(username = username, password = password, mail = email)
        }
        Logger.debug("UserRepository -> responseUserRegistration", "test = '$test'")

        return when (test) {
            is StateResponseDto.Success -> {
                Logger.debug("UserRepository -> responseUserRegistration", "data = ${test.data}")
                val data = (test.data as? LoginUserDto)
                val user = data?.aboutUser ?: UserDto.createDefaultUser(-4, "Not created")
                if (data?.tokens is UserTokensDto) {
                    RetrofitInstance.accessToken = data.tokens.accessToken
                    RetrofitInstance.refreshToken = data.tokens.refreshToken
                    globalState.saveUserAccessToken(data.tokens.accessToken ?: "")
                }
                globalState.setAuthUser(user)
                data?.let {
                    sharedViewModel.updateCurrentAuthorizingUser(it.aboutUser)
                }
                Logger.debug(
                    "StateResponseDto.Success -> responseUserRegistration",
                    "globalStateUser = ${globalState.authUser.value}"
                )
                UiState.Success(data = UserMapper.map(user))
            }

            is StateResponseDto.Error -> {
                val data = (test.data as? ErrorDataDto)
                Logger.debug("return when(test)", "data = '${test}'")
                UiState.Error(
                    data = null,
                    message = data?.message ?: "UserRepository -> responseUserRegistration",
                    typeError = data?.typeError ?: "UserRepository -> responseUserRegistration",
                    statusCode = -1
                )
            }
        }
    }
}