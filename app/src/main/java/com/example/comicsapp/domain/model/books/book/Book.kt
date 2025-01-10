package com.example.comicsapp.domain.model.books.book


data class Book (
    val bookID: Int,
    val bookAddedBy: Int,
    val bookTitle: String,
    val bookTitleImage: Int,
    val bookRating: Float,
    val bookDatePublication: String,
    val bookDescription: String,
    val bookISBN: String,
    val uploadDate: String,
)