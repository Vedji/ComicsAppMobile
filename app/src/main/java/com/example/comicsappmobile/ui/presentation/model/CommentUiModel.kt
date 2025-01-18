package com.example.comicsappmobile.ui.presentation.model

data class CommentUiModel(
    val bookId: Int,
    val commentId: Int,
    val rating: Float,  // TODO replace to Int
    val comment: String,
    val uploadDate: String,
    val aboutUser: UserUiModel,
){

    companion object{
        fun createDefaultCommentUiModel(
            bookId: Int = -1,
            commentId: Int = -1,
            rating: Float = 1f,  // TODO replace to Int
            comment: String = "Simple comment",
            uploadDate: String = "08:49:35 29-12-2024",
            aboutUser: UserUiModel = UserUiModel.createDefaultUser(-1, "Testing")
        ): CommentUiModel {
            return CommentUiModel(
                bookId = bookId,
                commentId = commentId,
                rating = rating,
                comment = comment,
                uploadDate = uploadDate,
                aboutUser = aboutUser,
            )
        }
    }

    override fun toString(): String {
        return "<CommentDto(commentId = '$commentId', username = '${aboutUser.username}')>"
    }

}