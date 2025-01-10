package com.example.comicsapp.data.model.api.response

import com.google.gson.annotations.SerializedName


data class Metadata(
    @SerializedName("pagination") val pagination: Pagination
)