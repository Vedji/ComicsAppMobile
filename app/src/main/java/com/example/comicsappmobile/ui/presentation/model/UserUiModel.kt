package com.example.comicsappmobile.ui.presentation.model

data class UserUiModel(
    val userId: Int,
    val username: String,
    val userTitleImage: Int,

    val email: String?,
    val userDescription: String?,
    val permission: Int?,
    val createdAt: String?
) {
    override fun toString(): String {
        return "<UserUiModel(userId = '$userId', username = '$username')>"
    }
    companion object {
        const val DEFAULT_PERMISSION = 0
        // Фабричный метод
        fun createDefaultUser(userId: Int, username: String): UserUiModel {
            return UserUiModel(
                userId = userId,
                username = username,
                userTitleImage = -1, // Замените на значение по умолчанию
                email = null,
                userDescription = null,
                permission = DEFAULT_PERMISSION,
                createdAt = "1970-01-01T00:00:00Z" // Значение по умолчанию
            )
        }
    }
}