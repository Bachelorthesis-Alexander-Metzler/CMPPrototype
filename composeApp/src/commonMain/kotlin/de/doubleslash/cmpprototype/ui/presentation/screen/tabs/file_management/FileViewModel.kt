package de.doubleslash.cmpprototype.ui.presentation.screen.tabs.file_management

import androidx.compose.runtime.mutableStateListOf
import cafe.adriel.voyager.core.model.ScreenModel
import de.doubleslash.cmpprototype.domain.model.file_mgmt.FileEntity

class FileViewModel : ScreenModel {
    private val allFiles = mutableStateListOf<FileEntity>()

    fun addFile(fileName: String, extension: String, filePath: String) {
        val file = FileEntity(baseName = fileName, extension = extension, path = filePath)
        allFiles.add(file)
    }

    fun deleteFile(file: FileEntity) {
        allFiles.remove(file)
    }

    fun getAllFiles(): List<FileEntity> {
        return allFiles
    }
}