package com.example.comicsapp.data.model.api

import com.google.gson.annotations.SerializedName

data class ApiUser (
    @SerializedName("userID") val userID: Int,
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
    @SerializedName("userTitleImage") val userTitleImage: Int,
    @SerializedName("userDescription") val userDescription: String,
    @SerializedName("permission") val permission: Int,
    @SerializedName("createdAt") val createdAt: String
)