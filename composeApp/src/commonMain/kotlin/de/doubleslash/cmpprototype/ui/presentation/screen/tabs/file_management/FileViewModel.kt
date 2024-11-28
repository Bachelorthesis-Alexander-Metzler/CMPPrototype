package de.doubleslash.cmpprototype.ui.presentation.screen.tabs.file_management

import androidx.compose.runtime.mutableStateListOf
import cafe.adriel.voyager.core.model.ScreenModel
import com.plusmobileapps.konnectivity.NetworkConnection
import de.doubleslash.cmpprototype.domain.model.file_mgmt.FileModel
import de.doubleslash.cmpprototype.domain.use_case.checkNetworkStatus.GetConnectionStatusUseCase
import de.doubleslash.cmpprototype.domain.use_case.checkNetworkStatus.GetNetworkStatusUseCase
import de.doubleslash.cmpprototype.domain.use_case.localStorage.DeleteLocalFileUseCase
import de.doubleslash.cmpprototype.domain.use_case.localStorage.LoadAllFilesUseCase
import de.doubleslash.cmpprototype.domain.use_case.localStorage.SaveFileUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FileViewModel(
    private val saveFileUseCase: SaveFileUseCase,
    private val loadAllFilesUseCase: LoadAllFilesUseCase,
    private val deleteLocalFileUseCase: DeleteLocalFileUseCase,
    private val getConnectionStatusUseCase: GetConnectionStatusUseCase,
    private val getNetworkConnectionUseCase: GetNetworkStatusUseCase,
) : ScreenModel {
    private val allFiles = mutableStateListOf<FileModel>()

    // network status
    var networkStatus: StateFlow<NetworkConnection> = getNetworkConnectionUseCase.invoke()
    var isConnected: StateFlow<Boolean> = getConnectionStatusUseCase.invoke()

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
            deleteLocalFileUseCase.invoke(file)
            loadFiles() // load data again to update the UI
        }
    }

    fun getAllFiles(): List<FileModel> {
        return allFiles
    }
}