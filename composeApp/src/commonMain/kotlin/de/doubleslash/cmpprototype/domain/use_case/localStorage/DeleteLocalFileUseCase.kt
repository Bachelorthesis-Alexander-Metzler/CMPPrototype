package de.doubleslash.cmpprototype.domain.use_case.localStorage

import de.doubleslash.cmpprototype.data.datasource.local.localDB.dto.FileDTO
import de.doubleslash.cmpprototype.domain.model.file_mgmt.FileModel
import de.doubleslash.cmpprototype.domain.repository.localStorage.FileStorageRepository

class DeleteLocalFileUseCase(
    private val fileStorageRepository: FileStorageRepository
) {
    suspend operator fun invoke(fileModel: FileModel): Boolean {
        val fileDTO = FileDTO().apply {
            this._id = fileModel._id
            this.baseName = fileModel.baseName
            this.extension = fileModel.extension
            this.path = fileModel.path
        }
        return fileStorageRepository.deleteFile(fileDTO)
    }
}