package com.example.comicsappmobile.data.dto.entities.metadata

import com.google.gson.annotations.SerializedName

data class Pagination(
    @SerializedName("hasPrevious") val hasPrevious: Boolean = false,
    @SerializedName("hasNext") val hasNext: Boolean = false,
    @SerializedName("limit") val limit: Int = 0,
    @SerializedName("offset") val offset: Int = 0,
    @SerializedName("total") val total: Int = 0,
    @SerializedName("totalPages") val totalPages: Int = 0,

    // Pages
    @SerializedName("lastItemId") val lastItemId: Int? = null,
    @SerializedName("nextItemId") val nextItemId: Int? = null,
)