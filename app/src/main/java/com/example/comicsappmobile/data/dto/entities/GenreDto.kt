package com.example.comicsappmobile.data.dto.entities

import com.google.gson.annotations.SerializedName


class GenreDto(
    @SerializedName("genreID")
    val genreId: Int = -1,

    @SerializedName("genreName")
    val genreName: String = "Нет названия"
) {
    override fun toString(): String {
        return "<GenreDto(genreId = '$genreId', genreName = $genreName)>"
    }
}