package com.example.comicsappmobile.ui.presentation.model

import com.google.gson.annotations.SerializedName

class FavoriteUiModel(
    val bookId: Int = -1,
    val chapterId: Int = -1,
    val favoriteId: Int = -1,
    val userId: Int = -1,
    val uploadAt: String = "-",
) {
    override fun toString(): String {
        return "<FavoriteUiModel(userId = $userId, favoriteId = '$favoriteId')>"
    }
}