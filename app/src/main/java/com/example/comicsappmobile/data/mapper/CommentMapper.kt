package com.example.comicsappmobile.data.mapper

import com.example.comicsappmobile.data.dto.entities.CommentDto
import com.example.comicsappmobile.data.dto.entities.user.UserComment
import com.example.comicsappmobile.data.dto.entities.user.UserDto
import com.example.comicsappmobile.ui.presentation.model.CommentUiModel

object CommentMapper {

    fun map(apiModel: UserComment, aboutUser: UserDto): CommentUiModel {
        return CommentUiModel(
            bookId = apiModel.bookId,
            commentId = apiModel.commentId,
            rating = apiModel.rating,
            comment = apiModel.comment,
            uploadDate = apiModel.uploadDate,
            aboutUser = UserMapper.map(aboutUser)
        )
    }

    fun map(apiModel: CommentDto): CommentUiModel {
        return CommentUiModel(
            bookId = apiModel.bookId,
            commentId = apiModel.commentId,
            rating = apiModel.rating,
            comment = apiModel.comment,
            uploadDate = apiModel.uploadDate,
            aboutUser = UserMapper.map(apiModel.aboutUser)
        )
    }

    fun mapList(apiModels: List<CommentDto>): List<CommentUiModel> {
        return apiModels.map { map(it) }
    }

    fun mapList(apiModelList: List<UserComment>, aboutUser: UserDto): List<CommentUiModel> {
        return apiModelList.map { map(it, aboutUser) }
    }

}