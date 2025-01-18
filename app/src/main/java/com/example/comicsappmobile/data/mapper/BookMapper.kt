package com.example.comicsappmobile.data.mapper

import com.example.comicsappmobile.data.dto.entities.BookDto
import com.example.comicsappmobile.ui.presentation.model.BookUiModel

object BookMapper{

    fun map(apiModel: BookDto): BookUiModel {
        return BookUiModel(
            bookId = apiModel.bookId,
            rusTitle = apiModel.rusTitle,
            engTitle = "No eng title",
            bookTitleImageId = apiModel.bookTitleImageId,
            bookRating = apiModel.bookRating,
            bookDescription = apiModel.bookDescription,
            bookDatePublication = apiModel.bookDatePublication,
            bookISBN = apiModel.bookISBN,
            bookAddedBy = apiModel.bookAddedBy,
            uploadDate = apiModel.uploadDate,
            genres = emptyList()
        )
    }

    fun mapList(apiModels: List<BookDto>): List<BookUiModel> {
        return apiModels.map { map(it) }
    }
}