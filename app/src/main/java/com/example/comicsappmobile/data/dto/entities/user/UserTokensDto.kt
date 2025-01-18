package com.example.comicsappmobile.data.dto.entities.user

import com.google.gson.annotations.SerializedName

data class UserTokensDto (
    @SerializedName("accessToken") val accessToken: String,
    @SerializedName("refreshToken") val refreshToken: String,
    @SerializedName("bcryptToken") val bcryptToken: String
)