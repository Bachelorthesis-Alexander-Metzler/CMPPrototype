package de.doubleslash.cmpprototype.ui.presentation.screen.tabs.download

import androidx.compose.runtime.mutableStateListOf
import cafe.adriel.voyager.core.model.ScreenModel
import de.doubleslash.cmpprototype.domain.model.file_mgmt.FileModel
import de.doubleslash.cmpprototype.domain.use_case.localStorage.DeleteLocalFileUseCase
import de.doubleslash.cmpprototype.domain.use_case.localStorage.LoadAllLocalFilesUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

class DownloadViewModel(
    private val loadAllLocalFilesUseCase: LoadAllLocalFilesUseCase,
    private val deleteLocalFileUseCase: DeleteLocalFileUseCase
) : ScreenModel {
    private val allFiles = mutableStateListOf<FileModel>()


    init {
        loadFiles()
    }

    private fun loadFiles() {
        allFiles.clear()
        allFiles.addAll(loadAllLocalFilesUseCase.invoke())
    }

    fun deleteFileLocally(file: FileModel) {
        CoroutineScope(Dispatchers.IO).launch {
            deleteLocalFileUseCase.invoke(file)
            loadFiles() // load data again to update the UI
        }
    }

    fun getAllFiles(): List<FileModel> {
        return allFiles
    }
}