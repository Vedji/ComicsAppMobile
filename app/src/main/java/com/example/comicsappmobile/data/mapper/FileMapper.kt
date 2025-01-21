package com.example.comicsappmobile.data.mapper

import com.example.comicsappmobile.data.dto.entities.FileDto
import com.example.comicsappmobile.ui.presentation.model.FileUiModel

object FileMapper {
    fun toUiModel(fileDto: FileDto): FileUiModel {
        return FileUiModel(
            fileID = fileDto.fileID,
            filePath = fileDto.filePath,
            fileName = fileDto.fileName,
            fileType = fileDto.fileType,
            addedBy = fileDto.addedBy,
            uploadDate = fileDto.uploadDate
        )
    }

    fun toListUiModel(filesDto: List<FileDto>): List<FileUiModel>{
        return filesDto.map { toUiModel(it) }
    }
}