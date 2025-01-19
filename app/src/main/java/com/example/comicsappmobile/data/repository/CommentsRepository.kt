package com.example.comicsappmobile.data.repository

import com.example.comicsappmobile.data.api.CommentsApi
import com.example.comicsappmobile.data.dto.StateResponseDto
import com.example.comicsappmobile.data.dto.entities.CommentDto
import com.example.comicsappmobile.data.dto.entities.metadata.Pagination
import com.example.comicsappmobile.data.dto.response.data.ErrorDataDto
import com.example.comicsappmobile.data.mapper.CommentMapper
import com.example.comicsappmobile.di.RetrofitInstance
import com.example.comicsappmobile.utils.Logger
import com.example.comicsappmobile.ui.presentation.viewmodel.UiState
import com.example.comicsappmobile.ui.presentation.model.CommentUiModel


class CommentsRepository (
    private val commentsApi: CommentsApi
): BaseRepository() {

    suspend fun loadComments(bookId: Int, limit: Int = 10, offset: Int = 0): UiState<List<CommentUiModel>> {

        val test = safeApiCall { commentsApi.getBookComments(bookId, limit, offset) }
        // if (test is StateResponseDto.Success)
        //     Logger.debug("loadComments", test.data.toString())
        return when(test) {
            is StateResponseDto.Success -> {
                 Logger.debug("loadComments", "data = '${test.data}'")
                val data = (test.data as? List<CommentDto>) ?: emptyList()
                Logger.debug("loadComments", "Start")
                Logger.debug("loadComments", "bookId = ${bookId},response = ${data.toString()}")
                val comments = data.map { CommentDto.toUiModel(it) }  // ?.map ?: emptyList()
                Logger.debug("loadComments", comments.toString())
                val metadata = test.metadata as? Pagination
                Logger.debug("loadComments", "mt = '${metadata}'")
                UiState.Success(
                    data = comments,
                    metadata = metadata
                    )
            }
            is StateResponseDto.Error -> {
                val data = (test.data as? ErrorDataDto)
                Logger.debug("return when(test)", "data = '${test}'")
                UiState.Error(
                    data = emptyList(),
                    message = data?.message ?: "CommentsRepository -> loadComments",
                    typeError = data?.typeError ?: "CommentsRepository -> loadComments",
                    statusCode = -1
                )
            }
        }
    }

    suspend fun loadUserCommentForBook(bookId: Int = -1): UiState<CommentUiModel> {
        Logger.debug("CommentsRepository -> loadUserCommentForBook", "token = '${RetrofitInstance.accessToken}'")
        val token: String = RetrofitInstance.accessToken
            ?: return UiState.Error(typeError = "AuthError", message = "Пользователь не авторизированный")
        val response = safeApiCall { commentsApi.getUserCommentForComments(token = token, bookId = bookId) }
        Logger.debug("CommentsRepository -> loadUserCommentForBook", "response = '${response}'")
        return when(response) {
            is StateResponseDto.Success -> {
                val responseBody = response.data as? CommentDto
                Logger.debug("CommentsRepository -> loadUserCommentForBook", "responseBody = '${responseBody}'")
                responseBody?.let {
                    UiState.Success(data = CommentMapper.map(it))
                } ?: UiState.Error(data = null, message = "CommentsRepository -> loadUserCommentForBook responseBody is null")
            }
            is StateResponseDto.Error -> {
                val data = (response.data as? ErrorDataDto)
                UiState.Error(
                    data = null,
                    message = data?.message ?: "CommentsRepository -> loadComments",
                    typeError = data?.typeError ?: "CommentsRepository -> loadComments",
                    statusCode = -1
                )
            }
        }
    }


    suspend fun setUserCommentForBook(bookId: Int = -1, rating: Int = 1, comment: String = "No comment"): UiState<CommentUiModel> {
        val token: String = RetrofitInstance.accessToken
            ?: return UiState.Error(typeError = "AuthError", message = "Пользователь не авторизированный")
        val response = safeApiCall { commentsApi.setUserCommentForComments(
            token = token,
            bookId = bookId,
            rating = rating,
            comment = comment
        ) }
        Logger.debug("CommentsRepository -> setUserCommentForBook", "response = '${response}'")
        return when(response) {
            is StateResponseDto.Success -> {
                val responseBody = response.data as? CommentDto
                Logger.debug("CommentsRepository -> setUserCommentForBook", "responseBody = '${responseBody}'")
                responseBody?.let {
                    UiState.Success(data = CommentMapper.map(it))
                } ?: UiState.Error(data = null, message = "CommentsRepository -> setUserCommentForBook responseBody is null")
            }
            is StateResponseDto.Error -> {
                val data = (response.data as? ErrorDataDto)
                UiState.Error(
                    data = null,
                    message = data?.message ?: "CommentsRepository -> loadComments",
                    typeError = data?.typeError ?: "CommentsRepository -> loadComments",
                    statusCode = -1
                )
            }
        }
    }

    suspend fun deleteUserCommentForBook(commentId: Int = -1): UiState<CommentUiModel> {
        val token: String = RetrofitInstance.accessToken
            ?: return UiState.Error(typeError = "AuthError", message = "Пользователь не авторизированный")
        val response = safeApiCall { commentsApi.deleteUserCommentForComments(
            token = token,
            commentId = commentId
        ) }
        Logger.debug("CommentsRepository -> deleteUserCommentForBook", "response = '${response}'")
        return when(response) {
            is StateResponseDto.Success -> {
                val responseBody = response.data as? CommentDto
                Logger.debug("CommentsRepository -> deleteUserCommentForBook", "responseBody = '${responseBody}'")
                responseBody?.let {
                    UiState.Success(data = CommentMapper.map(it))
                } ?: UiState.Error(data = null, message = "CommentsRepository -> deleteUserCommentForBook responseBody is null")
            }
            is StateResponseDto.Error -> {
                val data = (response.data as? ErrorDataDto)
                UiState.Error(
                    data = null,
                    message = data?.message ?: "CommentsRepository -> loadComments",
                    typeError = data?.typeError ?: "CommentsRepository -> loadComments",
                    statusCode = -1
                )
            }
        }
    }

}
