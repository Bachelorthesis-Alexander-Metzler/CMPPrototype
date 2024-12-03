package de.doubleslash.cmpprototype.ui.presentation.screen.camera

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import de.doubleslash.cmpprototype.domain.model.file_mgmt.FileModel
import de.doubleslash.cmpprototype.domain.use_case.localStorage.SaveLocalFileUseCase
import kotlinx.coroutines.launch

class CameraViewModel(
    private val saveLocalFileUseCase: SaveLocalFileUseCase
) : ScreenModel {

    fun saveImage(name: String, extension: String, path: String, fileContent: ByteArray) {
        screenModelScope.launch {
            val fileModel = FileModel(
                baseName = name,
                extension = extension,
                path = path,
                isRemoteFile = false,
                baseTypeId = "cmis:document",
                fileContent = fileContent
            )
            saveLocalFileUseCase.invoke(fileModel)
        }
    }
}