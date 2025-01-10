package com.example.comicsapp.data.model.api.response

import com.google.gson.annotations.SerializedName

class ApiBody<T> (
    @SerializedName("data") val data: T,
    @SerializedName("status") val status: String,
    @SerializedName("metadata") val metadata: Metadata? = null
)