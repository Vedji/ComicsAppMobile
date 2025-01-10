package com.example.comicsapp.data.model


data class ErrorDataModel(
    val message: String,
    val statusCode: Int,
    val typeError: String
)