package com.example.comicsapp.data.model.api.response

import com.google.gson.annotations.SerializedName

data class Pagination(
    @SerializedName("total")val total: Int,
    @SerializedName("offset")val offset: Int,
    @SerializedName("limit")val limit: Int,
    @SerializedName("hasNext")val hasNext: Boolean,
    @SerializedName("hasPrevious")val hasPrevious: Boolean,
    @SerializedName("currentPage")val currentPage: Int,
    @SerializedName("totalPages")val totalPages: Int
)