package de.doubleslash.cmpprototype.ui.presentation.screen.tabs.file_management

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.plusmobileapps.konnectivity.NetworkConnection
import de.doubleslash.cmpprototype.domain.model.auth.RequestCondition
import de.doubleslash.cmpprototype.domain.model.file_mgmt.FileModel
import de.doubleslash.cmpprototype.domain.use_case.checkNetworkStatus.GetConnectionStatusUseCase
import de.doubleslash.cmpprototype.domain.use_case.checkNetworkStatus.GetNetworkStatusUseCase
import de.doubleslash.cmpprototype.domain.use_case.cmis.LoadAllRemoteObjectsUseCase
import de.doubleslash.cmpprototype.domain.use_case.getSessionData.GetCredentialsUseCase
import de.doubleslash.cmpprototype.domain.use_case.localStorage.DeleteLocalFileUseCase
import de.doubleslash.cmpprototype.domain.use_case.localStorage.LoadAllLocalFilesUseCase
import de.doubleslash.cmpprototype.domain.use_case.localStorage.SaveLocalFileUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FileViewModel(
    // local storage
    private val saveLocalFileUseCase: SaveLocalFileUseCase,
    private val loadAllLocalFilesUseCase: LoadAllLocalFilesUseCase,
    private val deleteLocalFileUseCase: DeleteLocalFileUseCase,
    // network
    private val getConnectionStatusUseCase: GetConnectionStatusUseCase,
    private val getNetworkConnectionUseCase: GetNetworkStatusUseCase,
    // remote storage
    private val loadAllRemoteObjectsUseCase: LoadAllRemoteObjectsUseCase,
    // credentials
    private val getCredentialsUseCase: GetCredentialsUseCase
) : ScreenModel {
    private val allFiles = mutableStateListOf<FileModel>()

    // network status
    var networkStatus: StateFlow<NetworkConnection> = getNetworkConnectionUseCase.invoke()
    var isConnected: StateFlow<Boolean> = getConnectionStatusUseCase.invoke()

    // current state of loading cmis files
    var cmisState by mutableStateOf<RequestCondition<List<FileModel>>>(RequestCondition.IdleCondition)

    fun refreshFiles() {
        loadFiles()
    }

    private fun loadFiles() {
        allFiles.clear()
        // load files remotely
        screenModelScope.launch {
            // set state to loading
            cmisState = RequestCondition.LoadingCondition

            val credentials = getCredentialsUseCase.invoke()
            // fetch remote files
            val requestCondition = loadAllRemoteObjectsUseCase.invoke(
                serverAddress = credentials.serverAddress,
                username = credentials.username,
                password = credentials.password
            )

            when (requestCondition) {
                is RequestCondition.SuccessCondition -> {
                    val remoteObjects = requestCondition.data
                    allFiles.addAll(remoteObjects)
                    cmisState = RequestCondition.SuccessCondition(data = remoteObjects)
                }

                else -> cmisState = RequestCondition.ErrorCondition(errorMsg = "Error loading remote files")

            }
        }
        // load local files
        allFiles.addAll(loadAllLocalFilesUseCase.invoke())
    }


    fun saveFile(name: String, extension: String, path: String, fileContent: ByteArray) {
        CoroutineScope(Dispatchers.IO).launch {
            val fileModel = FileModel(
                baseName = name,
                extension = extension,
                path = path,
                isRemoteFile = false,
                baseTypeId = "cmis:document",
                fileContent = fileContent
            )
            saveLocalFileUseCase.invoke(fileModel)
            loadFiles() // refresh files after saving
        }
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