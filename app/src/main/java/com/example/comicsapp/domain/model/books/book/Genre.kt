package com.example.comicsapp.domain.model.books.book

import com.example.comicsapp.data.model.api.books.genres.ApiGenre

class Genre(
    private var _genreID: Int,
    private var _genreName: String
){
    var genreID: Int
        get() = _genreID
        set(value) {
            _genreID = value
        }

    var genreName: String
        get() = _genreName
        set(value) {
            _genreName = value
        }

    override fun equals(other: Any?): Boolean {
        if (other !is Genre) return false // Проверка типа
        return this.genreID == other.genreID
    }

    fun toApi(): ApiGenre {
        return ApiGenre(_genreID, _genreName)
    }

    override fun hashCode(): Int {
        var result = genreID
        result = 31 * result + genreName.hashCode()
        return result
    }

}