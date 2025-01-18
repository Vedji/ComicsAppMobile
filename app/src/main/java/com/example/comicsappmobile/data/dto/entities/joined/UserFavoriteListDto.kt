package com.example.comicsappmobile.data.dto.entities.joined

import com.example.comicsappmobile.data.dto.entities.BookDto
import com.google.gson.annotations.SerializedName


data class UserFavoriteListDto(
    @SerializedName("aboutBook") val aboutBook: BookDto,
    @SerializedName("chapterId") val chapterId: Int,
    @SerializedName("favoriteId") val favoriteId: Int,
    @SerializedName("uploadAt") val uploadAt: String
)