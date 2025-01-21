package com.example.comicsappmobile.data.api

import com.example.comicsappmobile.data.dto.entities.FileDto
import com.example.comicsappmobile.data.dto.response.ResponseDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part

interface FilesApi {

    @Multipart
    @PUT("/api/v2/files")
    suspend fun uploadFile(
        @Part uploadedFile: MultipartBody.Part,
        @Part("mimeType") mimeType: RequestBody,
        // @Part("fileName") titleId: RequestBody,
        // @Part("titleChaptersSequence") titleChaptersSequence: RequestBody
    ): Response<ResponseDto<FileDto>>
}