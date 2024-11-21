package de.doubleslash.cmpprototype.ui.presentation.screen.tabs.file_management

import androidx.compose.runtime.mutableStateListOf
import cafe.adriel.voyager.core.model.ScreenModel
import de.doubleslash.cmpprototype.domain.model.file_mgmt.FileModel
import de.doubleslash.cmpprototype.domain.use_case.localStorage.DeleteFileUseCase
import de.doubleslash.cmpprototype.domain.use_case.localStorage.LoadAllFilesUseCase
import de.doubleslash.cmpprototype.domain.use_case.localStorage.SaveFileUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

class FileViewModel(
    private val saveFileUseCase: SaveFileUseCase,
    private val loadAllFilesUseCase: LoadAllFilesUseCase,
    private val deleteFileUseCase: DeleteFileUseCase
) : ScreenModel {
    private val allFiles = mutableStateListOf<FileModel>()

    init {
        loadFiles()
    }

    fun loadFiles() {
        allFiles.clear()
        allFiles.addAll(loadAllFilesUseCase.invoke())
    }


    fun saveFile(name: String, extension: String, path: String, fileContent: ByteArray) {
        CoroutineScope(Dispatchers.IO).launch {
            val fileModel = FileModel(
                baseName = name,
                extension = extension,
                path = path,
                fileContent = fileContent
            )
            saveFileUseCase.invoke(fileModel)
            loadFiles() // refresh files after saving
        }
    }
    fun deleteFile(file: FileModel) {
        CoroutineScope(Dispatchers.IO).launch {
            deleteFileUseCase.invoke(file)
            loadFiles() // load data again to update the UI
        }
    }

    fun getAllFiles(): List<FileModel> {
        return allFiles
    }
}