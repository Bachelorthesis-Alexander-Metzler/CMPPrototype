package de.doubleslash.cmpprototype.ui.presentation.camera

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import de.doubleslash.cmpprototype.domain.model.file_mgmt.FileModel
import de.doubleslash.cmpprototype.domain.use_case.localStorage.SaveFileUseCase
import kotlinx.coroutines.launch

class CameraViewModel(
    private val saveFileUseCase: SaveFileUseCase
) : ScreenModel {

    fun saveImage(name: String, extension: String, path: String, fileContent: ByteArray) {
        screenModelScope.launch {
            val fileModel = FileModel(
                baseName = name,
                extension = extension,
                path = path,
                fileContent = fileContent
            )
            saveFileUseCase.invoke(fileModel)
        }
    }
}