package com.example.comicsappmobile.data.dto.entities.user

import com.google.gson.annotations.SerializedName

class LoginUserDto (
    @SerializedName("aboutUser") val aboutUser: UserDto,
    @SerializedName("tokens") val tokens: UserTokensDto,
)