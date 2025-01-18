package com.example.comicsappmobile.data.dto.response.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class ListDto<T>(
    @Json(name = "items") val items: List<T>
){
    override fun toString(): String {
        val result = "<ListDto( items = [${items.joinToString(separator = ",") }}])>"
        return result
    }
}