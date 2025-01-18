package com.example.comicsappmobile.data.dto.response

import com.example.comicsappmobile.data.dto.entities.metadata.Pagination
import com.google.gson.annotations.SerializedName


class ResponseDto<T> (
    @SerializedName("status") val status: String = "success",
    @SerializedName("data") val data: T,
    @SerializedName("metadata") val metadata: Pagination? = null
)

