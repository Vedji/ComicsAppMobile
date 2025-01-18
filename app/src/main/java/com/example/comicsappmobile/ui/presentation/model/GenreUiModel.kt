package com.example.comicsappmobile.ui.presentation.model

class GenreUiModel (
    val genreId: Int,
    val genreName: String
){
    override fun toString(): String {
        return "<GenreUiModel(genreId = $genreId, genreName = $genreName)>"
    }
}