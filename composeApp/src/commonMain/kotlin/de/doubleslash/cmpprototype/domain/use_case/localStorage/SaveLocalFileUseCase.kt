package de.doubleslash.cmpprototype.domain.use_case.localStorage

import de.doubleslash.cmpprototype.data.datasource.local.localDB.dto.FileDTO
import de.doubleslash.cmpprototype.domain.model.file_mgmt.FileModel
import de.doubleslash.cmpprototype.domain.repository.localStorage.FileStorageRepository

class SaveLocalFileUseCase(
    private val fileStorageRepository: FileStorageRepository
) {
    suspend operator fun invoke(fileModel: FileModel) {
        val fileDTO = FileDTO().apply {
            this.baseName = fileModel.baseName
            this.extension = fileModel.extension
            this.path = fileModel.path.toString()
            this.baseTypeId = fileModel.baseTypeId
            this.fileContent = fileModel.fileContent!!
        }
        fileStorageRepository.addFile(fileDTO)
    }
}