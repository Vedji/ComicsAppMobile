package com.example.comicsappmobile.ui.presentation.model



class FileUiModel(
    val fileID: Int = -1,
    val filePath: String = "",
    val fileName: String = "",
    val fileType: String = "",
    val addedBy: Int = -1,
    val uploadDate: String = "",
    ) {
    override fun toString(): String {
        return "<FileUiModel(fileID = $fileID, fileName = '$fileName', fileType = '$fileType')>"
    }
}