package com.example.comicsapp.data.model.api.books.genres

import com.example.comicsapp.domain.model.books.book.Genre
import com.google.gson.annotations.SerializedName

data class ApiGenre (
    @SerializedName("genreID") val genreID: Int,
    @SerializedName("genreName") val genreName: String
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true // Сравнение по ссылке
        if (other !is ApiGenre) return false // Проверка типа

        return genreID == other.genreID
    }

    override fun hashCode(): Int {
        var result = genreID
        result = 31 * result + genreName.hashCode()
        return result
    }

    fun toDomain(): Genre {
        return Genre(genreID, genreName)
    }
}