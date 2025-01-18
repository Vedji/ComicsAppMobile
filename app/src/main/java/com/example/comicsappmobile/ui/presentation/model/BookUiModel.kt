package com.example.comicsappmobile.ui.presentation.model

class BookUiModel (
    val bookId: Int = -1,
    val rusTitle: String = "Нет названия",
    val engTitle: String = "No title",
    val bookTitleImageId: Int = -1,
    val bookRating: Float = 0f,
    val bookDescription: String = "",
    val bookDatePublication: String = "",
    val bookISBN: String = "",
    val bookAddedBy: Int = -1,
    val uploadDate: String = "",

    var genres: List<String> = emptyList()
){
    override fun toString(): String {
        return "<BookUiModel(bookId = $bookId, title = $rusTitle)>"
    }
}