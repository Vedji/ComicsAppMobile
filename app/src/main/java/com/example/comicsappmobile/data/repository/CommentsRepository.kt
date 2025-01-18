package com.example.comicsappmobile.data.repository

import com.example.comicsappmobile.data.api.CommentsApi
import com.example.comicsappmobile.data.dto.StateResponseDto
import com.example.comicsappmobile.data.dto.entities.CommentDto
import com.example.comicsappmobile.data.dto.entities.metadata.Pagination
import com.example.comicsappmobile.data.dto.response.data.ErrorDataDto
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

}
