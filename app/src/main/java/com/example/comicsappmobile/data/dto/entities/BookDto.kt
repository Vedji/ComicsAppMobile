package com.example.comicsappmobile.data.dto.entities

import com.google.gson.annotations.SerializedName

class BookDto(
    @SerializedName("bookID")
    val bookId: Int = -1,

    @SerializedName("bookTitle")
    val rusTitle: String = "Нет названия",

    @SerializedName("bookTitleImage")
    val bookTitleImageId: Int = -1,

    @SerializedName("bookRating")
    val bookRating: Float = 0f,

    @SerializedName("bookDescription")
    val bookDescription: String = "",

    @SerializedName("bookDatePublication")
    val bookDatePublication: String = "",

    @SerializedName("bookISBN")
    val bookISBN: String = "",

    @SerializedName("bookAddedBy")
    val bookAddedBy: Int = -1,

    @SerializedName("uploadDate")
    val uploadDate: String = ""

) {
    override fun toString(): String {
        return "<BookDto(bookId = '$bookId', age = $rusTitle)>"
    }
}