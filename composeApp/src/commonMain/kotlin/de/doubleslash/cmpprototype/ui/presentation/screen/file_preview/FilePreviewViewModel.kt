package de.doubleslash.cmpprototype.ui.presentation.screen.file_preview

import cafe.adriel.voyager.core.model.ScreenModel
import de.doubleslash.cmpprototype.domain.model.file_mgmt.FileModel
import de.doubleslash.cmpprototype.domain.use_case.localStorage.LoadLocalFileUseCase
import org.mongodb.kbson.ObjectId

class FilePreviewViewModel(
    private val loadLocalFileUseCase: LoadLocalFileUseCase
) : ScreenModel {

    fun loadLocalFile(id: ObjectId): FileModel {
        return loadLocalFileUseCase.invoke(id)
    }
}