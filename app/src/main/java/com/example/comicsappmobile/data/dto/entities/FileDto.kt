package com.example.comicsappmobile.data.dto.entities

import com.google.gson.annotations.SerializedName

class FileDto(
    @SerializedName("fileID")
    val fileID: Int = -1,

    @SerializedName("filePath")
    val filePath: String = "",

    @SerializedName("fileName")
    val fileName: String = "",

    @SerializedName("fileType")
    val fileType: String = "",

    @SerializedName("addedBy")
    val addedBy: Int = -1,

    @SerializedName("uploadDate")
    val uploadDate: String = "",

) {
    override fun toString(): String {
        return "<FileDto(fileID = $fileID, fileName = '$fileName', fileType = '$fileType')>"
    }
}