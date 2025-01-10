package com.example.comicsapp.data.model.api.response

import com.google.gson.annotations.SerializedName

class ApiError (
    @SerializedName("message") val message: String,
    @SerializedName("statusCode") val statusCode: Int,
    @SerializedName("typeError") val typeError: String
)