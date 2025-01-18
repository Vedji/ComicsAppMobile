package com.example.comicsappmobile.data.mapper

import com.example.comicsappmobile.data.dto.entities.user.UserDto
import com.example.comicsappmobile.ui.presentation.model.UserUiModel

object UserMapper {
    fun map(apiModel: UserDto): UserUiModel {
        return UserUiModel(
            userId = apiModel.userId,
            username = apiModel.username,
            userTitleImage = apiModel.userTitleImage,
            email = apiModel.email,
            userDescription = apiModel.userDescription,
            permission = apiModel.permission,
            createdAt = apiModel.createdAt
        )
    }

    fun mapList(apiModels: List<UserDto>): List<UserUiModel> {
        return apiModels.map { map(it) }
    }
}