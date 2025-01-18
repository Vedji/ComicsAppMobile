package com.example.comicsappmobile.data.dto.entities

import com.example.comicsappmobile.data.dto.entities.user.UserDto
import com.example.comicsappmobile.ui.presentation.model.CommentUiModel
import com.google.gson.annotations.SerializedName

data class CommentDto(
    @SerializedName("bookId") val bookId: Int,
    @SerializedName("commentId") val commentId: Int,
    @SerializedName("rating") val rating: Float,
    @SerializedName("comment") val comment: String,
    @SerializedName("uploadDate") val uploadDate: String,
    @SerializedName("aboutUser") val aboutUser: UserDto = UserDto.createDefaultUser(-1, ""),
){

    companion object {
        private const val DEFAULT_PERMISSION = 0

        fun toUiModel(commentDto: CommentDto): CommentUiModel {
            return CommentUiModel(
                bookId = commentDto.bookId,
                commentId = commentDto.commentId,
                rating = commentDto.rating,
                comment = commentDto.comment,
                uploadDate = commentDto.uploadDate,
                aboutUser = UserDto.toUiModel(commentDto.aboutUser)
            )
        }

        fun toDtoModel(userDto: CommentUiModel): CommentDto {
            return CommentDto(
                bookId = userDto.bookId,
                commentId = userDto.commentId,
                rating = userDto.rating,
                comment = userDto.comment,
                uploadDate = userDto.uploadDate,
                aboutUser = UserDto.toDtoModel(userDto.aboutUser)
            )
        }
    }

    override fun toString(): String {
        return "<CommentDto(commentId = '$commentId', username = '${aboutUser.username}')>"
    }

}