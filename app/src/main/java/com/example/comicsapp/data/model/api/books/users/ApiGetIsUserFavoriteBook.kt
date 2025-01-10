package com.example.comicsapp.data.model.api.books.users

import com.google.gson.annotations.SerializedName

data class ApiGetIsUserFavoriteBook(
    @SerializedName("isBookInUserFavoriteList")
    val isBookInUserFavoriteList: Boolean
)