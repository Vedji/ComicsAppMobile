package com.example.comicsappmobile.data.dto.response.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ErrorDataDto(
    @Json(name = "message") val message: String,
    @Json(name = "statusCode") val statusCode: Int,
    @Json(name = "typeError") val typeError: String
)