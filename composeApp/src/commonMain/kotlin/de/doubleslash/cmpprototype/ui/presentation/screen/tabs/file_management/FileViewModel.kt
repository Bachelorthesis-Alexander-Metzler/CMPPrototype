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
import de.doubleslash.cmpprototype.domain.model.file_mgmt.FolderModel
import de.doubleslash.cmpprototype.domain.use_case.checkNetworkStatus.GetConnectionStatusUseCase
import de.doubleslash.cmpprototype.domain.use_case.checkNetworkStatus.GetNetworkStatusUseCase
import de.doubleslash.cmpprototype.domain.use_case.cmis.LoadAllRemoteFilesUseCase
import de.doubleslash.cmpprototype.domain.use_case.cmis.LoadAllRemoteFoldersUseCase
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
    getConnectionStatusUseCase: GetConnectionStatusUseCase,
    getNetworkConnectionUseCase: GetNetworkStatusUseCase,
    // remote storage
    private val loadAllRemoteFilesUseCase: LoadAllRemoteFilesUseCase,
    private val loadAllRemoteFoldersUseCase: LoadAllRemoteFoldersUseCase,
    // credentials
    private val getCredentialsUseCase: GetCredentialsUseCase
) : ScreenModel {
    private val allFiles = mutableStateListOf<FileModel>()
    private val allFolders = mutableStateListOf<FolderModel>()

    // network status
    var networkStatus: StateFlow<NetworkConnection> = getNetworkConnectionUseCase.invoke()
    var isConnected: StateFlow<Boolean> = getConnectionStatusUseCase.invoke()

    // current state of loading cmis files
    var fetchRemoteFilesState by mutableStateOf<RequestCondition<List<FileModel>>>(RequestCondition.IdleCondition)
    var fetchRemoteFoldersState by mutableStateOf<RequestCondition<List<FolderModel>>>(RequestCondition.IdleCondition)

    fun refreshFilesAndFolders() {
        loadFiles()
        loadFolders()
    }

    private fun loadFiles() {
        allFiles.clear()
        // load files remotely
        screenModelScope.launch {
            // set state to loading
            fetchRemoteFilesState = RequestCondition.LoadingCondition

            val credentials = getCredentialsUseCase.invoke()
            // fetch remote files
            val requestCondition = loadAllRemoteFilesUseCase.invoke(
                serverAddress = credentials.serverAddress,
                username = credentials.username,
                password = credentials.password
            )

            when (requestCondition) {
                is RequestCondition.SuccessCondition -> {
                    val remoteObjects = requestCondition.data
                    allFiles.addAll(remoteObjects)
                    fetchRemoteFilesState = RequestCondition.SuccessCondition(data = remoteObjects)
                }

                else -> fetchRemoteFilesState = RequestCondition.ErrorCondition(errorMsg = "Error loading remote files")
            }
        }
        // load local files
        allFiles.addAll(loadAllLocalFilesUseCase.invoke())
    }

    private fun loadFolders() {
        allFolders.clear()
        // load folders remotely
        screenModelScope.launch {
            // set state to loading
            fetchRemoteFoldersState = RequestCondition.LoadingCondition

            val credentials = getCredentialsUseCase.invoke()
            // fetch remote folders
            val requestCondition = loadAllRemoteFoldersUseCase.invoke(
                serverAddress = credentials.serverAddress,
                username = credentials.username,
                password = credentials.password
            )

            when (requestCondition) {
                is RequestCondition.SuccessCondition -> {
                    val remoteFolders = requestCondition.data
                    allFolders.addAll(remoteFolders)
                    fetchRemoteFoldersState = RequestCondition.SuccessCondition(data = remoteFolders)
                }

                else -> fetchRemoteFoldersState = RequestCondition.ErrorCondition(errorMsg = "Error loading remote folders")
            }
        }
    }

    fun saveFileLocally(name: String, extension: String, path: String, fileContent: ByteArray) {
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

    fun getAllFolders(): List<FolderModel> {
        return allFolders
    }
}
