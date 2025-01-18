package com.example.comicsappmobile.data.dto

import com.example.comicsappmobile.data.dto.response.data.ErrorDataDto
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

sealed class StateResponseDto<out T> {
    @JsonClass(generateAdapter = true)
    data class Success<T>(
        @Json(name = "status") val status: String = "success",
        @Json(name = "data") val data: T,
        @Json(name = "metadata") val metadata: Any? = null
    ) : StateResponseDto<T>(){
        override fun toString(): String {
            return "<StateResponseDto.Success(status = '$status', data = $data, metadata = $metadata)>"
        }
    }

    @JsonClass(generateAdapter = true)
    data class Error(
        @Json(name = "status") val status: String = "error",
        @Json(name = "data") val data: ErrorDataDto,
        @Json(name = "metadata") val metadata: Any? = null
    ) : StateResponseDto<Nothing>(){
        override fun toString(): String {
            return "<StateResponseDto.Error(status = '$status', data = {$data}, metadata = {$metadata})>"
        }
    }
}

