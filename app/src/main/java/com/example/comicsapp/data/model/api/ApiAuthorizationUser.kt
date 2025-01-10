package com.example.comicsapp.data.model.api

import com.google.gson.annotations.SerializedName

data class ApiAuthorizationUser (
    @SerializedName("aboutUser") val aboutUser: ApiUser,
    @SerializedName("accessToken") val accessToken: String,
    @SerializedName("refreshToken") val refreshToken: String
)