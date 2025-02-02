package com.example.comicsappmobile.data.dto.entities.user

import com.example.comicsappmobile.ui.presentation.model.UserUiModel
import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("userID") val userId: Int,
    @SerializedName("username") val username: String,
    @SerializedName("userTitleImage") val userTitleImage: Int,


    @SerializedName("email") val email: String? = null,
    @SerializedName("userDescription") val userDescription: String? = null,
    @SerializedName("permission") val permission: Int = 0,
    @SerializedName("createdAt") val createdAt: String? = null,
){

    companion object {
        private const val DEFAULT_PERMISSION = 0

        // Фабричный метод
        fun createDefaultUser(userId: Int, username: String): UserDto {
            return UserDto(
                userId = userId,
                username = username,
                userTitleImage = -1, // Замените на значение по умолчанию
                email = null,
                userDescription = null,
                permission = DEFAULT_PERMISSION,
                createdAt = "1970-01-01T00:00:00Z" // Значение по умолчанию
            )
        }

        fun toUiModel(userDto: UserDto): UserUiModel {
            return UserUiModel(
                userId = userDto.userId,
                username = userDto.username,
                userTitleImage = userDto.userTitleImage,
                email = userDto.email,
                userDescription = userDto.userDescription,
                permission = userDto.permission,
                createdAt = userDto.createdAt
            )
        }

        fun toDtoModel(userDto: UserUiModel): UserDto {
            return UserDto(
                userId = userDto.userId,
                username = userDto.username,
                userTitleImage = userDto.userTitleImage,
                email = userDto.email,
                userDescription = userDto.userDescription,
                permission = userDto.permission ?: 0,
                createdAt = userDto.createdAt
            )
        }

    }

    override fun toString(): String {
        return "<UserDto(userId = '$userId', username = '$username')>"
    }

}