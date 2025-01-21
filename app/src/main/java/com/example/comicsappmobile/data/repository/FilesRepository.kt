package com.example.comicsappmobile.data.repository

import android.content.Context
import android.net.Uri
import com.example.comicsappmobile.data.api.FilesApi
import com.example.comicsappmobile.data.dto.entities.FileDto
import com.example.comicsappmobile.data.mapper.FileMapper
import com.example.comicsappmobile.di.GlobalState
import com.example.comicsappmobile.ui.presentation.model.FileUiModel
import com.example.comicsappmobile.ui.presentation.viewmodel.UiState
import com.example.comicsappmobile.utils.Logger
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class FilesRepository(
    private val filesApi: FilesApi,
    private val globalState: GlobalState
): BaseRepository() {

    suspend fun uploadFile(context: Context, fileUri: Uri): UiState<FileUiModel> {
        Logger.debug("uploadBook", "Run")
        val inputStream: InputStream? = context.contentResolver.openInputStream(fileUri)
        val tempFile = File(context.cacheDir, "temp_upload_file")
        val mimeType: String? = context.contentResolver.getType(fileUri)
        inputStream?.use { input ->
            FileOutputStream(tempFile).use { output ->
                input.copyTo(output)
            }
        } ?: run {
            Logger.debug("uploadBook", "Failed to open input stream from URI")
            return UiState.Error(data = null, message = "Failed to open input stream from URI")
        }
        val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), tempFile)
        val filePart = MultipartBody.Part.createFormData("uploadedFile", tempFile.name, requestFile)

        val mimeTypePart = "$mimeType".toRequestBody("text/plain".toMediaTypeOrNull())

        return try {
            val response = filesApi.uploadFile(
                uploadedFile = filePart,
                mimeType = mimeTypePart
            )
            if (response.isSuccessful) {
                Logger.debug("uploadBook", "Upload successful")
                val fileDto: FileDto = response.body()?.data as? FileDto ?: FileDto()
                Logger.debug("uploadBook", "Upload fileDto =' ${fileDto}'")
                UiState.Success(data = FileMapper.toUiModel(fileDto))
            } else {
                UiState.Error(data = null, message = "Upload failed")
            }
        } catch (e: Exception) {
            Logger.debug("uploadBook", "Request failed: ${e.message}")
            UiState.Error(data = null, message = e.message)
        }

    }
}