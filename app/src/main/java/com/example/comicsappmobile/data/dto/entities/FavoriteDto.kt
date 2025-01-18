package com.example.comicsappmobile.data.dto.entities

import com.google.gson.annotations.SerializedName


class FavoriteDto(
    @SerializedName("bookId")
    val bookId: Int = -1,

    @SerializedName("chapterId")
    val chapterId: Int = -1,

    @SerializedName("favoriteId")
    val favoriteId: Int = -1,

    @SerializedName("userId")
    val userId: Int = -1,

    @SerializedName("uploadAt")
    val uploadAt: String = "-",
) {
    override fun toString(): String {
        return "<FavoriteDto(userId = $userId, favoriteId = '$favoriteId')>"
    }
}